package listeners;

import warriors.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para gestionar la recogida de objetos por parte de un guerrero.
 * Se encarga de aplicar efectos a la salud, mana y recursos del guerrero al recoger un objeto.
 */
public class ActionListenerItem implements ActionListener {
    private Warrior warrior; // El guerrero que recoge el objeto.
    private JLabel item; // El objeto que se puede recoger.
    private JPanel panel; // El panel donde se mostrará el objeto.

    /**
     * Constructor para ActionListenerItem.
     *
     * @param warrior El guerrero que recogerá el objeto.
     * @param item    El objeto que se puede recoger.
     * @param panel   El JPanel donde se mostrará el objeto.
     */
    public ActionListenerItem(Warrior warrior, JLabel item, JPanel panel) {
        this.warrior = warrior;
        this.item = item;
        this.panel = panel;
    }

    /**
     * Método que se ejecuta al activar la acción de recoger un objeto.
     * Comprueba si el guerrero puede recoger el objeto y aplica los efectos correspondientes.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Verifica si el guerrero está muerto o tiene el máximo de esmeraldas
        if (this.warrior.getLive() <= 0 || this.warrior.getEmeralds() >= 10) {
            panel.remove(item); // Elimina el objeto del panel
            ((Timer) e.getSource()).stop(); // Detiene el temporizador
        } else {
            Rectangle warriorBounds = warrior.getBody().getBounds(); // Obtiene los límites del guerrero
            Rectangle itemBounds = item.getBounds(); // Obtiene los límites del objeto
            // Comprueba si el guerrero colisiona con el objeto
            if (itemBounds.intersects(warriorBounds)) {
                // Si el objeto es de tipo "Energy" y el guerrero tiene menos de 10 de mana
                if (item.getName().equals("Energy") && warrior.getMana() < 10) {
                    warrior.addMana(panel); // Aumenta el mana del guerrero
                    panel.remove(item); // Elimina el objeto del panel
                    panel.repaint(); // Redibuja el panel
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador
                }
                // Si el objeto es de tipo "Live" y el guerrero tiene menos vidas que su máximo
                else if (item.getName().equals("Live") && warrior.getLive() < warrior.getLivesIcon().size()) {
                    warrior.addLive(); // Aumenta la vida del guerrero
                    panel.remove(item); // Elimina el objeto del panel
                    panel.repaint(); // Redibuja el panel
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador
                }
                // Si el objeto es de tipo "Emerald"
                else if (item.getName().equals("Emerald")) {
                    warrior.addEmerald(); // Aumenta las esmeraldas del guerrero
                    panel.remove(item); // Elimina el objeto del panel
                    panel.repaint(); // Redibuja el panel
                    ((Timer) e.getSource()).stop(); // Detiene el temporizador
                }
            }
        }
    }
}
