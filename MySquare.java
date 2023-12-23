package indy;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents a square on the chessboard. Contains logic for setting piece occupation.
 */
public class MySquare {
    private Rectangle mySquare;
    private Pane gamePane;
    private Piece occupying;
    private int row;
    private int col;
    private boolean isOccupied;

    /**
     * Constructs a MySquare object with the specified row, column, game pane, and occupied status.
     * Instantiates a new Rectangle for each MySquare and adds it graphically to the gamePane.
     *
     * @param row        The row of the square.
     * @param col        The column of the square.
     * @param gamePane   The pane in which the square is displayed.
     * @param isOccupied The initial occupied status of the square.
     */
    public MySquare(int row, int col, Pane gamePane, boolean isOccupied) {
        this.gamePane = gamePane;
        this.row = row;
        this.col = col;
        this.isOccupied = isOccupied;
        this.occupying = null;
        this.mySquare = new Rectangle(this.row * Constants.SQUARE_WIDTH, this.col * Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH, Constants.SQUARE_WIDTH);
        this.mySquare.setStroke(Color.GREY);
        this.gamePane.getChildren().add(this.mySquare);
    }


    /**
     * Takes in a Piece and a MySquare as arguments. Sets the Piece's X and Y locations
     * and adds the ImageView to the pane.
     */
    public void setOccupying(Piece occupying, MySquare clickedSquare) {
        if (clickedSquare != null) {
            this.isOccupied = true;
            this.occupying = occupying;
            this.occupying.setX(clickedSquare.getRow() * Constants.SQUARE_WIDTH);
            this.occupying.setY(clickedSquare.getCol() * Constants.SQUARE_WIDTH);
            this.occupying.addImageViewPane();
        }
    }

    /**
     * Sets the square as unoccupied.
     */
    public void setEmpty() {
        this.isOccupied = false;
        this.occupying = null;
    }

    /**
     * Removes the occupying Piece graphically.
     */
    public void removeOccupying() {
        if (this.occupying != null) {
            this.occupying.removeFromPane();
        }
        this.occupying = null;
        this.isOccupied = false;
    }

    /**
     * Assessor method to return the rectangle representing the square.
     */
    public Rectangle getMySquare() {
        return this.mySquare;
    }

    /**
     * Assessor method to return if the square is occupied by a Piece.
     */
    public boolean getIsOccupied() {
        return this.isOccupied;
    }

    /**
     * Mutator method to set the square as occupied.
     */
    public void setIsOccupied() {
        this.isOccupied = true;
    }

    /**
     * Assessor method to return the Piece object occupying the square.
     */
    public Piece getOccupying () {
        if (this.isOccupied) {
            return this.occupying;
        }
        return null;
    }

    /**
     * Assessor method to return the row of the square.
     */
    public int getRow () {
        return this.row;
    }

    /**
     * Assessor method to return the column of the square.
     */
    public int getCol () {
        return this.col;
    }

    /**
     * Mutator method to set the Color of the square to its argument.
     */
    public void setColor (Color color) {
        this.mySquare.setFill(color);
    }
}