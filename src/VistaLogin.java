package interfaz;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.*;

public class VistaLogin extends JFrame {
    private final JPanel loginPanel;
    private final JTextField usuarioField;
    private final JPasswordField contrasenaField;
    private final JButton btnInicio;
    private final JButton btnCancelar;

    public VistaLogin() {
        setTitle("Formulario de Login");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setLayout(new BorderLayout());

        loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        usuarioField = new JTextField();
        contrasenaField = new JPasswordField();
        btnInicio = new JButton("Iniciar sesión");
        btnCancelar = new JButton("Cancelar");

        loginPanel.add(new JLabel("Usuario: "));
        loginPanel.add(usuarioField);
        loginPanel.add(new JLabel("Contraseña: "));
        loginPanel.add(contrasenaField);
        loginPanel.add(btnInicio);
        loginPanel.add(btnCancelar);
        loginPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15)); // Add some padding

        btnInicio.addActionListener(e -> autenticar());
        btnCancelar.addActionListener(e -> System.exit(0));


        add(loginPanel, BorderLayout.CENTER);
    }


    //MÉTODOS
    private void autenticar(){
        String usuario = usuarioField.getText();
        String contrasena = new String(contrasenaField.getPassword());
        
        if ("cliente".equals(usuario.toLowerCase()) && "1234".equals(contrasena)) {
            JOptionPane.showMessageDialog(this, "Login exitoso");
            // Crea y muestra el nuevo frame
            VistaCliente cliente = new VistaCliente();
            cliente.setVisible(true);
        
        // Cierra el frame de login
        this.dispose(); // Cierra el frame actual
        } else if ("admin".equals(usuario.toLowerCase()) && "5678".equals(contrasena)) {
            JOptionPane.showMessageDialog(this, "¡Bienvenido, jefe!");
            VistaAdmin admin = new VistaAdmin();
            admin.setVisible(true);

            this.dispose();
        }else {
            JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
