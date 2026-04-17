# Prueba Técnica Backend - Franquicias

API REST construida con Spring Boot para gestionar franquicias, sucursales y productos con stock.

## Stack técnico

- Java 21
- Spring Boot 3.3.x
- Spring Web, Validation, Data JPA, Actuator
- MySQL 8
- Docker y Docker Compose

## Estructura de dominio

- `Franchise(id, name)`
- `Branch(id, name, franchise_id)`
- `Product(id, name, stock, branch_id)`

Reglas:
- `name` es obligatorio en las tres entidades.
- `stock` debe ser mayor o igual a 0.

## Endpoints implementados

Base:
- `POST /api/franchises`
- `POST /api/franchises/{franchiseId}/branches`
- `POST /api/branches/{branchId}/products`
- `DELETE /api/branches/{branchId}/products/{productId}`
- `PATCH /api/branches/{branchId}/products/{productId}/stock`
- `GET /api/franchises/{franchiseId}/top-stock-products-by-branch`

Plus:
- `PATCH /api/franchises/{franchiseId}/name`
- `PATCH /api/branches/{branchId}/name`
- `PATCH /api/products/{productId}/name`

## Ejecución rápida con Docker

```bash
docker compose up --build
```

La API queda en:
- `http://localhost:8080`

Health check:
- `GET http://localhost:8080/actuator/health`

## Ejecución local sin Docker (requiere MySQL corriendo)

Variables de entorno sugeridas:

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=franchise_db
export DB_USER=franchise_user
export DB_PASSWORD=franchise_pass
```

Compilar y ejecutar:

```bash
mvn clean spring-boot:run
```

El perfil `mysql` se activa por defecto para usar `src/main/resources/application-mysql.yml`.

## Pruebas

```bash
mvn test
```

Incluye:
- Unit tests de servicio
- Integration tests con `MockMvc` y H2 en memoria

## Documentación adicional

- [docs/01-requisitos.md](docs/01-requisitos.md)
- [docs/02-plan-ejecucion.md](docs/02-plan-ejecucion.md)
- [docs/03-ejecucion-local.md](docs/03-ejecucion-local.md)
- [docs/04-api.md](docs/04-api.md)
- [docs/05-deployment-roadmap.md](docs/05-deployment-roadmap.md)
- [docs/06-recursos-y-cuentas.md](docs/06-recursos-y-cuentas.md)
