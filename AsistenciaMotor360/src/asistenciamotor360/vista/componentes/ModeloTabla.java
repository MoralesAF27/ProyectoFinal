package asistenciamotor360.vista.componentes;

import javax.swing.table.AbstractTableModel;

import asistenciamotor360.modelo.estructuras.Lista;

/**
 * Modelo de tabla que almacena las filas en una lista propia y expone los datos
 * a un JTable de la interfaz grafica.
 *
 * @author Grupo CC1 2026-3
 * @version 1.0
 */
public class ModeloTabla extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final String[] columnas;
    private Lista<String[]> filas;

    /**
     * Crea un modelo de tabla con las columnas indicadas.
     *
     * @param columnas nombres de las columnas.
     */
    public ModeloTabla(String[] columnas) {
        this.columnas = columnas.clone();
        this.filas = new Lista<String[]>();
    }

    /**
     * Reemplaza las filas del modelo.
     *
     * @param nuevasFilas filas que se desean mostrar.
     */
    public void setFilas(Lista<String[]> nuevasFilas) {
        this.filas = nuevasFilas;
        fireTableDataChanged();
    }

    /**
     * Retorna la fila ubicada en la posicion indicada.
     *
     * @param indice posicion de la fila.
     * @return valores de la fila.
     */
    public String[] getFila(int indice) {
        return filas.obtener(indice);
    }

    /**
     * Retorna la cantidad de filas del modelo.
     *
     * @return cantidad de filas.
     */
    @Override
    public int getRowCount() {
        return filas.tamano();
    }

    /**
     * Retorna la cantidad de columnas del modelo.
     *
     * @return cantidad de columnas.
     */
    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    /**
     * Retorna el nombre de una columna.
     *
     * @param columna posicion de la columna.
     * @return nombre de la columna.
     */
    @Override
    public String getColumnName(int columna) {
        return columnas[columna];
    }

    /**
     * Retorna el valor de una celda.
     *
     * @param fila    posicion de la fila.
     * @param columna posicion de la columna.
     * @return valor de la celda.
     */
    @Override
    public Object getValueAt(int fila, int columna) {
        String[] datos = filas.obtener(fila);
        if (columna < datos.length) {
            return datos[columna];
        }
        return "";
    }

    /**
     * Indica que las celdas no son editables directamente.
     *
     * @param fila    posicion de la fila.
     * @param columna posicion de la columna.
     * @return falso porque la tabla es de solo lectura.
     */
    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}
