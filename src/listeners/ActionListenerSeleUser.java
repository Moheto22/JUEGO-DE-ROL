package listeners;

import user.User;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Mohamed Boutanghach
 * ActionListener que maneja la selección del usuario y actualiza el panel principal.
 */
public class ActionListenerSeleUser implements ActionListener {
    private JPanel panelMain; // Panel principal a actualizar
    private JPanel newPanel;  // Nuevo panel a mostrar después de la selección del usuario
    private User user;        // Objeto User que representa al usuario seleccionado

    /**
     * Constructor para ActionListenerSeleUser.
     *
     * @param user      El objeto User que representa al usuario seleccionado.
     * @param panelMain El JPanel principal a actualizar.
     * @param newPanel  El nuevo JPanel a mostrar.
     */
    public ActionListenerSeleUser(User user, JPanel panelMain, JPanel newPanel) {
        this.panelMain = panelMain;
        this.newPanel = newPanel;
        this.user = user;
    }

    /**
     * Acción a realizar cuando el usuario selecciona una opción.
     * Elimina todos los componentes del panel principal y agrega el nuevo panel.
     *
     * @param e El ActionEvent desencadenado por el temporizador.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(System.identityHashCode(user)); // Información de depuración

        if (user != null) {
            // Limpiar el panel principal y preparar para agregar el nuevo panel
            panelMain.removeAll();
            panelMain.repaint();

            // Agregar el nuevo panel al panel principal
            panelMain.add(newPanel);
            panelMain.repaint();

            // Detener el temporizador que activó esta acción
            ((Timer) e.getSource()).stop();
        }
    }
}