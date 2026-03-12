-- ============================================================
-- Script de creacion de Base de Datos: minipigdb
-- Motor: MySQL (XAMPP)
-- Usuario: root
-- Contrasena: (vacia por defecto en XAMPP)
-- URL de conexion: jdbc:mysql://localhost/minipigdb
-- ============================================================

CREATE DATABASE IF NOT EXISTS minipigdb
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE minipigdb;

-- Tabla principal de minipigs
CREATE TABLE IF NOT EXISTS minipigs (
    codigo       VARCHAR(20)  NOT NULL PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    genero       VARCHAR(20)  NOT NULL,
    id_microchip VARCHAR(50)  NOT NULL UNIQUE,
    raza         VARCHAR(50)  NOT NULL,
    color        VARCHAR(50),
    peso         VARCHAR(20),
    altura       VARCHAR(20),
    caracteristica1 VARCHAR(100),
    caracteristica2 VARCHAR(100),
    url_foto     VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ============================================================
-- FIN DEL SCRIPT
-- ============================================================
