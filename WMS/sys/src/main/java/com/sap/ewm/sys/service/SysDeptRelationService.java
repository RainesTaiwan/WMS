
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.model.SysDept;
import com.sap.ewm.sys.model.SysDeptRelation;

/**
 * <p>
 * 服務類
 * </p>
 *
 * @author syngna
 * @since 2018-02-12
 */
public interface SysDeptRelationService extends IService<SysDeptRelation> {

	/**
	 * 新建部門關係
	 *
	 * @param sysDept 部門
	 */
	void insertDeptRelation(SysDept sysDept);

	/**
	 * 通過ID刪除部門關係
	 *
	 * @param id
	 */
	void deleteAllDeptRealtion(Integer id);

	/**
	 * 更新部門關係
	 *
	 * @param relation
	 */
	void updateDeptRealtion(SysDeptRelation relation);
}
