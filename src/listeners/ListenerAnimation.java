package listeners;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja la animación de explosiones o efectos visuales en el juego.
 * Implementa ActionListener para permitir la eliminación de componentes en el panel de juego.
 */
public class ListenerAnimation implements ActionListener {
    private JPanel panelGame; // El panel del juego donde se encuentran los componentes
    private JLabel explo;     // JLabel que representa la explosión o efecto visual

    /**
     * Constructor para ListenerAnimation.
     *
     * @param panelGame El panel del juego donde se manejará la animación.
     * @param explo     El JLabel que representa la explosión o efecto visual.
     */
    public ListenerAnimation(JPanel panelGame, JLabel explo) {
        this.panelGame = panelGame; // Inicializa el panel del juego
        this.explo = explo;         // Inicializa el JLabel de la explosión
    }

    /**
     * Método llamado cuando se dispara el evento del temporizador.
     * Elimina el JLabel de la explosión del panel y actualiza la vista.
     *
     * @param e El evento de acción que se ha disparado.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        this.panelGame.remove(explo); // Elimina el JLabel de la explosión
        panelGame.repaint();           // Actualiza el panel para reflejar los cambios
        ((Timer) e.getSource()).stop(); // Detiene el temporizador que activó este evento
    }
}
