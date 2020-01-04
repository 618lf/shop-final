package com.tmt.core.utils;

import java.math.BigDecimal;

/**
 * 金额格式化
 * @author lifeng
 */
public class MoneyUtils {

	/**
     * 数字金额大写转换，思想先写个完整的然后将如零拾替换成零
     * 要用到正则表达式
     */
	public static String toString(double n){
        String fraction[] = {"角", "分"};
        String digit[] = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
        String unit[][] = {{"元", "万", "亿"}, {"", "拾", "佰", "仟"}};
        String head = n < 0? "负": ""; n = Math.abs(n);
        String s = "";
        for(int i = 0; i < fraction.length; i++) {
            double f1= new BigDecimal(n).setScale(2,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(10 * Math.pow(10, i))).doubleValue(); 
            s += (digit[(int) (Math.floor(f1) % 10)] + fraction[i]).replaceAll("(零.)+", "");
        }
        if(s.length()<1){ s = "整";}
        int integerPart = (int)Math.floor(n);
        for(int i = 0; i < unit[0].length && integerPart > 0; i++) {
            String p = "";
            for (int j = 0; j < unit[1].length && n > 0; j++) {
                p = digit[integerPart%10]+unit[1][j] + p;
                integerPart = integerPart/10;
            }
            s = p.replaceAll("(零.)*零$", "").replaceAll("^$", "零") + unit[0][i] + s;
        }
        return head + s.replaceAll("(零.)*零元", "元").replaceFirst("(零.)+", "").replaceAll("(零.)+", "零").replaceAll("^整$", "零元整");
    }
	
	public static void main(String[] args) {
		System.out.println(toString(0.94));  // 负壹亿壹仟零壹拾万壹仟零壹拾元壹角
	}
}
