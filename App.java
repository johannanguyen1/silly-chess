package indy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


/**
 *
 * Welcome to Chess!
 * Here is the App class, which is the top level class. It contains instances of the PaneOrganizer
 * and Scene classes, and sets up the stage. The main method of this application calls launch, a
 * JavaFX method which eventually calls the start method below.
 *
 */

public class App extends Application {

    @Override
    public void start(Stage stage) {
        // This instantiates the top-level object, sets up the scene and title, and shows the stage.
        PaneOrganizer organizer = new PaneOrganizer();
        Scene scene = new Scene(organizer.getRoot(), Constants.SCENE_WIDTH, Constants.SCENE_HEIGHT);
        stage.setTitle("stupid chess");
        stage.setScene(scene);
        stage.show();
    }

    /*
     * Here is the mainline!
     */
    public static void main(String[] args) {
        launch(args); // launch is a method inherited from Application
    }
}
