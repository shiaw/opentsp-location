package com.navinfo.opentsp.platform.dp.core.common.hash;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 构建Hash
 * @author HK
 *
 */
public final class KetamaNodeLocator {
	private static final Logger log = LoggerFactory.getLogger(KetamaNodeLocator.class);
	//
	private TreeMap<Long, Node> ketamaNodes;
	//
	private Hash hash;
	//
	private int virtualNode = 160;
	
	private ReadWriteLock lock = new ReentrantReadWriteLock();
	//实际节点与虚拟节点的映射表,辅助节点删除时,对ketamaNodes的回收
	private Map<Long, List<Long>> reverseKetamaNodes;

	
	public KetamaNodeLocator(Hash hash, int virtualNode) {
		Lock write = lock.writeLock();
		write.lock();
		this.ketamaNodes = new TreeMap<Long, Node>();
		this.reverseKetamaNodes = new ConcurrentHashMap<Long, List<Long>>();
		this.hash = hash;
		this.virtualNode = virtualNode;
		write.unlock();
	}

	
	public KetamaNodeLocator(List<Node> nodes, Hash alg, int nodeCopies) {
		Lock write = lock.writeLock();
		write.lock();
		try {
			this.reverseKetamaNodes = new ConcurrentHashMap<Long, List<Long>>();
			this.hash = alg;
			this.ketamaNodes = new TreeMap<Long, Node>();
			this.virtualNode = nodeCopies;

			for (Node node : nodes) {
				for (int i = 0; i < this.virtualNode / 4; i++) {
					byte[] digest = hash.computeMd5(String.valueOf(node.getNodeCode()) + i);
					for (int h = 0; h < 4; h++) {
						long m = this.hash.hash(digest, h);
						this.ketamaNodes.put(m, node);
					}
				}
				
				//?
				List<Long> virtualNodes = this.reverseKetamaNodes.get(node.getNodeCode());
				if(virtualNodes == null){
					virtualNodes = new ArrayList<Long>();
				}
				virtualNodes.add(node.getNodeCode());
				this.reverseKetamaNodes.put(node.getNodeCode(), virtualNodes);
				
			}			
		} catch (Exception e) {
		}finally{
			write.unlock();
		}

	}

	
	
	public void addNode(Node node) {
		Lock write = lock.writeLock();
		write.lock();
		try {
			if(this.reverseKetamaNodes.containsKey(node.getNodeCode())){
				log.info("节点[ "+node.getNodeCode()+" ]已加入HASH表");
				return;
			}
			for (int i = 0; i < this.virtualNode / 4; i++) {
				byte[] digest = this.hash.computeMd5(String.valueOf(node.getNodeCode())+i);
				for (int h = 0; h < 4; h++) {
					long m = this.hash.hash(digest, h);
					this.ketamaNodes.put(m, node);
					
					
					List<Long> virtualNodes = this.reverseKetamaNodes.get(node.getNodeCode());
					if(virtualNodes == null){
						virtualNodes = new ArrayList<Long>();
					}
					virtualNodes.add(m);
					this.reverseKetamaNodes.put(node.getNodeCode(), virtualNodes);
					
				}
			}			
		} catch (Exception e) {
		}finally{
			write.unlock();
		}

	}

	public int getActualNodeNumber(){
		return this.reverseKetamaNodes.size();
	}
	public int getVirtualNodeNumber(){
		return this.ketamaNodes.size();
	}
	public Set<Long> getActualNodeCodes(){
		return this.reverseKetamaNodes.keySet();
	}
	
	public void delNode(long nodeCode) {
		Lock write = lock.writeLock();
		write.lock();
		try {
			List<Long> virtualNodes = this.reverseKetamaNodes.get(nodeCode);
			if(virtualNodes != null){
				this.reverseKetamaNodes.remove(nodeCode);
				for (Long virtualNode : virtualNodes) {
					this.ketamaNodes.remove(virtualNode);
				}
			}			
		} catch (Exception e) {
		}finally{
			write.unlock();
		}

	}

	
	public Node getPrimary(final long k) {
		byte[] digest = hash.computeMd5(String.valueOf(k));
		Node rv = getNodeForKey(hash.hash(digest, 0));
		return rv;
	}

	Node getNodeForKey(long hash) {
		Lock read = lock.readLock();
		read.lock();
		try {
			final Node rv;
			Long key = hash;
			if (!ketamaNodes.containsKey(key)) {
				SortedMap<Long, Node> tailMap = ketamaNodes.tailMap(key);
				if (tailMap.isEmpty()) {
					key = ketamaNodes.firstKey();
				} else {
					key = tailMap.firstKey();
				}
				// For JDK1.6 version
				// key = ketamaNodes.ceilingKey(key);
				// if (key == null) {
				// key = ketamaNodes.firstKey();
				// }
			}

			rv = ketamaNodes.get(key);
			return rv;			
		} catch (Exception e) {
			return null;
		}finally{
			read.unlock();
		}

	}

	public boolean isValid() {
		return !this.ketamaNodes.isEmpty();
	}
}
