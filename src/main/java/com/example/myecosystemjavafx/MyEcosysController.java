package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.*;
import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import static com.example.myecosystemjavafx.EmojiLoader.loadEmotionImages;
import static com.example.myecosystemjavafx.Constants.*;

public class MyEcosysController {
    
    private static ArrayList<UIObject> objectsList = new ArrayList<>();
    private AnimationTimer animationTimer;
    private boolean isPaused = false;
    
    private MyEcosysTimeManager myEcosysTimeManager;
    private MyEcosysSeasonManager myEcosysSeasonManager;
    private MyEcosysLifeSim myEcosysLifeSim;
    
    @FXML
    private Canvas canvas;
    private GraphicsContext gc;
    
    @FXML
    private Button AddCarnivoraButton2;
    
    @FXML
    private Button AddCarnivoraButton2X10;
    
    @FXML
    private Button AddCarnivoraButton2X25;
    
    @FXML
    private Button AddCarnivoraButton2X5;
    
    @FXML
    private Button AddCarnivoraButton3;
    
    @FXML
    private Button AddCarnivoraButton3X10;
    
    @FXML
    private Button AddCarnivoraButton3X25;
    
    @FXML
    private Button AddCarnivoraButton3X5;
    
    @FXML
    private Button AddDeerButton;
    
    @FXML
    private Button AddDeerButtonX10;
    
    @FXML
    private Button AddDeerButtonX25;
    
    @FXML
    private Button AddDeerButtonX5;
    
    @FXML
    private Button AddHerbivoryButton3;
    
    @FXML
    private Button AddHerbivoryButton3X10;
    
    @FXML
    private Button AddHerbivoryButton3X25;
    
    @FXML
    private Button AddHerbivoryButton3X5;
    
    @FXML
    private Button AddMediumShrubButton;
    
    @FXML
    private Button AddMediumShrubButtonX10;
    
    @FXML
    private Button AddMediumShrubButtonX25;
    
    @FXML
    private Button AddMediumShrubButtonX5;
    
    @FXML
    private Button AddSmallShrubButton;
    
    @FXML
    private Button AddSmallShrubButtonX10;
    
    @FXML
    private Button AddSmallShrubButtonX25;
    
    @FXML
    private Button AddSmallShrubButtonX5;
    
    @FXML
    private Button AddRabbitButton;
    
    @FXML
    private Button AddRabbitButtonX10;
    
    @FXML
    private Button AddRabbitButtonX25;
    
    @FXML
    private Button AddRabbitButtonX5;
    
    @FXML
    private Button AddTreeButton;
    
    @FXML
    private Button AddTreeButtonX10;
    
    @FXML
    private Button AddTreeButtonX25;
    
    @FXML
    private Button AddTreeButtonX5;
    
    @FXML
    private Button AddWolfButton;
    
    @FXML
    private Button AddWolfButtonX10;
    
    @FXML
    private Button AddWolfButtonX25;
    
    @FXML
    private Button AddWolfButtonX5;
    
    @FXML
    private Button DeleteLastObjectButton;
    
