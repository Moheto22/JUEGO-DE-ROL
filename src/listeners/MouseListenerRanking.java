package listeners;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el evento de clic del ratón para mostrar el panel de ranking.
 * Extiende MouseAdapter para capturar los eventos de clic.
 */
public class MouseListenerRanking extends MouseAdapter {
    private JPanel panelMain;    // Panel principal donde se realizan los cambios
    private JPanel panelRanking;  // Panel que muestra el ranking

    /**
     * Constructor para la clase MouseListenerRanking.
     *
     * @param panelMain  El panel principal que contendrá los cambios.
     * @param panelRanking El panel que muestra el ranking al hacer clic.
     */
    public MouseListenerRanking(JPanel panelMain, JPanel panelRanking) {
        this.panelMain = panelMain;
        this.panelRanking = panelRanking;
    }

    /**
     * Maneja el evento de clic del ratón.
     * Cambia el panel principal al panel de ranking.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        this.panelMain.removeAll();      // Elimina todos los componentes del panel principal
        this.panelMain.repaint();        // Repinta el panel principal
        this.panelMain.add(panelRanking); // Agrega el panel de ranking al panel principal
        this.panelMain.repaint();        // Repinta el panel principal para mostrar el nuevo panel
    }
}