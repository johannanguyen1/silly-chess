package indy;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Game class is the top-level logic class for the chess game.
 * It manages the game state, player turns, and UI components using JavaFX.
 * It also manages piece clicking for Piece movement.
 */
public class Game {
    private final HBox hBox;
    private final VBox vBox;
    private final Pane gamePane;
    private Board board;
    private MySquare clickedSquare;
    private Piece selectedPiece;
    private Piece prevSelectedPiece;
    private final String whitePlayer;
    private final String blackPlayer;
    private String currentPlayer;
    private boolean isSilly;
    private Label gameLabel;
    private Label checkLabel;
    private boolean castle;

    /**
     * Constructs a new Game instance, defines instance variables, and calls helper methods
     * to set up the gamePane, HBox, and VBox.
     *
     * @param hBox      The horizontal box for UI controls.
     * @param vBox      The vertical box for UI labels and information.
     * @param gamePane  The main game pane where the chessboard is displayed and components are
     *                  graphically added.
     */
    public Game(HBox hBox, VBox vBox, Pane gamePane) {
        this.hBox = hBox;
        this.vBox = vBox;
        this.gamePane = gamePane;
        this.selectedPiece = null;
        this.prevSelectedPiece = null;
        this.clickedSquare = null;
        this.whitePlayer = Constants.WHITE;
        this.blackPlayer = Constants.BLACK;
        this.currentPlayer = this.whitePlayer;
        this.isSilly = false;
        this.castle = false;
        this.setupGamePane();
        this.setupBottom();
        this.board = new Board(gamePane, this);
    }

    /**
     * This method sets the color and size of the Pane, gamePane.
     */
    private void setupGamePane() {
        this.gamePane.setStyle(Constants.GAME_PANE_COLOR);
        this.gamePane.setPrefHeight(Constants.GAME_PANE_HEIGHT);
        this.gamePane.setPrefWidth(Constants.GAME_PANE_WIDTH);
    }

    /**
     * This sets the color, size, and positioning of the HBox and VBox, which contains
     * the player turn and game state labels. It also calls helper methods to set up
     * the basic, silly, restart, and quit buttons.
     */
    private void setupBottom() {
        this.hBox.setStyle(Constants.BOTTOM_PANE_COLOR);
        this.hBox.setPrefHeight(Constants.BOTTOM_PANE_HEIGHT);
        this.hBox.setPrefWidth(Constants.HBOX_WIDTH);
        this.vBox.setStyle(Constants.BOTTOM_PANE_COLOR);
        this.vBox.setPrefHeight(Constants.BOTTOM_PANE_HEIGHT);
        this.vBox.setPrefWidth(Constants.VBOX_WIDTH);
        this.setupGameLabel();
        this.setupOverLabel();
        this.setupBasicButton();
        this.setupSillyButton();
        this.setupRestartButton();
        this.setupQuitButton();

    }

    /**
     * This instantiates, sets font, and sets text to a new Label. This Label indicates the current
     * player's turn. It is added to the VBox.
     */
    private void setupGameLabel() {
        this.gameLabel = new Label(this.currentPlayer + "'s turn!");
        this.gameLabel.setFont(Font.font("Courier New"));
        this.gameLabel.setAlignment(Pos.CENTER_RIGHT);
        this.vBox.getChildren().add(this.gameLabel);
    }

    /**
     * This instantiates, sets font, and sets text to a new Label. This Label indicates the state
     * of the game (in check, game over). It is added to the VBox.
     */
    private void setupOverLabel() {
        this.checkLabel = new Label("");
        this.checkLabel.setFont(Font.font("Courier New"));
        this.checkLabel.setAlignment(Pos.CENTER_RIGHT);
        this.vBox.getChildren().add(this.checkLabel);
    }

    /**
     * This instantiates, sets size, and sets text to a new Button. When clicked,
     * this Button exits the program. This Button is added to the HBox.
     */
    private void setupQuitButton() {
        Button button = new Button("quit!");
        button.setFont(Font.font("Courier New"));
        button.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        button.setOnAction((ActionEvent e) ->
                System.exit(0));
        button.setFocusTraversable(false);
        this.hBox.getChildren().add(button);
    }

