package com.wimoor.amazon.common.service.impl;

import com.wimoor.amazon.common.pojo.entity.ExchangeRate;
import com.wimoor.common.GeneralUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PBCExchangeRateParser {

    // 币种名称到代码的映射（根据你的数据库）
    private static final Map<String, String> CURRENCY_CODE_MAP = new HashMap<>();
    private static final Map<String, String> CURRENCY_SYMBOL_MAP = new HashMap<>();

    static {
        // 初始化币种映射（根据你提供的数据库数据）
        CURRENCY_CODE_MAP.put("美元", "USD");
        CURRENCY_CODE_MAP.put("欧元", "EUR");
        CURRENCY_CODE_MAP.put("日元", "JPY");
        CURRENCY_CODE_MAP.put("港元", "HKD");
        CURRENCY_CODE_MAP.put("英镑", "GBP");
        CURRENCY_CODE_MAP.put("澳大利亚元", "AUD");
        CURRENCY_CODE_MAP.put("新西兰元", "NZD");
        CURRENCY_CODE_MAP.put("新加坡元", "SGD");
        CURRENCY_CODE_MAP.put("瑞士法郎", "CHF");
        CURRENCY_CODE_MAP.put("加拿大元", "CAD");
        CURRENCY_CODE_MAP.put("澳门元", "MOP");
        CURRENCY_CODE_MAP.put("林吉特", "MYR");
        CURRENCY_CODE_MAP.put("俄罗斯卢布", "RUB");
        CURRENCY_CODE_MAP.put("南非兰特", "ZAR");
        CURRENCY_CODE_MAP.put("韩元", "KRW");
        CURRENCY_CODE_MAP.put("阿联酋迪拉姆", "AED");
        CURRENCY_CODE_MAP.put("沙特里亚尔", "SAR");
        CURRENCY_CODE_MAP.put("匈牙利福林", "HUF");
        CURRENCY_CODE_MAP.put("波兰兹罗提", "PLN");
        CURRENCY_CODE_MAP.put("丹麦克朗", "DKK");
        CURRENCY_CODE_MAP.put("瑞典克朗", "SEK");
        CURRENCY_CODE_MAP.put("挪威克朗", "NOK");
        CURRENCY_CODE_MAP.put("土耳其里拉", "TRY");
        CURRENCY_CODE_MAP.put("墨西哥比索", "MXN");
        CURRENCY_CODE_MAP.put("泰铢", "THB");

        // 币种名称到数据库symbol字段的映射（根据你的数据库数据）
        CURRENCY_SYMBOL_MAP.put("美元", "美元");
        CURRENCY_SYMBOL_MAP.put("欧元", "欧元");
        CURRENCY_SYMBOL_MAP.put("日元", "日元");
        CURRENCY_SYMBOL_MAP.put("港元", "港币");
        CURRENCY_SYMBOL_MAP.put("英镑", "英镑");
        CURRENCY_SYMBOL_MAP.put("澳大利亚元", "澳大利亚元");
        CURRENCY_SYMBOL_MAP.put("新西兰元", "新西兰元");
        CURRENCY_SYMBOL_MAP.put("新加坡元", "新加坡元");
        CURRENCY_SYMBOL_MAP.put("瑞士法郎", "瑞士法郎");
        CURRENCY_SYMBOL_MAP.put("加拿大元", "加拿大元");
        CURRENCY_SYMBOL_MAP.put("澳门元", "澳门元");
        CURRENCY_SYMBOL_MAP.put("林吉特", "林吉特");
        CURRENCY_SYMBOL_MAP.put("俄罗斯卢布", "卢布");
        CURRENCY_SYMBOL_MAP.put("南非兰特", "南非兰特");
        CURRENCY_SYMBOL_MAP.put("韩元", "韩国元");
        CURRENCY_SYMBOL_MAP.put("阿联酋迪拉姆", "阿联酋迪拉姆");
        CURRENCY_SYMBOL_MAP.put("沙特里亚尔", "沙特里亚尔");
        CURRENCY_SYMBOL_MAP.put("匈牙利福林", "匈牙利福林");
        CURRENCY_SYMBOL_MAP.put("波兰兹罗提", "波兰兹罗提");
        CURRENCY_SYMBOL_MAP.put("丹麦克朗", "丹麦克朗");
        CURRENCY_SYMBOL_MAP.put("瑞典克朗", "瑞典克朗");
        CURRENCY_SYMBOL_MAP.put("挪威克朗", "挪威克朗");
        CURRENCY_SYMBOL_MAP.put("土耳其里拉", "土耳其里拉");
        CURRENCY_SYMBOL_MAP.put("墨西哥比索", "墨西哥比索");
        CURRENCY_SYMBOL_MAP.put("泰铢", "泰国铢");
    }

    /**
     * 解析完整的中国人民银行汇率文本
     * 注意：你的数据库存储的是 100单位外币对应多少人民币
     */
    public static List<ExchangeRate> parseExchangeRateText(String text) {
        List<ExchangeRate> result = new ArrayList<>();

        try {
            // 1. 提取日期
            Date exchangeDate = extractDateFromText(text);
            if (exchangeDate == null) {
                exchangeDate = new Date();
            }

            System.out.println("解析日期: " + exchangeDate);

            // 2. 解析"1外币对人民币X元"格式（前半部分）
            parseForeignToCNY(text, result, exchangeDate);

            // 3. 解析"人民币1元对外币X"格式（后半部分）
            parseCNYToForeign(text, result, exchangeDate);

            System.out.println("总共解析到 " + result.size() + " 种汇率");

        } catch (Exception e) {
            System.err.println("解析汇率文本失败: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 解析"1外币对人民币X元"格式（标准格式）
     * 例如：1美元对人民币7.0638元
     * 人民银行：1美元 = 7.0638人民币
     * 你需要：100美元 = ? 人民币
     * 计算：100 * 7.0638 = 706.38
     */
    private static void parseForeignToCNY(String text, List<ExchangeRate> result, Date exchangeDate) {
        // 匹配模式：数字+币种名称+对人民币+数字+元
        if(text.contains("外汇市场人民币汇率中间价为")){
            text = text.split("外汇市场人民币汇率中间价为")[1].trim();
        }
        Pattern pattern = Pattern.compile(
                "(\\d+(?:\\.\\d+)?)\\s*([^对]+?)对人民币\\s*(\\d+(?:\\.\\d+)?)\\s*元"
        );

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            try {
                String foreignAmountStr = matcher.group(1);  // 外币数量（通常是1或100）
                String currencyName = matcher.group(2).trim(); // 币种名称
                String cnyPerForeignStr = matcher.group(3);   // 1外币对应的人民币数
                System.out.println("解析到: " + foreignAmountStr + currencyName + "对人民币" + cnyPerForeignStr + "元");

                // 标准化币种名称
                currencyName = normalizeCurrencyName(currencyName);

                // 计算100外币对应多少人民币
                ExchangeRate rate = createRateFromForeignToCNY(
                        currencyName, foreignAmountStr, cnyPerForeignStr, exchangeDate);

                if (rate != null) {
                    result.add(rate);
                    System.out.println("转换后: 100" + currencyName + " = " + rate.getPrice() + " 人民币");
                }

            } catch (Exception e) {
                System.err.println("解析汇率失败(外币->人民币): " + matcher.group());
                e.printStackTrace();
            }
        }
    }

    /**
     * 解析"人民币1元对外币X"格式
     * 例如：人民币1元对1.1354澳门元
     * 人民银行：1人民币 = 1.1354澳门元
     * 你需要：100澳门元 = ? 人民币
     * 计算：100 / 1.1354 = 88.07
     */
    private static void parseCNYToForeign(String text, List<ExchangeRate> result, Date exchangeDate) {
        // 匹配模式：人民币+1元+对+数字+币种名称
        Pattern pattern = Pattern.compile(
                "人民币\\s*1\\s*元对\\s*(\\d+(?:\\.\\d+)?)\\s*([^元，。\\s]+)(?:元)?"
        );

        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            try {
                String foreignPerCNYStr = matcher.group(1);  // 1人民币对应的外币数
                String currencyName = matcher.group(2).trim(); // 币种名称

                System.out.println("解析到: 人民币1元对" + foreignPerCNYStr + currencyName);

                // 标准化币种名称
                currencyName = normalizeCurrencyName(currencyName);

                // 计算100外币对应多少人民币
                ExchangeRate rate = createRateFromCNYToForeign(
                        currencyName, foreignPerCNYStr, exchangeDate);

                if (rate != null) {
                    result.add(rate);
                    System.out.println("转换后: 100" + currencyName + " = " + rate.getPrice() + " 人民币");
                }

            } catch (Exception e) {
                System.err.println("解析汇率失败(人民币->外币): " + matcher.group());
                e.printStackTrace();
            }
        }
    }

    /**
     * 从"1外币对人民币X元"格式创建汇率对象
     * 计算公式：100外币 = 100 * (1外币对应的人民币数)
     */
    private static ExchangeRate createRateFromForeignToCNY(String currencyName,
                                                           String foreignAmountStr,
                                                           String cnyPerForeignStr,
                                                           Date exchangeDate) {
        try {
            // 获取币种代码和symbol
            String currencyCode = CURRENCY_CODE_MAP.get(currencyName);
            String symbol = CURRENCY_SYMBOL_MAP.get(currencyName);

            if (currencyCode == null || symbol == null) {
                System.out.println("未知币种(外币->人民币): " + currencyName);
                return null;
            }

            // 解析数值
            BigDecimal foreignAmount = new BigDecimal(foreignAmountStr);  // 通常是1或100
            BigDecimal cnyPerForeign = new BigDecimal(cnyPerForeignStr);  // 1外币对应的人民币

            BigDecimal price;

            if (foreignAmount.compareTo(new BigDecimal(100)) >= 0) {
                // 格式：100外币 = X人民币
                // 需要：100外币 = X 人民币（直接使用）
                price = cnyPerForeign;
            } else if(foreignAmount.compareTo(new BigDecimal(1)) >= 0){
                // 格式：1外币 = X人民币
                // 需要：100外币 = 100 * X 人民币
                price = cnyPerForeign.multiply(new BigDecimal("100"));
            }else{
                price = cnyPerForeign.multiply(new BigDecimal("100"));
            }

            return createExchangeRate(currencyCode, symbol, price, exchangeDate);

        } catch (Exception e) {
            System.err.println("创建汇率对象失败(外币->人民币): " + currencyName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 从"人民币1元对外币X"格式创建汇率对象
     * 计算公式：100外币 = 100 / (1人民币对应的外币数)
     */
    private static ExchangeRate createRateFromCNYToForeign(String currencyName,
                                                           String foreignPerCNYStr,
                                                           Date exchangeDate) {
        try {
            // 获取币种代码和symbol
            String currencyCode = CURRENCY_CODE_MAP.get(currencyName);
            String symbol = CURRENCY_SYMBOL_MAP.get(currencyName);

            if (currencyCode == null || symbol == null) {
                System.out.println("未知币种(人民币->外币): " + currencyName);
                return null;
            }

            // 人民币1元对外币X，则1外币 = 1/X 人民币
            // 100外币 = 100 / X 人民币
            BigDecimal foreignPerCNY = new BigDecimal(foreignPerCNYStr);
            BigDecimal price = new BigDecimal("100").divide(foreignPerCNY, 6, RoundingMode.HALF_UP);

            return createExchangeRate(currencyCode, symbol, price, exchangeDate);

        } catch (Exception e) {
            System.err.println("创建汇率对象失败(人民币->外币): " + currencyName);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 创建ExchangeRate对象
     */
    private static ExchangeRate createExchangeRate(String currencyCode, String symbol,
                                                   BigDecimal price, Date exchangeDate) {
        ExchangeRate exchangeRate = new ExchangeRate();
        exchangeRate.setName(currencyCode);
        exchangeRate.setSymbol(symbol);
        exchangeRate.setPrice(price);
        exchangeRate.setType("CNY(100)/" + currencyCode);
        exchangeRate.setUtctime(exchangeDate);
        return exchangeRate;
    }

    /**
     * 从文本中提取日期
     */
    private static Date extractDateFromText(String text) {
        try {
            // 匹配日期格式，例如：2025年12月12日
            Pattern pattern = Pattern.compile("(\\d{4})年(\\d{1,2})月(\\d{1,2})日");
            Matcher matcher = pattern.matcher(text);

            if (matcher.find()) {
                String year = matcher.group(1);
                String month = String.format("%02d", Integer.parseInt(matcher.group(2)));
                String day = String.format("%02d", Integer.parseInt(matcher.group(3)));

                String dateStr = year + "-" + month + "-" + day + " 00:00:00";
                return GeneralUtil.getDatez(dateStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 标准化币种名称
     */
    private static String normalizeCurrencyName(String currencyName) {
        currencyName = currencyName.trim();

        // 特殊处理一些可能的变体
        Map<String, String> nameMapping = new HashMap<>();
        nameMapping.put("澳元", "澳大利亚元");
        nameMapping.put("纽元", "新西兰元");
        nameMapping.put("坡元", "新加坡元");
        nameMapping.put("瑞郎", "瑞士法郎");
        nameMapping.put("加元", "加拿大元");
        nameMapping.put("港币", "港元");
        nameMapping.put("马币", "林吉特");
        nameMapping.put("卢布", "俄罗斯卢布");
        nameMapping.put("兰特", "南非兰特");
        nameMapping.put("韩币", "韩元");
        nameMapping.put("迪拉姆", "阿联酋迪拉姆");
        nameMapping.put("里亚尔", "沙特里亚尔");
        nameMapping.put("福林", "匈牙利福林");
        nameMapping.put("兹罗提", "波兰兹罗提");
        nameMapping.put("丹麦克朗", "丹麦克朗");
        nameMapping.put("瑞典克朗", "瑞典克朗");
        nameMapping.put("挪威克朗", "挪威克朗");
        nameMapping.put("里拉", "土耳其里拉");
        nameMapping.put("比索", "墨西哥比索");
        nameMapping.put("铢", "泰铢");

        // 检查是否有匹配的变体
        for (Map.Entry<String, String> entry : nameMapping.entrySet()) {
            if (currencyName.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        // 检查是否直接匹配
        if (CURRENCY_CODE_MAP.containsKey(currencyName)) {
            return currencyName;
        }

        // 尝试通过包含关系匹配
        for (String key : CURRENCY_CODE_MAP.keySet()) {
            if (key.contains(currencyName) || currencyName.contains(key)) {
                return key;
            }
        }

        System.out.println("未匹配的币种名称: " + currencyName);
        return currencyName;
    }

    /**
     * 测试方法
     */
    public static void main(String[] args) {
        String text = "中国人民银行授权中国外汇交易中心公布，2025年12月12日银行间外汇市场人民币汇率中间价为1美元对人民币7.0638元，1欧元对人民币8.2842元，100日元对人民币4.5344元，1港元对人民币0.90779元，1英镑对人民币9.4468元，1澳大利亚元对人民币4.6996元，1新西兰元对人民币4.0973元，1新加坡元对人民币5.4632元，1瑞士法郎对人民币8.8751元，1加拿大元对人民币5.1203元，人民币1元对1.1354澳门元，人民币1元对0.58198林吉特，人民币1元对11.3115俄罗斯卢布，人民币1元对2.3897南非兰特，人民币1元对208.68韩元，人民币1元对0.52070阿联酋迪拉姆，人民币1元对0.53204沙特里亚尔，人民币1元对46.2421匈牙利福林，人民币1元对0.51022波兰兹罗提，人民币1元对0.9020丹麦克朗，人民币1元对1.3114瑞典克朗，人民币1元对1.4271挪威克朗，人民币1元对6.04510土耳其里拉，人民币1元对2.5557墨西哥比索，人民币1元对4.4861泰铢。";

        List<ExchangeRate> rates = parseExchangeRateText(text);

        System.out.println("\n=== 解析结果 ===");
        System.out.println("总计: " + rates.size() + " 种汇率");

        for (ExchangeRate rate : rates) {
            System.out.printf("%-10s %-10s 100%s = %10.4f 人民币\n",
                    rate.getName(),
                    rate.getSymbol(),
                    rate.getSymbol(),
                    rate.getPrice().doubleValue());
        }

        // 验证计算结果
        System.out.println("\n=== 验证计算 ===");
        System.out.println("1. 美元: 1美元 = 7.0638人民币");
        System.out.println("   100美元 = 100 * 7.0638 = " + (100 * 7.0638) + " 人民币");

        System.out.println("2. 日元: 100日元 = 4.5344人民币");
        System.out.println("   100日元 = " + 4.5344 + " 人民币");

        System.out.println("3. 澳门元: 1人民币 = 1.1354澳门元");
        System.out.println("   1澳门元 = 1 / 1.1354 = " + (1 / 1.1354) + " 人民币");
        System.out.println("   100澳门元 = 100 / 1.1354 = " + (100 / 1.1354) + " 人民币");
    }
}