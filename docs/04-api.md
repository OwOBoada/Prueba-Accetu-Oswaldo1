# 04 - API

Base URL: `http://localhost:8080`

## 1) Crear franquicia

`POST /api/franchises`

Body:
```json
{"name":"Franquicia Norte"}
```

## 2) Agregar sucursal a franquicia

`POST /api/franchises/{franchiseId}/branches`

Body:
```json
{"name":"Sucursal Centro"}
```

## 3) Agregar producto a sucursal

`POST /api/branches/{branchId}/products`

Body:
```json
{"name":"Producto A","stock":10}
```

## 4) Eliminar producto de sucursal

`DELETE /api/branches/{branchId}/products/{productId}`

Respuesta: `204 No Content`

## 5) Actualizar stock de producto

`PATCH /api/branches/{branchId}/products/{productId}/stock`

Body:
```json
{"stock":25}
```

## 6) Producto con más stock por sucursal para una franquicia

`GET /api/franchises/{franchiseId}/top-stock-products-by-branch`

Respuesta ejemplo:
```json
[
  {
    "branchId": 1,
    "branchName": "Sucursal Centro",
    "productId": 8,
    "productName": "Producto A",
    "stock": 25
  }
]
```

## Plus

### Actualizar nombre franquicia

`PATCH /api/franchises/{franchiseId}/name`

Body:
```json
{"name":"Nuevo nombre franquicia"}
```

### Actualizar nombre sucursal

`PATCH /api/branches/{branchId}/name`

Body:
```json
{"name":"Nuevo nombre sucursal"}
```

### Actualizar nombre producto

`PATCH /api/products/{productId}/name`

Body:
```json
{"name":"Nuevo nombre producto"}
```

## Errores

Formato:
```json
{
  "timestamp": "2026-04-16T02:00:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Error de validación",
  "details": [
    "stock: El stock no puede ser negativo"
  ]
}
```
