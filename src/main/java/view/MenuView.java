package view;
import chess.Game;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * The MenuView class represents a view component for the main menu of a chess game.
 * It provides methods for initializing and retrieving the menu panel.
 */
public class MenuView {

    final JPanel menuPanel = new JPanel(new BorderLayout(3, 3));
    final JButton pvpButton = new JButton("Player versus Player");
    final JButton pvcButton = new JButton("Player versus computer");
    final JButton exitButton = new JButton("Exit");

    /**
     * Constructs a new MenuView object and initializes the menu panel.
     */
    public MenuView(){
        menuPanel.setBorder(new EmptyBorder(25, 30, 30, 30));

        menuPanel.setLayout(new GridLayout(4, 1, 10, 20));
        pvpButton.addActionListener(l->{
            Game.StateManager.runPvP();
        });
        menuPanel.add(pvpButton, BorderLayout.CENTER);
        menuPanel.add(pvcButton);
        exitButton.addActionListener(l->{
            System.exit(0);
        });
        menuPanel.add(exitButton);
    }

    /**
     * Retrieves the menu panel component.
     *
     * @return The JPanel representing the menu panel.
     */
    public Component getMenuPanel(){
        return menuPanel;
    }

}
