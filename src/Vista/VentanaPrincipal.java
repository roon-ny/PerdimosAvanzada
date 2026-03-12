package Vista;

import Control.Logica.GestorMinipig;
import Modelo.MinipigVO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

/**
 * Ventana principal del aplicativo MiniPig.
 *
 * <p>Proposito: Presentar la interfaz grafica organizada por pestanas,
 * registrar los listeners en los botones, y proveer metodos para que
 * {@code EventosVentana} lea y escriba datos en los campos.</p>
 *
 * <p>La Vista NO contiene logica de negocio, validaciones de datos ni
 * objetos del modelo dentro de su construccion. Solo maneja la presentacion
 * y delega toda accion al gestor a traves de {@code EventosVentana}.</p>
 *
 * <p>Principio SOLID aplicado: SRP - solo gestiona la presentacion.
 * Eventos separados en {@code EventosVentana} para cumplir el principio de
 * separacion de responsabilidades exigido por la materia.</p>
 *
 * <p>Se comunica con: {@code EventosVentana} (eventos), {@code GestorMinipig}
 * (referencia pasada al manejador de eventos).</p>
 *
 * @author Programacion Avanzada - UD
 * @version 1.0
 */
public class VentanaPrincipal extends JFrame {

    // ===== COLORES DEL TEMA =====
    /** Color de fondo principal (rosa palido). */
    private static final Color COLOR_FONDO      = new Color(255, 240, 245);
    /** Color de encabezados y bordes. */
    private static final Color COLOR_PRIMARIO   = new Color(210, 80, 120);
    /** Color de botones primarios. */
    private static final Color COLOR_BOTON      = new Color(230, 100, 140);
    /** Color de botones secundarios. */
    private static final Color COLOR_BOTON2     = new Color(180, 70, 100);
    /** Color de texto sobre botones. */
    private static final Color COLOR_TEXTO_BTN  = Color.WHITE;
    /** Color de paneles internos. */
    private static final Color COLOR_PANEL      = new Color(255, 248, 252);
    /** Fuente principal del sistema. */
    private static final Font  FUENTE_CAMPO     = new Font("Segoe UI", Font.PLAIN, 13);
    /** Fuente para labels. */
    private static final Font  FUENTE_LABEL     = new Font("Segoe UI", Font.BOLD, 13);
    /** Fuente para titulos. */
    private static final Font  FUENTE_TITULO    = new Font("Segoe UI", Font.BOLD, 22);

    // ===== PESTAÑA INSERTAR =====
    private JTextField txtInsertCodigo, txtInsertNombre, txtInsertGenero,
                       txtInsertMicrochip, txtInsertRaza, txtInsertColor,
                       txtInsertPeso, txtInsertAltura, txtInsertCaract1,
                       txtInsertCaract2, txtInsertFoto;
    private JButton btnInsertar;

    // ===== PESTAÑA CONSULTAR =====
    private JTextField txtBusqueda;
    private JButton btnBuscarCod, btnBuscarMic, btnBuscarRaza, btnBuscarNom;
    private JTextArea areaBusqueda;
    private JLabel lblFotoConsulta;
    private JComboBox<String> comboMinipigs;

    // ===== PESTAÑA ELIMINAR =====
    private JTextField txtEliminar;
    private JButton btnElimCod, btnElimMic;

    // ===== PESTAÑA MODIFICAR =====
    private JTextField txtModCodigo, txtModMicrochip,
                       txtModNombre, txtModGenero, txtModRaza,
                       txtModColor, txtModPeso, txtModAltura,
                       txtModCaract1, txtModCaract2, txtModFoto;
    private JButton btnCargarMod, btnGuardarMod;

    // ===== BARRA DE BOTONES GLOBALES =====
    private JButton btnLimpiar, btnSalir;

    /** Pestanas principales. */
    private JTabbedPane pestanas;

    /** Manejador de eventos (separado de la vista). */
    private EventosVentana eventos;

    /**
     * Constructor que recibe el gestor y construye toda la UI.
     *
     * @param gestor referencia al gestor de logica de negocio
     */
    public VentanaPrincipal(GestorMinipig gestor) {
        eventos = new EventosVentana(this, gestor);
        inicializarVentana();
        construirUI();
        registrarListeners();
        refrescarCombo(gestor.obtenerTodos());
    }

