package indy;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

/**
 *  The PaneOrganizer class is the top-level graphics class. This class is responsible for
 *  instantiating and/or positioning the root pane, gamePane, and bottom, which is made up of a
 *  VBox and an HBox. It contains helper methods to call in the constructor which create these
 *  graphical components.
 */
public class PaneOrganizer {
    private BorderPane root;
    private Pane gamePane;
    private HBox hBox;
    private VBox vBox;


    /**
     *  The PaneOrganizer constructor calls the three helper methods createRoot, createHBox,
     *  createVBox, and createGamePane. It also creates a new Game that takes these newly
     *  instantiated graphical components as arguments.
     */
    public PaneOrganizer() {
        this.createRoot();
        this.createHBox();
        this.createVBox();
        this.createGamePane();
        new Game(this.hBox, this.vBox, this.gamePane);
    }

    /**
     * Creates a new BorderPane called root and sets the color.
     */
    private void createRoot() {
        this.root = new BorderPane();
        this.root.setStyle(Constants.SCENE_COLOR);
    }

    /**
     * Instantiates a new HBox, sets the position of the HBox at the right of the root, and
     * sets the spacing of the components in the HBox.
     */
    private void createHBox() {
        this.hBox = new HBox();
        this.hBox.setSpacing(Constants.HBOX_SPACING);
        this.root.setRight(this.hBox);
    }

    /**
     * Instantiates a new VBox, sets the position at the left of the root, and sets the spacing
     * of the components in the VBox.
     */
    private void createVBox() {
        this.vBox = new VBox();
        this.vBox.setSpacing(Constants.VBOX_SPACING);
        this.root.setLeft(this.vBox);
    }

    /**
     * Instantiates a new Pane called gamePane and sets the position of the Pane at the top
     * of the root.
     */
    public void createGamePane() {
        this.gamePane = new Pane();
        this.root.setTop(this.gamePane);
    }

    /**
     *  The getRoot method is an accessor method which returns the BorderPane, root.
     */
    public BorderPane getRoot() {
        return this.root;
    }
}
