package core;


import model.game.LogMove;
import model.game.Move;
import model.piece.Piece;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * ChessLog class to maintain the log of moves and taken pieces in a Chess game.
 */
public class ChessLog {
    public Board board;
    private List<LogMove> moves;

    /**
     * Initializes an empty ChessLog.
     */
    ChessLog(){
        moves = new LinkedList<LogMove>();
    }

    /**
     * Returns the list of moves in the game.
     *
     * @return A List of LogMove instances.
     */
    public List<LogMove> getMoves(){
        return moves;
    }

    /**
     * Adds a move to the log.
     *
     * @param move The Move instance to be logged.
     */
    public void logMove(Move move){
        moves.add(new LogMove(move));
    };

    /**
     * Returns the last move in the game.
     *
     * @return The last LogMove instance if there are moves, null otherwise.
     */
    public LogMove lastMove() {
        if(moves.size() > 0) {
            return moves.get(moves.size() - 1);
        }else{
            return null;
        }
    }

    /**
     * Returns the list of taken pieces in the game.
     *
     * @return An ArrayList of taken Piece instances.
     */
    public ArrayList<Piece> getTaken() {
        ArrayList<Piece> taken = new ArrayList<>();
        for (LogMove move : moves){
            if(move.getTaken() != null){
                taken.add(move.getTaken());
            }
        }
        return taken;
    }

    /**
     * Logs a move with a specified technical flag.
     *
     * @param move         The Move instance to be logged.
     * @param isTechnical  True if the move is a technical move, false otherwise.
     */
    public void logMove(Move move, boolean isTechnical) {
        LogMove logmove = new LogMove(move);
        logmove.isTechnical = isTechnical;
        moves.add(logmove);
    }

    /**
     * Checks if the given piece has been moved in the game.
     *
     * @param piece The Piece instance to check.
     * @return True if the piece has been moved, false otherwise.
     */
    public boolean checkFigureMoved(Piece piece) {
        boolean returnValue = false;
        for(LogMove lm : moves){
            returnValue = lm.getMoved() == piece;
            if(returnValue) break;
        }
        return returnValue;
    }
}
