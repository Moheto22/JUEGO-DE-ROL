package listeners;

import warriors.Skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;


/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para gestionar el lanzamiento de cohetes en el juego.
 * Se encarga de crear y lanzar cohetes en un panel y controlar el tiempo de reutilización.
 */
public class ActionListenerBombing implements ActionListener {
    private JPanel panel; // Panel donde se lanzan los cohetes.
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos a afectar por los cohetes.
    private Timer cooldown; // Temporizador que controla el tiempo de reutilización de los cohetes.

    /**
     * Constructor para ActionListenerBombing.
     *
     * @param panel     JPanel donde se mostrarán los cohetes.
     * @param skeletons ArrayList de esqueletos que pueden ser afectados.
     */
    public ActionListenerBombing(JPanel panel, ArrayList<Skeleton> skeletons) {
        this.panel = panel;
        this.skeletons = skeletons;
        this.cooldown = new Timer(2000, new ListenerCooldown()); // 2000 ms de cooldown.
        this.cooldown.start(); // Inicia el temporizador de cooldown.
    }

    /**
     * Método que se ejecuta al realizar la acción.
     * Lanza un cohete si el cooldown está activo, y lo agrega al panel.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (cooldown.isRunning()) {
            JLabel rocket = generateRocket(); // Genera un nuevo cohete.
            panel.add(rocket); // Agrega el cohete al panel.
            panel.setComponentZOrder(rocket, 0); // Asegura que el cohete esté en el frente.
            Timer timerRocket = new Timer(10, new ListenerRocket(rocket, skeletons, panel)); // Temporizador para el movimiento del cohete.
            timerRocket.start(); // Inicia el movimiento del cohete.
        } else {
            ((Timer) e.getSource()).stop(); // Detiene el temporizador si el cooldown no está activo.
        }
    }

    /**
     * Genera un JLabel que representa un cohete.
     *
     * @return JLabel configurado como un cohete.
     */
    private JLabel generateRocket() {
        Random random = new Random();
        JLabel rocket = new JLabel(); // Crea un nuevo JLabel para el cohete.
        rocket.setSize(20, 60); // Establece el tamaño del cohete.
        Image img = new ImageIcon("src/images/priest/rocketBomb.gif").getImage(); // Carga la imagen del cohete.
        Icon icon = new ImageIcon(
                img.getScaledInstance(rocket.getWidth(), rocket.getHeight(), Image.SCALE_DEFAULT)
        );
        rocket.setIcon(icon); // Establece el icono del cohete.
        rocket.setLocation(116 + random.nextInt(1770), 0); // Establece una ubicación aleatoria para el cohete.
        return rocket; // Devuelve el JLabel configurado.
    }
}