package com.shike.beistmvc.webmvc.common.utils;

import java.util.UUID;

public class UUIDUtil {

    public static String getString() {
        return UUID.randomUUID().toString().replace("-", "");
    }

}
