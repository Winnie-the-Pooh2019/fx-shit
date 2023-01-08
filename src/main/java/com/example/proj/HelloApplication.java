package com.example.proj;

import com.example.proj.controllers.RegController;
import com.example.proj.controllers.state.MainWindowStateMachine;
import com.example.proj.data.GsonEditor;
import com.example.proj.data.models.FileSettings;
import com.example.proj.data.models.Settings;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        FileSettings sets = checkStart();
//        System.out.println(settings);

        if (sets == null || sets.getSettings().isEmpty()) {
            sets = new FileSettings();

            Stage dialog = new Stage();
            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("reg-view.fxml"));
            loader.setController(new RegController());
            Scene dialogScene = new Scene(loader.load());
            dialog.setScene(dialogScene);
            dialog.initOwner(stage);
            dialog.setOnCloseRequest(windowEvent -> {});
            dialog.initModality(Modality.APPLICATION_MODAL);

            Settings settings = new Settings();
            dialog.setUserData(settings);
            dialog.showAndWait();

            if (!sets.getSettings().contains(settings))
                sets.getSettings().add(settings);

            try {
                GsonEditor.write(Settings.FILENAME, sets);
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(settings);
        }

        Settings settings = sets.getSettings().get(sets.getSettings().size() - 1);
        MainWindowStateMachine.getInstance().settings = settings;

        ((Label) scene.lookup("#username")).setText(settings.getName() + " " + settings.getSurname());
        VBox vBox = (VBox) scene.lookup("#main_vbox");
        AnchorPane test = new FXMLLoader(HelloApplication.class.getResource("hello/start.fxml")).load();

        vBox.getChildren().add(test);
        VBox.setVgrow(test, Priority.ALWAYS);
    }

    private FileSettings checkStart() {
        FileSettings settings;
        try {
            settings = GsonEditor.read(Settings.FILENAME, new TypeToken<FileSettings>() {
            }.getType());
        } catch (IOException | JsonIOException | JsonSyntaxException | ClassCastException e) {
            e.printStackTrace();
            return null;
        }

        return settings;
    }

    public static void main(String[] args) {
        launch();
    }
}