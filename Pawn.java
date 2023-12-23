package indy;

import javafx.scene.layout.Pane;

/**
 * The Pawn class represents a pawn chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific move behavior of a Pawn.
 * Pawns move in towards its opponent's side by one or two squares. It may move two squares if
 * it is its first move. Pawns have a diagonal capturing mechanism, and they can perform
 * an en passant capture under specific conditions.
 */
public class Pawn extends Piece {
    private boolean isWhite;
    private Board board;
    private MySquare EPSquare;

    /**
     * Constructs a new Pawn object with the specified parameters. The constructor also defines
     * instance variables for en passant.
     *
     * @param gamePane The Pane where the game is displayed and the object is graphically added.
     * @param oldRow   The initial row of the Knight on the board.
     * @param oldCol   The initial column of the Knight on the board.
     * @param isWhite  A boolean indicating whether the Knight is white or black.
     * @param board    The Chess board on which the Knight is placed.
     */
    public Pawn(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
        this.isWhite = isWhite;
        this.board = board;
        this.EPSquare = null;
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess. First, moveValidity
     * defines the direction of the Pawn depending on its color. If the Pawn moves forward 2 squares,
     * it saves the square between its old and new location as an en passant square, which will be
     * used to determine if the move from the opponent is an en passant capture.
     * Returns true if satisfied.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        boolean inbounds = newRow >= 0 && newRow < Constants.BOARD_WIDTH && newCol >= 0 && newCol < Constants.BOARD_WIDTH;
        boolean check = false;
        int direction = 1;
        if (this.isWhite) {
            direction = -1;
        }

        if (inbounds) {
            if (newRow == oldRow && newCol == oldCol + direction) {
                check = !this.board.getBoard()[newRow][newCol].getIsOccupied();
            }
            if (!check && this.ifFirstMove() && newRow == oldRow && newCol == oldCol + 2 * direction) {
                check = !this.board.getBoard()[newRow][newCol].getIsOccupied() &&
                        !this.board.getBoard()[newRow][newCol - direction].getIsOccupied();

                this.EPSquare = this.board.getBoard()[oldRow][oldCol + direction];
            }
            if (newCol == oldCol + direction && Math.abs(newCol - oldCol) == 1 && Math.abs(newRow - oldRow) == 1) {
                if (((this.board.getBoard()[newRow][newCol].getIsOccupied() && this.captureValidity(newRow, newCol)) ||
                        (this.board.getGame().getPrevSelectedPiece() != null &&
                        (this.board.getGame().getPrevSelectedPiece().getEPSquare() != null) &&
                        this.board.getGame().getPrevSelectedPiece().getEPSquare().equals(this.board.getBoard()[newRow][newCol])))) {
                    check = true;
                }
            }

        }
        return check;
    }

    /**
     * When the opponent's Pawn moves to the en passant square in the next move,
     * the Pawn of the en passant square is captured. This accessor method gets
     * the en passant square.
     * */
    public MySquare getEPSquare() {
        if (this.EPSquare != null) {
            return this.EPSquare;
        }
        return null;
    }

    /**
     * Returns a String value of the type of Piece: Pawn.
     */
    @Override
    public String getType() {
        return Constants.PAWN;
    }
}
