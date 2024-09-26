package warriors;

import listeners.*;
import tools.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que representa a un mago en el juego. Hereda de la clase Warrior e incluye
 * propiedades específicas como energía, iconos de movimiento y habilidades especiales.
 */
public class Magician extends Warrior {
    /** JLabel que representa la energía del ataque especial del mago. */
    private JLabel energyMagUlti;

    /** JLabel que representa la energía del ataque del enemigo. */
    private JLabel energyEneUlti;

    /** Timer que gestiona el tiempo de recarga entre ataques. */
    private Timer cooldown;

    /**
     * Constructor por defecto de la clase Magician. Inicializa las propiedades del mago,
     * incluyendo vida, velocidad, iconos de movimiento y energía.
     */
    public Magician() {
        this.live = 3; // Establece la vida inicial del mago
        this.speed = 7; // Establece la velocidad del mago
        this.mana = 0; // Inicializa el maná
        // Carga los iconos de movimiento
        this.left = new ImageIcon("src/images/wizard/wizard_left.png");
        this.right = new ImageIcon("src/images/wizard/wizard_right.png");
        this.down = new ImageIcon("src/images/wizard/wizard_down.png");
        this.up = new ImageIcon("src/images/wizard/wizard_up.png");
        // Carga los iconos de movimiento para la animación
        this.leftMove = new ImageIcon("src/images/wizard/wizard_leftM.gif");
        this.rightMove = new ImageIcon("src/images/wizard/wizard_rightM.gif");
        this.upMove = new ImageIcon("src/images/wizard/wizard_upM.gif");
        this.downMove = new ImageIcon("src/images/wizard/wizard_downM.gif");
        // Genera el icono de vida
        this.livesIcon = Tools.generateLiveIcon(this.live);
        this.body.setIcon(this.down); // Establece la imagen inicial del cuerpo
        // Inicializa el JLabel de energía del ataque especial
        this.energyMagUlti = new JLabel();
        this.energyMagUlti.setSize(48, 48);
        ImageIcon img1 = new ImageIcon("src/images/energyMagi.gif");
        Icon icon1 = new ImageIcon(
                img1.getImage().getScaledInstance(energyMagUlti.getWidth(), energyMagUlti.getHeight(), Image.SCALE_SMOOTH)
        );
        this.energyMagUlti.setIcon(icon1); // Establece el icono de energía del ataque especial

        // Inicializa el JLabel de energía del enemigo
        this.energyEneUlti = new JLabel();
        this.energyEneUlti.setSize(52, 65);
        ImageIcon img2 = new ImageIcon("src/images/expoUltiMagi.gif");
        Icon icon2 = new ImageIcon(
                img2.getImage().getScaledInstance(energyEneUlti.getWidth(), energyEneUlti.getHeight(), Image.SCALE_SMOOTH)
        );
        this.energyEneUlti.setIcon(icon2); // Establece el icono de energía del enemigo

        // Inicializa el timer de cooldown para las habilidades
        this.cooldown = new Timer(500, new ListenerCooldown());
    }

    /**
     * Método que aplica daño al mago, decrementando su vida y actualizando el icono de vida.
     */
    @Override
    public void damage() {
        this.live--;
        ImageIcon img = new ImageIcon("src/images/heartDead.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(this.livesIcon.get(0).getWidth(), this.livesIcon.get(0).getHeight(), Image.SCALE_SMOOTH)
        );
        this.livesIcon.get(this.live).setIcon(icon); // Actualiza el icono de vida
    }

    /**
     * Método que permite al mago atacar. Genera una bola de energía en función de la dirección
     * en la que se encuentra el mago y maneja el cooldown entre ataques.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos en el juego.
     * @param listWalls La lista de paredes en el juego.
     */
    @Override
    public void attack(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        JLabel energyBall; // JLabel que representa la bola de energía
        Timer timerEneBall; // Timer para controlar el movimiento de la bola de energía

        if (!this.cooldown.isRunning()) {
            // Determina la dirección del ataque según el icono del cuerpo
            if (this.body.getIcon().equals(this.left) || this.body.getIcon().equals(this.leftMove)) {
                energyBall = generateEnergyBallH("a"); // Genera bola de energía horizontal hacia la izquierda
                energyBall.setLocation(this.body.getX() - energyBall.getWidth(), this.body.getY() + 4);
                timerEneBall = new Timer(10, new ListenerEneBall(listWalls, skeletons, "a", energyBall, panelGame));
            } else if (this.body.getIcon().equals(this.right) || this.body.getIcon().equals(this.rightMove)) {
                energyBall = generateEnergyBallH("d"); // Genera bola de energía horizontal hacia la derecha
                energyBall.setLocation(this.body.getX() + this.body.getWidth(), this.body.getY() + 4);
                timerEneBall = new Timer(10, new ListenerEneBall(listWalls, skeletons, "d", energyBall, panelGame));
            } else if (this.body.getIcon().equals(this.down) || this.body.getIcon().equals(this.downMove)) {
                energyBall = generateEnergyBallV("s"); // Genera bola de energía vertical hacia abajo
                energyBall.setLocation(this.body.getX() + 4, this.body.getY() + this.body.getHeight());
                timerEneBall = new Timer(10, new ListenerEneBall(listWalls, skeletons, "s", energyBall, panelGame));
            } else {
                energyBall = generateEnergyBallV("w"); // Genera bola de energía vertical hacia arriba
                energyBall.setLocation(this.body.getX() + 4, this.body.getY() - this.body.getHeight());
                timerEneBall = new Timer(10, new ListenerEneBall(listWalls, skeletons, "w", energyBall, panelGame));
            }
            cooldown = new Timer(500, new ListenerCooldown());
            cooldown.start();
            panelGame.add(energyBall);
            panelGame.setComponentZOrder(energyBall, 0);
            timerEneBall.start();
        }
    }

