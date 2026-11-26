package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.RegistroAccion;
import asistenciamotor360.modelo.Transaccion;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.ModeloLista;

public class PanelSupervisor extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JLabel etiquetaPendientes;
    private final JLabel etiquetaResultado;
    private final ModeloLista modeloTransacciones;
    private final JTextArea areaHistorial;

    public PanelSupervisor(ControladorPrincipal controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        etiquetaPendientes = new JLabel("Transacciones pendientes: 0");
        etiquetaPendientes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaResultado = Componentes.etiqueta("Aun no se han deshecho transacciones.");
        JButton botonDeshacer = Componentes.boton("Deshacer ultima transaccion");
        botonDeshacer.addActionListener(e -> deshacer());

        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panelSuperior.add(etiquetaPendientes);
        panelSuperior.add(botonDeshacer);
        panelSuperior.add(etiquetaResultado);
        add(panelSuperior, BorderLayout.NORTH);

        modeloTransacciones = new ModeloLista();
        JList<String> listaTransacciones = new JList<String>(modeloTransacciones);
        listaTransacciones.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane desplazamientoTransacciones = new JScrollPane(listaTransacciones);
        desplazamientoTransacciones.setBorder(BorderFactory.createTitledBorder(
                "Pila de transacciones, la cima se deshace primero"));

        areaHistorial = new JTextArea();
        areaHistorial.setEditable(false);
        areaHistorial.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane desplazamientoHistorial = new JScrollPane(areaHistorial);
        desplazamientoHistorial.setBorder(BorderFactory.createTitledBorder(
                "Historial reciente de acciones"));

        JPanel centro = new JPanel(new GridLayout(1, 2, 10, 10));
        centro.add(desplazamientoTransacciones);
        centro.add(desplazamientoHistorial);
        add(centro, BorderLayout.CENTER);
        actualizar();
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
            String resultado = controlador.deshacerUltimaTransaccion();
            if (resultado != null) {
                etiquetaResultado.setText("Transaccion deshecha: " + resultado);
            }
        }
    }

    @Override
    public void actualizar() {
        Lista<Transaccion> transacciones = controlador.listarTransaccionesPendientes();
        Lista<String> textos = new Lista<String>();
        for (int i = 0; i < transacciones.tamano(); i++) {
            textos.agregar(transacciones.obtener(i).toString());
        }
        modeloTransacciones.setElementos(textos);
        etiquetaPendientes.setText("Transacciones pendientes: " + transacciones.tamano());

        Lista<RegistroAccion> historial = controlador.listarHistorial();
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < historial.tamano(); i++) {
            texto.append(historial.obtener(i).toString()).append("\n");
        }
        areaHistorial.setText(historial.estaVacia() ? "Sin acciones registradas."
                : texto.toString());
    }
}