package indy;

import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * The Board class represents the chessboard in the Chess game. It contains the squares,
 * pieces, and methods to manage and update the state of the game board.
 */
public class Board {
    private MySquare[][] board;
    private Pane gamePane;
    private Game game;
    private LinkedList<Piece> whitePieces;
    private LinkedList<Piece> blackPieces;
    private boolean wInCheck;
    private boolean bInCheck;
    private Piece checking;
    private int numCheck;

    /**
     * Constructs a new Board object with the specified game pane and game. Calls helper
     * methods to set up the squares of the board and to set up the pieces of both colors.
     *
     * @param gamePane The Pane where the Chess game is displayed and the squares and pieces
     *                 are graphically added.
     * @param game     The Chess game associated with the board.
     */
    public Board (Pane gamePane, Game game) {
        this.gamePane = gamePane;
        this.board = new MySquare[Constants.BOARD_WIDTH][Constants.BOARD_WIDTH];
        this.whitePieces = new LinkedList<>();
        this.blackPieces = new LinkedList<>();
        this.game = game;
        this.wInCheck = false;
        this.bInCheck = false;
        this.checking = null;
        this.numCheck = 0;
        this.setupBoardLayout();
        this.setupWhitePieces();
        this.setupBlackPieces();

    }

    /**
     * Takes in an argument of the Piece to logically remove. Searches through
     * the player's LinkedList of Pieces depending on the color. Uses an
     * iterator to iterate through each Piece in the list until the iteration equals
     * the argument. Removes the Piece logically from the LinkedList.
     */
    public void removePieceLogically(Piece captured) {
        if (captured != null) {
            if (captured.getColor().equals(Constants.WHITE)) {
                Iterator<Piece> iterator = this.whitePieces.iterator();
                while (iterator.hasNext()) {
                    Piece piece = iterator.next();
                    if (piece == captured) {
                        iterator.remove();
                        break;
                    }
                }
            } else {
                Iterator<Piece> iterator = this.blackPieces.iterator();
                while (iterator.hasNext()) {
                    Piece piece = iterator.next();
                    if (piece == captured) {
                        iterator.remove();
                        break;
                    }
                }
            }
        }
    }

    /**
     * Adds the argument of the method into the LinkedList of the player's Pieces.
     */
    public void addPieceLogically(Piece added) {
        if (added.getColor().equals(Constants.WHITE)) {
            this.whitePieces.add(added);
        }
        else {
            this.blackPieces.add(added);
        }
    }

    /**
     * This checks if the opposite color of the passed argument is in check. By looping
     * through each Piece of the player and each valid square of each Piece, this method
     * returns true if the valid squares of the Pieces are located where the opposite
     * King is located.
     */
    public boolean isInCheck(String player) {
        this.wInCheck = false;
        this.bInCheck = false;
        this.numCheck = 0;
        this.checking = null;
        LinkedList<Piece> currentPlayerPieces = this.getPieces(player);
        Piece king = null;
        if (player.equals(Constants.WHITE)) {
            king = this.game.getBlackKing();
        }
        else {
            king = this.game.getWhiteKing();
        }
        for (Piece piece : currentPlayerPieces) {
            piece.setValidSquares();
            ArrayList<MySquare> validSquares = piece.getValidSquares();

            for (MySquare square : validSquares) {
                int newRow = square.getRow();
                int newCol = square.getCol();
                if (newCol == king.getOldCol() && newRow == king.getOldRow()) {
                    if (player.equals(Constants.WHITE)) {
                        this.bInCheck = true;
                    }
                    else {
                        this.wInCheck = true;
                    }
                    this.checking = piece;
                    this.numCheck++;
                }
            }
        }
        return (this.bInCheck || this.wInCheck);
    }