    /**
     * This instantiates, sets size, and sets text to a new Button. When clicked,
     * this Button creates a new Board to restart the game. This Button is added to the HBox.
     */
    private void setupRestartButton() {
        Button button = new Button("restart!");
        button.setFont(Font.font("Courier New"));
        button.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        button.setOnAction((ActionEvent e) ->
                this.restart());
        button.setFocusTraversable(false);
        this.hBox.getChildren().add(button);
    }

    /**
     * This helper method creates a new Board to restart the game and resets the current
     * player to white.
     */
    private void restart() {
        this.board = new Board(this.gamePane, this);
        this.checkLabel.setText("");
        this.currentPlayer = this.whitePlayer;
        this.gameLabel.setText(this.currentPlayer + "'s turn!");

    }

    /**
     * This instantiates, sets size, and sets text to a new Button. When clicked,
     * this Button calls sillyMode. This Button is added to the HBox.
     */
    private void setupSillyButton() {
        Button button = new Button("silly");
        button.setFont(Font.font("Courier New"));
        button.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        button.setOnAction((ActionEvent e) ->
                this.sillyMode());
        button.setFocusTraversable(false);
        this.hBox.getChildren().add(button);
    }

    /**
     * This helper method sets the isSilly boolean to true and restarts the game.
     */
    private void sillyMode() {
        this.isSilly = true;
        this.restart();
    }

    /**
     * This instantiates, sets size, and sets text to a new Button. When clicked,
     * this Button calls basicMode. This Button is added to the HBox.
     */
    private void setupBasicButton() {
        Button button = new Button("basic");
        button.setFont(Font.font("Courier New"));
        button.setPrefSize(Constants.BUTTON_WIDTH, Constants.BUTTON_HEIGHT);
        button.setOnAction((ActionEvent e) ->
                this.basicMode());
        button.setFocusTraversable(false);
        this.hBox.getChildren().add(button);
    }

    /**
     * This helper method sets the isSilly boolean to false and restarts the game.
     */
    private void basicMode() {
        this.isSilly = false;
        this.restart();
    }

    /**
     * Handles the click event on a chess piece.
     * If no piece is currently selected and the clicked piece belongs to the current player,
     * it selects the piece and highlights it. If a piece is already selected, the selected piece
     * is deselected and the castling instance variable is reset to false;
     */
    public void handlePieceClick(Piece clickedPiece) {
        if (this.selectedPiece == null && clickedPiece.getColor().equals(this.currentPlayer)) {
            this.selectedPiece = clickedPiece;
        }
        this.castle = false;
    }
    /**
     * Handles the click event on a chessboard square.
     * If a piece is selected, it attempts to move the selected piece to the clicked square
     * based on chess rules. Handles special moves like castling, en passant, and piece transformations.
     * Method also handles if the selected piece's move will endanger their own king.
     * After the first move, it updates the check label and checks for game over.
     * If the move was invalid, the user must select a new piece and a new square.
     */
    public void handleSquareClick(MySquare clickedSquare) {
        this.clickedSquare = clickedSquare;
        Piece captured = null;
        int newRow = this.clickedSquare.getRow();
        int newCol = this.clickedSquare.getCol();

        if (this.selectedPiece != null) {
            int oldRow = this.selectedPiece.getOldRow();
            int oldCol = this.selectedPiece.getOldCol();
            boolean kingMoveValid = this.kingMoveValid(this.getOppPlayer(), this.selectedPiece, this.clickedSquare);
            boolean whenInCheck = true;
            boolean notMovingAProtector = this.notMovingAProtector(this.getOppPlayer());
            boolean moveValidity = this.selectedPiece.moveValidity(oldRow, oldCol, newRow, newCol);
            if (this.board.getCheck()) {
                whenInCheck = this.selectedPiece.whenInCheck(oldRow, oldCol, newRow, newCol);
            }

            if (moveValidity && kingMoveValid && whenInCheck && notMovingAProtector) {
                this.castling(oldRow, oldCol, newRow, newCol);
                this.board.getBoard()[oldRow][oldCol].removeOccupying();
                if (this.clickedSquare.getIsOccupied() && !this.castle) {
                    captured = this.clickedSquare.getOccupying();
                    this.clickedSquare.removeOccupying();
                    this.board.removePieceLogically(captured);
                } else if (this.prevSelectedPiece != null && this.prevSelectedPiece.getType().equals(Constants.PAWN)
                        && this.selectedPiece.getType().equals(Constants.PAWN)) {
                    this.enPassant(newRow, newCol);
                }
                if (!this.castle) {
                    this.clickedSquare.setOccupying(this.selectedPiece, this.clickedSquare);
                    this.selectedPiece.movePiece(clickedSquare);
                    this.clickedSquare.setIsOccupied();
                }
                this.selectedPiece.setFirstMove(false);
                this.prevSelectedPiece = this.selectedPiece;
                this.selectedPiece = null;

                if (this.isSilly) {
                    if (captured != null) {
                        this.sillyShift(captured);
                    }
                }
                this.updateCheck();

                if (this.board.getCheck()) {
                    this.gameOver();
                }

                this.switchPlayer();

            } else {
                this.selectedPiece = null;
                this.castle = false;

            }
        }
    }

