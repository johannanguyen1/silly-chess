package indy;

import javafx.scene.layout.Pane;

/**
 * The King class represents a king chess piece in the Chess game.
 * It extends the abstract Piece class and defines the specific behavior of a king.
 * The King can move one square in any direction and can also perform castling under specific
 * conditions.
 */
public class King extends Piece {
    private Board board;

    /**
     * Constructs a new King object with the specified parameters.
     *
     * @param gamePane The Pane where the Chess game is displayed.
     * @param oldRow   The initial row of the king on the board.
     * @param oldCol   The initial column of the king on the board.
     * @param isWhite  A boolean indicating whether the king is white or black.
     * @param board    The Chess board on which the king is placed.
     */
    public King(Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super(gamePane, oldRow, oldCol, isWhite, board);
        this.board = board;
    }

    /**
     * Takes in the current and intended rows and columns of the piece and checks if
     * the object can move there depending on its specific move rules in chess.
     * The King can move only one square horizontally, vertically, or diagonally.
     * The helper canCastle is called before checking the other requirements since the logic
     * for capture and horizontal movement do not apply to castling cases.
     * Returns true if satisfied.
     */
    @Override
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        MySquare clickedSquare = this.board.getBoard()[newRow][newCol];
        // Castle move validity check
        if (this.canCastle(oldRow, oldCol, newRow, newCol)) {
            return true;
        }
        boolean inbounds =  newRow < Constants.BOARD_WIDTH && newCol < Constants.BOARD_WIDTH;
        boolean vertical = Math.abs(newRow - oldRow) == 1 && newCol == oldCol;
        boolean horizontal = Math.abs(newCol - oldCol) == 1 && newRow == oldRow;
        boolean diagonal = Math.abs(newRow - oldRow) == 1 && Math.abs(newCol - oldCol) == 1;
        boolean capture = this.captureValidity(newRow, newCol);

        return inbounds && capture && (vertical || horizontal || diagonal);
    }

    /**
     * Once in the game, each King is allowed to castle, in which the King moves two
     * squares towards a Rook of the same color and the Rook moves onto the square over
     * which the king crossed. The requirements include: Pieces are a King and a Rook,
     * they are the same color, it is both their first moves, and the King is not in
     * check, and the squares in between the King and Rook are empty. This method
     * returns true if the conditions for castling are met.
     */
    @Override
    public boolean canCastle(int oldRow, int oldCol, int newRow, int newCol) {
        if (!this.board.getBoard()[newRow][newCol].getIsOccupied()) {
            return false;
        }
        Piece rook = this.board.getBoard()[newRow][newCol].getOccupying();
        boolean kAndR = rook.getType().equals("Rook");
        boolean sameColor = this.getColor().equals(rook.getColor());
        boolean first = this.ifFirstMove() && rook.ifFirstMove();
        boolean notInCheck = false;
        if (this.getColor().equals("white")) {
            notInCheck = !this.board.getWInCheck();
        } else {
            notInCheck = !this.board.getBInCheck();
        }
        if (kAndR && sameColor && first && notInCheck) {
            if (Math.min(oldRow, newRow) == oldRow) {
                for (int row = oldRow + 1; row < newRow; row++) {
                    if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                        return false;
                    }
                }
            }
            if (Math.min(oldRow, newRow) == newRow) {
                for (int row = oldRow - 1; row > newRow; row--) {
                    if (this.board.getBoard()[row][oldCol].getIsOccupied()) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a String value of the type of Piece: King.
     */
    @Override
    public String getType() {
        return Constants.KING;
    }

}