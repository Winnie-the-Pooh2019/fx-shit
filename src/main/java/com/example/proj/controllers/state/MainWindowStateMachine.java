package com.example.proj.controllers.state;

import com.example.proj.data.models.Result;
import com.example.proj.data.models.Settings;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import org.apache.commons.lang3.time.StopWatch;

import java.util.ArrayList;

public class MainWindowStateMachine {
    private State menu;
    private State semiRes;
    private State quest;
    private State finalRes;
    private State chooseResult;

    public State getChooseResult() {
        return chooseResult;
    }

    public void setChooseResult(State chooseResult) {
        this.chooseResult = chooseResult;
    }

    private State currentState;
    private ArrayList<Result> results;
    public int currentQuestion = 0;
    public Settings settings;

    public StopWatch current;

    public AnimationTimer timer;

    public void addRes(String current) {
        results.get(currentQuestion).setCurrent(current);
    }

    public Result getQuestion(int index) {
        return results.get(index);
    }

    public Result getNext() {
        return (currentQuestion + 1 < results.size())
                ? results.get(++currentQuestion)
                : results.get(currentQuestion = 0);
    }

    public Result getPrev() {
        return (currentQuestion - 1 >= 0 )
                ? results.get(--currentQuestion)
                : results.get(currentQuestion = results.size() - 1);
    }

    public Result getCurrent() {
        return results.get(currentQuestion);
    }

    private static MainWindowStateMachine mainWindowStateMachine;

    private MainWindowStateMachine() {
        menu = new MenuState(this);
        semiRes = new SemiResultState(this);
        quest = new QuestState(this);
        finalRes = new FinalResultState(this);
        chooseResult = new ChooseResultState(this);

        results = new ArrayList<>();

        current = new StopWatch();
        
        currentState = menu;
    }

    public static MainWindowStateMachine getInstance() {
        if (mainWindowStateMachine == null)
            mainWindowStateMachine = new MainWindowStateMachine();

        return mainWindowStateMachine;
    }

    public ArrayList<Result> getResults() {
        return results;
    }

    public void setResults(ArrayList<Result> results) {
        this.results = results;
    }
    
    public void toMenu(ActionEvent event) {
        currentState.toMenu(event);
    }
    
    public void toSemiResult(ActionEvent event) {
        currentState.toSemiResult(event);
    }
    
    public void toQuest(ActionEvent event) {
        currentState.toQuest(event);
    }

    public void toChooseResult(ActionEvent event) {
        currentState.toChooseResult(event);
    }
    
    public void toFinalResult(ActionEvent event) {
        currentState.toFinalResult(event);
    }

    public State getMenu() {
        return menu;
    }

    public void setMenu(State menu) {
        this.menu = menu;
    }

    public State getSemiRes() {
        return semiRes;
    }

    public void setSemiRes(State semiRes) {
        this.semiRes = semiRes;
    }

    public State getQuest() {
        return quest;
    }

    public void setQuest(State quest) {
        this.quest = quest;
    }

    public State getFinalRes() {
        return finalRes;
    }

    public void setFinalRes(State finalRes) {
        this.finalRes = finalRes;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
