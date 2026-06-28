package com.czy.mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.czy.mall.entity.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {
}
