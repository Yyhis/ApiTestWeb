package net.yyhis.apitester.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import lombok.extern.slf4j.Slf4j;
import net.yyhis.apitester.dto.ApiResponse;

@Slf4j
@Component
public class HttpUtil {
    private static RestClient restClient = RestClient.create();
    
    // 기본 헤더 설정
    public static HttpHeaders setDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.add("Content-Type", "application/json");

        return headers;
    }
    
    // GET 요청 메소드
    public static ApiResponse getApiResponse(String urlString, HttpHeaders headers) {
        long startTime = System.nanoTime(); // 실행 시간 측정 시작

        try {
            ResponseEntity<String> responseEntity = restClient.get()
                .uri(parseURI(urlString))
                .headers(httpHeaders -> headers.forEach(httpHeaders::addAll))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> { 
                    throw new RuntimeException("GET API 호출 실패: " + response.getStatusCode()); 
                }).toEntity(String.class);

            // TODO: 측정 util
            long executionTime = System.nanoTime() - startTime; // 실행 시간 측정 종료
            
            int statusCode = responseEntity.getStatusCode().value();
            String responseBody = responseEntity.getBody();

            byte[] responseBytes = Objects.toString(responseBody, "").getBytes(StandardCharsets.UTF_8);
            long byteSize = responseBytes.length;

            return new ApiResponse(responseBody, statusCode, byteSize, executionTime);

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    }

    // POST 요청 메소드
    // TODO: body 부분 구체화하기
    public static ApiResponse postAPIResponse(String urlString, HttpHeaders headers, Map body) {
        long startTime = System.nanoTime(); // 실행 시간 측정 시작

        try {
            ResponseEntity<String> responseEntity = restClient.post()
                .uri(parseURI(urlString))
                .headers(httpHeaders -> headers.forEach((key, value) -> httpHeaders.addAll(key, value)))
                .body(body)
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, response) -> { 
                    throw new RuntimeException("POST API 호출 실패: " + response.getStatusCode() + " " + response.getBody().toString()); 
                }).toEntity(String.class);

            long executionTime = System.nanoTime() - startTime; // 실행 시간 측정 종료
            
            int statusCode = responseEntity.getStatusCode().value();
            String responseBody = responseEntity.getBody();

            byte[] responseBytes = Objects.toString(responseBody, "").getBytes(StandardCharsets.UTF_8);
            long byteSize = responseBytes.length;

            return new ApiResponse(responseBody, statusCode, byteSize, executionTime);

        } catch (RestClientException e) {
            e.printStackTrace();
            return null;
        }
    } 

    public static URI parseURI(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Invalid URL format");
        }
    }
}
