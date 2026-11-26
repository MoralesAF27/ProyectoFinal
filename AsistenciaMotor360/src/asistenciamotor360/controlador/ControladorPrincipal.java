
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

public class ControladorPrincipal {

    private final SistemaAsistencia sistema;
    private VentanaPrincipal vista;

    public ControladorPrincipal(SistemaAsistencia sistema) {
        this.sistema = sistema;
    }

    public void conectarVista(VentanaPrincipal vista) {
        this.vista = vista;
    }

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

    public String describirPrioridad(int riesgo, int afectacion, int severidad,
            TipoCliente tipoCliente) {
        return sistema.describirPrioridad(riesgo, afectacion, severidad, tipoCliente);
    }

    public Solicitud solicitudSiguiente() {
        return sistema.solicitudSiguiente();
    }

    public Lista<UnidadMovil> listarUnidades() {
        return sistema.listarUnidades();
    }

    public Lista<UnidadMovil> listarUnidadesDisponibles() {
        return sistema.listarUnidadesDisponibles();
    }

    public Lista<UnidadMovil> listarUnidadesEnTaller() {
        return sistema.listarUnidadesEnTaller();
    }

    public Lista<Tecnico> listarTecnicos() {
        return sistema.listarTecnicos();
    }

    public Lista<Tecnico> listarTecnicosDisponibles() {
        return sistema.listarTecnicosDisponibles();
    }

    public Lista<Cliente> listarClientes() {
        return sistema.listarClientes();
    }

    public Lista<Solicitud> listarColaOrdinaria() {
        return sistema.listarColaOrdinaria();
    }

    public Lista<Solicitud> listarColaPrioritaria() {
        return sistema.listarColaPrioritaria();
    }

    public Lista<Solicitud> listarServiciosActivos() {
        return sistema.listarServiciosActivos();
    }

    public Lista<Solicitud> listarServiciosSolucionados() {
        return sistema.listarServiciosSolucionados();
    }

    public Lista<Maletin> listarMaletinesDisponibles() {
        return sistema.listarMaletinesDisponibles();
    }

    public Lista<Maletin> listarMaletinesAuditoria() {
        return sistema.listarMaletinesAuditoria();
    }

    public Lista<Maletin> listarMaletinesEnServicio() {
        return sistema.listarMaletinesEnServicio();
    }

    public Lista<Maletin> listarMaletinesDescartados() {
        return sistema.listarMaletinesDescartados();
    }

    public Lista<Repuesto> listarRepuestos() {
        return sistema.listarRepuestos();
    }

    public Lista<Repuesto> listarRepuestosDespachados() {
        return sistema.listarRepuestosDespachados();
    }

    public Lista<RegistroAccion> listarHistorial() {
        return sistema.listarHistorial();
    }

    public Lista<Transaccion> listarTransaccionesPendientes() {
        return sistema.listarTransaccionesPendientes();
    }

    public int contarTransaccionesPendientes() {
        return sistema.contarTransaccionesPendientes();
    }

    public int contarUnidades(EstadoUnidad estado) {
        return sistema.contarUnidades(estado);
    }

    public int contarTecnicos(EstadoTecnico estado) {
        return sistema.contarTecnicos(estado);
    }

    public void mensaje(String texto) {
        if (vista != null) {
            vista.mostrarMensaje(texto);
        }
    }

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
