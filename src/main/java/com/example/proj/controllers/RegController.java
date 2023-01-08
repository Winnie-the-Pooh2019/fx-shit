package com.example.proj.controllers;

import com.example.proj.Main;
import com.example.proj.data.GsonEditor;
import com.example.proj.data.models.FileSettings;
import com.example.proj.data.models.Settings;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegController implements Initializable {
    private FileSettings settings;

    public RegController(FileSettings settings) {
        this.settings = settings;
    }

    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public TextField group;
    @FXML
    public TextField choose;
    @FXML
    public Button submit;
    @FXML
    public ComboBox<Settings> userChooser;

    public RegController() {
        System.out.println("hello from constructor");
    }

    @FXML
    public void initialize() {
        System.out.println("hello from common init");
    }

    public void submit(Event event) {
        Node node = (Node) event.getSource();
        Stage stage = (Stage) node.getScene().getWindow();

        Settings settings = (Settings) stage.getUserData();

        settings.setName(name.getText());
        settings.setSurname(surname.getText());
        settings.setGroup(group.getText());
        settings.setSourcePath(choose.getText());

        if (Objects.equals(settings.getName(), "") || Objects.equals(settings.getSurname(), "")
                || Objects.equals(settings.getGroup(), "") || Objects.equals(settings.getSourcePath(), "")
                || settings.getName() == null || settings.getSurname() == null
                || settings.getGroup() == null || settings.getSourcePath() == null
                || !new File(settings.getSourcePath()).exists()) {
            Main.showAlert(
                    "Error",
                    "Incorrect input",
                    "Don't leave empty fields\nand place the existing file to source path",
                    Alert.AlertType.ERROR
            );
        } else
            stage.close();
    }

    public void chooseFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json file", "*.json"));
        File selectedFile = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());

        choose.setText(selectedFile.getAbsolutePath());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("hello from over init");

        System.out.println(settings);

        if (settings != null && !settings.getSettings().isEmpty()) {
            Settings sets = settings.getSettings().get(settings.getSettings().size() - 1);

            name.setText(sets.getName());
            surname.setText(sets.getSurname());
            group.setText(sets.getGroup());
            choose.setText(sets.getSourcePath());

            List<Settings> names = settings.getSettings();
            userChooser.setItems(
                    FXCollections.observableList(names)
            );

            userChooser.setOnAction(event -> {
                Settings chosen = userChooser.getValue();

                name.setText(chosen.getName());
                surname.setText(chosen.getSurname());
                group.setText(chosen.getGroup());
                choose.setText(chosen.getSourcePath());
            });
        }
    }
}
