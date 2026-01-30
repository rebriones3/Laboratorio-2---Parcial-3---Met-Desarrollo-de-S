package Vista;

import controlador.CitaController;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HU02_ConsultarDisponibilidad extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaDisponibilidad;
    private DefaultTableModel modeloTabla;
    private JTextField txtFecha;
    private JComboBox<String> cmbPersonal;
    
    private CitaController controller;
    
    private static final String[] PERSONAL = {
        "Todos",
        "Dr. Juan Pérez",
        "Dra. María González",
        "Dr. Carlos Ramírez",
        "Dra. Ana Martínez"
    };
    
    private static final LocalTime[] HORARIOS = {
        LocalTime.of(8, 0), LocalTime.of(9, 0), LocalTime.of(10, 0), LocalTime.of(11, 0),
        LocalTime.of(14, 0), LocalTime.of(15, 0), LocalTime.of(16, 0), LocalTime.of(17, 0)
    };

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HU02_ConsultarDisponibilidad frame = new HU02_ConsultarDisponibilidad();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HU02_ConsultarDisponibilidad() {
        setTitle("Sistema de Gestión de Citas - Consultar Disponibilidad");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1000, 650);

        controller = new CitaController();

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

        centerPanel.add(createFiltrosPanel(), BorderLayout.NORTH);
        centerPanel.add(createTablaPanel(), BorderLayout.CENTER);

        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(76, 175, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Consultar Disponibilidad");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblSubtitulo = new JLabel("Verifica horarios disponibles en tiempo real");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createFiltrosPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(76, 175, 80), 2),
                "Filtros de Búsqueda",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(76, 175, 80)
        ));

        panel.add(new JLabel("Fecha:"));
        txtFecha = new JTextField(12);
        txtFecha.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        panel.add(txtFecha);

        panel.add(new JLabel("Personal:"));
        cmbPersonal = new JComboBox<>(PERSONAL);
        panel.add(cmbPersonal);

        JButton btnBuscar = new JButton("Buscar");
        btnBuscar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnBuscar.setBackground(new Color(76, 175, 80));
        btnBuscar.setForeground(Color.WHITE);
        btnBuscar.setFocusPainted(false);
        btnBuscar.addActionListener(e -> cargarDisponibilidad());
        panel.add(btnBuscar);

        return panel;
    }

    private JPanel createTablaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Hora", "Dr. Juan Pérez", "Dra. María González",
                "Dr. Carlos Ramírez", "Dra. Ana Martínez"};

        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaDisponibilidad = new JTable(modeloTabla);
        tablaDisponibilidad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaDisponibilidad.setRowHeight(40);
        tablaDisponibilidad.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaDisponibilidad.getTableHeader().setBackground(new Color(76, 175, 80));
        tablaDisponibilidad.getTableHeader().setForeground(Color.WHITE);

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (column > 0) {
                    String estado = value.toString();
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 12));

                    if (estado.equals("Disponible")) {
                        setBackground(new Color(232, 245, 233));
                        setForeground(new Color(76, 175, 80));
                    } else if (estado.equals("Ocupado")) {
                        setBackground(new Color(255, 235, 238));
                        setForeground(new Color(239, 83, 80));
                    }
                } else {
                    setBackground(Color.WHITE);
                    setForeground(Color.BLACK);
                    setHorizontalAlignment(CENTER);
                    setFont(new Font("Segoe UI", Font.BOLD, 13));
                }

                return c;
            }
        };

        for (int i = 1; i < tablaDisponibilidad.getColumnCount(); i++) {
            tablaDisponibilidad.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        JScrollPane scrollPane = new JScrollPane(tablaDisponibilidad);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);

        cargarDisponibilidad();
        return panel;
    }

    private void cargarDisponibilidad() {
        modeloTabla.setRowCount(0);
        
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(txtFecha.getText(), formatter);
            
            for (LocalTime hora : HORARIOS) {
                Object[] fila = new Object[5];
                fila[0] = hora.format(DateTimeFormatter.ofPattern("HH:mm"));
                
                // Para cada personal, verificar disponibilidad
                for (int i = 1; i < PERSONAL.length; i++) {
                    String personal = PERSONAL[i];
                    boolean disponible = controller.verificarDisponibilidad(personal, fecha, hora);
                    fila[i] = disponible ? "Disponible" : "Ocupado";
                }
                
                modeloTabla.addRow(fila);
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error al cargar disponibilidad: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0,
                new Color(224, 224, 224)));

        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.setPreferredSize(new Dimension(150, 45));
        btnActualizar.setBackground(new Color(76, 175, 80));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
            cargarDisponibilidad();
            JOptionPane.showMessageDialog(this,
                    "Disponibilidad actualizada correctamente",
                    "Actualización",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        panel.add(btnActualizar);
        return panel;
    }
}