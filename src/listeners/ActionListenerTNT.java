package listeners;

import tools.Tools;
import warriors.Skeleton;
import warriors.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * ActionListener que gestiona las interacciones con TNT en el juego.
 */
public class ActionListenerTNT implements ActionListener {
    private JPanel panelGame;       // El panel del juego donde se muestran TNT y otros componentes
    private JLabel tnt;             // El JLabel de TNT que representa el objeto TNT
    private Warrior warrior;        // El objeto Warrior que representa al personaje del jugador
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos en el juego

    /**
     * Constructor para ActionListenerTNT.
     *
     * @param panelGame El panel del juego donde ocurre la acción.
     * @param tnt      El JLabel que representa el objeto TNT.
     * @param warrior   El objeto Warrior que controla el jugador.
     * @param skeletons Lista de esqueletos en el juego.
     */
    public ActionListenerTNT(JPanel panelGame, JLabel tnt, Warrior warrior, ArrayList<Skeleton> skeletons) {
        this.panelGame = panelGame;
        this.tnt = tnt;
        this.warrior = warrior;
        this.skeletons = skeletons;
    }

    /**
     * Maneja el evento de acción cuando se activa el TNT.
     * Comprueba si el guerrero está vivo y si tiene menos de 10 esmeraldas.
     * Si es así, verifica colisiones con esqueletos y desencadena una explosión si es necesario.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Verificar si el guerrero está muerto o ha alcanzado el número máximo de esmeraldas
        if (this.warrior.getLive() <= 0 || this.warrior.getEmeralds() == 10) {
            panelGame.remove(tnt);  // Eliminar el TNT si se cumplen las condiciones
            panelGame.repaint();
            ((Timer) e.getSource()).stop(); // Detener el temporizador
        } else {
            int enemyIndex = Tools.intersectSkel(tnt, skeletons); // Verificar intersección con esqueletos
            if (enemyIndex != -1) {
                panelGame.remove(tnt); // Eliminar TNT si intersecta con un esqueleto
                JLabel explosion = generateExplosion(); // Generar animación de explosión

                // Infligir daño a todos los esqueletos que intersectan
                ArrayList<Integer> enemyIndices = Tools.multiIntersectSkel(explosion, skeletons);
                for (int index : enemyIndices) {
                    skeletons.get(index).makeDamage(3); // Aplicar daño al esqueleto
                }

                // Agregar la explosión al panel y repintar
                panelGame.add(explosion);
                panelGame.setComponentZOrder(explosion, 0);
                panelGame.repaint();

                // Iniciar el temporizador de animación de explosión
                Timer timer = new Timer(700, new ListenerAnimation(panelGame, explosion));
                timer.start();
                ((Timer) e.getSource()).stop(); // Detener el temporizador actual
            }
        }
    }

    /**
     * Genera un JLabel de explosión con tamaño e icono apropiados.
     *
     * @return El JLabel que representa la explosión.
     */
    private JLabel generateExplosion() {
        JLabel explosion = new JLabel();
        explosion.setSize(100, 110); // Establecer tamaño de la explosión
        explosion.setLocation(this.tnt.getX() - 40, this.tnt.getY() - 40); // Centrar explosión alrededor del TNT
        Image img = new ImageIcon("src/images/priest/explosionTNT.gif").getImage(); // Cargar imagen de explosión
        Icon icon = new ImageIcon(
                img.getScaledInstance(explosion.getWidth(), explosion.getHeight(), Image.SCALE_DEFAULT)
        );
        explosion.setIcon(icon); // Establecer el icono de la explosión
        return explosion;
    }
}