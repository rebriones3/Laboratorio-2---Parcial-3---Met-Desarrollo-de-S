package Vista;

import controlador.CitaController;
import modelo.Cita;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HU09_HistorialCitas extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    
    private CitaController controller;
    private JLabel lblTotalCitas, lblAtendidas, lblPendientes, lblCanceladas;
    
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    HU09_HistorialCitas frame = new HU09_HistorialCitas();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public HU09_HistorialCitas() {
        setTitle("Sistema de Gestión de Citas - Historial de Citas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1200, 700);
        
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
        
        centerPanel.add(createTablaPanel(), BorderLayout.CENTER);
        centerPanel.add(createResumenPanel(), BorderLayout.SOUTH);
        
        contentPane.add(centerPanel, BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(156, 39, 176));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("📚 Mi Historial de Citas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblSubtitulo = new JLabel("Consulta todas tus citas pasadas, presentes y futuras");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createTablaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        String[] columnas = {"Código", "Fecha", "Hora", "Servicio", "Personal", "Estado", "Motivo"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaHistorial.setRowHeight(38);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tablaHistorial.getTableHeader().setBackground(new Color(156, 39, 176));
        tablaHistorial.getTableHeader().setForeground(Color.WHITE);
        tablaHistorial.setSelectionBackground(new Color(225, 190, 231));
        
        tablaHistorial.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                String estado = value.toString();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                switch (estado) {
                    case "ATENDIDA":
                        setForeground(new Color(76, 175, 80));
                        break;
                    case "PENDIENTE":
                        setForeground(new Color(33, 150, 243));
                        break;
                    case "CANCELADA":
                        setForeground(new Color(239, 83, 80));
                        break;
                    case "NO_ASISTIO":
                        setForeground(new Color(255, 152, 0));
                        break;
                    default:
                        setForeground(Color.BLACK);
                        break;
                }

                setBackground(isSelected ? new Color(225, 190, 231) : Color.WHITE);
                return c;
            }
        });
        
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(120);
        tablaHistorial.getColumnModel().getColumn(6).setPreferredWidth(200);
        
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panel.add(scrollPane, BorderLayout.CENTER);
        
        cargarHistorial();
        
        return panel;
    }
    
    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        
        List<Cita> citas = controller.obtenerTodasLasCitas();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Cita cita : citas) {
            modeloTabla.addRow(new Object[]{
                cita.getCodigo(),
                cita.getFecha().format(dateFormatter),
                cita.getHora().format(timeFormatter),
                cita.getServicio(),
                cita.getPersonal(),
                cita.getEstado().name(),
                cita.getMotivo() != null ? cita.getMotivo() : "-"
            });
        }
        
        actualizarEstadisticas();
    }
    
    private void actualizarEstadisticas() {
        CitaController.EstadisticasCitas stats = controller.obtenerEstadisticas();
        
        lblTotalCitas.setText(String.valueOf(stats.getTotal()));
        lblAtendidas.setText(String.valueOf(stats.getAtendidas()));
        lblPendientes.setText(String.valueOf(stats.getPendientes()));
        lblCanceladas.setText(String.valueOf(stats.getCanceladas() + stats.getNoAsistio()));
    }
    
    private JPanel createResumenPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 0));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        panel.add(createEstadisticaBox("Total Citas", lblTotalCitas = new JLabel("0"), new Color(156, 39, 176)));
        panel.add(createEstadisticaBox("Atendidas", lblAtendidas = new JLabel("0"), new Color(76, 175, 80)));
        panel.add(createEstadisticaBox("Pendientes", lblPendientes = new JLabel("0"), new Color(33, 150, 243)));
        panel.add(createEstadisticaBox("Canceladas/No Asistió", lblCanceladas = new JLabel("0"), new Color(239, 83, 80)));
        
        return panel;
    }
    
    private JPanel createEstadisticaBox(String titulo, JLabel lblValor, Color color) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        
        lblValor.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValor.setForeground(color);
        lblValor.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblTitulo.setForeground(new Color(100, 100, 100));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblValor);
        panel.add(lblTitulo);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));
        
        JButton btnActualizar = new JButton("Actualizar");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnActualizar.setPreferredSize(new Dimension(150, 40));
        btnActualizar.setBackground(new Color(156, 39, 176));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
            cargarHistorial();
            JOptionPane.showMessageDialog(this,
                "Historial actualizado correctamente",
                "Actualización",
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        panel.add(btnActualizar);
        
        return panel;
    }
}