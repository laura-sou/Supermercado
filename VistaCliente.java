package interfaz;

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

public class VistaCliente extends JFrame {
    private final JTextField txtBusqueda;
    private final JComboBox<String> comboBusquedaEspecial;
    private final JComboBox<String> comboBusqueda;
    private final JTable tablaProductos;
    private final Inventario inventario;
    private final JPanel panelBusqueda;
    private final JPanel panelBotones;
    private final JPanel panelMenu;
    private final JButton btnBuscar;
    private final JButton btnClear;
    private final JButton btnAnadir;

    private final JPanel panelCompras;
    private final JTable tablaCarrito;
    private final DefaultTableModel tablaModeloCarrito;
    private final JLabel labelTotal;
    private final JPanel panelBotonesCompra;
    private final JButton btnEliminarCarrito;
    private final JButton btnPagar;

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public VistaCliente() {
        // Configuración ventana - Use the inherited JFrame (VistaCatalogo IS a JFrame)
        setTitle("Supermercado");
        setSize(1080, 607);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    
        // Inicialización inventario
        inventario = new Inventario();
        inventario.cargarProductosDesdeCSV("productos.csv");
        // Tabla
        String[] columnas = {"Código", "Nombre", "Categoría", "Precio", "Vence en...", "Cantidad"};
        Object[][] datos = obtenerDatosTabla(inventario.getTodosProductos());
        tablaProductos = new JTable(datos, columnas);


        // Panel de busqueda
        panelBusqueda = new JPanel(new GridLayout(5, 2, 5, 5));
        
        // Tipo de busqueda
        panelBusqueda.add(new JLabel("Tipo de busqueda"));
        String busqueda[] = {"Código", "Nombre", "Categoría", "Precio", "Vence en..."};
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

        btnBuscar = new JButton("Buscar");
        btnAnadir = new JButton("Añadir a carrito");
        btnClear = new JButton("Limpiar búsqueda");
    
        panelBotones.add(btnBuscar);
        panelBotones.add(btnClear);
        panelBotones.add(btnAnadir);

        btnBuscar.addActionListener(e -> realizarBusqueda());
        btnClear.addActionListener(e -> limpiarBusqueda());
        btnAnadir.addActionListener(e -> anadirACarrito());

        //Panel donde almacenar todo lo de la izquierda
        panelMenu = new JPanel(new BorderLayout());
        panelMenu.add(panelBusqueda, BorderLayout.NORTH);
        panelMenu.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
        panelMenu.add(panelBotones, BorderLayout.SOUTH);
        add(panelMenu, BorderLayout.CENTER);
    
        setVisible(true);

        /*
        CARRITO DE COMPRAS
        */
        
        //Panel
        panelCompras = new JPanel();
        panelCompras.setLayout((new BoxLayout(panelCompras, BoxLayout.Y_AXIS)));

        //Items comprados
        tablaModeloCarrito = new DefaultTableModel(new String[]{"Código", "Nombre", "Categoría", "Precio unitario", "Vence en...", "Cantidad", "Precio total"}, 0);
        tablaCarrito = new JTable(tablaModeloCarrito);
        panelCompras.add(new JScrollPane(tablaCarrito));

        //Mini panel de texto
        labelTotal = new JLabel("Total: ");
        panelCompras.add(labelTotal);

        //Botones
        panelBotonesCompra = new JPanel(new FlowLayout());

        btnEliminarCarrito = new JButton("Eliminar item");
        btnPagar = new JButton("Finalizar compra");

        btnEliminarCarrito.addActionListener(e -> eliminarItem());

        panelBotones.add(btnEliminarCarrito);
        panelBotones.add(btnPagar);
        panelCompras.add(panelBotonesCompra);
    
        add(panelCompras, BorderLayout.EAST);
    }

