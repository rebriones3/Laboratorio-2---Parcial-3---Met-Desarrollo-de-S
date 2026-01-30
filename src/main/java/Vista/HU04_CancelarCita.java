package Vista;

import controlador.CitaController;
import modelo.Cita;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class HU04_CancelarCita extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaCitas;
    private DefaultTableModel modeloTabla;
    private JButton btnCancelar, btnActualizar;
    
    private CitaController controller;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                HU04_CancelarCita frame = new HU04_CancelarCita();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public HU04_CancelarCita() {
        setTitle("Sistema de Gestión de Citas - Cancelar Cita");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 1100, 700);

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
        contentPane.add(createTablaPanel(), BorderLayout.CENTER);
        contentPane.add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(239, 83, 80));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JLabel lblTitulo = new JLabel("Cancelar Cita");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);

        JLabel lblSubtitulo = new JLabel("Gestiona y cancela tus citas con al menos 2 horas de anticipación");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);

        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createTablaPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        String[] columnas = {"Código", "Fecha", "Hora", "Servicio", "Personal", "Estado", "Acción"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaCitas = new JTable(modeloTabla);
        tablaCitas.setRowHeight(40);
        tablaCitas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaCitas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaCitas.getTableHeader().setBackground(new Color(239, 83, 80));
        tablaCitas.getTableHeader().setForeground(Color.WHITE);

        tablaCitas.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                String estado = value.toString();
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.BOLD, 12));

                if (estado.equals("PENDIENTE")) {
                    setForeground(new Color(76, 175, 80));
                } else if (estado.equals("CANCELADA")) {
                    setForeground(new Color(239, 83, 80));
                } else if (estado.equals("ATENDIDA")) {
                    setForeground(new Color(33, 150, 243));
                }

                setBackground(isSelected ? new Color(255, 205, 210) : Color.WHITE);
                return this;
            }
        });
        
        tablaCitas.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                          boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                setFont(new Font("Segoe UI", Font.PLAIN, 12));
                
                if (value.toString().equals("Puede cancelar")) {
                    setForeground(new Color(76, 175, 80));
                } else {
                    setForeground(new Color(158, 158, 158));
                }
                
                setBackground(isSelected ? new Color(255, 205, 210) : Color.WHITE);
                return this;
            }
        });

        tablaCitas.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                btnCancelar.setEnabled(tablaCitas.getSelectedRow() >= 0);
            }
        });

        JScrollPane scrollPane = new JScrollPane(tablaCitas);
        panel.add(scrollPane, BorderLayout.CENTER);

        cargarCitas();
        return panel;
    }

    private void cargarCitas() {
        modeloTabla.setRowCount(0);
        
        List<Cita> citas = controller.obtenerCitasPendientes();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (Cita cita : citas) {
            String accion = cita.puedeCancelar() ? "Puede cancelar" : "No cancelable";
            modeloTabla.addRow(new Object[]{
                cita.getCodigo(),
                cita.getFecha().format(dateFormatter),
                cita.getHora().format(timeFormatter),
                cita.getServicio(),
                cita.getPersonal(),
                cita.getEstado().name(),
                accion
            });
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));

        btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnActualizar.setPreferredSize(new Dimension(150, 40));
        btnActualizar.setFocusPainted(false);
        btnActualizar.addActionListener(e -> {
            cargarCitas();
            JOptionPane.showMessageDialog(this,
                    "Lista actualizada correctamente",
                    "Actualización",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        btnCancelar = new JButton("Cancelar Cita Seleccionada");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCancelar.setPreferredSize(new Dimension(200, 40));
        btnCancelar.setBackground(new Color(239, 83, 80));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(e -> cancelarCitaSeleccionada());

        panel.add(btnActualizar);
        panel.add(btnCancelar);
        return panel;
    }
    
    private void cancelarCitaSeleccionada() {
        int row = tablaCitas.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                "Por favor seleccione una cita",
                "Advertencia",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = modeloTabla.getValueAt(row, 0).toString();
        String accion = modeloTabla.getValueAt(row, 6).toString();
        
        if (!accion.equals("Puede cancelar")) {
            JOptionPane.showMessageDialog(this,
                "Esta cita no puede ser cancelada.\nDebe cancelar con al menos 2 horas de anticipación.",
                "No cancelable",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "¿Está seguro que desea cancelar la cita " + codigo + "?",
            "Confirmar Cancelación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            CitaController.ResultadoOperacion resultado = controller.cancelarCita(codigo);
            
            if (resultado.isExitoso()) {
                JOptionPane.showMessageDialog(this,
                    resultado.getMensaje(),
                    "Cancelación Exitosa",
                    JOptionPane.INFORMATION_MESSAGE);
                cargarCitas();
            } else {
                JOptionPane.showMessageDialog(this,
                    resultado.getMensaje(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}