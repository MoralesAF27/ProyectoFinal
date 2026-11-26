package asistenciamotor360.vista.componentes;

import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.JTableHeader;

/**
 * Fabrica de componentes visuales con el estilo unificado de la aplicacion.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public final class Componentes {

    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FUENTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 12);

    private Componentes() {
    }

    /**
     * Crea una etiqueta con el estilo de titulo de seccion.
     *
     * @param texto texto de la etiqueta.
     * @return etiqueta configurada.
     */
    public static JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_TITULO);
        return etiqueta;
    }

    /**
     * Crea una etiqueta con el estilo de texto normal.
     *
     * @param texto texto de la etiqueta.
     * @return etiqueta configurada.
     */
    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_TEXTO);
        return etiqueta;
    }

    /**
     * Crea un boton con el estilo de la aplicacion.
     *
     * @param texto texto del boton.
     * @return boton configurado.
     */
    public static JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_TEXTO);
        boton.setFocusPainted(false);
        return boton;
    }

    /**
     * Envuelve un componente en un panel con desplazamiento y borde titulado.
     *
     * @param titulo    titulo del panel.
     * @param contenido componente que se desea desplazar.
     * @return panel con desplazamiento.
     */
    public static JScrollPane panelDesplazable(String titulo, Component contenido) {
        JScrollPane panel = new JScrollPane(contenido);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return panel;
    }

    /**
     * Crea una tabla configurada con el estilo de la aplicacion.
     *
     * @param modelo modelo de datos de la tabla.
     * @return tabla configurada.
     */
    public static JTable tabla(ModeloTabla modelo) {
        JTable tabla = new JTable(modelo);
        tabla.setRowHeight(24);
        tabla.setFont(FUENTE_TEXTO);
        tabla.setAutoCreateRowSorter(false);
        tabla.setFillsViewportHeight(true);
        JTableHeader encabezado = tabla.getTableHeader();
        encabezado.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return tabla;
    }

    /**
     * Ajusta el ancho preferido de un componente de formulario.
     *
     * @param componente componente que se desea ajustar.
     * @return el mismo componente ajustado.
     */
    public static JComponent ajustar(JComponent componente) {
        componente.setFont(FUENTE_TEXTO);
        return componente;
    }
}
