package warriors;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * @author Mohamed Boutanghach
 * Clase que representa a un esqueleto en el juego, que extiende la clase Entity.
 * El esqueleto tiene atributos para vida, velocidad y gráficos para representar su movimiento.
 */
public class Skeleton extends Entity {

    private JLabel explotionLabel; // JLabel para mostrar la explosión al ser destruido.

    /**
     * Constructor de la clase Skeleton.
     * Inicializa los atributos del esqueleto, incluyendo su vida, velocidad,
     * tamaño y las imágenes para las distintas direcciones de movimiento.
     */
    public Skeleton() {
        explotionLabel = new JLabel(); // Crea el JLabel para la explosión.
        explotionLabel.setSize(70, 70); // Establece el tamaño del JLabel de explosión.
        Random random = new Random(); // Crea un objeto Random para asignar vida aleatoria.
        this.live = random.nextInt(2) + 1; // Asigna vida aleatoria entre 1 y 2.
        this.speed = 3; // Establece la velocidad del esqueleto.
        this.body.setSize(32, 32); // Establece el tamaño del cuerpo del esqueleto.

        // Carga y ajusta las imágenes para el movimiento en diferentes direcciones.
        Image left = new ImageIcon("src/images/skeleton/skeleton_left.gif").getImage();
        this.leftMove = new ImageIcon(
                left.getScaledInstance(this.body.getWidth(), this.body.getHeight(), Image.SCALE_DEFAULT)
        );

        Image right = new ImageIcon("src/images/skeleton/skeleton_right.gif").getImage();
        this.rightMove = new ImageIcon(
                right.getScaledInstance(this.body.getWidth(), this.body.getHeight(), Image.SCALE_DEFAULT)
        );

        Image down = new ImageIcon("src/images/skeleton/skeleton_down.gif").getImage();
        this.downMove = new ImageIcon(
                down.getScaledInstance(this.body.getWidth(), this.body.getHeight(), Image.SCALE_DEFAULT)
        );

        Image up = new ImageIcon("src/images/skeleton/skeleton_up.gif").getImage();
        this.upMove = new ImageIcon(
                up.getScaledInstance(this.body.getWidth(), this.body.getHeight(), Image.SCALE_DEFAULT)
        );

        this.body.setIcon(downMove); // Establece la imagen inicial del cuerpo en la dirección hacia abajo.

        // Carga la imagen de explosión.
        Image explo = new ImageIcon("src/images/skeleton/exploSkel.gif").getImage();
        Icon explotion = new ImageIcon(
                explo.getScaledInstance(explotionLabel.getWidth(), explotionLabel.getHeight(), Image.SCALE_DEFAULT)
        );
        explotionLabel.setIcon(explotion); // Establece la imagen de explosión en el JLabel.
    }

    /**
     * Método para aplicar daño al esqueleto.
     * Reduce la vida del esqueleto por la cantidad de daño recibido.
     *
     * @param damage La cantidad de daño a aplicar.
     */
    public void makeDamage(int damage) {
        this.live -= damage; // Decrementa la vida del esqueleto.
    }

    /**
     * Obtiene el JLabel que representa la explosión del esqueleto.
     *
     * @return JLabel que representa la explosión.
     */
    public JLabel getExplotionLabel() {
        return explotionLabel; // Retorna el JLabel de explosión.
    }
}
