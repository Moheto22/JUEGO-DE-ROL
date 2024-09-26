package warriors;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase abstracta que representa un guerrero en el juego.
 * Extiende la clase Entity y contiene atributos y métodos
 * comunes a todos los guerreros.
 */
public abstract class Warrior extends Entity {
    protected int emeralds; // Cantidad de esmeraldas que tiene el guerrero.
    protected JLabel quanty; // JLabel que muestra la cantidad de esmeraldas.
    protected ArrayList<JLabel> livesIcon; // Lista de iconos que representan la vida del guerrero.
    protected boolean invincible; // Estado de invulnerabilidad del guerrero.
    protected Icon left; // Icono para movimiento hacia la izquierda.
    protected Icon right; // Icono para movimiento hacia la derecha.
    protected int mana; // Cantidad de mana del guerrero.
    protected Icon down; // Icono para movimiento hacia abajo.
    protected Icon up; // Icono para movimiento hacia arriba.
    protected ArrayList<JLabel> labelEnergy; // Lista de etiquetas que representan la energía del guerrero.

    /**
     * Constructor de la clase Warrior.
     * Inicializa los atributos del guerrero y establece su posición inicial.
     */
    public Warrior() {
        super(); // Llama al constructor de la clase base (Entity).
        this.mana = 0; // Inicializa el mana a cero.
        this.body.setSize(32, 32); // Establece el tamaño del cuerpo del guerrero.
        this.body.setLocation(164, 214); // Establece la ubicación inicial del guerrero.
        this.invincible = false; // Inicialmente el guerrero no es invencible.
        labelEnergy = new ArrayList<>(); // Inicializa la lista de etiquetas de energía.
        emeralds = 0; // Inicializa la cantidad de esmeraldas a cero.

        // Inicializa la JLabel que muestra la cantidad de esmeraldas.
        quanty = new JLabel();
        quanty.setSize(70, 100);
        quanty.setText(this.emeralds + " x");
        quanty.setForeground(Color.white);
        quanty.setFont((new Font("Impact", Font.ITALIC, 40)));
        quanty.setLocation(1690, 40); // Establece la posición de la JLabel.
    }

    /**
     * Obtiene el JLabel que muestra la cantidad de esmeraldas.
     *
     * @return JLabel que representa la cantidad de esmeraldas.
     */
    public JLabel getQuanty() {
        return quanty; // Retorna el JLabel de esmeraldas.
    }

    /**
     * Obtiene la lista de etiquetas de energía del guerrero.
     *
     * @return ArrayList de JLabels que representan la energía.
     */
    public ArrayList<JLabel> getLabelEnergy() {
        return labelEnergy; // Retorna la lista de etiquetas de energía.
    }

    /**
     * Método abstracto que aplica daño al guerrero.
     * Debe ser implementado por las clases que extienden Warrior.
     */
    public abstract void damage();

    /**
     * Método abstracto que permite al guerrero atacar.
     * Debe ser implementado por las clases que extienden Warrior.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos (enemigos) en el juego.
     * @param listWalls La lista de paredes para validar movimientos.
     */
    public abstract void attack(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls);

    /**
     * Método abstracto que activa la habilidad definitiva del guerrero.
     * Debe ser implementado por las clases que extienden Warrior.
     *
     * @param panelGame El panel donde se dibuja el juego.
     * @param skeletons La lista de esqueletos (enemigos) en el juego.
     * @param listWalls La lista de paredes para validar movimientos.
     */
    public abstract void ulti(JPanel panelGame, ArrayList<Skeleton> skeletons, ArrayList<JLabel> listWalls);

    /**
     * Incrementa la cantidad de esmeraldas del guerrero.
     */
    public void addEmerald() {
        this.emeralds++; // Incrementa el contador de esmeraldas.
        this.quanty.setText(this.emeralds + " x"); // Actualiza el texto en la JLabel de esmeraldas.
    }

    /**
     * Incrementa la vida del guerrero y actualiza el icono correspondiente.
     */
    public void addLive() {
        ImageIcon img = new ImageIcon("src/images/heart.png");
        Icon icon = new ImageIcon(
                img.getImage().getScaledInstance(this.livesIcon.get(0).getWidth(), this.livesIcon.get(0).getHeight(), Image.SCALE_SMOOTH)
        );
        this.getLivesIcon().get(this.live).setIcon(icon); // Actualiza el icono de vida.
        this.live++; // Incrementa la vida del guerrero.
    }

