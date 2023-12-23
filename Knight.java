package indy;

import javafx.scene.layout.Pane;

/**
 * The Knight class represents a knight chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific move behavior of a Knight.
 * Knights move in L-shaped patterns, making two squares in one direction and one square perpendicular to it.
 */

public class Knight extends Piece {

    /**
     * Constructs a new Knight object with the specified parameters.
     *
     * @param gamePane The Pane where the game is displayed and the object is graphically added.
     * @param oldRow   The initial row of the Knight on the board.
     * @param oldCol   The initial column of the Knight on the board.
     * @param isWhite  A boolean indicating whether the Knight is white or black.
     * @param board    The Chess board on which the Knight is placed.
     */
    public Knight(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess.
     * If the new row is two rows away from the current row, its new column must be one away, and
     * vice versa. Returns true if satisfied.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        boolean check = true;
        if (newRow >= 0 && newRow < Constants.BOARD_WIDTH && newCol >= 0 && newCol < Constants.BOARD_WIDTH) {
            if ((Math.abs(newRow - oldRow) != 2 || Math.abs(newCol - oldCol) != 1) && (Math.abs(newRow - oldRow) != 1 || Math.abs(newCol - oldCol) != 2)) {
                check = false;
            }
            if (!this.captureValidity(newRow, newCol)) {
                check = false;
            }
        }
        return check;
    }

    /**
     * Returns a String value of the type of Piece: Knight.
     */
    @Override
    public String getType() {
        return Constants.KNIGHT;
    }
}
