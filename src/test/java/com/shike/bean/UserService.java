package com.shike.bean;


import com.shike.beans.BeansException;
import com.shike.beans.factory.*;
import com.shike.beans.factory.annotation.Autowired;
import com.shike.beans.factory.annotation.Qualifier;
import com.shike.context.ApplicationContext;
import com.shike.context.annotation.Scope;
import com.shike.stereotype.Component;

@Component
public class UserService  {

    private String uId;
    String company;

    String location;
    @Autowired
    @Qualifier("ss")
    private UserDao userDao;

//    private ApplicationContext context;

    private BeanFactory factory;

    public String queryUserInfo() {
        return userDao.queryUserName(uId);
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "UserService{" +
                "uId='" + uId + '\'' +
                ", company='" + company + '\'' +
                ", location='" + location + '\'' +
                ", userDao=" + userDao +
                '}';
    }
}