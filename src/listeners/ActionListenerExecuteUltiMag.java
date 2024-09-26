package listeners;

import warriors.Skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para gestionar la ejecución de la habilidad definitiva de un mago.
 * Se encarga de infligir daño a los enemigos y mostrar la animación de explosión.
 */
public class ActionListenerExecuteUltiMag implements ActionListener {
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos que representan enemigos.
    private ArrayList<Integer> enemys; // Índices de los enemigos a los que se les infligirá daño.
    private JPanel panel; // Panel donde se mostrará la explosión.

    /**
     * Constructor para ActionListenerExecuteUltiMag.
     *
     * @param skeletons Lista de esqueletos que representan enemigos.
     * @param enemys    Índices de los enemigos a los que se les infligirá daño.
     * @param panel     JPanel donde se mostrará la explosión.
     */
    public ActionListenerExecuteUltiMag(ArrayList<Skeleton> skeletons, ArrayList<Integer> enemys, JPanel panel) {
        this.skeletons = skeletons;
        this.enemys = enemys;
        this.panel = panel;
    }

    /**
     * Método que se ejecuta al activar la habilidad definitiva del mago.
     * Inflige daño a los enemigos seleccionados y muestra una animación de explosión.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < enemys.size(); i++) {
            // Inflige daño a cada enemigo
            skeletons.get(enemys.get(i)).makeDamage(3);
            JLabel explosion = generateExplosion(); // Genera la animación de explosión
            explosion.setLocation(skeletons.get(enemys.get(i)).getBody().getX() - 19,
                    skeletons.get(enemys.get(i)).getBody().getY() - 29);
            panel.add(explosion); // Agrega la explosión al panel
            panel.setComponentZOrder(explosion, 0); // Asegura que la explosión esté en el frente
            Timer timer = new Timer(500, new ListenerAnimation(panel, explosion)); // Temporizador para la animación
            timer.start(); // Inicia el temporizador
        }
        ((Timer) e.getSource()).stop(); // Detiene el temporizador que invoca esta acción
    }

    /**
     * Genera un JLabel que representa la explosión.
     *
     * @return JLabel que contiene la imagen de explosión.
     */
    private JLabel generateExplosion() {
        JLabel explosion = new JLabel();
        explosion.setSize(80, 100);
        Image img = new ImageIcon("src/images/wizard/expoUltiMagi.gif").getImage(); // Carga la imagen de explosión
        Icon icon = new ImageIcon(
                img.getScaledInstance(explosion.getWidth(), explosion.getHeight(), Image.SCALE_DEFAULT)
        );
        explosion.setIcon(icon); // Establece la imagen de explosión en el JLabel
        return explosion;
    }
}
