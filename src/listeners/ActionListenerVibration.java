package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * ActionListener que gestiona la vibración de un JPanel.
 */
public class ActionListenerVibration implements ActionListener {
    private JPanel panel; // El JPanel que será vibrado

    /**
     * Constructor para ActionListenerVibration.
     *
     * @param panel El JPanel que se va a vibrar.
     */
    public ActionListenerVibration(JPanel panel) {
        this.panel = panel;
    }

    /**
     * Acción que se ejecuta en cada tick del temporizador.
     * Cambia la posición del panel para crear un efecto de vibración.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Cambia la ubicación del panel para crear un efecto de vibración
        if (panel.getY() == 0 || panel.getY() == 5) {
            panel.setLocation(0, -5); // Mueve el panel hacia arriba
        } else {
            panel.setLocation(0, 5); // Mueve el panel hacia abajo
        }
    }
}