# Presentacion del desarrollo

## Diapositiva 1 - Proyecto

**SIGECAD: Sistema de Gestion y Control de Actualizaciones de Datos**

Prototipo operacional en Java nativo, JDBC y MySQL.

## Diapositiva 2 - Problema

- Recepcion y validacion manual de archivos.
- Errores detectados tarde.
- Informacion dispersa.
- Falta de trazabilidad de las ejecuciones.

## Diapositiva 3 - Objetivo

- Administrar procesos ETL.
- Validar archivos CSV.
- Registrar ejecuciones, errores, logs y alertas.
- Centralizar la informacion en MySQL.

## Diapositiva 4 - PUD

- Inicio: problema, actores y alcance.
- Elaboracion: arquitectura por capas y modelo de datos.
- Construccion: incrementos Java, DAO y validacion.
- Transicion: scripts, pruebas y documentacion.

## Diapositiva 5 - Estructura

```text
sigecad/
|- modelo/
|- dao/
|- servicio/
|- principal/
```

## Diapositiva 6 - POO

- Encapsulamiento: atributos privados.
- Abstraccion: `EventoEjecucion`.
- Herencia: `ErrorValidacion` y `LogEjecucion`.
- Polimorfismo: implementaciones diferentes de `resumir()`.

## Diapositiva 7 - Flujo CSV

```text
CSV -> ArchivoFuente -> EjecucionETL -> Validacion
                                      |- ErrorValidacion
                                      |- LogEjecucion
                                      `- Alerta
```

## Diapositiva 8 - MySQL

- Ocho tablas alineadas con el modelo presentado.
- Claves primarias y foraneas.
- Acceso mediante JDBC y `PreparedStatement`.
- Conexion configurada con variables de entorno.

## Diapositiva 9 - Demostracion

1. Listar procesos.
2. Validar `datos\ventas.csv`.
3. Consultar ejecucion y logs.
4. Validar `datos\ventas_error.csv`.
5. Consultar errores y alerta.

## Diapositiva 10 - Limite y siguiente incremento

El prototipo valida el CSV y persiste su trazabilidad. La carga de filas a
`tabla_ventas` o `tabla_clientes` requiere definir primero esas tablas destino.
