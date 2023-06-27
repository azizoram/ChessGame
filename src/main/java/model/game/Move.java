package model.game;


import model.piece.Piece;

/**
 * Move class represents a move action in a chess game, including the moved piece, the captured piece (if any),
 * and the destination of the move.
 */
public class Move {
    private Piece pieceToMove;
    private Piece pieceToCapture;
    private Coordinate movedTo;

    /**
     * Constructor for creating a new Move object.
     *
     * @param pieceToMove The piece to move.
     * @param pieceToCapture The piece to capture, or null if none.
     * @param movedTo The destination coordinate of the move.
     */
    public Move(Piece pieceToMove, Piece pieceToCapture, Coordinate movedTo) {
        this.pieceToMove = pieceToMove;
        this.pieceToCapture = pieceToCapture;
        this.movedTo = movedTo;
    }

    /**
     * Returns the moved piece.
     *
     * @return The piece being moved.
     */
    public Piece getPieceToMove() {
        return pieceToMove;
    }

    /**
     * Returns the captured piece.
     *
     * @return The piece being captured, or null if none.
     */
    public Piece getPieceToCapture() {
        return pieceToCapture;
    }

    /**
     * Returns the destination coordinate of the move.
     *
     * @return The destination coordinate of the move.
     */
    public Coordinate getMovedTo() {
        return new Coordinate(movedTo);
    }

    /**
     * Returns the starting coordinate of the move.
     *
     * @return The starting coordinate of the move.
     */
    public Coordinate getMovedFrom(){ return pieceToMove.getCoordinate();}

    /**
     * Returns the x coordinate of the destination of the move.
     *
     * @return The x coordinate of the destination.
     */
    public int getX(){ return movedTo.x;}

    /**
     * Returns the y coordinate of the destination of the move.
     *
     * @return The y coordinate of the destination.
     */
    public int getY(){ return movedTo.y;}

    /**
     * Checks if the move is valid.
     *
     * @return True if the move is valid, false otherwise.
     */
    public boolean isValid() {
        return !(movedTo.x == pieceToMove.getCoordinate().x && movedTo.y == pieceToMove.getCoordinate().y);
    }

    /**
     * Sets the captured piece for the move.
     *
     * @param piece The captured piece.
     */
    public void setPieceToCapture(Piece piece) {
        pieceToCapture = piece;
    }
}
