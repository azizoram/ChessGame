package view;


import chess.Game;
import core.Board;
import model.game.Move;
import model.piece.Piece;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The BoardView class represents the graphical user interface for the chess board.
 * It provides methods to initialize the GUI, render the game board, handle user actions,
 * and display relevant information to the user.
 */
public class BoardView {
    /**
     * Constructs a new BoardView and initializes the GUI.
     */
    public BoardView() {
        initializeGui();
    }

    /**
     * The main JPanel that contains the chess board and other components.
     */
    private final JPanel gui = new JPanel(new BorderLayout(3, 3));

    /**
     * The LogView instance for displaying the game log.
     */
    private LogView logView = new LogView();

    /**
     * The JPanel that represents the chess board.
     */
    private JPanel chessBoard;

    /**
     * 2D array of JPanels representing the individual squares on the chess board.
     */
    JPanel chessButtonSquares[][] = new JPanel[8][8];

    /**
     * The JLabel used to display messages to the user.
     */
    private final JLabel message = new JLabel("Chess Champ is ready to play!");

    /**
     * The string representation of the columns on the chess board.
     */
    private static final String COLS = "ABCDEFGH";

    /**
     * The color of the white squares on the chess board.
     */
    private Color whiteSquareColor = new Color(131, 131, 80);

    /**
     * The color of the black squares on the chess board.
     */
    private Color blackSquareColor = new Color(12, 49, 11);

    /**
     * The Board instance representing the game board.
     */
    public Board board = new Board(message);

    /**
     * A flag indicating whether it is the first player's turn (white) or not.
     */
    private boolean isFirstWhite = true;

    /**
     * The JLabel that displays the current turn (white or black).
     */
    JLabel currentTurnDisplay = new JLabel("", SwingConstants.CENTER);

    /**
     * The JToolBar that contains various buttons for game actions.
     */
    private JToolBar tools = new JToolBar();

    /**
     * The button used to resign the game and replace figures.
     */
    private JButton resignButton;

    /**
     * The button used to delete figures during figure placement mode.
     */
    private JButton deleteButton;

    /**
     * A flag indicating whether the game is in figure placement mode or not.
     */
    private boolean isPlacingMode = false;

    /**
     * The button used to access the main menu.
     */
    JButton menuButton;

    /**
     * The button used to undo the last move.
     */
    JButton undoButton;

    /**
     * Initializes the GUI by setting up the main components, buttons, and event listeners.
     */
    public final void initializeGui() {
        // set up the main GUI
        gui.setBorder(new EmptyBorder(5, 5, 5, 5));

        tools.setFloatable(false);
        gui.add(tools, BorderLayout.PAGE_START);

        menuButton = new JButton("Menu");
        menuButton.addActionListener(this::switchToMainMenu);
        tools.add(menuButton);
        JButton SaveButton = new JButton("Save chess.Game");
        SaveButton.addActionListener(this::handleSaveGame);
        tools.add(SaveButton);
        undoButton = new JButton("Undo");
        undoButton.addActionListener(this::handleUndo);
        tools.add(undoButton);
        tools.addSeparator();
        resignButton = new JButton("Replace figures");
        resignButton.addActionListener(l->{
            handleFigurePlacement();
        });
        tools.add(resignButton);
        deleteButton = new JButton("Delete");
        deleteButton.setVisible(false);
        deleteButton.addActionListener(this::handleFigureDelete);
        tools.add(deleteButton);
        tools.addSeparator();
        tools.add(message);
        chessBoard = new JPanel(new GridLayout(0, 9));
        chessBoard.setBorder(new LineBorder(Color.BLACK));
        logView.init(board.log);
        gui.add(logView.getView(), BorderLayout.EAST);

        renderGameBoard();
        gui.add(chessBoard);
    }

    /**
     * Handles the "Undo" button click event.
     *
     * @param actionEvent the action event
     */
    private void handleUndo(ActionEvent actionEvent) {
        board.handleUndo(isPlacingMode);
        renderGameBoard();
    }

    /**
     * Switches to the main menu when the "Menu" button is clicked.
     *
     * @param actionEvent the action event
     */
    private void switchToMainMenu(ActionEvent actionEvent) {
        Game.StateManager.runMainMenu();
    }

    /**
     * Handles the "Save chess.Game" button click event.
     *
     * @param actionEvent the action event
     */
    private void handleSaveGame(ActionEvent actionEvent) {
        SaveGameView nsgv = new SaveGameView(board, message);
    }

