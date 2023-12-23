package indy;

import javafx.scene.layout.Pane;

/**
 * The Queen class represents a queen chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific move behavior of a Queen.
 * Queens move any number of square horizontally, vertically, and diagonally as long as
 * the squares between its target and its original position are unoccupied.
 */
public class Queen extends Piece {
    private Board board;


    /**
     * Constructs a new Queen object with the specified parameters.
     *
     * @param gamePane The Pane where the game is displayed and the object is graphically added.
     * @param oldRow   The initial row of the Knight on the board.
     * @param oldCol   The initial column of the Knight on the board.
     * @param isWhite  A boolean indicating whether the Knight is white or black.
     * @param board    The Chess board on which the Knight is placed.
     */
    public Queen(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
        this.board = board;
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess.
     * This method checks that the new row and new column of the move is a straight or
     * diagonal move that is not blocked by any pieces. Returns true if satisfied.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        boolean straight = (oldRow == newRow && oldCol != newCol) || (oldRow != newRow && oldCol == newCol);
        boolean diagonal = Math.abs(newRow - oldRow) == Math.abs(newCol - oldCol);
        if (newRow >= 0 && newRow < Constants.BOARD_WIDTH && newCol >= 0 && newCol < Constants.BOARD_WIDTH) {
            if (straight) {
                if (oldRow == newRow) {
                    if (Math.min(oldCol, newCol) == oldCol) {
                        for (int col = oldCol + 1; col < newCol; col++) {
                            if (this.board.getBoard()[oldRow][col].getIsOccupied()) {
                                straight = false;
                            }
                        }
                    }
                    if (Math.min(oldCol, newCol) == newCol) {
                        for (int col = oldCol - 1; col > newCol; col--) {
                            if (this.board.getBoard()[oldRow][col].getIsOccupied()) {
                                straight = false;
                            }
                        }
                    }
                }
                if (oldCol == newCol) {
                    if (Math.min(oldRow, newRow) == oldRow) {
                        for (int row = oldRow + 1; row < newRow; row++) {
                            if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                                straight = false;
                            }
                        }
                    }
                    if (Math.min(oldRow, newRow) == newRow) {
                        for (int row = oldRow - 1; row > newRow; row--) {
                            if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                                straight = false;
                            }
                        }
                    }
                }
            }
            if (diagonal) {
                if (Math.min(oldCol, newCol) == oldCol && (Math.min(oldRow, newRow) == oldRow)) {
                    int row = oldRow;
                    for (int col = oldCol + 1; col < newCol; col++) {
                        row++;
                        if (this.board.getBoard()[row][col].getIsOccupied()) {
                            diagonal = false;
                        }
                    }
                }
                if (Math.min(oldCol, newCol) == newCol && (Math.min(oldRow, newRow) == oldRow)) {
                    int row = oldRow;
                    for (int col = oldCol - 1; col > newCol; col--) {
                        row++;
                        if (this.board.getBoard()[row][col].getIsOccupied()) {
                            diagonal = false;
                        }
                    }
                }
                if (Math.min(oldCol, newCol) == oldCol && (Math.min(oldRow, newRow) == newRow)) {
                    int row = oldRow;
                    for (int col = oldCol + 1; col < newCol; col++) {
                        row--;
                        if (this.board.getBoard()[row][col].getIsOccupied()) {
                            diagonal = false;
                        }
                    }
                }
                if (Math.min(oldCol, newCol) == newCol && (Math.min(oldRow, newRow) == newRow)) {
                    int row = oldRow;
                    for (int col = oldCol - 1; col > newCol; col--) {
                        row--;
                        if (this.board.getBoard()[row][col].getIsOccupied()) {
                            diagonal = false;
                        }
                    }
                }
            }
        }
        return this.captureValidity(newRow, newCol) && (diagonal || straight);
    }

    /**
     * Returns a String value of the type of Piece: Queen.
     */
    @Override
    public String getType() {
        return Constants.QUEEN;
    }
}