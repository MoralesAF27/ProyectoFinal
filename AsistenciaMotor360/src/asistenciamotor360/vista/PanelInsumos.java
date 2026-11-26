package asistenciamotor360.vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JLabel;

import asistenciamotor360.controlador.ControladorPrincipal;
import asistenciamotor360.modelo.Maletin;
import asistenciamotor360.modelo.Repuesto;
import asistenciamotor360.modelo.estructuras.Lista;
import asistenciamotor360.vista.componentes.Componentes;
import asistenciamotor360.vista.componentes.Formulario;
import asistenciamotor360.vista.componentes.ModeloLista;

public class PanelInsumos extends JPanel implements PanelModulo {

    private static final long serialVersionUID = 1L;

    private final ControladorPrincipal controlador;
    private final JTextField campoCodigoMaletin;
    private final JTextField campoContenidoMaletin;
    private final ModeloLista modeloDisponibles;
    private final ModeloLista modeloAuditoria;
    private final ModeloLista modeloEnServicio;
    private final JList<String> listaEnServicio;
    private final JLabel etiquetaDescartados;
    private final JTextField campoCodigoRepuesto;
    private final JTextField campoNombreRepuesto;
    private final ModeloLista modeloRepuestos;
    private final JTextArea areaDespachados;
    private Lista<Maletin> enServicioActual;

