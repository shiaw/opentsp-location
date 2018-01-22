package com.navinfo.tasktracker.rprest.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author HK
 *
 */
public class VehiclePassTimesTreeNode implements Serializable{
	private static final long serialVersionUID = 1L;
	private long tileId;
	private int times;
	private VehiclePassTimesTreeNode parent;
	private List<VehiclePassTimesTreeNode> children;
	private int currentDepth;
	private int month;
	private int district;
	private Map<Long, Integer> terminals;
	
	public VehiclePassTimesTreeNode(long tileId, int times, int month, int district){
		this.tileId = tileId;
		this.times = times;
		this.month = month;
		this.district = district;
		initChildren();
	}
	
	public VehiclePassTimesTreeNode(long tileId, int times, VehiclePassTimesTreeNode parent){
		this.tileId = tileId;
		this.times = times;
		this.parent = parent;
		this.currentDepth = parent.getCurrentDepth()+1;
		initChildren();
	}
	
	public Map<Long, Integer> getTerminals(){
		return terminals;
	}
	
	public void initChildren(){
		if(children == null){
			children = new ArrayList();
		}
		if(terminals == null){
			terminals = new HashMap<Long, Integer>();
		}
	}
	
	public void addChildren(VehiclePassTimesTreeNode node){
		initChildren();
		children.add(node);
	}
	
	public boolean isLeaf(){
		if(children == null){
			return true;
		}else{
			if(children.isEmpty()){
				return true;
			}else{
				return false;
			}
		}
	}
	
	public List<VehiclePassTimesTreeNode> getElders() {
		List<VehiclePassTimesTreeNode> elderList = new ArrayList<VehiclePassTimesTreeNode>();
		VehiclePassTimesTreeNode parentNode = this.getParent();
		if (parentNode == null) {
			return elderList;
		} else {
			elderList.add(parentNode);
			elderList.addAll(parentNode.getElders());
			return elderList;
		}
	}
	
	public List<VehiclePassTimesTreeNode> getJuniors() {
		List<VehiclePassTimesTreeNode> juniorList = new ArrayList<VehiclePassTimesTreeNode>();
		List<VehiclePassTimesTreeNode> childList = this.getChildren();
		if (childList == null) {
			return juniorList;
		} else {
			int childNumber = childList.size();
			for (int i = 0; i < childNumber; i++) {
				VehiclePassTimesTreeNode junior = childList.get(i);
				juniorList.add(junior);
				juniorList.addAll(junior.getJuniors());
			}
			return juniorList;
		}
	}
	
	public VehiclePassTimesTreeNode findTreeNodeById(long tileId) {
        if (this.tileId == tileId)
            return this;
        if (children == null || children.isEmpty()) {
            return null;
        } else {
            int childNumber = children.size();
            for (int i = 0; i < childNumber; i++) {
                VehiclePassTimesTreeNode child = children.get(i);
                VehiclePassTimesTreeNode resultNode = child.findTreeNodeById(tileId);
                if (resultNode != null) {
                    return resultNode;
                }
            }
            return null;
        }
    }
	
	public void traverse() {
        if (tileId <= 0)
            return;
        System.err.println(this.tileId);
        if (children == null || children.isEmpty())
            return;
        int childNumber = children.size();
        for (int i = 0; i < childNumber; i++) {
            VehiclePassTimesTreeNode child = children.get(i);
            child.traverse();
        }
    }
	
	public long getTileId() {
		return tileId;
	}

	public void setTileId(long tileId) {
		this.tileId = tileId;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public VehiclePassTimesTreeNode getParent() {
		return parent;
	}

	public void setParent(VehiclePassTimesTreeNode parent) {
		this.parent = parent;
	}

	public List<VehiclePassTimesTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<VehiclePassTimesTreeNode> children) {
		this.children = children;
	}
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public int getCurrentDepth() {
		return currentDepth;
	}

	public void setCurrentDepth(int currentDepth) {
		this.currentDepth = currentDepth;
	}
	
}