    /**
     * This helper method removes the previously selected piece if the current move
     * is an en passant capture.
     * */
    private void enPassant(int newRow, int newCol) {
        int enPassantCol = newCol + 1;
        int enPassantRowLeft = newRow - 1;
        int enPassantRowRight = newRow + 1;

        if (enPassantCol >= 0 && enPassantCol < Constants.BOARD_WIDTH) {
            for (int enPassantRow : new int[]{enPassantRowLeft, enPassantRowRight}) {
                if (enPassantRow >= 0 && enPassantRow < Constants.BOARD_WIDTH &&
                        !this.board.getBoard()[newRow][newCol].getIsOccupied() &&
                        this.board.getBoard()[newRow][newCol].equals(this.prevSelectedPiece.getEPSquare())) {
                    Piece captured = this.prevSelectedPiece;

                    if (captured.getColor().equals(Constants.WHITE)) {
                        this.board.getBoard()[newRow][newCol - 1].removeOccupying();
                    } else {
                        this.board.getBoard()[newRow][newCol + 1].removeOccupying();
                    }
                    this.board.removePieceLogically(captured);
                }
            }
        }
    }

    /**
     * This helper method handles the movement of a castling move. If the pieces are a King and
     * a Rook of the same color, the castling is their first moves, and the board is not in check,
     * the King moves two squares towards a Rook of the same color and the Rook moves onto the
     * square over which the king crossed.
     */
    private void castling(int oldRow, int oldCol, int newRow, int newCol) {
        if (this.clickedSquare.getIsOccupied()) {
            Piece rook = this.clickedSquare.getOccupying();
            Piece king = this.selectedPiece;
            boolean kAndR = king.getType().equals(Constants.KING) && rook.getType().equals(Constants.KING);
            boolean sameColor = king.getColor().equals(rook.getColor());
            boolean first = king.ifFirstMove() && rook.ifFirstMove();
            boolean inCheck;

            if (this.currentPlayer.equals(Constants.WHITE)) {
                inCheck = this.board.getWInCheck();
            } else {
                inCheck = this.board.getBInCheck();
            }
            if (!inCheck && kAndR && sameColor && first) {
                this.castle = true;
                this.board.getBoard()[oldRow][oldCol].removeOccupying();
                this.board.getBoard()[newRow][newCol].removeOccupying();
                this.board.removePieceLogically(rook);
                this.board.removePieceLogically(king);

                if (Math.min(oldRow, newRow) == oldRow) {
                    this.board.getBoard()[oldRow + 1][oldCol].setOccupying(rook, this.board.getBoard()[oldRow + 1][oldCol]);
                    this.board.getBoard()[oldRow + 2][oldCol].setOccupying(king, this.board.getBoard()[oldRow + 2][oldCol]);
                    rook.movePiece(this.board.getBoard()[oldRow + 1][oldCol]);
                    king.movePiece(this.board.getBoard()[oldRow + 2][oldCol]);
                    this.board.getBoard()[oldRow + 1][oldCol].setIsOccupied();
                    this.board.getBoard()[oldRow + 2][oldCol].setIsOccupied();
                } else {
                    this.board.getBoard()[oldRow - 1][oldCol].setOccupying(rook, this.board.getBoard()[oldRow - 1][oldCol]);
                    this.board.getBoard()[oldRow - 2][oldCol].setOccupying(king, this.board.getBoard()[oldRow - 2][oldCol]);
                    rook.movePiece(this.board.getBoard()[oldRow - 1][oldCol]);
                    king.movePiece(this.board.getBoard()[oldRow - 2][oldCol]);
                    this.board.getBoard()[oldRow - 1][oldCol].setIsOccupied();
                    this.board.getBoard()[oldRow - 2][oldCol].setIsOccupied();
                }
                this.board.addPieceLogically(king);
                this.board.addPieceLogically(rook);
            } else {
                this.castle = false;
            }
        }
    }

