package asistenciamotor360.vista.componentes;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Panel de formulario que organiza etiquetas y campos en dos columnas.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class Formulario extends JPanel {

    private static final long serialVersionUID = 1L;

    private final GridBagLayout diseno;
    private final GridBagConstraints restricciones;
    private int fila;

    /**
     * Crea un formulario vacio.
     */
    public Formulario() {
        this.diseno = new GridBagLayout();
        this.restricciones = new GridBagConstraints();
        this.fila = 0;
        setLayout(diseno);
        restricciones.insets = new Insets(4, 6, 4, 6);
        restricciones.anchor = GridBagConstraints.WEST;
        restricciones.fill = GridBagConstraints.HORIZONTAL;
    }

    /**
     * Agrega una etiqueta y un componente en la siguiente fila del formulario.
     *
     * @param texto      texto de la etiqueta.
     * @param componente componente asociado al campo.
     */
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

    /**
     * Agrega un componente que ocupa las dos columnas del formulario.
     *
     * @param componente componente que se desea agregar.
     */
    public void agregarAnchoCompleto(JComponent componente) {
        restricciones.gridx = 0;
        restricciones.gridy = fila;
        restricciones.gridwidth = 2;
        restricciones.weightx = 1;
        add(componente, restricciones);
        restricciones.gridwidth = 1;
        fila++;
    }

    /**
     * Agrega un componente en una columna especifica y una fila concreta.
     *
     * @param componente componente que se desea agregar.
     * @param columna    columna del formulario.
     * @param fila       fila del formulario.
     */
    public void agregarEn(JComponent componente, int columna, int fila) {
        restricciones.gridx = columna;
        restricciones.gridy = fila;
        restricciones.weightx = 1;
        add(componente, restricciones);
    }

    /**
     * Retorna la siguiente fila libre del formulario.
     *
     * @return numero de fila libre.
     */
    public int siguienteFila() {
        return fila;
    }
}
