package asistenciamotor360.modelo;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;

import asistenciamotor360.modelo.estructuras.Lista;

/**
 * Genera el reporte plano en formato CSV con las incidencias solucionadas
 * durante la jornada. El archivo se escribe en UTF-8 con separador punto y coma
 * para facilitar su apertura en hojas de calculo.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ExportadorCSV {

    private static final String SEPARADOR = ";";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm:ss");

    /**
     * Constructor privado de la clase utilitaria.
     */
    private ExportadorCSV() {
    }

    /**
     * Escribe el reporte CSV con las solicitudes solucionadas indicadas.
     *
     * @param solicitudes      solicitudes que se consolidaran en el reporte.
     * @param rutaArchivo      ruta del archivo plano que se desea generar.
     * @return cantidad de incidencias escritas en el reporte.
     * @throws IOException si ocurre un error de escritura en el archivo.
     */
    public static int exportar(Lista<Solicitud> solicitudes, String rutaArchivo)
            throws IOException {
        BufferedWriter escritor = null;
        try {
            escritor = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(rutaArchivo), StandardCharsets.UTF_8));
            escritor.write('\uFEFF');
            escritor.write(encabezado());
            escritor.newLine();
            for (int i = 0; i < solicitudes.tamano(); i++) {
                escritor.write(linea(solicitudes.obtener(i)));
                escritor.newLine();
            }
            escritor.flush();
        } finally {
            if (escritor != null) {
                escritor.close();
            }
        }
        return solicitudes.tamano();
    }

    private static String encabezado() {
        return "ID solicitud" + SEPARADOR
                + "Fecha recepcion" + SEPARADOR
                + "Hora recepcion" + SEPARADOR
                + "Fecha solucion" + SEPARADOR
                + "Hora solucion" + SEPARADOR
                + "Cliente" + SEPARADOR
                + "Identificacion cliente" + SEPARADOR
                + "Tipo de cliente" + SEPARADOR
                + "Tipo de incidente" + SEPARADOR
                + "Zona" + SEPARADOR
                + "Prioridad" + SEPARADOR
                + "Puntaje" + SEPARADOR
                + "Nivel de riesgo" + SEPARADOR
                + "Afectacion vial" + SEPARADOR
                + "Severidad" + SEPARADOR
                + "Unidad UUID" + SEPARADOR
                + "Unidad codigo" + SEPARADOR
                + "Unidad tipo" + SEPARADOR
                + "Tecnico" + SEPARADOR
                + "Tecnico identificacion" + SEPARADOR
                + "Minutos de atencion" + SEPARADOR
                + "Descripcion";
    }

    private static String linea(Solicitud solicitud) {
        String fechaRecepcion = solicitud.getFechaRecepcion() == null ? ""
                : solicitud.getFechaRecepcion().format(FORMATO_FECHA);
        String horaRecepcion = solicitud.getFechaRecepcion() == null ? ""
                : solicitud.getFechaRecepcion().format(FORMATO_HORA);
        String fechaSolucion = solicitud.getFechaSolucion() == null ? ""
                : solicitud.getFechaSolucion().format(FORMATO_FECHA);
        String horaSolucion = solicitud.getFechaSolucion() == null ? ""
                : solicitud.getFechaSolucion().format(FORMATO_HORA);
        String unidadUuid = solicitud.getUnidad() == null ? "" : solicitud.getUnidad().getUuid();
        String unidadCodigo = solicitud.getUnidad() == null ? ""
                : solicitud.getUnidad().getCodigo();
        String unidadTipo = solicitud.getUnidad() == null ? ""
                : solicitud.getUnidad().getTipo().getEtiqueta();
        String tecnicoNombre = solicitud.getTecnico() == null ? ""
                : solicitud.getTecnico().getNombre();
        String tecnicoIdentificacion = solicitud.getTecnico() == null ? ""
                : solicitud.getTecnico().getIdentificacion();
        String minutos = String.valueOf(minutosAtencion(solicitud));

        StringBuilder linea = new StringBuilder();
        linea.append(texto(solicitud.getId())).append(SEPARADOR);
        linea.append(texto(fechaRecepcion)).append(SEPARADOR);
        linea.append(texto(horaRecepcion)).append(SEPARADOR);
        linea.append(texto(fechaSolucion)).append(SEPARADOR);
        linea.append(texto(horaSolucion)).append(SEPARADOR);
        linea.append(texto(solicitud.getCliente().getNombre())).append(SEPARADOR);
        linea.append(texto(solicitud.getCliente().getIdentificacion())).append(SEPARADOR);
        linea.append(texto(solicitud.getCliente().getTipo().getEtiqueta())).append(SEPARADOR);
        linea.append(texto(solicitud.getTipoIncidente().getEtiqueta())).append(SEPARADOR);
        linea.append(texto(solicitud.getZona().getEtiqueta())).append(SEPARADOR);
        linea.append(texto(solicitud.isPrioritaria() ? "Prioritaria" : "Ordinaria")).append(SEPARADOR);
        linea.append(solicitud.getPuntajePrioridad()).append(SEPARADOR);
        linea.append(solicitud.getNivelRiesgo()).append(SEPARADOR);
        linea.append(solicitud.getAfectacionVial()).append(SEPARADOR);
        linea.append(solicitud.getSeveridad()).append(SEPARADOR);
        linea.append(texto(unidadUuid)).append(SEPARADOR);
        linea.append(texto(unidadCodigo)).append(SEPARADOR);
        linea.append(texto(unidadTipo)).append(SEPARADOR);
        linea.append(texto(tecnicoNombre)).append(SEPARADOR);
        linea.append(texto(tecnicoIdentificacion)).append(SEPARADOR);
        linea.append(minutos).append(SEPARADOR);
        linea.append(texto(solicitud.getDescripcion()));
        return linea.toString();
    }

    private static long minutosAtencion(Solicitud solicitud) {
        if (solicitud.getFechaRecepcion() == null || solicitud.getFechaSolucion() == null) {
            return 0;
        }
        return java.time.Duration.between(solicitud.getFechaRecepcion(),
                solicitud.getFechaSolucion()).toMinutes();
    }

    private static String texto(String valor) {
        String limpio = valor == null ? "" : valor.replace("\"", "\"\"");
        return "\"" + limpio + "\"";
    }
}