    /**
     * Incrementa la cantidad de mana del guerrero y lo muestra en el panel del juego.
     *
     * @param panelGame El panel donde se dibuja el juego.
     */
    public void addMana(JPanel panelGame) {
        JLabel mana = generateMana(); // Genera una nueva JLabel para el mana.
        mana.setLocation(128 + 4 * this.mana + mana.getWidth() * this.mana, 83); // Establece la posición de la JLabel de mana.
        panelGame.add(mana); // Añade la JLabel de mana al panel de juego.
        panelGame.setComponentZOrder(mana, 0); // Coloca el JLabel de mana en el fondo.
        panelGame.repaint(); // Repaint del panel para reflejar cambios.
        this.labelEnergy.add(mana); // Añade la JLabel de mana a la lista de energía.
        this.mana++; // Incrementa la cantidad de mana.
    }

    /**
     * Elimina todas las etiquetas de mana del panel de juego y reinicia el contador de mana.
     *
     * @param panel El panel donde se dibuja el juego.
     */
    protected void deleteAllMana(JPanel panel) {
        for (int i = 0; i < this.labelEnergy.size(); i++) {
            panel.remove(this.labelEnergy.get(i)); // Elimina cada JLabel de mana del panel.
        }
        this.mana = 0; // Reinicia la cantidad de mana a cero.
    }

    /**
     * Genera un JLabel que representa una etiqueta de mana.
     *
     * @return JLabel que representa la etiqueta de mana.
     */
    protected JLabel generateMana() {
        JLabel mana = new JLabel(); // Crea un nuevo JLabel para el mana.
        mana.setSize(46, 46); // Establece el tamaño del JLabel de mana.
        Image img = new ImageIcon("src/images/energy.gif").getImage();
        Icon icon = new ImageIcon(
                img.getScaledInstance(mana.getWidth(), mana.getHeight(), Image.SCALE_DEFAULT)
        );
        mana.setIcon(icon); // Establece el icono de mana en el JLabel.
        return mana; // Retorna el JLabel de mana.
    }

    /**
     * Verifica si el guerrero es invencible.
     *
     * @return true si el guerrero es invencible, false de lo contrario.
     */
    public boolean isInvincible() {
        return invincible; // Retorna el estado de invulnerabilidad.
    }

    /**
     * Establece el estado de invulnerabilidad del guerrero.
     *
     * @param invincible true para hacer al guerrero invencible, false de lo contrario.
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible; // Establece el estado de invulnerabilidad.
    }

    /**
     * Obtiene la cantidad de esmeraldas del guerrero.
     *
     * @return la cantidad de esmeraldas.
     */
    public int getEmeralds() {
        return emeralds; // Retorna la cantidad de esmeraldas.
    }

    /**
     * Obtiene la lista de iconos que representan la vida del guerrero.
     *
     * @return ArrayList de JLabels que representan la vida.
     */
    public ArrayList<JLabel> getLivesIcon() {
        return livesIcon; // Retorna la lista de iconos de vida.
    }

    /**
     * Obtiene la cantidad de mana del guerrero.
     *
     * @return la cantidad de mana.
     */
    public int getMana() {
        return mana; // Retorna la cantidad de mana.
    }

    /**
     * Obtiene el icono de movimiento hacia la derecha.
     *
     * @return Icono para movimiento hacia la derecha.
     */
    public Icon getRight() {
        return right; // Retorna el icono de movimiento a la derecha.
    }

    /**
     * Obtiene el icono de movimiento hacia arriba.
     *
     * @return Icono para movimiento hacia arriba.
     */
    public Icon getUp() {
        return up; // Retorna el icono de movimiento hacia arriba.
    }

    /**
     * Obtiene el icono de movimiento hacia la izquierda.
     *
     * @return Icono para movimiento hacia la izquierda.
     */
    public Icon getLeft() {
        return left;
    }
    /**
     * Obtiene el icono de movimiento hacia abajo.
     *
     * @return Icono para movimiento hacia abajo.
     */
    public Icon getDown() {
        return down;
    }

}
