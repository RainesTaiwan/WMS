package com.fw.wcs.sys.mapper;

import com.fw.wcs.sys.model.ButtonTask;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Glanz
 *
 */

@Repository
public interface ButtonTaskMapper extends BaseMapper<ButtonTask>{
    List<ButtonTask> findButtonTaskByReceiveStation(@Param("receiveStation") String receiveStation);
    ButtonTask findButtonTaskByHandle(@Param("handle") String handle);
}