    /**
     * Takes in opposite player, current King, and intended square as arguments.
     * Checks if the move for the king is valid. This could possibly be castling or
     * ensuring the move does not put the King at a square that is under attack by the
     * opponent.
     */
    public boolean kingMoveValid(String player, Piece king, MySquare mySquare) {
        boolean valid = true;
        int oldRow = king.getOldRow();
        int oldCol = king.getOldCol();
        int newRow = mySquare.getRow();
        int newCol = mySquare.getCol();

        if (king.getType().equals(Constants.KING)) {
            if (king.canCastle(oldRow, oldCol, newRow, newCol)) {
                return true;
            } else {
                for (Piece piece : this.board.getPieces(player)) {
                    if (!piece.getType().equals(Constants.PAWN)) {
                        this.board.getBoard()[oldRow][oldCol].removeOccupying();
                    }
                    piece.setValidSquares();
                    for (int i = 0; i < piece.getValidSquares().size(); i++) {
                        MySquare square = piece.getValidSquares().get(i);
                        if (square.getRow() == newRow && square.getCol() == newCol) {
                            valid = piece.getOldRow() == newRow && piece.getOldCol() == newCol;
                            break;
                        }
                    }
                    if (!piece.getType().equals(Constants.PAWN)) {
                        this.board.getBoard()[oldRow][oldCol].setOccupying(king, this.board.getBoard()[oldRow][oldCol]);
                        king.movePiece(this.board.getBoard()[oldRow][oldCol]);
                    }
                }
            }
        }
        return valid;
    }


    /**
     * Checks if the selected piece, acting as a protector, is not moving into a position
     * that exposes the player's king to a check. Does so by temporarily removing the protector
     * and checking the moves of the opposite side to see if its removal will threaten the King.
     * If its removal will threaten the king, check if the new moves will continue to protect
     * the King.
     * Returns true if the move maintains the King's safety.
     */
    public boolean notMovingAProtector(String player) {
        Piece attacker = null;
        Piece protector = this.selectedPiece;
        boolean safe = true;
        if (!this.selectedPiece.getType().equals("King")) {
            this.board.getBoard()[protector.getOldRow()][protector.getOldCol()].removeOccupying();
            for (Piece piece : this.board.getPieces(player)) {
                piece.setValidSquares();
                for (int i = 0; i < piece.getValidSquares().size(); i++) {
                    MySquare square = piece.getValidSquares().get(i);
                    if (square.getRow() == this.getCurrKing().getOldRow() && square.getCol() == this.getCurrKing().getOldCol()) {
                        attacker = piece;
                        safe = false;
                    }
                }
                protector.removeFromPane();
                this.board.getBoard()[protector.getOldRow()][protector.getOldCol()].setOccupying(protector, this.board.getBoard()[protector.getOldRow()][protector.getOldCol()]);
                protector.movePiece(this.board.getBoard()[protector.getOldRow()][protector.getOldCol()]);
            }
            // ensures that a move to maintain safety is possible, but user has the liberty to make
            // the incorrect move and put king at risk
            if (attacker != null) {
                int attRow = attacker.getOldRow();
                int attCol = attacker.getOldCol();
                int kRow = this.getCurrKing().getOldRow();
                int kCol = this.getCurrKing().getOldCol();
                safe = this.board.canIntersectPath(protector, attRow, attCol, kRow, kCol);
            }
        }
        return safe;
    }

    /**
     * If the captured piece in the argument is not a Pawn, shiftBoard is called.
     */
    public void sillyShift(Piece captured) {
        if (!captured.getType().equals(Constants.PAWN)) {
            this.shiftBoard();
        }
    }

    /**
     * A switch statement is used to randomly select a direction in which the board
     * will shift in.
     */
    private void shiftBoard() {
        int direction = (int) (Math.random() * Constants.DIRECTIONS);
        switch (direction) {
            case 0:
                this.shiftUp();
                break;
            case 1:
                this.shiftDown();
                break;
            case 2:
                this.shiftLeft();
                break;
            case 3:
                this.shiftRight();
                break;
        }
    }

