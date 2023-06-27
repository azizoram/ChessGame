package model.game;


import model.piece.Piece;
import java.util.Optional;

/**
 * LogMove class represents a move action in a chess game, including the starting and ending positions,
 * the moved piece, and the captured piece if applicable.
 */
public class LogMove {
    private Coordinate from, to;
    private Piece taken, moved;

    public boolean isTechnical = false;

    /**
     * Constructor for creating a LogMove instance with given coordinates and an optional captured piece.
     *
     * @param from The starting coordinate of the move.
     * @param to The ending coordinate of the move.
     * @param taken The optional captured piece.
     */
    LogMove(Coordinate from, Coordinate to, Optional<Piece> taken){
        this.taken = taken.orElse(null);
        this.from = new Coordinate(from);
        this.to = new Coordinate(to);
    }

    /**
     * Constructor for creating a LogMove instance from a Move object.
     *
     * @param move The Move object to be used for creating the LogMove.
     */
    public LogMove(Move move){
        this(move.getMovedFrom(), move.getMovedTo(), Optional.ofNullable(move.getPieceToCapture()));
        this.moved = move.getPieceToMove();
    }

    /**
     * Returns a string representation of the LogMove.
     *
     * @return A string representing the move, including starting and ending positions.
     */
    public String toString(){
        StringBuilder sb = new StringBuilder("");
        sb.append(shiftSymbolByInt('A', from.getX()));
        sb.append(shiftSymbolByInt('1', (7-from.getY())));
        sb.append("     -     ");
        sb.append(shiftSymbolByInt('A', to.getX()));
        sb.append(shiftSymbolByInt('1', (7-to.getY())));

        return sb.toString();
    }

    /**
     * Calculates the magnitude of the move, which is useful for en passant and castling conditions.
     *
     * @return The integer value representing the magnitude of the move.
     */
    public int magnitude(){
        return (int) Math.sqrt((double) (this.from.y - this.to.y)*(this.from.y - this.to.y) + (this.from.x - this.to.x)*(this.from.x - this.to.x));
    }
    private char shiftSymbolByInt(char sym, int shift) {
        return (char) ((int) sym + shift);
    }

    /**
     * Returns the ending coordinate of the move.
     *
     * @return The ending coordinate.
     */
    public Coordinate getMovedTo() {
        return to;
    }

    /**
     * Returns the starting coordinate of the move.
     *
     * @return The starting coordinate.
     */
    public Coordinate getMovedFrom() {
        return from;
    }

    /**
     * Returns the captured piece, if any.
     *
     * @return The captured piece or null if no piece was captured.
     */
    public Piece getTaken() {
        return taken;
    }

    /**
     * Returns the moved piece.
     *
     * @return The moved piece.
     */
    public Piece getMoved() {
        return moved;
    }
}
