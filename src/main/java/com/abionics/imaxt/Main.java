package com.abionics.imaxt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

public class Main extends Application {
    @Override
    public void start(@NotNull Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/imaxt.fxml"));
        primaryStage.setTitle("Imaxt");
        primaryStage.setScene(new Scene(root, 500, 540));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
