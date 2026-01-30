package Vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;

public class HU12_GenerarReportes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HU12_GenerarReportes frame = new HU12_GenerarReportes();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HU12_GenerarReportes() {
        setTitle("Sistema de Gestión de Citas - Generar Reportes y Estadísticas");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1300, 800);

        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 244, 248));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);

        initComponents();
    }

    private void initComponents() {
        contentPane.add(createHeaderPanel(), BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(400);

        splitPane.setLeftComponent(createConfiguracionPanel());
        splitPane.setRightComponent(createVisualizacionPanel());

        contentPane.add(splitPane, BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(255, 152, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Reportes y Estadísticas", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel(
                "Analiza el rendimiento y toma decisiones basadas en datos",
                SwingConstants.CENTER
        );
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));

        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createConfiguracionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(255, 152, 0), 2),
                        "Configuración del Reporte",
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 15),
                        new Color(255, 152, 0)
                ),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        panel.add(createSectionLabel("Tipo de Reporte"));
        panel.add(Box.createVerticalStrut(10));

        JRadioButton rbGeneral = new JRadioButton("Reporte General de Citas", true);
        JRadioButton rbPersonal = new JRadioButton("Reporte por Personal");
        JRadioButton rbServicio = new JRadioButton("Reporte por Servicio");
        JRadioButton rbFinanciero = new JRadioButton("Reporte Financiero");
        JRadioButton rbOcupacion = new JRadioButton("Análisis de Ocupación");

        ButtonGroup grupo = new ButtonGroup();
        grupo.add(rbGeneral);
        grupo.add(rbPersonal);
        grupo.add(rbServicio);
        grupo.add(rbFinanciero);
        grupo.add(rbOcupacion);

        rbGeneral.setBackground(Color.WHITE);
        rbPersonal.setBackground(Color.WHITE);
        rbServicio.setBackground(Color.WHITE);
        rbFinanciero.setBackground(Color.WHITE);
        rbOcupacion.setBackground(Color.WHITE);

        panel.add(rbGeneral);
        panel.add(rbPersonal);
        panel.add(rbServicio);
        panel.add(rbFinanciero);
        panel.add(rbOcupacion);

        panel.add(Box.createVerticalStrut(20));
        panel.add(createSectionLabel("Período"));
        panel.add(Box.createVerticalStrut(10));

        JComboBox<String> cmbPeriodo = new JComboBox<>(new String[]{
                "Hoy", "Esta Semana", "Este Mes", "Últimos 3 Meses",
                "Últimos 6 Meses", "Este Año", "Personalizado"
        });
        cmbPeriodo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbPeriodo);

        panel.add(Box.createVerticalStrut(20));
        panel.add(createSectionLabel("Filtros Adicionales"));
        panel.add(Box.createVerticalStrut(10));

        JCheckBox chk1 = new JCheckBox("Incluir Atendidas", true);
        JCheckBox chk2 = new JCheckBox("Incluir Pendientes", true);
        JCheckBox chk3 = new JCheckBox("Incluir Canceladas", true);
        JCheckBox chk4 = new JCheckBox("Incluir No Asistió", true);

        chk1.setBackground(Color.WHITE);
        chk2.setBackground(Color.WHITE);
        chk3.setBackground(Color.WHITE);
        chk4.setBackground(Color.WHITE);

        panel.add(chk1);
        panel.add(chk2);
        panel.add(chk3);
        panel.add(chk4);

        panel.add(Box.createVerticalStrut(25));

        JButton btnGenerar = new JButton("Generar Reporte");
        btnGenerar.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnGenerar.setBackground(new Color(255, 152, 0));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.setFocusPainted(false);
        btnGenerar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        panel.add(btnGenerar);
        panel.add(Box.createVerticalGlue());

        return panel;
    }

    private JPanel createVisualizacionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Segoe UI", Font.BOLD, 13));

        tabs.addTab("Gráfico de Barras", new JPanel());
        tabs.addTab("Gráfico de Líneas", new JPanel());
        tabs.addTab("Gráfico Circular", new JPanel());
        tabs.addTab("Tabla Detallada", createTablaDetallada());

        panel.add(tabs, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createTablaDetallada() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columnas = {
                "Personal", "Total Citas", "Atendidas",
                "Canceladas", "Tasa (%)", "Ingresos"
        };

        Object[][] datos = {
                {"Dr. Juan Pérez", "245", "218", "19", "89.0%", "$7,350"},
                {"Dra. María González", "198", "175", "15", "88.4%", "$5,940"},
                {"Dr. Carlos Ramírez", "187", "159", "12", "85.0%", "$6,545"},
                {"Dra. Ana Martínez", "217", "186", "9", "85.7%", "$5,575"}
        };

        JTable tabla = new JTable(datos, columnas);
        tabla.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tabla.setRowHeight(35);
        tabla.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tabla.getTableHeader().setBackground(new Color(255, 152, 0));
        tabla.getTableHeader().setForeground(Color.WHITE);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private JLabel createSectionLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(50, 50, 50));
        return label;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);

        JButton btnImprimir = new JButton("Imprimir");
        JButton btnPDF = new JButton("Exportar PDF");
        JButton btnExcel = new JButton("Exportar Excel");
        JButton btnCompartir = new JButton("Compartir");

        panel.add(btnImprimir);
        panel.add(btnPDF);
        panel.add(btnExcel);
        panel.add(btnCompartir);

        return panel;
    }
}
