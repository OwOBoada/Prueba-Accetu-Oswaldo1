#!/bin/bash

set -e

echo "=========================================="
echo "Franchise API - AWS EC2 Deployment Script"
echo "=========================================="

# Variables
REPO_URL="${REPO_URL:-https://github.com/tu-usuario/franchise-api.git}"
APP_DIR="/opt/franchise-api"
DOCKER_COMPOSE_FILE="$APP_DIR/docker-compose.yml"

echo "1. Actualizando paquetes del sistema..."
sudo apt-get update
sudo apt-get upgrade -y

echo "2. Instalando Docker..."
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release

curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

echo "deb [arch=amd64 signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" | \
  sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

echo "3. Instalando Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/download/v2.24.0/docker-compose-$(uname -s)-$(uname -m)" \
  -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

echo "4. Iniciando Docker..."
sudo systemctl start docker
sudo systemctl enable docker
sudo usermod -aG docker $USER

echo "5. Creando directorio de aplicación..."
sudo mkdir -p $APP_DIR
sudo chown -R $USER:$USER $APP_DIR

echo "6. Clonando repositorio..."
if [ -d "$APP_DIR/.git" ]; then
  cd $APP_DIR
  git pull origin main
else
  git clone $REPO_URL $APP_DIR
  cd $APP_DIR
fi

echo "7. Creando archivo .env..."
cat > $APP_DIR/.env << EOF
SPRING_PROFILES_ACTIVE=mysql
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/franchise_db?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC
SPRING_DATASOURCE_USERNAME=franchise_user
SPRING_DATASOURCE_PASSWORD=franchise_password
SPRING_JPA_HIBERNATE_DDL_AUTO=update
SERVER_PORT=8080
EOF

echo "8. Construyendo y levantando servicios con Docker Compose..."
cd $APP_DIR
docker-compose down || true
docker-compose build --no-cache
docker-compose up -d

echo "9. Esperando a que la aplicación esté lista..."
sleep 15

echo "10. Verificando salud de la aplicación..."
RETRY_COUNT=0
while [ $RETRY_COUNT -lt 10 ]; do
  if curl -f http://localhost:8080/actuator/health >/dev/null 2>&1; then
    echo "✓ Aplicación está lista!"
    break
  fi
  echo "Esperando (intento $((RETRY_COUNT + 1))/10)..."
  sleep 5
  RETRY_COUNT=$((RETRY_COUNT + 1))
done

if [ $RETRY_COUNT -eq 10 ]; then
  echo "✗ La aplicación no está respondiendo después de 50 segundos"
  echo "Logs:"
  docker-compose logs app
  exit 1
fi

echo ""
echo "=========================================="
echo "✓ Despliegue completado exitosamente!"
echo "=========================================="
echo "URL de la aplicación: http://$(ec2-metadata --public-ipv4 | cut -d' ' -f2):8080"
echo "API Health: http://localhost:8080/actuator/health"
echo "API Franchises: http://localhost:8080/api/franchises"
echo ""
echo "Comandos útiles:"
echo "  Ver logs:      docker-compose -f $DOCKER_COMPOSE_FILE logs -f app"
echo "  Detener:       docker-compose -f $DOCKER_COMPOSE_FILE down"
echo "  Reiniciar:     docker-compose -f $DOCKER_COMPOSE_FILE restart"
echo "=========================================="
