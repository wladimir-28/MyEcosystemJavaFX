package com.example.myecosystemjavafx;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import static com.example.myecosystemjavafx.EmojiLoader.loadEmotionImages;

public class MyEcosysController {
    
    private MyEcosysUIManager uiManager;
    private MyEcosysEntityManager objectManager;
    private MyEcosysTimeManager timeManager;
    private MyEcosysSeasonManager seasonManager;
    private MyEcosysLifeSim lifeSim;
    
    private AnimationTimer animationTimer;
    private boolean isPaused = false;
    
    @FXML private Canvas canvas;
    private GraphicsContext gc;
    
    @FXML private Button AddCarnivoraButton2;
    @FXML private Button AddCarnivoraButton2X10;
    @FXML private Button AddCarnivoraButton2X25;
    @FXML private Button AddCarnivoraButton2X5;
    @FXML private Button AddCarnivoraButton3;
    @FXML private Button AddCarnivoraButton3X10;
    @FXML private Button AddCarnivoraButton3X25;
    @FXML private Button AddCarnivoraButton3X5;
    @FXML private Button AddDeerButton;
    @FXML private Button AddDeerButtonX10;
    @FXML private Button AddDeerButtonX25;
    @FXML private Button AddDeerButtonX5;
    @FXML private Button AddHerbivoryButton3;
    @FXML private Button AddHerbivoryButton3X10;
    @FXML private Button AddHerbivoryButton3X25;
    @FXML private Button AddHerbivoryButton3X5;
    @FXML private Button AddMediumShrubButton;
    @FXML private Button AddMediumShrubButtonX10;
    @FXML private Button AddMediumShrubButtonX25;
    @FXML private Button AddMediumShrubButtonX5;
    @FXML private Button AddSmallShrubButton;
    @FXML private Button AddSmallShrubButtonX10;
    @FXML private Button AddSmallShrubButtonX25;
    @FXML private Button AddSmallShrubButtonX5;
    @FXML private Button AddRabbitButton;
    @FXML private Button AddRabbitButtonX10;
    @FXML private Button AddRabbitButtonX25;
    @FXML private Button AddRabbitButtonX5;
    @FXML private Button AddTreeButton;
    @FXML private Button AddTreeButtonX10;
    @FXML private Button AddTreeButtonX25;
    @FXML private Button AddTreeButtonX5;
    @FXML private Button AddWolfButton;
    @FXML private Button AddWolfButtonX10;
    @FXML private Button AddWolfButtonX25;
    @FXML private Button AddWolfButtonX5;
    @FXML private Button DeleteLastObjectButton;
    @FXML private Button PauseButton;
    @FXML private Button imageScaleDefaultButton;
    @FXML private Button imageScaleMinusButton;
    @FXML private Button imageScalePlusButton;
    
    
    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);
        
        objectManager = new MyEcosysEntityManager();
        timeManager = new MyEcosysTimeManager();
        seasonManager = new MyEcosysSeasonManager();
        lifeSim = new MyEcosysLifeSim(objectManager.getEntitiesList());
        uiManager = new MyEcosysUIManager(gc, canvas, seasonManager);
        
        seasonManager.loadBackgroundImages();
        ImageLoader.loadObjectsImages();
        loadEmotionImages();
        setupAnimationTimer();
        startAnimation();
    }
    
    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isPaused) return;
                
                if (!timeManager.shouldRender(now)) return;
                if (timeManager.getLastFrameTime() == 0) {
                    timeManager.initializeTime(now);
                    return;
                }
                
                timeManager.updateTime(now);
                uiManager.redrawCanvas();
                uiManager.drawObjects(objectManager.getYSortEntitiesList(), timeManager.getAlpha());
                
                if (timeManager.shouldUpdateLife(now)) {
                    lifeSim.lifeSimulation();
                }
                
                if (timeManager.shouldUpdateSeason(now)) {
                    seasonManager.nextSeason();
                    lifeSim.oldDeadSimulation(objectManager.getEntitiesList());
                    timeManager.updateSeasonTime(now);
                }
                
                if (timeManager.shouldUpdateYear(now)) {
                    seasonManager.nextYear(objectManager.getEntitiesList());
                    lifeSim.reproductionSimulation();
                    timeManager.updateYearTime(now);
                }
            }
        };
    }
    
    @FXML void togglePause(ActionEvent event) {
        if (isPaused) {
            resumeAnimation();
        } else {
            pauseAnimation();
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
        uiManager.drawPauseIndicator();
    }
    
    public void resumeAnimation() {
        isPaused = false;
        long currentTime = System.nanoTime();
        timeManager.initializeTime(currentTime);
        
        if (animationTimer != null) {
            animationTimer.stop();
            animationTimer.start();
        }
    }
    
    @FXML void addWolf(ActionEvent event) { objectManager.addWolfs(1); }
    @FXML void AddWolfX5(ActionEvent event) { objectManager.addWolfs(5); }
    @FXML void AddWolfX10(ActionEvent event) { objectManager.addWolfs(10); }
    @FXML void AddWolfX25(ActionEvent event) { objectManager.addWolfs(25); }
    
    @FXML void addDeer(ActionEvent event) { objectManager.addDeers(1); }
    @FXML void addDeerX5(ActionEvent event) { objectManager.addDeers(5); }
    @FXML void addDeerX10(ActionEvent event) { objectManager.addDeers(10); }
    @FXML void addDeerX25(ActionEvent event) { objectManager.addDeers(25); }
    
    @FXML void addRabbit(ActionEvent event) { objectManager.addRabbits(1); }
    @FXML void addRabbitX5(ActionEvent event) { objectManager.addRabbits(5); }
    @FXML void addRabbitX10(ActionEvent event) { objectManager.addRabbits(10); }
    @FXML void addRabbitX25(ActionEvent event) { objectManager.addRabbits(25); }
    
    @FXML void addTree(ActionEvent event) { objectManager.addTrees(1); }
    @FXML void addTreeX5(ActionEvent event) { objectManager.addTrees(5); }
    @FXML void addTreeX10(ActionEvent event) { objectManager.addTrees(10); }
    @FXML void addTreeX25(ActionEvent event) { objectManager.addTrees(25); }
    
    @FXML void addMediumShrub(ActionEvent event) { objectManager.addMediumShrubs(1); }
    @FXML void addMediumShrubX5(ActionEvent event) { objectManager.addMediumShrubs(5); }
    @FXML void addMediumShrubX10(ActionEvent event) { objectManager.addMediumShrubs(10); }
    @FXML void addMediumShrubX25(ActionEvent event) { objectManager.addMediumShrubs(25); }
    
    @FXML void addSmallShrub(ActionEvent event) { objectManager.addSmallShrubs(1); }
    @FXML void addSmallShrubX5(ActionEvent event) { objectManager.addSmallShrubs(5); }
    @FXML void addSmallShrubX10(ActionEvent event) { objectManager.addSmallShrubs(10); }
    @FXML void addSmallShrubX25(ActionEvent event) { objectManager.addSmallShrubs(25); }
    
    @FXML void deleteLastObject(ActionEvent event) { objectManager.deleteLastObject(); }
    
    @FXML void imageScaleDefault(ActionEvent event) {Constants.temp_k = 1.6;}
    @FXML void imageScalePlus(ActionEvent event) {Constants.temp_k *= 1.05;}
    @FXML void imageScaleMinus(ActionEvent event) {Constants.temp_k *= 0.95;}
    
}