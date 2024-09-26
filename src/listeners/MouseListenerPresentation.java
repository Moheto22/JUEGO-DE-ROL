package listeners;



import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/**
 * @author Mohamed Boutanghach
 * Clase que maneja el evento de clic del ratón para cambiar de panel en la interfaz gráfica.
 * Extiende MouseAdapter para capturar los eventos de clic.
 */
public class MouseListenerPresentation extends MouseAdapter {
    private JPanel panelMain;   // Panel principal donde se realizan los cambios
    private JPanel newPanel;     // Nuevo panel que se mostrará al hacer clic

    /**
     * Constructor para la clase MouseListenerPresentation.
     *
     * @param panelMain El panel principal que contendrá los cambios.
     * @param newPanel  El nuevo panel que se mostrará al hacer clic.
     */
    public MouseListenerPresentation(JPanel panelMain, JPanel newPanel) {
        this.panelMain = panelMain;
        this.newPanel = newPanel;
    }

    /**
     * Maneja el evento de clic del ratón.
     * Cambia el panel principal al nuevo panel especificado.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        panelMain.removeAll();       // Elimina todos los componentes del panel principal
        panelMain.repaint();         // Repinta el panel principal
        panelMain.add(newPanel);     // Agrega el nuevo panel al panel principal
        panelMain.repaint();         // Repinta el panel principal para mostrar el nuevo panel
    }
}
