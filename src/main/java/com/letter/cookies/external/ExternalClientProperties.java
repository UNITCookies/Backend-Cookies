package com.letter.cookies.external;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@AllArgsConstructor
@ConstructorBinding
@ConfigurationProperties("client")
public class ExternalClientProperties {

    private final KakaoMapApi kakaoMapApi;

    @Getter
    @AllArgsConstructor
    public static class KakaoMapApi {
        private final String baseUrl;
        private final String key;
    }
}
