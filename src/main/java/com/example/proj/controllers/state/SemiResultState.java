package com.example.proj.controllers.state;

import com.example.proj.data.GsonEditor;
import com.example.proj.data.models.FileResult;
import com.example.proj.data.models.Result;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public class SemiResultState extends State {
    public SemiResultState(MainWindowStateMachine mainWindowStateMachine) {
        super(mainWindowStateMachine);
    }

    @Override
    public void toMenu(ActionEvent event) {
        setNewContent("start", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getMenu());

        if (mainWindowStateMachine.timer != null && mainWindowStateMachine.current != null) {
            mainWindowStateMachine.timer.stop();
            mainWindowStateMachine.current.stop();
        }
    }

    @Override
    public void toSemiResult(ActionEvent event) {

    }

    @Override
    public void toQuest(ActionEvent event) {
        setNewContent("test_question", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getQuest());
    }

    @Override
    public void toChooseResult(ActionEvent event) {

    }

    @Override
    public void toFinalResult(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        setNewContent("final_results", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getFinalRes());

        FileResult fileResult = new FileResult(mainWindowStateMachine.getResults(), mainWindowStateMachine.current.formatTime(),
                mainWindowStateMachine.settings.getName(), mainWindowStateMachine.settings.getSurname(), mainWindowStateMachine.settings.getGroup());
        mainWindowStateMachine.getCurrentState().setContent(scene, fileResult);


        try {
            GsonEditor.write(Result.RESULT_FILE + "-" + mainWindowStateMachine.settings.getName() + "-" + mainWindowStateMachine.settings.getSurname()
                    + "-" + mainWindowStateMachine.settings.getGroup() + "-" + new Date().getTime() + ".json", fileResult);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (mainWindowStateMachine.timer != null && mainWindowStateMachine.current != null) {
            mainWindowStateMachine.timer.stop();
            mainWindowStateMachine.current.stop();
        }
    }

    @Override
    public <T> void setContent(Scene scene, T obj) {
        ScrollPane scroll = (ScrollPane) scene.lookup("#scrollPane");
        VBox vbox = (VBox) scroll.getContent();
        vbox.setMaxHeight(Double.MAX_VALUE);
        VBox.setVgrow(vbox, Priority.ALWAYS);
        System.out.println(vbox);
        System.out.println(obj);

        try {

            List<Result> results = mainWindowStateMachine.getResults();
            System.out.println(results);

            Label semiRes = (Label) scene.lookup("#semiresult");
            double solved = results.stream().filter(result -> result.getCurrent() != null).count();
            semiRes.setText(((int) solved) + " из " + results.size());

            ProgressBar bar = (ProgressBar) scene.lookup("#progress");
            bar.setProgress(solved / results.size());

            results.forEach(q -> {
                HBox hBox = new HBox();

                Label name = new Label(q.getQuestion());
                name.setStyle("-fx-font-size: 18pt");

                Button button = new Button("Перейти к вопросу");
                System.out.println(("q answer = " + q.getCurrent()));
                if (q.getCurrent() != null)
                    button.setStyle("-fx-background-color: #b0e3b0");
                button.setOnAction(event -> {
                    mainWindowStateMachine.toQuest(event);
                    mainWindowStateMachine.currentQuestion = mainWindowStateMachine.getResults().indexOf(q);

                    mainWindowStateMachine.getCurrentState().setContent(scene, q);
                });

                hBox.getChildren().addAll(button, name);

                HBox.setMargin(name, new Insets(20, 0, 0, 30));
                HBox.setMargin(button, new Insets(20, 0, 0, 30));

                vbox.getChildren().add(hBox);
                HBox.setHgrow(hBox, Priority.ALWAYS);
                HBox.setMargin(hBox, new Insets(10, 0, 10, 20));
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
