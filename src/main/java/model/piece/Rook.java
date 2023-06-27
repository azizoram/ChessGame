package model.piece;


import core.Board;
import model.game.Coordinate;
import model.game.Move;
import model.player.Color;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Piece {

    /**
     * Creates a new Rook
     * @param coordinate coordinate of the piece
     * @param color color of the piece
     */
    public Rook(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    /**
     * A method to get all the valid moves for a piece
     * @return list containing valid coordinates
     */
    // TODO consider DRYed up Queen movement
    public List<Move> validMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            int yStep = ((i+1)%2)*(i-1);
            int xStep = (i%2)*(2-i);

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
    public void makeMove(Move move, Board board){
        super.makeMove(move, board);
    }

    public boolean isMoved(Board board){
        return board.log.checkFigureMoved(this);
    }

    @Override
    public String getPathToImage(){
        return (color == Color.WHITE)?"/whiteTower.png":"/blackTower.png";
    }

    @Override
    public int getValue() {
        return 5;
    }
}
