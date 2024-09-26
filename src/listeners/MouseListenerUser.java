package listeners;

import user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.LinkedHashSet;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja los eventos de mouse para un botón de usuario.
 * Extiende MouseAdapter para manejar los eventos de entrada, salida y clic del ratón.
 */
public class MouseListenerUser extends MouseAdapter {
    private JButton botton;               // Botón que activará los eventos
    private JPanel panelMain;             // Panel principal que contendrá los cambios
    private JPanel panelSeleWarrior;      // Panel para la selección del guerrero

    /**
     * Constructor para la clase MouseListenerUser.
     *
     * @param botton           El botón que activará los eventos.
     * @param panelSeleWarrior El panel donde se seleccionará el guerrero.
     * @param panelMain       El panel principal que contendrá los cambios.
     */
    public MouseListenerUser(JButton botton, JPanel panelSeleWarrior, JPanel panelMain) {
        this.botton = botton;
        this.panelMain = panelMain;
        this.panelSeleWarrior = panelSeleWarrior;
    }

    /**
     * Maneja el evento cuando el ratón entra en el área del botón.
     *
     * @param e El evento de entrada del ratón.
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        super.mouseEntered(e);
        botton.setBackground(new Color(255, 164, 0)); // Cambia el color de fondo al entrar
        botton.setForeground(new Color(164, 8, 8));   // Cambia el color del texto al entrar
        botton.repaint(); // Repinta el botón para mostrar los cambios
    }

    /**
     * Maneja el evento cuando el ratón sale del área del botón.
     *
     * @param e El evento de salida del ratón.
     */
    @Override
    public void mouseExited(MouseEvent e) {
        super.mouseExited(e);
        botton.setBackground(new Color(255, 164, 0)); // Restablece el color de fondo
        botton.setForeground(Color.white);            // Restablece el color del texto
        botton.repaint(); // Repinta el botón para mostrar los cambios
    }

    /**
     * Maneja el evento cuando se hace clic en el botón.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        String name = botton.getText();               // Obtiene el texto del botón
        panelSeleWarrior.setName(name);               // Establece el nombre en el panel de selección de guerrero
        panelMain.removeAll();                         // Elimina todos los componentes del panel principal
        panelMain.repaint();                           // Repinta el panel principal
        panelMain.add(panelSeleWarrior);              // Agrega el panel de selección de guerrero al panel principal
        panelMain.repaint();                           // Repinta el panel principal para mostrar el nuevo panel
    }
}