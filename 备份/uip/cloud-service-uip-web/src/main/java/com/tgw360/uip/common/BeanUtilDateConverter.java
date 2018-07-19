package com.tgw360.uip.common;

import org.apache.commons.beanutils.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * BeanUtil日期转换器
 * Created by 邹祥 on 2017/11/23 11:10
 */
public class BeanUtilDateConverter implements Converter {
    private String pattern;

    public BeanUtilDateConverter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public <T> T convert(Class<T> type, Object value) {
        Date date1 = null;
        if (value instanceof String) {
            String date = (String) value;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            try {
                date1 = sdf.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return (T) date1;
    }
}
