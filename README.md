# SIGECAD - Java nativo, JDBC, MySQL y MVC

SIGECAD es un prototipo academico de consola para administrar procesos ETL,
validar archivos CSV y registrar trazabilidad de ejecuciones, errores, logs y
alertas. El proyecto usa Java estandar, JDBC y MySQL, sin frameworks.

## Arquitectura MVC

```text
Vista -> Controlador -> Servicio -> DAO -> MySQL
  |          |              |        |
  |          |              |        `- PreparedStatement / JDBC
  |          |              `- Reglas de negocio y validaciones
  |          `- Coordinacion de casos de uso
  `- Menu, lectura e impresion en consola
```

```text
src/main/java/sigecad/
|- modelo/        Entidades, enums y abstracciones del dominio
|- dao/           Persistencia JDBC/MySQL
|- servicio/      Reglas de negocio y validacion de CSV
|- vista/         MenuConsolaView
|- controlador/   Controladores MVC
|- principal/     Main, solo ensamble e inicio
```

DAO y Service complementan MVC: la Vista no conoce SQL ni reglas de negocio; el
Controlador coordina; el Servicio valida y aplica reglas; el DAO persiste.

## Requisitos

- JDK 17 o superior.
- MySQL 8 para modo MySQL.
- MySQL Connector/J como `lib/mysql-connector-j.jar` para modo MySQL.

## Ejecucion integrada con MySQL

El programa de consola usa MySQL obligatoriamente. `ejecutar-demo.bat` queda
solo como aviso para no ejecutar una version en memoria por error.

## Crear las tablas MySQL

Si tenes el cliente MySQL instalado, podes ejecutar el script:

```sql
source sql/sigecad.sql;
```

Tambien podes importarlo desde MySQL Workbench. El proyecto incluye ademas un
inicializador Java para crear la estructura sin depender del cliente `mysql`:

```powershell
$env:SIGECAD_DB_SERVER_URL="jdbc:mysql://localhost:3306/"
$env:SIGECAD_DB_USER="root"
$env:SIGECAD_DB_PASSWORD="tu_clave"
.\inicializar-mysql.bat
```

Luego ejecutar la aplicacion:

```powershell
$env:SIGECAD_DB_URL="jdbc:mysql://localhost:3306/sigecad"
$env:SIGECAD_DB_USER="root"
$env:SIGECAD_DB_PASSWORD="tu_clave"
.\ejecutar-mysql.bat
```

## Pruebas

```powershell
.\probar.bat
```

La prueba usa DAOs en memoria para validar la logica sin tocar la base real.
Verifica alta, busqueda, consulta por ID, modificacion completa, cambio de
estado, baja logica, CSV valido, CSV invalido, errores, alertas, historial y
polimorfismo.

## Funcionalidades demostrables

- Alta de procesos ETL.
- Consulta y listado.
- Busqueda por texto.
- Modificacion completa de procesos.
- Cambio de estado activo/inactivo.
- Baja logica.
- Validacion de CSV.
- Persistencia MySQL mediante JDBC.
- Registro de logs, errores de validacion y alertas.

## Alcance del CSV

El prototipo valida estructura y registra trazabilidad en `archivos_fuente`,
`ejecuciones_etl`, `errores_validacion`, `logs_ejecucion` y `alertas`. No carga
filas en tablas de negocio porque el modelo SQL entregado no define todavia
`tabla_ventas` ni `tabla_clientes`.
