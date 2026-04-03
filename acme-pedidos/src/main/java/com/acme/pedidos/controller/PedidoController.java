package com.acme.pedidos.controller;

import com.acme.pedidos.model.request.PedidoRequestWrapper;
import com.acme.pedidos.model.response.EnviarPedidoResponse;
import com.acme.pedidos.model.response.PedidoResponseWrapper;
import com.acme.pedidos.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "API de envío de pedidos ACME")
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/enviar")
    @Operation(
            summary = "Enviar pedido",
            description = "Recibe un pedido en JSON, lo transforma a SOAP XML, lo envía al backend y retorna la respuesta en JSON",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Pedido enviado exitosamente",
                            content = @Content(schema = @Schema(implementation = PedidoResponseWrapper.class))),
                    @ApiResponse(responseCode = "400", description = "Datos inválidos"),
                    @ApiResponse(responseCode = "502", description = "Error comunicándose con el servicio externo")
            }
    )
    public ResponseEntity<PedidoResponseWrapper> enviarPedido(@Valid @RequestBody PedidoRequestWrapper wrapper) {
        EnviarPedidoResponse response = pedidoService.enviarPedido(wrapper.getEnviarPedido());
        return ResponseEntity.ok(PedidoResponseWrapper.builder()
                .enviarPedidoRespuesta(response)
                .build());
    }
}
