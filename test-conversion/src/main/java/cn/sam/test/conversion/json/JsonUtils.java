package cn.sam.test.conversion.json;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class JsonUtils {

	private static final ObjectMapper MAPPER;

	private static final Gson GSON;
	
	static {
		MAPPER = new ObjectMapper();
		GSON = new GsonBuilder().serializeNulls().create();
	}
	
	public static String toJson(Object o) throws JsonProcessingException {
		return MAPPER.writeValueAsString(o);
	}
	
	public static String toJsonSilent(Object o) {
		try {
			return toJson(o);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String toJsonByGson(Object o) {
		return GSON.toJson(o);
	}
	
	public static <T> T toObject(String json, Class<T> c) throws JsonParseException, JsonMappingException, IOException {
		if (StringUtils.isBlank(json)) {
			return null;
		}
		return MAPPER.readValue(json, c);
	}
	
	public static <T> T toObjetByGson(String json, Class<T> classOfT) {
		return GSON.fromJson(json, classOfT);
	}
	
	public static <T> T toObjetByGson(String json, Type typeOfT) {
		return GSON.fromJson(json, typeOfT);
	}
	
	public static void main(String[] args) throws Exception {
//		String json = "{\"baseResp\":{\"seqNo\":null,\"respCode\":null,\"respDesc\":null},\"orderCode\":\"T05170150959410\",\"transactionSeq\":\"abc\",\"totalAmount\":1584.0,\"amount\":1604.5,\"orders\":[{\"baseResp\":{\"seqNo\":null,\"respCode\":null,\"respDesc\":null},\"orderCode\":\"10801150959410\",\"agentCode\":\"0002\",\"createdDate\":\"Nov 2, 2017 11:41:41 AM\",\"receiver\":\"张三\",\"provinceName\":\"广东省\",\"provinceLogisticCode\":\"440000000000\",\"cityName\":\"广州市\",\"cityLogisticCode\":\"440100000000\",\"districtName\":\"天河区\",\"districtLogisticCode\":\"440106000000\",\"receiverAddress\":\"广东省广州市天河区长兴路297号\",\"zip\":null,\"contactMobile\":\"13570299386\",\"contactPhone\":\"02081234567\",\"idCardType\":0,\"idCardNum\":\"532622197605210845\",\"idCardOwner\":\"张三\",\"invoiceTitle\":\"发票\",\"invoiceType\":41,\"invoiceContent\":\"创建一个发票\",\"totalAmount\":1184.0,\"amount\":1204.5,\"remark\":\"跨境订单测试\",\"insuredFee\":null,\"freight\":null,\"expressCompanyCode\":null,\"payStyle\":8,\"transactionSeq\":\"abc\",\"customsId\":\"2001\",\"ciqId\":\"1001\",\"tax\":20.5,\"busiMode\":null,\"entRegCodeOfCiq\":\"1500000192\",\"entRegCodeOfAgent\":\"1000000005\",\"entRegCodeOfCustoms\":\"440126T832\",\"orderDetails\":[{\"sku\":221345,\"productName\":\"澳洲Swisse  胶原蛋白片-C端 100片\",\"quantity\":24,\"regCodeOfCustoms\":\"GDO5165512010000185\",\"regCodeOfCiq\":\"ICIP03151201000155\",\"barCode\":\"9311770588218\",\"regPrice\":67.0,\"consumptionTaxRate\":0.0,\"valueAddedTaxRate\":0.17,\"tariffRate\":0.0,\"price\":45.33,\"goodsNo\":\"01-341-11002\",\"unitOfCiq\":\"142\",\"unitOfCustoms\":\"142\",\"hsCode\":\"3504009000\",\"originalPrice\":299.0,\"modelValueDesc\":null,\"dealPrice\":45.33,\"country\":null},{\"sku\":215968,\"productName\":\"澳洲Swisse 叶绿素片-同时存在集团平台两个商品库C端 100片/瓶\",\"quantity\":1,\"regCodeOfCustoms\":\"GDO5165510300001072\",\"regCodeOfCiq\":\"ICIP03151029000451\",\"barCode\":\"9311770589314\",\"regPrice\":210.0,\"consumptionTaxRate\":0.0,\"valueAddedTaxRate\":0.17,\"tariffRate\":0.0,\"price\":38.8,\"goodsNo\":\"01-339-11002\",\"unitOfCiq\":\"142\",\"unitOfCustoms\":\"142\",\"hsCode\":\"2106909090\",\"originalPrice\":258.0,\"modelValueDesc\":null,\"dealPrice\":38.8,\"country\":null},{\"sku\":221344,\"productName\":\"澳洲Swisse  钙+维生素D片 150片\",\"quantity\":2,\"regCodeOfCustoms\":\"GDO5165512010000184\",\"regCodeOfCiq\":\"ICIP03151201000154\",\"barCode\":\"9311770592505\",\"regPrice\":47.0,\"consumptionTaxRate\":0.0,\"valueAddedTaxRate\":0.17,\"tariffRate\":0.0,\"price\":28.7,\"goodsNo\":\"01-354-11502\",\"unitOfCiq\":\"142\",\"unitOfCustoms\":\"142\",\"hsCode\":\"2936240000\",\"originalPrice\":123.0,\"modelValueDesc\":null,\"dealPrice\":28.7,\"country\":null}],\"orderStatus\":5,\"orderSourceBusiness\":6,\"paidDate\":\"Nov 2, 2017 11:42:18 AM\",\"entRegNameOfCustoms\":null,\"customerId\":32737193,\"mchType\":\"Mama100\",\"points\":0,\"externalOrderId\":\"1588819034\",\"cancelOrderReason\":null}],\"header\":{\"seqNo\":\"151055820165607899\",\"action\":13,\"sourceSystem\":\"WEIXIN\",\"handler\":\"12487\",\"logMemo\":null,\"customerId\":32737193,\"version\":1}}";
//		String json2 = toJson(json);
//		System.out.println(json2);
		
//		SeaAmoyOrderBatchInfoResponse response = toObjetByGson(json, SeaAmoyOrderBatchInfoResponse.class);
//		System.out.println(toJson(response));
		
//		String json = "{\"statusCode\":3,\"successNum\":0,\"message\":\"SUCCESS\",\"errorsInfo\":[{\"resultCode\":\"FAIL\",\"resultMessage\":\"24622148064856:productCode产品编码为空\"}]}";
//		OCResponse r = toObject(json, OCResponse.class);
//		System.out.println(r);
		
		String json = toJson(null);
		System.out.println(json);
		
	}
}