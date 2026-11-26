package asistenciamotor360.vista;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Cliente;
import asistenciamotor360.modelo.TipoCliente;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Catalogos;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloCombo;
import asistenciamotor360.vista.componentes.ModeloTabla;

public class PanelClientes extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JTextField campoIdentificacion;
    private final JTextField campoNombre;
    private final ModeloCombo<TipoCliente> modeloTipo;
    private final JTextField campoTelefono;
    private final JTextField campoDireccion;
    private final ModeloTabla modeloTabla;

    public PanelClientes(ControladorPrincipal controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        campoIdentificacion = new JTextField();
        campoNombre = new JTextField();
        modeloTipo = new ModeloCombo<TipoCliente>();
        modeloTipo.setElementos(Catalogos.tiposCliente());
        campoTelefono = new JTextField();
        campoDireccion = new JTextField();

        Formulario formulario = new Formulario();
        formulario.agregar("Identificacion / NIT", campoIdentificacion);
        formulario.agregar("Nombre o razon social", campoNombre);
        formulario.agregar("Perfil del cliente", new JComboBox<TipoCliente>(modeloTipo));
        formulario.agregar("Telefono", campoTelefono);
        formulario.agregar("Direccion", campoDireccion);
        JButton botonRegistrar = Componentes.boton("Registrar cliente");
        botonRegistrar.addActionListener(e -> registrar());
        formulario.agregarAnchoCompleto(botonRegistrar);
        JPanel panelRegistro = new JPanel(new BorderLayout());
        panelRegistro.setBorder(BorderFactory.createTitledBorder("Nuevo cliente"));
        panelRegistro.add(formulario, BorderLayout.CENTER);
        add(panelRegistro, BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(new String[] {
            "Identificacion", "Nombre", "Perfil", "Telefono", "Direccion"
        });
        JTable tabla = Componentes.tabla(modeloTabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder("Clientes registrados"));
        add(desplazamiento, BorderLayout.CENTER);
        actualizar();
    }

    private void registrar() {
        controlador.registrarCliente(campoIdentificacion.getText(), campoNombre.getText(),
                modeloTipo.getSeleccionado(), campoTelefono.getText(), campoDireccion.getText());
        campoIdentificacion.setText("");
        campoNombre.setText("");
        campoTelefono.setText("");
        campoDireccion.setText("");
    }

    @Override
    public void actualizar() {
        Lista<Cliente> clientes = controlador.listarClientes();
        Lista<String[]> filas = new Lista<String[]>();
        for (int i = 0; i < clientes.tamano(); i++) {
            Cliente cliente = clientes.obtener(i);
            filas.agregar(new String[] {
                cliente.getIdentificacion(), cliente.getNombre(),
                cliente.getTipo().getEtiqueta(), cliente.getTelefono(), cliente.getDireccion()
            });
        }
        modeloTabla.setFilas(filas);
    }
}