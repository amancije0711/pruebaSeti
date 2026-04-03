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
@Schema(description = "Datos de respuesta del envío")
public class EnviarPedidoResponse {

    @Schema(example = "80375472")
    private String codigoEnvio;

    @Schema(example = "Entregado exitosamente al cliente")
    private String estado;
}
