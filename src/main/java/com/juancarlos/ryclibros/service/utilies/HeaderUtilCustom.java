package com.juancarlos.ryclibros.service.utilies;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public final class HeaderUtilCustom {

    private static final Logger log = LoggerFactory.getLogger(tech.jhipster.web.util.HeaderUtil.class);

    private HeaderUtilCustom() {}

    public static HttpHeaders createAlert(String applicationName, String message, String param) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-alert", message);

        try {
            headers.add("X-" + applicationName + "-params", URLEncoder.encode(param, StandardCharsets.UTF_8.toString()));
        } catch (UnsupportedEncodingException var5) {}

        return headers;
    }

    public static HttpHeaders createEntityCreationAlert(
        String applicationName,
        boolean enableTranslation,
        String article,
        String entityName,
        String param
    ) {
        String message = enableTranslation
            ? applicationName + "." + entityName + ".created"
            : "Se creó " + article + " " + entityName + " con identificador " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createEntityUpdateAlert(
        String applicationName,
        boolean enableTranslation,
        String article,
        String entityName,
        String param
    ) {
        String message = enableTranslation
            ? applicationName + "." + entityName + ".updated"
            : "Se actualizó " + article + " " + entityName + " con identificador " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createEntityDeletionAlert(
        String applicationName,
        boolean enableTranslation,
        String article,
        String entityName,
        String param
    ) {
        String message = enableTranslation
            ? applicationName + "." + entityName + ".deleted"
            : "Se eliminó " + article + " " + entityName + " con identificador " + param;
        return createAlert(applicationName, message, param);
    }

    public static HttpHeaders createFailureAlert(
        String applicationName,
        boolean enableTranslation,
        String entityName,
        String errorKey,
        String defaultMessage
    ) {
        log.error("Entity processing failed, {}", defaultMessage);
        String message = enableTranslation ? "error." + errorKey : defaultMessage;
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-" + applicationName + "-error", message);
        headers.add("X-" + applicationName + "-params", entityName);
        return headers;
    }
}
