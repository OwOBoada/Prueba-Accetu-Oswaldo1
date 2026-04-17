# 02 - Plan de ejecución

## Fase 1 - Base del proyecto

- Crear proyecto Spring Boot con Web, Validation, JPA, Actuator
- Definir modelo de dominio y relaciones
- Configurar datasource MySQL

## Fase 2 - Lógica de negocio y API

- Implementar capa `service` con reglas
- Implementar controladores y DTOs
- Implementar manejo global de errores

## Fase 3 - Pruebas

- Unit tests para reglas de negocio
- Integration tests para flujo principal y validaciones

## Fase 4 - Contenerización y documentación

- Dockerfile multi-stage
- Docker Compose con MySQL + API
- Documentación de ejecución, API y roadmap

## Checklist de entrega

- [x] Código fuente backend
- [x] Endpoints obligatorios
- [x] Endpoints plus de renombrado
- [x] Persistencia con MySQL
- [x] Docker y docker-compose
- [x] Documentación para GitHub
- [x] Pruebas unitarias e integración
