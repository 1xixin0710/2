package com.dlut.campusmarket.mapper;

import com.dlut.campusmarket.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    User findByUsername(String username);

    int insert(User user);
}
