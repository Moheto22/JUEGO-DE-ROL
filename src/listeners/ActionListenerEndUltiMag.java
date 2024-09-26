package listeners;

import warriors.Warrior;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para gestionar el final de la habilidad definitiva de un guerrero.
 * Se encarga de eliminar el aura visual del guerrero y restablecer su estado.
 */
public class ActionListenerEndUltiMag implements ActionListener {
    private Timer timer; // Temporizador que controla la duración de la habilidad definitiva.
    private JLabel aura; // Aura visual que representa la habilidad activa.
    private JPanel panel; // Panel donde se muestra el aura.
    private Warrior warrior; // Guerrero al que pertenece el aura.

    /**
     * Constructor para ActionListenerEndUltiMag.
     *
     * @param timer   Timer que controla el tiempo de la habilidad definitiva.
     * @param aura    JLabel que representa el aura visual del guerrero.
     * @param panel   JPanel donde se muestra el aura.
     * @param warrior Guerrero asociado a la habilidad definitiva.
     */
    public ActionListenerEndUltiMag(Timer timer, JLabel aura, JPanel panel, Warrior warrior) {
        this.timer = timer;
        this.aura = aura;
        this.panel = panel;
        this.warrior = warrior;
    }

    /**
     * Método que se ejecuta al finalizar la habilidad definitiva.
     * Elimina el aura del panel, restablece el estado del guerrero y detiene el temporizador.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        panel.remove(aura); // Elimina el aura del panel.
        panel.repaint(); // Repaint para actualizar la vista.
        panel.setFocusable(true); // Hace que el panel pueda recibir el foco.
        panel.requestFocusInWindow(); // Solicita el foco al panel.
        this.timer.stop(); // Detiene el temporizador de la habilidad.
        panel.setLocation(0, 0); // Restablece la posición del panel.
        this.warrior.setInvincible(false); // Desactiva la invulnerabilidad del guerrero.
        ((Timer) e.getSource()).stop(); // Detiene el temporizador que invoca este método.
    }
}

