package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.time.LocalDate;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Solicitud;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.ModeloTabla;

public class PanelReportes extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JLabel etiquetaReporte;
    private final ModeloTabla modeloTabla;

    public PanelReportes(ControladorPrincipal controlador) {
        this.controlador = controlador;
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        etiquetaReporte = Componentes.etiqueta("Seleccione la ruta para exportar el reporte.");
        JButton botonExportar = Componentes.boton("Exportar reporte CSV de la jornada");
        botonExportar.addActionListener(e -> exportar());
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 6));
        panelSuperior.add(botonExportar);
        panelSuperior.add(etiquetaReporte);
        add(panelSuperior, BorderLayout.NORTH);

        modeloTabla = new ModeloTabla(new String[] {
            "Solicitud", "Cliente", "Incidente", "Zona", "Unidad", "Tecnico", "Puntaje",
            "Hora solucion"
        });
        JTable tabla = Componentes.tabla(modeloTabla);
        JScrollPane desplazamiento = new JScrollPane(tabla);
        desplazamiento.setBorder(BorderFactory.createTitledBorder(
                "Incidencias solucionadas en la jornada"));
        add(desplazamiento, BorderLayout.CENTER);
        actualizar();
    }

    private void exportar() {
        JFileChooser selector = new JFileChooser();
        selector.setDialogTitle("Guardar reporte CSV de incidencias solucionadas");
        selector.setSelectedFile(new File("reporte_asistencia_motor_360_"
                + LocalDate.now().toString() + ".csv"));
        if (selector.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        File archivo = selector.getSelectedFile();
        if (!archivo.getName().toLowerCase().endsWith(".csv")) {
            archivo = new File(archivo.getParentFile(), archivo.getName() + ".csv");
        }
        int total = controlador.exportarCSV(archivo.getAbsolutePath());
        if (total >= 0) {
            etiquetaReporte.setText("Reporte generado con " + total + " incidencias en "
                    + archivo.getAbsolutePath());
        }
    }

    @Override
    public void actualizar() {
        Lista<Solicitud> solucionadas = controlador.listarServiciosSolucionados();
        Lista<String[]> filas = new Lista<String[]>();
        for (int i = 0; i < solucionadas.tamano(); i++) {
            Solicitud solicitud = solucionadas.obtener(i);
            filas.agregar(new String[] {
                solicitud.getId(), solicitud.getCliente().getNombre(),
                solicitud.getTipoIncidente().getEtiqueta(), solicitud.getZona().getEtiqueta(),
                solicitud.getUnidad() == null ? "-" : solicitud.getUnidad().getCodigo(),
                solicitud.getTecnico() == null ? "-" : solicitud.getTecnico().getNombre(),
                String.valueOf(solicitud.getPuntajePrioridad()),
                solicitud.getFechaSolucion() == null ? "-"
                        : String.format("%02d:%02d", solicitud.getFechaSolucion().getHour(),
                                solicitud.getFechaSolucion().getMinute())
            });
        }
        modeloTabla.setFilas(filas);
    }
}