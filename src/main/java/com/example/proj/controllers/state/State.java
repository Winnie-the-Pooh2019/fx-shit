package com.example.proj.controllers.state;

import com.example.proj.HelloApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;

public abstract class State {
    protected MainWindowStateMachine mainWindowStateMachine;

    public State(MainWindowStateMachine mainWindowStateMachine) {
        this.mainWindowStateMachine = mainWindowStateMachine;
    }

    protected void setNewContent(String sceneName, ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        System.out.println(sceneName);

        VBox vBox = (VBox) (scene).lookup("#main_vbox");
        vBox.getChildren().remove(scene.lookup("#content"));

        Node newContent = null;
        try {
            newContent = new FXMLLoader(HelloApplication.class.getResource("hello/" + sceneName + ".fxml")).load();
            vBox.getChildren().add(newContent);
            VBox.setVgrow(newContent, Priority.ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract void toMenu(ActionEvent event);

    public abstract void toSemiResult(ActionEvent event);

    public abstract void toQuest(ActionEvent event);

    public abstract void toChooseResult(ActionEvent event);

    public abstract void toFinalResult(ActionEvent event);

    public abstract <T> void setContent(Scene scene, T obj);
}
