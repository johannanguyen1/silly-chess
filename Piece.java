package indy;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Abstract class representing a chess piece. Contains general methods defining
 * Piece images, locations, and move validity. Some methods get overridden in the Piece
 * subclasses.
 */
public abstract class Piece {
    private Pane gamePane;
    private boolean isWhite;
    private Image image;
    private ImageView imageView;
    private Board board;
    private boolean isFirstMove;
    private int oldRow;
    private int oldCol;
    private ArrayList<MySquare> validSquares;


    /**
     * Constructs a Piece object with the specified parameters. Instantiates a new ArrayList
     * of valid squares and other instance variables.
     *
     * @param gamePane The pane in which the piece is displayed.
     * @param oldRow   The initial row of the piece.
     * @param oldCol   The initial column of the piece.
     * @param isWhite  {@code true} if the piece is white, {@code false} otherwise.
     * @param board    The chessboard on which the piece is placed.
     */
    public Piece (Pane gamePane, int oldRow, int oldCol, boolean isWhite, Board board) {
        super();
        this.gamePane = gamePane;
        this.isWhite = isWhite;
        this.board = board;
        this.isFirstMove = true;
        this.oldRow = oldRow;
        this.oldCol = oldCol;
        this.validSquares = new ArrayList<>();
    }

    /**
     * Sets the image for the piece by getting the image correlating the color and type of the
     * Piece. Sets height and width.
     */
    public void setImage() {
        this.image = new Image("indy/pieces/" + this.getColor() + this.getType() + ".png", Constants.PIECE_WIDTH, Constants.PIECE_WIDTH, true, true);
        this.imageView = new ImageView(this.image);
        this.imageView.setFitHeight(Constants.SQUARE_WIDTH-10);
        this.imageView.setFitWidth(Constants.SQUARE_WIDTH-10);
        this.imageView.setImage(this.image);
    }

    /**
     * Moves the piece to the specified square by setting the X and Y locations.
     */
    public void movePiece(MySquare clickedSquare) {
        this.setX(clickedSquare.getRow() * Constants.SQUARE_WIDTH);
        this.setY(clickedSquare.getCol() * Constants.SQUARE_WIDTH);
    }

    /**
     * Checks the validity of the move based on the piece's rules. Is overridden in each subclass.
     */
    public boolean moveValidity(int oldRow, int oldCol, int newRow, int newCol) {
        return true;
    }

    /**
     * Sets the valid squares for the piece to move to by checking move validity
     * for each square on the board.
     */
    public void setValidSquares() {
        this.validSquares.clear();
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                if (this.moveValidity(this.oldRow, this.oldCol, i, j)) {
                    MySquare square = this.board.getBoard()[i][j];
                    this.validSquares.add(square);
                }
            }
        }
    }

    /**
     * Checks the validity of capturing a piece at the specified square. The piece's color
     * must not equal the color of the piece occupying the specified square.
     */
    public boolean captureValidity(int newRow, int newCol) {
        boolean check = true;
        if (this.isWhite) {
            if (this.board.getBoard()[newRow][newCol].getIsOccupied() && this.board.getBoard()[newRow][newCol].getOccupying().getColor().equals("white")) {
                check = false;
            }
        }
        else {
            if (this.board.getBoard()[newRow][newCol].getIsOccupied() && this.board.getBoard()[newRow][newCol].getOccupying().getColor().equals("black")) {
                check = false;
            }
        }
        return check;
    }

    /**
     * Checks if the piece is in check and evaluates the move's impact on the check status.
     *
     * @param oldRow The current row of the piece.
     * @param oldCol The current column of the piece.
     * @param newRow The target row of the move.
     * @param newCol The target column of the move.
     * @return {@code true} if the move captures, blocks, or escapes from a check, {@code false} otherwise.
     */

    public boolean whenInCheck(int oldRow, int oldCol, int newRow, int newCol) {
        boolean capture = true;
        boolean block = true;
        boolean escape = true;
        if (this.board.getCheck()) {
            Piece king = this.board.getGame().getCurrKing();
            Piece attacker = this.board.getChecking();
            int kingsRow = king.getOldRow();
            int kingsCol = king.getOldCol();
            String oppPlayer = "white";
            if (this.getColor().equals("white")) {
                oppPlayer = "black";
            }
            if (attacker != null) {
                int attackersRow = attacker.getOldRow();
                int attackersCol = attacker.getOldCol();
                int rowIncrement = Integer.compare(attackersRow, kingsRow);
                int colIncrement = Integer.compare(attackersCol, kingsCol);
                for (int row = attackersRow + rowIncrement, col = attackersCol + colIncrement;
                     row != kingsRow || col != kingsCol;
                     row += rowIncrement, col += colIncrement) {
                    if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                        if (newRow != row || newCol != col) {
                            block = false;
                        }
                    }
                }
                if (!this.moveValidity(oldRow, oldCol, attacker.getOldRow(), attacker.getOldCol())) {
                    capture = false;
                }
                if (!this.board.kingCanEscape(oppPlayer)) {
                    escape = false;
                }
            }
        }
        return capture || block || escape;
    }

    /**
     * Returns true if Piece still has its first move.
     */
    public boolean ifFirstMove() {
        return this.isFirstMove;
    }

    /**
     * Mutator method to switch first move boolean to the boolean given in the argument.
     */
    public void setFirstMove(boolean isFirstMove) {
        this.isFirstMove = isFirstMove;
    }

    /**
     * Returns boolean for castling validity. Overridden in King.
     */
    public boolean canCastle(int oldRow, int oldCol, int newRow, int newCol) {
        return false;
    }

    /**
     * Accessor method for the current row of the piece.
     */
    public int getOldRow() {
        return this.oldRow;
    }

    /**
     * Accessor method for the current column of the piece.
     */
    public int getOldCol() {
        return this.oldCol;
    }

    /**
     * Accessor method for the ArrayList of valid squares for the piece to move to.
     */
    public ArrayList<MySquare> getValidSquares() {
        return this.validSquares;
    }

    /**
     * Returns a square for en passant capture. Overridden in Pawn.
     */
    public MySquare getEPSquare() {
        return null;
    }

    /**
     * Returns a String value of the type of Piece. Overridden in every subclass.
     */
    public String getType() {
        return "";
    }

    /**
     * Adds the piece's image view to the game pane.
     */
    public void addImageViewPane() {
        this.gamePane.getChildren().add(this.imageView);
    }

    /**
     * Removes the piece's image view to the game pane.
     */
    public void removeFromPane() {
        if (this.imageView != null) {
            this.gamePane.getChildren().remove(this.imageView);
        }
    }

    /**
     * Mutator method for the x-coordinate of the piece's image view. Adjusts old row value.
     */
    public void setX(double x) {
        this.imageView.setX(x);
        this.oldRow = (int) (x / Constants.SQUARE_WIDTH);
    }

    /**
     * Mutator method for the y-coordinate of the piece's image view. Adjusts old column value.
     */
    public void setY(double y) {
        this.imageView.setY(y);
        this.oldCol = (int) (y / Constants.SQUARE_WIDTH);
    }

    /**
     * Accessor method for the color of the Piece.
     */
    public String getColor() {
        if (this.isWhite) {
            return Constants.WHITE;
        }
        else {
            return Constants.BLACK;
        }
    }

    /**
     * Mutator method for the color of the Piece depending on its boolean argument.
     */
    public void setColor(boolean isWhite) {
        this.isWhite = isWhite;
    }

    /**
     * Accessor method for the image view of the piece.
     */
    public ImageView getImageView() {
        return this.imageView;
    }
}