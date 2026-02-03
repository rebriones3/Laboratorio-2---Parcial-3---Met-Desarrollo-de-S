package Vista;

import controlador.CitaController;
import modelo.Cita;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import javax.swing.Timer;
import java.util.List;

public class HU01_AgendarCita extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JComboBox<String> cmbServicio;
    private JComboBox<String> cmbPersonal;
    private JTextField txtFecha;
    private JPanel pnlHorarios;
    private JTextArea txtMotivo;
    private JLabel lblResumenServicio, lblResumenPersonal, lblResumenFecha, lblResumenHora;
    private JButton btnConfirmar;
    
    private String horaSeleccionada = null;
    private ButtonGroup grupoHorarios;
    
    private CitaController controller;
    
    // Horarios disponibles estándar
    private static final LocalTime[] HORARIOS_DISPONIBLES = {
        LocalTime.of(8, 0), LocalTime.of(8, 30), LocalTime.of(9, 0), LocalTime.of(9, 30),
        LocalTime.of(10, 0), LocalTime.of(10, 30), LocalTime.of(11, 0), LocalTime.of(11, 30),
        LocalTime.of(14, 0), LocalTime.of(14, 30), LocalTime.of(15, 0), LocalTime.of(15, 30),
        LocalTime.of(16, 0), LocalTime.of(16, 30), LocalTime.of(17, 0), LocalTime.of(17, 30)
    };
    
    
    
    public HU01_AgendarCita() {
        setTitle("Sistema de Gestión de Citas - Agendar Nueva Cita");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 700);
        
        controller = new CitaController();
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 244, 248));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);
        
        initComponents();
    }
    
    private void initComponents() {
        JPanel headerPanel = createHeaderPanel();
        contentPane.add(headerPanel, BorderLayout.NORTH);
        
        JPanel centerPanel = createCenterPanel();
        JScrollPane scrollPane = new JScrollPane(centerPanel);
        scrollPane.setBorder(null);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        
        JPanel buttonPanel = createButtonPanel();
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(102, 126, 234));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        JLabel lblTitulo = new JLabel("Agendar Nueva Cita");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setHorizontalAlignment(JLabel.CENTER);
        
        JLabel lblSubtitulo = new JLabel("Selecciona fecha y hora para tu atención");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 200));
        lblSubtitulo.setHorizontalAlignment(JLabel.CENTER);
        
        panel.add(lblTitulo, BorderLayout.CENTER);
        panel.add(lblSubtitulo, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createCenterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        panel.add(createInfoBox());
        panel.add(Box.createVerticalStrut(20));
        
        panel.add(createFieldPanel("Tipo de Servicio *", 
            cmbServicio = new JComboBox<>(new String[]{
                "-- Seleccione un servicio --",
                "Consulta General",
                "Consulta Especializada", 
                "Seguimiento",
                "Exámenes de Laboratorio"
            })));
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createFieldPanel("Personal de Atención *",
            cmbPersonal = new JComboBox<>(new String[]{
                "-- Seleccione el profesional --",
                "Dr. Juan Pérez",
                "Dra. María González",
                "Dr. Carlos Ramírez",
                "Dra. Ana Martínez"
            })));
        panel.add(Box.createVerticalStrut(15));
        
        txtFecha = new JTextField();
        txtFecha.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtFecha.setEditable(false);
        JButton btnCalendario = new JButton("📅 Calendario");
        btnCalendario.addActionListener(e -> seleccionarFecha());
        
        JPanel fechaPanel = new JPanel(new BorderLayout(5, 0));
        fechaPanel.setBackground(Color.WHITE);
        fechaPanel.add(txtFecha, BorderLayout.CENTER);
        fechaPanel.add(btnCalendario, BorderLayout.EAST);
        
        panel.add(createFieldPanel("Fecha de la Cita *", fechaPanel));
        panel.add(Box.createVerticalStrut(15));
        
        JLabel lblHorarios = new JLabel("Horario Disponible *");
        lblHorarios.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblHorarios);
        panel.add(Box.createVerticalStrut(10));
        
        panel.add(createLeyenda());
        panel.add(Box.createVerticalStrut(10));
        
        pnlHorarios = createHorariosPanel();
        panel.add(pnlHorarios);
        panel.add(Box.createVerticalStrut(15));
        
        JLabel lblMotivo = new JLabel("Motivo de la Consulta (Opcional)");
        lblMotivo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        panel.add(lblMotivo);
        panel.add(Box.createVerticalStrut(5));
        
        txtMotivo = new JTextArea(3, 30);
        txtMotivo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtMotivo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        JScrollPane scrollMotivo = new JScrollPane(txtMotivo);
        panel.add(scrollMotivo);
        panel.add(Box.createVerticalStrut(20));
        
        panel.add(createResumenPanel());
        
        cmbServicio.addActionListener(e -> {
            actualizarResumen();
            actualizarHorarios();
        });
        cmbPersonal.addActionListener(e -> {
            actualizarResumen();
            actualizarHorarios();
        });
        
        return panel;
    }
    
    private JPanel createInfoBox() {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(new Color(227, 242, 253));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 181, 246), 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        
        JLabel icono = new JLabel("ℹ️");
        icono.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        
        JLabel mensaje = new JLabel("<html><b>Importante:</b> Las citas pueden ser canceladas con al menos 2 horas de anticipación.</html>");
        mensaje.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        panel.add(icono, BorderLayout.WEST);
        panel.add(mensaje, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createFieldPanel(String labelText, Component field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        if (field instanceof JComponent) {
            ((JComponent)field).setAlignmentX(Component.LEFT_ALIGNMENT);
            ((JComponent)field).setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        }
        
        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(field);
        
        return panel;
    }
    
    private JPanel createLeyenda() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        panel.add(createLeyendaItem("🟢 Disponible", new Color(76, 175, 80)));
        panel.add(createLeyendaItem("🔴 Ocupado", new Color(244, 67, 54)));
        
        return panel;
    }
    
    private JLabel createLeyendaItem(String texto, Color color) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(color);
        return label;
    }
    
    private JPanel createHorariosPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 4, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        grupoHorarios = new ButtonGroup();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        
        for (LocalTime hora : HORARIOS_DISPONIBLES) {
            JToggleButton btnHora = new JToggleButton(hora.format(formatter));
            btnHora.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnHora.setBackground(new Color(232, 245, 233));
            btnHora.setForeground(new Color(46, 125, 50));
            btnHora.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
            btnHora.setFocusPainted(false);
            
            btnHora.addActionListener(e -> {
                horaSeleccionada = btnHora.getText();
                actualizarResumen();
                validarFormulario();
            });
            
            grupoHorarios.add(btnHora);
            panel.add(btnHora);
        }
        
        return panel;
    }
    
    private JPanel createResumenPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        JLabel titulo = new JLabel("📋 Resumen de tu Cita");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titulo);
        panel.add(Box.createVerticalStrut(15));
        
        panel.add(createResumenItem("Servicio:", lblResumenServicio = new JLabel("-")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createResumenItem("Personal:", lblResumenPersonal = new JLabel("-")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createResumenItem("Fecha:", lblResumenFecha = new JLabel("-")));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createResumenItem("Hora:", lblResumenHora = new JLabel("-")));
        
        return panel;
    }
    
    private JPanel createResumenItem(String label, JLabel valorLabel) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblLabel.setForeground(new Color(102, 102, 102));
        
        valorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valorLabel.setForeground(new Color(51, 51, 51));
        
        panel.add(lblLabel, BorderLayout.WEST);
        panel.add(valorLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(224, 224, 224)));
        
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setPreferredSize(new Dimension(150, 45));
        btnCancelar.setBackground(Color.WHITE);
        btnCancelar.setForeground(new Color(102, 126, 234));
        btnCancelar.setBorder(BorderFactory.createLineBorder(new Color(102, 126, 234), 2));
        btnCancelar.setFocusPainted(false);
        btnCancelar.addActionListener(e -> limpiarFormulario());
        
        btnConfirmar = new JButton("Confirmar Cita");
        btnConfirmar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConfirmar.setPreferredSize(new Dimension(150, 45));
        btnConfirmar.setBackground(new Color(102, 126, 234));
        btnConfirmar.setForeground(Color.WHITE);
        btnConfirmar.setBorder(null);
        btnConfirmar.setFocusPainted(false);
        btnConfirmar.setEnabled(false);
        btnConfirmar.addActionListener(e -> confirmarCita());
        
        panel.add(btnCancelar);
        panel.add(btnConfirmar);
        
        return panel;
    }
    
    private void seleccionarFecha() {
        LocalDate hoy = LocalDate.now();
        txtFecha.setText(hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        actualizarResumen();
        actualizarHorarios();
        validarFormulario();
    }
    
    /**
     * ✅ MÉTODO NUEVO: Actualiza los horarios disponibles según personal y fecha
     */
    private void actualizarHorarios() {
        // Verificar que tengamos personal y fecha seleccionados
        if (cmbPersonal.getSelectedIndex() <= 0 || txtFecha.getText().trim().isEmpty()) {
            return;
        }
        
        try {
            String personal = cmbPersonal.getSelectedItem().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fecha = LocalDate.parse(txtFecha.getText(), formatter);
            
            // Obtener horarios ocupados del controlador
            List<LocalTime> horariosOcupados = controller.obtenerHorariosOcupados(personal, fecha);
            
            // Actualizar botones de horarios
            Component[] components = pnlHorarios.getComponents();
            for (Component comp : components) {
                if (comp instanceof JToggleButton) {
                    JToggleButton btn = (JToggleButton) comp;
                    LocalTime horaBoton = LocalTime.parse(btn.getText());
                    
                    boolean ocupado = horariosOcupados.contains(horaBoton);
                    
                    if (ocupado) {
                        // Marcar como ocupado
                        btn.setEnabled(false);
                        btn.setBackground(new Color(255, 235, 238));
                        btn.setForeground(new Color(198, 40, 40));
                        btn.setBorder(BorderFactory.createLineBorder(new Color(244, 67, 54), 2));
                    } else {
                        // Marcar como disponible
                        btn.setEnabled(true);
                        btn.setBackground(new Color(232, 245, 233));
                        btn.setForeground(new Color(46, 125, 50));
                        btn.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al actualizar horarios: " + e.getMessage());
        }
    }
    
    private void actualizarResumen() {
        if (cmbServicio.getSelectedIndex() > 0) {
            lblResumenServicio.setText(cmbServicio.getSelectedItem().toString());
        } else {
            lblResumenServicio.setText("-");
        }
        
        if (cmbPersonal.getSelectedIndex() > 0) {
            lblResumenPersonal.setText(cmbPersonal.getSelectedItem().toString());
        } else {
            lblResumenPersonal.setText("-");
        }
        
        if (!txtFecha.getText().trim().isEmpty()) {
            lblResumenFecha.setText(txtFecha.getText());
        } else {
            lblResumenFecha.setText("-");
        }
        
        if (horaSeleccionada != null) {
            lblResumenHora.setText(horaSeleccionada);
        } else {
            lblResumenHora.setText("-");
        }
    }
    
    private void validarFormulario() {
        boolean valido = cmbServicio.getSelectedIndex() > 0 &&
                        cmbPersonal.getSelectedIndex() > 0 &&
                        !txtFecha.getText().trim().isEmpty() &&
                        horaSeleccionada != null;
        
        btnConfirmar.setEnabled(valido);
    }
    
    private void confirmarCita() {
        btnConfirmar.setEnabled(false);
        btnConfirmar.setText("Procesando...");
        
        Timer timer = new Timer(1500, e -> {
            try {
                String servicio = cmbServicio.getSelectedItem().toString();
                String personal = cmbPersonal.getSelectedItem().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fecha = LocalDate.parse(txtFecha.getText(), formatter);
                LocalTime hora = LocalTime.parse(horaSeleccionada);
                String motivo = txtMotivo.getText().trim();
                
                CitaController.ResultadoOperacion resultado = 
                    controller.agendarCita(servicio, personal, fecha, hora, motivo);
                
                if (resultado.isExitoso()) {
                    JOptionPane.showMessageDialog(this,
                        "✅ ¡Cita confirmada exitosamente!\n\n" +
                        "Código de confirmación: " + resultado.getCodigo() + "\n" +
                        "Recibirás un email con los detalles.",
                        "Confirmación Exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    Timer emailTimer = new Timer(2000, ev -> {
                        JOptionPane.showMessageDialog(this,
                            "📧 Email de confirmación enviado.",
                            "Notificación",
                            JOptionPane.INFORMATION_MESSAGE);
                    });
                    emailTimer.setRepeats(false);
                    emailTimer.start();
                    
                    limpiarFormulario();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "❌ Error al agendar la cita:\n" + resultado.getMensaje(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                    btnConfirmar.setEnabled(true);
                    btnConfirmar.setText("Confirmar Cita");
                }
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "❌ Error: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                btnConfirmar.setEnabled(true);
                btnConfirmar.setText("Confirmar Cita");
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
    
    private void limpiarFormulario() {
        cmbServicio.setSelectedIndex(0);
        cmbPersonal.setSelectedIndex(0);
        txtFecha.setText("");
        txtMotivo.setText("");
        grupoHorarios.clearSelection();
        horaSeleccionada = null;
        
        lblResumenServicio.setText("-");
        lblResumenPersonal.setText("-");
        lblResumenFecha.setText("-");
        lblResumenHora.setText("-");
        
        btnConfirmar.setEnabled(false);
        btnConfirmar.setText("Confirmar Cita");
        
        Component[] components = pnlHorarios.getComponents();
        for (Component comp : components) {
            if (comp instanceof JToggleButton) {
                JToggleButton btn = (JToggleButton) comp;
                btn.setEnabled(true);
                btn.setBackground(new Color(232, 245, 233));
                btn.setForeground(new Color(46, 125, 50));
                btn.setBorder(BorderFactory.createLineBorder(new Color(76, 175, 80), 2));
            }
        }
    }
}