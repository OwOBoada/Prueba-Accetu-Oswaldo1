# 05 - Roadmap de despliegue (opcional)

Este proyecto se entrega orientado a ejecución local. Para evolucionar a nube:

## Ruta simple en AWS

1. **Base de datos**: migrar MySQL local a Amazon RDS MySQL.
2. **Contenedor**: publicar imagen en Docker Hub o ECR.
3. **Ejecución**: desplegar API en ECS Fargate o Elastic Beanstalk.
4. **Configuración**: mover secretos a AWS Systems Manager Parameter Store o Secrets Manager.
5. **Observabilidad**: enviar logs a CloudWatch.

## Infraestructura como código (opcional)

- Terraform para crear:
  - VPC básica
  - RDS MySQL
  - ECS cluster/service o Beanstalk app
  - Security groups y variables

## Criterio de evolución

Mantener el mismo contrato de API y solo externalizar configuración por variables de entorno.
