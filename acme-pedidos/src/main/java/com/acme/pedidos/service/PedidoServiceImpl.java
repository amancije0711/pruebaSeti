package com.acme.pedidos.service;

import com.acme.pedidos.client.SoapClient;
import com.acme.pedidos.mapper.SoapMapper;
import com.acme.pedidos.model.request.EnviarPedidoRequest;
import com.acme.pedidos.model.response.EnviarPedidoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PedidoServiceImpl implements PedidoService {

    private final SoapClient soapClient;
    private final SoapMapper soapMapper;

    @Override
    public EnviarPedidoResponse enviarPedido(EnviarPedidoRequest request) {
        log.info("Procesando pedido: {}", request.getNumPedido());

        String soapRequest = soapMapper.toSoapXml(request);
        log.debug("SOAP Request generado: {}", soapRequest);

        String soapResponse = soapClient.enviarPedido(soapRequest);
        log.debug("SOAP Response recibido: {}", soapResponse);

        EnviarPedidoResponse response = soapMapper.fromSoapXml(soapResponse);
        log.info("Pedido procesado. CodigoEnvio: {}", response.getCodigoEnvio());

        return response;
    }
}
