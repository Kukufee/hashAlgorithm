package com.my.hashalgorithm;

/**
 *
 * @author MaYue
 * @Version CreateTime:2019年5月3日下午8:09:38
 *
 */

public class Client {
	public static void main(String[] args) {
		HashAlgorithm hashAlgorithm = new HashAlgorithm();
		hashAlgorithm.addPhysicalNode("127.0.0.1");
		hashAlgorithm.addPhysicalNode("127.0.0.2");
		hashAlgorithm.addPhysicalNode("127.0.0.3");
		String node1 = hashAlgorithm.getPhysicalNodeByKey("图片1");
		String node2 = hashAlgorithm.getPhysicalNodeByKey("图片2");
		String node3 = hashAlgorithm.getPhysicalNodeByKey("图片3");
		System.out.println(node1 + " " + node2 + " " + node3 + " ");
	}
}
