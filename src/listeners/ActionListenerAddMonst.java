package listeners;

import warriors.Skeleton;
import warriors.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author Mohamed Boutanghach
 * Clase que implementa ActionListener para agregar esqueletos al juego
 * en función de las acciones del guerrero y la lógica del juego.
 */
public class ActionListenerAddMonst implements ActionListener {
    private JPanel panel; // Panel donde se agregan los esqueletos.
    private Warrior warrior; // Guerrrero en juego.
    private ArrayList<JLabel> listWalls; // Lista de paredes.
    private ArrayList<JLabel> listFloor; // Lista de pisos.
    private ArrayList<Skeleton> skeletons; // Lista de esqueletos en juego.
    private ArrayList<JLabel> items; // Lista de ítems en el juego.

    /**
     * Constructor para ActionListenerAddMonst.
     *
     * @param warrior Guerrrero que se está controlando.
     * @param listWalls Lista de etiquetas que representan las paredes.
     * @param listFloor Lista de etiquetas que representan el piso.
     * @param panel Panel donde se van a agregar los esqueletos.
     * @param skeletons Lista de esqueletos que se están generando.
     * @param items Lista de ítems que pueden estar en el juego.
     */
    public ActionListenerAddMonst(Warrior warrior, ArrayList<JLabel> listWalls,
                                  ArrayList<JLabel> listFloor, JPanel panel,
                                  ArrayList<Skeleton> skeletons, ArrayList<JLabel> items) {
        this.warrior = warrior;
        this.listWalls = listWalls;
        this.listFloor = listFloor;
        this.panel = panel;
        this.skeletons = skeletons;
        this.items = items;
    }

    /**
     * Método que se ejecuta al realizar la acción.
     * Se encarga de agregar un nuevo esqueleto en posiciones aleatorias
     * dentro del mapa mientras las condiciones lo permitan.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Detiene el timer si el guerrero no tiene vidas o ha alcanzado 10 esmeraldas.
        if (this.warrior.getLive() <= 0 || this.warrior.getEmeralds() == 10) {
            ((Timer)e.getSource()).stop();
        } else {
            Random random = new Random(); // Generador de números aleatorios.
            Skeleton skeleton = new Skeleton(); // Crea un nuevo esqueleto.
            skeletons.add(skeleton); // Agrega el esqueleto a la lista.

            // Genera una posición aleatoria para el esqueleto dentro del mapa.
            do {
                skeleton.getBody().setLocation(random.nextInt(1921), random.nextInt(1081));
            } while (!isInMap(skeleton.getBody(), listFloor) || !isOutOfWall(skeleton.getBody(), listWalls));

            panel.add(skeleton.getBody()); // Agrega el esqueleto al panel.
            panel.setComponentZOrder(skeleton.getBody(), 0); // Ajusta el orden del componente en el panel.
            panel.repaint(); // Redibuja el panel.
            panel.requestFocusInWindow(); // Solicita foco para el panel.

            // Inicia un timer para mover el esqueleto.
            Timer timer = new Timer(10, new ActionListenerMoveSkel(skeleton, listWalls, warrior, panel, skeletons, items));
            timer.start(); // Comienza el movimiento del esqueleto.
        }
    }

    /**
     * Verifica si el esqueleto se encuentra fuera de las paredes.
     *
     * @param body JLabel que representa el esqueleto.
     * @param listWalls Lista de paredes en el juego.
     * @return true si el esqueleto está fuera de las paredes, false en caso contrario.
     */
    private boolean isOutOfWall(JLabel body, ArrayList<JLabel> listWalls) {
        Rectangle rectBody = body.getBounds(); // Obtiene el área del cuerpo del esqueleto.
        Rectangle bloc; // Variable para almacenar el área de cada pared.
        int i = 0; // Contador para las paredes.
        boolean isOut = true; // Indica si el esqueleto está fuera de las paredes.

        // Verifica intersecciones con cada pared.
        while (i < listWalls.size() && isOut) {
            bloc = listWalls.get(i).getBounds(); // Obtiene el área de la pared.
            if (bloc.intersects(rectBody)) { // Si hay intersección, el esqueleto está dentro de una pared.
                isOut = false;
            } else {
                i++;
            }
        }
        return isOut; // Retorna el resultado de la verificación.
    }

    /**
     * Verifica si el esqueleto está dentro del área del mapa.
     *
     * @param body JLabel que representa el esqueleto.
     * @param listFloor Lista de etiquetas que representan el piso del mapa.
     * @return true si el esqueleto está dentro del mapa, false en caso contrario.
     */
    private boolean isInMap(JLabel body, ArrayList<JLabel> listFloor) {
        Rectangle rectBody = body.getBounds(); // Obtiene el área del cuerpo del esqueleto.
        Rectangle bloc; // Variable para almacenar el área de cada sección del piso.
        int i = 0; // Contador para las secciones del piso.
        boolean isIn = false; // Indica si el esqueleto está dentro del mapa.

        // Verifica intersecciones con cada sección del piso.
        while (i < listFloor.size() && !isIn) {
            bloc = listFloor.get(i).getBounds(); // Obtiene el área de la sección del piso.
            isIn = bloc.intersects(rectBody); // Verifica si hay intersección.
            i++;
        }
        return isIn; // Retorna el resultado de la verificación.
    }
}
