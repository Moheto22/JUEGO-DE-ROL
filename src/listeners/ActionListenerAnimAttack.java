package listeners;

import warriors.Soldier;
import warriors.Warrior;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para manejar la animación de ataque del guerrero.
 * Cambia el ícono del guerrero basado en la dirección del ataque.
 */
public class ActionListenerAnimAttack implements ActionListener {
    private char direction; // Dirección en la que se realiza el ataque ('a', 's', 'w', 'd').
    private Warrior warrior; // El guerrero que está realizando el ataque.

    /**
     * Constructor para ActionListenerAnimAttack.
     *
     * @param warrior Guerrrero que está atacando.
     * @param direction Dirección en la que se realiza el ataque.
     */
    public ActionListenerAnimAttack(Warrior warrior, char direction) {
        this.warrior = warrior; // Inicializa el guerrero.
        this.direction = direction; // Inicializa la dirección del ataque.
    }

    /**
     * Método que se ejecuta al realizar la acción.
     * Cambia el ícono del guerrero basado en la dirección del ataque
     * y detiene el timer que ejecuta la animación.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Cambia el ícono del guerrero basado en la dirección del ataque.
        switch (direction) {
            case 'a': // Izquierda
                this.warrior.getBody().setIcon(this.warrior.getLeft());
                break;
            case 's': // Abajo
                this.warrior.getBody().setIcon(this.warrior.getDown());
                break;
            case 'w': // Arriba
                this.warrior.getBody().setIcon(this.warrior.getUp());
                break;
            case 'd': // Derecha
                this.warrior.getBody().setIcon(this.warrior.getRight());
                break;
        }
        // El guerrero deja de ser invencible después de atacar.
        this.warrior.setInvincible(false);
        // Detiene el timer que controla la animación.
        ((Timer)e.getSource()).stop();
    }
}
