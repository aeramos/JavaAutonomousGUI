package auto_gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        final String version = "0.3.0";

        Controller controller = new Controller(stage, version);
        stage.setTitle("SciBorgs Java Autonomous GUI - Version " + version);
        stage.setScene(new Scene(controller));
        stage.setResizable(false);
        stage.getIcons().add(new Image("api/resources/SciBorgs.png"));
        stage.toFront();
        stage.show();
    }
}
