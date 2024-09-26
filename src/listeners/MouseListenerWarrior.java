package listeners;

import user.Round;
import user.User;
import warriors.*;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.TreeSet;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja los eventos de mouse para la selección de un guerrero.
 * Extiende MouseAdapter para gestionar los eventos de entrada, salida y clic del ratón.
 */
public class MouseListenerWarrior extends MouseAdapter {
    private JPanel panelMain;               // Panel principal que contendrá los cambios
    private JPanel panelGame;                // Panel del juego
    private JPanel panelMainMenu;           // Panel del menú principal
    private JPanel panelSeleWarrior;        // Panel para la selección de guerreros
    private JPanel ranking;                  // Panel de ranking
    private JLabel label;                    // Etiqueta que representa el guerrero seleccionado
    private Warrior warrior;                 // Guerrero seleccionado
    private Timer timer;                     // Temporizador para efectos visuales
    private ArrayList<JLabel> listWalls;    // Lista de paredes en el juego
    private ArrayList<JLabel> listFloor;     // Lista de pisos en el juego
    private TreeSet<Round> listRounds;      // Lista de rondas jugadas
    private LinkedHashSet<User> listUsers;  // Lista de usuarios

    /**
     * Constructor para la clase MouseListenerWarrior.
     *
     * @param label          La etiqueta que representa el guerrero.
     * @param panelMain     El panel principal que contendrá los cambios.
     * @param panelGame     El panel del juego.
     * @param listWalls     La lista de paredes en el juego.
     * @param listFloor     La lista de pisos en el juego.
     * @param panelMainMenu El panel del menú principal.
     * @param panel         El panel de selección de guerreros.
     * @param listRounds    La lista de rondas jugadas.
     * @param ranking       El panel de ranking.
     * @param listUsers     La lista de usuarios.
     */
    public MouseListenerWarrior(JLabel label, JPanel panelMain, JPanel panelGame, ArrayList<JLabel> listWalls, ArrayList<JLabel> listFloor, JPanel panelMainMenu, JPanel panel, TreeSet<Round> listRounds, JPanel ranking, LinkedHashSet<User> listUsers) {
        this.label = label;
        this.panelMain = panelMain;
        this.warrior = null;
        this.panelGame = panelGame;
        this.listWalls = listWalls;
        this.listFloor = listFloor;
        this.panelMainMenu = panelMainMenu;
        this.panelSeleWarrior = panel;
        this.listRounds = listRounds;
        this.ranking = ranking;
        this.listUsers = listUsers;
    }

    /**
     * Maneja el evento cuando el ratón entra en el área de la etiqueta del guerrero.
     *
     * @param e El evento de entrada del ratón.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        if (this.timer != null) {
            if (this.timer.isRunning()) {
                this.timer.stop();
            }
        }
        this.timer = new Timer(10, new ActionListenerAugmentAva(label)); // Aumenta el efecto visual
        this.timer.start();
    }

    /**
     * Maneja el evento cuando el ratón sale del área de la etiqueta del guerrero.
     *
     * @param e El evento de salida del ratón.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        if (this.timer.isRunning()) {
            this.timer.stop();
        }
        this.timer = new Timer(10, new ActionListenerReduceAva(label)); // Reduce el efecto visual
        this.timer.start();
    }

    /**
     * Maneja el evento cuando se hace clic en la etiqueta del guerrero.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        JLabel body, aura;

        // Determina el tipo de guerrero según la imagen de la etiqueta
        if (label.getName().equals("src/images/magicAvatar.png")) {
            this.warrior = new Magician();
        } else if (label.getName().equals("src/images/priestAvatar.png")) {
            this.warrior = new Priest();
        } else {
            this.warrior = new Soldier();
        }

        ArrayList<JLabel> items = new ArrayList<>();
        ArrayList<Skeleton> skeletons = new ArrayList<>();
        this.panelMain.removeAll();
        this.panelMain.repaint();

        // Si es un soldado, se agrega su aura al panel del juego
        if (warrior instanceof Soldier) {
            aura = ((Soldier) warrior).getAura();
            panelGame.add(aura);
            panelGame.setComponentZOrder(aura, 0);
        }

        panelGame.setName(panelSeleWarrior.getName()); // Configura el nombre del panel de juego
        body = warrior.getBody(); // Obtiene el cuerpo del guerrero
        panelGame.add(body); // Agrega el cuerpo al panel del juego
        panelGame.setComponentZOrder(body, 0);
        this.panelMain.add(panelGame); // Agrega el panel del juego al panel principal

        // Inicia un temporizador para agregar monstruos cada 2 segundos
        Timer addMonsters = new Timer(2000, new ActionListenerAddMonst(warrior, listWalls, listFloor, panelGame, skeletons, items));
        addMonsters.start();

        // Agrega un KeyListener para manejar las acciones del guerrero
        panelGame.addKeyListener(new KeyListenerGame(warrior, listWalls, skeletons, panelGame, items));
        addHearts(); // Agrega los iconos de vida al juego
        panelGame.add(warrior.getQuanty());
        panelGame.setComponentZOrder(warrior.getQuanty(), 0);

        // Inicia el temporizador del juego
        Timer timerGame = new Timer(5, new ListenerGame(warrior, panelMain, panelMainMenu, panelGame, listRounds, ranking, listUsers));
        timerGame.start();

        // Prepara el panel del juego para recibir entrada
        panelGame.setFocusable(true);
        panelGame.requestFocusInWindow();

        if (this.timer.isRunning()) {
            this.timer.stop();
        }

        this.timer = new Timer(10, new ActionListenerReduceAva(label)); // Reduce el efecto visual
        this.timer.start();
        this.panelMain.repaint();
    }

    /**
     * Agrega los iconos de vida del guerrero al panel del juego.
     */
    private void addHearts() {
        for (int i = 0; i < this.warrior.getLive(); i++) {
            this.warrior.getLivesIcon().get(i).setLocation(150 + (i * this.warrior.getLivesIcon().get(i).getWidth() + 20), 20);
            this.panelGame.add(this.warrior.getLivesIcon().get(i)); // Agrega cada icono de vida al panel del juego
            this.panelGame.setComponentZOrder(this.warrior.getLivesIcon().get(i), 0);
        }
        panelGame.repaint(); // Repinta el panel del juego
    }
}
