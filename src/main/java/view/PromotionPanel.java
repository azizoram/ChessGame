package view;


import chess.Game;
import core.Board;
import model.piece.Piece;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static view.BoardView.resize;

/**
 * The PromotionPanel class represents a panel for promoting a pawn to a different piece in a chess game.
 * It provides methods for displaying the promotion panel, handling user interactions, and closing the panel.
 */
public class PromotionPanel {

    JFrame sgvFrame = new JFrame("PROMOTE A FIGURE");
    Board board;
    Piece pieceToPromote;

    /**
     * Constructs a new PromotionPanel object.
     *
     * @param board The chess board where the promotion occurs.
     * @param p The pawn piece to be promoted.
     * @param t The thread associated with the game.
     */
    public PromotionPanel(Board board, Piece p, Thread t){
        JPanel promotionalPanel = new JPanel(new GridLayout(1, 4, 8, 20));
        pieceToPromote = p;
        for(Piece figure: board.getPromotionalFigures(p)){
            BufferedImage pick= null;
            try {
                pick = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream(figure.getPathToImage())));
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert pick != null;
            JLabel label = new JLabel(new ImageIcon(resize(pick, 64, 64)));
            label.addMouseListener(getMouseListener(figure, t));
            promotionalPanel.add(label);
        }
        sgvFrame.add(promotionalPanel);
        sgvFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JFrame gameFrame = Game.StateManager.getMainFrame();
        sgvFrame.setLocationRelativeTo(gameFrame);
        sgvFrame.setLocation((int)(gameFrame.getSize().width / 1.8),(int) (gameFrame.getSize().height / 1.8));
        sgvFrame.pack();

        sgvFrame.setVisible(true);
        sgvFrame.setMinimumSize(sgvFrame.getSize());

        this.board = board;
    }

    /**
     * Returns a MouseListener for handling mouse events on the promoted piece options.
     *
     * @param figure The piece to promote to.
     * @param t The thread associated with the game.
     * @return A MouseListener for the piece.
     */
    private MouseListener getMouseListener(Piece figure, Thread t){
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleFigureClick(figure, t);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleFigureClick(figure, t);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                handleFigureClick(figure, t);
            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        };
    }

    /**
     * Handles the click event on a promoted piece option.
     *
     * @param figure The piece to promote to.
     * @param t The thread associated with the game.
     */
    private void handleFigureClick(Piece figure, Thread t) {
        board.promoteTo(figure, t);
        closeWindow();
    }

    /**
     * Closes the promotion window.
     */
    private void closeWindow(){
        sgvFrame.dispose();
    }
}
