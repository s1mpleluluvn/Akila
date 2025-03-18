package com.akila.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Value;

@Configuration
@EntityScan("com.akila.entity")
@EnableJpaRepositories(basePackages = "com.akila.repository")
public class BeanConfig {

    @Value("${proxy.host}")
    private String proxyServerHost;

    @Value("${proxy.port}")
    private int proxyServerPort;

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .setConnectTimeout(Duration.ofMillis(10000))
                .build();
    }

    @Bean
    public RestTemplate restTemplateWithProxy(RestTemplateBuilder builder) {

        HttpHost httpHost = new HttpHost(proxyServerHost, proxyServerPort);

        HttpClientBuilder clientBuilder = HttpClientBuilder.create();
        clientBuilder.setProxy(httpHost);

        HttpClient httpClient = clientBuilder.build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        // factory.setHttpClient(httpClient);
        factory.setConnectionRequestTimeout(10000);
        factory.setConnectTimeout(10000);
        //factory.setReadTimeout(30000);

        return builder.requestFactory(() -> factory).build();
    }

    @Bean
    public RestTemplate ignoreVerifyHostRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();

        var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(socketFactory).build();
        var httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

        return new RestTemplateBuilder() {
            @Override
            public ClientHttpRequestFactory buildRequestFactory() {
                return new HttpComponentsClientHttpRequestFactory(
                        httpClient);
            }
        }.build();
    }

    @Bean
    public RestTemplate ignoreVerifyHostRestTemplateForRegister() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

        SSLContext sslContext = SSLContexts.custom()
                .loadTrustMaterial(null, (x509Certificates, s) -> true)
                .build();

        var socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
        HttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create().setSSLSocketFactory(socketFactory).build();
        var httpClient = HttpClients.custom().setConnectionManager(connectionManager).build();

        return new RestTemplateBuilder() {
            @Override
            public ClientHttpRequestFactory buildRequestFactory() {
                return new HttpComponentsClientHttpRequestFactory(
                        httpClient);
            }
        }.setConnectTimeout(Duration.ofMillis(5000))
                .build();
    }

    @Bean
    public RestTemplate restTemplateWithNoRedirect(RestTemplateBuilder builder) {
        var restTemplateWithNoRedirect = builder
                .setConnectTimeout(Duration.ofMillis(10000))
                .build();

        final var factory = new HttpComponentsClientHttpRequestFactory();

        var build = HttpClientBuilder.create().disableRedirectHandling().build();
        factory.setHttpClient(build);
        restTemplateWithNoRedirect.setRequestFactory(factory);
        return restTemplateWithNoRedirect;
    }

    @Bean
    public RestTemplate customizedConverterRestTemplate(RestTemplateBuilder builder) {
        var customizedConverterRestTemplate = builder
                .setConnectTimeout(Duration.ofMillis(10000))
                .build();

        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));
        messageConverters.add(converter);
        customizedConverterRestTemplate.setMessageConverters(messageConverters);

        return customizedConverterRestTemplate;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry
//                        .addMapping("/**").allowedMethods("GET", "POST", "PUT", "DELETE").allowedOrigins("*")
//                        .allowedHeaders("*");
//            }
//        };
//    }
    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return Jackson2ObjectMapperBuilder
                .json()
                .modules(new JavaTimeModule())
                .featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
    }
}
