package com.wimoor.amazon.adv.common.pojo;

import java.io.IOException;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class JsonBigIntergeSerializer extends JsonSerializer<BigInteger> {

	@Override
	public void serialize(BigInteger value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		// TODO Auto-generated method stub
		if(value != null) {
			gen.writeString(value.toString());
		}else {
			gen.writeString("");
		}
	}
}
 