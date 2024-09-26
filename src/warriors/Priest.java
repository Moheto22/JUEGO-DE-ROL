package warriors;

import listeners.ActionListenerBombing;
import listeners.ActionListenerTNT;
import listeners.ListenerCooldown;
import tools.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que representa al sacerdote en el juego, que extiende la clase Warrior.
 * El sacerdote tiene habilidades de ataque y puede realizar ataques especiales.
 */
public class Priest extends Warrior {
    private Timer cooldown; // Temporizador para controlar el tiempo de recarga de las habilidades.

    /**
     * Constructor de la clase Priest.
     * Inicializa los atributos del sacerdote, incluyendo vida, velocidad,
     * imágenes para las distintas direcciones y el temporizador de cooldown.
     */
    public Priest() {
        this.live = 5; // Establece el número de vidas del sacerdote.
        this.speed = 5; // Establece la velocidad del sacerdote.
        this.mana = 0; // Inicializa el maná en 0.
        // Inicializa las imágenes para las distintas direcciones del sacerdote.
        this.left = new ImageIcon("src/images/priest/priest_left.png");
        this.right = new ImageIcon("src/images/priest/priest_rightS.png");
        this.down = new ImageIcon("src/images/priest/priest_down.png");
        this.up = new ImageIcon("src/images/priest/priest_up.png");
        this.leftMove = new ImageIcon("src/images/priest/priest_leftM.gif");
        this.rightMove = new ImageIcon("src/images/priest/priest_right.gif");
        this.upMove = new ImageIcon("src/images/priest/priest_upM.gif");
        this.downMove = new ImageIcon("src/images/priest/priest_downM.gif");
        this.livesIcon = Tools.generateLiveIcon(this.live); // Genera los iconos de vida.
        body.setIcon(this.down); // Establece la imagen inicial del cuerpo.
        this.cooldown = new Timer(5000, new ListenerCooldown()); // Inicializa el temporizador de cooldown.
    }

    /**
     * Método que se ejecuta cuando el sacerdote recibe daño.
     * Reduce la vida del sacerdote y actualiza el icono de vida en la interfaz.
     */
    @Override
    public void damage() {
        this.live--; // Reduce la vida del sacerdote.
        // Carga la imagen de corazón muerto.
        ImageIcon img = new ImageIcon("src/images/heartDead.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(this.livesIcon.get(0).getWidth(), this.livesIcon.get(0).getHeight(), Image.SCALE_SMOOTH)
        );
        // Actualiza el icono de vida correspondiente.
        this.livesIcon.get(this.live).setIcon(icon);
    }

    /**
     * Método para realizar un ataque del sacerdote.
     * Genera una bomba y la añade al panel de juego, manejando el cooldown de ataque.
     *
     * @param panelGame  El panel donde se dibuja el juego.
     * @param skeletons  La lista de esqueletos que pueden ser afectados por el ataque.
     * @param listWalls  La lista de paredes presentes en el juego.
     */
    @Override
    public void attack(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        if (!this.cooldown.isRunning()) { // Verifica si el cooldown no está en ejecución.
            JLabel bomb = generateBomb(); // Genera una bomba.
            panelGame.add(bomb); // Añade la bomba al panel de juego.
            panelGame.setComponentZOrder(bomb, 0); // Ajusta la z-order de la bomba.
            panelGame.repaint(); // Repaint del panel para mostrar la bomba.
            Timer timerBomb = new Timer(20, new ActionListenerTNT(panelGame, bomb, this, skeletons)); // Crea un timer para manejar la bomba.
            timerBomb.start(); // Inicia el timer de la bomba.
            this.cooldown = new Timer(1000, new ListenerCooldown()); // Crea un nuevo cooldown para el ataque.
            this.cooldown.start(); // Inicia el cooldown.
        }
    }

    /**
     * Genera una bomba que el sacerdote puede lanzar.
     *
     * @return JLabel que representa la bomba generada.
     */
    private JLabel generateBomb() {
        JLabel bomb = new JLabel(); // Crea un JLabel para la bomba.
        bomb.setSize(25, 25); // Establece el tamaño de la bomba.
        ImageIcon image = new ImageIcon("src/images/priest/bomb.png"); // Carga la imagen de la bomba.
        Icon icon = new ImageIcon(
                image.getImage().getScaledInstance(bomb.getWidth(), bomb.getHeight(), Image.SCALE_SMOOTH)
        );
        bomb.setIcon(icon); // Establece el icono de la bomba.
        bomb.setLocation(this.body.getLocation()); // Establece la ubicación de la bomba.
        return bomb; // Retorna la bomba generada.
    }

    /**
     * Ejecuta la habilidad especial del sacerdote, que implica lanzar una serie de bombardeos.
     *
     * @param panelGame  El panel donde se dibuja el juego.
     * @param skeletons  La lista de esqueletos que pueden ser afectados por el ataque.
     * @param listWalls  La lista de paredes presentes en el juego.
     */
    @Override
    public void ulti(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        deleteAllMana(panelGame); // Elimina todo el maná disponible del sacerdote.
        Timer bombing = new Timer(100, new ActionListenerBombing(panelGame, skeletons)); // Crea un timer para el bombardeo.
        bombing.start(); // Inicia el bombardeo.
    }
}