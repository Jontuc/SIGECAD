# SIGECAD - Java nativo y MySQL

Prototipo de consola para registrar procesos ETL, validar archivos CSV y
persistir la trazabilidad de sus ejecuciones. No utiliza frameworks.

## Estructura

```text
src/
|- main/java/sigecad/
|  |- modelo/       Objetos correspondientes a las tablas
|  |- dao/          Acceso a datos con JDBC o memoria
|  |- servicio/     Reglas de negocio y validacion CSV
|  |- principal/    Menu de consola
|- test/java/       Prueba funcional
sql/
|- sigecad.sql      Modelo MySQL de SIGECAD
datos/
|- ventas.csv
|- ventas_error.csv
```

## Ejecucion sin MySQL

```powershell
.\ejecutar-demo.bat
```

En el menu, la opcion 4 permite probar:

- Archivo valido: `datos\ventas.csv`
- Archivo invalido: `datos\ventas_error.csv`
- Separador: `;`

## Pruebas

```powershell
.\probar.bat
```

## Ejecucion con MySQL

1. Ejecutar `sql/sigecad.sql` en MySQL.
2. Guardar MySQL Connector/J como `lib/mysql-connector-j.jar`.
3. Configurar la conexion y ejecutar:

```powershell
$env:SIGECAD_DB_URL="jdbc:mysql://localhost:3306/sigecad"
$env:SIGECAD_DB_USER="root"
$env:SIGECAD_DB_PASSWORD="tu_clave"
.\ejecutar-mysql.bat
```

## Alcance del CSV

El prototipo abre el CSV, valida su extension, existencia y cantidad de
columnas, y persiste:

- El archivo recibido en `archivos_fuente`.
- La ejecucion en `ejecuciones_etl`.
- Los errores en `errores_validacion`.
- Los eventos en `logs_ejecucion`.
- Una alerta en `alertas` cuando falla.

No inserta las filas en `tabla_ventas` o `tabla_clientes` porque esas tablas de
negocio no estan definidas en el modelo SQL entregado.
