# Informe de desarrollo - SIGECAD

## 1. Modelo de negocio

La organizacion controla manualmente la recepcion y validacion de archivos
utilizados por procesos ETL. Los errores, archivos procesados y resultados
quedan dispersos, dificultando la deteccion de incidentes y la auditoria.

SIGECAD centraliza usuarios, procesos, archivos fuente, ejecuciones, errores,
logs y alertas. El prototipo permite demostrar el flujo operacional completo de
validacion y trazabilidad de un archivo CSV.

## 2. Alcance

- Registrar, listar, buscar, consultar por ID, modificar y cambiar el estado de
  procesos ETL.
- Aplicar baja logica de procesos para respetar integridad referencial.
- Recibir la ruta de un CSV y validar su estructura.
- Registrar el archivo y su ejecucion.
- Guardar errores de validacion, logs y alertas.
- Consultar los resultados desde un menu.
- Trabajar en memoria o sobre MySQL mediante JDBC.

El modelo entregado no define `tabla_ventas` ni `tabla_clientes`. Por ese motivo
el prototipo valida y audita los CSV, pero no inserta sus filas en una tabla de
negocio. Esa carga sera un incremento posterior cuando se defina la estructura
de cada destino.

## 3. Proceso Unificado de Desarrollo

**Inicio.** Se definieron el problema, los actores y los casos de uso:
administrar procesos, validar archivos y consultar resultados.

**Elaboracion.** Se adopto MVC como organizacion principal y se mantuvieron DAO
y servicios como capas internas para persistencia y reglas de negocio.

**Construccion.** Se desarrollaron modelos, DAO, servicios, vista, controladores,
validacion CSV, transaccion JDBC en la ejecucion y pruebas incrementales.

**Transicion.** Se agregaron scripts de compilacion, modo demostracion, modo
MySQL, datos de ejemplo y documentacion.

## 4. Arquitectura

```text
vista/MenuConsolaView
        |
controlador/
        |
servicio/
        |
dao/ -------- JDBC -------- MySQL
        |
modelo/
```

`principal.Main` queda limitado a abrir la conexion, crear DAOs, servicios,
vista y controladores, cargar datos demo e iniciar la aplicacion.

`modelo` contiene `Usuario`, `Rol`, `ProcesoETL`, `ArchivoFuente`,
`EjecucionETL`, `ErrorValidacion`, `LogEjecucion` y `Alerta`.

`dao` encapsula las sentencias SQL con `PreparedStatement`. La aplicacion final
trabaja integrada con MySQL; el almacenamiento en memoria queda reservado para
la prueba automatica de logica sin afectar la base real.

`servicio` contiene autenticacion, administracion de procesos, validacion de
datos y coordinacion de ejecuciones.

`vista` muestra el menu, lee datos, confirma operaciones y presenta resultados.
`controlador` recibe esas acciones, invoca servicios y maneja errores de negocio
sin SQL ni JDBC directo.

## 5. Programacion orientada a objetos

**Encapsulamiento.** Los modelos mantienen atributos privados y validan sus
valores mediante constructores y metodos.

**Abstraccion.** `EventoEjecucion` representa cualquier evento asociado a una
ejecucion y declara el metodo abstracto `resumir()`.

**Herencia.** `ErrorValidacion` y `LogEjecucion` heredan de
`EventoEjecucion`.

**Polimorfismo.** `EjecucionETLService` combina errores y logs como una lista de
`EventoEjecucion` e invoca `resumir()`. Cada subclase produce su propio
resultado.

## 6. Flujo de ejecucion CSV

1. El usuario elige un proceso activo.
2. `ArchivoFuenteDAO` registra el archivo.
3. `EjecucionETLDAO` crea la ejecucion.
4. `ValidadorDatosService` abre el CSV con `BufferedReader`.
5. Un ciclo `while` verifica cada fila y su cantidad de columnas.
6. Los errores se guardan mediante `ErrorValidacionDAO`.
7. Se actualiza la ejecucion como `EXITOSO` o `FALLIDO`.
8. Se guardan logs y, si corresponde, una alerta pendiente.
9. En modo MySQL, el flujo se ejecuta dentro de una transaccion JDBC: commit si
   termina correctamente y rollback ante errores inesperados.

## 7. Base de datos

El script `sql/sigecad.sql` crea las ocho tablas del modelo presentado:

- `roles`
- `usuarios`
- `procesos_etl`
- `archivos_fuente`
- `ejecuciones_etl`
- `errores_validacion`
- `alertas`
- `logs_ejecucion`

Las relaciones se implementan con claves foraneas. La configuracion JDBC se
obtiene de variables de entorno para no guardar la clave de MySQL en Java.

## 8. Recursos de Java

- Tipos primitivos, referencias, enumeraciones y objetos.
- Constructores y encapsulamiento.
- `if`, `switch`, operador ternario, `while`, `do-while` y `for`.
- `List`, `Map`, `Queue`, ordenamiento con `Comparator` y busqueda.
- Manejo de `SQLException`, `IOException`, `NumberFormatException`,
  `IllegalArgumentException` e `IllegalStateException`.
- `try-with-resources` para archivos, conexiones y sentencias SQL.

## 9. Evidencia

`probar.bat` verifica alta, consulta por ID, busqueda, modificacion completa,
cambio de estado, baja logica, CSV valido, CSV invalido, historial, errores,
alertas y polimorfismo. `inicializar-mysql.bat` crea las tablas con JDBC y
`ejecutar-mysql.bat` utiliza el esquema real.
