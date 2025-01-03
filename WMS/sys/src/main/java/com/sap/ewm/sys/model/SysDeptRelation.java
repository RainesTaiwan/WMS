
package com.sap.ewm.sys.model;

import com.baomidou.mybatisplus.extension.activerecord.Model;

/**
 * <p>
 * 部門關係表
 * </p>
 *
 * @author syngna
 * @since 2018-01-22
 */
public class SysDeptRelation extends Model<SysDeptRelation> {

	private static final long serialVersionUID = 1L;
	/**
	 * 祖先節點
	 */
	private Integer ancestor;
	/**
	 * 後代節點
	 */
	private Integer descendant;

	public Integer getAncestor() {
		return ancestor;
	}

	public void setAncestor(Integer ancestor) {
		this.ancestor = ancestor;
	}

	public Integer getDescendant() {
		return descendant;
	}

	public void setDescendant(Integer descendant) {
		this.descendant = descendant;
	}
}
