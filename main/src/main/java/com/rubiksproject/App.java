package com.rubiksproject;

import com.rubiksSolver.Search;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ObservableDoubleValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point3D;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.lang.Math;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.io.IOException;

/**
 * JavaFX App
 */


public class App extends Application {


    private Cube cube;


    @Override
    public void start(Stage stage) throws IOException {
        
        
        cube = new Cube();
        
        Rotate yRotate = new Rotate(0, 0,0,1200, Rotate.Y_AXIS);
        Rotate xRotate = new Rotate(0, 0,0,1200, Rotate.X_AXIS);

        Camera camera = new PerspectiveCamera(true);
        camera.translateZProperty().set(-1200);
        camera.setFarClip(5000);
        camera.setNearClip(1);
        camera.getTransforms().addAll (yRotate, xRotate);

        Timeline[] list = {new Timeline(), new Timeline()};

        Group cubeRoot = new Group();
        cubeRoot.getChildren().add(cube.asNode());
        cubeRoot.getChildren().add(camera);


        HBox root = new HBox();
        

        SubScene subScene = new SubScene(cubeRoot, 800, 700, true, SceneAntialiasing.BALANCED);
        subScene.setFill(Color.DARKGREY);
        subScene.setCamera(camera);

        Pane pane = new Pane();
        pane.setPrefSize(1920, 1080);
        pane.getChildren().add(subScene);

        root.getChildren().addAll(pane, prepareControls());

        subScene.heightProperty().bind(root.heightProperty());
        subScene.widthProperty().bind(root.widthProperty());

        Scene sceneRoot = new Scene(root, 1000, 700, true, SceneAntialiasing.BALANCED);
        stage.setScene(sceneRoot);
        stage.setMinWidth(900);
        stage.setMinHeight(500);
        stage.addEventHandler(KeyEvent.KEY_PRESSED, event -> keyPressListener(event, list, yRotate, xRotate));
        stage.addEventHandler(KeyEvent.KEY_RELEASED, event -> keyReleaseListener(event, list));

        stage.show();
    }

    private void keyReleaseListener(KeyEvent event, Timeline[] list){
        if (event.getCode() == KeyCode.A || event.getCode() == KeyCode.D) list[0].stop();
        else if (event.getCode() == KeyCode.W || event.getCode() == KeyCode.S ) list[1].stop();  
    }

