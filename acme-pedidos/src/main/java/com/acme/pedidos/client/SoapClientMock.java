package com.acme.pedidos.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("!prod")
public class SoapClientMock implements SoapClient {

    private static final String MOCK_RESPONSE = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
                <soapenv:Header/>
                <soapenv:Body>
                    <env:EnvioPedidoAcmeResponse>
                        <EnvioPedidoResponse>
                            <Codigo>80375472</Codigo>
                            <Mensaje>Entregado exitosamente al cliente</Mensaje>
                        </EnvioPedidoResponse>
                    </env:EnvioPedidoAcmeResponse>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

    @Override
    public String enviarPedido(String soapXml) {
        log.warn("[MOCK] Cliente SOAP simulado activo. Use el perfil 'prod' para el servicio real.");
        log.debug("[MOCK] XML enviado: {}", soapXml);
        return MOCK_RESPONSE;
    }
}
