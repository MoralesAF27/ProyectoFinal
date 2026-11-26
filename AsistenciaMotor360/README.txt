ASISTENCIA MOTOR 360
Sistema de informacion para la gestion de servicios de asistencia vial
Universidad Distrital Francisco Jose de Caldas - Ingenieria de Sistemas
Proyecto Final - Ciencias de la Computacion I - 2026-3

CONTENIDO

1. Codigo fuente Eclipse       -> carpeta src
2. Documentacion JavaDoc       -> carpeta docs (abrir index.html)
3. Documento del proyecto      -> carpeta entregables (documento_proyecto.pdf, 10 paginas)
4. Reporte CSV de ejemplo      -> carpeta entregables (muestra_reporte.csv)

REQUISITOS

- Eclipse IDE con Java 17 o superior.
- No requiere bibliotecas externas. La interfaz usa Swing y todos los datos se
  almacenan en estructuras de datos propias.

IMPORTACION EN ECLIPSE

1. File > Import > General > Existing Projects into Workspace.
2. Seleccione la carpeta AsistenciaMotor360.
3. Ejecute la clase asistenciamotor360.app.Main.

La aplicacion abre el Centro de control con las pestanas: Panel de control,
Unidades moviles, Tecnicos, Clientes, Solicitudes, Despacho, Insumos,
Supervisor y Reportes.

FLUJO RECOMENDADO PARA LA SUSTENTACION

1. Menu Sistema > Cargar datos de demostracion.
2. En Solicitudes observe la evaluacion automatica de prioridad y las dos
   colas: prioritaria y ordinaria (FIFO estricto).
3. En Despacho atienda primero la solicitud prioritaria con los recursos
   disponibles. Tambien puede usar el despacho automatico.
4. Marque la solicitud como atendida y luego solucionela. La unidad y el
   tecnico quedan nuevamente disponibles.
5. En Supervisor deshaga las ultimas transacciones y observe la restauracion
   paso a paso en orden inverso.
6. En Insumos apile y despache maletines y repuestos verificando el
   comportamiento LIFO (el ultimo apilado sale primero). Envie un maletin a
   auditoria y reincorporarlo.
7. En Reportes exporte el CSV de incidencias solucionadas de la jornada.

REGLAS DE NEGOCIO CUBIERTAS

- UUID universal unico para cada unidad movil.
- Categoria, estatus, zona y nivel de disponibilidad obligatorios.
- Identificacion, especialidad y estado vigente del personal tecnico.
- Una unidad no puede figurar disponible y asignada al mismo tiempo.
- Un tecnico no puede tener mas de un servicio activo.
- Una unidad en mantenimiento no se puede asignar.
- Toda solicitud requiere un cliente registrado.
- Estado atendida solo con recursos asignados.
- El servicio solucionado se retira del flujo pendiente.
- Las solicitudes ordinarias se atienden estrictamente por turno de llegada.
- Las solicitudes criticas se escalan segun riesgo, afectacion vial, perfil del
  cliente y severidad mediante un puntaje maximo de 50 puntos.
- Deshacer las ultimas transacciones en orden inverso.
- Maletines y repuestos con despacho LIFO.
- Exportacion CSV de las incidencias solucionadas del dia.

ESTRUCTURAS DE DATOS PROPIAS

Lista, Pila, Cola circular, Cola de prioridad con monticulo binario y TablaHash
con encadenamiento. No se utiliza ninguna API del framework de colecciones de
Java; solo se emplea java.util.UUID como generador de identificadores.

ARQUITECTURA MVC

- Modelo: asistenciamotor360.modelo y asistenciamotor360.modelo.estructuras.
- Vista: asistenciamotor360.vista y asistenciamotor360.vista.componentes.
- Controlador: asistenciamotor360.controlador.
- Arranque: asistenciamotor360.app.Main.

INTEGRANTES DEL GRUPO

<COMPLETAR CON NOMBRES Y CODIGOS DE LOS INTEGRANTES>

OBSERVACION

El archivo ZIP de entrega debe nombrarse CC1_202603_<APELLIDOS_NOMBRES>.zip
segun lo indicado por el docente.
