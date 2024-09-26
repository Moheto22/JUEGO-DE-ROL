package listeners;

import tools.Tools;
import warriors.Skeleton;
import warriors.Warrior;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * ActionListener que gestiona el efecto de aura del guerrero.
 * Este aura daña a los esqueletos cercanos al guerrero.
 */
public class AuraWarrior implements ActionListener {
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos en el juego
    private Warrior warrior; // El guerrero que tiene el aura

    /**
     * Constructor para AuraWarrior.
     *
     * @param skeletons Lista de esqueletos en el juego.
     * @param warrior   El guerrero que tiene el efecto de aura.
     */
    public AuraWarrior(ArrayList<Skeleton> skeletons, Warrior warrior) {
        this.skeletons = skeletons; // Inicializa la lista de esqueletos
        this.warrior = warrior;     // Inicializa el guerrero
    }

    /**
     * Acción que se ejecuta en cada tick del temporizador.
     * Verifica si hay esqueletos en el área de efecto del guerrero y les causa daño.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Obtiene los índices de los esqueletos que intersectan con el cuerpo del guerrero
        ArrayList<Integer> enemys = Tools.multiIntersectSkel(warrior.getBody(), skeletons);
        if (!enemys.isEmpty()) { // Si hay esqueletos en el área de efecto
            for (int i = 0; i < enemys.size(); i++) {
                skeletons.get(enemys.get(i)).makeDamage(3); // Aplica daño a cada esqueleto
            }
        }
    }
}
