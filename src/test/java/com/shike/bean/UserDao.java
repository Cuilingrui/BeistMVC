package com.shike.bean;

import com.shike.context.annotation.Scope;
import com.shike.stereotype.Component;

import java.util.HashMap;
import java.util.Map;



@Component("ss")
public class UserDao {

    private static Map<String, String> hashMap = new HashMap<>();

    static {
        hashMap.put("10001", "小傅哥");
        hashMap.put("10002", "八杯水");
        hashMap.put("10003", "阿毛");
    }

    public String queryUserName(String uId) {
        return hashMap.get(uId);
    }


    public void destroyDataMethod(){
        System.out.println("清空数据");
        hashMap.clear();
    }

}