    /**
     * Handles the "Delete" button click event during figure placement mode.
     *
     * @param actionEvent the action event
     */
    private void handleFigureDelete(ActionEvent actionEvent) {
        if(isPlacingMode) {
            board.deleteFigure();
            renderGameBoard();
        }
    }

    /**
     * Renders the game board on the GUI.
     */
    private void renderGameBoard(){
        if(board.check != null){
            message.setText( (board.check == board.whiteKing.getColor()?
                    "Check for whites!" : "Check for blacks!") );
        }else{
            message.setText( "");
        }
        if(board.winner != null){
            message.setText((board.winner == board.blackKing.getColor()?
                    "Checkmate for whites! Black WINS!" : "Checkmate for blacks! White WINS!") );
        }
        message.revalidate();
        message.repaint();
        chessBoard.removeAll();
        chessButtonSquares = new JPanel[8][8];
        for (int ii = 0; ii < chessButtonSquares.length; ii++) {
            for (int jj = 0; jj < chessButtonSquares[ii].length; jj++) {
                JPanel b = new JPanel();
                Color bgcolor = ( (ii%2 + jj%2) %2 == 0 ^ !isFirstWhite )?whiteSquareColor:blackSquareColor;

                b.setBackground(bgcolor);
                if(board != null){
                    enplaceFigure(b, board.gameFieldO[ii][jj]);
                }
                chessButtonSquares[jj][ii] = b;
            }
        }

        currentTurnDisplay.addMouseListener( turnDisplayClicked);
        currentTurnDisplay.setText((board.isWhiteTurn)?"White":"Black");
        chessBoard.add(currentTurnDisplay);
        // fill the top row
        for (int ii = 0; ii < 8; ii++) {
            chessBoard.add(new JLabel(COLS.substring(ii, ii + 1),
                    SwingConstants.CENTER));
        }
        // fill the black non-pawn piece row
        for (int ii = 0; ii < 8; ii++) {
            for (int jj = 0; jj < 8; jj++) {
                if(jj == 0){
                    chessBoard.add(new JLabel("" + (8 - ii), SwingConstants.CENTER));
                }
                chessBoard.add(chessButtonSquares[jj][ii]);
            }
        }
        chessBoard.revalidate();
        chessBoard.repaint();

        renderLog();
    }

    /**
     * Renders the game log on the GUI.
     */
    private void renderLog() {
        logView.getView().revalidate();
        logView.render(board.log);
        logView.getView().repaint();
    }