    /**
     * Shifts pieces in the upwards direction on the chessboard.
     * It moves each piece in each row from the top to the bottom
     * within their respective rows, shifting the entire row by one square up.
     * Then, it calls transformRandomPieceInWrappedCol to generate a lower
     * hierarchy piece which will switch colors.
     */
    private void shiftUp() {
        int offset = -1;
        int topCol = 0;
        int botCol = Constants.BOARD_WIDTH - 1;
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece lastPiece = null;
            if (this.board.getBoard()[i][topCol].getIsOccupied()) {
                lastPiece = this.board.getBoard()[i][topCol].getOccupying();
                this.board.getBoard()[i][topCol].removeOccupying();
            }
            Piece[] tempPieces = new Piece[Constants.BOARD_WIDTH];
            for (int col = botCol; col >= 0; col--) {
                this.storeWrappedCol(tempPieces, i, col);
            }
            for (int col = botCol; col >= 1; col--) {
                this.shiftMid(tempPieces, i, col, 0, offset);
            }
            if (lastPiece != null) {
                lastPiece.removeFromPane();
                if (this.board.getBoard()[i][botCol].getIsOccupied()) {
                    this.board.getBoard()[i][botCol].removeOccupying();
                }
                this.board.getBoard()[i][botCol].setOccupying(lastPiece, this.board.getBoard()[i][botCol]);
                this.board.getBoard()[i][botCol].setIsOccupied();
                lastPiece.movePiece(this.board.getBoard()[i][botCol]);
            }
        }
        this.transformRandomPieceInWrappedCol(botCol);
    }

    /**
     * Shifts pieces in the downwards direction on the chessboard.
     * It moves each piece in each row from the bottom to the top
     * within their respective rows, shifting the entire row by one square down.
     * Then, it calls transformRandomPieceInWrappedCol to generate a lower
     * hierarchy piece which will switch colors.
     */
    private void shiftDown() {
        int offset = 1;
        int topCol = 0;
        int botCol = Constants.BOARD_WIDTH - 1;
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece lastPiece = null;
            if (this.board.getBoard()[i][botCol].getIsOccupied()) {
                lastPiece = this.board.getBoard()[i][botCol].getOccupying();
                this.board.getBoard()[i][botCol].removeOccupying();
            }
            Piece[] tempPieces = new Piece[Constants.BOARD_WIDTH];
            for (int col = topCol; col < Constants.BOARD_WIDTH; col++) {
                this.storeWrappedCol(tempPieces, i, col);
            }
            for (int col = topCol; col < botCol; col++) {
                this.shiftMid(tempPieces, i, col, 0, offset);
            }
            if (lastPiece != null) {
                lastPiece.removeFromPane();
                if (this.board.getBoard()[i][topCol].getIsOccupied()) {
                    this.board.getBoard()[i][topCol].removeOccupying();
                }
                this.board.getBoard()[i][topCol].setOccupying(lastPiece, this.board.getBoard()[i][topCol]);
                this.board.getBoard()[i][topCol].setIsOccupied();
                lastPiece.movePiece(this.board.getBoard()[i][topCol]);
            }
        }
        this.transformRandomPieceInWrappedCol(topCol);
    }

    /**
     * Shifts pieces in the right direction on the chessboard.
     * It moves each piece in each column from the leftmost side to the rightmost side
     * within their respective columns, shifting the entire column by one square to the right.
     * Then, it calls transformRandomPieceInWrappedRow to generate a lower
     * hierarchy piece which will switch colors.
     */
    private void shiftRight() {
        int offset = 1;
        int leftRow = 0;
        int rightRow = Constants.BOARD_WIDTH - 1;
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece lastPiece = null;
            if (this.board.getBoard()[rightRow][i].getIsOccupied()) {
                lastPiece = this.board.getBoard()[rightRow][i].getOccupying();
                this.board.getBoard()[rightRow][i].removeOccupying();
            }
            Piece[] tempPieces = new Piece[Constants.BOARD_WIDTH];
            for (int row = leftRow; row <= rightRow; row++) {
                this.storeWrappedRow(tempPieces, row, i);
            }
            for (int row = leftRow; row < rightRow; row++) {
                this.shiftMid(tempPieces, row, i, offset, 0);
            }
            if (lastPiece != null) {
                lastPiece.removeFromPane();
                if (this.board.getBoard()[leftRow][i].getIsOccupied()) {
                    this.board.getBoard()[leftRow][i].removeOccupying();
                }
                this.board.getBoard()[leftRow][i].setOccupying(lastPiece, this.board.getBoard()[leftRow][i]);
                this.board.getBoard()[leftRow][i].setIsOccupied();
                lastPiece.movePiece(this.board.getBoard()[leftRow][i]);
            }
        }
        this.transformRandomPieceInWrappedRow(leftRow);

    }

    /**
     * Shifts pieces in the left direction on the chessboard.
     * It moves each piece in each column from the rightmost side to the leftmost side
     * within their respective columns, shifting the entire column by one square to the left.
     * Then, it calls transformRandomPieceInWrappedRow to generate a lower
     * hierarchy piece which will switch colors.
     */
    private void shiftLeft() {
        int offset = -1;
        int rightRow = Constants.BOARD_WIDTH - 1;
        int leftRow = 0;
        for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
            Piece lastPiece = null;
            if (this.board.getBoard()[leftRow][i].getIsOccupied()) {
                lastPiece = this.board.getBoard()[leftRow][i].getOccupying();
                this.board.getBoard()[leftRow][i].removeOccupying();
            }
            Piece[] tempPieces = new Piece[Constants.BOARD_WIDTH];
            for (int row = rightRow; row >= leftRow; row--) {
                this.storeWrappedRow(tempPieces, row, i);
            }
            for (int row = rightRow; row > leftRow; row--) {
                this.shiftMid(tempPieces, row, i, offset, 0);
            }
            if (lastPiece != null) {
                lastPiece.removeFromPane();
                if (this.board.getBoard()[rightRow][i].getIsOccupied()) {
                    this.board.getBoard()[rightRow][i].removeOccupying();
                }
                this.board.getBoard()[rightRow][i].setOccupying(lastPiece, this.board.getBoard()[rightRow][i]);
                this.board.getBoard()[rightRow][i].setIsOccupied();
                lastPiece.movePiece(this.board.getBoard()[rightRow][i]);
            }
        }
        this.transformRandomPieceInWrappedRow(rightRow);
    }

    /**
     * Takes in an array of Pieces, row, column, row offset, and column offset as arguments.
     * Depending on the offsets, it shifts the pieces of a row or column to the right, left,
     * up, or down.
     */
    private void shiftMid(Piece[] tempPieces, int row, int col, int rowOffset, int colOffset) {
        Piece currentPiece = null;
        if (rowOffset == 0) {
            currentPiece = tempPieces[col];
        }
        else {
            currentPiece = tempPieces[row];
        }
        if (currentPiece != null) {
            this.board.getBoard()[row + rowOffset][col + colOffset].removeOccupying();
            currentPiece.removeFromPane();
            this.board.getBoard()[row + rowOffset][col + colOffset].setOccupying(currentPiece, this.board.getBoard()[row + rowOffset][col + colOffset]);
            this.board.getBoard()[row + rowOffset][col + colOffset].setIsOccupied();
            currentPiece.movePiece(this.board.getBoard()[row + rowOffset][col + colOffset]);
        } else {
            this.board.getBoard()[row + rowOffset][col + colOffset].setEmpty();
        }
    }

    /**
     * Stores the piece occupying the specified row and column in the array provided in
     * the argument. If the square is occupied, the occupying piece is stored; Else, the array
     * is set to contain a null value at the specified column index.
     */
    private void storeWrappedCol(Piece[] tempPieces, int row, int col) {
        if (this.board.getBoard()[row][col].getIsOccupied()) {
            tempPieces[col] = this.board.getBoard()[row][col].getOccupying();
            this.board.getBoard()[row][col].removeOccupying();
        } else {
            tempPieces[col] = null;
        }
    }

    /**
     * Stores the piece occupying the specified row and column in the array provided in
     * the argument. If the square is occupied, the occupying piece is stored; Else, the array
     * is set to contain a null value at the specified row index.
     */
    private void storeWrappedRow(Piece[] tempPieces, int row, int col) {
        if (this.board.getBoard()[row][col].getIsOccupied()) {
            tempPieces[row] = this.board.getBoard()[row][col].getOccupying();
            this.board.getBoard()[row][col].removeOccupying();
        } else {
            tempPieces[row] = null;
        }
    }

    /**
     * Takes in a Piece as an argument and uses a switch statement to instantiate a new Piece
     * of a lower hierarchy. Hierarchy: queen, rook, bishop, knight, pawn.
     */
    private Piece generateLowerHierarchyPiece(Piece higherHierarchyPiece) {
        boolean isWhite = higherHierarchyPiece.getColor().equals(Constants.WHITE);
        Piece lowerPiece = new Pawn(this.gamePane, 0, 0, !isWhite, this.board);
        int random = Constants.LH;
        if (higherHierarchyPiece.getType().equals(Constants.ROOK)) {
            random = Constants.LH_ROOK;
        }
        else if (higherHierarchyPiece.getType().equals(Constants.BISHOP)) {
            random = Constants.LH_BISHOP;
        }
        else if (higherHierarchyPiece.getType().equals(Constants.KNIGHT)) {
            random = Constants.LH_KNIGHT;
        }
        else if (higherHierarchyPiece.getType().equals(Constants.PAWN)) {
            random = 0;
        }
        // The oldRow and oldCol values do not matter here, since the lower hierarchy piece will
        // just be set to a new location.
            switch ((int) (Math.random() * random)) {
                case 0:
                    lowerPiece = new Pawn(this.gamePane, 0, 0, !isWhite, this.board);
                    break;
                case 1:
                    lowerPiece = new Knight(this.gamePane, 0, 0, !isWhite, this.board);
                    break;
                case 2:
                    lowerPiece = new Bishop(this.gamePane, 0, 0, !isWhite, this.board);
                    break;
                case 3:
                    lowerPiece = new Rook(this.gamePane, 0, 0, !isWhite, this.board);
                    break;
            }
        return lowerPiece;
    }

    /**
     * Transforms a randomly selected piece in the specified column into a lower hierarchy piece.
     * The transformation involves replacing the selected piece with a lower hierarchy piece
     * and updating its properties, color, image, and event handlers. The lower hierarchy piece
     * switches to its opponent's color.
     */
    private void transformRandomPieceInWrappedCol(int col) {
        ArrayList<Integer> occupiedRows = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            if (this.board.getBoard()[row][col].getIsOccupied()) {
                if (!this.board.getBoard()[row][col].getOccupying().getType().equals("King")) {
                    occupiedRows.add(row);
                }
            }
        }
        if (!occupiedRows.isEmpty()) {
            int randomIndex = (int) (Math.random() * occupiedRows.size());
            int selectedRow = occupiedRows.get(randomIndex);
            if (this.board.getBoard()[selectedRow][col].getIsOccupied()) {
                Piece selectedPiece = this.board.getBoard()[selectedRow][col].getOccupying();
                this.board.getBoard()[selectedRow][col].removeOccupying();
                this.board.removePieceLogically(selectedPiece);
                Piece lowerPiece = this.generateLowerHierarchyPiece(selectedPiece);
                lowerPiece.setColor(!selectedPiece.getColor().equals("white"));
                if (lowerPiece.getColor().equals("white")) {
                    this.board.getPieces(this.whitePlayer).add(lowerPiece);
                }
                lowerPiece.setImage();
                this.board.getBoard()[selectedRow][col].setOccupying(lowerPiece, this.board.getBoard()[selectedRow][col]);
                this.board.getBoard()[selectedRow][col].setIsOccupied();
                lowerPiece.movePiece(this.board.getBoard()[selectedRow][col]);
                lowerPiece.getImageView().setOnMouseClicked(event -> this.handlePieceClick(lowerPiece));
            }
        }
    }

    /**
     * Transforms a randomly selected piece in the specified row into a lower hierarchy piece.
     * The transformation involves replacing the selected piece with a lower hierarchy piece
     * and updating its properties, color, image, and event handlers.
     */
    private void transformRandomPieceInWrappedRow(int row) {
        ArrayList<Integer> occupiedCols = new ArrayList<>();
        for (int col = 0; col < 8; col++) {
            if (this.board.getBoard()[row][col].getIsOccupied()) {
                if (!this.board.getBoard()[row][col].getOccupying().getType().equals(Constants.KING)) {
                    occupiedCols.add(col);
                }
            }
        }
        if (!occupiedCols.isEmpty()) {
            int randomIndex = (int) (Math.random() * occupiedCols.size());
            int selectedCol = occupiedCols.get(randomIndex);
            if (this.board.getBoard()[row][selectedCol].getIsOccupied()) {
                Piece selectedPiece = this.board.getBoard()[row][selectedCol].getOccupying();
                this.board.getBoard()[row][selectedCol].removeOccupying();
                Piece lowerPiece = this.generateLowerHierarchyPiece(selectedPiece);
                lowerPiece.setColor(!selectedPiece.getColor().equals(Constants.WHITE));
                if (lowerPiece.getColor().equals(Constants.WHITE)) {
                    this.board.getPieces(this.whitePlayer).add(lowerPiece);
                }
                lowerPiece.setImage();
                this.board.getBoard()[row][selectedCol].setOccupying(lowerPiece, this.board.getBoard()[row][selectedCol]);
                lowerPiece.getImageView().setOnMouseClicked(event -> this.handlePieceClick(lowerPiece));
                lowerPiece.movePiece(this.board.getBoard()[row][selectedCol]);
            }
        }
    }

    /**
     * Updates the check label to display which player is in check. If none,
     * set text to empty.
     */
    private void updateCheck() {
        if (this.board.isInCheck(this.blackPlayer)) {
            this.checkLabel.setText(this.whitePlayer + " is in check!");
        }
        else if (this.board.isInCheck(this.whitePlayer)) {
            this.checkLabel.setText(this.blackPlayer + " is in check!");
        }
        else {
            this.checkLabel.setText("");
        }
    }

    /**
     * If either color is in checkmate, set all piece clicking and square clicking to transparent.
     * Set the game label and check label to indicate "game over".
     */
    private void gameOver() {
        if (this.board.isCheckmate(this.whitePlayer) || this.board.isCheckmate(this.blackPlayer)) {
            LinkedList<Piece> allPieces = new LinkedList<>(this.board.getPieces(this.whitePlayer));
            allPieces.addAll(this.board.getPieces(this.blackPlayer));
            for (Piece all : allPieces) {
                all.getImageView().setMouseTransparent(true);
            }
            for (int i = 0; i < Constants.BOARD_WIDTH; i++) {
                for (int j = 0; j < Constants.BOARD_WIDTH; j++) {
                    this.board.getBoard()[i][j].getMySquare().setMouseTransparent(true);
                }
            }
            this.checkLabel.setText("game over ;)");
            this.gameLabel.setText("");
        }
    }

    /**
     * Switches the current player to the opposite color and updates the game label to display
     * the updated player's turn.
     */
    public void switchPlayer() {
        if (this.currentPlayer.equals(this.whitePlayer)) {
            this.currentPlayer = this.blackPlayer;
        }
        else {
            this.currentPlayer = this.whitePlayer;
        }
        this.gameLabel.setText(this.currentPlayer + "'s turn!");

    }

    /**
     * Accessor method for the white King.
     */
    public Piece getWhiteKing() {
        for (Piece piece : this.board.getPieces(this.whitePlayer)) {
            if (Constants.KING.equals(piece.getType())) {
                return piece;
            }
        }
        return null;
    }

    /**
     * Accessor method for the black King.
     */
    public Piece getBlackKing() {
        for (Piece piece : this.board.getPieces(this.blackPlayer)) {
            if (Constants.KING.equals(piece.getType())) {
                return piece;
            }
        }
        return null;
    }


    /**
     * Accessor method for current player's King. Located by searching through the LinkedList
     * of the current player's pieces.
     */
    public Piece getCurrKing() {
        if (this.currentPlayer.equals(Constants.WHITE)) {
            for (int i = 0; i < this.board.getPieces(this.whitePlayer).size(); i++) {
                Piece piece = this.board.getPieces(this.whitePlayer).get(i);
                if (piece.getType().equals(Constants.KING)) {
                    return piece;
                }
            }
        } else {
            for (int i = 0; i < this.board.getPieces(this.blackPlayer).size(); i++) {
                Piece piece = this.board.getPieces(this.blackPlayer).get(i);
                if (piece.getType().equals(Constants.KING)) {
                    return piece;
                }
            }
        }
        return null;
    }

    /**
     * Accessor method for previously selected piece.
     */
    public Piece getPrevSelectedPiece() {
        if (this.prevSelectedPiece != null) {
            return this.prevSelectedPiece;
        }
        return null;
    }

    /**
     * Accessor method for the color of the opposite player.
     */
    public String getOppPlayer() {
        if (this.currentPlayer.equals(Constants.WHITE)) {
            return Constants.BLACK;
        }
        return Constants.WHITE;
    }
}