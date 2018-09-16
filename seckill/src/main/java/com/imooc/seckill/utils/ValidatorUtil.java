package com.imooc.seckill.utils;

import org.thymeleaf.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ky on 2018/9/10.
 */
public class ValidatorUtil {
    private static final Pattern pattern=Pattern.compile("1\\d{10}");
    public static boolean isMobile(String mobile){
        if(StringUtils.isEmpty(mobile)){
            return false;
        }
        Matcher match=pattern.matcher(mobile);
        return match.matches();
    }

}
