package com.wimoor.common.mvc;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
 
public class MyStringJsonSerializer extends JsonSerializer<String> {
	ObjectMapper mapper;
    FileUpload fileUpload;
	public MyStringJsonSerializer() {
		mapper = new ObjectMapper();
	}

	public MyStringJsonSerializer(ObjectMapper mapper,FileUpload fileUpload) {
		this.mapper = mapper;
		this.fileUpload=fileUpload;
	}


	@Override
	public void serialize(String value, JsonGenerator gen, SerializerProvider serializers)
			throws IOException, JsonProcessingException {
		if (value != null) {
			if(gen.getOutputContext().getCurrentName()!=null)
				if("image".equals(gen.getOutputContext().getCurrentName())||
				   "location".equals(gen.getOutputContext().getCurrentName())) {
				    value=fileUpload.getPictureImage(value);
			}
		} 
		gen.writeString(value);
	}
}
