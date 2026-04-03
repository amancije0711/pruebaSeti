package com.acme.pedidos.controller;

import com.acme.pedidos.model.response.EnviarPedidoResponse;
import com.acme.pedidos.service.PedidoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PedidoController.class)
class PedidoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PedidoService pedidoService;

    private static final String REQUEST_VALIDO = """
            {
              "enviarPedido": {
                "numPedido": "75630275",
                "cantidadPedido": "1",
                "codigoEAN": "00110000765191002104587",
                "nombreProducto": "Armario INVAL",
                "numDocumento": "1113987400",
                "direccion": "CR 72B 45 12 APT 301"
              }
            }
            """;

    @Test
    void enviarPedido_conRequestValido_debeRetornar200() throws Exception {
        when(pedidoService.enviarPedido(any())).thenReturn(
                EnviarPedidoResponse.builder()
                        .codigoEnvio("80375472")
                        .estado("Entregado exitosamente al cliente")
                        .build()
        );

        mockMvc.perform(post("/api/v1/pedidos/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enviarPedidoRespuesta.codigoEnvio").value("80375472"))
                .andExpect(jsonPath("$.enviarPedidoRespuesta.estado").value("Entregado exitosamente al cliente"));
    }

    @Test
    void enviarPedido_sinNumPedido_debeRetornar400() throws Exception {
        String requestInvalido = """
                {
                  "enviarPedido": {
                    "cantidadPedido": "1",
                    "codigoEAN": "00110000765191002104587",
                    "nombreProducto": "Armario INVAL",
                    "numDocumento": "1113987400",
                    "direccion": "CR 72B 45 12 APT 301"
                  }
                }
                """;

        mockMvc.perform(post("/api/v1/pedidos/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['enviarPedido.numPedido']").value("numPedido es requerido"));
    }

    @Test
    void enviarPedido_sinBodyEnviarPedido_debeRetornar400() throws Exception {
        String requestInvalido = "{}";

        mockMvc.perform(post("/api/v1/pedidos/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestInvalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void enviarPedido_cuandoServicioFalla_debeRetornar502() throws Exception {
        when(pedidoService.enviarPedido(any()))
                .thenThrow(new RuntimeException("Error al comunicarse con el servicio SOAP"));

        mockMvc.perform(post("/api/v1/pedidos/enviar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_VALIDO))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.error").exists());
    }
}
