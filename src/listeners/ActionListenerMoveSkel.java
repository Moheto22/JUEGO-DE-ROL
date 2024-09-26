package listeners;

import tools.Tools;
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
 * Clase que implementa ActionListener para mover un esqueleto (Skeleton) en el juego.
 * Se encarga de gestionar el movimiento del esqueleto hacia el guerrero, la lógica de ataque
 * y la eliminación del esqueleto al ser derrotado.
 */
public class ActionListenerMoveSkel implements ActionListener {
    private Skeleton skeleton; // El esqueleto que se moverá.
    private ArrayList<JLabel> listWalls; // Lista de paredes para evitar colisiones.
    private Warrior warrior; // El guerrero objetivo del esqueleto.
    private JPanel panel; // El panel donde se mostrará el esqueleto.
    private ArrayList<Skeleton> skeletons; // Lista de todos los esqueletos en el juego.
    private Timer timerCooldownAttack; // Temporizador para el cooldown de ataque.
    private ArrayList<JLabel> items; // Lista de objetos en el juego.

    /**
     * Constructor para ActionListenerMoveSkel.
     *
     * @param skeleton    El esqueleto que se moverá.
     * @param listWalls   La lista de paredes para comprobar colisiones.
     * @param warrior     El guerrero al que el esqueleto perseguirá.
     * @param panel       El JPanel donde se mostrará el esqueleto.
     * @param skeletons   La lista de todos los esqueletos en el juego.
     * @param items       La lista de objetos en el juego.
     */
    public ActionListenerMoveSkel(Skeleton skeleton, ArrayList<JLabel> listWalls, Warrior warrior, JPanel panel, ArrayList<Skeleton> skeletons, ArrayList<JLabel> items) {
        this.warrior = warrior;
        this.skeleton = skeleton;
        this.listWalls = listWalls;
        this.panel = panel;
        this.skeletons = skeletons;
        this.timerCooldownAttack = new Timer(1000, new ListenerCooldown());
        this.items = items;
    }

    /**
     * Método que se ejecuta al activar la acción de mover el esqueleto.
     * Comprueba la vida del guerrero y del esqueleto, y mueve el esqueleto hacia el guerrero,
     * gestionando ataques y eliminaciones.
     *
     * @param e Evento de acción que ocurre (trigger).
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // Verifica si el guerrero está muerto o si tiene el máximo de esmeraldas
        if (this.warrior.getLive() <= 0 || this.warrior.getEmeralds() == 10) {
            panel.remove(skeleton.getBody()); // Elimina el esqueleto del panel
            skeletons.remove(skeleton); // Elimina el esqueleto de la lista
            ((Timer) e.getSource()).stop(); // Detiene el temporizador
        } else if (skeleton.getLive() > 0) { // Verifica si el esqueleto está vivo
            Point locO = skeleton.getBody().getLocation(); // Guarda la ubicación original del esqueleto
            Rectangle war, ske; // Rectángulos para comprobar colisiones
            int distanceX = skeleton.getBody().getX() - warrior.getBody().getX(); // Distancia en el eje X
            int distanceY = skeleton.getBody().getY() - warrior.getBody().getY(); // Distancia en el eje Y

            // Movimiento basado en la distancia
            if (Math.abs(distanceX) < Math.abs(distanceY)) {
                if (Math.abs(distanceX) > 32) {
                    if (distanceX < 0) {
                        moveRight(); // Mueve a la derecha
                    } else {
                        moveLeft(); // Mueve a la izquierda
                    }
                    if (locO.equals(skeleton.getBody().getLocation())) {
                        if (distanceY < 0) {
                            moveDown(); // Mueve hacia abajo
                        } else {
                            moveUp(); // Mueve hacia arriba
                        }
                    }
                } else {
                    if (distanceY < 0) {
                        moveDown(); // Mueve hacia abajo
                    } else {
                        moveUp(); // Mueve hacia arriba
                    }
                    if (locO.equals(skeleton.getBody().getLocation())) {
                        if (distanceX < 0) {
                            moveRight(); // Mueve a la derecha
                        } else {
                            moveLeft(); // Mueve a la izquierda
                        }
                    }
                }
            } else {
                if (Math.abs(distanceY) > 32) {
                    if (distanceY < 0) {
                        moveDown(); // Mueve hacia abajo
                    } else {
                        moveUp(); // Mueve hacia arriba
                    }
                    if (locO.equals(skeleton.getBody().getLocation())) {
                        if (distanceX < 0) {
                            moveRight(); // Mueve a la derecha
                        } else {
                            moveLeft(); // Mueve a la izquierda
                        }
                    }
                } else {
                    if (distanceX < 0) {
                        moveRight(); // Mueve a la derecha
                    } else {
                        moveLeft(); // Mueve a la izquierda
                    }
                    if (locO.equals(skeleton.getBody().getLocation())) {
                        if (distanceY < 0) {
                            moveDown(); // Mueve hacia abajo
                        } else {
                            moveUp(); // Mueve hacia arriba
                        }
                    }
                }
            }

            // Comprobar si el guerrero colisiona con el esqueleto
            war = warrior.getBody().getBounds();
            ske = skeleton.getBody().getBounds();
            if (war.intersects(ske)) {
                if (!timerCooldownAttack.isRunning() && !warrior.isInvincible()) {
                    warrior.damage(); // Inflige daño al guerrero
                    timerCooldownAttack = new Timer(1000, new ListenerCooldown()); // Reinicia el temporizador de cooldown
                    timerCooldownAttack.start();
                }
            }
        } else { // El esqueleto ha sido derrotado
            panel.remove(skeleton.getBody()); // Elimina el cuerpo del esqueleto
            skeleton.getExplotionLabel().setLocation(skeleton.getBody().getLocation()); // Coloca la explosión en la ubicación del esqueleto
            skeletons.remove(skeleton); // Elimina el esqueleto de la lista
            JLabel death = skeleton.getExplotionLabel(); // Obtiene la etiqueta de explosión
            panel.add(death); // Agrega la explosión al panel
            panel.setComponentZOrder(death, 0); // Coloca la explosión en el fondo
            Timer timer = new Timer(500, new ListenerAnimation(panel, death)); // Temporizador para la animación de explosión
            timer.start();

            // Generación de objetos al azar al morir el esqueleto
            Random random = new Random();
            double drop = random.nextDouble(); // Valor aleatorio para determinar el objeto que se generará
            if (drop > 0.3) {
                JLabel item = generateItem(); // Genera un nuevo objeto
                if (drop < 0.6) {
                    item.setIcon(generateIconEner()); // Establece el icono como energía
                    item.setName("Energy"); // Nombre del objeto
                } else if (drop > 0.6 && drop < 0.9) {
                    item.setIcon(generateIconLive()); // Establece el icono como vida
                    item.setName("Live"); // Nombre del objeto
                } else {
                    item.setIcon(generateIconEmer()); // Establece el icono como esmeralda
                    item.setName("Emerald"); // Nombre del objeto
                }
                panel.add(item); // Agrega el objeto al panel
                panel.setComponentZOrder(item, 0); // Coloca el objeto en el fondo
                Timer timerItem = new Timer(30, new ActionListenerItem(this.warrior, item, panel)); // Temporizador para recoger el objeto
                timerItem.start();
            }
            ((Timer) e.getSource()).stop(); // Detiene el temporizador del esqueleto
        }
    }

    /**
     * Mueve el esqueleto hacia arriba, validando la colisión con paredes.
     */
    private void moveUp() {
        // Actualiza la posición del esqueleto hacia arriba
        skeleton.getBody().setLocation(
                skeleton.getBody().getX(),
                skeleton.getBody().getY() - Tools.validateMove(skeleton.getBody(), listWalls, skeleton.getSpeed(), 'w')
        );
    }

