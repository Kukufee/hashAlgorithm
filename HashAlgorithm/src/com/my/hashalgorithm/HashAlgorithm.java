package com.my.hashalgorithm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author MaYue
 * @Version CreateTime:2019年5月3日下午5:22:19
 * 一致性hash算法思路：hash算法（CRC32_hash,FNV1_32_HASH,KETAMA_HASHD等）
 * KETAMA_HASH是MemCache推荐的一致性hash算法
 * 此处使用FNV1_32_HASH
 * 环即第一个代表最后一个
 * 1.定义一个环：使物理节点均匀的分布到一个环上
 * 2.为了使其能够均匀分布需要将物理节点通过hash算法生成多个虚拟节点，越多越均匀
 * 3.hash算法的选择/尽量散列：hashCode()/散列效果没那么好;
 * 4.当某个key需要存入缓存服务器中时 先将key进行hash，存入大于该hash中最小虚拟节点对应的物理节点
 */

public class HashAlgorithm {
	
	//每个物理节点虚拟节点数量
	private int virtualNodeNum = 100;
	
	public HashAlgorithm() {
		super();
	}

	public HashAlgorithm(int virtualNodeNum) {
		super();
		this.virtualNodeNum = virtualNodeNum;
	}

	//物理节点
	//private List<String> physicalNodes= new ArrayList<String>();
	private Set<String> physicalNodes = new HashSet<String>();
	
	//key为物理节点，value为对应虚拟节点
	private Map<String, List<Integer>> virtualNodesContainer = new HashMap<String, List<Integer>>();
	
	//一致性哈希环,key虚拟节点，value为物理节点，一对多关系
	private TreeMap<Integer, String> hashCircle = new TreeMap<Integer, String>();
	
	public String getPhysicalNodeByKey(String key) {
		//获得key的hash
		Integer keyHash = FNV1_32_HASH(key);
		//通过hash获取红黑树大于部分
		SortedMap<Integer,String> tailMap = hashCircle.tailMap(keyHash);
		//如果比最大的虚拟节点大则存入第一个虚拟节点，这才是环
		if (tailMap.isEmpty()) {
			String physicalNode = hashCircle.get(hashCircle.firstKey());
			return physicalNode;
		}
		//取大于部分的第一个值即为需要存放的物理节点的虚拟节点
		String physicalNode = tailMap.get(tailMap.firstKey());
		return physicalNode;
	}
	
	public void addPhysicalNode(String physicalNode) {
		physicalNodes.add(physicalNode);
		generateVirtualNodes(physicalNode);
	}
	
	public void removePhysicalNode(String physicalNode) {
		physicalNodes.remove(physicalNode);
	}
	
	/**
	 * 为物理节点生成虚拟节点
	 */
	private void generateVirtualNodes(String physicalNode) {
		//统计虚拟节点的次数，必须为virtualNodeNum
		int i = 0;
		int count = 0;
		List<Integer> hashes = new ArrayList<Integer>();
		while (count < virtualNodeNum) {
			String node = physicalNode + "***" + i;
			i++;
			Integer hash = FNV1_32_HASH(node);
			if (!hashCircle.containsKey(hash)) {
				hashes.add(hash);
				hashCircle.put(hash, physicalNode);
				count++;
			}
		}
		virtualNodesContainer.put(physicalNode, hashes);
	}
	
	/**
	 * FNV1_32_HASH 算法
	 * @param target
	 * @return
	 */
	private Integer FNV1_32_HASH(String target) {
		int p = 1677619;
		//使用Long类型
		int hash = (int)2166136261L;
		char[] charArray = target.toCharArray();
		for (int i = 0; i < charArray.length; i++) {
			hash = (hash ^ charArray[i]) * p;
		}
		hash += hash << 13;
		hash ^= hash >> 7;
		hash += hash << 3;
		hash ^= hash >> 17;
		hash += hash << 5;
		if (hash < 0) {
			hash = Math.abs(hash);
		}
		return hash;
	}
}
