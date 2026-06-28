package com.czy.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czy.mall.dto.UserDTO;
import com.czy.mall.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    List<User> selectByUser(UserDTO userDTO);
}
