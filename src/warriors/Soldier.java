package warriors;

import listeners.ActionListenerAnimAttack;
import listeners.ActionListenerVibration;
import listeners.ChargeWarrioEnergy;
import tools.Tools;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


/**
 * @author Mohamed Boutanghach
 * Clase que representa a un soldado en el juego, que extiende la clase Warrior.
 * El soldado tiene atributos para vida, velocidad, mana y gráficos para su ataque
 * en diferentes direcciones, así como un aura especial.
 */
public class Soldier extends Warrior {
    private Icon attackUp, attackLeft, attackRight, attackDown; // Iconos para los ataques en diferentes direcciones.
    private JLabel aura; // JLabel que representa el aura del soldado.

    public Soldier() {
        this.live = 7;
        this.speed = 5;
        this.mana = 0;
        // Carga las imágenes para el movimiento del soldado.
        this.left = new ImageIcon("src/images/warrior/warrior_left.png");
        this.right = new ImageIcon("src/images/warrior/warrior_right.png");
        this.up = new ImageIcon("src/images/warrior/warrior_up.png");
        this.down = new ImageIcon("src/images/warrior/warrior_down.png");
        this.leftMove = new ImageIcon("src/images/warrior/warrior_left.gif");
        this.rightMove = new ImageIcon("src/images/warrior/warrior_right.gif");
        this.upMove = new ImageIcon("src/images/warrior/warrior_up.gif");
        this.downMove = new ImageIcon("src/images/warrior/warrior_down.gif");

        // Genera los iconos para los ataques en cada dirección.
        this.attackDown = new ImageIcon("src/images/warrior/attackWarDown.gif");
        this.attackLeft = new ImageIcon("src/images/warrior/attackWarLeft.gif");
        this.attackRight = new ImageIcon("src/images/warrior/attackWarRight.gif");
        this.attackUp = new ImageIcon("src/images/warrior/attackWarUp.gif");

        // Genera el aura del soldado.
        this.aura = generateAura();
        this.livesIcon = Tools.generateLiveIcon(this.live); // Genera los iconos de vida.
        this.body.setIcon(this.down); // Establece la imagen inicial del cuerpo en la dirección hacia abajo.
    }

    /**
     * Obtiene el JLabel que representa el aura del soldado.
     *
     * @return JLabel que representa el aura.
     */
    public JLabel getAura() {
        return aura; // Retorna el JLabel de aura.
    }

    /**
     * Genera un JLabel que representa el aura del soldado.
     *
     * @return JLabel con el aura.
     */
    private JLabel generateAura() {
        JLabel aura = new JLabel(); // Crea un JLabel para el aura.
        aura.setSize(50, 50); // Establece el tamaño del aura.
        Image img = new ImageIcon("src/images/warrior/auraWarrior.gif").getImage();
        Icon icon = new ImageIcon(
                img.getScaledInstance(aura.getWidth(), aura.getHeight(), Image.SCALE_DEFAULT)
        );
        aura.setIcon(icon); // Establece la imagen del aura.
        aura.setLocation(this.body.getX() - 9, this.body.getY() - 9); // Coloca el aura relativa al soldado.
        aura.setVisible(false); // Inicialmente, el aura no es visible.
        return aura; // Retorna el JLabel del aura.
    }

    /**
     * Método que aplica daño al soldado.
     * Reduce la vida del soldado por uno y actualiza el icono correspondiente en la interfaz.
     */
    @Override
    public void damage() {
        this.live--; // Decrementa la vida del soldado.
        ImageIcon img = new ImageIcon("src/images/heartDead.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(this.livesIcon.get(0).getWidth(), this.livesIcon.get(0).getHeight(), Image.SCALE_SMOOTH)
        );
        this.livesIcon.get(this.live).setIcon(icon); // Actualiza el icono de vida.
    }

