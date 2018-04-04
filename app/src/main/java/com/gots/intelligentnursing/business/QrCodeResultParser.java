package com.gots.intelligentnursing.business;

import com.gots.intelligentnursing.exception.ParseException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhqy
 * @date 2018/4/3
 */

public class QrCodeResultParser {

    private static final String URL_PREFIX = "http://www.test.com/abc?";
    private static final String KEY_ID = "id";
    private static final String ERROR_URL_NOT_SUPPORT = "二维码内容不支持";
    private static final String ERROR_ID_NOT_FOUND = "二维码有误";

    public static String parse(String result) throws ParseException {
        if(!result.startsWith(URL_PREFIX)){
            throw new ParseException(ERROR_URL_NOT_SUPPORT);
        }else{
            String paramString = result.replace(URL_PREFIX, "");
            char[] paramCharArray = paramString.toCharArray();

            // 存储参数的键和值
            Map<String, String> param = new HashMap<>(5);
            String key = null;
            String value = null;

            StringBuilder builder = new StringBuilder();

            // 对参数进行解析
            for(int i = 0;i < paramCharArray.length;i++){
                if(paramCharArray[i] == '='){
                    // 表示该参数的key结束
                    key = builder.toString();
                    builder.setLength(0);
                } else if(key != null && paramCharArray[i] == '&'){
                    // 当读取到键时
                    // &被认为是值的结尾
                    value = builder.toString();
                    param.put(key, value);
                    builder.setLength(0);
                    key = null;
                } else{
                    builder.append(paramCharArray[i]);
                }
            }
            if(key != null){
                // 最后一个value没有以&结尾，单独处理
                value = builder.toString();
                param.put(key, value);
            }

            String id = param.get(KEY_ID);
            if(id != null){
                return id;
            }else{
                throw new ParseException(ERROR_ID_NOT_FOUND);
            }
        }
    }
}
