package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.estructuras.Lista;

public class VentanaPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final Lista<PanelModulo> paneles;
    private final JLabel etiquetaEstado;

    public VentanaPrincipal(ControladorPrincipal controlador) {
        super("Asistencia Motor 360 | Centro de control de operaciones");
        this.controlador = controlador;
        this.paneles = new Lista<PanelModulo>();
        this.etiquetaEstado = new JLabel("  Sistema listo. Registre recursos para iniciar.");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 720));
        setSize(1320, 850);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JTabbedPane pestanas = new JTabbedPane();
        agregarPestana(pestanas, "Panel de control", new PanelDashboard(controlador));
        agregarPestana(pestanas, "Unidades moviles", new PanelUnidades(controlador));
        agregarPestana(pestanas, "Tecnicos", new PanelTecnicos(controlador));
        agregarPestana(pestanas, "Clientes", new PanelClientes(controlador));
        agregarPestana(pestanas, "Solicitudes", new PanelSolicitudes(controlador));
        agregarPestana(pestanas, "Despacho", new PanelDespacho(controlador));
        agregarPestana(pestanas, "Insumos", new PanelInsumos(controlador));
        agregarPestana(pestanas, "Supervisor", new PanelSupervisor(controlador));
        agregarPestana(pestanas, "Reportes", new PanelReportes(controlador));
        add(pestanas, BorderLayout.CENTER);

        JPanel barraEstado = new JPanel(new BorderLayout());
        barraEstado.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 205, 212)));
        etiquetaEstado.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        barraEstado.add(etiquetaEstado, BorderLayout.WEST);
        add(barraEstado, BorderLayout.SOUTH);

        setJMenuBar(crearMenu());
        actualizarPaneles();
    }

    private void agregarPestana(JTabbedPane pestanas, String titulo, PanelModulo panel) {
        paneles.agregar(panel);
        pestanas.addTab(titulo, (Component) panel);
    }

    private JMenuBar crearMenu() {
        JMenuBar barra = new JMenuBar();

        JMenu menuSistema = new JMenu("Sistema");
        JMenuItem itemDemo = new JMenuItem("Cargar datos de demostracion");
        itemDemo.addActionListener(e -> cargarDemostracion());
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> dispose());
        menuSistema.add(itemDemo);
        menuSistema.addSeparator();
        menuSistema.add(itemSalir);
        barra.add(menuSistema);

        JMenu menuSupervisor = new JMenu("Supervisor");
        JMenuItem itemDeshacer = new JMenuItem("Deshacer ultima transaccion");
        itemDeshacer.addActionListener(e -> deshacer());
        menuSupervisor.add(itemDeshacer);
        barra.add(menuSupervisor);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de Asistencia Motor 360");
        itemAcerca.addActionListener(e -> JOptionPane.showMessageDialog(this,
                "Asistencia Motor 360\nCentro de control de servicios viales\n"
                        + "Universidad Distrital Francisco Jose de Caldas\n"
                        + "Proyecto final de Ciencias de la Computacion I\n\n"
                        + "Arquitectura MVC con estructuras de datos propias.",
                "Acerca de", JOptionPane.INFORMATION_MESSAGE));
        menuAyuda.add(itemAcerca);
        barra.add(menuAyuda);
        return barra;
    }

    private void cargarDemostracion() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Se cargaran unidades, tecnicos, clientes y solicitudes de ejemplo. "
                        + "Desea continuar?",
                "Datos de demostracion", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            controlador.cargarDatosDemostracion();
        }
    }

    private void deshacer() {
        if (controlador.contarTransaccionesPendientes() == 0) {
            controlador.error("No hay transacciones pendientes por deshacer.");
            return;
        }
        int respuesta = JOptionPane.showConfirmDialog(this,
                "Se deshara la ultima transaccion registrada. Desea continuar?",
                "Confirmar deshacer", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            controlador.deshacerUltimaTransaccion();
        }
    }

    public void actualizarPaneles() {
        for (int i = 0; i < paneles.tamano(); i++) {
            paneles.obtener(i).actualizar();
        }
    }

    public void mostrarMensaje(String texto) {
        etiquetaEstado.setText("  " + texto);
    }
}