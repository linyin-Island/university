package com.example.university.service;

import com.example.university.entity.ShoppingCart;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 购物车 服务类
 * </p>
 *
 * @author linyin
 * @since 2023-06-23
 */
public interface ShoppingCartService extends IService<ShoppingCart> {
    public void clean();
}
