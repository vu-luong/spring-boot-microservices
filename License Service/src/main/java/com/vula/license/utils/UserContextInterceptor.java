package com.vula.license.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static com.vula.license.utils.UserContext.AUTH_TOKEN;
import static com.vula.license.utils.UserContext.CORRELATION_ID;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(UserContextInterceptor.class);

    @Override
    public ClientHttpResponse intercept(
        HttpRequest request,
        byte[] body,
        ClientHttpRequestExecution execution
    ) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(
            CORRELATION_ID,
            UserContextHolder.getContext()
                .getCorrelationId()
        );
        headers.add(
            AUTH_TOKEN,
            UserContextHolder.getContext()
                .getAuthToken()
        );

        return execution.execute(request, body);
    }
}
