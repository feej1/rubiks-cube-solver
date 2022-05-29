package com.rubiksproject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.animation.Animation.Status;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

public class Cube {
    
    private List<Node> cubeNodeList;
    private List<Rectangle> blueStickers;
    private List<Rectangle> redStickers;
    private List<Rectangle> greenStickers;
    private List<Rectangle> orangeStickers;
    private List<Rectangle> yellowStickers;
    private List<Rectangle> whiteStickers;


    private Group root;

    final private int LENGTH = 100; 
    final private int GAP = 3; 
    final private int STICKER_MARGIN = 3;
    Thread t;

    public Cube(){
        cubeNodeList = new ArrayList<>();
        blueStickers = new ArrayList<>();
        redStickers = new ArrayList<>();
        greenStickers = new ArrayList<>();
        orangeStickers = new ArrayList<>();
        yellowStickers = new ArrayList<>();
        whiteStickers = new ArrayList<>();
        
        root = new Group();
        addCubeNodes(cubeNodeList);
        addStickerNodes(cubeNodeList);
        nodesToCublets(cubeNodeList);  
    }

    
    public String getCubeStringDefinition(){
        //UUUUUUUUURRRRRRRRRFFFFFFFFFDDDDDDDDDLLLLLLLLLBBBBBBBBB
        String def = "";

        for (int i = 0; i < 6; i++){
            List<Rectangle> stickerList;
            if (i == 0 ) stickerList = yellowStickers;
            else if(i == 1)  stickerList = redStickers;
            else if(i == 2)  stickerList = blueStickers;
            else if(i == 3)  stickerList = whiteStickers;
            else if(i == 4)  stickerList = orangeStickers;
            else stickerList = greenStickers;
            for (int j = 0; j<3; j++){
                for (int k = 0; k < 3; k++){
                    Rectangle sticker = stickerList.get((k * 3)+j);
                    if (sticker.getFill() == Color.YELLOW ) def += "U";
                    else if(sticker.getFill() == Color.RED ) def += "R";
                    else if(sticker.getFill() == Color.SKYBLUE ) def += "F";
                    else if(sticker.getFill() == Color.WHITESMOKE) def += "D";
                    else if(sticker.getFill() == Color.ORANGE ) def += "L";
                    else def += "B";
                }
            }
        }
        return def;
    }

   

    public void makeRotations(String[] commands, Consumer<String> func){
        t = new Thread(){
            public void run(){
                for (int i = 0; i < commands.length; i++){
                    rotateFace(commands[i]);
                    try{sleep(600);} catch(Exception e){e.printStackTrace();}
                }
                func.accept("");
            }
        };
        try{
            t.start();
        }catch(Exception e){System.out.println("error");}
        
    }

    public void makeRotations(String[] commands){
        t = new Thread(){
            public void run(){
                for (int i = 0; i < commands.length; i++){
                    rotateFace(commands[i]);
                    try{sleep(600);} catch(Exception e){e.printStackTrace();}
                }
            }
        };
        try{
            t.start();
        }catch(Exception e){System.out.println("error");}
    }

