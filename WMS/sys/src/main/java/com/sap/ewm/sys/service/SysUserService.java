
package com.sap.ewm.sys.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sap.ewm.sys.dto.UserDTO;
import com.sap.ewm.sys.dto.UserInfo;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.vo.UserVO;

import java.util.List;

/**
 * @author syngna
 * @date 2017/10/31
 */
public interface SysUserService extends IService<SysUser> {
	/**
	 * 查詢使用者資訊
	 *
	 * @param sysUser 使用者
	 * @return userInfo
	 */
	UserInfo findUserInfo(SysUser sysUser);

	/**
	 * 分頁查詢使用者資訊（含有角色資訊）
	 *
	 * @param page    分頁對像
	 * @param userDTO 參數列表
	 * @return
	 */
	IPage getUsersWithRolePage(Page page, UserDTO userDTO);

	SysUser selectUserByName(String userName);

	/**
	 * 刪除使用者
	 *
	 * @param sysUser 使用者
	 * @return boolean
	 */
	Boolean deleteUserById(SysUser sysUser);

	/**
	 * 更新目前使用者基本資訊
	 *
	 * @param userDto 使用者資訊
	 * @return Boolean
	 */
	Boolean updateUserInfo(UserDTO userDto);

	/**
	 * 更新指定使用者資訊
	 *
	 * @param userDto 使用者資訊
	 * @return
	 */
	Boolean updateUser(UserDTO userDto);

	/**
	 * 通過ID查詢使用者資訊
	 *
	 * @param id 使用者ID
	 * @return 使用者資訊
	 */
	UserVO selectUserVoById(Integer id);

	/**
	 * 儲存使用者資訊
	 *
	 * @param userDto DTO 對像
	 * @return success/fail
	 */
	Boolean saveUser(UserDTO userDto);

	/**
	 * 查詢上級部門的使用者資訊
	 *
	 * @param username 使用者名稱
	 * @return R
	 */
	List<SysUser> listAncestorUsers(String username);
}
