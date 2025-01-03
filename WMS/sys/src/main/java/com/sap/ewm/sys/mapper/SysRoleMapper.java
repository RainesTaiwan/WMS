package com.sap.ewm.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sap.ewm.sys.model.SysRole;

import java.util.List;

/**
 * <p>
 * Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysRoleMapper extends BaseMapper<SysRole> {
	/**
	 * 通過使用者ID，查詢角色資訊
	 *
	 * @param userId
	 * @return
	 */
	List<SysRole> listRolesByUserId(Integer userId);
}
