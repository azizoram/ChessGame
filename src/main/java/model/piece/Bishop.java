package model.piece;


import core.Board;
import model.game.Coordinate;
import model.game.Move;
import model.player.Color;

import java.util.ArrayList;
import java.util.List;


public class Bishop extends Piece{

    /**
     * Creates a new Bishop
     * @param coordinate coordinate of the piece
     * @param color color of the piece
     */
    public Bishop(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    /**
     * A method to get all the valid moves for a piece
     * @return list containing valid coordinates
     */
    public List<Move> validMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            int yStep = i == 0 || i == 3 ? -1 : 1;
            int xStep = i > 1 ? -1 : 1;
            Coordinate stepVector = new Coordinate(xStep, yStep);
            Coordinate iteratedCell = this.coordinate.Add(stepVector);
            Piece figureMet = board.getFigureAt(iteratedCell);
            while (!board.checkOutBounds(iteratedCell) && figureMet == null){
                moves.add(new Move(this, null, iteratedCell));
                iteratedCell = iteratedCell.Add(stepVector);
                figureMet = board.getFigureAt(iteratedCell);
            }
            if(!board.checkOutBounds(iteratedCell) && figureMet.color != this.color){
                moves.add(new Move(this, figureMet, iteratedCell));
            }
        }
        moves.removeIf(move -> isKingAttacked(move,board));

        return moves;
    }

    @Override
    public String getPathToImage(){
        return (color == Color.WHITE)?"/whiteBishop.png":"/blackBishop.png";
    }

}
