package model.piece;


import core.Board;
import model.game.Coordinate;
import model.game.Move;
import model.player.Color;
import java.util.ArrayList;
import java.util.List;


public class King extends Piece{

    /**
     * Creates a new King
     * @param coordinate coordinate of the piece
     * @param color color of the piece
     */
    public King(Coordinate coordinate, Color color) {
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
            int yStep = ((i+1)%2)*(i-1);
            int xStep = (i%2)*(2-i);

            Coordinate stepVector = new Coordinate(xStep, yStep);
            Coordinate iteratedCell = this.coordinate.Add(stepVector);
            Piece figureMet = board.getFigureAt(iteratedCell);

            if(!board.checkOutBounds(iteratedCell)){
                moves.add(new Move(this, figureMet, iteratedCell));
            }

            yStep = i == 0 || i == 3 ? -1 : 1;
            xStep = i > 1 ? -1 : 1;
            stepVector = new Coordinate(xStep, yStep);
            iteratedCell = this.coordinate.Add(stepVector);
            figureMet = board.getFigureAt(iteratedCell);

            if(!board.checkOutBounds(iteratedCell)){
                moves.add(new Move(this, figureMet, iteratedCell));
            }
        }
        // castling: checks if castling is possible for a king
        for(int xDir = -1; xDir < 2; xDir+=2){
            Coordinate stepVector = new Coordinate(xDir, 0);
            Coordinate iteratedCell = this.coordinate.Add(stepVector);
            Piece figureMet = board.getFigureAt(iteratedCell);

            while (!board.checkOutBounds(iteratedCell) && figureMet == null){
                iteratedCell = iteratedCell.Add(stepVector);
                figureMet = board.getFigureAt(iteratedCell);
            }
            if (figureMet instanceof Rook && figureMet.color == this.color &&
                    !((Rook) figureMet).isMoved(board) && !isMoved(board) &&
                    (board.check == null || board.check != color)){
                moves.add(new Move(this, null, this.coordinate.Add(stepVector).Add(stepVector)));
            }
        }

        moves.removeIf(move -> move.getPieceToCapture() != null && move.getPieceToCapture().color == this.color);
        moves.removeIf(move -> isKingAttacked(move,board));

        return moves;
    }

    @Override
    public void makeMove(Move move, Board board){
        // castling: makes rook change it position case king is doing castling
        if( Math.abs(move.getX() - this.getCoordinate().x) == 2 ){
            int xDir = (move.getX() - this.getCoordinate().x)/2;
            Coordinate stepVector = new Coordinate(xDir, 0);
            Coordinate iteratedCell = this.coordinate.Add(stepVector);
            Piece figureMet = board.getFigureAt(iteratedCell);

            while (!board.checkOutBounds(iteratedCell) && figureMet == null){
                iteratedCell = iteratedCell.Add(stepVector);
                figureMet = board.getFigureAt(iteratedCell);
            }
            if (figureMet instanceof Rook){
                board.gameFieldO[this.coordinate.y][this.coordinate.x + xDir] = figureMet;
                board.gameFieldO[figureMet.coordinate.y][figureMet.coordinate.x] = null;
                figureMet.coordinate = new Coordinate(this.coordinate.x + xDir, this.coordinate.y);
            }
        }
        super.makeMove(move, board);
    }

    public boolean isMoved(Board board){
        return board.log.checkFigureMoved(this);
    }


    @Override
    public String getPathToImage(){
        return (color == Color.WHITE)?"/whiteKingPower.png":"/blackKingBar.png";
    }

    @Override
    public int getValue() {
        return 100;
    } // priceless



}
