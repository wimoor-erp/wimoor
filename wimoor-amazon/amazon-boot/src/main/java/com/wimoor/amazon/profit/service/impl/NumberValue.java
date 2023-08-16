package com.wimoor.amazon.profit.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.googlecode.aviator.runtime.function.AbstractVariadicFunction;
import com.googlecode.aviator.runtime.function.FunctionUtils;
import com.googlecode.aviator.runtime.type.AviatorDecimal;
import com.googlecode.aviator.runtime.type.AviatorObject;

public class NumberValue extends AbstractVariadicFunction {

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "numberOfValue";
	}

    @Override
    public AviatorObject variadicCall(Map<String, Object> env, AviatorObject... args) {
         AviatorObject each =args[0];
         AviatorObject num =args[1];
            if(each.getValue(env) == null){
            	   return new AviatorDecimal(new BigDecimal(0));
            }
            if(num.getValue(env) == null){
         	   return new AviatorDecimal(new BigDecimal(0));
            }
              double value = FunctionUtils.getNumberValue(each, env).doubleValue();
              double value2 = FunctionUtils.getNumberValue(num, env).doubleValue();
              int number=0;
            while(value>value2) {
            	value=value-value2;
            	number=number+1;
            }
        return new AviatorDecimal(new BigDecimal(number));
    }

}
