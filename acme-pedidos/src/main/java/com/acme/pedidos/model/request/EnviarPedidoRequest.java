package com.acme.pedidos.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos del pedido a enviar")
public class EnviarPedidoRequest {

    @NotBlank(message = "numPedido es requerido")
    private String numPedido;

    @NotBlank(message = "cantidadPedido es requerido")
    private String cantidadPedido;

    @NotBlank(message = "codigoEAN es requerido")
    private String codigoEAN;

    @NotBlank(message = "nombreProducto es requerido")
    private String nombreProducto;

    @NotBlank(message = "numDocumento es requerido")
    private String numDocumento;

    @NotBlank(message = "direccion es requerido")
    private String direccion;
}
