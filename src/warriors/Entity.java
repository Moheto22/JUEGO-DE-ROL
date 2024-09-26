package warriors;

import javax.swing.*;

/**
 * @author Mohamed Boutanghach
 *
 * Clase abstracta que representa una entidad genérica dentro del juego, con atributos
 * comunes como su movimiento (iconos en diferentes direcciones), su cuerpo visual (JLabel),
 * su cantidad de vida y su velocidad.
 */
public abstract class Entity {
    /** Icono que representa el movimiento hacia la derecha de la entidad. */
    protected Icon rightMove;

    /** Icono que representa el movimiento hacia la izquierda de la entidad. */
    protected Icon leftMove;

    /** Icono que representa el movimiento hacia abajo de la entidad. */
    protected Icon downMove;

    /** Icono que representa el movimiento hacia arriba de la entidad. */
    protected Icon upMove;

    /** JLabel que representa el cuerpo visual de la entidad dentro de la interfaz gráfica. */
    protected JLabel body;

    /** Entero que representa los puntos de vida de la entidad. */
    protected int live;

    /** Entero que representa la velocidad de la entidad en el juego. */
    protected int speed;

    /**
     * Constructor por defecto de la clase Entity. Inicializa el cuerpo de la entidad como un JLabel vacío.
     */
    public Entity() {
        this.body = new JLabel();
    }

    /**
     * Obtiene el icono que representa el movimiento hacia la derecha de la entidad.
     *
     * @return El icono del movimiento hacia la derecha.
     */
    public Icon getRightMove() {
        return rightMove;
    }

    /**
     * Obtiene el icono que representa el movimiento hacia la izquierda de la entidad.
     *
     * @return El icono del movimiento hacia la izquierda.
     */
    public Icon getLeftMove() {
        return leftMove;
    }

    /**
     * Obtiene el icono que representa el movimiento hacia abajo de la entidad.
     *
     * @return El icono del movimiento hacia abajo.
     */
    public Icon getDownMove() {
        return downMove;
    }

    /**
     * Obtiene el icono que representa el movimiento hacia arriba de la entidad.
     *
     * @return El icono del movimiento hacia arriba.
     */
    public Icon getUpMove() {
        return upMove;
    }

    /**
     * Obtiene el JLabel que representa el cuerpo visual de la entidad.
     *
     * @return Un JLabel que contiene la representación gráfica de la entidad.
     */
    public JLabel getBody() {
        return body;
    }

    /**
     * Obtiene la cantidad de vida de la entidad.
     *
     * @return Un entero que representa los puntos de vida de la entidad.
     */
    public int getLive() {
        return live;
    }

    /**
     * Obtiene la velocidad de la entidad.
     *
     * @return Un entero que representa la velocidad de la entidad.
     */
    public int getSpeed() {
        return speed;
    }
}