    /**
     * Resizes an image to the specified dimensions.
     *
     * @param img  the original image
     * @param newW the desired width
     * @param newH the desired height
     * @return the resized image
     */
    public static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    /**
     * Places a figure on the specified panel.
     *
     * @param b the panel to place the figure on
     * @param c the figure to be placed
     */
    private void enplaceFigure(JPanel b, Piece c){
        if(c == null){
            return;
        }
        try {
            JLabel label = null;
            BufferedImage pick= ImageIO.read(Objects.requireNonNull(this.getClass().
                    getResourceAsStream(c.getPathToImage())));
            label = new JLabel(new ImageIcon(resize(pick, 64, 64)));
            JLabel finalLabel = label;

            label.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    onMouseClicked(c);
                }

                @Override
                public void mousePressed(MouseEvent e) {                   }
                @Override
                public void mouseReleased(MouseEvent e) {                    }
                @Override
                public void mouseEntered(MouseEvent e) {                    }
                @Override
                public void mouseExited(MouseEvent e) {                    }
            });
            b.add(label);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Handles the mouse click event on a chess piece.
     *
     * @param clickedOnFigure the chess piece that was clicked on
     */
    private void onMouseClicked(Piece clickedOnFigure) {
        if(isPlacingMode){
            outlinePlaceable(clickedOnFigure);
            return;
        }
        if(board.winner != null){
            return;
        }
        if(board.interuptedByPromotion){
            return;
        }
        if(checkClickOnEnemy(clickedOnFigure)){
            return;
        }
        if(clickedOnFigure.getColor() != ((board.isWhiteTurn)?
                model.player.Color.WHITE: model.player.Color.BLACK))
            return;
        clearPossibleMoves();

        board.activeFigure = clickedOnFigure;
        board.possibleMoves = clickedOnFigure.validMoves(board);

        for(Move move: board.possibleMoves){
            chessButtonSquares[move.getMovedTo().x][move.getMovedTo().y].
                    setBorder(BorderFactory.createDashedBorder((move.getPieceToCapture()
                            !=null?Color.red:Color.white), 2.5f, 4.5f, 1.5f, true));
            chessButtonSquares[move.getMovedTo().x][move.getMovedTo().y].
                    addMouseListener(getMouseClickListener(clickedOnFigure, move));
        }
    }

    /**
     * Outlines the placeable positions for a chess piece during figure placement mode.
     *
     * @param clickedOnFigure the chess piece that was clicked on
     */
    private void outlinePlaceable(Piece clickedOnFigure) {
        board.activeFigure = clickedOnFigure;
        board.computeEmptyCells();

        for (Move move : board.possibleMoves) {
            chessButtonSquares[move.getMovedTo().x][move.getMovedTo().y].setBorder
                    (BorderFactory.createDashedBorder((Color.white),
                            2.5f, 4.5f, 1.5f, true));
            chessButtonSquares[move.getMovedTo().x][move.getMovedTo().y].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    board.relocate(move);
                    renderGameBoard();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    board.relocate(move);
                    renderGameBoard();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    board.relocate(move);
                    renderGameBoard();
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }
    }

    /**
     * Checks if a chess piece clicked on belongs to the enemy team.
     *
     * @param clickedOnFigure the chess piece that was clicked on
     * @return true if the piece belongs to the enemy team, false otherwise
     */
    private MouseListener getMouseClickListener(Piece clickedOnFigure, Move move) {
        return new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                processMove(move, board, e, clickedOnFigure);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                processMove(move, board, e, clickedOnFigure);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                processMove(move, board, e, clickedOnFigure);
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
     * Clears the possible move indicators from the chess board.
     */
    private boolean checkClickOnEnemy(Piece clickedOnFigure) {
        Move moveOnEnemy = null;
        if(board.possibleMoves == null || board.activeFigure == null){
            return false;
        }
        for(Move move: board.possibleMoves){
            if(move.getMovedTo().equals(clickedOnFigure.getCoordinate())){
                moveOnEnemy = move;
            }
        }
        if(moveOnEnemy != null){
            processMove(moveOnEnemy, board,null, board.activeFigure);
            return true;
        }
        return false;
    }

    /**
     * Processes a move on the chess board.
     *
     * @param move the move to be processed
     * @param board the chess board
     * @param e the mouse event
     * @param c the chess piece to be moved
     */
    private void processMove(Move move, Board board, MouseEvent e, Piece c) {

        c.makeMove(move, board);
        Thread t = new Thread(){
            public void run(){
                renderGameBoard();
            }
        };
        board.checkPromotion(t);
        renderGameBoard();
    }

    /**
     * Clears the possible move indicators from the chess board.
     */
    private void clearPossibleMoves() {
        for (int i = 0; i < board.height; i++){
            for(int j = 0; j < board.width; j++){
                chessButtonSquares[i][j].setBorder(BorderFactory.createEmptyBorder());
                for(MouseListener ml : chessButtonSquares[i][j].getMouseListeners()){
                    chessButtonSquares[i][j].removeMouseListener(ml);
                }
            }
        }
    }

    /**
     * Returns the graphical user interface component representing the chess game board.
     *
     * @return The GUI component representing the chess game board.
     */
    public final JComponent getGui() {
        return gui;
    }

    /**
     * Toggles the figure placement mode in the chess game.
     * When figure placement mode is activated, the user can place chess figures on the board.
     * When figure placement mode is deactivated, the user cannot place figures.
     * The visibility of the delete button is updated accordingly.
     */
    private void handleFigurePlacement(){
        isPlacingMode = !isPlacingMode;
        deleteButton.setVisible(isPlacingMode);
    }

    /**
     * Handles the click event on the turn display component.
     * If figure placement mode is active, toggles the turn between white and black players.
     * Updates the text and visual representation of the current turn display accordingly.
     */
    private final MouseListener turnDisplayClicked = new MouseListener() {
        void handleClick(){
            if(isPlacingMode) {
                board.isWhiteTurn = !board.isWhiteTurn;
            }
            currentTurnDisplay.setText((board.isWhiteTurn)?"White":"Black");
            currentTurnDisplay.revalidate();
            currentTurnDisplay.repaint();
        }
        @Override
        public void mouseClicked(MouseEvent e) {
            handleClick();
        }
        @Override
        public void mousePressed(MouseEvent e) {
        }
        @Override
        public void mouseReleased(MouseEvent e) {
        }
        @Override
        public void mouseEntered(MouseEvent e) {            }
        @Override
        public void mouseExited(MouseEvent e) {            }
    };

}