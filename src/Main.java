import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("search.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Signup Screen"); // Optional: Set a window title
            stage.show();
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
