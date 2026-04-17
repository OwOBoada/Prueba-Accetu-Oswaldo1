# 01 - Requisitos de la prueba

## Enunciado recibido

Construir un API en Spring Boot para manejar:
- Franquicias
- Sucursales por franquicia
- Productos por sucursal

## Criterios de aceptación y estado

1. Proyecto en Spring Boot: **Cumplido**
2. Endpoint para agregar franquicia: **Cumplido**
3. Endpoint para agregar sucursal a franquicia: **Cumplido**
4. Endpoint para agregar producto a sucursal: **Cumplido**
5. Endpoint para eliminar producto de sucursal: **Cumplido**
6. Endpoint para modificar stock de producto: **Cumplido**
7. Endpoint para producto con más stock por sucursal de franquicia: **Cumplido**
8. Persistencia (Redis/MySQL/Mongo/Dynamo en proveedor nube): **Cumplido parcialmente (MySQL local Docker)**

## Puntos extra y estado

- Empaquetado con Docker: **Cumplido**
- Programación funcional/reactiva: **No aplicado (se priorizó simplicidad)**
- Actualizar nombre franquicia: **Cumplido**
- Actualizar nombre sucursal: **Cumplido**
- Actualizar nombre producto: **Cumplido**
- IaC (Terraform/CloudFormation): **No aplicado**
- Despliegue en nube: **No aplicado**

## Observación

Se priorizó un MVP sólido, claro y ejecutable en local, con documentación completa para evaluación técnica.
