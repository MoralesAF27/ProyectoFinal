package asistenciamotor360.vista.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class Formulario extends JPanel {

    private static final long serialVersionUID = 1L;

    private final GridBagLayout diseno;
    private final GridBagConstraints restricciones;
    private int fila;

    public Formulario() {
        this.diseno = new GridBagLayout();
        this.restricciones = new GridBagConstraints();
        this.fila = 0;
        setLayout(diseno);
        restricciones.insets = new Insets(4, 6, 4, 6);
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
    }

    public void agregar(String texto, JComponent componente) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.weightx = 0;
        add(Componentes.etiqueta(texto), restricciones);
        restricciones.gridx = 1;
        restricciones.weightx = 1;
        add(componente, restricciones);
        fila++;
    }

    public void agregarAnchoCompleto(JComponent componente) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.gridwidth = 2;
        restricciones.weightx = 1;
        add(componente, restricciones);
        restricciones.gridwidth = 1;
        fila++;
    }

    public void agregarEn(JComponent componente, int columna, int fila) {
        restricciones.gridx = columna;
        restricciones.gridy = fila;
        restricciones.weightx = 1;
        add(componente, restricciones);
    }

    public int siguienteFila() {
        return fila;
    }
}