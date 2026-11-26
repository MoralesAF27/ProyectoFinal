package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Cliente;
import asistenciamotor360.modelo.Solicitud;
import asistenciamotor360.modelo.TipoIncidente;
import asistenciamotor360.modelo.ZonaOperacion;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Catalogos;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloCombo;
import asistenciamotor360.vista.componentes.ModeloLista;

/**
 * Panel de recepcion de solicitudes de asistencia. Muestra la evaluacion de
 * prioridad, las colas de atencion y la opcion de escalar casos criticos.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class PanelSolicitudes extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final ModeloCombo<Cliente> modeloClientes;
    private final ModeloCombo<TipoIncidente> modeloIncidentes;
    private final ModeloCombo<ZonaOperacion> modeloZonas;
    private final ModeloCombo<Integer> modeloRiesgo;
    private final ModeloCombo<Integer> modeloAfectacion;
    private final ModeloCombo<Integer> modeloSeveridad;
    private final JTextField campoDescripcion;
    private final JLabel etiquetaPrioridad;
    private final ModeloLista modeloPrioritarias;
    private final ModeloLista modeloOrdinarias;
    private final JList<String> listaOrdinaria;
    private final JLabel etiquetaSiguiente;
    private Lista<Solicitud> ordinariasActuales;

    /**
     * Crea el panel de solicitudes.
     *
     * @param controlador controlador principal de la aplicacion.
     */
    public PanelSolicitudes(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.ordinariasActuales = new Lista<Solicitud>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        modeloClientes = new ModeloCombo<Cliente>();
        modeloIncidentes = new ModeloCombo<TipoIncidente>();
        modeloIncidentes.setElementos(Catalogos.tiposIncidente());
        modeloZonas = new ModeloCombo<ZonaOperacion>();
        modeloZonas.setElementos(Catalogos.zonas());
        modeloRiesgo = new ModeloCombo<Integer>();
        modeloRiesgo.setElementos(Catalogos.escala());
        modeloAfectacion = new ModeloCombo<Integer>();
        modeloAfectacion.setElementos(Catalogos.escala());
        modeloSeveridad = new ModeloCombo<Integer>();
        modeloSeveridad.setElementos(Catalogos.escala());
        campoDescripcion = new JTextField();
        etiquetaPrioridad = new JLabel("Seleccione un cliente para evaluar la prioridad.");
        etiquetaPrioridad.setFont(new Font("Segoe UI", Font.BOLD, 12));

        Formulario formulario = new Formulario();
        formulario.agregar("Cliente", new JComboBox<Cliente>(modeloClientes));
        formulario.agregar("Tipo de incidente", new JComboBox<TipoIncidente>(modeloIncidentes));
        formulario.agregar("Zona del incidente", new JComboBox<ZonaOperacion>(modeloZonas));
        formulario.agregar("Nivel de riesgo (1-5)", new JComboBox<Integer>(modeloRiesgo));
        formulario.agregar("Afectacion vial (1-5)", new JComboBox<Integer>(modeloAfectacion));
        formulario.agregar("Severidad (1-5)", new JComboBox<Integer>(modeloSeveridad));
        formulario.agregar("Descripcion", campoDescripcion);
        formulario.agregarAnchoCompleto(etiquetaPrioridad);
        JButton botonRegistrar = Componentes.boton("Registrar solicitud");
        botonRegistrar.addActionListener(e -> registrar());
        formulario.agregarAnchoCompleto(botonRegistrar);
        JPanel panelFormulario = new JPanel(new BorderLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder("Nueva solicitud de asistencia"));
        panelFormulario.add(formulario, BorderLayout.CENTER);
        add(panelFormulario, BorderLayout.NORTH);

        modeloPrioritarias = new ModeloLista();
        modeloOrdinarias = new ModeloLista();
        JList<String> listaPrioritaria = new JList<String>(modeloPrioritarias);
        listaOrdinaria = new JList<String>(modeloOrdinarias);
        listaPrioritaria.setFont(new Font("Consolas", Font.PLAIN, 12));
        listaOrdinaria.setFont(new Font("Consolas", Font.PLAIN, 12));

        JScrollPane desplazamientoPrioritarias = new JScrollPane(listaPrioritaria);
        desplazamientoPrioritarias.setBorder(BorderFactory.createTitledBorder(
                "Cola prioritaria, mayor puntaje primero"));
        JScrollPane desplazamientoOrdinarias = new JScrollPane(listaOrdinaria);
        desplazamientoOrdinarias.setBorder(BorderFactory.createTitledBorder(
                "Cola ordinaria, estricto orden de llegada"));

        JPanel panelListas = new JPanel(new GridLayout(1, 2, 10, 10));
        panelListas.add(desplazamientoPrioritarias);
        panelListas.add(desplazamientoOrdinarias);
        add(panelListas, BorderLayout.CENTER);

        etiquetaSiguiente = new JLabel("Sin solicitudes pendientes.");
        JButton botonEscalar = Componentes.boton("Escalar solicitud ordinaria seleccionada");
        botonEscalar.addActionListener(e -> escalar());
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panelInferior.add(botonEscalar);
        panelInferior.add(etiquetaSiguiente);
        add(panelInferior, BorderLayout.SOUTH);

        modeloClientes.addListDataListener(new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }
        });
        modeloIncidentes.addListDataListener(escuchaPrioridad());
        modeloZonas.addListDataListener(escuchaPrioridad());
        modeloRiesgo.addListDataListener(escuchaPrioridad());
        modeloAfectacion.addListDataListener(escuchaPrioridad());
        modeloSeveridad.addListDataListener(escuchaPrioridad());

        actualizar();
    }

    private javax.swing.event.ListDataListener escuchaPrioridad() {
        return new javax.swing.event.ListDataListener() {
            @Override
            public void intervalAdded(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }

            @Override
            public void intervalRemoved(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }

            @Override
            public void contentsChanged(javax.swing.event.ListDataEvent evento) {
                actualizarPrioridad();
            }
        };
    }

    private void actualizarPrioridad() {
        Cliente cliente = modeloClientes.getSeleccionado();
        Integer riesgo = modeloRiesgo.getSeleccionado();
        Integer afectacion = modeloAfectacion.getSeleccionado();
        Integer severidad = modeloSeveridad.getSeleccionado();
        if (cliente == null || riesgo == null || afectacion == null || severidad == null) {
            etiquetaPrioridad.setText("Seleccione un cliente para evaluar la prioridad.");
            return;
        }
        String descripcion = controlador.describirPrioridad(riesgo.intValue(),
                afectacion.intValue(), severidad.intValue(), cliente.getTipo());
        etiquetaPrioridad.setText(descripcion);
        etiquetaPrioridad.setForeground(descripcion.contains("PRIORITARIA")
                ? new java.awt.Color(183, 28, 28) : new java.awt.Color(27, 94, 32));
    }

    private void registrar() {
        Cliente cliente = modeloClientes.getSeleccionado();
        TipoIncidente incidente = modeloIncidentes.getSeleccionado();
        ZonaOperacion zona = modeloZonas.getSeleccionado();
        Integer riesgo = modeloRiesgo.getSeleccionado();
        Integer afectacion = modeloAfectacion.getSeleccionado();
        Integer severidad = modeloSeveridad.getSeleccionado();
        if (cliente == null || incidente == null || zona == null || riesgo == null
                || afectacion == null || severidad == null) {
            controlador.error("Complete todos los datos de la solicitud.");
            return;
        }
        controlador.registrarSolicitud(cliente.getIdentificacion(), incidente, zona,
                riesgo.intValue(), afectacion.intValue(), severidad.intValue(),
                campoDescripcion.getText());
        campoDescripcion.setText("");
        actualizarPrioridad();
    }

    private void escalar() {
        int indice = listaOrdinaria.getSelectedIndex();
        if (indice < 0 || indice >= ordinariasActuales.tamano()) {
            controlador.error("Seleccione una solicitud de la cola ordinaria para escalar.");
            return;
        }
        controlador.escalarSolicitud(ordinariasActuales.obtener(indice).getId());
    }

    /**
     * Actualiza los combos, las colas y el mensaje de siguiente atencion.
     */
    @Override
    public void actualizar() {
        modeloClientes.setElementos(controlador.listarClientes());
        Lista<Solicitud> prioritarias = controlador.listarColaPrioritaria();
        ordinariasActuales = controlador.listarColaOrdinaria();

        Lista<String> textosPrioritarias = new Lista<String>();
        for (int i = 0; i < prioritarias.tamano(); i++) {
            Solicitud solicitud = prioritarias.obtener(i);
            textosPrioritarias.agregar(solicitud.getId() + " | "
                    + solicitud.getCliente().getNombre() + " | "
                    + solicitud.getTipoIncidente().getEtiqueta() + " | "
                    + solicitud.getZona().getEtiqueta() + " | puntaje "
                    + solicitud.getPuntajePrioridad());
        }
        modeloPrioritarias.setElementos(textosPrioritarias);

        Lista<String> textosOrdinarias = new Lista<String>();
        for (int i = 0; i < ordinariasActuales.tamano(); i++) {
            Solicitud solicitud = ordinariasActuales.obtener(i);
            textosOrdinarias.agregar((i + 1) + ". " + solicitud.getId() + " | "
                    + solicitud.getCliente().getNombre() + " | "
                    + solicitud.getTipoIncidente().getEtiqueta());
        }
        modeloOrdinarias.setElementos(textosOrdinarias);

        Solicitud siguiente = controlador.solicitudSiguiente();
        if (siguiente == null) {
            etiquetaSiguiente.setText("Sin solicitudes pendientes en el flujo de atencion.");
        } else {
            etiquetaSiguiente.setText("Siguiente en atencion: " + siguiente.getId() + " | "
                    + siguiente.getCliente().getNombre() + " | puntaje "
                    + siguiente.getPuntajePrioridad());
        }
        actualizarPrioridad();
    }
}
