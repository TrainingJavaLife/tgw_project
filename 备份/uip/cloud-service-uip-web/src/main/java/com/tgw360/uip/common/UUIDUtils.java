package com.tgw360.uip.common;

import java.util.UUID;

/**
 * UUID生成
 * Created by 邹祥 on 2017/12/18 16:30
 */
public abstract class UUIDUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
