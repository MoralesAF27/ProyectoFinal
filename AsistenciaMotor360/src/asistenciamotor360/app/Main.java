package asistenciamotor360.app;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.SistemaAsistencia;
import asistenciamotor360.vista.VentanaPrincipal;

/**
 * Clase de arranque de la aplicacion Asistencia Motor 360.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public final class Main {

    private Main() {
    }

    /**
     * Inicia la aplicacion en el hilo de eventos de Swing.
     *
     * @param argumentos argumentos de linea de comandos, no utilizados.
     */
    public static void main(String[] argumentos) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception excepcion) {
            UIManager.getLookAndFeelDefaults();
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SistemaAsistencia sistema = new SistemaAsistencia();
                ControladorPrincipal controlador = new ControladorPrincipal(sistema);
                VentanaPrincipal ventana = new VentanaPrincipal(controlador);
                controlador.conectarVista(ventana);
                ventana.setVisible(true);
            }
        });
    }
}
