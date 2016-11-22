package cn.sam.test.conversion.json.jackson;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cn.sam.test.conversion.bean.Apple;

public class Test {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	
	public static String toJson(Object o) throws JsonProcessingException {
		return MAPPER.writeValueAsString(o);
	}
	
	public static <T> T toObject(String json, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(json, c);
	}

	public static void main(String[] args) throws IOException {
//		System.out.println("null object: " + toJson(null));	// null
//		System.out.println("null json: " + toObject(null, Apple.class));	//NullPointerException
		System.out.println("empty json: " + toObject("", Apple.class));	//JsonMappingException
	}

}
