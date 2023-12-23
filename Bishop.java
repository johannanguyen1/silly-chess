package indy;

import javafx.scene.layout.Pane;

/**
 * The Bishop class represents a bishop chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific move behavior of a bishop.
 * The Bishop can move diagonally across the board if the squares between are unoccupied.
 */
public class Bishop extends Piece {
    private Board board;

    /**
     * Constructs a new Bishop object with the specified parameters.
     *
     * @param gamePane The Pane where the game is displayed and the object is graphically added.
     * @param oldRow   The initial row of the Bishop on the board.
     * @param oldCol   The initial column of the Bishop on the board.
     * @param isWhite  A boolean indicating whether the Bishop is white or black.
     * @param board    The Chess board on which the Bishop is placed.
     */
    public Bishop(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
        this.board = board;
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess.
     * Bishops can move in any direction diagonally as long as the squares between
     * the old and new locations are unoccupied. If the target location is occupied,
     * check if the Bishop can capture the occupying piece. Returns true if satisfied.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        boolean check = true;
        if (newRow >= 0 && newRow < Constants.BOARD_WIDTH && newCol >= 0 && newCol < Constants.BOARD_WIDTH) {
            if (Math.abs(newRow - oldRow) != Math.abs(newCol - oldCol)) {
                return false;
            }
            if (Math.min(oldCol, newCol) == oldCol && (Math.min(oldRow, newRow) == oldRow)) {
                int row = oldRow;
                for (int col = oldCol + 1; col < newCol; col++) {
                    row++;
                    if (this.board.getBoard()[row][col].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (Math.min(oldCol, newCol) == newCol && (Math.min(oldRow, newRow) == oldRow)) {
                int row = oldRow;
                for (int col = oldCol - 1; col > newCol; col--) {
                    row++;
                    if (this.board.getBoard()[row][col].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (Math.min(oldCol, newCol) == oldCol && (Math.min(oldRow, newRow) == newRow)) {
                int row = oldRow;
                for (int col = oldCol + 1; col < newCol; col++) {
                    row--;
                    if (this.board.getBoard()[row][col].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (Math.min(oldCol, newCol) == newCol && (Math.min(oldRow, newRow) == newRow)) {
                int row = oldRow;
                for (int col = oldCol - 1; col > newCol; col--) {
                    row--;
                    if (this.board.getBoard()[row][col].getIsOccupied()) {
                        check = false;
                    }
                }
            }
            if (!this.captureValidity(newRow, newCol)) {
                check = false;
            }
        }
        return check;
    }

    /**
     * Returns a String value of the type of Piece: Bishop.
     */
    @Override
    public String getType() {
        return Constants.BISHOP;
    }
}
