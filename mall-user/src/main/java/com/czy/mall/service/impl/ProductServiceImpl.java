package com.czy.mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.czy.mall.entity.Product;
import com.czy.mall.mapper.ProductMapper;
import com.czy.mall.service.ProductService;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl
        extends ServiceImpl<ProductMapper, Product>
        implements ProductService {
}
