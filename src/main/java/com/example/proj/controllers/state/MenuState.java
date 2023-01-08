package com.example.proj.controllers.state;

import com.example.proj.Main;
import com.example.proj.data.GsonEditor;
import com.example.proj.data.models.FileResult;
import com.example.proj.data.models.Result;
import com.example.proj.data.models.Settings;
import com.google.gson.reflect.TypeToken;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

public class MenuState extends State {
    public MenuState(MainWindowStateMachine mainWindowStateMachine) {
        super(mainWindowStateMachine);
    }

    @Override
    public void toMenu(ActionEvent event) {

    }

    @Override
    public void toSemiResult(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        mainWindowStateMachine.timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                try {
                    Label timerLabel = (Label) scene.lookup("#timer");
                    timerLabel.setText(mainWindowStateMachine.current.formatTime());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mainWindowStateMachine.timer.start();
        mainWindowStateMachine.current.reset();
        mainWindowStateMachine.current.start();

        setNewContent("semi_results", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getSemiRes());

        List<Result> questionList;
        try {
            Settings settings = mainWindowStateMachine.settings;
            String path = settings.getSourcePath();
            questionList = GsonEditor.absoluteRead(path, new TypeToken<List<Result>>() {
            }.getType());

            mainWindowStateMachine.setResults(new ArrayList<>(questionList));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        questionList.forEach(System.out::println);

        mainWindowStateMachine.getCurrentState().setContent(scene, questionList);
    }

    @Override
    public void toQuest(ActionEvent event) {

    }

    @Override
    public void toChooseResult(ActionEvent event) {
        Settings settings = mainWindowStateMachine.settings;

        File direct = new File(".\\");
        System.out.println("DIRECT NAME IS " + direct.getAbsolutePath());
        File[] filesArray = direct.listFiles();
        List<String> fileNames = new ArrayList<>();

        if (filesArray != null) {
            fileNames = Arrays.stream(filesArray).map(File::getName)
                    .filter(name -> {
                        return name
                                .matches("result-" + settings.getName() + "-" + settings.getSurname() + "-" + settings.getGroup() + "\\S*\\.json");
                    }).toList();

            System.out.println(fileNames);
        }

        Scene scene = ((Node) event.getSource()).getScene();
        setNewContent("choose_result", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getChooseResult());
        mainWindowStateMachine.getCurrentState().setContent(scene, fileNames);
    }

    @Override
    public void toFinalResult(ActionEvent event) {
        try {
            String filename = Result.RESULT_FILE + "-" + mainWindowStateMachine.settings.getName() + "-" +
                    mainWindowStateMachine.settings.getSurname() + "-" + mainWindowStateMachine.settings.getGroup() + ".json";

            File file = new File(filename);
            FileResult prevRes = GsonEditor.absoluteRead(
                    file.getAbsolutePath(),
                    new TypeToken<FileResult>() {
                    }.getType()
            );

            System.out.println(("prev results are " + prevRes.toString()));

            Scene scene = ((Node) event.getSource()).getScene();

            setNewContent("final_results", event);
            mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getFinalRes());


            mainWindowStateMachine.getCurrentState().setContent(scene, prevRes);
        } catch (IOException e) {
            e.printStackTrace();
            Main.showAlert(
                    "Inform",
                    "No prev results found",
                    "For user " + mainWindowStateMachine.settings.getName() + " no previous solution found",
                    Alert.AlertType.INFORMATION);
        } catch (Exception e) {
            e.printStackTrace();
            Main.showAlert(
                    "Error",
                    "Result file is corrupted",
                    "",
                    Alert.AlertType.ERROR);
        }
    }

    @Override
    public <T> void setContent(Scene scene, T obj) {

    }
}
