package com.bbs.util;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ONE;
import static org.apache.commons.lang3.math.NumberUtils.INTEGER_ZERO;

/**
 * @author WYH
 */
@Component
public class FirstWordsSqlUtils {

    //依次从小到大排序
    private static final Map<String,String> wordsMap;

    static {
        wordsMap = new HashMap<>();
        wordsMap.put("a","45217,45252");wordsMap.put("b","45253,45760");wordsMap.put("c","45761,46317");
        wordsMap.put("d","46318,46825");wordsMap.put("e","46826,47009");wordsMap.put("f","47010,47296");
        wordsMap.put("g","47297,47613");wordsMap.put("h","47614,48118");wordsMap.put("j","48119,49061");
        wordsMap.put("k","49062,49323");wordsMap.put("l","49324,49895");wordsMap.put("m","49896,50370");
        wordsMap.put("n","50371,50613");wordsMap.put("o","50614,50621");wordsMap.put("p","50622,50905");
        wordsMap.put("q","50906,51386");wordsMap.put("r","51387,51445");wordsMap.put("s","51446,52217");
        wordsMap.put("t","52218,52697");wordsMap.put("w","52698,52979");wordsMap.put("x","52980,53640");
        wordsMap.put("y","53689,54480");wordsMap.put("z","54481,55289");
    }
    /**
     * 拼接sql
     */
    public static String getSql(String str){
        String wordsStr = str.toLowerCase();
        //排除该三个首字母，因为中文就没有以他们开头的拼音
        String firstStr = str.substring(INTEGER_ZERO, INTEGER_ONE);
        if(firstStr.equals("i") || firstStr.equals("u") || firstStr.equals("v")){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < wordsStr.length(); i++) {
            String c = wordsStr.charAt(i)+"";
            if(c.equals("i") || c.equals("u") || c.equals("v")){
                continue;
            }
            if(i > INTEGER_ZERO) sb.append(" and ");
            String wordsASC  = wordsMap.get(c);
            String[] asc = wordsASC.split(",");
            int ASC01 = Integer.parseInt(asc[0]);
            int ASC02 = Integer.parseInt(asc[1]);
            sb.append("CONV(HEX(SUBSTRING(CONVERT(name USING gbk ), ").append(i + 1).append(",1)), 16, 10) BETWEEN ").append(ASC01).append(" AND ").append(ASC02);
        }
        return sb.toString();
    }
}