    /**
     * Método que permite al soldado atacar a los enemigos.
     * Cambia el icono del soldado según la dirección en la que está atacando
     * y actualiza su posición. También aplica daño a los enemigos si hay intersección.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos (enemigos) en el juego.
     * @param listWalls La lista de paredes para validar movimientos.
     */
    @Override
    public void attack(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        int enemy; // Variable para almacenar el índice del enemigo.
        Timer timerAttack; // Timer para manejar la animación del ataque.
        this.invincible = true; // Establece al soldado como invencible durante el ataque.

        // Verifica la dirección del soldado y actualiza su icono y posición.
        if (this.body.getIcon().equals(this.left) || this.body.getIcon().equals(this.leftMove)) {
            this.body.setIcon(this.attackLeft);
            this.body.setLocation(this.body.getX() - Tools.validateMove(this.body, listWalls, 20, 'a'), this.body.getY());
            timerAttack = new Timer(400, new ActionListenerAnimAttack(this, 'a'));
        } else if (this.body.getIcon().equals(this.right) || this.body.getIcon().equals(this.rightMove)) {
            this.body.setIcon(this.attackRight);
            this.body.setLocation(this.body.getX() + Tools.validateMove(this.body, listWalls, 20, 'd'), this.body.getY());
            timerAttack = new Timer(400, new ActionListenerAnimAttack(this, 'd'));
        } else if (this.body.getIcon().equals(this.down) || this.body.getIcon().equals(this.downMove)) {
            this.body.setIcon(this.attackDown);
            this.body.setLocation(this.body.getX(), this.body.getY() + Tools.validateMove(this.body, listWalls, 20, 's'));
            timerAttack = new Timer(400, new ActionListenerAnimAttack(this, 's'));
        } else {
            this.body.setIcon(this.attackUp);
            this.body.setLocation(this.body.getX(), this.body.getY() - Tools.validateMove(this.body, listWalls, 20, 'w'));
            timerAttack = new Timer(400, new ActionListenerAnimAttack(this, 'w'));
        }

        aura.setLocation(body.getX() - 9, body.getY() - 9); // Actualiza la posición del aura.
        timerAttack.start(); // Inicia el temporizador de animación de ataque.
        enemy = Tools.intersectSkel(this.body, skeletons); // Verifica si hay un enemigo en la zona de ataque.

        if (enemy != -1) {
            skeletons.get(enemy).makeDamage(3); // Aplica daño al enemigo.
        }
    }

    /**
     * Método que activa la habilidad definitiva del soldado.
     * Elimina el mana y genera un efecto de carga de energía.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos (enemigos) en el juego.
     * @param listWalls La lista de paredes para validar movimientos.
     */
    @Override
    public void ulti(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls) {
        deleteAllMana(panelGame); // Elimina el mana del jugador.
        this.invincible = true; // Establece al soldado como invencible durante la habilidad.
        JLabel chargeEnergy = generateLabelEnergy(); // Genera el JLabel de carga de energía.
        panelGame.setFocusable(false); // Desactiva el foco en el panel de juego.
        panelGame.add(chargeEnergy); // Añade el JLabel de carga al panel.
        panelGame.setComponentZOrder(chargeEnergy, 1); // Coloca el JLabel de carga en la parte superior.
        panelGame.repaint(); // Repaint del panel para reflejar cambios.

        // Temporizadores para efectos visuales y carga de energía.
        Timer timerVibration = new Timer(5, new ActionListenerVibration(panelGame));
        Timer timerCharge = new Timer(2500, new ChargeWarrioEnergy(panelGame, chargeEnergy, timerVibration, skeletons, this));
        timerVibration.start(); // Inicia la vibración visual.
        timerCharge.start(); // Inicia la carga de energía.
    }

    /**
     * Genera un JLabel que representa la energía del soldado.
     *
     * @return JLabel que representa la carga de energía.
     */
    private JLabel generateLabelEnergy() {
        JLabel energy=new JLabel();
        energy.setSize(100,50);
        Image img=new ImageIcon("src/images/warrior/energyWar.gif").getImage();
        Icon icon=new ImageIcon(
                img.getScaledInstance(energy.getWidth(),energy.getHeight(),Image.SCALE_DEFAULT)
        );
        energy.setIcon(icon);
        energy.setLocation(this.body.getX()-34,this.body.getY()-10);
        return energy;

    }

}
