package com.wimoor.erp.util;

public class CountryUtil {
    public static String getCountryName(String countryCode) {
        String countryName = "";
        switch (countryCode) {
            case "US":
                countryName = "美国";
                break;
            case "CA":
                countryName = "加拿大";
                break;
            case "AU":
                countryName = "澳大利亚";
                break;
            case "DE":
                countryName = "德国";
                break;
            case "ES":
                countryName = "西班牙";
                break;
            case "FR":
                countryName = "法国";
                break;
            case "SA":
                countryName = "沙特";
                break;
            case "NL":
                countryName = "荷兰";
                break;
            case "SG":
                countryName = "新加坡";
                break;
            case "MX":
                countryName = "墨西哥";
                break;
            case "IT":
                countryName = "意大利";
                break;
            case "BR":
                countryName = "巴西";
                break;
            case "JP":
                countryName = "日本";
                break;
            case "IN":
                countryName = "印度";
                break;
            case "CN":
                countryName = "中国";
                break;
            case "TR":
                countryName = "土耳其";
                break;
            case "PL":
                countryName = "波兰";
                break;
            case "UK":
                countryName = "英国";
                break;
            case "SE":
                countryName = "瑞典";
                break;
            case "AE":
                countryName = "阿联酋";
                break;
            case "BE":
                countryName = "比利时";
                break;
            case "EG":
                countryName = "埃及";
                break;
            case "NZ":
                countryName = "新西兰";
                break;
            default:
                countryName = null;
                break;
        }
        return countryName;
    }

    public static String getCountryCodeEn(String countryName) {
        String countryCode = "";
        switch (countryName.toLowerCase()) {
            case "america":
                countryCode = "US";
                break;
            case "canada":
                countryCode = "CA";
                break;
            case "australia":
                countryCode = "AU";
                break;
            case "germany":
                countryCode = "DE";
                break;
            case "spain":
                countryCode = "ES";
                break;
            case "france":
                countryCode = "FR";
                break;
            case "saudi":
                countryCode = "SA";
                break;
            case "netherlands":
                countryCode = "NL";
                break;
            case "singapore":
                countryCode = "SG";
                break;
            case "mexico":
                countryCode = "MX";
                break;
            case "italy":
                countryCode = "IT";
                break;
            case "brazil":
                countryCode = "BR";
                break;
            case "japan":
                countryCode = "JP";
                break;
            case "india":
                countryCode = "IN";
                break;
            case "china":
                countryCode = "CN";
                break;
            case "turkey":
                countryCode = "TR";
                break;
            case "poland":
                countryCode = "PL";
                break;
            case "england":
                countryCode = "UK";
                break;
            case "sweden":
                countryCode = "SE";
                break;
            case "uae":
                countryCode = "AE";
                break;
            case "belgium":
                countryCode = "BE";
                break;
            case "egypt":
                countryCode = "EG";
                break;
            case "newzealand":
                countryCode = "NZ";
                break;
            default:
                countryCode = null;
                break;

        }
        return countryCode;
    }

    public static String getCountryCode(String countryName) {
        String countryCode = "";
        switch (countryName) {
            case "美国":
                countryCode = "US";
                break;
            case "加拿大":
                countryCode = "CA";
                break;
            case "澳大利亚":
                countryCode = "AU";
                break;
            case "德国":
                countryCode = "DE";
                break;
            case "西班牙":
                countryCode = "ES";
                break;
            case "法国":
                countryCode = "FR";
                break;
            case "沙特":
                countryCode = "SA";
                break;
            case "荷兰":
                countryCode = "NL";
                break;
            case "新加坡":
                countryCode = "SG";
                break;
            case "墨西哥":
                countryCode = "MX";
                break;
            case "意大利":
                countryCode = "IT";
                break;
            case "巴西":
                countryCode = "BR";
                break;
            case "日本":
                countryCode = "JP";
                break;
            case "印度":
                countryCode = "IN";
                break;
            case "中国":
                countryCode = "CN";
                break;
            case "土耳其":
                countryCode = "TR";
                break;
            case "波兰":
                countryCode = "PL";
                break;
            case "英国":
                countryCode = "UK";
                break;
            case "瑞典":
                countryCode = "SE";
                break;
            case "阿联酋":
                countryCode = "AE";
                break;
            case "比利时":
                countryCode = "BE";
                break;
            case "埃及":
                countryCode = "EG";
                break;
            default:
                countryCode = null;
                break;
        }
        return countryCode;
    }

