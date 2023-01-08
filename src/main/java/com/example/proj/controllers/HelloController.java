package com.example.proj.controllers;

import com.example.proj.HelloApplication;
import com.example.proj.Main;
import com.example.proj.controllers.state.MainWindowStateMachine;
import com.example.proj.data.GsonEditor;
import com.example.proj.data.models.FileSettings;
import com.example.proj.data.models.Settings;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.util.HashSet;
import java.util.Objects;

public class HelloController {
    @FXML
    private ImageView usericon;

    @FXML
    private Label username;

    @FXML
    private Label semiresult;

    @FXML
    private Label result;

    @FXML
    public void userClick() {
        if (MainWindowStateMachine.getInstance().getCurrentState() == MainWindowStateMachine.getInstance().getMenu()) {
            FileSettings sets;
            try {
                sets = GsonEditor.read(Settings.FILENAME, new TypeToken<FileSettings>() {
                }.getType());

                Settings settings;
                if (sets.getSettings().isEmpty())
                    settings = new Settings();
                else {
                    var s = sets.getSettings().get(sets.getSettings().size() - 1);
                    settings = new Settings(
                            s.getName(),
                            s.getSurname(),
                            s.getGroup(),
                            s.getSourcePath()
                    );
                }

                System.out.println(settings);
                Stage dialog = new Stage();
                FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("reg-view.fxml"));
                loader.setController(new RegController(sets));
                Scene dialogScene = new Scene(
                        loader.load()
                );
                dialog.setScene(dialogScene);
                dialog.initOwner(usericon.getScene().getWindow());
                dialog.initStyle(StageStyle.TRANSPARENT);
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.setUserData(settings);
                dialog.showAndWait();

                if (!sets.getSettings().contains(settings))
                    sets.getSettings().add(settings);

                username.setText(settings.getName() + " " + settings.getSurname());
                GsonEditor.write(Settings.FILENAME, sets);
                MainWindowStateMachine.getInstance().settings = settings;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void start(ActionEvent event) {
        MainWindowStateMachine.getInstance().toSemiResult(event);
    }

    @FXML
    public void showOld(ActionEvent event) {
        MainWindowStateMachine.getInstance().toChooseResult(event);
    }

    @FXML
    public void menu(ActionEvent event) {
        MainWindowStateMachine.getInstance().toMenu(event);
    }

    @FXML
    public void finnish(ActionEvent event) {
        MainWindowStateMachine.getInstance().toFinalResult(event);
    }

    @FXML
    public void toMenu(ActionEvent event) {
        System.out.println("from final to menu");
        MainWindowStateMachine.getInstance().toMenu(event);
    }

    @FXML
    public void fromQuestToMenu(ActionEvent event) {
        System.out.println("to menu");
        MainWindowStateMachine.getInstance().toMenu(event);
    }

    @FXML
    public void toQuestionList(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        recordAnswer(scene);

        MainWindowStateMachine.getInstance().toSemiResult(event);
    }

    @FXML
    public void next(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        System.out.println("in next");

        recordAnswer(scene);
        MainWindowStateMachine.getInstance().getNext();

        MainWindowStateMachine.getInstance().toQuest(event);
    }

    @FXML
    public void prev(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        recordAnswer(scene);
        MainWindowStateMachine.getInstance().getPrev();

        MainWindowStateMachine.getInstance().toQuest(event);
    }

    private void recordAnswer(Scene scene) {
        RadioButton ans0 = (RadioButton) scene.lookup("#ans0");
        ToggleGroup group = ans0.getToggleGroup();
        RadioButton selected = (RadioButton) group.getSelectedToggle();
        String answer = null;
        if (selected != null)
            answer = (selected).getText();

        System.out.println(("answer = " + answer));

        MainWindowStateMachine.getInstance().getResults()
                .get(MainWindowStateMachine.getInstance().currentQuestion).setCurrent(answer);
    }
}