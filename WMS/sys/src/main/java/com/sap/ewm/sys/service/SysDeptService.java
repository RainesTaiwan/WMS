
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.dto.DeptTree;
import com.sap.ewm.sys.model.SysDept;

import java.util.List;

/**
 * <p>
 * 部門管理 服務類
 * </p>
 *
 * @author syngna
 * @since 2018-01-20
 */
public interface SysDeptService extends IService<SysDept> {

	/**
	 * 查詢部門樹菜單
	 *
	 * @return 樹
	 */
	List<DeptTree> selectTree();

	/**
	 * 新增資訊部門
	 *
	 * @param sysDept
	 * @return
	 */
	Boolean saveDept(SysDept sysDept);

	/**
	 * 刪除部門
	 *
	 * @param id 部門 ID
	 * @return 成功、失敗
	 */
	Boolean removeDeptById(Integer id);

	/**
	 * 更新部門
	 *
	 * @param sysDept 部門資訊
	 * @return 成功、失敗
	 */
	Boolean updateDeptById(SysDept sysDept);

}
