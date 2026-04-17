#!/bin/bash
# Verificación rápida de la configuración Docker

echo "=========================================="
echo "Verificación de Configuración Docker"
echo "=========================================="
echo ""

# Verificar Docker
echo "1. Verificando Docker..."
if command -v docker &> /dev/null; then
    DOCKER_VERSION=$(docker --version)
    echo "   ✓ Docker instalado: $DOCKER_VERSION"
else
    echo "   ✗ Docker NO instalado"
    echo "   Descarga desde: https://www.docker.com/products/docker-desktop"
fi

# Verificar Docker Compose
echo ""
echo "2. Verificando Docker Compose..."
if command -v docker-compose &> /dev/null; then
    DC_VERSION=$(docker-compose --version)
    echo "   ✓ Docker Compose instalado: $DC_VERSION"
else
    echo "   ✗ Docker Compose NO instalado"
fi

# Verificar Docker daemon
echo ""
echo "3. Verificando Docker daemon..."
if docker info > /dev/null 2>&1; then
    echo "   ✓ Docker daemon está corriendo"
else
    echo "   ✗ Docker daemon NO está corriendo"
    echo "   Inicia Docker Desktop o ejecuta: sudo systemctl start docker"
fi

# Verificar espacio en disco
echo ""
echo "4. Verificando espacio en disco..."
DISK_SPACE=$(df / | awk 'NR==2 {print $4}')
DISK_NEEDED=$((2000000)) # 2GB en KB
if [ "$DISK_SPACE" -gt "$DISK_NEEDED" ]; then
    echo "   ✓ Espacio disponible: $(numfmt --to=iec $DISK_SPACE 2>/dev/null || echo $DISK_SPACE KB)"
else
    echo "   ⚠ Espacio bajo (necesita ~2GB)"
fi

# Verificar archivos de configuración
echo ""
echo "5. Verificando archivos de configuración..."
CONFIG_FILES=("Dockerfile" "docker-compose.yml" "init-db.sql" ".env.example")
for file in "${CONFIG_FILES[@]}"; do
    if [ -f "$file" ]; then
        echo "   ✓ $file"
    else
        echo "   ✗ $file (no encontrado)"
    fi
done

# Verificar scripts
echo ""
echo "6. Verificando scripts de despliegue..."
SCRIPTS=("deploy-aws-ec2.sh" "run-docker.bat")
for script in "${SCRIPTS[@]}"; do
    if [ -f "$script" ]; then
        echo "   ✓ $script"
    else
        echo "   ✗ $script (no encontrado)"
    fi
done

echo ""
echo "=========================================="
echo "Próximos pasos:"
echo "=========================================="
echo ""
echo "Opción 1: Ejecutar localmente con Docker"
echo "  Windows: run-docker.bat"
echo "  Linux/Mac: docker-compose up -d"
echo ""
echo "Opción 2: Desplegar en AWS EC2"
echo "  bash deploy-aws-ec2.sh"
echo ""
echo "Documentación: Ver DOCKER-DEPLOYMENT.md"
echo "=========================================="