    /**
     * Genera una bola de energía que se mueve en dirección vertical.
     *
     * @param direction La dirección en la que se generará la bola de energía.
     *                  "s" para abajo, cualquier otro valor generará hacia arriba.
     * @return JLabel que representa la bola de energía generada.
     */
    private JLabel generateEnergyBallV(String direction) {
        JLabel ball = new JLabel(); // Crea un JLabel para la bola de energía.
        Image img; // Variable para almacenar la imagen.
        Icon icon; // Variable para almacenar el icono escalado.
        ball.setSize(20, 25); // Establece el tamaño de la bola de energía.

        // Carga la imagen correspondiente según la dirección.
        if (direction.equals("s")) {
            img = new ImageIcon("src/images/atackMagiDown.gif").getImage();
            icon = new ImageIcon(
                    img.getScaledInstance(ball.getWidth(), ball.getHeight(), Image.SCALE_DEFAULT)
            );
            ball.setIcon(icon); // Establece el icono de la bola de energía hacia abajo.
        } else {
            img = new ImageIcon("src/images/atackMagiUp.gif").getImage();
            icon = new ImageIcon(
                    img.getScaledInstance(ball.getWidth(), ball.getHeight(), Image.SCALE_DEFAULT)
            );
            ball.setIcon(icon); // Establece el icono de la bola de energía hacia arriba.
        }
        return ball; // Retorna la bola de energía generada.
    }

    /**
     * Genera una bola de energía que se mueve en dirección horizontal.
     *
     * @param direction La dirección en la que se generará la bola de energía.
     *                  "d" para derecha, "a" para izquierda.
     * @return JLabel que representa la bola de energía generada.
     */
    private JLabel generateEnergyBallH(String direction) {
        JLabel ball = new JLabel(); // Crea un JLabel para la bola de energía.
        Image img; // Variable para almacenar la imagen.
        Icon icon; // Variable para almacenar el icono escalado.
        ball.setSize(25, 20); // Establece el tamaño de la bola de energía.

        // Carga la imagen correspondiente según la dirección.
        if (direction.equals("d")) {
            img = new ImageIcon("src/images/atackMagiRight.gif").getImage();
            icon = new ImageIcon(
                    img.getScaledInstance(ball.getWidth(), ball.getHeight(), Image.SCALE_DEFAULT)
            );
            ball.setIcon(icon); // Establece el icono de la bola de energía hacia la derecha.
        } else {
            img = new ImageIcon("src/images/atackMagiLeft.gif").getImage();
            icon = new ImageIcon(
                    img.getScaledInstance(ball.getWidth(), ball.getHeight(), Image.SCALE_DEFAULT)
            );
            ball.setIcon(icon); // Establece el icono de la bola de energía hacia la izquierda.
        }
        return ball; // Retorna la bola de energía generada.
    }

    /**
     * Ejecuta la habilidad especial del mago, creando un área de impacto y afectando a los enemigos.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos que pueden ser afectados.
     * @param listWalls La lista de paredes presentes en el juego.
     */
    @Override
    public void ulti(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        deleteAllMana(panelGame);
        this.invincible = true; // Habilita la invulnerabilidad del mago durante la habilidad.
        JLabel chargeEnergy = generateLabelEnergy();
        panelGame.add(chargeEnergy);
        panelGame.setComponentZOrder(chargeEnergy, 0);
        panelGame.setComponentZOrder(this.body, 0);
        panelGame.repaint();

        panelGame.setFocusable(false); // Desactiva el foco del panel para evitar interrupciones.
        Timer timerVibration = new Timer(5, new ActionListenerVibration(panelGame)); // Timer para vibración.
        timerVibration.start(); // Inicia el timer de vibración.

        JLabel impactArea = new JLabel();
        impactArea.setSize(800, 800);
        impactArea.setLocation(this.body.getX() - 384, this.body.getY() - 384);

        // Intersecta el área de impacto con los esqueletos para determinar los enemigos afectados.
        ArrayList<Integer> enemys = Tools.multiIntersectSkel(impactArea, skeletons);
        Timer execute = new Timer(1500, new ActionListenerExecuteUltiMag(skeletons, enemys, panelGame));
        execute.start();

        Timer endVibration = new Timer(3000, new ActionListenerEndUltiMag(timerVibration, chargeEnergy, panelGame, this)); // Timer para terminar la habilidad.
        endVibration.start(); // Inicia el timer que finaliza la habilidad.
    }

    /**
     * Genera el JLabel que representa la carga de energía del mago.
     *
     * @return JLabel que representa el icono de energía.
     */
    private JLabel generateLabelEnergy() {
        JLabel energy = new JLabel(); // Crea un JLabel para la energía.
        energy.setSize(90, 100); // Establece el tamaño del JLabel de energía.
        Image img = new ImageIcon("src/images/wizard/energyMag.gif").getImage(); // Carga la imagen de energía.
        Icon icon = new ImageIcon(
                img.getScaledInstance(energy.getWidth(), energy.getHeight(), Image.SCALE_DEFAULT)
        );
        energy.setIcon(icon); // Establece el icono de la energía.
        energy.setLocation(this.body.getX() - 27, this.body.getY() - 40); // Establece la ubicación del JLabel.
        return energy; // Retorna el JLabel de energía generado.
    }
}