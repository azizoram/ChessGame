package model.piece;


import core.Board;
import model.game.Coordinate;
import model.game.Move;
import model.player.Color;
import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    /**
     * Creates a new Pawn
     * @param coordinate coordinate of the piece
     * @param color color of the piece
     */
    public Pawn(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    /**
     * A method to get all the valid moves for a piece
     * @return list containing valid coordinates
     */
    public List<Move> validMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        int yDir, jumpCoord;
        yDir = (color == Color.BLACK) ? 1 : -1;
        jumpCoord = (color == Color.BLACK) ? 1 : 6;

        moves.add(new Move(this, null,
                new Coordinate(getCoordinate().x, getCoordinate().y + yDir)));
        if(board.gameFieldO[moves.get(0).getY()][moves.get(0).getX()] == null && coordinate.y == jumpCoord){
            moves.add(new Move(this, null,
                    new Coordinate(getCoordinate().x, getCoordinate().y + 2*yDir)));
        }
        moves.removeIf(this::checkOutBounds);
        moves.removeIf(move -> board.gameFieldO[move.getY()][move.getX()] != null);
        for(int i = 0; i < 2; i++) {
            Move taken = new Move(this, null,
                    new Coordinate(coordinate.x - 1 + i * 2, coordinate.y + yDir));
            if (!checkOutBounds(taken) && board.gameFieldO[taken.getY()][taken.getX()] != null
                    && board.gameFieldO[taken.getY()][taken.getX()].color != color) {
                taken.setPieceToCapture(board.gameFieldO[taken.getY()][taken.getX()]);
                moves.add(taken);
            }
            taken = new Move(this, null,
                    new Coordinate(coordinate.x - 1 + i * 2, coordinate.y));
            if(!checkOutBounds(taken) && board.log.getMoves().size() > 0
                    && board.log.lastMove().getMovedTo().equals(taken.getMovedTo())
                    && board.gameFieldO[taken.getY()][taken.getX()] instanceof Pawn
                    && board.log.lastMove().magnitude() == 2
            ){
                taken = new Move(this, board.gameFieldO[taken.getY()][taken.getX()],
                        new Coordinate(coordinate.x- 1 + i * 2, coordinate.y + yDir));
                moves.add(taken);
            }

        }

        moves.removeIf(move -> isKingAttacked(move,board));

        return moves;
    }


    @Override
    public String getPathToImage() {
        return (color == Color.WHITE)? "/whitePawn.png": "/blackPawn.png";
    }

    @Override
    public int getValue() {
        return 1;
    }
}
