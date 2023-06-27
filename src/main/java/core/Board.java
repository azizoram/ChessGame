package core;


import model.game.Coordinate;
import model.game.LogMove;
import model.game.Move;
import model.piece.*;
import model.player.Color;
import view.PromotionPanel;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * A class representing a chess board, containing the pieces and game logic.
 */
public class Board {

    // Class properties
    public Piece[][] gameFieldO;
    public King whiteKing, blackKing;
    public char playersTurn;
    public Piece activeFigure;
    public List<Move> possibleMoves;
    public boolean isWhiteTurn = true;
    public final int height = 8;
    public final int width = 8;
    public ChessLog log;
    public Color winner = null;
    public Color check = null;
    private JLabel displayMessage;
    private Timer whiteTimer, blackTimer;
    public boolean interuptedByPromotion = false;

    /**
     * Copy constructor.
     *
     * @param board The Board instance to copy.
     */
    public Board(Board board) {
        this();
        for (int y = 0; y < height; y++){
            System.arraycopy(board.gameFieldO[y], 0, gameFieldO[y], 0, width);
        }
        whiteKing=board.whiteKing;
        blackKing=board.blackKing;
        isWhiteTurn=board.isWhiteTurn;
    }


    /**
     * Initializes a new Board instance with a JLabel for displaying messages.
     *
     * @param message The JLabel used for displaying messages.
     */
    public Board(JLabel message) {
        this();
        displayMessage = message;
    }

