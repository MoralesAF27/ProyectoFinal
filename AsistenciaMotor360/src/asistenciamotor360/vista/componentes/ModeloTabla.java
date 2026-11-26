package asistenciamotor360.vista.componentes;

import javax.swing.table.AbstractTableModel;

import asistenciamotor360.modelo.estructuras.Lista;

public class ModeloTabla extends AbstractTableModel {

    private static final long serialVersionUID = 1L;

    private final String[] columnas;
    private Lista<String[]> filas;

    public ModeloTabla(String[] columnas) {
        this.columnas = columnas.clone();
        this.filas = new Lista<String[]>();
    }

    public void setFilas(Lista<String[]> nuevasFilas) {
        this.filas = nuevasFilas;
        fireTableDataChanged();
    }

    public String[] getFila(int indice) {
        return filas.obtener(indice);
    }

    @Override
    public int getRowCount() {
        return filas.tamano();
    }

    @Override
    public int getColumnCount() {
        return columnas.length;
    }

    @Override
    public String getColumnName(int columna) {
        return columnas[columna];
    }

    @Override
    public Object getValueAt(int fila, int columna) {
        String[] datos = filas.obtener(fila);
        if (columna < datos.length) {
            return datos[columna];
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int fila, int columna) {
        return false;
    }
}