package com.sap.ewm.sys.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sap.ewm.sys.dto.UserDTO;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.vo.UserVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 使用者表 Mapper 介面
 * </p>
 *
 * @author syngna
 * @since 2017-10-29
 */
public interface SysUserMapper extends BaseMapper<SysUser> {
	/**
	 * 通過使用者名稱查詢使用者資訊（含有角色資訊）
	 *
	 * @param username 使用者名稱
	 * @return userVo
	 */
	UserVO getUserVoByUsername(String username);

	/**
	 * 分頁查詢使用者資訊（含角色）
	 *
	 * @param page      分頁
	 * @param userDTO   查詢參數
	 * @return list
	 */
	IPage<List<UserVO>> getUserVosPage(Page page, @Param("query") UserDTO userDTO);

	/**
	 * 通過ID查詢使用者資訊
	 *
	 * @param id 使用者ID
	 * @return userVo
	 */
	UserVO getUserVoById(Integer id);
}
