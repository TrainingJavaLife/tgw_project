package com.tgw360.common;

import java.util.Random;

/**
 * Created by 危宇 on 2018/1/9.
 */
public class UUIDUtil {
    public static final String allCharStr = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!@#$%^&*()_+";
    public static final String allChar = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String letterChar = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String numberChar = "0123456789";
    public static final String specialChar = "!@#$%^&*()_+";

    /**
     * 生成一个定长度的随机字符串（只包数字）
     * @param length 随机字符串长度
     * @return
     */
    public static String generateInteger(int length){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<length;i++){
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        return sb.toString();
    }

    /**
     * 生成一个定长度的随机字符串（只包含大小写字母，数字）
     * @param length 随机字符串长度
     * @return
     */
    public static String generateString(int length){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<length;i++){
            sb.append(allChar.charAt(random.nextInt(allChar.length())));
        }
        return sb.toString();
    }

    /**
     * 生成一个定长度的随机字符串（包含大小字母，数字，特殊符号)
     * @param length 随机字符串长度
     * @return
     */
    public static String generateAllString(int length){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<length;i++){
            sb.append(allCharStr.charAt(random.nextInt(allCharStr.length())));
        }
        return sb.toString();
    }

    /**
     * 生成一个定长度的随机纯字母字符串（只包含大小字母)
     * @param length 随机字符串长度
     * @return
     */
    public static String generateMixLetter(int length){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<length;i++){
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
        }
        return sb.toString();
    }

    /**
     * 生成一个定长度的随机字符串（只包含小写字母)
     * @param length
     * @return
     */
    public static String generateLowerLetter(int length){
        return generateMixLetter(length).toLowerCase();
    }

    /**
     * 生成一个定长度的随机字符串（只包含大写字母)
     * @param length
     * @return
     */
    public static String generateUpperLetter(int length){
        return generateMixLetter(length).toUpperCase();
    }

    /**
     * 生成一个定长的纯0字符串
     * @param length
     * @return
     */
    public static String generateZeroString(int length){
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<length;i++){
            sb.append('0');
        }
        return sb.toString();
    }

    /**
     * 根据数字生成一个定长的字符串，长度不够前面补0
     * @param num 数字
     * @param length 字符串长度
     * @return
     */
    public static String fixedStringLength(long num, int length){
        StringBuffer sb = new StringBuffer();
        String strNum = String.valueOf(num);
        if(length-strNum.length() >= 0){
            sb.append(generateZeroString(length-strNum.length()));
        }else{
            throw new RuntimeException("将数字"+num+"转化为长度为"+length+"的字符串发生异常！");
        }
        sb.append(strNum);
        return sb.toString();
    }

    /**
     * 八位随机密码（字母+数字+特殊字符）
     * @return
     */
    public static String generateRandomPwd(){
        StringBuffer sb = new StringBuffer();
        Random random = new Random();
        for(int i=0;i<3;i++){
            sb.append(letterChar.charAt(random.nextInt(letterChar.length())));
            sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
        }
        for(int i=0;i<2;i++){
            sb.append(specialChar.charAt(random.nextInt(specialChar.length())));
        }
        return sb.toString();
    }
}
