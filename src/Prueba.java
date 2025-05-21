import interfaz.VistaCliente;
import javax.swing.SwingUtilities;

public class Prueba {
    public static void main (String args[]){
        SwingUtilities.invokeLater(() -> {
            VistaCliente vista = new VistaCliente();
            vista.setVisible(true);
            });
    }
}