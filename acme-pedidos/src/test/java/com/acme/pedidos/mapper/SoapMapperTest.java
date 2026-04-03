package com.acme.pedidos.mapper;

import com.acme.pedidos.model.request.EnviarPedidoRequest;
import com.acme.pedidos.model.response.EnviarPedidoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SoapMapperTest {

    private SoapMapper soapMapper;

    @BeforeEach
    void setUp() {
        soapMapper = new SoapMapper();
    }

    @Test
    void toSoapXml_debeContenerTodosLosCampos() {
        EnviarPedidoRequest request = buildRequest();

        String xml = soapMapper.toSoapXml(request);

        assertThat(xml).contains("<pedido>75630275</pedido>");
        assertThat(xml).contains("<Cantidad>1</Cantidad>");
        assertThat(xml).contains("<EAN>00110000765191002104587</EAN>");
        assertThat(xml).contains("<Producto>Armario INVAL</Producto>");
        assertThat(xml).contains("<Cedula>1113987400</Cedula>");
        assertThat(xml).contains("<Direccion>CR 72B 45 12 APT 301</Direccion>");
        assertThat(xml).contains("soapenv:Envelope");
        assertThat(xml).contains("EnvioPedidosAcme");
    }

    @Test
    void toSoapXml_debeEscaparCaracteresEspeciales() {
        EnviarPedidoRequest request = EnviarPedidoRequest.builder()
                .numPedido("123")
                .cantidadPedido("1")
                .codigoEAN("456")
                .nombreProducto("Producto & Especial")
                .numDocumento("789")
                .direccion("Calle <5>")
                .build();

        String xml = soapMapper.toSoapXml(request);

        assertThat(xml).contains("Producto &amp; Especial");
        assertThat(xml).contains("Calle &lt;5&gt;");
    }

    @Test
    void fromSoapXml_debeMapearCamposCorrectamente() {
        String xmlResponse = """
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

        EnviarPedidoResponse response = soapMapper.fromSoapXml(xmlResponse);

        assertThat(response.getCodigoEnvio()).isEqualTo("80375472");
        assertThat(response.getEstado()).isEqualTo("Entregado exitosamente al cliente");
    }

    @Test
    void fromSoapXml_cuandoNoExisteCodigo_debeRetornarNull() {
        String xmlResponse = "<soapenv:Envelope><soapenv:Body></soapenv:Body></soapenv:Envelope>";

        EnviarPedidoResponse response = soapMapper.fromSoapXml(xmlResponse);

        assertThat(response.getCodigoEnvio()).isNull();
        assertThat(response.getEstado()).isNull();
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