    /**
     * Initializes the board by filling it with empty cells and loading the default piece positions.
     */
    private void initBoard(){
        log = new ChessLog();
        log.board = this;
        for (int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                gameFieldO[y][x] = null;
            }
        }
        loadBoard("default.txt");

    }

    /**
     * Default constructor.
     */
    public Board(){
        gameFieldO = new Piece[height][width];
        initBoard();
    }

    /**
     * Opens a file from a given path.
     *
     * @param path The path of the file to open.
     * @return An InputStream object representing the opened file.
     * @throws IllegalArgumentException If the file is not found.
     */
    private InputStream openFile(String path){
        // get the file url, not working in JAR file.
        InputStream resource = getClass().getClassLoader().getResourceAsStream(path);
        if (resource == null) {
            throw new IllegalArgumentException("file not found!");
        } else {
            try {
                return resource;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load the board configuration from a file.
     *
     * @param path The file path of the board configuration.
     */
    private void loadBoard(String path) {
        InputStream file = openFile(path);
        if (file == null) {
            return; // TODO: Handle file not opened
        }

        try (Scanner sc = new Scanner(file)) {
            int count = 0;
            char[] nextline = new char[8];
            playersTurn = sc.nextLine().toCharArray()[0];
            isWhiteTurn = playersTurn == 'W';

            while (count < 8) {
                nextline = sc.nextLine().toCharArray();
                for (int i = 0; i < width; i++) {
                    gameFieldO[count][i] = emplaceFigure(nextline[i], new Coordinate(i, count));
                }
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void loadBoard(String path){
//        InputStream file = openFile(path);
//        if(file == null){
//            return;// todo file not opend
//        }
//        Scanner sc = null;
//        try {
//            sc = new Scanner(file);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        int count = 0;
//        char[] nextline = new char[8];
//        playersTurn = sc.nextLine().toCharArray()[0];
//        isWhiteTurn = playersTurn == 'W';
//
//        while (count < 8){
//            nextline = sc.nextLine().toCharArray();
//            for(int i = 0; i < width; i++){
//                gameFieldO[count][i] = emplaceFigure(nextline[i], new Coordinate(i, count));
//
//            }
//            count++;
//        }
//    }



    /**
     * Creates a new chess piece and places it on the board at the specified coordinate.
     *
     * @param c The character representing the chess piece to be created.
     * @param coordinate The coordinate where the chess piece will be placed.
     * @return The created chess piece.
     */
    private Piece emplaceFigure(char c, Coordinate coordinate) {
        Piece figure = null;
        Color color;
        if(Character.isUpperCase(c)){
            color = Color.WHITE;
        }else{
            color = Color.BLACK;
        }

        switch (Character.toLowerCase(c)){
            case 'p':
                figure = new Pawn(coordinate, color);
                ((Pawn) figure).literal = 'p';
                break;
            case 'c':
                figure = new King(coordinate, color);
                figure.literal = 'c';
                if(color==Color.WHITE){
                    whiteKing = (King)figure;
                }else{
                    blackKing = (King)figure;
                }
                break;
            case 'k':
                figure = new Knight(coordinate, color);
                figure.literal = 'k';
                break;
            case 'r':
                figure = new Rook(coordinate, color);
                figure.literal = 'r';
                break;
            case 'q':
                figure = new Queen(coordinate, color);
                figure.literal = 'q';
                break;
            case 'b':
                figure = new Bishop(coordinate, color);
                figure.literal = 'b';
            default:
                break;
        }
        return figure;
    }

    /**
     * Saves the current game state to a file with the specified path.
     *
     * @param path The path of the file to save the game state.
     * @return A boolean indicating whether the game state was successfully saved.
     */
    public boolean saveGame(String path){
//        file.setWritable(true);
        boolean writeSuccessful = false;
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(path));
            char []boardLine = new char[width+1];
            boardLine[8] = '\n';
            writer.write( (isWhiteTurn?"W\n":"B\n"));
            for(int i = 0; i < height; i++){
                for (int j = 0; j< width; j++){
                    boardLine[j] = getLiteralAt(j, i);
                }
                writer.write(boardLine);
                writeSuccessful = true;
            }

            for (LogMove lm : log.getMoves()){
                StringBuilder sb = new StringBuilder("");
                sb.append(lm.getMovedFrom().toString());
                sb.append('-');
                sb.append(lm.getMovedTo().toString());
                sb.append('-');
                sb.append(
                        lm.getTaken()!=null?
                                (lm.getTaken().getColor() == Color.BLACK ? lm.getTaken().literal :
                                        Character.toUpperCase(lm.getTaken().literal)) :'e');
                sb.append('\n');
                writer.write(sb.toString());
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return writeSuccessful;
    }

    /**
     * Retrieves the literal character representing the chess piece at the specified coordinates.
     *
     * @param x The x-coordinate of the chess piece.
     * @param y The y-coordinate of the chess piece.
     * @return A character representing the chess piece at the specified coordinates.
     */
    private char getLiteralAt(int x, int y) {
        return gameFieldO[y][x]!=null?
                (gameFieldO[y][x].getColor() == Color.BLACK ? gameFieldO[y][x].literal :
                        Character.toUpperCase(gameFieldO[y][x].literal)) :'e';
    }

    /**
     * Changes the active player (white or black).
     */
    public void changePlayer(){
        isWhiteTurn = !isWhiteTurn;
        flipTimer();
    }

    /**
     * Retrieves the chess piece at the specified coordinate.
     *
     * @param cord The coordinate of the chess piece.
     * @return The chess piece at the specified coordinate.
     */
    public Piece getFigureAt(Coordinate cord){
        return checkOutBounds(cord)?null:gameFieldO[cord.getY()][cord.getX()];
    }

    /**
     * Checks if the specified coordinate is out of bounds.
     *
     * @param cord The coordinate to check.
     * @return A boolean indicating whether the coordinate is out of bounds.
     */
    public Boolean checkOutBounds(Coordinate cord){
        return cord.getX()>7 || cord.getX()<0 || cord.getY()>7 || cord.getY()<0;
    }

    /**
     * Computes and stores all possible moves for a piece in placement mode, considering only empty cells.
     */
    public void computeEmptyCells() {
        possibleMoves = new ArrayList<Move>();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(gameFieldO[y][x]==null){
                    possibleMoves.add(new Move(activeFigure, null, new Coordinate(x, y)));
                }
            }
        }
    }

    /**
     * Relocates a piece on the board according to the given move.
     *
     * @param move The move containing information about the piece to be relocated and its destination.
     */
    public void relocate(Move move) {
        if (activeFigure == null){
            return;
        }

        gameFieldO[move.getPieceToMove().getCoordinate().y][move.getPieceToMove().getCoordinate().x] = null;
        move.getPieceToMove().getCoordinate().y = move.getMovedTo().y;
        move.getPieceToMove().getCoordinate().x = move.getMovedTo().x;
        gameFieldO[move.getPieceToMove().getCoordinate().y][move.getPieceToMove().getCoordinate().x] =
                                                                                                move.getPieceToMove();

        activeFigure = null;
        possibleMoves.clear();
    }

    /**
     * Sets the board's timers and starts them.
     *
     * @param message The JLabel to display the timer messages.
     */
    public void setTimer(JLabel message){
        blackTimer = new Timer(false, message, this);
        whiteTimer = new Timer(true, message, this);
        Thread bt = new Thread(blackTimer, "Black timer thread");
        Thread wt = new Thread(whiteTimer, "White timer thread");
        bt.start();
        wt.start();
    }

    /**
     * Flips the active timer between white and black players.
     */
    public void flipTimer(){
        blackTimer.toggleThread();
        whiteTimer.toggleThread();
        if(whiteTimer.isRuns() && blackTimer.isRuns()){
            whiteTimer.toggleThread();
        }
    }

    /**
     * Deletes the currently active figure from the board.
     */
    public void deleteFigure() {
        if(activeFigure == null){
            return;
        }
        gameFieldO[activeFigure.getCoordinate().y][activeFigure.getCoordinate().x] = null;
        log.logMove(new Move(activeFigure, activeFigure, new Coordinate(-1,-1)), true);
        activeFigure = null;
    }

    /**
     * Sets the winner of the game and stops the timers.
     *
     * @param isWhiteWinner A boolean indicating if the winner is the white player.
     * @param message A string containing the message to be displayed.
     */
    public void setWinner(boolean isWhiteWinner, String message){
        winner = isWhiteWinner ? Color.WHITE : Color.BLACK;
        blackTimer.stop();
        whiteTimer.stop();
        displayMessage.setText(message);
    }

    /**
     * Handles undo operations for normal moves and piece placements.
     *
     * @param isPlacingMode A boolean indicating if the current mode is piece placement.
     */
    public void handleUndo(boolean isPlacingMode) {
        LogMove lm = log.lastMove();
        if(lm == null){
            return;
        }
        if(!(isPlacingMode)){
            if(!lm.isTechnical){
                gameFieldO[lm.getMovedFrom().y][lm.getMovedFrom().x] = gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x];
                gameFieldO[lm.getMovedFrom().y][lm.getMovedFrom().x].getCoordinate().x = lm.getMovedFrom().x;
                gameFieldO[lm.getMovedFrom().y][lm.getMovedFrom().x].getCoordinate().y = lm.getMovedFrom().y;

                gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x] = lm.getTaken();
                if(lm.getTaken() != null){
                    gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x].getCoordinate().x = lm.getMovedTo().x;
                    gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x].getCoordinate().y = lm.getMovedTo().y;
                }

                log.getMoves().remove(lm);
                // check en passant undo
                if(log.lastMove() != null && lm.getTaken() != null && lm.getTaken().getValue() == 1
                        && log.lastMove().getMoved().getValue() == 1
                        && log.lastMove().magnitude() == 2 && lm.getMovedTo().x == log.lastMove().getMovedTo().x){
                    gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x] = null;
                    gameFieldO[log.lastMove().getMovedTo().y][lm.getMovedTo().x] = lm.getTaken();
                    gameFieldO[log.lastMove().getMovedTo().y][lm.getMovedTo().x].getCoordinate().y =
                                                                                        log.lastMove().getMovedTo().y;
                }
                // check castling
                if(gameFieldO[lm.getMovedFrom().y][lm.getMovedFrom().x].getValue() == 100
                        && lm.magnitude() == 2){
                    int kingDirection = (lm.getMovedTo().x - lm.getMovedFrom().x)/2;
                    Piece rook = gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x-kingDirection];
                    int xpos = (kingDirection < 0)?0:7;
                    rook.getCoordinate().x = xpos;
                    gameFieldO[lm.getMovedTo().y][lm.getMovedTo().x-kingDirection] = null;
                    gameFieldO[lm.getMovedTo().y][xpos] = rook;
                }
                changePlayer();
            }

            winner = null;
            displayMessage.setText("UNDONE");
        }
    }

    /**
     * Checks if a pawn promotion has occurred and processes it if needed.
     *
     * @param t The thread responsible for handling the promotion.
     */
    public void checkPromotion(Thread t) {
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(gameFieldO[y][x] != null && gameFieldO[y][x].getValue() == 1 &&
                        ((y == 7 && gameFieldO[y][x].getColor() == Color.BLACK) ||
                                (y == 0 && gameFieldO[y][x].getColor() == Color.WHITE)) ){
                    processPromotion(gameFieldO[y][x], t);
                    return;
                }
            }
        }
    }

    /**
     * Processes the promotion of a pawn and shows the promotion panel.
     *
     * @param piece The pawn piece to be promoted.
     * @param t The thread responsible for handling the promotion.
     */
    private void processPromotion(Piece piece, Thread t) {
        changePlayer();
        interuptedByPromotion=true;
        new PromotionPanel(this, piece, t);
    }

    /**
     * Returns an array of promotion options for the given pawn piece.
     *
     * @param p The pawn piece to be promoted.
     * @return An array of Piece objects representing the promotion options.
     */
    public Piece[] getPromotionalFigures(Piece p) {
        Piece []figures = new Piece[4];
        Coordinate coord = new Coordinate(p.getCoordinate());
        figures[0] = new Queen(coord, p.getColor());
        figures[1] = new Rook(coord, p.getColor());
        figures[2] = new Bishop(coord, p.getColor());
        figures[3] = new Knight(coord, p.getColor());
        return figures;
    }


    /**
     * Promotes the pawn piece to the selected figure and resumes the game.
     *
     * @param figure The selected figure to which the pawn will be promoted.
     * @param t The thread responsible for handling the promotion.
     */
    public void promoteTo(Piece figure, Thread t) {
        gameFieldO[figure.getCoordinate().y][figure.getCoordinate().x] = figure;
        interuptedByPromotion=false;
        changePlayer();
        t.start();
    }

}
