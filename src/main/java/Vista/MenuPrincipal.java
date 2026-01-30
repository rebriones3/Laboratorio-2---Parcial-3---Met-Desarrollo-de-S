package Vista;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MenuPrincipal extends JFrame {
    
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    
    public MenuPrincipal() {
        setTitle("Sistema de Gestión de Citas - Menú Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 600);
        
        contentPane = new JPanel();
        contentPane.setBackground(new Color(240, 244, 248));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new BorderLayout(10, 10));
        setContentPane(contentPane);
        
        initComponents();
        setLocationRelativeTo(null);
    }
    
    private void initComponents() {
        contentPane.add(createHeaderPanel(), BorderLayout.NORTH);
        contentPane.add(createMenuPanel(), BorderLayout.CENTER);
        contentPane.add(createFooterPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(63, 81, 181));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JLabel lblTitulo = new JLabel("Sistema de Gestión de Citas");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel lblSubtitulo = new JLabel("Práctica 5 - Metodologías de Desarrollo de Software");
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(255, 255, 255, 180));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblTitulo);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblSubtitulo);
        
        return panel;
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        // Título sección funcionales
        JLabel lblFuncionales = new JLabel("INTERFACES FUNCIONALES");
        lblFuncionales.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblFuncionales.setForeground(new Color(76, 175, 80));
        lblFuncionales.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblFuncionales);
        panel.add(Box.createVerticalStrut(12));
        
        // Botones funcionales
        panel.add(createMenuButton(
            "Agendar Cita",
            "HU01 - Funcionalidad completa",
            new Color(102, 126, 234),
            e -> abrirVentana(new HU01_AgendarCita())
        ));
        panel.add(Box.createVerticalStrut(8));
        
        panel.add(createMenuButton(
            "Consultar Disponibilidad",
            "HU02 - Actualización en tiempo real",
            new Color(76, 175, 80),
            e -> abrirVentana(new HU02_ConsultarDisponibilidad())
        ));
        panel.add(Box.createVerticalStrut(8));
        
        panel.add(createMenuButton(
            "Cancelar Cita",
            "HU04 - Validación de política",
            new Color(239, 83, 80),
            e -> abrirVentana(new HU04_CancelarCita())
        ));
        
        panel.add(Box.createVerticalStrut(20));
        panel.add(new JSeparator());
        panel.add(Box.createVerticalStrut(15));
        
        // Título sección diseño
        JLabel lblDiseno = new JLabel("INTERFACES SOLO DISEÑO");
        lblDiseno.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblDiseno.setForeground(new Color(255, 152, 0));
        lblDiseno.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lblDiseno);
        panel.add(Box.createVerticalStrut(12));
        
        // Botones diseño
        panel.add(createMenuButton(
            "Gestionar Horarios",
            "HU06 - Panel de administración",
            new Color(33, 150, 243),
            e -> abrirVentana(new HU06_GestionarHorarios())
        ));
        panel.add(Box.createVerticalStrut(8));
        
        panel.add(createMenuButton(
            "Historial de Citas",
            "HU09 - Vista de registros",
            new Color(156, 39, 176),
            e -> abrirVentana(new HU09_HistorialCitas())
        ));
        panel.add(Box.createVerticalStrut(8));
        
        panel.add(createMenuButton(
            "Generar Reportes",
            "HU12 - Dashboard de estadísticas",
            new Color(255, 152, 0),
            e -> abrirVentana(new HU12_GenerarReportes())
        ));
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JButton createMenuButton(String titulo, String descripcion, Color color, ActionListener action) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout(10, 0));
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color, 2),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        button.setFocusPainted(false);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Panel con texto
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setBackground(Color.WHITE);
        
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitulo.setForeground(color);
        
        JLabel lblDescripcion = new JLabel(descripcion);
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDescripcion.setForeground(new Color(120, 120, 120));
        
        textPanel.add(lblTitulo);
        textPanel.add(Box.createVerticalStrut(3));
        textPanel.add(lblDescripcion);
        
        // Flecha
        JLabel lblFlecha = new JLabel("→");
        lblFlecha.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblFlecha.setForeground(color);
        
        button.add(textPanel, BorderLayout.CENTER);
        button.add(lblFlecha, BorderLayout.EAST);
        
        // Efecto hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(248, 248, 248));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(Color.WHITE);
            }
        });
        
        button.addActionListener(action);
        
        return button;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(250, 250, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        JLabel lblInfo = new JLabel("NRC: 30746 | Docente: Ing. John Javier Cruz Garzón");
        lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblInfo.setForeground(new Color(100, 100, 100));
        
        JButton btnSalir = new JButton("Salir");
        btnSalir.setFont(new Font("Segoe UI", Font.BOLD, 11));
        btnSalir.setForeground(new Color(239, 83, 80));
        btnSalir.setBackground(Color.WHITE);
        btnSalir.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(239, 83, 80), 2),
            BorderFactory.createEmptyBorder(4, 12, 4, 12)
        ));
        btnSalir.setFocusPainted(false);
        btnSalir.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalir.addActionListener(e -> salirAplicacion());
        
        panel.add(lblInfo, BorderLayout.WEST);
        panel.add(btnSalir, BorderLayout.EAST);
        
        return panel;
    }
    
    private void abrirVentana(JFrame ventana) {
        ventana.setVisible(true);
    }
    
    private void salirAplicacion() {
        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Estás seguro de que deseas salir del sistema?",
            "Confirmar Salida",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (opcion == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
}