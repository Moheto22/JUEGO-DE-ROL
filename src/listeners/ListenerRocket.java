package listeners;

import tools.Tools;
import warriors.Skeleton;

import javax.swing.*;
import javax.tools.Tool;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el comportamiento de un cohete en el juego.
 * Implementa ActionListener para gestionar el movimiento del cohete y las colisiones con enemigos.
 */
public class ListenerRocket implements ActionListener {
    private JLabel rocket; // JLabel que representa el cohete
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos enemigos
    private JPanel panel; // Panel donde se dibuja el juego

    /**
     * Constructor para ListenerRocket.
     *
     * @param rocket     JLabel que representa el cohete.
     * @param skeletons  Lista de esqueletos enemigos.
     * @param panel      Panel donde se dibuja el juego.
     */
    public ListenerRocket(JLabel rocket, ArrayList<Skeleton> skeletons, JPanel panel) {
        this.rocket = rocket;
        this.skeletons = skeletons;
        this.panel = panel;
    }

    /**
     * Maneja el evento de acción en cada intervalo de tiempo.
     * Mueve el cohete hacia abajo y verifica las colisiones con los enemigos.
     * Si hay una colisión, genera una explosión y aplica daño a los enemigos.
     * Si el cohete sale del área visible, lo elimina.
     *
     * @param e El evento de acción disparado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        rocket.setLocation(rocket.getX(), rocket.getY() + 15); // Mover el cohete hacia abajo
        int enemy = Tools.intersectSkel(rocket, skeletons); // Verificar colisión con enemigos
        if (enemy != -1) {
            JLabel explotion = generateExplotion(); // Generar explosión
            ArrayList<Integer> enemys = Tools.multiIntersectSkel(explotion, skeletons); // Obtener enemigos afectados por la explosión
            for (int i = 0; i < enemys.size(); i++) {
                skeletons.get(enemys.get(i)).makeDamage(3); // Aplicar daño a los enemigos
            }
            panel.remove(rocket); // Eliminar el cohete del panel
            panel.add(explotion); // Agregar la explosión al panel
            panel.setComponentZOrder(explotion, 0); // Colocar la explosión en el fondo
            panel.repaint(); // Repaint del panel
            Timer timer = new Timer(700, new ListenerAnimation(panel, explotion)); // Temporizador para eliminar la explosión
            timer.start();
            ((Timer) e.getSource()).stop(); // Detener el temporizador del cohete
        } else if (rocket.getY() > panel.getHeight() - rocket.getHeight()) {
            panel.remove(rocket); // Eliminar el cohete si sale del panel
            ((Timer) e.getSource()).stop(); // Detener el temporizador del cohete
        }
    }

    /**
     * Genera un JLabel que representa la explosión.
     *
     * @return JLabel que contiene la imagen de la explosión.
     */
    private JLabel generateExplotion() {
        JLabel explo = new JLabel();
        explo.setSize(100, 110); // Establecer tamaño de la explosión
        explo.setLocation(this.rocket.getX() - 50, this.rocket.getY() - 40); // Establecer posición de la explosión
        Image img = new ImageIcon("src/images/priest/explosionTNT.gif").getImage(); // Cargar imagen de explosión
        Icon icon = new ImageIcon(
                img.getScaledInstance(explo.getWidth(), explo.getHeight(), Image.SCALE_DEFAULT) // Escalar imagen
        );
        explo.setIcon(icon); // Establecer icono de la explosión
        return explo; // Retornar el JLabel de la explosión
    }
}