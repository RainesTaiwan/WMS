
package com.sap.ewm.sys.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.utils.CacheUtil;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.model.SysRole;
import com.sap.ewm.sys.service.SysRoleMenuService;
import com.sap.ewm.sys.service.SysRoleService;
import com.sap.ewm.sys.vo.RoleMenuVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author syngna
 * @date 2017/11/5
 */
@RestController
@RequestMapping("/role")
public class SysRoleController {

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysRoleMenuService sysRoleMenuService;

	/**
	 * 通過ID查詢角色資訊
	 *
	 * @param id ID
	 * @return 角色資訊
	 */
	@GetMapping("/{id}")
	public AjaxResult getById(@PathVariable Integer id) {
		return AjaxResult.success(sysRoleService.getById(id));
	}

	/**
	 * 新增角色
	 *
	 * @param sysRole 角色資訊
	 * @return success、false
	 */
	@PostMapping
	public AjaxResult save(@Valid @RequestBody SysRole sysRole) {
		return AjaxResult.success(sysRoleService.save(sysRole));
	}

	/**
	 * 修改角色
	 *
	 * @param sysRole 角色資訊
	 * @return success/false
	 */
	@PutMapping
	public AjaxResult update(@Valid @RequestBody SysRole sysRole) {
		return AjaxResult.success(sysRoleService.updateById(sysRole));
	}

	/**
	 * 刪除角色
	 *
	 * @param id
	 * @return
	 */
	@DeleteMapping("/{id}")
	public AjaxResult removeById(@PathVariable Integer id) {
		return AjaxResult.success(sysRoleService.removeRoleById(id));
	}

	/**
	 * 獲取角色列表
	 *
	 * @return 角色列表
	 */
	@GetMapping("/list")
	public AjaxResult listRoles() {
		return AjaxResult.success(sysRoleService.list(Wrappers.emptyWrapper()));
	}

	/**
	 * 分頁查詢角色資訊
	 *
	 * @param page 分頁對像
	 * @return 分頁對像
	 */
	@GetMapping("/page")
	public AjaxResult getRolePage(Page page) {
		return AjaxResult.success(sysRoleService.page(page, Wrappers.emptyWrapper()));
	}

	/**
	 * 更新角色菜單
	 *
	 * @param roleMenuVO
	 * @return success、false
	 */
	@PutMapping("/menu")
	public AjaxResult saveRoleMenus(@RequestBody RoleMenuVO roleMenuVO) {
		Integer roleId = roleMenuVO.getRoleId();
		List<String> menuIdList = roleMenuVO.getMenuIds();
		String menuIds = "";
		if(menuIdList != null && menuIdList.size() > 0) {
			menuIds = StringUtils.join(menuIdList, ",");
		}
		SysRole sysRole = sysRoleService.getById(roleId);
		sysRoleMenuService.saveRoleMenus(sysRole.getRoleCode(), roleId, menuIds);

		CacheUtil.removeByPerfix(CommonConstants.USER_PERMISSION_CACHE_KEY);

		return AjaxResult.success();
	}

	/**
	 * 通過角色ID 查詢角色列表
	 *
	 * @param roleIdList 角色ID
	 * @return
	 */
	@PostMapping("/getRoleList")
	public AjaxResult getRoleList(@RequestBody List<String> roleIdList) {
		return AjaxResult.success(sysRoleService.listByIds(roleIdList));
	}
}
