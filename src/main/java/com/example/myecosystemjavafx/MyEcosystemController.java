package com.example.myecosystemjavafx;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;

import com.example.myecosystemjavafx.entities.*;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Emotions.loadEmotionImages;
import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.DangerState.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;
import static com.example.myecosystemjavafx.Engines.SeasonsOfYear.*;

public class MyEcosystemController {

    private Image summerBackgroundImage;
    private Image autumnBackgroundImage;
    private Image winterBackgroundImage;
    private Image springBackgroundImage;
    private Image currentBackgroundImage;
    private final Color SUMMER_RESERVE_COLOR = Color.GREENYELLOW;
    private final Color AUTUMN_RESERVE_COLOR = Color.GOLD;
    private final Color WINTER_RESERVE_COLOR = Color.LIGHTCYAN;
    private final Color SPRING_RESERVE_COLOR = Color.PERU;
    private Color currentReserveColor = SPRING_RESERVE_COLOR;
    private static SeasonsOfYear seasonOfYear = Spring;
    private boolean backgroundImageLoaded = false;

    private static ArrayList<UIObject> objectsList = new ArrayList<>();
    private AnimationTimer animationTimer;
    private boolean isPaused = false;
    private long lastUpdateTime = 0;
    private long lastLifeUpdate = 0;
    private long lastSeasonUpdate = 0;
    private long lastYearsUpdate = 0;
    private long lastFrameTime = 0;
    private long accumulatedTime = 0;
    private long lastRenderTime = 0;
    private double alpha = 0.0;
    private final long LIFE_UPDATE_INTERVAL = 1_000_000_000L;
    private final long SEASON_INTERVAL = SECOND_IN_SEASON * 1_000_000_000L;
    private final long YEAR_INTERVAL = SECOND_IN_SEASON * 4_000_000_000L;
    private static final long TARGET_FRAME_TIME = 22_222_222L;; // 45 FPS

