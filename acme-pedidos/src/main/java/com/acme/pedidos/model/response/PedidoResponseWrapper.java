package com.acme.pedidos.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Envelope de la respuesta del pedido")
public class PedidoResponseWrapper {

    private EnviarPedidoResponse enviarPedidoRespuesta;
}
