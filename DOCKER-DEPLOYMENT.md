# 🐳 Deployment con Docker & AWS EC2

Guía para desplegar la aplicación Franchise API con Docker y MySQL en AWS EC2.

## Tabla de Contenidos
1. [Requisitos](#requisitos)
2. [Ejecución Local con Docker](#ejecución-local-con-docker)
3. [Despliegue en AWS EC2](#despliegue-en-aws-ec2)
4. [Configuración de Seguridad](#configuración-de-seguridad)
5. [Monitoreo y Logs](#monitoreo-y-logs)
6. [Troubleshooting](#troubleshooting)

---

## Requisitos

### Local (Windows/Mac/Linux)
- **Docker Desktop** (incluye Docker y Docker Compose)
  - [Descargar aquí](https://www.docker.com/products/docker-desktop)
- **Git** (opcional, para clonar el repo)

### AWS EC2
- **Instancia EC2** con Ubuntu 22.04 o similar
- **Security Group** con puertos abiertos:
  - `8080` (HTTP - Aplicación)
  - `3306` (MySQL - opcional si es acceso privado)
  - `22` (SSH - acceso a la instancia)

---

## Ejecución Local con Docker

### Opción 1: Windows (Batch Script)

```bash
# Ejecutar el script
run-docker.bat
```

El script:
- ✓ Compila la aplicación
- ✓ Construye la imagen Docker
- ✓ Inicia MySQL
- ✓ Levanta la aplicación

### Opción 2: Línea de Comandos (Multiplataforma)

```bash
# Construir imagen
docker-compose build

# Iniciar servicios
docker-compose up -d

# Ver logs
docker-compose logs -f app

# Detener
docker-compose down
```

### Verificar que todo funciona

```bash
# Chequear health
curl http://localhost:8080/actuator/health

# Listar franquicias
curl http://localhost:8080/api/franchises

# Crear franquicia
curl -X POST http://localhost:8080/api/franchises \
  -H "Content-Type: application/json" \
  -d '{"name":"Mi Franquicia"}'
```

---

## Despliegue en AWS EC2

### Paso 1: Crear Instancia EC2

```bash
# Especificaciones recomendadas:
# - AMI: Ubuntu 22.04 LTS
# - Tipo: t3.small o superior (mínimo 2GB RAM)
# - Almacenamiento: 20GB gp3
# - Security Group: Puertos 22, 80, 8080 abiertos
```

### Paso 2: Conectarse a la Instancia

```bash
# Usar tu .pem key
ssh -i "tu-key.pem" ubuntu@<instancia-ip>
```

### Paso 3: Ejecutar Script de Automatización

```bash
# Opción A: Descargando el script desde GitHub
curl -fsSL https://raw.githubusercontent.com/tu-usuario/franchise-api/main/deploy-aws-ec2.sh | bash

# Opción B: Ejecutar manualmente
git clone https://github.com/tu-usuario/franchise-api.git /opt/franchise-api
cd /opt/franchise-api
bash deploy-aws-ec2.sh
```

### Paso 4: Configurar Variables de Entorno (Opcional)

```bash
# Editar configuración
sudo nano /opt/franchise-api/.env

# Cambiar credenciales o URL de base de datos si es necesario
```

### Paso 5: Verificar Despliegue

```bash
# Ver estado de contenedores
docker ps

# Ver logs de la aplicación
docker logs -f franchise-api

# Probar la API
curl http://localhost:8080/actuator/health
```

---

## Configuración de Seguridad

### Security Group (AWS Console)

Agregar reglas de entrada:

| Protocolo | Puerto | Origen |
|-----------|--------|--------|
| SSH | 22 | 0.0.0.0/0 (o tu IP) |
| HTTP | 8080 | 0.0.0.0/0 |
| MySQL | 3306 | 0.0.0.0/0 (solo si necesitas acceso remoto) |

### Credenciales por Defecto

```
MySQL:
  Usuario: franchise_user
  Contraseña: franchise_password
  BD: franchise_db

Cambiar en docker-compose.yml:
  MYSQL_PASSWORD=<tu-contraseña-segura>
  SPRING_DATASOURCE_PASSWORD=<igual-a-arriba>
```

### Usar MySQL Remota (RDS)

Si quieres usar AWS RDS en lugar de MySQL en Docker:

```bash
# Editar docker-compose.yml
SPRING_DATASOURCE_URL=jdbc:mysql://tu-rds-endpoint:3306/franchise_db

# Luego ejecutar solo la aplicación (sin MySQL)
docker run -d \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://tu-rds-endpoint:3306/franchise_db \
  -e SPRING_DATASOURCE_USERNAME=admin \
  -e SPRING_DATASOURCE_PASSWORD=tu-password \
  -p 8080:8080 \
  --name franchise-api \
  franchise-api:latest
```

---

## Monitoreo y Logs

### Ver Logs en Tiempo Real

```bash
# Logs de aplicación
docker-compose logs -f app

# Logs de MySQL
docker-compose logs -f mysql

# Todos los logs
docker-compose logs -f
```

### Verificar Salud de Servicios

```bash
# Estado de contenedores
docker-compose ps

# Detalles de la red
docker network ls
docker inspect franchise-network

# Memoria y CPU
docker stats
```

### Exportar Logs a Archivo

```bash
docker-compose logs > logs.txt
```

---

## Troubleshooting

### Error: "Port 8080 is already in use"

```bash
# Encontrar proceso usando puerto 8080
lsof -i :8080

# Detener contenedor existente
docker-compose down
docker stop $(docker ps -q)
```

### Error: "MySQL connection refused"

```bash
# Verificar que MySQL está corriendo
docker-compose ps

# Ver logs de MySQL
docker-compose logs mysql

# Reiniciar MySQL
docker-compose restart mysql
```

### Error: "Cannot connect to Docker daemon"

```bash
# En EC2
sudo systemctl start docker

# En Windows/Mac
# Abre Docker Desktop desde el menú de inicio
```

### Error: "JAVA_HOME not set"

El Dockerfile incluye JDK 21, no necesitas configurar JAVA_HOME.

### Aplicación lenta o con errores 500

```bash
# Verificar logs
docker-compose logs app | tail -50

# Aumentar memoria si es necesario
# En docker-compose.yml:
deploy:
  resources:
    limits:
      memory: 1G
```

### Persistencia de datos

MySQL guarda datos en volumen Docker:
```bash
docker volume ls
docker volume inspect franchise-api_mysql_data
```

Para hacer backup:
```bash
docker-compose exec mysql mysqldump -u root -p$MYSQL_ROOT_PASSWORD franchise_db > backup.sql
```

Para restaurar:
```bash
docker-compose exec -T mysql mysql -u root -p$MYSQL_ROOT_PASSWORD franchise_db < backup.sql
```

---

## Arquivos Generados

```
.
├── Dockerfile                 # Imagen Docker multietapa
├── docker-compose.yml         # Orquestación MySQL + App
├── init-db.sql               # Script de inicialización BD
├── deploy-aws-ec2.sh         # Script automatización AWS
├── run-docker.bat            # Script para Windows
└── DOCKER-DEPLOYMENT.md      # Esta documentación
```

---

## URLs Útiles (Después de desplegar)

```
# Local
http://localhost:8080/api/franchises
http://localhost:8080/actuator/health

# AWS EC2 (reemplaza con tu IP)
http://<ec2-ip>:8080/api/franchises
http://<ec2-ip>:8080/actuator/health
```

---

## Preguntas Frecuentes

**P: ¿Puedo cambiar el puerto 8080?**
A: Sí, en docker-compose.yml: `ports: - "8081:8080"`

**P: ¿Cómo hago backup de los datos?**
A: Ver sección "Troubleshooting" - comandos mysqldump

**P: ¿Puedo usar PostgreSQL en lugar de MySQL?**
A: Sí, actualiza docker-compose.yml y las dependencias en pom.xml

**P: ¿Se reinician los contenedores si falla la aplicación?**
A: Sí, `restart: unless-stopped` en docker-compose.yml

---

**Última actualización:** 17 de abril de 2026
