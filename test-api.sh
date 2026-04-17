#!/bin/bash
# Script de pruebas de API - Franchise API

# Configuración
HOST="${1:-localhost}"
PORT="${2:-8080}"
BASE_URL="http://$HOST:$PORT"

echo "=========================================="
echo "Pruebas de API - Franchise API"
echo "=========================================="
echo "Base URL: $BASE_URL"
echo ""

# Función para hacer request
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local description=$4
    
    echo "─────────────────────────────────────"
    echo "Prueba: $description"
    echo "Método: $method $endpoint"
    
    if [ -z "$data" ]; then
        echo "Request:"
        curl -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -w "\n"
    else
        echo "Datos: $data"
        echo "Request:"
        curl -X $method "$BASE_URL$endpoint" \
            -H "Content-Type: application/json" \
            -d "$data" \
            -w "\n"
    fi
    echo ""
}

# Prueba 1: Health Check
echo "1️⃣ HEALTH CHECK"
test_endpoint "GET" "/actuator/health" "" "Health de la aplicación"

# Prueba 2: Listar franquicias (vacío inicialmente)
echo "2️⃣ LISTAR FRANQUICIAS"
test_endpoint "GET" "/api/franchises" "" "Obtener todas las franquicias"

# Prueba 3: Crear franquicia 1
echo "3️⃣ CREAR FRANQUICIA"
FRANCHISE_1='{"name": "Franquicia Tecnología"}'
test_endpoint "POST" "/api/franchises" "$FRANCHISE_1" "Crear primera franquicia"

# Prueba 4: Crear franquicia 2
FRANCHISE_2='{"name": "Franquicia Alimentaria"}'
test_endpoint "POST" "/api/franchises" "$FRANCHISE_2" "Crear segunda franquicia"

# Prueba 5: Listar franquicias (con datos)
echo "4️⃣ LISTAR FRANQUICIAS (CON DATOS)"
test_endpoint "GET" "/api/franchises" "" "Obtener todas las franquicias"

# Prueba 6: Obtener franquicia por ID
echo "5️⃣ OBTENER FRANQUICIA POR ID"
test_endpoint "GET" "/api/franchises/1" "" "Obtener franquicia con ID 1"

# Prueba 7: Crear sucursal
echo "6️⃣ CREAR SUCURSAL"
BRANCH='{"franchiseId": 1, "name": "Sucursal Centro"}'
test_endpoint "POST" "/api/branches" "$BRANCH" "Crear sucursal para franquicia 1"

# Prueba 8: Crear producto
echo "7️⃣ CREAR PRODUCTO"
PRODUCT='{"branchId": 1, "name": "Laptop", "stock": 50}'
test_endpoint "POST" "/api/products" "$PRODUCT" "Crear producto en sucursal 1"

# Prueba 9: Actualizar nombre de franquicia
echo "8️⃣ ACTUALIZAR NOMBRE DE FRANQUICIA"
UPDATE_NAME='{"name": "Franquicia Tecnología Premium"}'
test_endpoint "PUT" "/api/franchises/1" "$UPDATE_NAME" "Actualizar nombre de franquicia"

# Prueba 10: Actualizar stock de producto
echo "9️⃣ ACTUALIZAR STOCK DE PRODUCTO"
UPDATE_STOCK='{"stock": 100}'
test_endpoint "PUT" "/api/products/1" "$UPDATE_STOCK" "Actualizar stock del producto"

# Prueba 11: Top stock products
echo "🔟 TOP PRODUCTS POR STOCK"
test_endpoint "GET" "/api/branches/1/products/top" "" "Obtener productos con más stock"

# Resumen
echo "=========================================="
echo "✓ Pruebas completadas"
echo "=========================================="
echo ""
echo "Resumen de endpoints probados:"
echo "  GET  /actuator/health           - Estado de la aplicación"
echo "  GET  /api/franchises             - Listar franquicias"
echo "  POST /api/franchises             - Crear franquicia"
echo "  GET  /api/franchises/:id         - Obtener franquicia"
echo "  PUT  /api/franchises/:id         - Actualizar franquicia"
echo "  POST /api/branches               - Crear sucursal"
echo "  POST /api/products               - Crear producto"
echo "  PUT  /api/products/:id           - Actualizar producto"
echo "  GET  /api/branches/:id/products/top - Top productos"
echo ""
echo "Base de datos (Docker):"
echo "  Host: mysql (desde contenedor) | localhost:3306 (local)"
echo "  Usuario: franchise_user"
echo "  Contraseña: franchise_password"
echo "  BD: franchise_db"
echo "=========================================="