    public static SeasonsOfYear getSeasonOfYear() {return seasonOfYear;}

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);  //сглаживание
        loadBackgroundImage();
        ImageLoader.loadObjectsImages();
        loadEmotionImages();
        setupAnimationTimer();
        startAnimation();
    }

    protected void loadBackgroundImage() {
        try {
            springBackgroundImage = new Image("/background_spring.jpg");
            summerBackgroundImage = new Image("/background_summer.jpg");
            autumnBackgroundImage = new Image("/background_autumn.jpg");
            winterBackgroundImage = new Image("/background_winter.jpg");
            currentBackgroundImage = springBackgroundImage;
            backgroundImageLoaded = true;
        } catch (Exception e) {
            System.err.println("Не удалось загрузить задние фоны: " + e.getMessage());
            backgroundImageLoaded = false;
        }
    }

    protected void nextSeason() {
        if (seasonOfYear == Spring) {
            seasonOfYear = Summer;
            currentBackgroundImage = summerBackgroundImage;
            currentReserveColor = SUMMER_RESERVE_COLOR;
        }
        else if (seasonOfYear == Summer) {
            seasonOfYear = Autumn;
            currentBackgroundImage = autumnBackgroundImage;
            currentReserveColor = AUTUMN_RESERVE_COLOR;
        }
        else if (seasonOfYear == Autumn) {
            seasonOfYear = Winter;
            currentBackgroundImage = winterBackgroundImage;
            currentReserveColor = WINTER_RESERVE_COLOR;
        }
        else {
            seasonOfYear = Spring;
            currentBackgroundImage = springBackgroundImage;
            currentReserveColor = SPRING_RESERVE_COLOR;
        }
    }

    protected void nextYear() {
        for (UIObject object : objectsList) {
        object.addAge(1);
        }
    }

    protected void oldDeadSimulation() {
        for (UIObject object : objectsList) {
            if (object.getAge() < 5) {
                continue;
            }
            double random = Math.random();
            double deathProbability = getDeathProbability(object.getAge(), object.getLongevity());

            if (random < deathProbability) {
                object.setObjectMode(Dead);
                System.out.println("Умер от старости");
            }
        }
    }

    private double getDeathProbability(int age, double longevity) {
        switch (age) {
            case 5: return 0.015 * (1/longevity);
            case 6: return 0.03 * (1/longevity);
            case 7: return 0.05 * (1/longevity);
            case 8: return 0.08 * (1/longevity);
            case 9: return 0.11 * (1/longevity);
            case 10: return 0.15 * (1/longevity);
            case 11: return 0.175 * (1/longevity);
            case 12: return 0.2 * (1/longevity);
            default: return 0.225 * (1/longevity);
        }
    }

    public void reproductionSimulation() {
        ListIterator<UIObject> iterator = objectsList.listIterator();
        while(iterator.hasNext()) {
            UIObject object = iterator.next();
            if (object.getPregnant()) {
                for (int i = 0; i < object.randomNumberOfChildren(); i++) {
                    iterator.add(object.copy());
                }
                object.setPregnant(false);
            }
        }
    }

    private void redrawCanvas() {
        if (backgroundImageLoaded == true) {
            gc.clearRect(0, 0, CANVAS_WIDTH, Engines.CANVAS_HEIGHT);
            gc.drawImage(currentBackgroundImage, 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(currentReserveColor);
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {


            @Override
            public void handle(long now) {

                if (isPaused) return;

                // Ограничение до 30 FPS
                if (now - lastRenderTime < TARGET_FRAME_TIME) {
                    return;
                }

                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    lastUpdateTime = now;
                    lastLifeUpdate = now;
                    lastSeasonUpdate = now;
                    lastYearsUpdate = now;
                    lastRenderTime = now;
                    return;
                }

                long deltaTime = now - lastFrameTime;
                accumulatedTime += deltaTime;
                alpha = Math.min(1.0, (double) accumulatedTime / LIFE_UPDATE_INTERVAL);

                redrawCanvas();
                drawObjects();


                while (accumulatedTime >= LIFE_UPDATE_INTERVAL) {
                    lifeSimulation();
                    accumulatedTime -= LIFE_UPDATE_INTERVAL;
                    lastLifeUpdate = now;
                }

                if (now - lastSeasonUpdate >= SEASON_INTERVAL) {
                    nextSeason();
                    oldDeadSimulation();
                    lastSeasonUpdate = now;
                }

                if (now - lastYearsUpdate >= YEAR_INTERVAL) {
                    nextYear();
                    reproductionSimulation();
                    lastYearsUpdate = now;
                }


                lastFrameTime = now;
                lastRenderTime = now;
            }
        };
    }

    private void drawObjects() {
        if (objectsList == null) {return;}
        for (UIObject object : objectsList) {
            object.printObject(gc, alpha);
            Emotions.drawEmotion(gc, object);
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
        lastFrameTime = currentTime;
        lastUpdateTime = currentTime;
        lastLifeUpdate = currentTime;
        lastSeasonUpdate = currentTime;
        lastYearsUpdate = currentTime;
        accumulatedTime = 0;

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
        gc.fillRect(x, y, size, 2.5 *  size);
        gc.fillRect(x + size + spacing, y, size, 2.5 * size);
    }

    public void lifeSimulation() {
        //System.out.println("Обновление физики");
        for (UIObject object : objectsList) {
            object.checkState();            // проверка показателей голода и энергии
            searchTargetDanger(object, objectsList); // проверка окружения и идентификация целей и опасностей
            object.selectObjectMode();      // выбор поведения
            object.giveBuffDebuff();        // баффы дебаффы на скорость и радиус видимости
            object.actionObject();        // опредедение направления движения и движение по факту
            interact(object, objectsList, RADIUS_INTERACTION); // взаимодействия (втч убийства)
            object.getInfo();
        }
        deleteCorpses();                  // удаление мертвых
    }

    public void searchTargetDanger(UIObject thisObject, ArrayList<UIObject> objectsList) {
        if (thisObject instanceof APlant || thisObject.getObjectMode() == Dead ) {return;}
        boolean foundDanger = false;

        double minTargetDistance = BIG_RADIUS_VISION;
        double minPartnerDistance = BIG_RADIUS_VISION;
        for (UIObject other : objectsList) {
            if (other != thisObject && other.getObjectMode() != Dead && thisObject.isInRadius(other, thisObject.getBigRadiusVision())) {
                double distance = Engines.calculateDistance(thisObject.getCenterX(), thisObject.getCenterY(), other.getCenterX(), other.getCenterY());

                if (thisObject.isTarget(other) && distance < minTargetDistance) {
                    minTargetDistance = distance;
                    thisObject.setTarget(other.getCenterX(), other.getCenterY());
                }

                if (thisObject.isItPartner(other) && distance <= minPartnerDistance) { // идентификация увиденного
                    minPartnerDistance = distance;
                    thisObject.setPartner(other.getCenterX(), other.getCenterY(), distance);
                }

                if (thisObject.isDanger(other)) {
                    thisObject.setDanger(other.getCenterX(), other.getCenterY());
                    if (thisObject.isInRadius(other, SMALL_RADIUS_VISION)  && other.isSenseDanger()) {
                        thisObject.setDangerState(Danger);
                        foundDanger = true;
                    }
                }
            }
        }

        if (!foundDanger) {
            thisObject.setDangerState(Safety);
        }
    }

    public void interact(UIObject thisObject, ArrayList<UIObject> objectsList, double radius) {
        if (thisObject instanceof APlant || thisObject.getObjectMode() == Dead ) {return;}

        for (UIObject other : objectsList) {
            if (other != thisObject && other.getObjectMode() != Dead && thisObject.isInRadius(other, radius)) {
                if (thisObject.getObjectMode() == Hunting && thisObject.isTarget(other)) {
                    thisObject.catchTarget(other);
                }

                if ((thisObject.getObjectMode() == Walk || thisObject.getObjectMode() == Rest) && thisObject.isItPartner(other)) {
                    thisObject.lovePartner(other);
                }
            }
        }
    }

    public static void shareFood(UIObject thisObject, int satiety) {
        int counter = 0;
        for (UIObject other : objectsList) {
            if (counter == MAX_NUMBER_FOODSHARER) {break;}
            if (other != thisObject &&  thisObject.getClass().equals(other.getClass()) && thisObject.isInRadius(other, SHARE_FOOD_DISTANCE)) {
                if (counter == 0) {
                    other.addSatiety((int) (satiety * 0.5));
                } else if (counter == 1) {
                    other.addSatiety((int) (satiety * 0.25));
                } else {
                    other.addSatiety((int) (satiety * 0.1));
                }
                counter += 1;
                //System.out.println("Разделили добычу");
            }
        }
    }

    public void deleteCorpses() {
        Iterator<UIObject> iterator = objectsList.iterator();
        while (iterator.hasNext()) {
            UIObject object = iterator.next();

            if (object != null && object.isCorpseDelete()) {
                iterator.remove();
                UIObject.decrementCounterObject();
            }
        }
    }

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
    void addWolf(ActionEvent event) {addWolfs(1);}

    @FXML
    void AddWolfX5(ActionEvent event) {addWolfs(5);}

    @FXML
    void AddWolfX10(ActionEvent event) {addWolfs(10);}

    @FXML
    void AddWolfX25(ActionEvent event) {addWolfs(25);}

    /// /////////////////////////////////////////////

    @FXML
    void addDeer(ActionEvent event) {addDeers(1);}

    @FXML
    void addDeerX5(ActionEvent event) {addDeers(5);}

    @FXML
    void addDeerX10(ActionEvent event) {addDeers(10);}

    @FXML
    void addDeerX25(ActionEvent event) {addDeers(25);}

    @FXML
    void addRabbit(ActionEvent event) {addRabbits(1);}

    @FXML
    void addRabbitX5(ActionEvent event) {addRabbits(5);}

    @FXML
    void addRabbitX10(ActionEvent event) {addRabbits(10);}

    @FXML
    void addRabbitX25(ActionEvent event) {addRabbits(25);}

    /// ////////////////////////////////////////////////////////
    @FXML
    void addTree(ActionEvent event) {addTrees(1);}

    @FXML
    void addTreeX5(ActionEvent event) {addTrees(5);}

    @FXML
    void addTreeX10(ActionEvent event) {addTrees(10);}

    @FXML
    void addTreeX25(ActionEvent event) {addTrees(25);}

    @FXML
    void addMediumShrub(ActionEvent event) {addMediumShrubs(1);}

    @FXML
    void addMediumShrubX5(ActionEvent event) {addMediumShrubs(5);}

    @FXML
    void addMediumShrubX10(ActionEvent event) {addMediumShrubs(10);}

    @FXML
    void addMediumShrubX25(ActionEvent event) {addMediumShrubs(25);}

    @FXML
    void addSmallShrub(ActionEvent event) {addSmallShrubs(1);}

    @FXML
    void addSmallShrubX5(ActionEvent event) {addSmallShrubs(5);}

    @FXML
    void addSmallShrubX10(ActionEvent event) {addSmallShrubs(10);}

    @FXML
    void addSmallShrubX25(ActionEvent event) {addSmallShrubs(25);}

    /// ///////////////////////////////////////////////////////////////////

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
