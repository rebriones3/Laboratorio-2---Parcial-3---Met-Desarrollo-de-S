package Vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;

public class HU06_GestionarHorarios extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaHorarios;
    private DefaultTableModel modeloTabla;
    
    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HU06_GestionarHorarios frame = new HU06_GestionarHorarios();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    /**
     * Create the frame.
     */
    public HU06_GestionarHorarios() {
        setTitle("Sistema de Gestión de Citas - Gestionar Horarios de Atención");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1200, 750);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 244, 248));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);
        
        initComponents();
    }
    
    private void initComponents() {
        contentPane.add(createHeaderPanel(), BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        centerPanel.add(createFormPanel(), BorderLayout.WEST);
        centerPanel.add(createCalendarioPanel(), BorderLayout.CENTER);
        
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(33, 150, 243));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("Gestionar Horarios de Atención");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblSubtitulo = new JLabel("Configura tu disponibilidad y bloques de atención");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createFormPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setPreferredSize(new Dimension(350, 0));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(33, 150, 243), 2),
                "Crear Bloque de Disponibilidad",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(33, 150, 243)
            ),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        panel.add(createLabel("Tipo de Bloque"));
        JComboBox<String> cmbTipo = new JComboBox<>(new String[]{
            "Disponibilidad Normal",
            "Bloqueo (Almuerzo)",
            "Bloqueo (Reunión)",
            "Bloqueo (Otros)"
        });
        cmbTipo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbTipo);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createLabel("Día de la Semana"));
        JComboBox<String> cmbDia = new JComboBox<>(new String[]{
            "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
        });
        cmbDia.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbDia);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createLabel("Fecha Específica (Opcional)"));
        JTextField txtFecha = new JTextField();
        txtFecha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(txtFecha);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createLabel("Hora de Inicio"));
        JPanel pnlHoraInicio = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlHoraInicio.setBackground(Color.WHITE);
        pnlHoraInicio.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JSpinner spnHoraInicio = new JSpinner(new SpinnerNumberModel(8, 0, 23, 1));
        JSpinner spnMinInicio = new JSpinner(new SpinnerNumberModel(0, 0, 59, 15));
        pnlHoraInicio.add(spnHoraInicio);
        pnlHoraInicio.add(spnMinInicio);
        panel.add(pnlHoraInicio);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createLabel("Hora de Fin"));
        JPanel pnlHoraFin = new JPanel(new GridLayout(1, 2, 10, 0));
        pnlHoraFin.setBackground(Color.WHITE);
        pnlHoraFin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        JSpinner spnHoraFin = new JSpinner(new SpinnerNumberModel(17, 0, 23, 1));
        JSpinner spnMinFin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 15));
        pnlHoraFin.add(spnHoraFin);
        pnlHoraFin.add(spnMinFin);
        panel.add(pnlHoraFin);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createLabel("Duración de Cada Cita (minutos)"));
        JComboBox<Integer> cmbDuracion = new JComboBox<>(new Integer[]{15, 20, 30, 45, 60});
        cmbDuracion.setSelectedItem(30);
        cmbDuracion.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        panel.add(cmbDuracion);
        panel.add(Box.createVerticalStrut(15));
        
        JCheckBox chkRepetir = new JCheckBox("Repetir todas las semanas");
        chkRepetir.setBackground(Color.WHITE);
        chkRepetir.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(chkRepetir);
        panel.add(Box.createVerticalStrut(20));
        
        JButton btnAgregar = new JButton("➕ Agregar Bloque");
        btnAgregar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnAgregar.setBackground(new Color(33, 150, 243));
        btnAgregar.setForeground(Color.WHITE);
        btnAgregar.setFocusPainted(false);
        btnAgregar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAgregar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.add(btnAgregar);
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createCalendarioPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(Color.WHITE);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel("📅 Mi Calendario de Disponibilidad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        topPanel.add(lblTitulo, BorderLayout.WEST);
        
        JPanel filtrosPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        filtrosPanel.setBackground(Color.WHITE);
        
        JComboBox<String> cmbVista = new JComboBox<>(new String[]{
            "Vista Semanal", "Vista Mensual", "Vista Diaria"
        });
        filtrosPanel.add(new JLabel("Vista:"));
        filtrosPanel.add(cmbVista);
        
        topPanel.add(filtrosPanel, BorderLayout.EAST);
        
        panel.add(topPanel, BorderLayout.NORTH);
        
        String[] columnas = {"Día", "Hora Inicio", "Hora Fin", "Tipo", "Estado", "Citas Agendadas", "Acciones"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        Object[][] datos = {
            {"Lunes", "08:00", "12:00", "Disponibilidad", "Activo", "5/8", "Editar | Eliminar"},
            {"Lunes", "13:00", "14:00", "Bloqueo - Almuerzo", "Activo", "-", "Editar | Eliminar"},
            {"Lunes", "14:00", "18:00", "Disponibilidad", "Activo", "7/8", "Editar | Eliminar"},
            {"Martes", "08:00", "12:00", "Disponibilidad", "Activo", "3/8", "Editar | Eliminar"},
            {"Martes", "14:00", "18:00", "Disponibilidad", "Activo", "6/8", "Editar | Eliminar"},
            {"Miércoles", "09:00", "13:00", "Disponibilidad", "Activo", "4/8", "Editar | Eliminar"},
            {"Miércoles", "15:00", "16:00", "Bloqueo - Reunión", "Activo", "-", "Editar | Eliminar"},
            {"Jueves", "08:00", "12:00", "Disponibilidad", "Activo", "2/8", "Editar | Eliminar"},
            {"Viernes", "08:00", "14:00", "Disponibilidad", "Activo", "8/12", "Editar | Eliminar"}
        };
        
        for (Object[] fila : datos) {
            modeloTabla.addRow(fila);
        }
        
        tablaHorarios = new JTable(modeloTabla);
        tablaHorarios.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHorarios.setRowHeight(40);
        tablaHorarios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaHorarios.getTableHeader().setBackground(new Color(33, 150, 243));
        tablaHorarios.getTableHeader().setForeground(Color.WHITE);
        
        tablaHorarios.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                String tipo = value.toString();
                if (tipo.startsWith("Bloqueo")) {
                    setForeground(new Color(239, 83, 80));
                } else {
                    setForeground(new Color(76, 175, 80));
                }
                setBackground(isSelected ? new Color(187, 222, 251) : Color.WHITE);
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tablaHorarios);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statsPanel.setBackground(new Color(245, 245, 245));
        statsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        statsPanel.add(createStatBox("Total Bloques", "9", new Color(33, 150, 243)));
        statsPanel.add(createStatBox("Horas Disponibles/Semana", "40h", new Color(76, 175, 80)));
        statsPanel.add(createStatBox("Ocupación Promedio", "65%", new Color(255, 152, 0)));
        statsPanel.add(createStatBox("Citas Esta Semana", "35", new Color(156, 39, 176)));
        
        panel.add(statsPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createStatBox(String label, String value, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblValue.setForeground(color);
        lblValue.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLabel.setForeground(new Color(100, 100, 100));
        lblLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblValue);
        panel.add(lblLabel);
        
        return panel;
    }
    
    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));
        
        JButton btnCopiar = new JButton("📋 Copiar Semana");
        btnCopiar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnCopiar.setPreferredSize(new Dimension(150, 40));
        btnCopiar.setFocusPainted(false);
        
        JButton btnExportar = new JButton("📥 Exportar");
        btnExportar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnExportar.setPreferredSize(new Dimension(120, 40));
        btnExportar.setFocusPainted(false);
        
        JButton btnGuardar = new JButton("💾 Guardar Cambios");
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setPreferredSize(new Dimension(180, 40));
        btnGuardar.setBackground(new Color(33, 150, 243));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        
        panel.add(btnCopiar);
        panel.add(btnExportar);
        panel.add(btnGuardar);
        
        return panel;
    }
}