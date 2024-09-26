package listeners;

import user.User;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedHashSet;

/**
 * @author Mohamed Boutanghach
 * Clase que maneja el evento de clic del ratón para seleccionar un usuario.
 * Extiende MouseAdapter para capturar los eventos de clic.
 */
public class MouseListenerSeleUser extends MouseAdapter {
    private JPanel panelMain;          // Panel principal donde se realizan los cambios
    private JPanel newPanel;           // Nuevo panel que se mostrará al seleccionar un usuario
    private LinkedHashSet<User> listUsers; // Lista de usuarios disponibles

    /**
     * Constructor para la clase MouseListenerSeleUser.
     *
     * @param panelMain   El panel principal que contendrá los cambios.
     * @param newPanel    El panel que se mostrará al seleccionar un usuario.
     * @param listUsers   La lista de usuarios que han sido creados.
     */
    public MouseListenerSeleUser(JPanel panelMain, JPanel newPanel, LinkedHashSet<User> listUsers) {
        this.newPanel = newPanel;
        this.panelMain = panelMain;
        this.listUsers = listUsers;
    }

    /**
     * Maneja el evento de clic del ratón.
     * Cambia el panel principal al nuevo panel si hay al menos un usuario creado.
     *
     * @param e El evento de clic del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if (listUsers.isEmpty()) {
            // Muestra un mensaje si no hay usuarios disponibles
            JOptionPane.showMessageDialog(null, "Antes de jugar debes de crear mínimo un usuario");
        } else {
            panelMain.removeAll();         // Elimina todos los componentes del panel principal
            panelMain.repaint();           // Repinta el panel principal
            panelMain.add(newPanel);       // Agrega el nuevo panel al panel principal
            panelMain.repaint();           // Repinta el panel principal para mostrar el nuevo panel
        }
    }
}
