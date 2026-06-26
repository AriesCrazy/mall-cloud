package com.czy.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czy.mall.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
