-- Crear tablas si no existen
USE franchise_db;

CREATE TABLE IF NOT EXISTS franchise (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS branch (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (franchise_id) REFERENCES franchise(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    branch_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (branch_id) REFERENCES branch(id) ON DELETE CASCADE
);

-- Insertar datos de ejemplo
INSERT IGNORE INTO franchise (name) VALUES 
  ('Franquicia A'),
  ('Franquicia B'),
  ('Franquicia C');

INSERT IGNORE INTO branch (franchise_id, name) VALUES 
  (1, 'Sucursal Centro'),
  (1, 'Sucursal Norte'),
  (2, 'Sucursal Este');

INSERT IGNORE INTO product (branch_id, name, stock) VALUES 
  (1, 'Producto 1', 100),
  (1, 'Producto 2', 50),
  (2, 'Producto 3', 75),
  (3, 'Producto 4', 25);
