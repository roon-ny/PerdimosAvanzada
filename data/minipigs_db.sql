CREATE DATABASE IF NOT EXISTS minipigs_db;
USE minipigs_db;

CREATE TABLE IF NOT EXISTS minipigs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    raza VARCHAR(30),
    edad INT,          -- En meses, para la lógica de etapa
    altura DOUBLE,     -- En cm, para la lógica de categoría
    peso DOUBLE,
    categoria VARCHAR(50), -- Se llena desde la lógica del Control
    etapa VARCHAR(30)      -- Se llena desde la lógica del Control
);