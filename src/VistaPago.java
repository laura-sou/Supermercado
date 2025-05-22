package interfaz;

import java.awt.*;
import javax.swing.*;

public class VistaPago extends JFrame {
    private double total;

    public VistaPago(double total) {
        this.total = total;

        setTitle("Método de Pago");
        setSize(300, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JButton btnNequi = new JButton("Nequi");
        JButton btnDaviplata = new JButton("Daviplata");
        JButton btnEfectivo = new JButton("Efectivo");
        JButton btnTarjeta = new JButton("Tarjeta");

        panel.add(btnNequi);
        panel.add(btnDaviplata);
        panel.add(btnEfectivo);
        panel.add(btnTarjeta);

        add(panel);

        btnNequi.addActionListener(e -> abrirVentanaPagoMovil("Nequi"));
        btnDaviplata.addActionListener(e -> abrirVentanaPagoMovil("Daviplata"));
        btnEfectivo.addActionListener(e -> abrirVentanaEfectivo());
        btnTarjeta.addActionListener(e -> abrirVentanaTarjeta());
    }

    private void abrirVentanaPagoMovil(String metodo) {
        JFrame ventana = new JFrame("Pago con " + metodo);
        ventana.setSize(300, 250);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField nombre = new JTextField();
        JTextField telefono = new JTextField();
        JTextField transaccion = new JTextField();
        JButton confirmar = new JButton("Confirmar");

        ventana.add(new JLabel("Nombre:"));
        ventana.add(nombre);
        ventana.add(new JLabel("Teléfono:"));
        ventana.add(telefono);
        ventana.add(new JLabel("No. Transacción:"));
        ventana.add(transaccion);
        ventana.add(new JLabel());
        ventana.add(confirmar);

        confirmar.addActionListener(e -> {
            if (nombre.getText().isEmpty() || telefono.getText().isEmpty() || transaccion.getText().isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "Debes introducir todos los datos para completar la compra.");
            } else {
                try {
                    int telefonoNum = Integer.parseInt(telefono.getText());
                    int transaccionNum = Integer.parseInt(transaccion.getText());
                    JOptionPane.showMessageDialog(ventana, "¡Compra realizada con éxito!");
                    ventana.dispose();
                } catch (NumberFormatException n) {
                    JOptionPane.showMessageDialog(ventana, "¡Teléfono o transacción inválidos!", "ERROR", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        ventana.setVisible(true);
    }

    private void abrirVentanaEfectivo() {
        JFrame ventana = new JFrame("Pago en Efectivo");
        ventana.setSize(300, 200);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridLayout(3, 2, 10, 10));

        JTextField nombre = new JTextField();
        JComboBox<String> billetes = new JComboBox<>(new String[]{"", "$2000", "$5000", "$10000", "$20000", "$50000"});
        JButton confirmar = new JButton("Confirmar");

        ventana.add(new JLabel("Nombre:"));
        ventana.add(nombre);
        ventana.add(new JLabel("Billete con que pagará:"));
        ventana.add(billetes);
        ventana.add(new JLabel());
        ventana.add(confirmar);

        confirmar.addActionListener(e -> {
            if (nombre.getText().isEmpty() || billetes.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(ventana, "Debes introducir todos los datos para completar la compra.");
            } else {
                String billeteSelec = (String) billetes.getSelectedItem();
                String billete = billeteSelec.substring(1);
                if (Double.parseDouble(billete) < total){
                    String msjInsuficiente = String.join("", "Billete inferior al total ($", String.valueOf(total), ").");
                    JOptionPane.showMessageDialog(ventana, msjInsuficiente);
                } else {
                    double cambio = Double.parseDouble(billete) - total;
                        String msjCambio = String.join("", "Compra exitosa, ¡gracias por elegirnos!\n", "Tu cambio: $", String.valueOf(cambio), ".");
                        JOptionPane.showMessageDialog(ventana, msjCambio);
                        ventana.dispose();
                }
            }
        });

        ventana.setVisible(true);
    }

    private void abrirVentanaTarjeta() {
        JFrame ventana = new JFrame("Pago con Tarjeta");
        ventana.setSize(300, 250);
        ventana.setLocationRelativeTo(null);
        ventana.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField numeroTarjeta = new JTextField();
        JTextField fechaExpedicion = new JTextField();
        JTextField codigoSeguridad = new JTextField();
        JButton confirmar = new JButton("Confirmar");

        ventana.add(new JLabel("Número de Tarjeta:"));
        ventana.add(numeroTarjeta);
        ventana.add(new JLabel("Fecha de Expedición:"));
        ventana.add(fechaExpedicion);
        ventana.add(new JLabel("Código de Seguridad:"));
        ventana.add(codigoSeguridad);
        ventana.add(new JLabel());
        ventana.add(confirmar);

        confirmar.addActionListener(e -> {
            if (numeroTarjeta.getText().isEmpty() ||
                fechaExpedicion.getText().isEmpty() ||
                codigoSeguridad.getText().isEmpty()) {
                JOptionPane.showMessageDialog(ventana, "Debes introducir todos los datos para completar la compra.");
            } else {
                JOptionPane.showMessageDialog(ventana, "Compra exitosa, gracias por elegirnos.");
                ventana.dispose();
            }
        });

        ventana.setVisible(true);
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaPago ventana = new VistaPago();
            ventana.setVisible(true);
        });
    }*/
}
