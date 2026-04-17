@echo off
REM Script para ejecutar la aplicación con Docker Compose en Windows

echo ==========================================
echo Franchise API - Docker Compose
echo ==========================================

REM Verificar si Docker está instalado
docker --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker no está instalado o no está en el PATH
    echo Descarga Docker Desktop desde: https://www.docker.com/products/docker-desktop
    pause
    exit /b 1
)

REM Verificar si Docker Compose está disponible
docker-compose --version >nul 2>&1
if errorlevel 1 (
    echo Error: Docker Compose no está disponible
    pause
    exit /b 1
)

echo.
echo 1. Deteniendo contenedores anteriores...
docker-compose down

echo.
echo 2. Construyendo imagen Docker...
docker-compose build --no-cache

echo.
echo 3. Iniciando servicios...
docker-compose up -d

echo.
echo Esperando a que MySQL esté listo...
timeout /t 10 /nobreak

echo.
echo Esperando a que la aplicación esté lista...
timeout /t 15 /nobreak

echo.
echo ==========================================
echo Ver estado de los contenedores:
docker ps -a

echo.
echo ==========================================
echo Acceso a la aplicación:
echo   - URL: http://localhost:8080
echo   - API: http://localhost:8080/api/franchises
echo   - Health: http://localhost:8080/actuator/health
echo.
echo Base de datos:
echo   - Host: localhost
echo   - Puerto: 3306
echo   - Usuario: franchise_user
echo   - Contraseña: franchise_password
echo   - Base de datos: franchise_db
echo.
echo Comandos útiles:
echo   Ver logs:      docker-compose logs -f app
echo   Logs MySQL:    docker-compose logs -f mysql
echo   Detener:       docker-compose down
echo   Reiniciar:     docker-compose restart
echo ==========================================

pause
