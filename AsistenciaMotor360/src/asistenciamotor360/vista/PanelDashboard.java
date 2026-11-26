package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.EstadoTecnico;
import asistenciamotor360.modelo.EstadoUnidad;
import asistenciamotor360.modelo.RegistroAccion;
import asistenciamotor360.modelo.Solicitud;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Componentes;

public class PanelDashboard extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private static final String[] TITULOS = {
        "Unidades disponibles",
        "Unidades asignadas",
        "Unidades en taller",
        "Tecnicos disponibles",
        "Tecnicos en atencion",
        "Solicitudes prioritarias",
        "Solicitudes ordinarias",
        "Rescates activos",
        "Maletines en auditoria",
        "Repuestos apilados"
    };

    private final ControladorPrincipal controlador;
    private final JLabel[] valores;
    private final JTextArea areaPrioritarias;
    private final JTextArea areaOrdinarias;
    private final JTextArea areaActivos;
    private final JTextArea areaHistorial;

    public PanelDashboard(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.valores = new JLabel[TITULOS.length];
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        JPanel tarjetas = new JPanel(new GridLayout(2, 5, 8, 8));
        for (int i = 0; i < TITULOS.length; i++) {
            tarjetas.add(tarjeta(TITULOS[i], i));
        }
        add(tarjetas, BorderLayout.NORTH);

        areaPrioritarias = area();
        areaOrdinarias = area();
        areaActivos = area();
        areaHistorial = area();

        JPanel centro = new JPanel(new GridLayout(2, 2, 8, 8));
        centro.add(Componentes.panelDesplazable("Cola prioritaria (mayor puntaje primero)",
                areaPrioritarias));
        centro.add(Componentes.panelDesplazable("Cola ordinaria (estricto orden de llegada)",
                areaOrdinarias));
        centro.add(Componentes.panelDesplazable("Rescates activos", areaActivos));
        centro.add(Componentes.panelDesplazable("Historial reciente de acciones", areaHistorial));
        add(centro, BorderLayout.CENTER);
        actualizar();
    }

    private JPanel tarjeta(String titulo, int indice) {
        JPanel panel = new JPanel(new BorderLayout(2, 2));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new java.awt.Color(205, 214, 224)),
                new javax.swing.border.EmptyBorder(8, 10, 8, 10)));
        JLabel valor = new JLabel("0", SwingConstants.CENTER);
        valor.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valor.setForeground(new java.awt.Color(21, 101, 192));
        JLabel nombre = new JLabel(titulo, SwingConstants.CENTER);
        nombre.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        panel.add(valor, BorderLayout.CENTER);
        panel.add(nombre, BorderLayout.SOUTH);
        valores[indice] = valor;
        return panel;
    }

    private JTextArea area() {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBorder(new javax.swing.border.EmptyBorder(4, 4, 4, 4));
        return area;
    }

    @Override
    public void actualizar() {
        valores[0].setText(String.valueOf(controlador.contarUnidades(EstadoUnidad.DISPONIBLE)));
        valores[1].setText(String.valueOf(controlador.contarUnidades(EstadoUnidad.ASIGNADA)));
        valores[2].setText(String.valueOf(controlador.listarUnidadesEnTaller().tamano()));
        valores[3].setText(String.valueOf(controlador.contarTecnicos(EstadoTecnico.DISPONIBLE)));
        valores[4].setText(String.valueOf(controlador.contarTecnicos(EstadoTecnico.EN_ATENCION)));
        Lista<Solicitud> prioritarias = controlador.listarColaPrioritaria();
        Lista<Solicitud> ordinarias = controlador.listarColaOrdinaria();
        Lista<Solicitud> activos = controlador.listarServiciosActivos();
        valores[5].setText(String.valueOf(prioritarias.tamano()));
        valores[6].setText(String.valueOf(ordinarias.tamano()));
        valores[7].setText(String.valueOf(activos.tamano()));
        valores[8].setText(String.valueOf(controlador.listarMaletinesAuditoria().tamano()));
        valores[9].setText(String.valueOf(controlador.listarRepuestos().tamano()));

        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < prioritarias.tamano(); i++) {
            Solicitud solicitud = prioritarias.obtener(i);
            texto.append(solicitud.getId()).append("  ").append(solicitud.getCliente().getNombre())
                    .append("\n   ").append(solicitud.getTipoIncidente().getEtiqueta())
                    .append(" | ").append(solicitud.getZona().getEtiqueta())
                    .append(" | puntaje ").append(solicitud.getPuntajePrioridad())
                    .append("\n");
        }
        areaPrioritarias.setText(prioritarias.estaVacia() ? "Sin solicitudes prioritarias."
                : texto.toString());

        texto = new StringBuilder();
        for (int i = 0; i < ordinarias.tamano(); i++) {
            Solicitud solicitud = ordinarias.obtener(i);
            texto.append(i + 1).append(". ").append(solicitud.getId()).append("  ")
                    .append(solicitud.getCliente().getNombre()).append("\n   ")
                    .append(solicitud.getTipoIncidente().getEtiqueta()).append(" | ")
                    .append(solicitud.getZona().getEtiqueta()).append("\n");
        }
        areaOrdinarias.setText(ordinarias.estaVacia() ? "Sin solicitudes ordinarias."
                : texto.toString());

        texto = new StringBuilder();
        for (int i = 0; i < activos.tamano(); i++) {
            Solicitud solicitud = activos.obtener(i);
            texto.append(solicitud.getId()).append("  ").append(solicitud.getCliente().getNombre())
                    .append("\n   Unidad: ")
                    .append(solicitud.getUnidad() == null ? "sin asignar"
                            : solicitud.getUnidad().getCodigo())
                    .append(" | Tecnico: ")
                    .append(solicitud.getTecnico() == null ? "sin asignar"
                            : solicitud.getTecnico().getNombre())
                    .append("\n   Estado: ").append(solicitud.getEstado().getEtiqueta()).append("\n");
        }
        areaActivos.setText(activos.estaVacia() ? "Sin rescates activos." : texto.toString());

        Lista<RegistroAccion> historial = controlador.listarHistorial();
        texto = new StringBuilder();
        for (int i = 0; i < historial.tamano(); i++) {
            texto.append(historial.obtener(i).toString()).append("\n");
        }
        areaHistorial.setText(historial.estaVacia() ? "Sin acciones registradas."
                : texto.toString());
        areaHistorial.setCaretPosition(0);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 720);
    }
}