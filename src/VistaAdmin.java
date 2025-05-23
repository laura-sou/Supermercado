package interfaz;

import java.awt.*;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import modelo.Producto;

public class VistaAdmin extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private final String csvPath = "productos.csv";
    private final List<Producto> productos = new ArrayList<>();
    private JComboBox<String> comboCategoria;

    public VistaAdmin() {
        setTitle("Panel Administrador - Supermercado");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cargarProductos();

        // Panel lateral con botones
        JPanel menuPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton agregarBtn = new JButton("1. Agregar Producto");
        JButton modificarBtn = new JButton("2. Modificar Precio");
        JButton ventasBtn = new JButton("3. Mostrar Ventas");
        JButton pdfBtn = new JButton("4. Generar Informe PDF");

        menuPanel.add(agregarBtn);
        menuPanel.add(modificarBtn);
        menuPanel.add(ventasBtn);
        menuPanel.add(pdfBtn);
        add(menuPanel, BorderLayout.WEST);

        // Panel central con CardLayout
        contentPanel = new JPanel();
        cardLayout = new CardLayout();
        contentPanel.setLayout(cardLayout);

        contentPanel.add(crearPanelAgregar(), "agregar");
        contentPanel.add(crearPanelModificar(), "modificar");
        contentPanel.add(crearPanelVentas(), "ventas");
        contentPanel.add(crearPanelPDF(), "pdf");

        add(contentPanel, BorderLayout.CENTER);

        // Eventos
        agregarBtn.addActionListener(e -> cardLayout.show(contentPanel, "agregar"));
        modificarBtn.addActionListener(e -> cardLayout.show(contentPanel, "modificar"));
        ventasBtn.addActionListener(e -> cardLayout.show(contentPanel, "ventas"));
        pdfBtn.addActionListener(e -> cardLayout.show(contentPanel, "pdf"));
    }

    // ===== PANEL: AGREGAR PRODUCTO =====
    private JPanel crearPanelAgregar() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));

        JPanel panelOpciones = new JPanel(new GridLayout(6, 1, 5, 5));
        //panel.setBorder(BorderFactory.createTitledBorder("Agregar Producto"));

        JTextField codigoField = new JTextField();
        JTextField nombreField = new JTextField();
        String[] categorias = {"Congelados", "Frutas y Verduras", "Despensa", "Lácteos", "Carnes", "Snacks y golosinas"};
        comboCategoria = new JComboBox<>(categorias);
        JTextField precioField = new JTextField();
        JTextField vencimientoField = new JTextField();
        JTextField cantidadField = new JTextField();
        JButton guardarBtn = new JButton("Guardar");


        panelOpciones.add(new JLabel("Código:"));
        panelOpciones.add(codigoField);
        panelOpciones.add(new JLabel("Nombre:"));
        panelOpciones.add(nombreField);
        panelOpciones.add(new JLabel("Categoría:"));
        panelOpciones.add(comboCategoria);
        panelOpciones.add(new JLabel("Precio:"));
        panelOpciones.add(precioField);
        panelOpciones.add(new JLabel("Fecha de vencimiento (dd/MM/yyyy):"));
        panelOpciones.add(vencimientoField);
        panelOpciones.add(new JLabel("Cantidad disponible:"));
        panelOpciones.add(cantidadField);
        panelOpciones.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 5));
        panel.add(panelOpciones, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
        bottomPanel.add(guardarBtn);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Optional padding
        panel.add(bottomPanel, BorderLayout.SOUTH);

        guardarBtn.addActionListener(e -> {
            DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate fechaVencimiento = null;
            
            String codigo = codigoField.getText().trim();
            String nombre = nombreField.getText().trim();
            String categoria = (String) comboCategoria.getSelectedItem();
            String precioStr = precioField.getText().trim();
            String vencimientoStr = vencimientoField.getText().trim();
            String cantidadStr = cantidadField.getText().trim();
            double precio = 0.0;
            int cantidad = 0;
            try {
                fechaVencimiento = LocalDate.parse(vencimientoStr, DATE_FORMATTER);
            } catch (DateTimeParseException d) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
                return;  // Sale del método si la fecha es inválida
            }

            try {
                fechaVencimiento = LocalDate.parse(vencimientoStr, DATE_FORMATTER);
            } catch (Exception f) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto.","", JOptionPane.ERROR_MESSAGE);
            }

            try {
                cantidad = Integer.parseInt(cantidadStr);
                precio = Double.parseDouble(precioStr);
            } catch (NumberFormatException n) {
                JOptionPane.showMessageDialog(this, "Ingresa un número.","", JOptionPane.ERROR_MESSAGE);
            }


            if (!codigo.isEmpty() && !nombre.isEmpty() && !precioStr.isEmpty() && !categoria.isEmpty() && !vencimientoStr.isEmpty() && !cantidadStr.isEmpty()) {
                if(cantidad > 0 && precio > 0.0){
                    productos.add(new Producto(codigo, nombre, categoria, precio, fechaVencimiento, cantidad));
                    guardarProductos();
                    JOptionPane.showMessageDialog(this, "Producto agregado.");
                    nombreField.setText(""); precioField.setText(""); comboCategoria.setSelectedIndex(-1); codigoField.setText(""); cantidadField.setText("");
                }
                else {
                    JOptionPane.showMessageDialog(this, "Añade una cantidad y precio válidos.","", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Completa todos los campos.","", JOptionPane.WARNING_MESSAGE);
            }
        });

        return panel;
    }

    // ===== PANEL: MODIFICAR PRECIO =====
    private JPanel crearPanelModificar() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Modificar Precio"));

        JTextField nombreField = new JTextField();
        JTextField nuevoPrecioField = new JTextField();
        JButton actualizarBtn = new JButton("Actualizar");

        panel.add(new JLabel("Nombre del producto:"));
        panel.add(nombreField);
        panel.add(new JLabel("Nuevo precio:"));
        panel.add(nuevoPrecioField);
        panel.add(new JLabel());
        panel.add(actualizarBtn);

        actualizarBtn.addActionListener(e -> {
            String nombre = nombreField.getText().trim();
            String nuevoPrecio = nuevoPrecioField.getText().trim();
            boolean encontrado = false;

            for (Producto prod : productos) {
                if (prod.getNombre().equalsIgnoreCase(nombre)) {
                    prod.setPrecio(Double.parseDouble(nuevoPrecio));
                    encontrado = true;
                    break;
                }
            }

            if (encontrado) {
                guardarProductos();
                JOptionPane.showMessageDialog(this, "Precio actualizado.");
            } else {
                JOptionPane.showMessageDialog(this, "Producto no encontrado.");
            }
        });

        return panel;
    }

    /* 
    private LocalDate formatoVencimiento(String input){
        DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaVencimiento = null; 

        try {
            return fechaVencimiento = LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Use dd/MM/yyyy", "Error", JOptionPane.ERROR_MESSAGE);
            
        }
    }
        */

    // ===== PANEL: MOSTRAR VENTAS =====
    private JPanel crearPanelVentas() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Ventas"));

        JTextArea ventasArea = new JTextArea("Aquí se mostrarán las ventas (simulado).");
        panel.add(new JScrollPane(ventasArea), BorderLayout.CENTER);

        return panel;
    }

    // ===== PANEL: GENERAR PDF =====
    private JPanel crearPanelPDF() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Generar PDF"));

        JButton generarPDFBtn = new JButton("Generar PDF");
        generarPDFBtn.addActionListener(e -> JOptionPane.showMessageDialog(this, "PDF generado (simulado)."));

        panel.add(generarPDFBtn);
        return panel;
    }

    // ===== PRODUCTOS: CARGAR Y GUARDAR CSV =====
    private void cargarProductos() {
    productos.clear();
    File archivo = new File(csvPath);
    if (!archivo.exists()) return;

    try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
        String linea;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        while ((linea = br.readLine()) != null) {
            String[] datos = linea.split(";");
            
            if (datos.length == 6) { //Si hay 6 datos, actualizar
                try {
                    String codigo = datos[0];
                    String nombre = datos[1];
                    String categoria = datos[2];
                    double precio = Double.parseDouble(datos[3]);
                    LocalDate fechaVencimiento = LocalDate.parse(datos[4], dateFormatter);
                    int cantidad = Integer.parseInt(datos[5]);
                    
                    productos.add(new Producto( //añadir producto
                        codigo,
                        nombre,
                        categoria,
                        precio,
                        fechaVencimiento,
                        cantidad
                    ));
                } catch (NumberFormatException | DateTimeParseException e) {
                    System.err.println("Error parsing line: " + linea);
                    e.printStackTrace();
                }
            } else {
                System.err.println("Invalid CSV line (expected 6 fields): " + linea);
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    private void guardarProductos() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvPath))) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            for (Producto prod : productos) {
                // Convert each field to String and join with ";"
                String line = String.join(";",
                    prod.getCodigo(),            // String
                    prod.getNombre(),            // String
                    prod.getCategoria(),         // String
                    String.valueOf(prod.getPrecio()),  // double → String
                    prod.getfechaVencimiento().format(dateFormatter), // LocalDate → String
                    String.valueOf(prod.getCantidad()) // int → String
                );
                
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //PANEL 3 : VENTAS


    
    //.......................................................
    private static void mostrarLogin() {
        File archivoCredenciales = new File("credenciales.txt");
        if (!archivoCredenciales.exists()) {
            registrarNuevoUsuario(archivoCredenciales);
        }

        while (true) {
            JTextField usuarioField = new JTextField();
            JPasswordField claveField = new JPasswordField();
            Object[] campos = {
                "Usuario:", usuarioField,
                "Contraseña:", claveField
            };

            int opcion = JOptionPane.showConfirmDialog(null, campos, "Login", JOptionPane.OK_CANCEL_OPTION);
            if (opcion != JOptionPane.OK_OPTION) System.exit(0);

            String usuarioIngresado = usuarioField.getText();
            String claveIngresada = new String(claveField.getPassword());

            try (BufferedReader br = new BufferedReader(new FileReader(archivoCredenciales))) {
                String usuario = br.readLine();
                String clave = br.readLine();

                if (usuario.equals(usuarioIngresado) && clave.equals(claveIngresada)) {
                    SwingUtilities.invokeLater(() -> new VistaAdmin().setVisible(true));
                    break;
                } else {
                    JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void registrarNuevoUsuario(File archivoCredenciales) {
        JTextField usuarioField = new JTextField();
        JPasswordField claveField = new JPasswordField();
        Object[] campos = {
            "Nuevo usuario:", usuarioField,
            "Nueva contraseña:", claveField
        };

        int opcion = JOptionPane.showConfirmDialog(null, campos, "Registrar administrador", JOptionPane.OK_CANCEL_OPTION);
        if (opcion == JOptionPane.OK_OPTION) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivoCredenciales))) {
                bw.write(usuarioField.getText());
                bw.newLine();
                bw.write(new String(claveField.getPassword()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.exit(0);
        }
    }

}
