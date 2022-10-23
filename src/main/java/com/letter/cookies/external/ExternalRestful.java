package com.letter.cookies.external;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class ExternalRestful {

    private final WebClient externalApiWebClient;

    public String conversion(String x, String y) {

        // GET https://dapi.kakao.com/v2/local/geo/coord2address.json?x=%7B%7D&y=%7B%7D
        String uri = "?x=" + y + "&y=" + x;

        // {"error":"parameter error","invalid parameters":{"x":"{}","y":"{}"}}
        // 카카오맵 좌표로 주소 검색 시 x = 경도(y), y = 위도(x) 로 파라미터를 넘겨줘야 함
        Mono<String> result = externalApiWebClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchangeToMono(clientResponse -> {
                    return clientResponse.bodyToMono(String.class);
                });
        return result.block();
    }

    public String getRegionAddress(String jsonString) {

        JSONObject jsonObject = (JSONObject) JSONValue.parse(jsonString);
        JSONObject jsonObjectMeta = (JSONObject) jsonObject.get("meta");
        long size = (long) jsonObjectMeta.get("total_count");

        // size 가 1 >= 1 : 가장 첫 번째 주소를 제목으로 설정
        String targetAddressName = "";
        if (size > 0) {
            JSONArray jsonArray = (JSONArray) jsonObject.get("documents");
            JSONObject tmpJsonObject = (JSONObject) jsonArray.get(0);
            JSONObject roadAddress = (JSONObject) tmpJsonObject.get("address");
            String addressName = (String) roadAddress.get("address_name");
            String[] addressNameArr = addressName.split("\\s+");
            targetAddressName = addressNameArr[1] + " " + addressNameArr[2] + " " + addressNameArr[3];
        }
        return targetAddressName;
    }
}