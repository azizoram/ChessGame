package view;

import chess.Game;
import core.Board;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

/**
 * The SaveGameView class represents a view for saving a chess game.
 * It provides a window with a text field to enter the save name and a button to perform the save operation.
 */
public class SaveGameView {
    JFrame sgvFrame = new JFrame("SAVE");
    Board board;
    JTextField saveName = new JTextField("autosave.ch" );
    JLabel messageBoard;

    /**
     * Constructs a new SaveGameView object.
     *
     * @param board The chess board associated with the game.
     * @param msg The message board JLabel to display save status messages.
     */
    public SaveGameView(Board board, JLabel msg){
        JPanel savePanel = new JPanel(new GridLayout(1, 2, 8, 20));
        savePanel.setBorder(new EmptyBorder(20,20,20,20));
        savePanel.add(saveName);
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(this::handleSaveButton);
        savePanel.add(saveButton);

        sgvFrame.add(savePanel);

        sgvFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JFrame gameFrame = Game.StateManager.getMainFrame();
        sgvFrame.setLocationRelativeTo(gameFrame);
        sgvFrame.setLocation((int)(gameFrame.getSize().width / 1.8),(int) (gameFrame.getSize().height / 1.8));
        sgvFrame.pack();
        sgvFrame.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowLostFocus(WindowEvent e) {
                closeWindow();
            }
            @Override
            public void windowGainedFocus(WindowEvent e) {            }
        });

        sgvFrame.setVisible(true);
        sgvFrame.setMinimumSize(sgvFrame.getSize());
        messageBoard = msg;
        this.board = board;
    }

    /**
     * Handles the save button click event.
     *
     * @param actionEvent The action event triggered by the save button.
     */
    private void handleSaveButton(ActionEvent actionEvent) {
        boolean saveStatus = board.saveGame(saveName.getText());
        messageBoard.setText( (saveStatus?"Saved successfully!":"Couldn't save your game!") );
        closeWindow();
    }

    /**
     * Closes the save window.
     */
    private void closeWindow(){
        sgvFrame.dispose();
    }
}
