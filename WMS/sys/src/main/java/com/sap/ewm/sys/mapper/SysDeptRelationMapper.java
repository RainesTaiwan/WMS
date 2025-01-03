
package com.sap.ewm.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.sys.model.SysDeptRelation;

/**
 * <p>
 * Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2018-02-12
 */
public interface SysDeptRelationMapper extends BaseMapper<SysDeptRelation> {
	/**
	 * 刪除部門關係表數據
	 *
	 * @param id 部門ID
	 */
	void deleteDeptRelationsById(Integer id);

	/**
	 * 更改部分關係表數據
	 *
	 * @param deptRelation
	 */
	void updateDeptRelations(SysDeptRelation deptRelation);

}
