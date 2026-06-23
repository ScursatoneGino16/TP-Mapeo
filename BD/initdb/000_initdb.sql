\set ON_ERROR_STOP on

-- ============================================================================
-- SCRIPT DE INICIALIZACION
-- ---------------------------------------------------------------------------
-- Este archivo hace una carga tipo ETL en PostgreSQL:
-- 1. Elimina las tablas anteriores para empezar limpio.
-- 2. Crea las tablas definitivas donde quedaran guardados los datos.
-- 3. Crea tablas temporales para recibir los CSV tal como vienen.
-- 4. Copia los CSV a las tablas temporales con COPY.
-- 5. Inserta los datos desde las tablas temporales hacia las tablas finales.
--
-- La idea es separar la etapa de carga cruda de la etapa de guardado final.
-- Asi el alumno puede ver claramente el flujo: 
-- archivo CSV -> tabla temporal -> tabla definitiva.
-- ============================================================================

-- Paso 1: borrar las tablas definitivas si ya existen.
-- Se usa CASCADE para borrar primero las dependencias y evitar errores cuando
-- las tablas estan relacionadas entre si por foreign keys.
DROP TABLE IF EXISTS localidades CASCADE;
DROP TABLE IF EXISTS departamentos CASCADE;
DROP TABLE IF EXISTS municipios CASCADE;
DROP TABLE IF EXISTS provincias CASCADE;

-- Paso 2: crear las tablas definitivas.
-- Estas son las tablas que quedaran en la base de datos luego de terminar la carga.
-- provincias es la tabla padre de la jerarquia territorial.
CREATE TABLE provincias (
    id BIGINT PRIMARY KEY,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    nombre VARCHAR(255)
);

-- departamentos depende de provincias mediante provincia_id.
-- Si se borra una provincia, sus departamentos tambien se eliminan por cascada.
CREATE TABLE departamentos (
    id BIGINT PRIMARY KEY,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    provincia_id BIGINT,
    nombre VARCHAR(255),
    CONSTRAINT fk_departamentos_provincia FOREIGN KEY (provincia_id) REFERENCES provincias(id) ON DELETE CASCADE
);

-- localidades depende de departamentos mediante departamento_id.
-- Tambien se elimina en cascada si se borra el departamento padre.
CREATE TABLE localidades (
    id BIGINT PRIMARY KEY,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    departamento_id BIGINT,
    nombre VARCHAR(255),
    CONSTRAINT fk_localidades_departamento FOREIGN KEY (departamento_id) REFERENCES departamentos(id) ON DELETE CASCADE
);

-- Paso 3: crear tablas temporales.
-- Estas tablas funcionan como area de trabajo intermedia.
-- Reciben el contenido de los CSV sin hacer conversiones complejas ni validaciones extra.
-- Esto ayuda a detectar problemas de formato y simplifica la insercion final.
CREATE TEMP TABLE tmp_provincias (
    categoria TEXT,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    fuente TEXT,
    id TEXT,
    iso_id TEXT,
    iso_nombre TEXT,
    nombre TEXT,
    nombre_completo TEXT
);

CREATE TEMP TABLE tmp_departamentos (
    categoria TEXT,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    fuente TEXT,
    id TEXT,
    nombre TEXT,
    nombre_completo TEXT,
    provincia_id TEXT,
    provincia_interseccion DOUBLE PRECISION,
    provincia_nombre TEXT
);

-- tmp_localidades almacena la estructura completa del CSV de localidades.
-- Incluye campos administrativos como departamento, municipio y provincia.
CREATE TEMP TABLE tmp_localidades (
    categoria TEXT,
    centroide_lat DOUBLE PRECISION,
    centroide_lon DOUBLE PRECISION,
    departamento_id TEXT,
    departamento_nombre TEXT,
    fuente TEXT,
    id TEXT,
    localidad_censal_id TEXT,
    localidad_censal_nombre TEXT,
    municipio_id TEXT,
    municipio_nombre TEXT,
    nombre TEXT,
    provincia_id TEXT,
    provincia_nombre TEXT
);

-- Paso 4: cargar cada CSV en su tabla temporal con COPY.
-- COPY es la forma mas rapida de importar archivos CSV en PostgreSQL.
-- Como los archivos ya estan montados dentro del contenedor en /csv,
-- se puede leer cada archivo directamente desde esa ruta.
COPY tmp_provincias
FROM '/csv/provincias.csv'
WITH (FORMAT csv, HEADER true, DELIMITER ',', QUOTE '"');

COPY tmp_departamentos
FROM '/csv/departamentos.csv'
WITH (FORMAT csv, HEADER true, DELIMITER ',', QUOTE '"');

COPY tmp_localidades
FROM '/csv/localidades.csv'
WITH (FORMAT csv, HEADER true, DELIMITER ',', QUOTE '"');

-- Paso 5: mover los datos desde la tabla temporal hacia la tabla definitiva provincias.
-- En los CSV el id viene como texto, por eso se convierte a BIGINT al insertar.
-- Esta conversion asegura que el tipo de dato de la tabla final sea numerico.
INSERT INTO provincias (
    centroide_lat,
    centroide_lon,
    id,
    nombre
)
SELECT
    centroide_lat,
    centroide_lon,
    id::BIGINT,
    nombre
FROM tmp_provincias;

-- Paso 6: cargar departamentos en la tabla definitiva.
-- provincia_id tambien llega como texto en el CSV, asi que se convierte a BIGINT.
-- De esta forma la foreign key apunta correctamente a provincias.id.
INSERT INTO departamentos (
    centroide_lat,
    centroide_lon,
    id,
    nombre,
    provincia_id
)
SELECT
    centroide_lat,
    centroide_lon,
    id::BIGINT,
    nombre,
    provincia_id::BIGINT

FROM tmp_departamentos;

-- Paso 7: cargar localidades en la tabla definitiva.
-- departamento_id se convierte a BIGINT para respetar la foreign key.
-- El resultado final queda relacionado con su departamento correspondiente.
INSERT INTO localidades (
    centroide_lat,
    centroide_lon,
    id,
    nombre,
    departamento_id
)
SELECT
    centroide_lat,
    centroide_lon,
    id::BIGINT,
    nombre,
    departamento_id::BIGINT
FROM tmp_localidades;