    /*
    Convierte lista de productos a datos para la tabla
    */
    private Object[][] obtenerDatosTabla(List<Producto> productos) {
    Object[][] datos = new Object[productos.size()][6];
    //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    for (int i = 0; i < productos.size(); i++) {
        Producto p = productos.get(i);
        datos[i][0] = p.getCodigo();
        datos[i][1] = p.getNombre();
        datos[i][2] = p.getCategoria();
        datos[i][3] = p.getPrecio(); // Formateo
        datos[i][4] = p.getfechaVencimiento().format(dateFormatter); // Conversión fecha
        datos[i][5] = p.getCantidad();
    }
    return datos;
}

    /*
    Modifica el JComboBox que se muestra dependiendo si se busca por F.V. o por categoría
    */
    private void actualizarCampoBusqueda() {
        String tipo = (String)comboBusqueda.getSelectedItem();
        boolean esCombo = tipo.equals("Categoría") || tipo.equals("Vence en...");

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
            if (tipo.equals("Vence en...")) {
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
        }  else {
            valor = txtBusqueda.getText();
        }

        if("Vence en...".equals(criterio)){
            valor = calcularFechaDesdeRango(valor);
        }

        List<Producto> resultados = inventario.buscarProductos(criterio, valor);
        Object[][] datos = obtenerDatosTabla(resultados);
    
        tablaProductos.setModel(new DefaultTableModel(datos, new String[] {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento", "Cantidad"}));

    }

    private String calcularFechaDesdeRango(String rango){ 
        //DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate hoy = LocalDate.now();
        return switch (rango) {
            case "7 días" -> hoy.plusDays(7).format(dateFormatter);
            case "14 días" -> hoy.plusDays(14).format(dateFormatter);
            case "1 mes" -> hoy.plusMonths(1).format(dateFormatter);
            default -> hoy.format(dateFormatter);
        };
    }

    private void limpiarBusqueda(){
        Object[][] datos = obtenerDatosTabla(inventario.getTodosProductos());
    
        tablaProductos.setModel(new DefaultTableModel(datos, new String[] {"Código", "Nombre", "Categoría", "Precio", "Fecha de vencimiento", "Cantidad"}));
    }

    private void anadirACarrito(){
        int row = tablaProductos.getSelectedRow();
        if (row != -1) {
            String input = JOptionPane.showInputDialog("Ingrese la cantidad deseada: ");
            int cantidad = cantidadInput(input, row);
            if(cantidad > 0){
                double dCantidad = cantidad;
                String codigo = tablaProductos.getValueAt(row, 0).toString();
                String nombre = tablaProductos.getValueAt(row, 1).toString();
                String categoria = tablaProductos.getValueAt(row, 2).toString();
                String precio = tablaProductos.getValueAt(row, 3).toString(); 
                String vencimiento = tablaProductos.getValueAt(row, 4).toString(); 
                double precioTotal = Double.parseDouble(precio) * dCantidad;
                tablaModeloCarrito.addRow(new String[]{codigo, nombre, categoria, precio, vencimiento, String.valueOf(cantidad), String.valueOf(precioTotal)});
            }
            else {
            }

            calcularTotal();
        }
    }

    private Integer cantidadInput(String cantidad, int row) {
        int cantidadNum; //Guardaré la versión int de cantidad acá

        try {
            cantidadNum = Integer.parseInt(cantidad);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida", "", JOptionPane.ERROR_MESSAGE);
            return 0;
        }

        if (cantidad == null || cantidad.trim().isEmpty() || Integer.parseInt(cantidad) > (int) tablaProductos.getValueAt(row, 5)) {
            JOptionPane.showMessageDialog(null, "Cantidad inválida", "",JOptionPane.ERROR_MESSAGE);
            return 0;
        }
        
        return cantidadNum;
    }

    
    private void calcularTotal() {
        double suma = 0;
        for(int i = 0; i < tablaModeloCarrito.getRowCount(); i++){
            String precioItem = tablaModeloCarrito.getValueAt(i, 6).toString();
            suma += Double.parseDouble(precioItem);
        }

        labelTotal.setText("Total: $"+ suma);
    }

    private void eliminarItem() {
        int row = tablaCarrito.getSelectedRow();
        tablaModeloCarrito.removeRow(row);
        calcularTotal();
    }
}

