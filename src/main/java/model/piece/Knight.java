package model.piece;


import core.Board;
import model.game.Coordinate;
import model.game.Move;
import model.player.Color;
import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    /**
     * Creates a new King
     * @param coordinate coordinate of the piece
     * @param color color of the piece
     */
    public Knight(Coordinate coordinate, Color color) {
        this.coordinate = coordinate;
        this.color = color;
    }

    /**
     * A method to get all the valid moves for a piece
     * @return list containing valid coordinates
     */
    public List<Move> validMoves(Board board) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            for(int j = -1; j < 2; j=j+2){
                Coordinate movedTo = new Coordinate(getCoordinate().x + (2-i)*(i%2)*2 + j*((i+1)%2),
                        getCoordinate().y + (i%2)*j + (1-i)*2*((i+1)%2) );
                moves.add(new Move(this, board.getFigureAt(movedTo),
                        movedTo));
            }
        }
        moves.removeIf(move -> checkOutBounds(move)
                ||
                move.getPieceToCapture() != null &&
                        move.getPieceToCapture().color == this.color);

        moves.removeIf(move -> isKingAttacked(move,board));

        return moves;
    }

    @Override
    public String getPathToImage(){
        return (color == Color.WHITE)?"/dragonKnight.png":"/kazakhshorse.png";
    }

    @Override
    public int getValue() {
        return 3;
    }
}
