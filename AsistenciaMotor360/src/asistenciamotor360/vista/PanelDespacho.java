package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Solicitud;
import asistenciamotor360.modelo.Tecnico;
import asistenciamotor360.modelo.UnidadMovil;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloCombo;
import asistenciamotor360.vista.componentes.ModeloTabla;

public class PanelDespacho extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JLabel etiquetaSiguiente;
    private final ModeloCombo<UnidadMovil> modeloUnidades;
    private final ModeloCombo<Tecnico> modeloTecnicos;
    private final ModeloTabla modeloTabla;
    private final JTable tabla;
    private Lista<Solicitud> activas;

    public PanelDespacho(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.activas = new Lista<Solicitud>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        etiquetaSiguiente = new JLabel("Sin solicitudes pendientes.");
        etiquetaSiguiente.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        modeloUnidades = new ModeloCombo<UnidadMovil>();
        modeloTecnicos = new ModeloCombo<Tecnico>();

        Formulario formulario = new Formulario();
        formulario.agregarAnchoCompleto(etiquetaSiguiente);
        formulario.agregar("Unidad disponible", new JComboBox<UnidadMovil>(modeloUnidades));
        formulario.agregar("Tecnico disponible", new JComboBox<Tecnico>(modeloTecnicos));

        JButton botonDespachar = Componentes.boton("Despachar siguiente solicitud");
        botonDespachar.addActionListener(e -> despachar());
        JButton botonAutomatico = Componentes.boton("Despacho automatico");
        botonAutomatico.addActionListener(e -> controlador.despachoAutomatico());
        JPanel botonesDespacho = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        botonesDespacho.add(botonDespachar);
        botonesDespacho.add(botonAutomatico);
        formulario.agregarAnchoCompleto(botonesDespacho);

        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Despacho de recursos"));
        panelSuperior.add(formulario, BorderLayout.CENTER);
        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(new String[] {
            "Solicitud", "Cliente", "Incidente", "Zona", "Unidad", "Tecnico", "Estado",
            "Asignada"
        });
        tabla = Componentes.tabla(modeloTabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Rescates activos"));
        add(desplazamiento, BorderLayout.CENTER);

        JButton botonAtendida = Componentes.boton("Marcar atendida");
        botonAtendida.addActionListener(e -> marcarAtendida());
        JButton botonSolucionar = Componentes.boton("Solucionar y liberar recursos");
        botonSolucionar.addActionListener(e -> solucionar());
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panelInferior.add(botonAtendida);
        panelInferior.add(botonSolucionar);
        panelInferior.add(Componentes.etiqueta(
                "Seleccione un rescate activo para marcarlo atendido o solucionarlo."));
        add(panelInferior, BorderLayout.SOUTH);
        actualizar();
    }

    private void despachar() {
        UnidadMovil unidad = modeloUnidades.getSeleccionado();
        Tecnico tecnico = modeloTecnicos.getSeleccionado();
        controlador.despacharSiguiente(unidad == null ? null : unidad.getUuid(),
                tecnico == null ? null : tecnico.getIdentificacion());
    }

    private void marcarAtendida() {
        Solicitud solicitud = seleccionada();
        if (solicitud == null) {
            controlador.error("Seleccione un rescate activo en la tabla.");
            return;
        }
        controlador.marcarAtendida(solicitud.getId());
    }

    private void solucionar() {
        Solicitud solicitud = seleccionada();
        if (solicitud == null) {
            controlador.error("Seleccione un rescate activo en la tabla.");
            return;
        }
        controlador.solucionarSolicitud(solicitud.getId());
    }

    private Solicitud seleccionada() {
        int fila = tabla.getSelectedRow();
        if (fila < 0 || fila >= activas.tamano()) {
            return null;
        }
        return activas.obtener(fila);
    }

    @Override
    public void actualizar() {
        modeloUnidades.setElementos(controlador.listarUnidadesDisponibles());
        modeloTecnicos.setElementos(controlador.listarTecnicosDisponibles());

        Solicitud siguiente = controlador.solicitudSiguiente();
        if (siguiente == null) {
            etiquetaSiguiente.setText("Sin solicitudes pendientes en el flujo de atencion.");
        } else {
            etiquetaSiguiente.setText("<html><b>Siguiente en atencion:</b> " + siguiente.getId()
                    + " | " + siguiente.getCliente().getNombre() + " | "
                    + siguiente.getTipoIncidente().getEtiqueta() + " | "
                    + siguiente.getZona().getEtiqueta() + " | puntaje "
                    + siguiente.getPuntajePrioridad() + "<br><i>"
                    + siguiente.getMotivoPrioridad() + "</i></html>");
        }

        activas = controlador.listarServiciosActivos();
        Lista<String[]> filas = new Lista<String[]>();
        for (int i = 0; i < activas.tamano(); i++) {
            Solicitud solicitud = activas.obtener(i);
            filas.agregar(new String[] {
                solicitud.getId(), solicitud.getCliente().getNombre(),
                solicitud.getTipoIncidente().getEtiqueta(), solicitud.getZona().getEtiqueta(),
                solicitud.getUnidad() == null ? "-" : solicitud.getUnidad().getCodigo(),
                solicitud.getTecnico() == null ? "-" : solicitud.getTecnico().getNombre(),
                solicitud.getEstado().getEtiqueta(),
                solicitud.getFechaAsignacion() == null ? "-"
                        : formatearHora(solicitud.getFechaAsignacion())
            });
        }
        modeloTabla.setFilas(filas);
    }

    private String formatearHora(java.time.LocalDateTime fecha) {
        return String.format("%02d:%02d", fecha.getHour(), fecha.getMinute());
    }
}