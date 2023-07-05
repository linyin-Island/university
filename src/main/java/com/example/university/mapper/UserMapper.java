package com.example.university.mapper;

import com.example.university.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户信息 Mapper 接口
 * </p>
 *
 * @author linyin
 * @since 2023-06-21
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

}
