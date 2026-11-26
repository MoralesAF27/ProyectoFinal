package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Especialidad;
import asistenciamotor360.modelo.EstadoTecnico;
import asistenciamotor360.modelo.Tecnico;
import asistenciamotor360.modelo.ZonaOperacion;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Catalogos;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloCombo;
import asistenciamotor360.vista.componentes.ModeloTabla;

/**
 * Panel de administracion del personal tecnico de la compania.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class PanelTecnicos extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JTextField campoIdentificacion;
    private final JTextField campoNombre;
    private final ModeloCombo<Especialidad> modeloEspecialidad;
    private final ModeloCombo<ZonaOperacion> modeloSector;
    private final ModeloCombo<Tecnico> modeloTecnicos;
    private final ModeloCombo<EstadoTecnico> modeloEstado;
    private final ModeloTabla modeloTabla;

    /**
     * Crea el panel de tecnicos.
     *
     * @param controlador controlador principal de la aplicacion.
     */
    public PanelTecnicos(ControladorPrincipal controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        campoIdentificacion = new JTextField();
        campoNombre = new JTextField();
        modeloEspecialidad = new ModeloCombo<Especialidad>();
        modeloEspecialidad.setElementos(Catalogos.especialidades());
        modeloSector = new ModeloCombo<ZonaOperacion>();
        modeloSector.setElementos(Catalogos.zonas());

        Formulario registro = new Formulario();
        registro.agregar("Identificacion", campoIdentificacion);
        registro.agregar("Nombre completo", campoNombre);
        registro.agregar("Especialidad", new JComboBox<Especialidad>(modeloEspecialidad));
        registro.agregar("Sector de patrullaje", new JComboBox<ZonaOperacion>(modeloSector));
        JButton botonRegistrar = Componentes.boton("Registrar tecnico");
        botonRegistrar.addActionListener(e -> registrar());
        registro.agregarAnchoCompleto(botonRegistrar);
        JPanel panelRegistro = new JPanel(new BorderLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registrar personal tecnico"));
        panelRegistro.add(registro, BorderLayout.CENTER);

        modeloTecnicos = new ModeloCombo<Tecnico>();
        modeloEstado = new ModeloCombo<EstadoTecnico>();
        modeloEstado.setElementos(Catalogos.estadosTecnicos());
        Formulario cambio = new Formulario();
        cambio.agregar("Tecnico", new JComboBox<Tecnico>(modeloTecnicos));
        cambio.agregar("Nuevo estado", new JComboBox<EstadoTecnico>(modeloEstado));
        JButton botonEstado = Componentes.boton("Cambiar estado");
        botonEstado.addActionListener(e -> cambiarEstado());
        cambio.agregarAnchoCompleto(botonEstado);
        JPanel panelCambio = new JPanel(new BorderLayout());
        panelCambio.setBorder(BorderFactory.createTitledBorder("Estado vigente del tecnico"));
        panelCambio.add(cambio, BorderLayout.CENTER);

        JPanel formularios = new JPanel(new GridLayout(1, 2, 12, 12));
        formularios.add(panelRegistro);
        formularios.add(panelCambio);
        add(formularios, BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(new String[] {
            "Identificacion", "Nombre", "Especialidad", "Sector", "Estado", "Servicio activo"
        });
        JTable tabla = Componentes.tabla(modeloTabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Personal tecnico registrado"));
        add(desplazamiento, BorderLayout.CENTER);
        actualizar();
    }

    private void registrar() {
        controlador.registrarTecnico(campoIdentificacion.getText(), campoNombre.getText(),
                modeloEspecialidad.getSeleccionado(), modeloSector.getSeleccionado());
        campoIdentificacion.setText("");
        campoNombre.setText("");
    }

    private void cambiarEstado() {
        Tecnico tecnico = modeloTecnicos.getSeleccionado();
        if (tecnico == null) {
            controlador.error("Seleccione un tecnico registrado.");
            return;
        }
        controlador.cambiarEstadoTecnico(tecnico.getIdentificacion(),
                modeloEstado.getSeleccionado());
    }

    /**
     * Actualiza el listado y los combos del panel.
     */
    @Override
    public void actualizar() {
        Lista<Tecnico> tecnicos = controlador.listarTecnicos();
        modeloTecnicos.setElementos(tecnicos);
        Lista<String[]> filas = new Lista<String[]>();
        for (int i = 0; i < tecnicos.tamano(); i++) {
            Tecnico tecnico = tecnicos.obtener(i);
            filas.agregar(new String[] {
                tecnico.getIdentificacion(), tecnico.getNombre(),
                tecnico.getEspecialidad().getEtiqueta(), tecnico.getSector().getEtiqueta(),
                tecnico.getEstado().getEtiqueta(),
                tecnico.getIdServicioActivo() == null ? "-" : tecnico.getIdServicioActivo()
            });
        }
        modeloTabla.setFilas(filas);
    }
}