    @FXML
    private Button PauseButton;
    
    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);
        
        myEcosysTimeManager = new MyEcosysTimeManager();
        myEcosysSeasonManager = new MyEcosysSeasonManager();
        myEcosysLifeSim = new MyEcosysLifeSim(objectsList);
        
        myEcosysSeasonManager.loadBackgroundImages();
        ImageLoader.loadObjectsImages();
        loadEmotionImages();
        setupAnimationTimer();
        startAnimation();
    }
    
    private void redrawCanvas() {
        if (myEcosysSeasonManager.isBackgroundImageLoaded()) {
            gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            gc.drawImage(myEcosysSeasonManager.getCurrentBackgroundImage(), 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(myEcosysSeasonManager.getCurrentReserveColor());
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }
    
    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return;
                
                if (!myEcosysTimeManager.shouldRender(now)) {
                    return;
                }
                
                if (myEcosysTimeManager.getLastFrameTime() == 0) {
                    myEcosysTimeManager.initializeTime(now);
                    return;
                }
                
                myEcosysTimeManager.updateTime(now);
                redrawCanvas();
                drawObjects();
                
                if (myEcosysTimeManager.shouldUpdateLife(now)) {
                    myEcosysLifeSim.lifeSimulation();
                }
                
                if (myEcosysTimeManager.shouldUpdateSeason(now)) {
                    myEcosysSeasonManager.nextSeason();
                    myEcosysSeasonManager.oldDeadSimulation(objectsList);
                    myEcosysTimeManager.updateSeasonTime(now);
                }
                
                if (myEcosysTimeManager.shouldUpdateYear(now)) {
                    myEcosysSeasonManager.nextYear(objectsList);
                    myEcosysLifeSim.reproductionSimulation();
                    myEcosysTimeManager.updateYearTime(now);
                }
            }
        };
    }
    
    private void drawObjects() {
        if (objectsList == null) {
            return;
        }
        for (UIObject object : objectsList) {
            object.printObject(gc, myEcosysTimeManager.getAlpha());
            EmojiLoader.drawEmotion(gc, object);
        }
    }
    
    public void startAnimation() {
        if (animationTimer != null) {
            animationTimer.start();
            isPaused = false;
        }
    }
    
    public void pauseAnimation() {
        isPaused = true;
        drawPauseIndicator();
    }
    
    public void resumeAnimation() {
        isPaused = false;
        long currentTime = System.nanoTime();
        myEcosysTimeManager.initializeTime(currentTime);
        
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer.start();
        }
    }
    
    private void drawPauseIndicator() {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeRect(2, 2, CANVAS_WIDTH - 4, CANVAS_HEIGHT - 4);
        
        double size = 8;
        double spacing = 4;
        double x = CANVAS_WIDTH - 30;
        double y = 15;
        gc.setFill(Color.RED);
        gc.fillRect(x, y, size, 2.5 * size);
        gc.fillRect(x + size + spacing, y, size, 2.5 * size);
    }
    
    @FXML
    void addWolf(ActionEvent event) {
        addWolfs(1);
    }
    
    @FXML
    void AddWolfX5(ActionEvent event) {
        addWolfs(5);
    }
    
    @FXML
    void AddWolfX10(ActionEvent event) {
        addWolfs(10);
    }
    
    @FXML
    void AddWolfX25(ActionEvent event) {
        addWolfs(25);
    }
    
    @FXML
    void addDeer(ActionEvent event) {
        addDeers(1);
    }
    
    @FXML
    void addDeerX5(ActionEvent event) {
        addDeers(5);
    }
    
    @FXML
    void addDeerX10(ActionEvent event) {
        addDeers(10);
    }
    
    @FXML
    void addDeerX25(ActionEvent event) {
        addDeers(25);
    }
    
    @FXML
    void addRabbit(ActionEvent event) {
        addRabbits(1);
    }
    
    @FXML
    void addRabbitX5(ActionEvent event) {
        addRabbits(5);
    }
    
    @FXML
    void addRabbitX10(ActionEvent event) {
        addRabbits(10);
    }
    
    @FXML
    void addRabbitX25(ActionEvent event) {
        addRabbits(25);
    }
    
    @FXML
    void addTree(ActionEvent event) {
        addTrees(1);
    }
    
    @FXML
    void addTreeX5(ActionEvent event) {
        addTrees(5);
    }
    
    @FXML
    void addTreeX10(ActionEvent event) {
        addTrees(10);
    }
    
    @FXML
    void addTreeX25(ActionEvent event) {
        addTrees(25);
    }
    
    @FXML
    void addMediumShrub(ActionEvent event) {
        addMediumShrubs(1);
    }
    
    @FXML
    void addMediumShrubX5(ActionEvent event) {
        addMediumShrubs(5);
    }
    
    @FXML
    void addMediumShrubX10(ActionEvent event) {
        addMediumShrubs(10);
    }
    
    @FXML
    void addMediumShrubX25(ActionEvent event) {
        addMediumShrubs(25);
    }
    
    @FXML
    void addSmallShrub(ActionEvent event) {
        addSmallShrubs(1);
    }
    
    @FXML
    void addSmallShrubX5(ActionEvent event) {
        addSmallShrubs(5);
    }
    
    @FXML
    void addSmallShrubX10(ActionEvent event) {
        addSmallShrubs(10);
    }
    
    @FXML
    void addSmallShrubX25(ActionEvent event) {
        addSmallShrubs(25);
    }
    
    @FXML
    void deleteLastObject(ActionEvent event) {
        if (UIObject.getCounterObjects() != 0) {
            objectsList.remove(UIObject.getCounterObjects() - 1);
            UIObject.decrementCounterObject();
        }
    }
    
    @FXML
    void togglePause(ActionEvent event) {
        if (isPaused) {
            resumeAnimation();
        } else {
            pauseAnimation();
        }
    }
    
    void addWolfs(int countCarnivoras) {
        for (int i = 0; i < countCarnivoras; i++) {
            objectsList.add(UIObject.getCounterObjects(), new CarnivoraWolf());
        }
    }
    
    void addDeers(int countHerbivores) {
        for (int i = 0; i < countHerbivores; i++) {
            objectsList.add(UIObject.getCounterObjects(), new HerbivoryDeer());
        }
    }
    
    void addRabbits(int countHerbivores) {
        for (int i = 0; i < countHerbivores; i++) {
            objectsList.add(UIObject.getCounterObjects(), new HerbivoryRabbit());
        }
    }
    
    void addTrees(int countPlants) {
        for (int i = 0; i < countPlants; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantTree());
        }
    }
    
    void addMediumShrubs(int countPlants) {
        for (int i = 0; i < countPlants; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantMediumShrub());
        }
    }
    
    void addSmallShrubs(int countPlants) {
        for (int i = 0; i < countPlants; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantSmallShrub());
        }
    }
}