    public static String formatCurrency(String currency) {
        String fcuurency = null;
        if (currency != null) {
            if (currency.equalsIgnoreCase("USD")) {
                fcuurency = "$";
            }
            if (currency.equalsIgnoreCase("GBP")) {
                fcuurency = "£";
            }
            if (currency.equalsIgnoreCase("EUR")) {
                fcuurency = "€";
            }
            if (currency.equalsIgnoreCase("JPY")) {
                fcuurency = "¥";
            }
            if (currency.equalsIgnoreCase("CNY")) {
                fcuurency = "¥";
            }
            if (currency.equalsIgnoreCase("RMB")) {
                fcuurency = "¥";
            }
            if (currency.equalsIgnoreCase("CAD")) {
                fcuurency = "C$";
            }
            if (currency.equalsIgnoreCase("INR")) {
                fcuurency = "₹";
            }
            if (currency.equalsIgnoreCase("AUD")) {
                fcuurency = "A$";
            }
            if (currency.equalsIgnoreCase("MXN")) {
                fcuurency = "Mex$";
            }
            if (currency.equalsIgnoreCase("SEK")) {
                fcuurency = "Kr";
            }
            if (currency.equalsIgnoreCase("SAR")) {
                fcuurency = "S$";
            }
            if (currency.equalsIgnoreCase("AED")) {
                fcuurency = "AE$";
            }
            if (currency.equalsIgnoreCase("PLN")) {
                fcuurency = "zł";
            }
            if (currency.equalsIgnoreCase("TRY")) {
                fcuurency = "₺";
            }

        }
        return fcuurency;
    }

    public static String getMarketname(String mcurrency) {
        String marketname = null;
        if (mcurrency != null) {
            if ("USD".equals(mcurrency)) {
                marketname = "美国";
            } else if ("CAD".equals(mcurrency)) {
                marketname = "加拿大";
            } else if ("GBP".equals(mcurrency)) {
                marketname = "英国";
            } else if ("INR".equals(mcurrency)) {
                marketname = "印度";
            } else if ("JPY".equals(mcurrency)) {
                marketname = "日本";
            } else if ("AUD".equals(mcurrency)) {
                marketname = "澳大利亚";
            } else if ("MXN".equals(mcurrency)) {
                marketname = "墨西哥";
            } else if ("EUR".equals(mcurrency)) {
                marketname = "欧洲（不包含英国）";
            } else if ("AED".equals(mcurrency)) {
                marketname = "阿联酋";
            } else if ("SAR".equals(mcurrency)) {
                marketname = "沙特阿拉伯";
            } else if ("PLN".equals(mcurrency)) {
                marketname = "波兰";
            } else if ("SEK".equals(mcurrency)) {
                marketname = "瑞典";
            }
        }
        return marketname;
    }

    public static String getMarketPlaceId(String mcurrency) {
        String marketname = null;
        if (mcurrency != null) {
            if ("USD".equals(mcurrency)) {
                marketname = "ATVPDKIKX0DER";
            } else if ("CAD".equals(mcurrency)) {
                marketname = "A2EUQ1WTGCTBG2";
            } else if ("GBP".equals(mcurrency)) {
                marketname = "A1F83G8C2ARO7P";
            } else if ("INR".equals(mcurrency)) {
                marketname = "A21TJRUUN4KGV";
            } else if ("JPY".equals(mcurrency)) {
                marketname = "A1VC38T7YXB528";
            } else if ("AUD".equals(mcurrency)) {
                marketname = "A39IBJ37TRP1C6";
            } else if ("MXN".equals(mcurrency)) {
                marketname = "A1AM78C64UM0Y8";
            } else if ("EUR".equals(mcurrency)) {
                marketname = "EU";
            } else if ("AED".equals(mcurrency)) {
                marketname = "A2VIGQ35RCS4UG";
            } else if ("SEK".equals(mcurrency)) {
                marketname = "A2NODRKZP88ZB9";
            } else if ("PLN".equals(mcurrency)) {
                marketname = "A1C3SOZRARQ6R3";
            } else if ("SAR".equals(mcurrency)) {
                marketname = "A17E79C6D8DWNP";
            }
        }
        return marketname;
    }

}
