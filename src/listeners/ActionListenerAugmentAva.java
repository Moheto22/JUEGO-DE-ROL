package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para aumentar el tamaño de un JLabel.
 * Utiliza una animación para incrementar el tamaño del JLabel hasta un límite máximo.
 */
public class ActionListenerAugmentAva implements ActionListener {
    private JLabel label; // JLabel que se va a aumentar.

    /**
     * Constructor para ActionListenerAugmentAva.
     *
     * @param label JLabel que se desea aumentar.
     */
    public ActionListenerAugmentAva(JLabel label) {
        this.label = label; // Inicializa el JLabel.
    }

    /**
     * Método que se ejecuta al realizar la acción.
     * Aumenta el tamaño del JLabel y ajusta su icono,
     * deteniendo el timer una vez que alcanza un tamaño máximo.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Comprueba si el ancho del JLabel es menor que 550.
        if (label.getWidth() < 550) {
            // Aumenta el tamaño del JLabel.
            label.setSize(label.getWidth() + 10, label.getHeight() + 10);
            // Carga la imagen del JLabel y ajusta su tamaño.
            ImageIcon imageIcon = new ImageIcon(label.getName());
            Icon icon = new ImageIcon(
                    imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_AREA_AVERAGING)
            );
            label.setIcon(icon); // Establece el icono ajustado en el JLabel.
            // Ajusta la ubicación del JLabel para centrarlo visualmente.
            label.setLocation(label.getX() - 5, label.getY() - 5);
        } else {
            // Detiene el timer cuando el JLabel alcanza el tamaño máximo.
            ((Timer) e.getSource()).stop();
        }
    }
}
