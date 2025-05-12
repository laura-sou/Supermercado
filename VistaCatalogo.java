package vista;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.swing.*;
import modelo.Inventario;
import modelo.Producto;

public class VistaCatalogo extends JFrame {
    private JTextField txtBusqueda;
    private JComboBox<String> comboBusquedaEspecial;
    private JComboBox<String> comboBusqueda;
    private JTable tablaProductos;
    private Inventario inventario;
    private JPanel panelBusqueda;

    public VistaCatalogo() {
        //Configuración ventana
        setTitle("Supermercado");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        //Inicialización inventario
        inventario = new Inventario();
        inventario.cargarProductosDesdeCSV("productos.csv");

        //Panel de busqueda
        panelBusqueda = new JPanel(new GridLayout(5, 2, 5, 5));
        
        //Tipo de busqueda
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

        //Tabla para mostrar productos
        String[] columnas = {"Código", "Nombre", "Categoría", "Precio", "Vencimiento", "Cantidad"};
        Object[][] datos = obtenerDatosTabla(inventario.getTodosProductos());
        tablaProductos = new JTable(datos, columnas);

        //Agregar componentes a la ventana
        add(panelBusqueda, BorderLayout.NORTH); // Add the panel to the frame
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
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


    //Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaCatalogo vista = new VistaCatalogo();
            vista.setVisible(true);
        });
    }
}

