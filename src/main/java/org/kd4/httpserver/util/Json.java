package org.kd4.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Json {
  private static ObjectMapper objectMapper = defaultObjectMapper();

  private static ObjectMapper defaultObjectMapper() {
    ObjectMapper om = new ObjectMapper();
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    return om;
  }

  public static JsonNode parse(String jsonSrc) throws JsonProcessingException {
    return objectMapper.readTree(jsonSrc);
  }

  public static <A> A fromJson(JsonNode jsonNode, Class<A> clazz) throws JsonProcessingException {
    return objectMapper.treeToValue(jsonNode, clazz);
  }

  public static JsonNode toJson(Object obj) {
    return objectMapper.valueToTree(obj);
  }

  public static String stringify(JsonNode jsonNode) throws JsonProcessingException {
    return generateJson(jsonNode, false);
  }

  public static String stringifyPretty(JsonNode jsonNode) throws JsonProcessingException {
    return generateJson(jsonNode, true);
  }

  private static String generateJson(Object o, boolean pretty) throws JsonProcessingException {
    ObjectWriter objectWriter = objectMapper.writer();
    if (pretty)
      objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
    return objectWriter.writeValueAsString(o);
  }

}
