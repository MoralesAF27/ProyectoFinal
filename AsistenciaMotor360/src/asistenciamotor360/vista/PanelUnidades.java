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
import asistenciamotor360.modelo.EstadoUnidad;
import asistenciamotor360.modelo.NivelDisponibilidad;
import asistenciamotor360.modelo.TipoUnidad;
import asistenciamotor360.modelo.UnidadMovil;
import asistenciamotor360.modelo.ZonaOperacion;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Catalogos;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloCombo;
import asistenciamotor360.vista.componentes.ModeloTabla;

/**
 * Panel de administracion de la flota de unidades moviles.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class PanelUnidades extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JTextField campoCodigo;
    private final ModeloCombo<TipoUnidad> modeloTipo;
    private final ModeloCombo<ZonaOperacion> modeloZona;
    private final ModeloCombo<NivelDisponibilidad> modeloDisponibilidad;
    private final ModeloCombo<UnidadMovil> modeloUnidades;
    private final ModeloCombo<EstadoUnidad> modeloEstado;
    private final ModeloTabla modeloTabla;

    /**
     * Crea el panel de unidades moviles.
     *
     * @param controlador controlador principal de la aplicacion.
     */
    public PanelUnidades(ControladorPrincipal controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        campoCodigo = new JTextField();
        modeloTipo = new ModeloCombo<TipoUnidad>();
        modeloTipo.setElementos(Catalogos.tiposUnidad());
        modeloZona = new ModeloCombo<ZonaOperacion>();
        modeloZona.setElementos(Catalogos.zonas());
        modeloDisponibilidad = new ModeloCombo<NivelDisponibilidad>();
        modeloDisponibilidad.setElementos(Catalogos.nivelesDisponibilidad());

        Formulario registro = new Formulario();
        registro.agregar("Codigo o placa", campoCodigo);
        registro.agregar("Categoria", new JComboBox<TipoUnidad>(modeloTipo));
        registro.agregar("Zona de operacion", new JComboBox<ZonaOperacion>(modeloZona));
        registro.agregar("Disponibilidad", new JComboBox<NivelDisponibilidad>(modeloDisponibilidad));
        JButton botonRegistrar = Componentes.boton("Registrar unidad");
        botonRegistrar.addActionListener(e -> registrar());
        registro.agregarAnchoCompleto(botonRegistrar);
        JPanel panelRegistro = new JPanel(new BorderLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Registrar unidad movil"));
        panelRegistro.add(registro, BorderLayout.CENTER);

        modeloUnidades = new ModeloCombo<UnidadMovil>();
        modeloEstado = new ModeloCombo<EstadoUnidad>();
        modeloEstado.setElementos(Catalogos.estadosUnidad());
        Formulario cambio = new Formulario();
        cambio.agregar("Unidad", new JComboBox<UnidadMovil>(modeloUnidades));
        cambio.agregar("Nuevo estado", new JComboBox<EstadoUnidad>(modeloEstado));
        JButton botonEstado = Componentes.boton("Cambiar estado");
        botonEstado.addActionListener(e -> cambiarEstado());
        cambio.agregarAnchoCompleto(botonEstado);
        JPanel panelCambio = new JPanel(new BorderLayout());
        panelCambio.setBorder(BorderFactory.createTitledBorder("Cambiar estado operativo"));
        panelCambio.add(cambio, BorderLayout.CENTER);

        JPanel formularios = new JPanel(new GridLayout(1, 2, 12, 12));
        formularios.add(panelRegistro);
        formularios.add(panelCambio);
        add(formularios, BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(new String[] {
            "UUID", "Codigo", "Categoria", "Zona", "Disponibilidad", "Estado"
        });
        JTable tabla = Componentes.tabla(modeloTabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder(
                "Inventario de unidades moviles"));
        add(desplazamiento, BorderLayout.CENTER);
        actualizar();
    }

    private void registrar() {
        TipoUnidad tipo = modeloTipo.getSeleccionado();
        ZonaOperacion zona = modeloZona.getSeleccionado();
        NivelDisponibilidad disponibilidad = modeloDisponibilidad.getSeleccionado();
        controlador.registrarUnidad(campoCodigo.getText(), tipo, zona, disponibilidad);
        campoCodigo.setText("");
    }

    private void cambiarEstado() {
        UnidadMovil unidad = modeloUnidades.getSeleccionado();
        if (unidad == null) {
            controlador.error("Seleccione una unidad registrada.");
            return;
        }
        controlador.cambiarEstadoUnidad(unidad.getUuid(), modeloEstado.getSeleccionado());
    }

    /**
     * Actualiza el listado y los combos del panel.
     */
    @Override
    public void actualizar() {
        Lista<UnidadMovil> unidades = controlador.listarUnidades();
        modeloUnidades.setElementos(unidades);
        Lista<String[]> filas = new Lista<String[]>();
        for (int i = 0; i < unidades.tamano(); i++) {
            UnidadMovil unidad = unidades.obtener(i);
            filas.agregar(new String[] {
                unidad.getUuid(), unidad.getCodigo(), unidad.getTipo().getEtiqueta(),
                unidad.getZona().getEtiqueta(), unidad.getDisponibilidad().getEtiqueta(),
                unidad.getEstado().getEtiqueta()
            });
        }
        modeloTabla.setFilas(filas);
    }
}
