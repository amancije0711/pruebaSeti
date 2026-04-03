# ACME Pedidos API

API REST que transforma peticiones JSON a SOAP XML y viceversa para el sistema de envío de pedidos ACME.

## Arquitectura

```
Cliente (JSON) → Controller → Service → SoapMapper (JSON→XML) → SoapClient → Servicio SOAP
                                                                      ↓
Cliente (JSON) ← Controller ← Service ← SoapMapper (XML→JSON) ←────────
```

## Perfiles

| Perfil | Cliente SOAP | Uso |
|--------|-------------|-----|
| `default` | `SoapClientMock` (respuesta simulada) | Desarrollo y pruebas |
| `prod` | `SoapClientImpl` (servicio real) | Producción |

## Requisitos

- Java 17
- Maven 3.8+
- Docker (opcional)

---

## Ejecución local

### Modo mock (desarrollo)
```bash
mvn spring-boot:run
```

### Modo producción (servicio SOAP real)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

---

## Ejecución con Docker

### Modo mock
```bash
docker-compose --profile mock up --build
```

### Modo producción
```bash
docker-compose --profile prod up --build
```

---

## Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## Endpoint

**POST** `/api/v1/pedidos/enviar`

### Request
```json
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
```

### Response 200
```json
{
  "enviarPedidoRespuesta": {
    "codigoEnvio": "80375472",
    "estado": "Entregado exitosamente al cliente"
  }
}
```

### Response 400 - Validación
```json
{
  "enviarPedido.numPedido": "numPedido es requerido"
}
```

---

## Pruebas unitarias

```bash
mvn test
```

### Cobertura de tests

| Clase | Tests |
|-------|-------|
| `SoapMapperTest` | Mapeo JSON→XML, XML→JSON, escape de caracteres |
| `PedidoServiceImplTest` | Flujo completo, propagación de errores |
| `PedidoControllerTest` | HTTP 200, 400 validaciones, 502 error servicio |

---

## Patrones de diseño aplicados

| Patrón | Dónde |
|--------|-------|
| **Strategy** | `SoapClient` (interfaz) + `SoapClientImpl` / `SoapClientMock` |
| **Builder** | Lombok `@Builder` en todos los modelos |
| **Facade** | `PedidoController` delega lógica al servicio |
| **Mapper** | `SoapMapper` encapsula transformación XML↔JSON |
| **Dependency Injection** | `@RequiredArgsConstructor` en toda la cadena |