    // =====================================================================
    // CONSTRUCCION DE LA UI
    // =====================================================================

    /**
     * Configura propiedades basicas del JFrame.
     */
    private void inicializarVentana() {
        setTitle("🐷 MiniPig Manager - UD Programacion Avanzada");
        setSize(900, 680);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        getContentPane().setBackground(COLOR_FONDO);
        setLayout(new BorderLayout());
    }

    /**
     * Construye todos los componentes y los agrega al frame.
     */
    private void construirUI() {
        add(construirEncabezado(), BorderLayout.NORTH);

        pestanas = new JTabbedPane();
        pestanas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestanas.setBackground(COLOR_FONDO);
        pestanas.addTab("➕ Insertar",  construirPanelInsertar());
        pestanas.addTab("🔍 Consultar", construirPanelConsultar());
        pestanas.addTab("🗑️ Eliminar",  construirPanelEliminar());
        pestanas.addTab("✏️ Modificar",  construirPanelModificar());

        add(pestanas, BorderLayout.CENTER);
        add(construirBarraInferior(), BorderLayout.SOUTH);
    }

    /**
     * Construye el encabezado decorativo con titulo.
     * @return panel de encabezado
     */
    private JPanel construirEncabezado() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COLOR_PRIMARIO);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("🐷  MiniPig Manager", SwingConstants.CENTER);
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Sistema de Gestion de Minipigs - Programacion Avanzada UD",
                SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitulo.setForeground(new Color(255, 220, 230));

        panel.add(lblTitulo,    BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);
        return panel;
    }

    /**
     * Construye el panel de insercion de nuevos minipigs.
     * @return JScrollPane con el formulario
     */
    private JScrollPane construirPanelInsertar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtInsertCodigo    = crearCampo(); txtInsertNombre    = crearCampo();
        txtInsertGenero    = crearCampo(); txtInsertMicrochip = crearCampo();
        txtInsertRaza      = crearCampo(); txtInsertColor     = crearCampo();
        txtInsertPeso      = crearCampo(); txtInsertAltura    = crearCampo();
        txtInsertCaract1   = crearCampo(); txtInsertCaract2   = crearCampo();
        txtInsertFoto      = crearCampo();

        String[][] filas = {
            {"Codigo *:", "Nombre *:"},
            {"Genero *:", "Id Microchip *:"},
            {"Raza *:", "Color:"},
            {"Peso:", "Altura:"},
            {"Caracteristica 1:", "Caracteristica 2:"}
        };
        JTextField[][] campos = {
            {txtInsertCodigo,  txtInsertNombre},
            {txtInsertGenero,  txtInsertMicrochip},
            {txtInsertRaza,    txtInsertColor},
            {txtInsertPeso,    txtInsertAltura},
            {txtInsertCaract1, txtInsertCaract2}
        };

        for (int f = 0; f < filas.length; f++) {
            for (int c = 0; c < 2; c++) {
                gbc.gridx = c * 2; gbc.gridy = f; gbc.weightx = 0;
                panel.add(crearLabel(filas[f][c]), gbc);
                gbc.gridx = c * 2 + 1; gbc.weightx = 0.5;
                panel.add(campos[f][c], gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0;
        panel.add(crearLabel("URL / Ruta Foto:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.weightx = 1;
        panel.add(txtInsertFoto, gbc);
        gbc.gridwidth = 1;

        btnInsertar = crearBoton("➕  Registrar Minipig", COLOR_BOTON);
        btnInsertar.setActionCommand(EventosVentana.CMD_INSERTAR);
        gbc.gridx = 1; gbc.gridy = 6; gbc.gridwidth = 2; gbc.weightx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnInsertar, gbc);

        return new JScrollPane(panel);
    }

    /**
     * Construye el panel de consultas.
     * @return panel de consultas
     */
    private JPanel construirPanelConsultar() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));

        // Fila de busqueda
        JPanel panelBusq = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelBusq.setBackground(COLOR_PANEL);
        panelBusq.setBorder(titulado("Buscar"));

        txtBusqueda = crearCampo();
        txtBusqueda.setPreferredSize(new Dimension(200, 30));
        panelBusq.add(crearLabel("Valor:"));
        panelBusq.add(txtBusqueda);

        btnBuscarCod  = crearBoton("Por Codigo",   COLOR_BOTON);  btnBuscarCod.setActionCommand(EventosVentana.CMD_BUSCAR_COD);
        btnBuscarMic  = crearBoton("Por Microchip",COLOR_BOTON);  btnBuscarMic.setActionCommand(EventosVentana.CMD_BUSCAR_MIC);
        btnBuscarRaza = crearBoton("Por Raza",     COLOR_BOTON2); btnBuscarRaza.setActionCommand(EventosVentana.CMD_BUSCAR_RAZA);
        btnBuscarNom  = crearBoton("Por Nombre",   COLOR_BOTON2); btnBuscarNom.setActionCommand(EventosVentana.CMD_BUSCAR_NOM);

        panelBusq.add(btnBuscarCod); panelBusq.add(btnBuscarMic);
        panelBusq.add(btnBuscarRaza); panelBusq.add(btnBuscarNom);

        // Combo de todos los minipigs
        JPanel panelCombo = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        panelCombo.setBackground(COLOR_PANEL);
        panelCombo.setBorder(titulado("Seleccionar Minipig"));
        comboMinipigs = new JComboBox<>();
        comboMinipigs.setFont(FUENTE_CAMPO);
        comboMinipigs.setPreferredSize(new Dimension(340, 30));
        comboMinipigs.setActionCommand(EventosVentana.CMD_COMBO_SEL);
        panelCombo.add(crearLabel("Minipig:"));
        panelCombo.add(comboMinipigs);

        JPanel panelTop = new JPanel(new GridLayout(2, 1, 5, 5));
        panelTop.setBackground(COLOR_PANEL);
        panelTop.add(panelBusq);
        panelTop.add(panelCombo);

        // Resultados
        JPanel panelResultados = new JPanel(new BorderLayout(10, 5));
        panelResultados.setBackground(COLOR_PANEL);
        panelResultados.setBorder(titulado("Resultado"));

        areaBusqueda = new JTextArea(12, 40);
        areaBusqueda.setFont(new Font("Monospaced", Font.PLAIN, 12));
        areaBusqueda.setEditable(false);
        areaBusqueda.setBackground(new Color(255, 252, 255));

        lblFotoConsulta = new JLabel("", SwingConstants.CENTER);
        lblFotoConsulta.setPreferredSize(new Dimension(160, 160));
        lblFotoConsulta.setBorder(BorderFactory.createLineBorder(COLOR_PRIMARIO, 2));
        lblFotoConsulta.setBackground(new Color(255, 240, 248));
        lblFotoConsulta.setOpaque(true);
        lblFotoConsulta.setText("<html><center>Sin<br>foto</center></html>");
        lblFotoConsulta.setFont(FUENTE_LABEL);
        lblFotoConsulta.setForeground(COLOR_PRIMARIO);

        panelResultados.add(new JScrollPane(areaBusqueda), BorderLayout.CENTER);
        panelResultados.add(lblFotoConsulta,                BorderLayout.EAST);

        panel.add(panelTop,        BorderLayout.NORTH);
        panel.add(panelResultados, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Construye el panel de eliminacion.
     * @return panel de eliminacion
     */
    private JPanel construirPanelEliminar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        JLabel lblInfo = new JLabel("<html><center>Ingrese el codigo o el id de microchip<br>"
                + "del minipig que desea eliminar.</center></html>", SwingConstants.CENTER);
        lblInfo.setFont(FUENTE_LABEL);
        lblInfo.setForeground(COLOR_BOTON2);

        txtEliminar = crearCampo();
        btnElimCod  = crearBoton("🗑️  Eliminar por Codigo",   new Color(190, 50, 70));
        btnElimCod.setActionCommand(EventosVentana.CMD_ELIM_COD);
        btnElimMic  = crearBoton("🗑️  Eliminar por Microchip", new Color(190, 50, 70));
        btnElimMic.setActionCommand(EventosVentana.CMD_ELIM_MIC);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(lblInfo, gbc);
        gbc.gridy = 1; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(crearLabel("Codigo / Microchip:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        panel.add(txtEliminar, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.weightx = 0;
        panel.add(btnElimCod, gbc);
        gbc.gridx = 1;
        panel.add(btnElimMic, gbc);
        return panel;
    }

    /**
     * Construye el panel de modificacion.
     * @return JScrollPane con el formulario de modificacion
     */
    private JScrollPane construirPanelModificar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COLOR_PANEL);
        panel.setBorder(new EmptyBorder(15, 20, 15, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill   = GridBagConstraints.HORIZONTAL;

        txtModCodigo    = crearCampo(); txtModCodigo.setEditable(false); txtModCodigo.setBackground(new Color(240, 220, 230));
        txtModMicrochip = crearCampo(); txtModMicrochip.setEditable(false); txtModMicrochip.setBackground(new Color(240, 220, 230));
        txtModNombre    = crearCampo(); txtModGenero    = crearCampo();
        txtModRaza      = crearCampo(); txtModColor     = crearCampo();
        txtModPeso      = crearCampo(); txtModAltura    = crearCampo();
        txtModCaract1   = crearCampo(); txtModCaract2   = crearCampo();
        txtModFoto      = crearCampo();

        JLabel lblAviso = new JLabel("🔒 Codigo y Microchip NO son modificables", SwingConstants.CENTER);
        lblAviso.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        lblAviso.setForeground(new Color(140, 50, 80));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 4;
        panel.add(lblAviso, gbc);
        gbc.gridwidth = 1;

        // Fila de carga
        JPanel panelCarga = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panelCarga.setBackground(COLOR_PANEL);
        panelCarga.add(crearLabel("Codigo a modificar:"));
        txtModCodigo.setPreferredSize(new Dimension(120, 28));
        panelCarga.add(txtModCodigo);
        btnCargarMod = crearBoton("🔍 Cargar Datos", COLOR_BOTON);
        btnCargarMod.setActionCommand(EventosVentana.CMD_CARGAR_MOD);
        panelCarga.add(btnCargarMod);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 4;
        panel.add(panelCarga, gbc);
        gbc.gridwidth = 1;

        String[][] filas = {{"Microchip (no modif.):", "Nombre:"}, {"Genero:", "Raza:"}, {"Color:", "Peso:"}, {"Altura:", "Caracteristica 1:"}};
        JTextField[][] camposMod = {{txtModMicrochip, txtModNombre}, {txtModGenero, txtModRaza}, {txtModColor, txtModPeso}, {txtModAltura, txtModCaract1}};

        for (int f = 0; f < filas.length; f++) {
            for (int c = 0; c < 2; c++) {
                gbc.gridx = c * 2; gbc.gridy = f + 2; gbc.weightx = 0;
                panel.add(crearLabel(filas[f][c]), gbc);
                gbc.gridx = c * 2 + 1; gbc.weightx = 0.5;
                panel.add(camposMod[f][c], gbc);
            }
        }

        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        panel.add(crearLabel("Caracteristica 2:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        panel.add(txtModCaract2, gbc);
        gbc.gridx = 2;
        panel.add(crearLabel("URL Foto:"), gbc);
        gbc.gridx = 3;
        panel.add(txtModFoto, gbc);

        btnGuardarMod = crearBoton("💾  Guardar Cambios", new Color(80, 150, 80));
        btnGuardarMod.setActionCommand(EventosVentana.CMD_GUARDAR_MOD);
        gbc.gridx = 1; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnGuardarMod, gbc);

        return new JScrollPane(panel);
    }

    /**
     * Construye la barra inferior con botones globales.
     * @return panel inferior
     */
    private JPanel construirBarraInferior() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setBackground(COLOR_PRIMARIO);

        btnLimpiar = crearBoton("🧹  Limpiar", new Color(160, 100, 50));
        btnLimpiar.setActionCommand(EventosVentana.CMD_LIMPIAR);
        btnSalir   = crearBoton("🚪  Salir",   new Color(100, 30, 50));
        btnSalir.setActionCommand(EventosVentana.CMD_SALIR);

        panel.add(btnLimpiar);
        panel.add(btnSalir);
        return panel;
    }

    // =====================================================================
    // REGISTRO DE LISTENERS (separado de la construccion de la UI)
    // =====================================================================

    /**
     * Registra {@code EventosVentana} como listener de todos los botones
     * y del combo desplegable.
     */
    private void registrarListeners() {
        btnInsertar.addActionListener(eventos);
        btnBuscarCod.addActionListener(eventos);
        btnBuscarMic.addActionListener(eventos);
        btnBuscarRaza.addActionListener(eventos);
        btnBuscarNom.addActionListener(eventos);
        btnElimCod.addActionListener(eventos);
        btnElimMic.addActionListener(eventos);
        btnCargarMod.addActionListener(eventos);
        btnGuardarMod.addActionListener(eventos);
        btnLimpiar.addActionListener(eventos);
        btnSalir.addActionListener(eventos);
        comboMinipigs.addActionListener(eventos);

        // Listener de cierre de ventana
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                btnSalir.doClick();
            }
        });
    }

    // =====================================================================
    // METODOS DE ACCESO PARA EventosVentana
    // =====================================================================

    /**
     * Lee y valida los campos del panel de insercion.
     * Muestra advertencia si faltan campos obligatorios.
     *
     * @return {@code MinipigVO} con los datos, o {@code null} si hay error
     */
    public MinipigVO obtenerDatosInsercion() {
        String codigo    = txtInsertCodigo.getText().trim();
        String nombre    = txtInsertNombre.getText().trim();
        String genero    = txtInsertGenero.getText().trim();
        String microchip = txtInsertMicrochip.getText().trim();
        String raza      = txtInsertRaza.getText().trim();

        if (codigo.isEmpty() || nombre.isEmpty() || genero.isEmpty()
                || microchip.isEmpty() || raza.isEmpty()) {
            mostrarMensaje("Complete los campos obligatorios (*).", "Campos requeridos", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        return new MinipigVO(
                codigo, nombre, genero, microchip, raza,
                txtInsertColor.getText().trim(),
                txtInsertPeso.getText().trim(),
                txtInsertAltura.getText().trim(),
                txtInsertCaract1.getText().trim(),
                txtInsertCaract2.getText().trim(),
                txtInsertFoto.getText().trim()
        );
    }

    /**
     * Retorna el valor del campo de busqueda.
     * @return texto del campo de busqueda
     */
    public String getCampoBusqueda() {
        return txtBusqueda.getText().trim();
    }

    /**
     * Retorna el valor del campo de eliminacion.
     * @return texto del campo de eliminacion
     */
    public String getCampoEliminar() {
        return txtEliminar.getText().trim();
    }

    /**
     * Retorna el codigo del campo de modificacion (campo de carga).
     * @return codigo para buscar al modificar
     */
    public String getCodigoModificacion() {
        return txtModCodigo.getText().trim();
    }

    /**
     * Carga los datos de un MinipigVO en los campos del panel de modificacion.
     * Los campos no editables (codigo, microchip) se muestran en solo lectura.
     *
     * @param m minipig cuyos datos se cargaran
     */
    public void cargarDatosEnModificacion(MinipigVO m) {
        txtModCodigo.setText(m.getCodigo());
        txtModMicrochip.setText(m.getIdMicrochip());
        txtModNombre.setText(m.getNombre());
        txtModGenero.setText(m.getGenero());
        txtModRaza.setText(m.getRaza());
        txtModColor.setText(m.getColor());
        txtModPeso.setText(m.getPeso());
        txtModAltura.setText(m.getAltura());
        txtModCaract1.setText(m.getCaracteristica1());
        txtModCaract2.setText(m.getCaracteristica2());
        txtModFoto.setText(m.getUrlFoto());
    }

    /**
     * Lee los datos del panel de modificacion y los retorna como MinipigVO (DTO en memoria).
     * No envia directamente al DAO; eso es responsabilidad del Gestor.
     *
     * @return {@code MinipigVO} con datos actualizados, o {@code null} si el codigo esta vacio
     */
    public MinipigVO obtenerDatosModificacion() {
        String codigo = txtModCodigo.getText().trim();
        if (codigo.isEmpty()) {
            mostrarMensaje("Cargue primero un minipig usando el boton 'Cargar Datos'.",
                    "Sin datos", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        return new MinipigVO(
                codigo,
                txtModNombre.getText().trim(),
                txtModGenero.getText().trim(),
                txtModMicrochip.getText().trim(),
                txtModRaza.getText().trim(),
                txtModColor.getText().trim(),
                txtModPeso.getText().trim(),
                txtModAltura.getText().trim(),
                txtModCaract1.getText().trim(),
                txtModCaract2.getText().trim(),
                txtModFoto.getText().trim()
        );
    }

    /**
     * Muestra un minipig en el area de resultados de consulta e intenta cargar su foto.
     *
     * @param m minipig a mostrar
     */
    public void mostrarMinipig(MinipigVO m) {
        areaBusqueda.setText(
            "═══════════════════════════════════════\n"
          + "  Codigo      : " + m.getCodigo()          + "\n"
          + "  Nombre      : " + m.getNombre()          + "\n"
          + "  Genero      : " + m.getGenero()          + "\n"
          + "  Microchip   : " + m.getIdMicrochip()     + "\n"
          + "  Raza        : " + m.getRaza()            + "\n"
          + "  Color       : " + m.getColor()           + "\n"
          + "  Peso        : " + m.getPeso()            + "\n"
          + "  Altura      : " + m.getAltura()          + "\n"
          + "  Caract. 1   : " + m.getCaracteristica1() + "\n"
          + "  Caract. 2   : " + m.getCaracteristica2() + "\n"
          + "  URL Foto    : " + m.getUrlFoto()         + "\n"
          + "═══════════════════════════════════════\n"
        );
        cargarFoto(m.getUrlFoto());
        pestanas.setSelectedIndex(1);
    }

    /**
     * Muestra una lista de minipigs en el area de resultados.
     *
     * @param lista  lista de minipigs
     * @param titulo titulo del resultado
     */
    public void mostrarListaMinipigs(ArrayList<MinipigVO> lista, String titulo) {
        if (lista.isEmpty()) {
            areaBusqueda.setText("Sin resultados para: " + titulo);
            return;
        }
        StringBuilder sb = new StringBuilder("=== " + titulo + " ===\n\n");
        for (MinipigVO m : lista) {
            sb.append("Codigo: ").append(m.getCodigo())
              .append("  |  Nombre: ").append(m.getNombre())
              .append("  |  Raza: ").append(m.getRaza()).append("\n");
        }
        areaBusqueda.setText(sb.toString());
        lblFotoConsulta.setIcon(null);
        lblFotoConsulta.setText("<html><center>Sin<br>foto</center></html>");
        pestanas.setSelectedIndex(1);
    }

    /**
     * Actualiza el combo desplegable con la lista actual de minipigs.
     *
     * @param lista lista actualizada de minipigs
     */
    public void refrescarCombo(ArrayList<MinipigVO> lista) {
        comboMinipigs.removeAllItems();
        comboMinipigs.addItem("-- Seleccione un minipig --");
        for (MinipigVO m : lista) {
            comboMinipigs.addItem(m.getCodigo() + " - " + m.getNombre());
        }
    }

    /**
     * Retorna el item seleccionado en el combo.
     * @return cadena "codigo - nombre" o null si es el item por defecto
     */
    public String getComboSeleccionado() {
        Object sel = comboMinipigs.getSelectedItem();
        if (sel == null || sel.toString().startsWith("--")) return null;
        return sel.toString();
    }

    /**
     * Limpia todos los campos de texto de todos los paneles.
     */
    public void limpiarCampos() {
        JTextField[] todos = {
            txtInsertCodigo, txtInsertNombre, txtInsertGenero, txtInsertMicrochip,
            txtInsertRaza, txtInsertColor, txtInsertPeso, txtInsertAltura,
            txtInsertCaract1, txtInsertCaract2, txtInsertFoto,
            txtBusqueda, txtEliminar,
            txtModCodigo, txtModMicrochip, txtModNombre, txtModGenero,
            txtModRaza, txtModColor, txtModPeso, txtModAltura,
            txtModCaract1, txtModCaract2, txtModFoto
        };
        for (JTextField t : todos) { if (t != null) t.setText(""); }
        if (areaBusqueda != null) areaBusqueda.setText("");
        if (lblFotoConsulta != null) { lblFotoConsulta.setIcon(null); lblFotoConsulta.setText("<html><center>Sin<br>foto</center></html>"); }
    }

    /**
     * Muestra un dialogo de mensaje al usuario.
     *
     * @param mensaje   texto del mensaje
     * @param titulo    titulo del dialogo
     * @param tipoIcono constante de {@code JOptionPane}
     */
    public void mostrarMensaje(String mensaje, String titulo, int tipoIcono) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipoIcono);
    }

    /**
     * Muestra un dialogo de confirmacion Si/No.
     *
     * @param pregunta texto de la pregunta
     * @return resultado de {@code JOptionPane} (YES_OPTION / NO_OPTION)
     */
    public int confirmarAccion(String pregunta) {
        return JOptionPane.showConfirmDialog(this, pregunta, "Confirmar", JOptionPane.YES_NO_OPTION);
    }

    // =====================================================================
    // UTILIDADES INTERNAS
    // =====================================================================

    /**
     * Crea un JTextField con el estilo del tema.
     * @return JTextField configurado
     */
    private JTextField crearCampo() {
        JTextField t = new JTextField(18);
        t.setFont(FUENTE_CAMPO);
        t.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1),
                BorderFactory.createEmptyBorder(3, 6, 3, 6)));
        return t;
    }

    /**
     * Crea un JLabel con el estilo del tema.
     * @param texto texto del label
     * @return JLabel configurado
     */
    private JLabel crearLabel(String texto) {
        JLabel l = new JLabel(texto);
        l.setFont(FUENTE_LABEL);
        l.setForeground(new Color(100, 30, 60));
        return l;
    }

    /**
     * Crea un JButton con el estilo del tema.
     * @param texto  texto del boton
     * @param fondo  color de fondo
     * @return JButton configurado
     */
    private JButton crearBoton(String texto, Color fondo) {
        JButton b = new JButton(texto);
        b.setFont(new Font("Segoe UI", Font.BOLD, 13));
        b.setBackground(fondo);
        b.setForeground(COLOR_TEXTO_BTN);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(fondo.darker(), 1),
                BorderFactory.createEmptyBorder(7, 18, 7, 18)));
        return b;
    }

    /**
     * Crea un TitledBorder con el estilo del tema.
     * @param titulo titulo del borde
     * @return TitledBorder configurado
     */
    private TitledBorder titulado(String titulo) {
        TitledBorder tb = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARIO, 1), titulo);
        tb.setTitleFont(FUENTE_LABEL);
        tb.setTitleColor(COLOR_PRIMARIO);
        return tb;
    }

    /**
     * Intenta cargar y mostrar una imagen desde la ruta dada.
     * Si no puede cargar, muestra un texto por defecto.
     *
     * @param ruta ruta o URL de la imagen
     */
    private void cargarFoto(String ruta) {
        if (ruta == null || ruta.isEmpty() || ruta.equalsIgnoreCase("null")) {
            lblFotoConsulta.setIcon(null);
            lblFotoConsulta.setText("<html><center>Sin<br>foto</center></html>");
            return;
        }
        try {
            BufferedImage img = ImageIO.read(new File(ruta));
            if (img != null) {
                Image scaled = img.getScaledInstance(155, 155, Image.SCALE_SMOOTH);
                lblFotoConsulta.setIcon(new ImageIcon(scaled));
                lblFotoConsulta.setText("");
            } else {
                lblFotoConsulta.setIcon(null);
                lblFotoConsulta.setText("<html><center>Foto no<br>disponible</center></html>");
            }
        } catch (Exception ex) {
            lblFotoConsulta.setIcon(null);
            lblFotoConsulta.setText("<html><center>Foto no<br>disponible</center></html>");
        }
    }
}
