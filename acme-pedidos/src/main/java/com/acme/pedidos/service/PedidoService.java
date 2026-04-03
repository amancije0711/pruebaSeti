package com.acme.pedidos.service;

import com.acme.pedidos.model.request.EnviarPedidoRequest;
import com.acme.pedidos.model.response.EnviarPedidoResponse;

public interface PedidoService {

    EnviarPedidoResponse enviarPedido(EnviarPedidoRequest request);
}