    public void rotateFace(String command, Consumer<String> func){

        
        Rotate rot = new Rotate();
        Timeline animation = new Timeline();
        int angle = 0;
        Predicate<Node> getNodesToAnimate = node -> true;
        EventHandler<ActionEvent> removeAndSwitch = event -> {
            // empty for now
        };

        switch (command){
            case "R" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    R();
                }; 
                break;
            case "R'" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear());
                    func.accept(command);
                    R(); R(); R(); 
                }; 
                break;
            case "R2" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear());
                    func.accept(command);
                    R(); R(); 
                    
                }; 
                break;
            case "L" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    L();
                    
                }; 
                break;
            case "L'" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    L(); L(); L();
                }; 
                break;
            case "L2" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    L(); L();
                    
                }; 
                break;
            case "F" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    F();  
                }; 
                break;
            case "F'" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    F(); F(); F();
                    
                }; 
                break;
            case "F2" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    F(); F();
                    
                }; 
                break;
            case "U" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    U();
                    
                }; 
                break;
            case "U'" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    U(); U(); U();
                    
                }; 
                break;
            case "U2" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    U();U();
                }; 
                break;
            case "B":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    B();
                }; 
                break;
            case "B'":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    B(); B(); B();
                }; 
                break;
            case "B2":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    B(); B();
                }; 
                break;
            case "D":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    D();
                }; 
                break;
            case "D'":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    D(); D(); D();
                }; 
                break;
            case "D2":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    func.accept(command);
                    D(); D();
                }; 
                break;
            default: return;
        }

        Rotate rotate = rot;
        animation.getKeyFrames().addAll(new KeyFrame(Duration.seconds(0), new KeyValue(rotate.angleProperty(), 0)),new KeyFrame(Duration.seconds(.5), new KeyValue(rotate.angleProperty(), angle)));
        root.getChildren()
            .filtered(getNodesToAnimate)
            .forEach(node -> node.getTransforms().add(rotate));

        animation.setCycleCount(1);
        animation.setOnFinished(removeAndSwitch);
        animation.play();
        
    }

    public void rotateFace(String command){
        Rotate rot = new Rotate();
        Timeline animation = new Timeline();
        int angle = 0;
        Predicate<Node> getNodesToAnimate = node -> true;
        EventHandler<ActionEvent> removeAndSwitch = event -> {
            // empty for now
        };

        switch (command){
            case "R" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                    R();
                }; 
                break;
            case "R'" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear());
                         
                
                    R(); R(); R();
                    
                }; 
                break;
            case "R2" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() > 0 )
                        .forEach(node -> node.getTransforms().clear());
                         
                
                    R(); R(); 
                    
                }; 
                break;
            case "L" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    L();
                    
                }; 
                break;
            case "L'" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    L(); L(); L();
                    
                }; 
                break;
            case "L2" :
                rot = new Rotate(0, Rotate.X_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterX() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterX() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    L(); L();
                    
                }; 
                break;
            case "F" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    F();
                    
                }; 
                break;
            case "F'" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    F(); F(); F();
                    
                }; 
                break;
            case "F2" :
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    F(); F();
                    
                }; 
                break;
            case "U" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    U();
                    
                }; 
                break;
            case "U'" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    U(); U(); U();
                    
                }; 
                break;
            case "U2" :
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() < 0;
                angle = 180;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() < 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    U();U();
                    
                }; 
                break;
            case "B":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    B();
                    
                }; 
                break;
            case "B'":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    B(); B(); B();
                    
                }; 
                break;
            case "B2":
                rot = new Rotate(0, Rotate.Z_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterZ() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterZ() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    B(); B();
                    
                }; 
                break;
            case "D":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = -90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    D();
                    
                }; 
                break;
            case "D'":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = 90;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    D(); D(); D();
                    
                }; 
                break;
            case "D2":
                rot = new Rotate(0, Rotate.Y_AXIS);
                getNodesToAnimate = node -> node.boundsInParentProperty().getValue().getCenterY() > 0;
                angle = 180;
                removeAndSwitch = event -> {
                    // remove the roation 
                    
                    root.getChildren()
                        .filtered(node -> node.boundsInParentProperty().getValue().getCenterY() > 0 )
                        .forEach(node -> node.getTransforms().clear() );
                    
                
                    D(); D();
                    
                }; 
                break;
        }

        Rotate rotate = rot;
        animation.getKeyFrames().addAll(new KeyFrame(Duration.seconds(0), new KeyValue(rotate.angleProperty(), 0)),new KeyFrame(Duration.seconds(.5), new KeyValue(rotate.angleProperty(), angle)));
        root.getChildren()
            .filtered(getNodesToAnimate)
            .forEach(node -> node.getTransforms().add(rotate));

        animation.setCycleCount(1);
        animation.setOnFinished(removeAndSwitch);
        animation.play();
        
    }

    public ObservableList<Node> asNodeList(){
        return root.getChildren();
    }

    public Node asNode(){
        return root;
    }

    private void D(){
        Rectangle rect0 = whiteStickers.get(0);
        Rectangle rect2 = whiteStickers.get(2);
        Rectangle rect6 = whiteStickers.get(6);
        Rectangle rect8 = whiteStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = whiteStickers.get(1);
        Rectangle rect3 = whiteStickers.get(3);
        Rectangle rect5 = whiteStickers.get(5);
        Rectangle rect7 = whiteStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect2Blu = blueStickers.get(2);
        Rectangle rect5Blu = blueStickers.get(5);
        Rectangle rect8Blu = blueStickers.get(8);

        Rectangle rect2red = redStickers.get(2);
        Rectangle rect5red = redStickers.get(5);
        Rectangle rect8red = redStickers.get(8);

        Rectangle rect2green = greenStickers.get(2);
        Rectangle rect5green = greenStickers.get(5);
        Rectangle rect8green = greenStickers.get(8);

        Rectangle rect2orange = orangeStickers.get(2);
        Rectangle rect5orange = orangeStickers.get(5);
        Rectangle rect8orange = orangeStickers.get(8);

        Paint temp2 = rect2green.getFill();
        Paint temp5 = rect5green.getFill();
        Paint temp8 = rect8green.getFill();

        rect2green.setFill(rect2red.getFill());
        rect5green.setFill(rect5red.getFill());
        rect8green.setFill(rect8red.getFill());

        rect2red.setFill(rect2Blu.getFill());
        rect5red.setFill(rect5Blu.getFill());
        rect8red.setFill(rect8Blu.getFill());

        rect2Blu.setFill(rect2orange.getFill());
        rect5Blu.setFill(rect5orange.getFill());
        rect8Blu.setFill(rect8orange.getFill());

        rect2orange.setFill(temp2);
        rect5orange.setFill(temp5);
        rect8orange.setFill(temp8);
    }

    private void B(){
        Rectangle rect0 = greenStickers.get(0);
        Rectangle rect2 = greenStickers.get(2);
        Rectangle rect6 = greenStickers.get(6);
        Rectangle rect8 = greenStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = greenStickers.get(1);
        Rectangle rect3 = greenStickers.get(3);
        Rectangle rect5 = greenStickers.get(5);
        Rectangle rect7 = greenStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect0yel = yellowStickers.get(0);
        Rectangle rect3yel = yellowStickers.get(3);
        Rectangle rect6yel = yellowStickers.get(6);

        Rectangle rect6red = redStickers.get(6);
        Rectangle rect7red = redStickers.get(7);
        Rectangle rect8red = redStickers.get(8);

        Rectangle rect2whi = whiteStickers.get(2);
        Rectangle rect5whi = whiteStickers.get(5);
        Rectangle rect8whi = whiteStickers.get(8);

        Rectangle rect0orange = orangeStickers.get(0);
        Rectangle rect1orange = orangeStickers.get(1);
        Rectangle rect2orange = orangeStickers.get(2);

        Paint temp0 = rect0yel.getFill();
        Paint temp3 = rect3yel.getFill();
        Paint temp6 = rect6yel.getFill();

        rect0yel.setFill(rect6red.getFill());
        rect3yel.setFill(rect7red.getFill());
        rect6yel.setFill(rect8red.getFill());

        rect6red.setFill(rect8whi.getFill());
        rect7red.setFill(rect5whi.getFill());
        rect8red.setFill(rect2whi.getFill());

        rect2whi.setFill(rect0orange.getFill());
        rect5whi.setFill(rect1orange.getFill());
        rect8whi.setFill(rect2orange.getFill());

        rect0orange.setFill(temp6);
        rect1orange.setFill(temp3);
        rect2orange.setFill(temp0);
    }

    private void F(){
        Rectangle rect0 = blueStickers.get(0);
        Rectangle rect2 = blueStickers.get(2);
        Rectangle rect6 = blueStickers.get(6);
        Rectangle rect8 = blueStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = blueStickers.get(1);
        Rectangle rect3 = blueStickers.get(3);
        Rectangle rect5 = blueStickers.get(5);
        Rectangle rect7 = blueStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect2yel = yellowStickers.get(2);
        Rectangle rect5yel = yellowStickers.get(5);
        Rectangle rect8yel = yellowStickers.get(8);

        Rectangle rect0red = redStickers.get(0);
        Rectangle rect1red = redStickers.get(1);
        Rectangle rect2red = redStickers.get(2);

        Rectangle rect0whi = whiteStickers.get(0);
        Rectangle rect3whi = whiteStickers.get(3);
        Rectangle rect6whi = whiteStickers.get(6);

        Rectangle rect6orange = orangeStickers.get(6);
        Rectangle rect7orange = orangeStickers.get(7);
        Rectangle rect8orange = orangeStickers.get(8);

        Paint temp2 = rect2yel.getFill();
        Paint temp5 = rect5yel.getFill();
        Paint temp8 = rect8yel.getFill();

        rect2yel.setFill(rect8orange.getFill());
        rect5yel.setFill(rect7orange.getFill());
        rect8yel.setFill(rect6orange.getFill());

        rect8orange.setFill(rect6whi.getFill());
        rect7orange.setFill(rect3whi.getFill());
        rect6orange.setFill(rect0whi.getFill());

        rect6whi.setFill(rect0red.getFill());
        rect3whi.setFill(rect1red.getFill());
        rect0whi.setFill(rect2red.getFill());

        rect0red.setFill(temp2);
        rect1red.setFill(temp5);
        rect2red.setFill(temp8);
    }

    private void L(){
        Rectangle rect0 = orangeStickers.get(0);
        Rectangle rect2 = orangeStickers.get(2);
        Rectangle rect6 = orangeStickers.get(6);
        Rectangle rect8 = orangeStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = orangeStickers.get(1);
        Rectangle rect3 = orangeStickers.get(3);
        Rectangle rect5 = orangeStickers.get(5);
        Rectangle rect7 = orangeStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect0Blu = blueStickers.get(0);
        Rectangle rect1Blu = blueStickers.get(1);
        Rectangle rect2Blu = blueStickers.get(2);

        Rectangle rect0yel = yellowStickers.get(0);
        Rectangle rect1yel = yellowStickers.get(1);
        Rectangle rect2yel = yellowStickers.get(2);

        Rectangle rect0whi = whiteStickers.get(0);
        Rectangle rect1whi = whiteStickers.get(1);
        Rectangle rect2whi = whiteStickers.get(2);

        Rectangle rect6green = greenStickers.get(6);
        Rectangle rect7green = greenStickers.get(7);
        Rectangle rect8green = greenStickers.get(8);

        Paint temp6 = rect6green.getFill();
        Paint temp7 = rect7green.getFill();
        Paint temp8 = rect8green.getFill();

        rect6green.setFill(rect2whi.getFill());
        rect7green.setFill(rect1whi.getFill());
        rect8green.setFill(rect0whi.getFill());

        rect0whi.setFill(rect0Blu.getFill());
        rect1whi.setFill(rect1Blu.getFill());
        rect2whi.setFill(rect2Blu.getFill());

        rect0Blu.setFill(rect0yel.getFill());
        rect1Blu.setFill(rect1yel.getFill());
        rect2Blu.setFill(rect2yel.getFill());

        rect0yel.setFill(temp8);
        rect1yel.setFill(temp7);
        rect2yel.setFill(temp6);
    }

    private void U(){
        Rectangle rect0 = yellowStickers.get(0);
        Rectangle rect2 = yellowStickers.get(2);
        Rectangle rect6 = yellowStickers.get(6);
        Rectangle rect8 = yellowStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = yellowStickers.get(1);
        Rectangle rect3 = yellowStickers.get(3);
        Rectangle rect5 = yellowStickers.get(5);
        Rectangle rect7 = yellowStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect0Blu = blueStickers.get(0);
        Rectangle rect3Blu = blueStickers.get(3);
        Rectangle rect6Blu = blueStickers.get(6);

        Rectangle rect0red = redStickers.get(0);
        Rectangle rect3red = redStickers.get(3);
        Rectangle rect6red = redStickers.get(6);

        Rectangle rect0green = greenStickers.get(0);
        Rectangle rect3green = greenStickers.get(3);
        Rectangle rect6green = greenStickers.get(6);

        Rectangle rect0orange = orangeStickers.get(0);
        Rectangle rect3orange = orangeStickers.get(3);
        Rectangle rect6orange = orangeStickers.get(6);

        Paint temp0 = rect0green.getFill();
        Paint temp3 = rect3green.getFill();
        Paint temp6 = rect6green.getFill();

        rect0green.setFill(rect0orange.getFill());
        rect3green.setFill(rect3orange.getFill());
        rect6green.setFill(rect6orange.getFill());

        rect0orange.setFill(rect0Blu.getFill());
        rect3orange.setFill(rect3Blu.getFill());
        rect6orange.setFill(rect6Blu.getFill());

        rect0Blu.setFill(rect0red.getFill());
        rect3Blu.setFill(rect3red.getFill());
        rect6Blu.setFill(rect6red.getFill());

        rect0red.setFill(temp0);
        rect3red.setFill(temp3);
        rect6red.setFill(temp6);
    }

    private void R(){
        Rectangle rect0 = redStickers.get(0);
        Rectangle rect2 = redStickers.get(2);
        Rectangle rect6 = redStickers.get(6);
        Rectangle rect8 = redStickers.get(8);
        Paint temp = rect0.getFill();
        rect0.setFill(rect2.getFill());
        rect2.setFill(rect8.getFill());
        rect8.setFill(rect6.getFill());
        rect6.setFill(temp);
                
        Rectangle rect1 = redStickers.get(1);
        Rectangle rect3 = redStickers.get(3);
        Rectangle rect5 = redStickers.get(5);
        Rectangle rect7 = redStickers.get(7);
        temp = rect1.getFill();
        rect1.setFill(rect5.getFill());
        rect5.setFill(rect7.getFill());
        rect7.setFill(rect3.getFill());
        rect3.setFill(temp);


        Rectangle rect6Blu = blueStickers.get(6);
        Rectangle rect7Blu = blueStickers.get(7);
        Rectangle rect8Blu = blueStickers.get(8);

        Rectangle rect6yel = yellowStickers.get(6);
        Rectangle rect7yel = yellowStickers.get(7);
        Rectangle rect8yel = yellowStickers.get(8);

        Rectangle rect6whi = whiteStickers.get(6);
        Rectangle rect7whi = whiteStickers.get(7);
        Rectangle rect8whi = whiteStickers.get(8);

        Rectangle rect0green = greenStickers.get(0);
        Rectangle rect1green = greenStickers.get(1);
        Rectangle rect2green = greenStickers.get(2);

        Paint temp0 = rect0green.getFill();
        Paint temp1 = rect1green.getFill();
        Paint temp2 = rect2green.getFill();

        rect0green.setFill(rect8yel.getFill());
        rect1green.setFill(rect7yel.getFill());
        rect2green.setFill(rect6yel.getFill());

        rect6yel.setFill(rect6Blu.getFill());
        rect7yel.setFill(rect7Blu.getFill());
        rect8yel.setFill(rect8Blu.getFill());

        rect6Blu.setFill(rect6whi.getFill());
        rect7Blu.setFill(rect7whi.getFill());
        rect8Blu.setFill(rect8whi.getFill());

        rect6whi.setFill(temp2);
        rect7whi.setFill(temp1);
        rect8whi.setFill(temp0);
    }

    private void nodesToCublets(List<Node> nodeList){
        
        Group[][][] cublets = {{{new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}}, {{new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}}, {{new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}, {new Group(),new Group(),new Group()}}};
        int x,y,z;

        for (int i = 0; i < nodeList.size(); i++){
            if (nodeList.get(i).boundsInParentProperty().getValue().getCenterX() > 0) x = 2;
            else if(nodeList.get(i).boundsInParentProperty().getValue().getCenterX() < 0) x = 0;
            else x = 1;
            
            if (nodeList.get(i).boundsInParentProperty().getValue().getCenterY() > 0) y = 2;
            else if(nodeList.get(i).boundsInParentProperty().getValue().getCenterY() < 0) y = 0;
            else y = 1;

            if (nodeList.get(i).boundsInParentProperty().getValue().getCenterZ() > 0) z = 2;
            else if(nodeList.get(i).boundsInParentProperty().getValue().getCenterZ() < 0) z = 0;
            else z = 1;

            cublets[x][y][z].getChildren().add(nodeList.get(i));
        }
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    root.getChildren().add(cublets[i][j][k]);
                }
            }
        }
    }

    private void addCubeNodes(List<Node> nodeList){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 3; k++){
                    Box cublet = new Box(LENGTH, LENGTH, LENGTH);
                    int offset = LENGTH + GAP;
                    cublet.getTransforms().add(new Translate(-(offset) + (i * offset),-(offset) + (j * offset),-(offset) + (k * offset))); 
                    cublet.setMaterial(new PhongMaterial(Color.BLACK));
                    cubeNodeList.add(cublet);
                }
            }
        }
    }

    private void addStickerNodes(List<Node> nodeList){
        for (int i = 0; i < 3; i++){
            for (int j = 0; j < 3; j++){
                for (int k = 0; k < 6; k++){
                    Rectangle sticker = new Rectangle(LENGTH - STICKER_MARGIN, LENGTH - STICKER_MARGIN);
                    int offset = LENGTH + GAP;
                    switch (k) {
                        case 0:
                            sticker.getTransforms().add(
                                new Translate(-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (i * offset),-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (j * offset), -((1.5 * LENGTH) + GAP +.3 ))
                            );
                            sticker.setFill(Color.SKYBLUE);
                            blueStickers.add(sticker);
                            break;
                        case 1:
                            sticker.getTransforms().add(
                                new Translate(((.5 * LENGTH) + GAP + ((double)STICKER_MARGIN / 2)) - (i * offset),-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (j * offset), ((1.5 * LENGTH) + GAP +.3 ))
                            );
                            sticker.setFill(Color.DARKSEAGREEN);
                            greenStickers.add(sticker);
                            break;
                        case 2:
                            sticker.getTransforms().addAll(
                                new Translate((1.5 * LENGTH) + GAP +.3, -((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (j * offset),-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (i * offset) ),
                                new Rotate(-90, Rotate.Y_AXIS)
                            );
                            sticker.setFill(Color.RED);
                            redStickers.add(sticker);
                            break;
                        case 3:
                            sticker.getTransforms().addAll(
                                new Translate(-((1.5 * LENGTH) + GAP +.3), -((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (j * offset),((.5 * LENGTH) + GAP + ((double)STICKER_MARGIN / 2)) - (i * offset)),
                                new Rotate(-90, Rotate.Y_AXIS)
                            );
                            sticker.setFill(Color.ORANGE);
                            orangeStickers.add(sticker);
                            break;
                        case 4:
                            sticker.getTransforms().addAll(
                                new Translate(-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (i * offset),(1.5 * LENGTH) + GAP +.3 ,-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (j * offset)),
                                new Rotate(90, Rotate.X_AXIS)
                            );
                            sticker.setFill(Color.WHITESMOKE);
                            whiteStickers.add(sticker);
                            break;
                        case 5:
                            sticker.getTransforms().addAll(
                                new Translate(-((1.5 * LENGTH) + GAP - ((double)STICKER_MARGIN / 2)) + (i * offset),-((1.5 * LENGTH) + GAP +.3 ), ((.5 * LENGTH) + GAP + ((double)STICKER_MARGIN / 2)) - (j * offset)),
                                new Rotate(90, Rotate.X_AXIS)
                            );
                            sticker.setFill(Color.YELLOW);
                            yellowStickers.add(sticker);
                            break;  
                    }
                    cubeNodeList.add(sticker);
                } 
            }
        }
    }


}
