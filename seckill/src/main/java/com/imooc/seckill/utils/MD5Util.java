package com.imooc.seckill.utils;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Created by ky on 2018/9/10.
 */
public class MD5Util {
    private static String salt="1a2b3c4d";
    //两次MD5
    public static String md5(String str){
        return DigestUtils.md5Hex(str);
    }
    //第一次MD5
    public static String inputFormPass(String password){
        String str=""+salt.charAt(0)+salt.charAt(2)+password+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    //第二次MD5,将第一次MD5的密码转化为数据库密码
    public static String formPassToDbPass(String formPass,String salt){
        String str=""+salt.charAt(0)+salt.charAt(2)+formPass+salt.charAt(5)+salt.charAt(4);
        return md5(str);
    }
    //将输入的密码通过两次MD5转成DB密码
    public static String inputPassToDbPass(String inputPass,String salt){
        String str=inputFormPass(inputPass);
        return formPassToDbPass(str,salt);
    }
   /*public static void main(String[] args){
        String password="123456";
        String salt="1a2b3c4d";//1a2b3c4d
        System.out.println(inputFormPass(password));//d3b1294a61a07da9b49b6e22b2cbd7f9
   }*/
}
