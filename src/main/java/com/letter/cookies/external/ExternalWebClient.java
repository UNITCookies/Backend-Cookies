


package com.letter.cookies.external;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.client.reactive.ReactorResourceFactory;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Slf4j
@Configuration
@EnableConfigurationProperties(ExternalClientProperties.class)
public class ExternalWebClient {

    public HttpClient httpClient(int connectTimeout, int readTimeout, int writeTimeout) {
        return HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout))
                );
    }

//    @Bean
//    public ReactorResourceFactory resourceFactory() {
//        ReactorResourceFactory factory = new ReactorResourceFactory();
//        factory.setUseGlobalResources(false);
//        return factory;
//    }

    @Bean
    public WebClient kakaoMapApiWebClient(ExternalClientProperties externalClientProperties) {
        return WebClient.builder()
                .baseUrl(externalClientProperties.getKakaoMapApi().getBaseUrl())
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("Authorization", externalClientProperties.getKakaoMapApi().getKey())
                .clientConnector(
                        new ReactorClientHttpConnector(httpClient(1000, 5, 5))
                )
//                .filter(logRequest())
//                .filter(logResponse())
                .build();
    }

    // 클라이언트 필터 등록
    private static ExchangeFilterFunction logRequest() {
        return ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
            log.info("externalApiWebClient Request: url: {}, method: {}", clientRequest.url(), clientRequest.method());
            return Mono.just(clientRequest);
        });
    }

    private static ExchangeFilterFunction logResponse() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            logStatus(clientResponse);
            return logBody(clientResponse);
        });
    }

    private static void logStatus(ClientResponse clientResponse) {
        HttpStatus httpStatus = clientResponse.statusCode();
        log.info("externalApiWebClient status code {} ({})", httpStatus.value(), httpStatus.getReasonPhrase());
    }

    private static Mono<ClientResponse> logBody(ClientResponse clientResponse) {
        if (clientResponse.statusCode().is4xxClientError() || clientResponse.statusCode().is5xxServerError()) {
            return clientResponse.bodyToMono(String.class)
                    .flatMap(body -> {
                        log.info("externalApiWebClient response body {}", body);
                        return Mono.just(clientResponse);
                    });
        } else {
            return Mono.just(clientResponse);
        }
    }
}