    private void keyPressListener(KeyEvent event, Timeline[] list, Rotate yRotate, Rotate xRotate){
        if(event.getCode() == KeyCode.A){
            if (list[0].getStatus() != Status.RUNNING ){
                list[0] = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0), 
                            new KeyValue(yRotate.angleProperty(), yRotate.getAngle())
                    ),
                    new KeyFrame(
                            Duration.seconds(7), 
                            new KeyValue(yRotate.angleProperty(),yRotate.getAngle() + 360)
                    )
                );
                list[0].setCycleCount(Timeline.INDEFINITE);
                list[0].play();
            }
        }
        else if(event.getCode() == KeyCode.D){
            if (list[0].getStatus() != Status.RUNNING ){
                list[0] = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0), 
                            new KeyValue(yRotate.angleProperty(), yRotate.getAngle())
                    ),
                    new KeyFrame(
                            Duration.seconds(7), 
                            new KeyValue(yRotate.angleProperty(),yRotate.getAngle() - 360)
                    )
                );
                list[0].setCycleCount(Timeline.INDEFINITE);
                list[0].play();
            }
        }
        else if(event.getCode() == KeyCode.W){
            if (list[1].getStatus() != Status.RUNNING ){
                list[1] = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0), 
                            new KeyValue(xRotate.angleProperty(), xRotate.getAngle())
                    ),
                    new KeyFrame(
                            Duration.seconds(7), 
                            new KeyValue(xRotate.angleProperty(),xRotate.getAngle() + 360)
                    )
                );
                list[1].setCycleCount(Timeline.INDEFINITE);
                list[1].play();
            }
        }
        else if(event.getCode() == KeyCode.S){
            if (list[1].getStatus() != Status.RUNNING ){
                list[1] = new Timeline(
                    new KeyFrame(
                            Duration.seconds(0), 
                            new KeyValue(xRotate.angleProperty(), xRotate.getAngle())
                    ),
                    new KeyFrame(
                            Duration.seconds(7), 
                            new KeyValue(xRotate.angleProperty(),xRotate.getAngle() - 360)
                    )
                );
                list[1].setCycleCount(Timeline.INDEFINITE);
                list[1].play();
            }
        }
    }
    

    private void executeOnclick(MouseEvent event, TextField input, Button[] buttons){
        // validate sequnce
        String[] commands = input.getText().split(" ");
        Boolean valid = true;

        // for (int i=0; i<commands.length; i++){
        //     if ((commands[i] != "R") && (commands[i] != "R'") && (commands[i] != "R2") && 
        //         (commands[i] != "L") && (commands[i] != "L'") && (commands[i] != "L2") &&
        //         (commands[i] != "U") && (commands[i] != "U'") && (commands[i] != "U2") &&
        //         (commands[i] != "D") && (commands[i] != "D'") && (commands[i] != "D2") &&
        //         (commands[i] != "F") && (commands[i] != "F'") && (commands[i] != "F2") &&
        //         (commands[i] != "B") && (commands[i] != "B'") && (commands[i] != "B2") ) valid = false;
        // }

        if (! valid) input.setText("Parsing Error");
        
        Arrays.stream(buttons).forEach(button -> button.setDisable(true));

        // for (int i = 0; i < commands.length; i++) System.out.println(commands[i] + "||");
        cube.makeRotations(commands, str -> {
            Arrays.stream(buttons).forEach(button -> button.setDisable(false));
        });

    }


    // scramble after solve messes everything up
    private void scrambleButtonOnClick(MouseEvent event, Button[] buttons){
        String[] availableMoves = {"R", "R'", "R2",
                                   "L", "L'", "L2", 
                                   "U", "U'", "U2",
                                   "D", "D'", "D2",
                                   "F", "F'", "F2",
                                   "B", "B'", "B2",};
        
        String[] scramble = new String[30];
        Arrays.stream(buttons).forEach(button -> button.setDisable(true));
        for (int i = 0; i < 30; i++){
            String nextMove = availableMoves[(int)Math.round(Math.random() * 17)];
            if (i > 0 && nextMove.charAt(0) == scramble[i-1].charAt(0)) i--;
            else scramble[i] = nextMove;
        }
        cube.makeRotations(scramble, str -> {
            Arrays.stream(buttons).forEach(button -> button.setDisable(false));
        });
    }

    private void getSolveSequenceOnClick(MouseEvent event, TextField lb){
        String scramble = cube.getCubeStringDefinition();
        String solution;
        System.out.println(scramble);
        solution = new Search().solution(scramble, 21, 100000000, 10000, 0);
        lb.setText(solution);
    }

    private VBox prepareControls(){
        List<Button> movementButtons = new ArrayList<>();
        TextField solution = new TextField();

        TextField input = new TextField();
        input.setPromptText("R U R' U' F2 ...");

        Button scrambleButton = new Button("Scramble");
        Button getSolveSequence = new Button("Get Solve Sequence");
        Button executeButton = new Button("Execute");
        Button solve = new Button("Solve");


        scrambleButton.setMinWidth(70);
        scrambleButton.setOnMouseClicked(event ->{
            Button[] arr = {executeButton, getSolveSequence, scrambleButton } ;
            movementButtons.addAll( Arrays.asList(arr));
            arr = movementButtons.toArray(arr);
            executeOnclick(event, solution, arr);
            scrambleButtonOnClick(event, arr);
        });

        
        getSolveSequence.setMinWidth(70);
        getSolveSequence.setOnMouseClicked(event ->{
            getSolveSequenceOnClick(event, solution);
            solve.setDisable(false);
        } );

        solve.setMinWidth(70);
        solve.setDisable(true);
        solve.setOnMouseClicked(event -> {
            solve.setDisable(true);
            Button[] arr = {executeButton, getSolveSequence, scrambleButton } ;
            movementButtons.addAll( Arrays.asList(arr));
            arr = movementButtons.toArray(arr);
            executeOnclick(event, solution, arr);
        });

        
        executeButton.setMinWidth(70);
        executeButton.setOnMouseClicked(event -> {
            Button[] arr = {executeButton, solve, getSolveSequence, scrambleButton } ;
            movementButtons.addAll( Arrays.asList(arr));
            arr = movementButtons.toArray(arr);
            executeOnclick(event, input, arr);
        });

        HBox buttonRow = new HBox();
        buttonRow.setSpacing(5);
        HBox buttonRow2 = new HBox();
        buttonRow2.setSpacing(5);

        
        for (int i = 0; i < 12; i++){
            Button buttonToAdd;
            if (i == 0)  buttonToAdd = new Button("R");
            else if (i == 1) buttonToAdd = new Button("L");
            else if (i == 2) buttonToAdd = new Button("U");
            else if (i == 3) buttonToAdd = new Button("D");
            else if (i == 4) buttonToAdd = new Button("F");
            else if (i == 5) buttonToAdd = new Button("B");
            else if (i == 6) buttonToAdd = new Button("R'");
            else if (i == 7) buttonToAdd = new Button("L'");
            else if (i == 8) buttonToAdd = new Button("U'");
            else if (i == 9) buttonToAdd = new Button("D'");
            else if (i == 10) buttonToAdd = new Button("F'");
            else buttonToAdd = new Button("B'");

            if (i > 5) buttonRow2.getChildren().add(buttonToAdd);
            else buttonRow.getChildren().add(buttonToAdd);
            movementButtons.add(buttonToAdd);

            buttonToAdd.setMinWidth(30);
            buttonToAdd.setOnMouseClicked(click -> {
                movementButtons.stream().forEach(button -> button.setDisable(true));
                solve.setDisable(true);
                scrambleButton.setDisable(true);
                getSolveSequence.setDisable(true);
                executeButton.setDisable(true);
                cube.rotateFace(buttonToAdd.getText(), str -> {
                    movementButtons.stream().forEach(button -> button.setDisable(false));
                    scrambleButton.setDisable(false);
                    getSolveSequence.setDisable(false);
                    executeButton.setDisable(false);
                });
                

            });
        }

        HBox inputContainer = new HBox(input, executeButton);
        HBox scrambleSolveContainer = new HBox(scrambleButton, solve);
        scrambleSolveContainer.setSpacing(20);
        scrambleSolveContainer.setAlignment(Pos.CENTER);

        VBox controlRoot = new VBox(buttonRow, buttonRow2, inputContainer, scrambleSolveContainer, getSolveSequence, solution );
        // controlRoot.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        controlRoot.setSpacing(20);
        controlRoot.setAlignment(Pos.TOP_CENTER);
        controlRoot.setPadding(new Insets(10,10,0,0));
        return controlRoot;
    }

    public static void main(String[] args) {
        launch(args);
    }

}




