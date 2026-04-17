# 03 - Ejecución local

## Opción A (recomendada): Docker Compose

### Requisitos

- Docker Desktop o Docker Engine + Compose

### Pasos

1. Ubicarse en la raíz del proyecto
2. Ejecutar:

```bash
docker compose up --build
```

3. Verificar API:

```bash
curl http://localhost:8080/actuator/health
```

Respuesta esperada:

```json
{"status":"UP"}
```

## Opción B: ejecución manual

### Requisitos

- JDK 21
- Maven 3.9+
- MySQL 8 levantado local

### Variables de entorno

```bash
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=franchise_db
export DB_USER=franchise_user
export DB_PASSWORD=franchise_pass
```

### Ejecución

```bash
mvn clean spring-boot:run
```

### Pruebas

```bash
mvn test
```
