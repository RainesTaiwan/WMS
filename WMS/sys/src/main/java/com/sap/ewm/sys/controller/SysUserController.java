
package com.sap.ewm.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.base.FrontPage;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.core.utils.CacheUtil;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.dto.UserDTO;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author syngna
 * @date 2018/12/16
 */
@RestController
@RequestMapping("/user")
public class SysUserController {

	@Autowired
	private SysUserService userService;

	/**
	 * 獲取目前使用者全部資訊
	 *
	 * @return 使用者資訊
	 */
	@GetMapping(value = {"/info"})
	public AjaxResult info() {
		String username = CommonMethods.getUser();
		SysUser user = userService.getOne(Wrappers.<SysUser>query()
				.lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return AjaxResult.error("獲取目前使用者資訊失敗");
		}
		return AjaxResult.success(userService.findUserInfo(user));
	}

	/**
	 * 獲取指定使用者全部資訊
	 *
	 * @return 使用者資訊
	 */
	@GetMapping("/info/{username}")
	public AjaxResult info(@PathVariable String username) {
		SysUser user = userService.getOne(Wrappers.<SysUser>query()
				.lambda().eq(SysUser::getUsername, username));
		if (user == null) {
			return AjaxResult.error(String.format("使用者資訊為空 %s", username));
		}
		return AjaxResult.success(userService.findUserInfo(user));
	}

	/**
	 * 通過ID查詢使用者資訊
	 *
	 * @param id ID
	 * @return 使用者資訊
	 */
	@GetMapping("/{id}")
	public AjaxResult user(@PathVariable Integer id) {
		return AjaxResult.success(userService.selectUserVoById(id));
	}

	/**
	 * 根據使用者名稱查詢使用者資訊
	 *
	 * @param username 使用者名稱
	 * @return
	 */
	@GetMapping("/details/{username}")
	public AjaxResult user(@PathVariable String username) {
		SysUser condition = new SysUser();
		condition.setUsername(username);
		return AjaxResult.success(userService.getOne(new QueryWrapper<>(condition)));
	}

	/**
	 * 刪除使用者資訊
	 *
	 * @param id ID
	 * @return R
	 */
	@Secured("sys_user_delete")
	@DeleteMapping("/{id}")
	public AjaxResult userDel(@PathVariable Integer id) {
		SysUser sysUser = userService.getById(id);
		return AjaxResult.success(userService.deleteUserById(sysUser));
	}

	/**
	 * 新增使用者
	 *
	 * @param userDto 使用者資訊
	 * @return success/false
	 */
	@Secured("sys_user_add")
	@PostMapping
	public AjaxResult user(@RequestBody UserDTO userDto) {
		return AjaxResult.success(userService.saveUser(userDto));
	}

	/**
	 * 更新使用者資訊
	 *
	 * @param userDto 使用者資訊
	 * @return R
	 */
	@Secured("sys_user_edit")
	@PutMapping
	public AjaxResult updateUser(@Valid @RequestBody UserDTO userDto) {
		Boolean updated = userService.updateUser(userDto);
		CacheUtil.remove(CommonConstants.USER_PERMISSION_CACHE_KEY + userDto.getUsername());
		return AjaxResult.success(updated);
	}

	/**
	 * 分頁查詢使用者
	 *
	 * @param page    參數集
	 * @param userDTO 查詢參數列表
	 * @return 使用者集合
	 */
	@GetMapping("/page")
	public AjaxResult getUserPage(FrontPage<SysUser> page, UserDTO userDTO) {
		return AjaxResult.success(userService.getUsersWithRolePage(page.getPagePlus(), userDTO));
	}

	/**
	 * 修改個人資訊
	 *
	 * @param userDto userDto
	 * @return success/false
	 */
	@PutMapping("/edit")
	public AjaxResult updateUserInfo(@Valid @RequestBody UserDTO userDto) {
		return AjaxResult.success(userService.updateUserInfo(userDto));
	}

	/**
	 * @param username 使用者名稱稱
	 * @return 上級部門使用者列表
	 */
	@GetMapping("/ancestor/{username}")
	public AjaxResult listAncestorUsers(@PathVariable String username) {
		return AjaxResult.success(userService.listAncestorUsers(username));
	}

	/**
	 * @param username 使用者名稱
	 * @return
	 */
	@GetMapping("/removePermissionCache/{username}")
	public AjaxResult removePermissionCache(@PathVariable String username) {
		CacheUtil.remove(CommonConstants.USER_PERMISSION_CACHE_KEY + username);
		return AjaxResult.success();
	}
}
