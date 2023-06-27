package model.piece;

import model.game.Coordinate;
import model.game.Move;
import model.player.Color;
import core.Board;
import java.util.List;

/**
 * A generic Piece class. Other pieces inherit this class
 */
public abstract class Piece {
    public char literal;
    protected Coordinate coordinate;
    protected Color color;

    /**
     * Gets the color of this piece
     * @return object representing the piece's color
     */
    public Color getColor() {
        return this.color;
    }

    /**
     * Returns the coordinate of the piece
     * @return the coordinate of the piece
     */
    public Coordinate getCoordinate() {
        return this.coordinate;
    }

    public int getValue() {
        return 0;
    }

    /**
     * Moves the piece to a new coordinate
     * @param c the coordinate to moved to
     */
    public void movedTo(Coordinate c) {
        this.coordinate = c;
    }

    /**
     * Gets a list of the valid piece's moves
     * @return valid moves
     */
    public abstract List<Move> validMoves(Board board);

    /**
     * Makes a move on the board
     * @param move the move to be made
     * @param board the board on which the move is made
     */
    public void makeMove(Move move, Board board) {
        if(!move.isValid()){
            return;
        }

        board.log.logMove(move);
        if(move.getPieceToCapture() != null){
            board.gameFieldO[move.getPieceToCapture().getCoordinate().getY()]
                    [move.getPieceToCapture().getCoordinate().getX()] = null;
        }
        board.gameFieldO[move.getMovedTo().y][move.getMovedTo().x] = this;
        board.gameFieldO[this.coordinate.y][this.coordinate.x] = null;

        this.coordinate = move.getMovedTo();

        checkCheck(new Move(null, null, new Coordinate(-1,-1)), board);
        board.changePlayer();
        board.activeFigure = null;
    }

    /**
     * Checks if the move is valid
     * @param move the move to be checked
     * @param board the board on which the move is made
     * @return true if the move is valid, false otherwise
     */
    private void checkCheck(Move move, Board board) {
        board.check = null;
        board.isWhiteTurn = !board.isWhiteTurn;
        if(this.getColor() == board.whiteKing.getColor()){
            if(board.blackKing.isKingAttacked(move, board)){
                System.out.println("Black king attacked");
                board.check = board.blackKing.color;
                if(checkMate(board, board.blackKing.color)){
                    board.setWinner(true, "CHECKMATE FOR BLACKS!");
                }
            }
        }else{
            if(board.blackKing.isKingAttacked(move, board)){
                System.out.println("White king attacked");
                board.check = board.whiteKing.color;

                if(checkMate(board, board.whiteKing.color)){
                    board.setWinner(false, "CHECKMATE FOR WHITES!");
                }
            }
        }
        board.isWhiteTurn = !board.isWhiteTurn;

    }

    /**
     * Checks if the player of the given color is in checkmate on the board.
     * @param board The game board.
     * @param color The color of the player to check for checkmate.
     * @return `true` if the player is in checkmate, `false` otherwise.
     */
    private boolean checkMate(Board board, Color color) {
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if(board.gameFieldO[i][j] != null && board.gameFieldO[i][j].getColor() == color
                        && board.gameFieldO[i][j].validMoves(board).size() > 0){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the specified move is out of bounds on the game board.
     * @param move The move to check.
     * @return `true` if the move is out of bounds, `false` otherwise.
     */
    public boolean checkOutBounds(Move move) {
        return move.getX()>7 || move.getX()<0 || move.getY()>7 || move.getY()<0;
    }

    /**
     * Gets the path to the image file representing the piece.
     * @return The path to the image file.
     */
    public String getPathToImage(){
        return "/kongokingo.png";
    }

    /**
     * Checks if the king of the current player is under attack after making the specified move on the board.
     * @param move The move to check.
     * @param board The game board.
     * @return `true` if the king is attacked after the move, `false` otherwise.
     */
    public boolean isKingAttacked(Move move, Board board){
        Board tempBoard = new Board(board);

        if(move.getPieceToCapture() != null){
            tempBoard.gameFieldO[move.getPieceToCapture().getCoordinate().getY()]
                    [move.getPieceToCapture().getCoordinate().getX()] = null;
        }
        Coordinate kingCo = (tempBoard.isWhiteTurn ? tempBoard.whiteKing:tempBoard.blackKing).coordinate;

        if(!checkOutBounds(move)) {
            tempBoard.gameFieldO[move.getMovedTo().y][move.getMovedTo().x] = move.getPieceToMove();
            tempBoard.gameFieldO[move.getPieceToMove().coordinate.y][move.getPieceToMove().coordinate.x] = null;

            if(move.getPieceToMove().coordinate.equals(kingCo)){
                kingCo = move.getMovedTo();
            }
        }

        Color enemyColor = tempBoard.isWhiteTurn?Color.BLACK:Color.WHITE;

        for(int i = 0; i < 4; i++){
            int yStep = ((i+1)%2)*(i-1);
            int xStep = (i%2)*(2-i);
            int distance = 1;

            Coordinate stepVector = new Coordinate(xStep, yStep);
            Coordinate iteratedCell = kingCo.Add(stepVector);
            Piece figureMet = tempBoard.getFigureAt(iteratedCell);
            while (!tempBoard.checkOutBounds(iteratedCell) && figureMet == null){
                iteratedCell = iteratedCell.Add(stepVector);
                figureMet = tempBoard.getFigureAt(iteratedCell);
                distance++;
            }

            if(!tempBoard.checkOutBounds(iteratedCell) && figureMet.color == enemyColor){
                if(figureMet instanceof Queen || figureMet instanceof Rook){
                    return true;
                }
                if(distance == 1 && figureMet instanceof King){
                    return true;
                }
            }

            yStep = i == 0 || i == 3 ? -1 : 1;
            xStep = i > 1 ? -1 : 1;
            stepVector = new Coordinate(xStep, yStep);
            iteratedCell = kingCo.Add(stepVector);
            figureMet = tempBoard.getFigureAt(iteratedCell);
            distance = 1;
            while (!tempBoard.checkOutBounds(iteratedCell) && figureMet == null){
                iteratedCell = iteratedCell.Add(stepVector);
                figureMet = tempBoard.getFigureAt(iteratedCell);
                distance++;
            }
            if(!tempBoard.checkOutBounds(iteratedCell) && figureMet.color == enemyColor){
                if(figureMet instanceof Bishop || figureMet instanceof Queen){
                    return true;
                }
                if(distance == 1 && figureMet instanceof King){
                    return true;
                }
                if(distance==1 && (yStep==(tempBoard.isWhiteTurn?-1:1)) && figureMet instanceof Pawn){
                    return true;
                }
            }
        }

        for (int i = 0; i < 4; i++){
            for(int j = -1; j < 2; j=j+2){
                Coordinate movedTo = new Coordinate(kingCo.getCoordinate().x + (2-i)*(i%2)*2 + j*((i+1)%2)    ,
                        kingCo.getCoordinate().y + (i%2)*j + (1-i)*2*((i+1)%2) );
                Piece figureMet = tempBoard.getFigureAt(movedTo);
                if(!tempBoard.checkOutBounds(movedTo) && figureMet instanceof Knight && figureMet.color == enemyColor ){
                    return true;
                }
            }
        }

        return false;
    }
}
