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

public final class Componentes {

    private static final Font FUENTE_TITULO = new Font("Segoe UI", Font.BOLD, 15);
    private static final Font FUENTE_TEXTO = new Font("Segoe UI", Font.PLAIN, 12);

    private Componentes() {
    }

    public static JLabel titulo(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_TITULO);
        return etiqueta;
    }

    public static JLabel etiqueta(String texto) {
        JLabel etiqueta = new JLabel(texto);
        etiqueta.setFont(FUENTE_TEXTO);
        return etiqueta;
    }

    public static JButton boton(String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_TEXTO);
        boton.setFocusPainted(false);
        return boton;
    }

    public static JScrollPane panelDesplazable(String titulo, Component contenido) {
        JScrollPane panel = new JScrollPane(contenido);
        panel.setBorder(BorderFactory.createTitledBorder(titulo));
        panel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        return panel;
    }

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

    public static JComponent ajustar(JComponent componente) {
        componente.setFont(FUENTE_TEXTO);
        return componente;
    }
}