package com.myapp.service;

import com.myapp.entity.User;
import com.myapp.dao.UserDao;

public class UserService extends norm.Service<User, Integer> {

    public UserService(){
        super(UserDao.class);
    }

}