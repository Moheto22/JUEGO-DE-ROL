package listeners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el evento de salida de la aplicación.
 * Extiende MouseAdapter para capturar clics del ratón.
 */
public class MouseListenerExit extends MouseAdapter {

    /**
     * Maneja el evento de clic del ratón.
     * Se ejecuta al hacer clic y cierra la aplicación.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        System.exit(0); // Cierra la aplicación
    }
}
