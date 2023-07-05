package com.example.university.service.impl;

import com.example.university.entity.User;
import com.example.university.mapper.UserMapper;
import com.example.university.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author linyin
 * @since 2023-06-21
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

}
