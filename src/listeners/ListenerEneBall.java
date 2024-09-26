package listeners;

import tools.Tools;
import warriors.Skeleton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el comportamiento de la bola de energía en el juego.
 * Implementa ActionListener para gestionar el movimiento y las colisiones.
 */
public class ListenerEneBall implements ActionListener {
    private ArrayList<JLabel> listWalls; // Lista de paredes en el juego
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos en el juego
    private String direction; // Dirección en la que se mueve la bola de energía
    private JLabel energyBall; // JLabel que representa la bola de energía
    private JPanel panelGame; // Panel del juego donde se dibujan los elementos

    /**
     * Constructor para ListenerEneBall.
     *
     * @param listWalls   Lista de etiquetas que representan las paredes.
     * @param skeletons   Lista de esqueletos en el juego.
     * @param direction   Dirección del movimiento de la bola de energía (d, a, w, s).
     * @param energyBall  JLabel que representa la bola de energía.
     * @param panelGame   JPanel del juego donde se actualizan los elementos.
     */
    public ListenerEneBall(ArrayList<JLabel> listWalls, ArrayList<Skeleton> skeletons, String direction, JLabel energyBall, JPanel panelGame) {
        this.direction = direction;
        this.listWalls = listWalls;
        this.skeletons = skeletons;
        this.energyBall = energyBall;
        this.panelGame = panelGame;
    }

    /**
     * Maneja el evento de acción para mover la bola de energía.
     * Verifica colisiones con paredes y esqueletos, y genera explosiones si es necesario.
     *
     * @param e El evento de acción disparado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Mover la bola de energía en la dirección correspondiente
        switch (direction) {
            case "d": // Movimiento a la derecha
                energyBall.setLocation(energyBall.getX() + 10, energyBall.getY());
                break;
            case "a": // Movimiento a la izquierda
                energyBall.setLocation(energyBall.getX() - 10, energyBall.getY());
                break;
            case "w": // Movimiento hacia arriba
                energyBall.setLocation(energyBall.getX(), energyBall.getY() - 10);
                break;
            case "s": // Movimiento hacia abajo
                energyBall.setLocation(energyBall.getX(), energyBall.getY() + 10);
                break;
        }

        // Verificar colisiones con paredes o esqueletos
        if (intersectWall()) {
            makeExplotion(e); // Crear explosión al colisionar con una pared
        } else {
            int skelInter = Tools.intersectSkel(this.energyBall, skeletons); // Verificar intersección con esqueletos
            if (skelInter != -1) {
                skeletons.get(skelInter).makeDamage(1); // Infligir daño al esqueleto
                makeExplotion(e); // Crear explosión al colisionar con un esqueleto
            }
        }
        panelGame.repaint(); // Repaint del panel del juego
    }

    /**
     * Verifica si la bola de energía intersecta con alguna pared.
     *
     * @return true si hay intersección con una pared, false en caso contrario.
     */
    private boolean intersectWall() {
        Rectangle ball = energyBall.getBounds(); // Obtener límites de la bola
        int i = 0;
        boolean intersect = false;
        // Verificar intersecciones con todas las paredes
        while (i < this.listWalls.size() && !intersect) {
            Rectangle walls = this.listWalls.get(i).getBounds();
            intersect = walls.intersects(ball); // Verificar intersección
            i++;
        }
        return intersect; // Retornar el resultado de la verificación
    }

    /**
     * Genera una explosión cuando la bola de energía colisiona con un objeto.
     *
     * @param e El evento de acción que disparó la explosión.
     */
    private void makeExplotion(ActionEvent e) {
        JLabel explo = new JLabel(); // Crear JLabel para la explosión
        explo.setSize(60, 60); // Establecer el tamaño de la explosión
        Image img = new ImageIcon("src/images/exploFinMagi.gif").getImage(); // Cargar imagen de explosión
        Icon icon = new ImageIcon(
                img.getScaledInstance(explo.getWidth(), explo.getHeight(), Image.SCALE_DEFAULT)
        );
        explo.setIcon(icon); // Establecer el icono de explosión
        panelGame.remove(energyBall); // Eliminar la bola de energía del panel
        explo.setLocation(energyBall.getLocation()); // Colocar la explosión en la ubicación de la bola
        panelGame.add(explo); // Agregar la explosión al panel
        panelGame.setComponentZOrder(explo, 0); // Establecer el orden de los componentes
        panelGame.repaint(); // Repaint del panel del juego
        Timer timerExplo = new Timer(350, new ListenerAnimation(panelGame, explo)); // Temporizador para la animación de explosión
        timerExplo.start(); // Iniciar temporizador
        ((Timer)e.getSource()).stop(); // Detener el temporizador que activó la acción
    }
}
