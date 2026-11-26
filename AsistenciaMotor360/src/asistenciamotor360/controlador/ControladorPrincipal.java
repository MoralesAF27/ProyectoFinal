package asistenciamotor360.controlador;

import java.io.IOException;

import javax.swing.JOptionPane;

import asistenciamotor360.modelo.Cliente;
import asistenciamotor360.modelo.Especialidad;
import asistenciamotor360.modelo.EstadoTecnico;
import asistenciamotor360.modelo.EstadoUnidad;
import asistenciamotor360.modelo.Maletin;
import asistenciamotor360.modelo.NivelDisponibilidad;
import asistenciamotor360.modelo.RegistroAccion;
import asistenciamotor360.modelo.Repuesto;
import asistenciamotor360.modelo.SistemaAsistencia;
import asistenciamotor360.modelo.Solicitud;
import asistenciamotor360.modelo.Tecnico;
import asistenciamotor360.modelo.TipoCliente;
import asistenciamotor360.modelo.TipoIncidente;
import asistenciamotor360.modelo.TipoUnidad;
import asistenciamotor360.modelo.Transaccion;
import asistenciamotor360.modelo.UnidadMovil;
import asistenciamotor360.modelo.ZonaOperacion;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.VentanaPrincipal;

/**
 * Controlador principal de la aplicacion. Recibe las solicitudes de la vista,
 * invoca las operaciones del modelo, presenta los resultados y ordena la
 * actualizacion de la interfaz, manteniendo la separacion propia del patron
 * MVC.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ControladorPrincipal {

    private final SistemaAsistencia sistema;
    private VentanaPrincipal vista;

    /**
     * Crea el controlador asociado al modelo de negocio.
     *
     * @param sistema modelo de negocio del sistema.
     */
    public ControladorPrincipal(SistemaAsistencia sistema) {
        this.sistema = sistema;
    }

    /**
     * Vincula la ventana principal con el controlador.
     *
     * @param vista ventana principal de la aplicacion.
     */
    public void conectarVista(VentanaPrincipal vista) {
        this.vista = vista;
    }

    /**
     * Registra una unidad movil en el sistema.
     *
     * @param codigo         codigo o placa de la unidad.
     * @param tipo           categoria de la unidad.
     * @param zona           zona de operacion.
     * @param disponibilidad nivel de disponibilidad.
     */
    public void registrarUnidad(String codigo, TipoUnidad tipo, ZonaOperacion zona,
            NivelDisponibilidad disponibilidad) {
        try {
            UnidadMovil unidad = sistema.registrarUnidad(codigo, tipo, zona, disponibilidad);
            mensaje("Unidad " + unidad.getCodigo() + " registrada con UUID " + unidad.getUuid());
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Cambia el estado operativo de una unidad movil.
     *
     * @param unidad identificador universal de la unidad.
     * @param estado nuevo estado.
     */
    public void cambiarEstadoUnidad(String unidad, EstadoUnidad estado) {
        if (unidad == null || estado == null) {
            error("Seleccione la unidad y el nuevo estado.");
            return;
        }
        try {
            sistema.cambiarEstadoUnidad(unidad, estado);
            mensaje("Estado de la unidad actualizado a " + estado.getEtiqueta() + ".");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Registra un tecnico en el sistema.
     *
     * @param identificacion documento del tecnico.
     * @param nombre         nombre completo.
     * @param especialidad   area de especialidad.
     * @param sector         sector de patrullaje.
     */
    public void registrarTecnico(String identificacion, String nombre, Especialidad especialidad,
            ZonaOperacion sector) {
        try {
            Tecnico tecnico = sistema.registrarTecnico(identificacion, nombre, especialidad,
                    sector);
            mensaje("Tecnico " + tecnico.getNombre() + " registrado en "
                    + tecnico.getSector().getEtiqueta() + ".");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Cambia el estado de un tecnico.
     *
     * @param identificacion identificacion del tecnico.
     * @param estado         nuevo estado.
     */
    public void cambiarEstadoTecnico(String identificacion, EstadoTecnico estado) {
        if (identificacion == null || estado == null) {
            error("Seleccione el tecnico y el nuevo estado.");
            return;
        }
        try {
            sistema.cambiarEstadoTecnico(identificacion, estado);
            mensaje("Estado del tecnico actualizado a " + estado.getEtiqueta() + ".");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Registra un cliente en el sistema.
     *
     * @param identificacion documento o NIT.
     * @param nombre         nombre o razon social.
     * @param tipo           perfil del cliente.
     * @param telefono       telefono de contacto.
     * @param direccion      direccion principal.
     */
    public void registrarCliente(String identificacion, String nombre, TipoCliente tipo,
            String telefono, String direccion) {
        try {
            Cliente cliente = sistema.registrarCliente(identificacion, nombre, tipo, telefono,
                    direccion);
            mensaje("Cliente " + cliente.getNombre() + " registrado.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Registra una solicitud de asistencia vinculada a un cliente.
     *
     * @param cliente       identificacion del cliente.
     * @param incidente     tipo de incidente.
     * @param zona          zona del incidente.
     * @param riesgo        nivel de riesgo entre uno y cinco.
     * @param afectacion    afectacion vial entre uno y cinco.
     * @param severidad     severidad entre uno y cinco.
     * @param descripcion   detalle del incidente.
     * @return solicitud registrada o nula si se presento un error.
     */
    public Solicitud registrarSolicitud(String cliente, TipoIncidente incidente,
            ZonaOperacion zona, int riesgo, int afectacion, int severidad, String descripcion) {
        try {
            Solicitud solicitud = sistema.crearSolicitud(cliente, incidente, zona, riesgo,
                    afectacion, severidad, descripcion);
            mensaje("Solicitud " + solicitud.getId() + " registrada en la cola "
                    + (solicitud.isPrioritaria() ? "prioritaria" : "ordinaria") + ".");
            return solicitud;
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
            return null;
        } finally {
            refrescar();
        }
    }

    /**
     * Escala una solicitud pendiente al flujo prioritario.
     *
     * @param idSolicitud identificador de la solicitud.
     */
    public void escalarSolicitud(String idSolicitud) {
        if (idSolicitud == null) {
            error("Seleccione una solicitud ordinaria para escalar.");
            return;
        }
        try {
            sistema.escalarSolicitud(idSolicitud);
            mensaje("Solicitud " + idSolicitud + " promovida a atencion prioritaria.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Despacha la siguiente solicitud pendiente con los recursos indicados.
     *
     * @param unidad  identificador universal de la unidad.
     * @param tecnico identificacion del tecnico.
     */
    public void despacharSiguiente(String unidad, String tecnico) {
        Solicitud siguiente = sistema.solicitudSiguiente();
        if (siguiente == null) {
            error("No hay solicitudes pendientes para despachar.");
            return;
        }
        if (unidad == null || tecnico == null) {
            error("Seleccione una unidad y un tecnico disponibles.");
            return;
        }
        try {
            sistema.despachar(siguiente.getId(), unidad, tecnico);
            mensaje("Solicitud " + siguiente.getId() + " despachada.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Ejecuta el despacho automatico de la siguiente solicitud pendiente.
     */
    public void despachoAutomatico() {
        try {
            Solicitud solicitud = sistema.despachoAutomatico();
            mensaje("Despacho automatico de la solicitud " + solicitud.getId() + " completado.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Marca una solicitud como atendida.
     *
     * @param idSolicitud identificador de la solicitud.
     */
    public void marcarAtendida(String idSolicitud) {
        if (idSolicitud == null) {
            error("Seleccione un servicio activo.");
            return;
        }
        try {
            sistema.marcarAtendida(idSolicitud);
            mensaje("Solicitud " + idSolicitud + " marcada como atendida.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Soluciona una solicitud activa y libera sus recursos.
     *
     * @param idSolicitud identificador de la solicitud.
     */
    public void solucionarSolicitud(String idSolicitud) {
        if (idSolicitud == null) {
            error("Seleccione un servicio activo.");
            return;
        }
        try {
            sistema.solucionarSolicitud(idSolicitud);
            mensaje("Solicitud " + idSolicitud + " solucionada y retirada del flujo pendiente.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Deshace la ultima transaccion registrada en el sistema.
     *
     * @return descripcion de la transaccion deshecha o nula si no fue posible.
     */
    public String deshacerUltimaTransaccion() {
        try {
            Transaccion transaccion = sistema.deshacerUltimaTransaccion();
            mensaje("Se deshizo la transaccion #" + transaccion.getConsecutivo() + ": "
                    + transaccion.getDescripcion());
            return transaccion.toString();
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
            return null;
        } finally {
            refrescar();
        }
    }

    /**
     * Registra un maletin de respuesta rapida.
     *
     * @param codigo    codigo del maletin.
     * @param contenido contenido del maletin.
     */
    public void registrarMaletin(String codigo, String contenido) {
        try {
            Maletin maletin = sistema.registrarMaletin(codigo, contenido);
            mensaje("Maletin " + maletin.getCodigo() + " apilado en la estanteria.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Despacha el ultimo maletin apilado.
     */
    public void despacharMaletin() {
        try {
            Maletin maletin = sistema.despacharMaletin();
            mensaje("Maletin " + maletin.getCodigo() + " despachado para servicio.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Envia un maletin utilizado a la estacion de auditoria.
     *
     * @param codigo codigo del maletin.
     */
    public void enviarMaletinAuditoria(String codigo) {
        if (codigo == null) {
            error("Seleccione un maletin en servicio.");
            return;
        }
        try {
            sistema.enviarMaletinAuditoria(codigo);
            mensaje("Maletin " + codigo + " enviado a la estacion de auditoria.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Audita el maletin ubicado en la cima de la estacion.
     *
     * @param aprobado verdadero si el maletin se reincorpora a la estanteria.
     */
    public void auditarMaletin(boolean aprobado) {
        try {
            Maletin maletin = sistema.auditarMaletin(aprobado);
            mensaje("Auditoria del maletin " + maletin.getCodigo()
                    + (aprobado ? " aprobada." : " rechazada."));
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Apila una pieza de repuesto de alta rotacion.
     *
     * @param codigo codigo de la pieza.
     * @param nombre nombre de la pieza.
     */
    public void apilarRepuesto(String codigo, String nombre) {
        try {
            Repuesto repuesto = sistema.apilarRepuesto(codigo, nombre);
            mensaje("Repuesto " + repuesto.getCodigo() + " apilado en el almacen.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Despacha la ultima pieza de repuesto apilada.
     */
    public void despacharRepuesto() {
        try {
            Repuesto repuesto = sistema.despacharRepuesto();
            mensaje("Repuesto " + repuesto.getCodigo() + " despachado desde la cima.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Exporta el reporte CSV de las incidencias solucionadas en la jornada.
     *
     * @param ruta ruta del archivo que se desea generar.
     * @return cantidad de incidencias exportadas o menos uno si hubo error.
     */
    public int exportarCSV(String ruta) {
        try {
            int total = sistema.exportarReporteCSV(ruta);
            mensaje("Reporte CSV generado con " + total + " incidencias en " + ruta);
            return total;
        } catch (IOException excepcion) {
            error("No fue posible escribir el archivo: " + excepcion.getMessage());
            return -1;
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
            return -1;
        } finally {
            refrescar();
        }
    }

    /**
     * Carga datos de demostracion para la sustentacion del proyecto.
     */
    public void cargarDatosDemostracion() {
        try {
            sistema.cargarDatosDemostracion();
            mensaje("Datos de demostracion cargados correctamente.");
        } catch (RuntimeException excepcion) {
            error(excepcion.getMessage());
        } finally {
            refrescar();
        }
    }

    /**
     * Retorna la descripcion del calculo de prioridad de una solicitud.
     *
     * @param riesgo       nivel de riesgo entre uno y cinco.
     * @param afectacion   afectacion vial entre uno y cinco.
     * @param severidad    severidad entre uno y cinco.
     * @param tipoCliente  perfil del cliente.
     * @return texto explicativo del puntaje.
     */
    public String describirPrioridad(int riesgo, int afectacion, int severidad,
            TipoCliente tipoCliente) {
        return sistema.describirPrioridad(riesgo, afectacion, severidad, tipoCliente);
    }

    /**
     * Retorna la siguiente solicitud pendiente de atencion.
     *
     * @return solicitud siguiente o nula si no hay pendientes.
     */
    public Solicitud solicitudSiguiente() {
        return sistema.solicitudSiguiente();
    }

    /**
     * Retorna las unidades registradas.
     *
     * @return lista de unidades.
     */
    public Lista<UnidadMovil> listarUnidades() {
        return sistema.listarUnidades();
    }

    /**
     * Retorna las unidades disponibles.
     *
     * @return lista de unidades disponibles.
     */
    public Lista<UnidadMovil> listarUnidadesDisponibles() {
        return sistema.listarUnidadesDisponibles();
    }

    /**
     * Retorna las unidades en taller o mantenimiento.
     *
     * @return lista de unidades fuera de operacion.
     */
    public Lista<UnidadMovil> listarUnidadesEnTaller() {
        return sistema.listarUnidadesEnTaller();
    }

    /**
     * Retorna los tecnicos registrados.
     *
     * @return lista de tecnicos.
     */
    public Lista<Tecnico> listarTecnicos() {
        return sistema.listarTecnicos();
    }

    /**
     * Retorna los tecnicos disponibles.
     *
     * @return lista de tecnicos disponibles.
     */
    public Lista<Tecnico> listarTecnicosDisponibles() {
        return sistema.listarTecnicosDisponibles();
    }

    /**
     * Retorna los clientes registrados.
     *
     * @return lista de clientes.
     */
    public Lista<Cliente> listarClientes() {
        return sistema.listarClientes();
    }

    /**
     * Retorna las solicitudes ordinarias pendientes.
     *
     * @return lista de solicitudes ordinarias.
     */
    public Lista<Solicitud> listarColaOrdinaria() {
        return sistema.listarColaOrdinaria();
    }

    /**
     * Retorna las solicitudes prioritarias pendientes.
     *
     * @return lista de solicitudes prioritarias.
     */
    public Lista<Solicitud> listarColaPrioritaria() {
        return sistema.listarColaPrioritaria();
    }

    /**
     * Retorna los servicios activos.
     *
     * @return lista de servicios activos.
     */
    public Lista<Solicitud> listarServiciosActivos() {
        return sistema.listarServiciosActivos();
    }

    /**
     * Retorna las incidencias solucionadas en la jornada.
     *
     * @return lista de solicitudes solucionadas.
     */
    public Lista<Solicitud> listarServiciosSolucionados() {
        return sistema.listarServiciosSolucionados();
    }

    /**
     * Retorna los maletines disponibles.
     *
     * @return lista de maletines disponibles.
     */
    public Lista<Maletin> listarMaletinesDisponibles() {
        return sistema.listarMaletinesDisponibles();
    }

    /**
     * Retorna los maletines en auditoria.
     *
     * @return lista de maletines en auditoria.
     */
    public Lista<Maletin> listarMaletinesAuditoria() {
        return sistema.listarMaletinesAuditoria();
    }

    /**
     * Retorna los maletines en servicio.
     *
     * @return lista de maletines en servicio.
     */
    public Lista<Maletin> listarMaletinesEnServicio() {
        return sistema.listarMaletinesEnServicio();
    }

    /**
     * Retorna los maletines rechazados en auditoria.
     *
     * @return lista de maletines descartados.
     */
    public Lista<Maletin> listarMaletinesDescartados() {
        return sistema.listarMaletinesDescartados();
    }

    /**
     * Retorna los repuestos apilados.
     *
     * @return lista de repuestos disponibles.
     */
    public Lista<Repuesto> listarRepuestos() {
        return sistema.listarRepuestos();
    }

    /**
     * Retorna los repuestos despachados durante la jornada.
     *
     * @return lista de repuestos despachados.
     */
    public Lista<Repuesto> listarRepuestosDespachados() {
        return sistema.listarRepuestosDespachados();
    }

    /**
     * Retorna el historial reciente de acciones.
     *
     * @return lista de registros de auditoria.
     */
    public Lista<RegistroAccion> listarHistorial() {
        return sistema.listarHistorial();
    }

    /**
     * Retorna las transacciones pendientes de deshacer.
     *
     * @return lista de transacciones.
     */
    public Lista<Transaccion> listarTransaccionesPendientes() {
        return sistema.listarTransaccionesPendientes();
    }

    /**
     * Retorna la cantidad de transacciones pendientes.
     *
     * @return cantidad de transacciones.
     */
    public int contarTransaccionesPendientes() {
        return sistema.contarTransaccionesPendientes();
    }

    /**
     * Retorna la cantidad de unidades en un estado.
     *
     * @param estado estado consultado.
     * @return cantidad de unidades.
     */
    public int contarUnidades(EstadoUnidad estado) {
        return sistema.contarUnidades(estado);
    }

    /**
     * Retorna la cantidad de tecnicos en un estado.
     *
     * @param estado estado consultado.
     * @return cantidad de tecnicos.
     */
    public int contarTecnicos(EstadoTecnico estado) {
        return sistema.contarTecnicos(estado);
    }

    /**
     * Presenta un mensaje informativo en la barra de estado y en un dialogo.
     *
     * @param texto mensaje que se desea mostrar.
     */
    public void mensaje(String texto) {
        if (vista != null) {
            vista.mostrarMensaje(texto);
        }
    }

    /**
     * Presenta un mensaje de error al usuario.
     *
     * @param texto descripcion del error.
     */
    public void error(String texto) {
        JOptionPane.showMessageDialog(vista, texto, "Regla de negocio",
                JOptionPane.WARNING_MESSAGE);
        if (vista != null) {
            vista.mostrarMensaje("Atencion: " + texto);
        }
    }

    private void refrescar() {
        if (vista != null) {
            vista.actualizarPaneles();
        }
    }
}
