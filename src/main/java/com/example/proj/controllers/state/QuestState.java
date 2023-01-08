package com.example.proj.controllers.state;

import com.example.proj.data.models.Result;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

public class QuestState extends State {
    public QuestState(MainWindowStateMachine mainWindowStateMachine) {
        super(mainWindowStateMachine);
    }

    @Override
    public void toMenu(ActionEvent event) {

    }

    @Override
    public void toSemiResult(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        setNewContent("semi_results", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getSemiRes());
        mainWindowStateMachine.getCurrentState().setContent(scene, MainWindowStateMachine.getInstance().getResults());
    }

    @Override
    public void toQuest(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();

        setNewContent("test_question", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getQuest());
        mainWindowStateMachine.getCurrentState().setContent(scene,
                MainWindowStateMachine.getInstance().getCurrent());
    }

    @Override
    public void toChooseResult(ActionEvent event) {

    }

    @Override
    public void toFinalResult(ActionEvent event) {

    }

    @Override
    public <T> void setContent(Scene scene, T obj) {
        Result question = (Result) obj;

        double solved = mainWindowStateMachine.getResults().stream().filter(result -> result.getCurrent() != null).count();
        ((ProgressBar) scene.lookup("#progress")).setProgress(solved / mainWindowStateMachine.getResults().size());
        ((Label) scene.lookup("#answersRel")).setText(((int) (solved / mainWindowStateMachine.getResults().size() * 100)) + "%");

        int index;
        if (question.getCurrent() != null) {
            System.out.println(("question current in question mark = " + question.getCurrent()));

            index = question.getAnswers().indexOf(question.getCurrent());
        } else {
            index = -1;
        }

        Label questionTitle = (Label) scene.lookup("#question");
        questionTitle.setText(question.getQuestion());

        VBox answersVbox = (VBox) scene.lookup("#questionVbox");

        ToggleGroup toggleGroup = new ToggleGroup();

        int i = 0;
        question.getAnswers().forEach(s -> {
            RadioButton button = new RadioButton(s);
            button.setId("ans" + i);
            button.setToggleGroup(toggleGroup);

            if (i == index)
                button.setSelected(true);

            answersVbox.getChildren().add(button);
            VBox.setMargin(button, new Insets(50, 0, 0, 0));
        });

        if (index != -1) {
            RadioButton radio = (RadioButton) answersVbox.getChildren().get(index);

            radio.setSelected(true);
        }
    }
}
