package indy;

import javafx.scene.paint.Color;

/**
 * This is the Constants class, which defines various values used in Chess.
 */
public class Constants {
    // scene details
    public static final int SCENE_WIDTH = 560; // (UNITS: pixels)
    public static final int SCENE_HEIGHT = 600; // (UNITS: pixels)
    public static final String SCENE_COLOR = "-fx-background-color: #f5f5f5";
    // bottomPane details
    public static final int BOTTOM_PANE_HEIGHT = 20; // (UNITS: pixels)
    public static final String BOTTOM_PANE_COLOR = "-fx-background-color: #f0fff0";
    // bottom HBox and VBox dimensions
    public static final int HBOX_SPACING = 15; // (UNITS: pixels)
    public static final int HBOX_WIDTH = 800;
    public static final int VBOX_SPACING = 7; // (UNITS: pixels)
    public static final int VBOX_WIDTH = 150;
    // game pane details
    public static final String GAME_PANE_COLOR = "-fx-background-color: #FFFFED";
    public static final int GAME_PANE_HEIGHT = 560; // (UNITS: pixels)
    public static final int GAME_PANE_WIDTH = 560; // (UNITS: pixels)

    // button dimensions
    public static final int BUTTON_WIDTH = 90; // (UNITS: pixels)
    public static final int BUTTON_HEIGHT = 30; // (UNITS: pixels)
    // component details
    public static final int SQUARE_WIDTH = 70; // (UNITS: pixels)
    public static final int PIECE_WIDTH = 50; // (UNITS: pixels)
    // board details
    public static final int BOARD_WIDTH = 8;
    public static final Color BOARD_COLOR = Color.LIGHTBLUE;
    // colors
    public static final String WHITE = "white";
    public static final String BLACK = "black";
    // piece types
    public static final String PAWN = "Pawn";
    public static final String BISHOP = "Bishop";
    public static final String KNIGHT = "Knight";
    public static final String ROOK = "Rook";
    public static final String QUEEN = "Queen";
    public static final String KING = "King";
    // lower hierarchy constants
    public static final int LH = 4;
    public static final int LH_ROOK = 3;
    public static final int LH_BISHOP = 2;
    public static final int LH_KNIGHT = 1;
    //misc
    public static final int DIRECTIONS = 4;
    public static final int IS_EVEN = 2;



}
