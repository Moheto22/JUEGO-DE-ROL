package listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el enfriamiento de acciones en el juego.
 * Implementa ActionListener para detener el temporizador cuando se activa el evento.
 */
public class ListenerCooldown implements ActionListener {

    /**
     * Método llamado cuando se dispara el evento del temporizador.
     * Detiene el temporizador que activó este evento.
     *
     * @param e El evento de acción que se ha disparado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        ((Timer)e.getSource()).stop(); // Detiene el temporizador que activó la acción
    }
}