    public PanelInsumos(ControladorPrincipal controlador) {
        this.controlador = controlador;
        this.enServicioActual = new Lista<Maletin>();
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));

        modeloDisponibles = new ModeloLista();
        modeloAuditoria = new ModeloLista();
        modeloEnServicio = new ModeloLista();
        listaEnServicio = new JList<String>(modeloEnServicio);
        listaEnServicio.setFont(new Font("Consolas", Font.PLAIN, 12));
        campoCodigoMaletin = new JTextField();
        campoContenidoMaletin = new JTextField();
        etiquetaDescartados = Componentes.etiqueta("Maletines rechazados: 0");
        campoCodigoRepuesto = new JTextField();
        campoNombreRepuesto = new JTextField();
        modeloRepuestos = new ModeloLista();
        areaDespachados = new JTextArea();
        areaDespachados.setEditable(false);
        areaDespachados.setFont(new Font("Consolas", Font.PLAIN, 12));

        JTabbedPane pestanas = new JTabbedPane();
        pestanas.addTab("Maletines de respuesta rapida", construirMaletines());
        pestanas.addTab("Repuestos de alta rotacion", construirRepuestos());
        add(pestanas, BorderLayout.CENTER);

        actualizar();
    }

    private JPanel construirMaletines() {
        Formulario formulario = new Formulario();
        formulario.agregar("Codigo del maletin", campoCodigoMaletin);
        formulario.agregar("Contenido", campoContenidoMaletin);
        JButton botonRegistrar = Componentes.boton("Apilar maletin");
        botonRegistrar.addActionListener(e -> {
            controlador.registrarMaletin(campoCodigoMaletin.getText(),
                    campoContenidoMaletin.getText());
            campoCodigoMaletin.setText("");
            campoContenidoMaletin.setText("");
        });
        formulario.agregarAnchoCompleto(botonRegistrar);

        JPanel superior = new JPanel(new BorderLayout());
        superior.setBorder(BorderFactory.createTitledBorder(
                "Registrar maletin en la estanteria"));
        superior.add(formulario, BorderLayout.CENTER);

        JList<String> listaDisponibles = new JList<String>(modeloDisponibles);
        JList<String> listaAuditoria = new JList<String>(modeloAuditoria);
        listaDisponibles.setFont(new Font("Consolas", Font.PLAIN, 12));
        listaAuditoria.setFont(new Font("Consolas", Font.PLAIN, 12));

        JPanel listas = new JPanel(new GridLayout(1, 3, 8, 8));
        listas.add(Componentes.panelDesplazable("Estanteria (cima primero)", listaDisponibles));
        listas.add(Componentes.panelDesplazable("Auditoria (cima primero)", listaAuditoria));
        listas.add(Componentes.panelDesplazable("Maletines en servicio", listaEnServicio));

        JButton botonDespachar = Componentes.boton("Despachar ultimo maletin (LIFO)");
        botonDespachar.addActionListener(e -> controlador.despacharMaletin());
        JButton botonAuditoria = Componentes.boton("Enviar seleccionado a auditoria");
        botonAuditoria.addActionListener(e -> enviarAuditoria());
        JButton botonAuditar = Componentes.boton("Auditar cima y devolver");
        botonAuditar.addActionListener(e -> controlador.auditarMaletin(true));
        JButton botonRechazar = Componentes.boton("Rechazar cima");
        botonRechazar.addActionListener(e -> controlador.auditarMaletin(false));

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        botones.add(botonDespachar);
        botones.add(botonAuditoria);
        botones.add(botonAuditar);
        botones.add(botonRechazar);
        botones.add(etiquetaDescartados);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(superior, BorderLayout.NORTH);
        panel.add(listas, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirRepuestos() {
        Formulario formulario = new Formulario();
        formulario.agregar("Codigo de la pieza", campoCodigoRepuesto);
        formulario.agregar("Nombre de la pieza", campoNombreRepuesto);
        JButton botonApilar = Componentes.boton("Apilar repuesto");
        formulario.agregarAnchoCompleto(botonApilar);
        JPanel superior = new JPanel(new BorderLayout());
        superior.setBorder(BorderFactory.createTitledBorder(
                "Preparar repuesto de alta rotacion"));
        superior.add(formulario, BorderLayout.CENTER);

        JList<String> listaRepuestos = new JList<String>(modeloRepuestos);
        listaRepuestos.setFont(new Font("Consolas", Font.PLAIN, 12));
        JPanel listas = new JPanel(new GridLayout(1, 2, 8, 8));
        listas.add(Componentes.panelDesplazable("Pila de repuestos (cima primero)",
                listaRepuestos));
        listas.add(Componentes.panelDesplazable("Repuestos despachados", areaDespachados));

        JButton botonDespachar = Componentes.boton("Despachar ultima pieza (LIFO)");
        botonDespachar.addActionListener(e -> controlador.despacharRepuesto());
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        botones.add(botonDespachar);

        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.add(superior, BorderLayout.NORTH);
        panel.add(listas, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        botonApilar.addActionListener(e -> {
            controlador.apilarRepuesto(campoCodigoRepuesto.getText(),
                    campoNombreRepuesto.getText());
            campoCodigoRepuesto.setText("");
            campoNombreRepuesto.setText("");
        });
        return panel;
    }

    private void enviarAuditoria() {
        int indice = listaEnServicio.getSelectedIndex();
        if (indice < 0 || indice >= enServicioActual.tamano()) {
            controlador.error("Seleccione un maletin en servicio.");
            return;
        }
        controlador.enviarMaletinAuditoria(enServicioActual.obtener(indice).getCodigo());
    }

    @Override
    public void actualizar() {
        Lista<Maletin> disponibles = controlador.listarMaletinesDisponibles();
        Lista<Maletin> auditoria = controlador.listarMaletinesAuditoria();
        enServicioActual = controlador.listarMaletinesEnServicio();

        modeloDisponibles.setElementos(textosMaletines(disponibles));
        modeloAuditoria.setElementos(textosMaletines(auditoria));

        Lista<String> textosServicio = new Lista<String>();
        for (int i = 0; i < enServicioActual.tamano(); i++) {
            textosServicio.agregar(enServicioActual.obtener(i).toString());
        }
        modeloEnServicio.setElementos(textosServicio);
        etiquetaDescartados.setText("Maletines rechazados: "
                + controlador.listarMaletinesDescartados().tamano());

        Lista<Repuesto> repuestos = controlador.listarRepuestos();
        Lista<String> textosRepuestos = new Lista<String>();
        for (int i = 0; i < repuestos.tamano(); i++) {
            String marca = i == 0 ? "CIMA -> " : "        ";
            textosRepuestos.agregar(marca + repuestos.obtener(i).toString());
        }
        modeloRepuestos.setElementos(textosRepuestos);

        Lista<Repuesto> despachados = controlador.listarRepuestosDespachados();
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < despachados.tamano(); i++) {
            texto.append(despachados.obtener(i).toString()).append("\n");
        }
        areaDespachados.setText(despachados.estaVacia()
                ? "Sin repuestos despachados." : texto.toString());
    }

    private Lista<String> textosMaletines(Lista<Maletin> maletines) {
        Lista<String> textos = new Lista<String>();
        for (int i = 0; i < maletines.tamano(); i++) {
            String marca = i == 0 ? "CIMA -> " : "        ";
            textos.agregar(marca + maletines.obtener(i).toString());
        }
        return textos;
    }
}