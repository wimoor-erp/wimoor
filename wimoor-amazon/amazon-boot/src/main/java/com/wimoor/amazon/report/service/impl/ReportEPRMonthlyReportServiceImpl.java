package com.wimoor.amazon.report.service.impl;

import cn.hutool.core.util.StrUtil;
import com.amazon.spapi.model.reports.CreateReportSpecification;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wimoor.amazon.auth.pojo.entity.AmazonAuthority;
import com.wimoor.amazon.auth.pojo.entity.Marketplace;
import com.wimoor.amazon.report.mapper.AmzEprMonthlyReportMapper;
import com.wimoor.amazon.report.pojo.entity.AmazonEprMonthlyReport;
import com.wimoor.amazon.report.pojo.entity.ReportType;
import com.wimoor.amazon.util.AmzDateUtils;
import com.wimoor.common.GeneralUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("reportEPRMonthlyReportService")
public class ReportEPRMonthlyReportServiceImpl extends ReportServiceImpl {
    @Resource
    AmzEprMonthlyReportMapper amzEprMonthlyReportMapper;

    private String getStrValue(String[] info, Map<String, Integer> titleList, String key) {
        key=key.toUpperCase();
        Integer position = titleList.get(key);
        if(position==null)return null;
        if(position<info.length) {
            return info[position];
        }
        return null;
    }
    private String getStrValue(String[] info, Map<String, Integer> titleList, int position) {
        if(position<info.length) {
            return info[position];
        }
        return null;
    }
    private Date getDateValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length) {
            return GeneralUtil.getDatez(info[position]);
        }
        return null;
    }
    private Integer getIntegerValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length && StrUtil.isNumeric(info[position])) {
            return Integer.parseInt(info[position]);
        }
        return null;
    }



    private BigDecimal getBigDecimalValue(String[] info, Map<String, Integer> titleList, String key) {
        Integer position = titleList.get(key.toUpperCase());
        if(position==null)return null;
        if(position<info.length) {
            if(isNumericzidai(info[position])) {
                return new BigDecimal(info[position]);
            }
        }
        return null;
    }

    public static boolean isNumericzidai(String str) {
        if (str == null)
            return false;
        Pattern pattern = Pattern.compile("-?[0-9]+\\.?[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 获取前一个月的第一天
     * @param current 当前日期Calendar对象
     * @return 前一个月第一天的Calendar对象
     */
    public static Calendar getFirstDayOfPreviousMonth(Calendar current) {
        Calendar prevMonth = (Calendar) current.clone();
        // 将月份减1
        prevMonth.add(Calendar.MONTH, -1);
        // 设置为该月第一天
        prevMonth.set(Calendar.DAY_OF_MONTH, 3);
        // 清除时间部分
        clearTime(prevMonth);
        return prevMonth;
    }

    /**
     * 获取前一个月的最后一天
     * @param current 当前日期Calendar对象
     * @return 前一个月最后一天的Calendar对象
     */
    public static Calendar getLastDayOfPreviousMonth(Calendar current) {
        Calendar prevMonth = (Calendar) current.clone();
        // 设置为当前月第一天
        prevMonth.set(Calendar.DAY_OF_MONTH, 1);
        // 减1天得到上个月最后一天
        prevMonth.add(Calendar.DATE, -1);
        // 清除时间部分
        clearTime(prevMonth);
        return prevMonth;
    }

    /**
     * 清除Calendar对象的时间部分
     * @param calendar 要处理的Calendar对象
     */
    private static void clearTime(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }


    public  void   requestReport(AmazonAuthority amazonAuthority,Calendar cstart,Calendar cend,Boolean ignore) {
        List<Marketplace> marketlist = marketplaceService.findbyauth(amazonAuthority.getId());
        boolean iseu=false;
        for(Marketplace market:marketlist) {
            CreateReportSpecification body=new CreateReportSpecification();
            body.setReportType(myReportType());
            body.setDataStartTime(AmzDateUtils.getOffsetDateTimeUTC(cstart));
            body.setDataEndTime(AmzDateUtils.getOffsetDateTimeUTC(cend));
            if(market.getRegion().equals("EU")) {
                if(iseu) {
                    return;
                }else {
                    iseu=true;
                }
            }
            List<String> list=new ArrayList<String>();
            list.add(market.getMarketplaceid());
            amazonAuthority.setMarketPlace(market);
            if(ignore==null||ignore==false) {
                Map<String,Object> param=new HashMap<String,Object>();
                param.put("sellerid", amazonAuthority.getSellerid());
                param.put("reporttype", this.myReportType());
                param.put("marketplacelist", list);
                Date lastupdate= iReportRequestRecordService.lastUpdateRequestByType(param);
                if(lastupdate!=null&& GeneralUtil.distanceOfHour(lastupdate, new Date())<5) {
                    continue;
                }
            }
            body.setMarketplaceIds(list);
            callCreateAPI(this, body, amazonAuthority, market,cstart.getTime(),cend.getTime());
        }
    }

    @Override
    public String treatResponse(AmazonAuthority amazonAuthority, BufferedReader br) {
        int lineNumber = 0;
        String line;
        String mlog="";
        Date startDate=null;
        Map<String,Integer> titleList = new TreeMap<String,Integer>();
        try {
            while ((line = br.readLine()) != null) {
                String[] info = line.split("\t");
                if (lineNumber == 0) {
                    for (Integer i = 0; i < info.length; i++) {
                        titleList.put(info[i].trim().toUpperCase(),i);
                    }
                }else {
                    AmazonEprMonthlyReport report = new AmazonEprMonthlyReport();
                    report.setSellerid(getStrValue(info,titleList,0));
                    report.setReportPeriodStart(getDateValue(info,titleList,"report_period_start"));
                    report.setReportPeriodEnd(getDateValue(info,titleList,"report_period_end"));
                    report.setAsin(getStrValue(info,titleList,"asin"));
                    report.setAmazonMarketplace(getStrValue(info,titleList,"amazon_marketplace"));
                    report.setShipToCountryCode(getStrValue(info,titleList,"ship_to_country_code"));
                    report.setShipToCountry(getStrValue(info,titleList,"ship_to_country"));
                    //report.setItemNameInEnglish(getStrValue(info,titleList,"item_name_in_english"));
                    //report.setItemNameAsInMarketplace(getStrValue(info,titleList,"item_name_as_in_marketplace"));
                    report.setRegistrationNumber(getStrValue(info,titleList,"registration_number"));
                    report.setEprCategory(getStrValue(info,titleList,"epr_category"));
                    report.setEprSubcategory1(getStrValue(info,titleList,"epr_subcategory1"));
                    report.setEprSubcategory2(getStrValue(info,titleList,"epr_subcategory2"));
                    report.setEprSubcategory3(getStrValue(info,titleList,"epr_subcategory3"));
                    report.setEprSubcategory4(getStrValue(info,titleList,"epr_subcategory4"));
                    report.setGlProductGroupDescription(getStrValue(info,titleList,"gl_product_group_description"));
                    report.setProductType(getStrValue(info,titleList,"product_type"));
                    report.setTotalUnitsSold(getIntegerValue(info,titleList,"total_units_sold"));
                    report.setUnitsPerAsin(getIntegerValue(info,titleList,"units_per_asin"));
                    report.setBatteryEmbedded(getStrValue(info,titleList,"battery_embedded"));
                    report.setItemWeightWithoutPackageKg(getBigDecimalValue(info,titleList,"item_weight_without_package_kg"));
                    report.setItemWeightWithPackageKg(getBigDecimalValue(info,titleList,"item_weight_with_package_kg"));
                    report.setTotalReportedWeightKg(getBigDecimalValue(info,titleList,"total_reported_weight_kg"));
                    report.setItemWidthCm(getBigDecimalValue(info,titleList,"item_width_cm"));
                    report.setPackageWidthCm(getBigDecimalValue(info,titleList,"package_width_cm"));
                    report.setItemHeightCm(getBigDecimalValue(info,titleList,"item_height_cm"));
                    report.setPackageHeightCm(getBigDecimalValue(info,titleList,"package_height_cm"));
                    report.setPaperKg(getBigDecimalValue(info,titleList,"paper_kg"));
                    report.setGlassKg(getBigDecimalValue(info,titleList,"glass_kg"));
                    report.setAluminumKg(getBigDecimalValue(info,titleList,"aluminum_kg"));
                    report.setSteelKg(getBigDecimalValue(info,titleList,"steel_kg"));
                    report.setPlasticKg(getBigDecimalValue(info,titleList,"plastic_kg"));
                    report.setWoodKg(getBigDecimalValue(info,titleList,"wood_kg"));
                    report.setOtherKg(getBigDecimalValue(info,titleList,"other_kg"));
                    if(lineNumber==1){
                        LambdaQueryWrapper<AmazonEprMonthlyReport> query=new LambdaQueryWrapper<AmazonEprMonthlyReport>();
                        query.eq(AmazonEprMonthlyReport::getSellerid,amazonAuthority.getSellerid());
                        query.eq(AmazonEprMonthlyReport::getReportPeriodStart,report.getReportPeriodStart());
                        amzEprMonthlyReportMapper.delete(query);
                    }
                    amzEprMonthlyReportMapper.insert(report);
                }
                lineNumber++;
            }
        } catch (NumberFormatException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mlog+=e.getMessage();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            mlog+=e.getMessage();
        } finally {
            if(br!=null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mlog+=e.getMessage();
                }
            }
        }
        return mlog;
    }

    @Override
    public String myReportType() {
        return ReportType.FBAEPRMonthlyReport;
    }

}