package listeners;

import tools.Tools;
import warriors.Skeleton;
import warriors.Soldier;
import warriors.Warrior;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja los eventos de teclado en el juego.
 * Este listener permite controlar el movimiento y las acciones del guerrero.
 */
public class KeyListenerGame extends KeyAdapter {
    private Warrior warrior;                // El guerrero que el jugador controla
    private ArrayList<JLabel> listWalls;    // Lista de paredes en el juego
    private ArrayList<Skeleton> skeletons;   // Lista de esqueletos en el juego
    private JPanel panelGame;                // Panel del juego donde se dibujan los componentes

    /**
     * Constructor para KeyListenerGame.
     *
     * @param warrior   El guerrero que el jugador controla.
     * @param listWalls Lista de paredes en el juego.
     * @param skeletons Lista de esqueletos en el juego.
     * @param panelGame El panel donde se dibujan los componentes del juego.
     * @param items     Lista de ítems en el juego.
     */
    public KeyListenerGame(Warrior warrior, ArrayList<JLabel> listWalls, ArrayList<Skeleton> skeletons, JPanel panelGame, ArrayList<JLabel> items) {
        this.warrior = warrior;            // Inicializa el guerrero
        this.listWalls = listWalls;        // Inicializa la lista de paredes
        this.skeletons = skeletons;        // Inicializa la lista de esqueletos
        this.panelGame = panelGame;        // Inicializa el panel del juego
    }

    /**
     * Método llamado cuando se presiona una tecla.
     * Maneja el movimiento y las acciones del guerrero según la tecla presionada.
     *
     * @param e El evento de tecla que se ha presionado.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        super.keyPressed(e);
        // Verifica si el guerrero está muerto o si ha alcanzado el límite de esmeraldas
        if (this.warrior.getLive() <= 0 || this.warrior.getEmeralds() == 10) {
            panelGame.removeKeyListener(this); // Elimina el listener si el guerrero no puede jugar
        } else {
            char key = Character.toLowerCase(e.getKeyChar()); // Convierte la tecla a minúscula
            switch (key) {
                case 'a': // Mover hacia la izquierda
                    if (!warrior.getBody().getIcon().equals(warrior.getLeftMove())) {
                        warrior.getBody().setIcon(warrior.getLeftMove());
                    }
                    warrior.getBody().setLocation(warrior.getBody().getX() - Tools.validateMove(warrior.getBody(), listWalls, warrior.getSpeed(), key), warrior.getBody().getY());
                    relocate(); // Reloca el aura si el guerrero es un soldado
                    break;
                case 's': // Mover hacia abajo
                    if (!warrior.getBody().getIcon().equals(warrior.getDownMove())) {
                        warrior.getBody().setIcon(warrior.getDownMove());
                    }
                    warrior.getBody().setLocation(warrior.getBody().getX(), warrior.getBody().getY() + Tools.validateMove(warrior.getBody(), listWalls, warrior.getSpeed(), key));
                    relocate(); // Reloca el aura
                    break;
                case 'd': // Mover hacia la derecha
                    if (!warrior.getBody().getIcon().equals(warrior.getRightMove())) {
                        warrior.getBody().setIcon(warrior.getRightMove());
                    }
                    warrior.getBody().setLocation(Tools.validateMove(warrior.getBody(), listWalls, warrior.getSpeed(), key) + warrior.getBody().getX(), warrior.getBody().getY());
                    relocate(); // Reloca el aura
                    break;
                case 'w': // Mover hacia arriba
                    if (!warrior.getBody().getIcon().equals(warrior.getUpMove())) {
                        warrior.getBody().setIcon(warrior.getUpMove());
                    }
                    warrior.getBody().setLocation(warrior.getBody().getX(), warrior.getBody().getY() - Tools.validateMove(warrior.getBody(), listWalls, warrior.getSpeed(), key));
                    relocate(); // Reloca el aura
                    break;
                case 'p': // Atacar
                    warrior.attack(panelGame, skeletons, listWalls);
                    break;
                case 'o': // Usar habilidad definitiva si el mana está en 10 o 0
                    if (this.warrior.getMana() == 10 || this.warrior.getMana() == 0) {
                        warrior.ulti(panelGame, skeletons, listWalls);
                    }
                    break;
                case 't': // Añadir 10 esmeraldas al guerrero
                    for (int i = 0; i < 10; i++) {
                        warrior.addEmerald();
                    }
                    break;
            }
        }
    }

    /**
     * Método que reubica el aura del guerrero si es un soldado.
     */
    private void relocate() {
        if (warrior instanceof Soldier) {
            ((Soldier) warrior).getAura().setLocation(warrior.getBody().getX() - 9, warrior.getBody().getY() - 9);
        }
    }

    /**
     * Método llamado cuando se suelta una tecla.
     * Cambia el ícono del guerrero al estado correspondiente.
     *
     * @param e El evento de tecla que se ha soltado.
     */
    @Override
    public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        char key = Character.toLowerCase(e.getKeyChar());
        switch (key) {
            case 'a':
                warrior.getBody().setIcon(warrior.getLeft());
                break;
            case 's':
                warrior.getBody().setIcon(warrior.getDown());
                break;
            case 'd':
                warrior.getBody().setIcon(warrior.getRight());
                break;
            case 'w':
                warrior.getBody().setIcon(warrior.getUp());
                break;
        }
    }
}