    /**
     * Checks if the current state of the game is checkmate for the specified player.
     * If the state is not already check, there can be no checkmate. If there is
     * a double check by two attackers, the only valid move is to move the King.
     * If it is a single check, check if the King can escape the attacker, the
     * attacker can be captured, or the attacker can be blocked. This depends on the
     * type of Piece the attacker is.
     */
    public boolean isCheckmate(String player) {
        if (!this.getCheck()) {
            return false;
        }
        if (this.doubleCheck()) {
            return !this.kingCanEscape(player);
        }
        if (this.singleCheck()) {

            if (this.checking.getType().equals(Constants.PAWN) || this.checking.getType().equals(Constants.KNIGHT)) {
                return !this.kingCanEscape(player) && !this.canCaptureAttacker(player);
            }

            if (this.checking.getType().equals(Constants.ROOK) || this.checking.getType().equals(Constants.BISHOP)
                    || (this.checking.getType().equals(Constants.QUEEN))) {
                return !this.kingCanEscape(player) && !this.canCaptureAttacker(player) && !this.canBlockAttacker(player);
            }

            if (this.checking.getType().equals(Constants.KING)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is a double check on the board.
     */
    private boolean doubleCheck() {
        return this.numCheck > 1;
    }

    /**
     * Checks if there is a single check on the board.
     */
    private boolean singleCheck() {
        return this.numCheck == 1;
    }


    /**
     * Checks if the king can escape from the check by moving to a valid square.
     *
     */
    public boolean kingCanEscape(String player) {
        Piece king = null;
        if (player.equals(Constants.WHITE)) {
            king = this.game.getBlackKing();
        }
        else {
            king = this.game.getWhiteKing();
        }
        king.setValidSquares();
        ArrayList<MySquare> kSquares = king.getValidSquares();
        for (MySquare square : kSquares) {
            if (this.game.kingMoveValid(player, king, square)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if any piece of the opponent can capture the attacking piece.
     * By looping through each Piece of the player of the argument and each valid square of each
     * Piece, this method returns true if the valid squares of the Pieces are located where the
     * attacker is located.
     */
    private boolean canCaptureAttacker(String player) {
        LinkedList<Piece> oppPieces = null;
        if (player.equals(Constants.WHITE)) {
            oppPieces = this.whitePieces;
        }
        else {
            oppPieces = this.blackPieces;
        }
        for (Piece piece : oppPieces) {
            piece.setValidSquares();
            ArrayList<MySquare> validSquares = new ArrayList<>(piece.getValidSquares());
            for (MySquare square : validSquares) {
                int newRow = square.getRow();
                int newCol = square.getCol();

                if (newRow == this.checking.getOldRow() && newCol == this.checking.getOldCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if any piece of the opponent can block the attacking piece.
     * By looping through each Piece of the player of the argument and each valid square of each
     * Piece, this method returns true if the valid squares of the Pieces are located between the
     * attacker and the King.
     */
    private boolean canBlockAttacker(String player) {
        int attackersRow = this.checking.getOldRow();
        int attackersCol = this.checking.getOldCol();
        Piece king = null;
        if (player.equals(Constants.WHITE)) {
            king = this.game.getWhiteKing();
        }
        else {
            king = this.game.getBlackKing();
        }

        int kingsRow = king.getOldRow();
        int kingsCol = king.getOldCol();

        LinkedList<Piece> oppPieces = null;
        if (player.equals(Constants.WHITE)) {
            oppPieces = this.whitePieces;
        }
        else {
            oppPieces = this.blackPieces;
        }

        for (Piece piece : oppPieces) {
            piece.setValidSquares();
            ArrayList<MySquare> validSquares = new ArrayList<>(piece.getValidSquares());
            // Try each valid move
            for (MySquare square : validSquares) {
                int newRow = square.getRow();
                int newCol = square.getCol();

                if ((newRow == attackersRow && newRow == kingsRow)
                        || (newCol == attackersCol && newCol == kingsCol)) {
                    return this.canIntersectPath(piece, attackersRow, attackersCol, kingsRow, kingsCol);
                }
            }
        }
        return false;
    }

    /**
     * Checks if the path between the attacking piece and the king can be intersected by the Piece
     * passed in the argument.
     *
     * @param piece          The piece that might intersect the path.
     * @param attackersRow   The row of the attacking piece.
     * @param attackersCol   The column of the attacking piece.
     * @param kingsRow       The row of the king.
     * @param kingsCol       The column of the king.
     */
    public boolean canIntersectPath(Piece piece, int attackersRow, int attackersCol, int kingsRow, int kingsCol) {
        int rowIncrement = Integer.compare(attackersRow, kingsRow);
        int colIncrement = Integer.compare(attackersCol, kingsCol);
        for (int row = attackersRow + rowIncrement, col = attackersCol + colIncrement;
             row != kingsRow || col != kingsCol;
             row += rowIncrement, col += colIncrement) {
            if (row >= 0 && row < 8 && col >= 0 && col < 8) {
                if (piece.getValidSquares().contains(this.board[row][col])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Sets up the layout of the chessboard, including square colors and click handlers.
     * The colors of the board alternate to create a checkered design.
     */
    public void setupBoardLayout() {
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                if ((i % Constants.IS_EVEN == 1) && (j % Constants.IS_EVEN == 1) ||
                        (i % Constants.IS_EVEN == 0) && (j % Constants.IS_EVEN == 0)) {
                    this.board[i][j] = new MySquare(i, j, this.gamePane, false);
                    this.board[i][j].setColor(Constants.BOARD_COLOR.darker().saturate());
                }
                else {
                    this.board[i][j] = new MySquare(i, j, this.gamePane, false);
                    this.board[i][j].setColor(Constants.BOARD_COLOR);
                }
                this.board[i][j].getOccupying();
                final MySquare currentSquare = this.board[i][j];
                currentSquare.getMySquare().setOnMouseClicked(event -> this.game.handleSquareClick(currentSquare));
            }
        }
    }

    /**
     * Set up the white chess pieces on the initial chessboard configuration.
     * Creates and places white pawns, knights, rooks, bishops, king, and queen on the board.
     * Also sets up event handlers for piece clicks.
     */
    private void setupWhitePieces() {
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece pawn = new Pawn(this.gamePane, i, 6, true, this);
            this.whitePieces.add(pawn);
            pawn.setImage();
            this.board[i][6].setOccupying(pawn, this.board[i][6]);
            pawn.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(pawn));
        }
        Piece knight1 = new Knight(this.gamePane, 1, 7, true, this);
        this.whitePieces.add(knight1);
        knight1.setImage();
        this.board[1][7].setOccupying(knight1, this.board[1][7]);
        knight1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(knight1));

        Piece knight2 = new Knight(this.gamePane, 6, 7, true, this);
        this.whitePieces.add(knight2);
        knight2.setImage();
        this.board[6][7].setOccupying(knight2, this.board[6][7]);
        knight2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(knight2));

        Piece rook1 = new Rook(this.gamePane, 0, 7, true, this);
        this.whitePieces.add(rook1);
        rook1.setImage();
        this.board[0][7].setOccupying(rook1, this.board[0][7]);
        rook1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(rook1));

        Piece rook2 = new Rook(this.gamePane, 7, 7, true, this);
        this.whitePieces.add(rook2);
        rook2.setImage();
        this.board[7][7].setOccupying(rook2, this.board[7][7]);
        rook2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(rook2));

        Piece bishop1 = new Bishop(this.gamePane, 2, 7, true, this);
        this.whitePieces.add(bishop1);
        bishop1.setImage();
        this.board[2][7].setOccupying(bishop1, this.board[2][7]);
        bishop1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(bishop1));

        Piece bishop2 = new Bishop(this.gamePane, 5, 7, true, this);
        this.whitePieces.add(bishop2);
        bishop2.setImage();
        this.board[5][7].setOccupying(bishop2, this.board[5][7]);
        bishop2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(bishop2));


        Piece king = new King(this.gamePane, 3, 7, true, this);
        this.whitePieces.add(king);
        king.setImage();
        this.board[3][7].setOccupying(king, this.board[3][7]);
        king.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(king));


        Piece queen = new Queen(this.gamePane, 4, 7, true, this);
        this.whitePieces.add(queen);
        queen.setImage();
        this.board[4][7].setOccupying(queen, this.board[4][7]);
        queen.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(queen));
    }

    /**
     * Set up the white chess pieces on the initial chessboard configuration.
     * Creates and places white pawns, knights, rooks, bishops, king, and queen on the board.
     * Also sets up event handlers for piece clicks.
     */
    private void setupBlackPieces() {
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece pawn = new Pawn(this.gamePane, i, 1, false, this);
            this.blackPieces.add(pawn);
            pawn.setImage();
            this.board[i][1].setOccupying(pawn, this.board[i][1]);
            pawn.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(pawn));
        }
        Piece knight1 = new Knight(this.gamePane, 1, 0, false, this);
        this.blackPieces.add(knight1);
        knight1.setImage();
        this.board[1][0].setOccupying(knight1, this.board[1][0]);
        knight1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(knight1));

