package listeners;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * ActionListener que reduce el tamaño de un JLabel (avatar) con el tiempo.
 */
public class ActionListenerReduceAva implements ActionListener {
    private JLabel label; // El JLabel que se va a redimensionar

    /**
     * Constructor para ActionListenerReduceAva.
     *
     * @param label El JLabel cuyo tamaño se reducirá.
     */
    public ActionListenerReduceAva(JLabel label) {
        this.label = label;
    }

    /**
     * Acción a realizar en cada tick del temporizador.
     * Reduce el tamaño de la etiqueta hasta que sea menor de 400 píxeles de ancho.
     * Actualiza el icono para que coincida con el nuevo tamaño y reposiciona ligeramente la etiqueta.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Comprobar si el ancho de la etiqueta es mayor o igual a 400 píxeles
        if (label.getWidth() >= 400) {
            // Disminuir el ancho y la altura de la etiqueta en 10 píxeles
            label.setSize(label.getWidth() - 10, label.getHeight() - 10);

            // Crear un nuevo ImageIcon a partir del icono actual usando el tamaño actualizado
            ImageIcon imageIcon = new ImageIcon(label.getName());
            Icon icon = new ImageIcon(
                    imageIcon.getImage().getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_AREA_AVERAGING)
            );

            // Establecer el nuevo icono en la etiqueta
            label.setIcon(icon);

            // Ajustar la ubicación de la etiqueta ligeramente para crear un efecto visual
            label.setLocation(label.getX() + 5, label.getY() + 5);
        } else {
            // Detener el temporizador si la etiqueta es más pequeña que 400 píxeles
            ((Timer)e.getSource()).stop();
        }
    }
}
