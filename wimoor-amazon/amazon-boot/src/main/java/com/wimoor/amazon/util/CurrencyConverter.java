package com.wimoor.amazon.util;


import java.util.HashMap;
import java.util.Map;

public class CurrencyConverter {

    private static final Map<String, String> CN_TO_CODE = new HashMap<>();

    static {
        CN_TO_CODE.put("美元", "USD");
        CN_TO_CODE.put("欧元", "EUR");
        CN_TO_CODE.put("日元", "JPY");
        CN_TO_CODE.put("港元", "HKD");
        CN_TO_CODE.put("英镑", "GBP");
        CN_TO_CODE.put("澳元", "AUD");
        CN_TO_CODE.put("新西兰元", "NZD");
        CN_TO_CODE.put("新加坡元", "SGD");
        CN_TO_CODE.put("瑞士法郎", "CHF");
        CN_TO_CODE.put("加元", "CAD");
        CN_TO_CODE.put("澳门元", "MOP");
        CN_TO_CODE.put("林吉特", "MYR");
        CN_TO_CODE.put("卢布", "RUB");
        CN_TO_CODE.put("兰特", "ZAR");
        CN_TO_CODE.put("韩元", "KRW");
        CN_TO_CODE.put("迪拉姆", "AED");       // 阿联酋迪拉姆
        CN_TO_CODE.put("里亚尔", "SAR");       // 沙特里亚尔（常见）
        CN_TO_CODE.put("福林", "HUF");
        CN_TO_CODE.put("兹罗提", "PLN");
        CN_TO_CODE.put("丹麦克朗", "DKK");
        CN_TO_CODE.put("瑞典克朗", "SEK");
        CN_TO_CODE.put("挪威克朗", "NOK");
        CN_TO_CODE.put("里拉", "TRY");         // 土耳其里拉
        CN_TO_CODE.put("比索", "MXN");         // 墨西哥比索（根据常见场景，也可改为 PHP 等）
        CN_TO_CODE.put("泰铢", "THB");
    }

    /**
     * 将中文币种名称转换为对应的三位货币代码（ISO 4217）。
     *
     * @param chineseName 中文币种名称，例如 "美元"
     * @return 对应的货币代码，如 "USD"；如果未找到映射则返回 null
     */
    public static String toCurrencyCode(String chineseName) {
        if (chineseName == null || chineseName.trim().isEmpty()) {
            return null;
        }
        return CN_TO_CODE.get(chineseName);
    }
}