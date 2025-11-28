package com.wimoor.common.service.impl;

 
import java.lang.annotation.*;
@Target({ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})    
@Retention(RetentionPolicy.RUNTIME)    
@Documented    
public  @interface SystemControllerLog {    
    String value()  default "";    
}  