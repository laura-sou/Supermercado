package modelo;

import java.io.*; // For file operations
import java.time.LocalDate; // For handling expiration dates
import java.time.format.DateTimeFormatter; // For date formatting
import java.util.ArrayList; // For list operations
import java.util.List; // For using List interface

public class Inventario {
    // Stores all products in memory using ArrayList
    private final List<Producto> productos;
    
    // Formato doe las fechas (ISO format: YYYY-MM-DD)
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Constructor - initializes the empty product list
    public Inventario() {
        // ArrayList chosen for fast random access and iteration
        productos = new ArrayList<>();
    }
    
    /**
     * Adds a single product to inventory
     */
    public void agregarProducto(Producto producto) {
        // Simple addition to the list
        productos.add(producto);
    }

    /**
     * Finds a product by exact name match (case-insensitive)
     */

    public Producto buscarProductoNombre(String nombre) {
        for (Producto p : productos) {
            //IgnoreCase para evitar problemas de mayúsculas
            if (p.getNombre().equalsIgnoreCase(nombre)) {
                return p;
            }
        }
        return null; // No hay coincidencias
    }

    /*
     * Carga productos del CSV al inventario
     */
    public void cargarProductosDesdeCSV(String archivo) {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            //Buffering -> Precargando y guardando una porción de media en el buffer (memoria temporal)
            // Leer línea por linea

            while ((linea = br.readLine()) != null) {
                // Separador por ","
                String[] datos = linea.split(";");
                
                // Validar que hayan 5 campos por línea
                if (datos.length == 6) {
                    // Quita los espacios vacíos
                    String codigo = datos[0].trim();
                    String nombre = datos[1].trim();
                    String categoria = datos[2].trim();
                    double precio = Double.parseDouble(datos[3].trim()); //Convertir a double
                    LocalDate fechaVencimiento = LocalDate.parse(datos[4].trim(), DATE_FORMATTER); //Formatear al formato fecha
                    int cantidad = Integer.parseInt(datos[5].trim());
                    
                    // Create and add new product
                    productos.add(new Producto(codigo, nombre, categoria, precio, fechaVencimiento, cantidad));
                }
            }
        } catch (IOException e) {
            System.err.println("Error cargando inventario: " + e.getMessage());
        }
    }


    //Retorna lista filtrada
    public List<Producto> buscarProductos(String criterio, String valor) {
        //Criterio = argumento por el que se filtra(nombre, categoría...)
        //Valor = lo que el usuario pone o selecciona ("Pan Bimbo", "lácteos", ...)
        List<Producto> resultados = new ArrayList<>(); //Nueva lista con los productos que aplican
        String valorLower = valor.toLowerCase(); //Lo que el usuario insertó -> minúsculas
        
        for (Producto p : productos) {
            // Dependiento el tipo de busqueda (criterio)
            switch (criterio.toLowerCase()) {
                case "nombre" -> {
                    //Chat dice que los String no devuelven null, sino false, así que no le agregué un try catch
                    //A parte, los productos siempre deberían tener un código, nombre :'D
                    if (p.getNombre().toLowerCase().contains(valorLower)) {
                        resultados.add(p);
                    }
                }
                    
                case "codigo" -> {
                    if (p.getCodigo().toLowerCase().contains(valorLower)) {
                        resultados.add(p);
                    }
                }
                    
                case "precio" -> {
                    try {
                        // Find products <= specified price
                        double precio = Double.parseDouble(valor);
                        if (p.getPrecio() <= precio) {
                            resultados.add(p);
                        }
                    } catch (NumberFormatException e) {
                        //Ignoramos si hay error
                    }
                }
                    
                case "categoria" -> {
                    try {
                        if (p.getCategoria().equalsIgnoreCase(valor)){
                            resultados.add(p);
                        }
                    } catch (Exception e) {
                        // Por si no tiene categoría el producto, ignoralo :)
                    }
                }
            
                case "vencimiento" -> {
                    try {
                        // Find products expiring before specified date
                        LocalDate fecha = LocalDate.parse(valor, DATE_FORMATTER);
                        if (p.getfechaVencimiento().isBefore(fecha)) {
                            resultados.add(p);
                        }
                    } catch (Exception e) {
                        //Ignora los errores LOL
                    }
                }
                default -> // Unknown criteria - could throw exception instead
                    System.out.println("Criterio de búsqueda no válido: " + criterio);
            }
        }
        return resultados;
    }

    //Copia de la lista productos (no podemos poner productos como tal porque es una lista privada)
    public List<Producto> getTodosProductos() {
        return new ArrayList<>(productos);
    }

    public boolean eliminarProducto(String codigo) {
        // Uses Java 8+ removeIf with lambda expression
        return productos.removeIf(p -> p.getCodigo().equals(codigo));
    }
}