package xyz.wongs.weathertop.base.utils;

import lombok.Data;

import java.util.List;

/**
 * The class Tree node.
 *
 * @author iwcngs@gmail.com
 */
@Data
public class TreeNode {
	/**
	 * 节点编码
	 */
	private String nodeCode;
	/**
	 * 节点名称
	 */
	private String nodeName;
	/**
	 * ID
	 */
	private Long id;
	/**
	 * 父ID
	 */
	private Long pid;
	/**
	 * 孩子节点信息
	 */
	private List<TreeNode> children;

}
