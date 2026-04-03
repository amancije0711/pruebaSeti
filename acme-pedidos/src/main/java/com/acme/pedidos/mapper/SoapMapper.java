package com.acme.pedidos.mapper;

import com.acme.pedidos.model.request.EnviarPedidoRequest;
import com.acme.pedidos.model.response.EnviarPedidoResponse;
import org.springframework.stereotype.Component;

@Component
public class SoapMapper {

    private static final String SOAP_ENVELOPE_OPEN = """
            <soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
                              xmlns:env="http://WSDLs/EnvioPedidos/EnvioPedidosAcme">
                <soapenv:Header/>
                <soapenv:Body>
                    <env:EnvioPedidoAcme>
                        <EnvioPedidoRequest>
            """;

    private static final String SOAP_ENVELOPE_CLOSE = """
                        </EnvioPedidoRequest>
                    </env:EnvioPedidoAcme>
                </soapenv:Body>
            </soapenv:Envelope>
            """;

    public String toSoapXml(EnviarPedidoRequest request) {
        return SOAP_ENVELOPE_OPEN +
               "            <pedido>" + escapeXml(request.getNumPedido()) + "</pedido>\n" +
               "            <Cantidad>" + escapeXml(request.getCantidadPedido()) + "</Cantidad>\n" +
               "            <EAN>" + escapeXml(request.getCodigoEAN()) + "</EAN>\n" +
               "            <Producto>" + escapeXml(request.getNombreProducto()) + "</Producto>\n" +
               "            <Cedula>" + escapeXml(request.getNumDocumento()) + "</Cedula>\n" +
               "            <Direccion>" + escapeXml(request.getDireccion()) + "</Direccion>\n" +
               SOAP_ENVELOPE_CLOSE;
    }

    public EnviarPedidoResponse fromSoapXml(String xmlResponse) {
        String codigo = extractTag(xmlResponse, "Codigo");
        String mensaje = extractTag(xmlResponse, "Mensaje");

        return EnviarPedidoResponse.builder()
                .codigoEnvio(codigo)
                .estado(mensaje)
                .build();
    }

    private String extractTag(String xml, String tag) {
        String open = "<" + tag + ">";
        String close = "</" + tag + ">";
        int start = xml.indexOf(open);
        int end = xml.indexOf(close);
        if (start == -1 || end == -1) return null;
        return xml.substring(start + open.length(), end).trim();
    }

    private String escapeXml(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }
}
