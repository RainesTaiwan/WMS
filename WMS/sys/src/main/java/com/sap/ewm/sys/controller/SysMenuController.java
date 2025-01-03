package com.sap.ewm.sys.controller;


import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.sap.ewm.core.base.AjaxResult;
import com.sap.ewm.core.security.annotation.Secured;
import com.sap.ewm.core.security.common.CommonMethods;
import com.sap.ewm.sys.constants.CommonConstants;
import com.sap.ewm.sys.dto.MenuTree;
import com.sap.ewm.sys.dto.TreeUtil;
import com.sap.ewm.sys.model.SysMenu;
import com.sap.ewm.sys.model.SysUser;
import com.sap.ewm.sys.service.SysMenuService;
import com.sap.ewm.sys.service.SysRoleService;
import com.sap.ewm.sys.service.SysUserService;
import com.sap.ewm.sys.vo.MenuVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜單管理模組
 * @author syngna
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class SysMenuController {

	@Autowired
	private SysMenuService sysMenuService;

	@Autowired
	private SysRoleService sysRoleService;

	@Autowired
	private SysUserService sysUserService;

	/**
	 * 返回目前使用者的樹形菜單集合
	 *
	 * @param parentId 父節點ID
	 * @return 目前使用者的樹形菜單
	 */
	@GetMapping
	public AjaxResult getUserMenu(Integer parentId) {

		String userName = CommonMethods.getUser();
		SysUser sysUser = sysUserService.selectUserByName(userName);
		if(sysUser == null) {
			return AjaxResult.error("未找到使用者[" + userName + "]");
		}
		// 獲取符合條件的菜單
		Set<MenuVO> all = new HashSet<>();
		sysRoleService.findRolesByUserId(sysUser.getUserId())
				.forEach(role -> all.addAll(sysMenuService.findMenuByRoleId(role.getRoleId())));
		List<MenuTree> menuTreeList = all.stream()
				.filter(menuVo -> CommonConstants.MENU.equals(menuVo.getType()))
				.map(MenuTree::new)
				.sorted(Comparator.comparingInt(MenuTree::getSort))
				.collect(Collectors.toList());


		return AjaxResult.success(TreeUtil.build(menuTreeList, parentId == null
				? CommonConstants.MENU_TREE_ROOT_ID : parentId));
	}

	/**
	 * 返回樹形菜單集合
	 *
	 * @return 樹形菜單
	 */
	@GetMapping(value = "/tree")
	public AjaxResult getTree() {
		return AjaxResult.success(TreeUtil.buildTree(sysMenuService
				.list(Wrappers.<SysMenu>lambdaQuery()
						.orderByAsc(SysMenu::getSort)), CommonConstants.MENU_TREE_ROOT_ID));
	}

	/**
	 * 返回角色的菜單集合
	 *
	 * @param roleId 角色ID
	 * @return 屬性集合
	 */
	@GetMapping("/tree/{roleId}")
	public AjaxResult getRoleTree(@PathVariable Integer roleId) {
		return AjaxResult.success(sysMenuService.findMenuByRoleId(roleId)
				.stream()
				.map(MenuVO::getMenuId)
				.collect(Collectors.toList()));
	}

	/**
	 * 通過ID查詢菜單的詳細資訊
	 *
	 * @param id 菜單ID
	 * @return 菜單詳細資訊
	 */
	@GetMapping("/{id}")
	public AjaxResult getById(@PathVariable Integer id) {
		return AjaxResult.success(sysMenuService.getById(id));
	}

	/**
	 * 新增菜單
	 *
	 * @param sysMenu 菜單資訊
	 * @return success/false
	 */
	@Secured("sys_menu_add")
	@PostMapping
	public AjaxResult save(@Valid @RequestBody SysMenu sysMenu) {
		return AjaxResult.success(sysMenuService.save(sysMenu));
	}

	/**
	 * 刪除菜單
	 *
	 * @param id 菜單ID
	 * @return success/false
	 */
	@Secured("sys_menu_delete")
	@DeleteMapping("/{id}")
	public AjaxResult removeById(@PathVariable Integer id) {
		return AjaxResult.success(sysMenuService.removeMenuById(id));
	}

	/**
	 * 更新菜單
	 *
	 * @param sysMenu
	 * @return
	 */
	@Secured("sys_menu_edit")
	@PutMapping
	public AjaxResult update(@Valid @RequestBody SysMenu sysMenu) {
		return AjaxResult.success(sysMenuService.updateMenuById(sysMenu));
	}

}
