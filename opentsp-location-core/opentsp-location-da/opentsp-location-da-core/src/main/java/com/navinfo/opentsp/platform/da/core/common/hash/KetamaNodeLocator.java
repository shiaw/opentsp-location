package com.navinfo.opentsp.platform.da.core.common.hash;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public final class KetamaNodeLocator {

	private TreeMap<Long, Node> ketamaNodes;
	private Hash hash;
	private int virtualNode = 160;

	public KetamaNodeLocator(List<Node> nodes, Hash alg, int nodeCopies) {
		hash = alg;
		ketamaNodes = new TreeMap<Long, Node>();

		virtualNode = nodeCopies;

		for (Node node : nodes) {
			for (int i = 0; i < virtualNode / 4; i++) {
				byte[] digest = hash.computeMd5(node.getNodeCode() + i);
				for (int h = 0; h < 4; h++) {
					long m = hash.hash(digest, h);

					ketamaNodes.put(m, node);
				}
			}
		}
	}

	public void addNode(Node node) {
		for (int i = 0; i < virtualNode / 4; i++) {
			byte[] digest = hash.computeMd5(node.getNodeCode() + i);
			for (int h = 0; h < 4; h++) {
				long m = hash.hash(digest, h);

				ketamaNodes.put(m, node);
			}
		}
	}

	public void delNode(String nodeCode) {
		ketamaNodes.remove(nodeCode);
	}

	public Node getPrimary(final Object k) {
		byte[] digest = hash.computeMd5(String.valueOf(k));
		Node rv = getNodeForKey(hash.hash(digest, 0));
		return rv;
	}

	Node getNodeForKey(long hash) {
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
	}
}
