
package com.sap.ewm.sys.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sap.ewm.sys.mapper.SysDeptRelationMapper;
import com.sap.ewm.sys.model.SysDept;
import com.sap.ewm.sys.model.SysDeptRelation;
import com.sap.ewm.sys.service.SysDeptRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 服務實現類
 * </p>
 *
 * @author syngna
 * @since 2018-02-12
 */
@Service
public class SysDeptRelationServiceImpl extends ServiceImpl<SysDeptRelationMapper, SysDeptRelation> implements SysDeptRelationService {

	@Autowired
	private SysDeptRelationMapper sysDeptRelationMapper;

	/**
	 * 維護部門關係
	 *
	 * @param sysDept 部門
	 */
	@Override
	@Transactional(rollbackFor = Exception.class)
	public void insertDeptRelation(SysDept sysDept) {
		//增加部門關係表
		SysDeptRelation condition = new SysDeptRelation();
		condition.setDescendant(sysDept.getParentId());
		List<SysDeptRelation> relationList = sysDeptRelationMapper
			.selectList(Wrappers.<SysDeptRelation>query().lambda()
				.eq(SysDeptRelation::getDescendant, sysDept.getParentId()))
			.stream().map(relation -> {
				relation.setDescendant(sysDept.getDeptId());
				return relation;
			}).collect(Collectors.toList());
		if (CollUtil.isNotEmpty(relationList)) {
			this.saveBatch(relationList);
		}

		//自己也要維護到關係表中
		SysDeptRelation own = new SysDeptRelation();
		own.setDescendant(sysDept.getDeptId());
		own.setAncestor(sysDept.getDeptId());
		sysDeptRelationMapper.insert(own);
	}

	/**
	 * 通過ID刪除部門關係
	 *
	 * @param id
	 */
	@Override
	public void deleteAllDeptRealtion(Integer id) {
		baseMapper.deleteDeptRelationsById(id);
	}

	/**
	 * 更新部門關係
	 *
	 * @param relation
	 */
	@Override
	public void updateDeptRealtion(SysDeptRelation relation) {
		baseMapper.updateDeptRelations(relation);
	}

}
