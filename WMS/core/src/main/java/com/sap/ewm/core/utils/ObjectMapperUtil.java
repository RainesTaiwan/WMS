package com.sap.ewm.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ObjectMapperUtil {

    /**
     * 處理駝峰欄位對映
     *
     * @return
     */
    public static ObjectMapper configureObjectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        objectMapper.configure( MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true );
        objectMapper.setDateFormat( new DynamicDateFormat() );
        return objectMapper;
    }

    /**
     * 處理下劃線欄位對映
     *
     * @return
     */
    public static ObjectMapper configureObjectMapperForUnderscore(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure( DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false );
        objectMapper.configure( MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true );
        objectMapper.setPropertyNamingStrategy( PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES );
        return objectMapper;
    }

    public static <T> T readValueForUnderscore(String json, Class<T> valueType ) throws IOException {
        ObjectMapper objectMapper = configureObjectMapperForUnderscore();
        objectMapper.setDateFormat( new DynamicDateFormat() );
        T value = objectMapper.readValue( json, valueType );
        return value;
    }


    public static <T> T readValueForUnderscore(String json, Class<T> valueType, String dateFormatPattern ) throws IOException {
        ObjectMapper objectMapper = configureObjectMapperForUnderscore();
        objectMapper.setDateFormat( new SimpleDateFormat( dateFormatPattern ) );
        T value = objectMapper.readValue( json, valueType );
        return value;
    }

    public static <T> T readValue(String json, Class<T> valueType ) throws IOException {
        ObjectMapper objectMapper = configureObjectMapper();
        objectMapper.setDateFormat( new DynamicDateFormat() );
        T value = objectMapper.readValue( json, valueType );
        return value;
    }

    public static <T> T convertListForUnderscore(Object value, Class... targetClass ){
        ObjectMapper objectMapper = configureObjectMapperForUnderscore();
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType( List.class, null, targetClass );
        return objectMapper.convertValue( value, javaType );
    }

    public static <T> T convertList(Object value, Class... targetClass ){
        ObjectMapper objectMapper = configureObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType( List.class, null, targetClass );
        return objectMapper.convertValue( value, javaType );
    }

    public static <T> T convertValue( Object fromValue, Class<T> toValueType ){
        ObjectMapper objectMapper = configureObjectMapper();
        return objectMapper.convertValue( fromValue, toValueType );
    }

    public static <T> T convertValue( Object fromValue, JavaType javaType ){
        ObjectMapper objectMapper = configureObjectMapper();
        return objectMapper.convertValue( fromValue, javaType );
    }

    public static String writeValueAsString( Object object ){
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        try {
            return objectWriter.writeValueAsString( object );
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static class DynamicDateFormat extends SimpleDateFormat {

        private String DATE_FORMAT1 = "yyyy-MM-dd HH:mm:quartz.SSS";
        private String DATE_FORMAT2 = "yyyy/MM/dd HH:mm:quartz.SSS";
        private String DATE_FORMAT3 = "yyyy-MM-dd HH:mm:quartz";
        private String DATE_FORMAT4 = "yyyy/MM/dd HH:mm:quartz";
        private String DATE_FORMAT5 = "yyyy-MM-dd";
        private String DATE_FORMAT6 = "yyyy/MM/dd";

        @Override
        public Date parse(String source, ParsePosition pos) {
            Date date = null;
            if( source.length() == 23 ){
                if( source.indexOf( "/" ) > -1 ){
                    super.applyPattern( DATE_FORMAT2 );
                }else if( source.indexOf( "-" ) > -1 ){
                    super.applyPattern( DATE_FORMAT1 );
                }
            }
            if( source.length() == 19 ){
                if( source.indexOf( "/" ) > -1 ){
                    super.applyPattern( DATE_FORMAT4 );
                }else if( source.indexOf( "-" ) > -1 ){
                    super.applyPattern( DATE_FORMAT3 );
                }
            }
            if( source.length() == 10 ){
                if( source.indexOf( "/" ) > -1 ){
                    super.applyPattern( DATE_FORMAT6 );
                }else if( source.indexOf( "-" ) > -1 ){
                    super.applyPattern( DATE_FORMAT5 );
                }
            }
            date = super.parse( source, pos );
            return date;
        }
    }
}