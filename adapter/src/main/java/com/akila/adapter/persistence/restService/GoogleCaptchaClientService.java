package com.akila.adapter.persistence.restService;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Log4j2
@Component
public class GoogleCaptchaClientService {

    private static final String RECAPTCHA_VERIFY_ENDPOINT = "https://www.google.com/recaptcha/api/siteverify";

    private final RestTemplate restTemplate;
    private final String reCaptchaSecret;

    public GoogleCaptchaClientService(RestTemplate restTemplate,
            @Value("${google.recaptcha.secretKey}") String reCaptchaSecret) {
        this.restTemplate = restTemplate;
        this.reCaptchaSecret = reCaptchaSecret;
    }

    public boolean verifyCaptcha(String token) {
        // request body parameters
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("secret", this.reCaptchaSecret);
        map.add("response", token);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        var response = this.restTemplate.postForEntity(
                RECAPTCHA_VERIFY_ENDPOINT,
                request,
                GoogleCaptchaClientService.ReCaptchaVerifyResponse.class);
        if (response.getStatusCode() != HttpStatus.OK) {
            log.warn("reCaptcha returns status code {}", response.getStatusCode());
            return false;
        }

        if (response.getBody() == null) {
            log.warn("reCaptcha returns empty body");
            return false;
        }

        return response.getBody().isSuccess();
    }

    @Getter
    @Setter
    static class ReCaptchaVerifyResponse {

        @JsonProperty("success")
        private boolean success;

        @JsonProperty("challenge_ts")
        private OffsetDateTime timestamp;

        @JsonProperty("error-codes")
        private List<String> errorCodes;
    }
}
