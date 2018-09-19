package application;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class App extends Application {

    @Override public void start(Stage stage) throws IOException {
        stage.setTitle("FRC-Boundary");
        final NumberAxis xAxis = new NumberAxis(0, 400, 0);
        final NumberAxis yAxis = new NumberAxis(0, 400,0);
        final ScatterChart<Number,Number> sc = new
                ScatterChart<Number,Number>(xAxis,yAxis);
        xAxis.setOpacity(0);
        yAxis.setOpacity(0);
        sc.setTitle("v0.1 Pre Alpha");
        sc.setMinSize(216*2,216*2);
        sc.setMaxSize(216*2,216*2);

        XYChart.Series currentPos   = new XYChart.Series();
        currentPos.setName("Position");
        sc.getData().addAll(currentPos);

        Scene scene  = new Scene(new Group());

        final VBox vbox = new VBox();
        final HBox hbox = new HBox();


        final Label label = new Label("Target IP:");
        TextField textField = new TextField("");
        textField.setPromptText("Ex: 127.0.0.1");
        final Label statusLabel = new Label("Connected:");
        Circle statusConfirmation = new Circle(10, Paint.valueOf("Red"));
        Button disconnectButton = new Button("Disconnect");

        //Connect to ip
        Runnable r = new Update(textField.getText(), currentPos);

        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Runnable r = new Update(textField.getText(), currentPos);
//                new Thread(r).start();  //TODO: Test Connect to Thread

                textField.setEditable(false);
                statusConfirmation.setFill(Paint.valueOf("Green"));
            }
        });


        disconnectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
//                new Thread(r).interrupt();  //TODO: Test Disconnect to Thread

                textField.setEditable(true);
                statusConfirmation.setFill(Paint.valueOf("Red"));
            }
        });


        hbox.setSpacing(10);

        vbox.getChildren().addAll(sc, hbox);
        hbox.getChildren().addAll(label, textField, statusLabel, statusConfirmation, disconnectButton);
        hbox.setPadding(new Insets(10, 5, 10, 5));

        ((Group)scene.getRoot()).getChildren().add(vbox);
        stage.setScene(scene);
        stage.setWidth(475);
        stage.setHeight(500);
        stage.setX(0);
        stage.setY(0);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args){
        launch(args);

    }

}