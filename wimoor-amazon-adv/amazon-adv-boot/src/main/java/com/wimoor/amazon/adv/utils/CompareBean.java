package com.wimoor.amazon.adv.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;

public class CompareBean {

        private int value;

        // 省略构造方法和其他方法

        public static boolean isContentEqual(Object obj,Object other) throws IllegalAccessException {
            if (obj == other) {
                return true;
            }
            if (obj == null || other.getClass() != obj.getClass()) {
                return false;
            }
            Field[] fields = other.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object thisValue = field.get(obj);
                Object otherValue = field.get(other);
                if (!isEqual(thisValue, otherValue)) {
                    return false;
                }
            }
            return true;
        }

        private static boolean isEqual(Object obj1, Object obj2) {
            if (obj1 == null && obj2 == null) {
                return true;
            }
            if (obj1 == null || obj2 == null) {
                return false;
            }
            if(obj1 instanceof String){
                return obj1.equals(obj2);
            }else if(obj1 instanceof Integer){
                return obj1==obj2;
            }else if(obj1 instanceof Long){
                return  obj1==obj2;
            }else if(obj1 instanceof BigDecimal){
                return ((BigDecimal) obj1).compareTo((BigDecimal) obj2)==0;
            }else if(obj1 instanceof Date){
                DateFormat dateFormat = DateFormat.getDateInstance();
                return dateFormat.format(obj1).equals(dateFormat.format(obj2));
            }else{
                return obj1.equals(obj2);
            }
        }

}
