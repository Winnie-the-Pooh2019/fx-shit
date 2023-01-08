package com.example.proj.controllers.state;

import javafx.event.ActionEvent;
import javafx.scene.Scene;

public class RegistrationState extends State {
    public RegistrationState(MainWindowStateMachine mainWindowStateMachine) {
        super(mainWindowStateMachine);
    }

    @Override
    public void toMenu(ActionEvent event) {
        this.setNewContent("start", event);
        mainWindowStateMachine.setCurrentState(mainWindowStateMachine.getMenu());
    }

    @Override
    public void toSemiResult(ActionEvent event) {

    }

    @Override
    public void toQuest(ActionEvent event) {

    }

    @Override
    public void toChooseResult(ActionEvent event) {

    }

    @Override
    public void toFinalResult(ActionEvent event) {

    }

    @Override
    public <T> void setContent(Scene scene, T obj) {

    }
}