        Piece knight2 = new Knight(this.gamePane, 6, 0, false, this);
        this.blackPieces.add(knight2);
        knight2.setImage();
        this.board[6][0].setOccupying(knight2, this.board[6][0]);
        knight2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(knight2));

        Piece rook1 = new Rook(this.gamePane, 0, 0, false, this);
        this.blackPieces.add(rook1);
        rook1.setImage();
        this.board[0][0].setOccupying(rook1, this.board[0][0]);
        rook1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(rook1));

        Piece rook2 = new Rook(this.gamePane, 7, 0, false, this);
        this.blackPieces.add(rook2);
        rook2.setImage();
        this.board[7][0].setOccupying(rook2, this.board[7][0]);
        rook2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(rook2));

        Piece bishop1 = new Bishop(this.gamePane, 2, 0, false, this);
        this.blackPieces.add(bishop1);
        bishop1.setImage();
        this.board[2][0].setOccupying(bishop1, this.board[2][0]);
        bishop1.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(bishop1));

        Piece bishop2 = new Bishop(this.gamePane, 5, 0, false, this);
        this.blackPieces.add(bishop2);
        bishop2.setImage();
        this.board[5][0].setOccupying(bishop2, this.board[5][0]);
        bishop2.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(bishop2));

        Piece king = new King(this.gamePane, 4, 0, false, this);
        this.blackPieces.add(king);
        king.setImage();
        this.board[4][0].setOccupying(king, this.board[4][0]);
        king.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(king));

        Piece queen = new Queen(this.gamePane, 3, 0, false, this);
        this.blackPieces.add(queen);
        queen.setImage();
        this.board[3][0].setOccupying(queen, this.board[3][0]);
        queen.getImageView().setOnMouseClicked(event -> this.game.handlePieceClick(queen));
    }

    /**
     * Returns a LinkedList of pieces for the player specified in the parameter.
     */
    public LinkedList<Piece> getPieces(String player) {
        if (player.equals(Constants.WHITE)) {
            return this.whitePieces;
        }
        else {
            return this.blackPieces;
        }
    }

    /**
     * Returns the Piece that is attacking, or doing the checking.
     */
    public Piece getChecking() {
        if (this.checking != null) {
            return this.checking;
        }
        return null;
    }

    /**
     * Returns true if either player is in check.
     */
    public boolean getCheck() {
        return this.wInCheck || this.bInCheck;
    }

    /**
     * Returns true if the white player is in check.
     */
    public boolean getWInCheck() {
        return this.wInCheck;
    }

    /**
     * Returns true if the black player is in check.
     */
    public boolean getBInCheck() {
        return this.bInCheck;
    }

    /**
     * Accessor method for the Game.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Accessor method for the 2D array of MySquares that represent the board.
     */
    public MySquare[][] getBoard() {
        return this.board;
    }
}