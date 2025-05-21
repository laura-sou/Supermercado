import interfaz.VistaLogin;
import javax.swing.SwingUtilities;

public class App {
    //Test main
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            VistaLogin login = new VistaLogin();
            login.setVisible(true);
        });
    }
}
