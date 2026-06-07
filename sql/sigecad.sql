DROP DATABASE IF EXISTS sigecad;
CREATE DATABASE sigecad
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE sigecad;

CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    id_rol INT NOT NULL,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
);

CREATE TABLE procesos_etl (
    id_proceso INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fuente_datos VARCHAR(150) NOT NULL,
    destino_datos VARCHAR(150) NOT NULL,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE archivos_fuente (
    id_archivo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_archivo VARCHAR(150) NOT NULL,
    ruta_archivo VARCHAR(255) NOT NULL,
    fecha_recepcion DATETIME NOT NULL
);

CREATE TABLE ejecuciones_etl (
    id_ejecucion INT AUTO_INCREMENT PRIMARY KEY,
    id_proceso INT NOT NULL,
    id_archivo INT NOT NULL,
    fecha_inicio DATETIME NOT NULL,
    fecha_fin DATETIME,
    estado VARCHAR(30) NOT NULL,
    registros_procesados INT DEFAULT 0,
    FOREIGN KEY (id_proceso) REFERENCES procesos_etl(id_proceso),
    FOREIGN KEY (id_archivo) REFERENCES archivos_fuente(id_archivo)
);

CREATE TABLE errores_validacion (
    id_error INT AUTO_INCREMENT PRIMARY KEY,
    id_ejecucion INT NOT NULL,
    fila INT NOT NULL,
    campo VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NOT NULL,
    FOREIGN KEY (id_ejecucion) REFERENCES ejecuciones_etl(id_ejecucion)
);

CREATE TABLE alertas (
    id_alerta INT AUTO_INCREMENT PRIMARY KEY,
    id_ejecucion INT NOT NULL,
    mensaje VARCHAR(255) NOT NULL,
    fecha_alerta DATETIME NOT NULL,
    estado VARCHAR(30) NOT NULL,
    FOREIGN KEY (id_ejecucion) REFERENCES ejecuciones_etl(id_ejecucion)
);

CREATE TABLE logs_ejecucion (
    id_log INT AUTO_INCREMENT PRIMARY KEY,
    id_ejecucion INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    evento VARCHAR(100) NOT NULL,
    detalle TEXT,
    FOREIGN KEY (id_ejecucion) REFERENCES ejecuciones_etl(id_ejecucion)
);

INSERT INTO roles (nombre_rol) VALUES
('Administrador'),
('Analista de datos'),
('Operador');

-- Claves solo para la demostracion academica.
INSERT INTO usuarios (nombre, email, password, id_rol) VALUES
('Jonatan Diaz', 'jonatan.diaz@empresa.com', '123456', 1),
('Ana Gomez', 'ana.gomez@empresa.com', '123456', 2),
('Carlos Perez', 'carlos.perez@empresa.com', '123456', 3);

INSERT INTO procesos_etl
(nombre, descripcion, fuente_datos, destino_datos, estado) VALUES
('Carga de ventas CSV',
 'Proceso ETL para cargar informacion de ventas desde archivo CSV',
 'ventas.csv', 'tabla_ventas', 'ACTIVO'),
('Carga de clientes CSV',
 'Proceso ETL para cargar informacion de clientes desde archivo CSV',
 'clientes.csv', 'tabla_clientes', 'ACTIVO');

INSERT INTO archivos_fuente
(nombre_archivo, ruta_archivo, fecha_recepcion) VALUES
('ventas_mayo.csv', 'C:/sigecad/entrada/ventas_mayo.csv', NOW()),
('clientes_mayo.csv', 'C:/sigecad/entrada/clientes_mayo.csv', NOW());

INSERT INTO ejecuciones_etl
(id_proceso, id_archivo, fecha_inicio, fecha_fin, estado, registros_procesados)
VALUES
(1, 1, NOW(), NOW(), 'EXITOSO', 1500),
(2, 2, NOW(), NOW(), 'FALLIDO', 0);

INSERT INTO errores_validacion
(id_ejecucion, fila, campo, descripcion) VALUES
(2, 15, 'email', 'Formato de correo electronico invalido'),
(2, 27, 'fecha_alta', 'Formato de fecha incorrecto');

INSERT INTO alertas
(id_ejecucion, mensaje, fecha_alerta, estado) VALUES
(2, 'El proceso ETL de clientes finalizo con errores de validacion',
 NOW(), 'PENDIENTE');

INSERT INTO logs_ejecucion
(id_ejecucion, fecha_hora, evento, detalle) VALUES
(1, NOW(), 'INICIO', 'Inicio de ejecucion del proceso de ventas'),
(1, NOW(), 'FIN', 'Proceso finalizado correctamente'),
(2, NOW(), 'INICIO', 'Inicio de ejecucion del proceso de clientes'),
(2, NOW(), 'ERROR', 'Se detectaron errores de validacion en el archivo');
