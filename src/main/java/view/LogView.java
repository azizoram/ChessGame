package view;



import core.ChessLog;
import model.piece.Piece;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

/**
 * The LogView class represents a view component for displaying a chess log.
 * It provides methods for initializing, rendering, and customizing the log display.
 */
public class LogView {
    JPanel logPanel = null;
    public JLabel logMessage = new JLabel("Moves log");

    /**
     * Retrieves the view component for the log.
     *
     * @return The JPanel representing the log view.
     */
    public JPanel getView() {
        return logPanel;
    }
    private JPanel takenPanel = null;

    /**
     * Initializes the log view with the specified game log.
     *
     * @param gameLog The ChessLog object representing the game log.
     */
    public void init(ChessLog gameLog) {
        logPanel = new JPanel();
        logPanel.setMinimumSize(getLogSize());
        logPanel.setPreferredSize(getLogSize());
        logPanel.setBorder(new LineBorder(Color.BLACK));
        takenPanel = generateTaken(gameLog);
        render(gameLog);
        gameLog.board.setTimer(logMessage);
    }

    /**
     * Generates the panel for displaying the taken pieces based on the game log.
     *
     * @param gameLog The ChessLog object representing the game log.
     * @return The JPanel representing the taken pieces.
     */
    private JPanel generateTaken(ChessLog gameLog) {
        JPanel takenPanel = new JPanel(new GridLayout(2,1,5,5));
        takenPanel.setMinimumSize(new Dimension(200,150));
        ArrayList<Piece> takenWhitePieces = gameLog.getTaken();
        ArrayList<Piece> takenBlackPieces = gameLog.getTaken();

        takenWhitePieces.removeIf(p -> p.getColor() == model.player.Color.BLACK);
        takenBlackPieces.removeIf(p -> p.getColor() == model.player.Color.WHITE);

        takenWhitePieces.sort(Comparator.comparing(Piece::getValue).reversed());
        takenBlackPieces.sort(Comparator.comparing(Piece::getValue).reversed());

        JPanel left = new JPanel(),right = new JPanel();

        Piece before = null;
        int counter = 1;
        for(Piece taken : takenWhitePieces){
            if(before != null && before.getClass() != taken.getClass()){
                JPanel piecePanel = createPanel("x " + counter , new ImageIcon(extractIcon(before)));

                left.add(piecePanel);
                counter = 1;
            }
            if(before != null && before.getClass() == taken.getClass()){
                counter++;
            }
            before=taken;
        }
        if(before != null){
            JPanel piecePanel = createPanel("x " + counter , new ImageIcon(extractIcon(before)));

            left.add(piecePanel);
        }
        counter = 1;
        before = null;
        for(Piece taken : takenBlackPieces){
            if(before != null && before.getClass() != taken.getClass()){
                JPanel piecePanel = createPanel("x " + counter , new ImageIcon(extractIcon(before)));

                right.add(piecePanel);
                counter = 1;
            }
            if(before != null && before.getClass() == taken.getClass()){
                counter++;
            }
            before=taken;
        }
        if(before != null){
            JPanel piecePanel = createPanel("x " + counter , new ImageIcon(extractIcon(before)));

            right.add(piecePanel);
        }
        takenPanel.add(left);
        takenPanel.add(right);
        return takenPanel;
    }

    /**
     * Extracts the icon for the specified piece.
     *
     * @param before The piece to extract the icon for.
     * @return The BufferedImage representing the icon.
     */
    private BufferedImage extractIcon(Piece before) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(Objects.requireNonNull(this.getClass().getResourceAsStream(before.getPathToImage())));
            bi = BoardView.resize(bi, 16, 16);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bi;
    }

    /**
     * Renders the log view based on the specified game log.
     *
     * @param gameLog The ChessLog object representing the game log.
     */
    public void render(ChessLog gameLog){
        logPanel.removeAll();
        logPanel.add(logMessage);

        JList<Object> list = new JList<>();
        list.setBackground(new Color(171, 171, 171));
        list.setModel(new DefaultListModel<>());
        list.setCellRenderer(new ChessLogCellRenderer());
        list.setListData(gameLog.getMoves().toArray());
        JScrollPane scrollableList = new JScrollPane(list);
        scrollableList.setPreferredSize(getLogSize());
        scrollableList.setBorder(new LineBorder(Color.BLACK));

        enableWheelScroll(scrollableList);

        scrollToBottom(scrollableList);

        scrollableList.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        logPanel.add(scrollableList);
        takenPanel = generateTaken(gameLog);
        logPanel.add(takenPanel);
    }

    /**
     * Enables wheel scrolling for the specified JScrollPane.
     *
     * @param scrollableList The JScrollPane component to enable wheel scrolling for.
     */
    private void enableWheelScroll(JScrollPane scrollableList) {
        scrollableList.addMouseWheelListener(e -> {
            JScrollBar verticalBar = scrollableList.getVerticalScrollBar();

            int amountToScroll = e.getWheelRotation() * verticalBar.getUnitIncrement(1);
            verticalBar.setValue(verticalBar.getValue() + amountToScroll);
        });
    }

    /**
     * Scrolls the specified JScrollPane to the bottom.
     *
     * @param scrollableList The JScrollPane component to scroll.
     */
    private void scrollToBottom(JScrollPane scrollableList) {
        JScrollBar verticalBar = scrollableList.getVerticalScrollBar();
        int currentScrollValue = verticalBar.getValue();
        int previousScrollValue = -1;

        while (currentScrollValue != previousScrollValue) {
            // Scroll one list element down
            int downDirection = 1;
            int amountToScroll = verticalBar.getUnitIncrement(downDirection);
            verticalBar.setValue(currentScrollValue + amountToScroll);

            previousScrollValue = currentScrollValue;
            currentScrollValue = verticalBar.getValue();
        }
    }

    /**
     * Retrieves the preferred size for the log view.
     *
     * @return The preferred size as a Dimension object.
     */
    private Dimension getLogSize() {
        return new Dimension(200, 550);
    }

    /**
     * Creates a JPanel with the specified text and icon.
     *
     * @param s The text to display.
     * @param i The icon to display.
     * @return The JPanel with the specified text and icon.
     */
    private JPanel createPanel(String s, ImageIcon i) {
        JPanel p = new JPanel(new BorderLayout());
        p.add(new JLabel(s, JLabel.RIGHT), BorderLayout.EAST);
        p.add(new JLabel(i, JLabel.LEFT), BorderLayout.WEST);
        return p;
    }
}