    /**
     * Mueve el esqueleto hacia la derecha, validando la colisión con paredes.
     */
    private void moveRight() {
        // Actualiza la posición del esqueleto hacia la derecha
        skeleton.getBody().setLocation(
                skeleton.getBody().getX() + Tools.validateMove(skeleton.getBody(), listWalls, skeleton.getSpeed(), 'd'),
                skeleton.getBody().getY()
        );
    }

    /**
     * Mueve el esqueleto hacia la izquierda, validando la colisión con paredes.
     */
    private void moveLeft() {
        // Actualiza la posición del esqueleto hacia la izquierda
        skeleton.getBody().setLocation(
                skeleton.getBody().getX() - Tools.validateMove(skeleton.getBody(), listWalls, skeleton.getSpeed(), 'a'),
                skeleton.getBody().getY()
        );
    }

    /**
     * Mueve el esqueleto hacia abajo, validando la colisión con paredes.
     */
    private void moveDown() {
        // Actualiza la posición del esqueleto hacia abajo
        skeleton.getBody().setLocation(
                skeleton.getBody().getX(),
                skeleton.getBody().getY() + Tools.validateMove(skeleton.getBody(), listWalls, skeleton.getSpeed(), 's')
        );
    }

    /**
     * Genera un nuevo objeto (item) que se puede recoger.
     *
     * @return Un JLabel que representa el objeto.
     */
    private JLabel generateItem() {
        JLabel item = new JLabel();
        item.setSize(28, 28); // Establece el tamaño del objeto
        item.setLocation(this.skeleton.getBody().getX() + 10, this.skeleton.getBody().getY() + 10); // Posiciona el objeto
        return item;
    }

    /**
     * Genera el icono para el objeto de vida.
     *
     * @return Un Icon que representa el icono de vida.
     */
    private Icon generateIconLive() {
        Image img = new ImageIcon("src/images/live.gif").getImage(); // Carga la imagen de vida
        Icon icon = new ImageIcon(
                img.getScaledInstance(28, 28, Image.SCALE_DEFAULT) // Escala la imagen
        );
        return icon;
    }

    /**
     * Genera el icono para el objeto de energía.
     *
     * @return Un Icon que representa el icono de energía.
     */
    private Icon generateIconEner() {
        Image img = new ImageIcon("src/images/energyBall.gif").getImage(); // Carga la imagen de energía
        Icon icon = new ImageIcon(
                img.getScaledInstance(28, 28, Image.SCALE_DEFAULT) // Escala la imagen
        );
        return icon;
    }

    /**
     * Genera el icono para el objeto de esmeralda.
     *
     * @return Un Icon que representa el icono de esmeralda.
     */
    private Icon generateIconEmer() {
        Image img = new ImageIcon("src/images/emerald.gif").getImage(); // Carga la imagen de esmeralda
        Icon icon = new ImageIcon(
                img.getScaledInstance(28, 28, Image.SCALE_DEFAULT) // Escala la imagen
        );
        return icon;
    }

}