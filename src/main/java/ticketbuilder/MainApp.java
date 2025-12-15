package ticketbuilder;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/fxml/login.fxml")
        );

        Scene scene = new Scene(loader.load());

        scene.getStylesheets().add(
                getClass().getResource("/style.css").toExternalForm()
        );

        stage.setTitle("Авторизация");
        stage.setScene(scene);      // ✅ ВАЖНО
        stage.sizeToScene();        // ✅ берёт размеры из FXML
        stage.setResizable(false);  // по желанию
        stage.show();
    }
}
