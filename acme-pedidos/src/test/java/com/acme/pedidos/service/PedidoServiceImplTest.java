package com.acme.pedidos.service;

import com.acme.pedidos.client.SoapClient;
import com.acme.pedidos.mapper.SoapMapper;
import com.acme.pedidos.model.request.EnviarPedidoRequest;
import com.acme.pedidos.model.response.EnviarPedidoResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PedidoServiceImplTest {

    @Mock
    private SoapClient soapClient;

    @Mock
    private SoapMapper soapMapper;

    @InjectMocks
    private PedidoServiceImpl pedidoService;

    @Test
    void enviarPedido_flujoCompleto_debeRetornarRespuesta() {
        EnviarPedidoRequest request = buildRequest();
        String soapXml = "<soapenv:Envelope>...</soapenv:Envelope>";
        String soapResponse = "<soapenv:Envelope><Codigo>80375472</Codigo></soapenv:Envelope>";
        EnviarPedidoResponse expectedResponse = EnviarPedidoResponse.builder()
                .codigoEnvio("80375472")
                .estado("Entregado exitosamente al cliente")
                .build();

        when(soapMapper.toSoapXml(request)).thenReturn(soapXml);
        when(soapClient.enviarPedido(soapXml)).thenReturn(soapResponse);
        when(soapMapper.fromSoapXml(soapResponse)).thenReturn(expectedResponse);

        EnviarPedidoResponse result = pedidoService.enviarPedido(request);

        assertThat(result.getCodigoEnvio()).isEqualTo("80375472");
        assertThat(result.getEstado()).isEqualTo("Entregado exitosamente al cliente");
        verify(soapMapper).toSoapXml(request);
        verify(soapClient).enviarPedido(soapXml);
        verify(soapMapper).fromSoapXml(soapResponse);
    }

    @Test
    void enviarPedido_cuandoSoapClientFalla_debePropagar() {
        EnviarPedidoRequest request = buildRequest();

        when(soapMapper.toSoapXml(any())).thenReturn("<xml/>");
        when(soapClient.enviarPedido(anyString())).thenThrow(new RuntimeException("Timeout"));

        assertThatThrownBy(() -> pedidoService.enviarPedido(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Timeout");
    }

    private EnviarPedidoRequest buildRequest() {
        return EnviarPedidoRequest.builder()
                .numPedido("75630275")
                .cantidadPedido("1")
                .codigoEAN("00110000765191002104587")
                .nombreProducto("Armario INVAL")
                .numDocumento("1113987400")
                .direccion("CR 72B 45 12 APT 301")
                .build();
    }
}
