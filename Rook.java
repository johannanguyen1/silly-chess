package indy;

import javafx.scene.layout.Pane;

/**
 * The Rook class represents a rook chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific move behavior of a Rook.
 * Rooks move in vertical or horizontal directions if the squares between are unoccupied.
 * */
public class Rook extends Piece {
    private Board board;

    /**
     * Constructs a new Rook object with the specified parameters.
     *
     * @param gamePane The Pane where the game is displayed and the object is graphically added.
     * @param oldRow   The initial row of the Knight on the board.
     * @param oldCol   The initial column of the Knight on the board.
     * @param isWhite  A boolean indicating whether the Knight is white or black.
     * @param board    The Chess board on which the Knight is placed.
     */
    public Rook(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
        this.board = board;
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess.
     * First, checks that the new location is within the board bounds and is a straight move.
     * Then, if the path between the new and old location is occupied, returns false.
     * Returns true if satisfied conditions.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        boolean check = true;
        boolean inBounds = newRow >= 0 && newRow < Constants.BOARD_WIDTH && newCol >= 0 && newCol < Constants.BOARD_WIDTH;

        if ((oldRow != newRow || oldCol == newCol) && (oldRow == newRow || oldCol != newCol)) {
            check = false;
        }

        if (oldRow == newRow) {
            if (Math.min(oldCol, newCol) == oldCol) {
                for (int col = oldCol + 1; col < newCol; col++) {
                    if (this.board.getBoard()[oldRow][col].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (Math.min(oldCol, newCol) == newCol) {
                for (int col = oldCol - 1; col > newCol; col--) {
                    if (this.board.getBoard()[oldRow][col].getIsOccupied()) {
                        check = false;
                    }
                }

            }
        }
        if (oldCol == newCol) {
            if (Math.min(oldRow, newRow) == oldRow) {
                for (int row = oldRow + 1; row < newRow; row++) {
                    if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (Math.min(oldRow, newRow) == newRow) {
                for (int row = oldRow - 1; row > newRow; row--) {
                    if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                        check = false;
                    }
                }
            }
        }
        if (!this.captureValidity(newRow, newCol)) {
            check = false;
        }
        return inBounds && check;
    }

    /**
     * Returns a String value of the type of Piece: Rook.
     */
    @Override
    public String getType() {
        return Constants.ROOK;
    }
}
