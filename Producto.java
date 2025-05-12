package modelo;

import java.time.LocalDate;

public class Producto {
    private String codigo;
    private String nombre;
    private String categoria;
    private double precio;
    private int cantidad;
    private LocalDate fechaVencimiento;

    public Producto(String codigo, String nombre, String categoria, double precio, LocalDate fechaVencimiento, int cantidad){
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getCodigo() { return codigo;}
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre;}
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public String getCategoria() { return categoria;}
    public void setCategoria(String categoria) {this.categoria = categoria;}
    
    public int getCantidad() { return cantidad;}
    public void setCantidad(int cantidad) {this.cantidad = cantidad;}

    public LocalDate getfechaVencimiento() {return fechaVencimiento;}

    public void setfechaVencimiento(String fecha) {
        LocalDate nuevaFecha = LocalDate.parse(fecha);
        this.fechaVencimiento = nuevaFecha;
    }

    @Override
    public String toString() {
        return nombre + "," + precio + "," + categoria + "," + cantidad + ", " + fechaVencimiento;
    }

    //Si la fecha actual + límite de días (7) ya ocurrió
    public boolean cercaDeVencer(int limiteDias){
        return LocalDate.now().plusDays(limiteDias).isAfter(fechaVencimiento);
    }

    //Revisa si la fecha actual ya superó la de vencimiento
    public boolean vencido(){
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    //Descuentos
    public double descuento() {
        if (vencido()){
            return 0;
        } else if (cercaDeVencer(7)){
            return precio * 0.7;
        } else { return precio; }
    }
}