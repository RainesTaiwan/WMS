package com.fw.wcs.core.utils;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class HttpUtil {

    public static ResponseEntity callPost(String url, String requestBody ){
        RestTemplate httpClient = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.parseMediaType( "application/x-www-form-urlencoded; charset=UTF-8" ) );
        HttpEntity<String> entity = new HttpEntity<String>( requestBody, headers );
        ResponseEntity<String> responseEntity = httpClient.exchange( url, HttpMethod.POST, entity, String.class );
        return responseEntity;
    }
}
