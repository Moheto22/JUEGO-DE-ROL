package listeners;

import warriors.Soldier;
import warriors.Warrior;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * ActionListener que gestiona el final de la habilidad definitiva del guerrero.
 * Este listener se encarga de desactivar el aura y la invulnerabilidad del guerrero.
 */
public class EndUltiWarrior implements ActionListener {
    private Warrior warrior;   // El guerrero al que se le aplica el final de la habilidad
    private Timer timerAura;   // Temporizador asociado al aura del guerrero

    /**
     * Constructor para EndUltiWarrior.
     *
     * @param warrior  El guerrero al que se le aplica el final de la habilidad.
     * @param timerAura Temporizador asociado al aura del guerrero.
     */
    public EndUltiWarrior(Warrior warrior, Timer timerAura) {
        this.warrior = warrior;   // Inicializa el guerrero
        this.timerAura = timerAura; // Inicializa el temporizador del aura
    }

    /**
     * Acción a realizar cuando se activa el temporizador.
     * Detiene el temporizador del aura, oculta el aura y desactiva la invulnerabilidad del guerrero.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        timerAura.stop(); // Detiene el temporizador del aura
        ((Soldier) warrior).getAura().setVisible(false); // Oculta el aura
        warrior.setInvincible(false); // Desactiva la invulnerabilidad del guerrero
        ((Timer) e.getSource()).stop(); // Detiene el temporizador que activó esta acción
    }
}
