package vista;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import modelo.Inventario;
import modelo.Producto;

public class VistaCatalogo extends JFrame {
    private JTextField txtBusqueda;
    private JComboBox<String> comboBusquedaEspecial;
    private JComboBox<String> comboBusqueda;
    private JTable tablaProductos;
    private Inventario inventario;
    private JPanel panelBusqueda;
    private JPanel panelBotones;
    private JPanel mainPanel;
    private JButton btnBuscar;
    private JButton btnClear;
    private JButton btnAnadir;

    public VistaCatalogo() {
        // Configuración ventana - Use the inherited JFrame (VistaCatalogo IS a JFrame)
        setTitle("Supermercado");
        setSize(800, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    
        // Inicialización inventario
        inventario = new Inventario();
        inventario.cargarProductosDesdeCSV("productos.csv");
    
        // Panel de busqueda
        panelBusqueda = new JPanel(new GridLayout(5, 2, 5, 5));
        
        // Tipo de busqueda
        panelBusqueda.add(new JLabel("Tipo de busqueda"));
        String busqueda[] = {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento"};
        comboBusqueda = new JComboBox<String>(busqueda);
        panelBusqueda.add(comboBusqueda);
        
        txtBusqueda = new JTextField();
        String especial[] = {};
        comboBusquedaEspecial = new JComboBox<>(especial);
        comboBusquedaEspecial.setVisible(false);
    
        comboBusqueda.addActionListener(e -> actualizarCampoBusqueda());
    
        panelBusqueda.add(new JLabel("Busqueda"));
        panelBusqueda.add(txtBusqueda);
        panelBusqueda.add(comboBusquedaEspecial);
    
        // Botones
        panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0)); // Add some padding

        btnBuscar = new JButton("Buscar");
        btnAnadir = new JButton("Añadir a carrito");
        btnClear = new JButton("Limpiar búsqueda");
    
        panelBotones.add(btnBuscar);
        panelBotones.add(btnClear);
        panelBotones.add(btnAnadir);

        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnClear.addActionListener(e -> limpiarBusqueda());
    
        // Tabla
        String[] columnas = {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento", "Cantidad"};
        Object[][] datos = obtenerDatosTabla(inventario.getTodosProductos());
        tablaProductos = new JTable(datos, columnas);
    
        // Add components to the main frame (this)
        add(panelBusqueda, BorderLayout.NORTH);
        add(panelBotones, BorderLayout.CENTER);
        add(new JScrollPane(tablaProductos), BorderLayout.SOUTH);
    
        setVisible(true);
    }


    //Convierte lista de productos a datos para la tabla
    private Object[][] obtenerDatosTabla(List<Producto> productos) {
    Object[][] datos = new Object[productos.size()][6];
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    for (int i = 0; i < productos.size(); i++) {
        Producto p = productos.get(i);
        datos[i][0] = p.getCodigo();
        datos[i][1] = p.getNombre();
        datos[i][2] = p.getCategoria();
        datos[i][3] = String.format("$%.2f", p.getPrecio()); // Formateo
        datos[i][4] = p.getfechaVencimiento().format(dateFormatter); // Conversión fecha
        datos[i][5] = p.getCantidad();
    }
    return datos;
}

    private void actualizarCampoBusqueda() {
        String tipo = (String)comboBusqueda.getSelectedItem();
        boolean esCombo = tipo.equals("Categoría") || tipo.equals("Fecha de vencimiento");

        txtBusqueda.setVisible(!esCombo);
        comboBusquedaEspecial.setVisible(esCombo);

        if (esCombo) {
            DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();

            if (tipo.equals("Categoría")) {
                String[] categorias = {"Congelados", "Frutas y Verduras", "Despensa", "Lácteos", "Carnes", "Snacks y golosinas"};
                for (String categoria : categorias) {
                    model.addElement(categoria);
                }
            }
            if (tipo.equals("Fecha de vencimiento")) {
                String[] rangos = {"7 días", "14 días", "1 mes"};
                for (String rango : rangos) {
                    model.addElement(rango);
                }
            }

            comboBusquedaEspecial.setModel(model);
        }
        panelBusqueda.revalidate();
        panelBusqueda.repaint();
    }


    private void realizarBusqueda(){
        String criterio = (String)comboBusqueda.getSelectedItem();
        String valor;

        if (comboBusquedaEspecial.isVisible()){
            valor = (String)comboBusquedaEspecial.getSelectedItem();
        } else {
            valor = txtBusqueda.getText();
        }

        //Switch lleno de maneras de manejar los diferentes tipos de datos
        switch(criterio) {
            case "Fecha de vencimiento" -> valor = calcularFechaDesdeRango(valor);
        }

        List<Producto> resultados = inventario.buscarProductos(criterio, valor);
        Object[][] datos = obtenerDatosTabla(resultados);
    
        tablaProductos.setModel(new DefaultTableModel(datos, new String[] {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento", "Cantidad"}));

    }

    private String calcularFechaDesdeRango(String rango){ 
        LocalDate hoy = LocalDate.now();
        return switch (rango) {
            case "7 días" -> hoy.plusDays(7).toString();
            case "14 días" -> hoy.plusDays(14).toString();
            case "1 mes" -> hoy.plusMonths(1).toString();
            default -> hoy.toString();
        };
    }

    private void limpiarBusqueda(){
        Object[][] datos = obtenerDatosTabla(inventario.getTodosProductos());
    
        tablaProductos.setModel(new DefaultTableModel(datos, new String[] {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento", "Cantidad"}));
    }

    //Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaCatalogo vista = new VistaCatalogo();
            vista.setVisible(true);
        });
    }
}
