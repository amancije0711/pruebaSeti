package com.acme.pedidos.client;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Component
@Profile("prod")
public class SoapClientImpl implements SoapClient {

    private final WebClient webClient;

    public SoapClientImpl(@Value("${acme.soap.url}") String soapUrl) throws SSLException {
        SslContext sslContext = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE)
                .build();

        HttpClient httpClient = HttpClient.create()
                .secure(spec -> spec.sslContext(sslContext));

        this.webClient = WebClient.builder()
                .baseUrl(soapUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .defaultHeader("Content-Type", "text/xml; charset=utf-8")
                .defaultHeader("SOAPAction", "")
                .build();
    }

    @Override
    public String enviarPedido(String soapXml) {
        log.info("Enviando petición SOAP al servicio externo");
        return webClient.post()
                .bodyValue(soapXml)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorMap(e -> new RuntimeException("Error al comunicarse con el servicio SOAP: " + e.getMessage(), e))
                .block();
    }
}
