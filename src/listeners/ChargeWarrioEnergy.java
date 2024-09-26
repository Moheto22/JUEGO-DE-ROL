package listeners;

import warriors.Skeleton;
import warriors.Soldier;
import warriors.Warrior;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChargeWarrioEnergy implements ActionListener {
    private JPanel panel;
    private JLabel energy;
    private Timer timerVibration;
    private ArrayList<Skeleton> skeletons;
    private Warrior warrior;
    public ChargeWarrioEnergy(JPanel panel, JLabel energy, Timer timerVibration, ArrayList<Skeleton> skeletons, Soldier warrior) {
        this.panel = panel;
        this.energy = energy;
        this.timerVibration = timerVibration;
        this.skeletons=skeletons;
        this.warrior=warrior;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timerVibration.stop();
        panel.setLocation(0,0);
        panel.remove(energy);
        panel.setFocusable(true);
        panel.requestFocusInWindow();
        ((Soldier)warrior).getAura().setVisible(true);
        panel.setComponentZOrder(warrior.getBody(),0);
        Timer timerAura=new Timer(10, new AuraWarrior(skeletons,warrior));
        Timer finalUlti=new Timer(10000,new EndUltiWarrior(warrior, timerAura));
        timerAura.start();
        finalUlti.start();
        ((Timer)e.getSource()).stop();
    }
}
