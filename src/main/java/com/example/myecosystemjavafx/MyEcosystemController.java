package com.example.myecosystemjavafx;

import javafx.fxml.FXML;
import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
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

    private static SeasonsOfYear seasonOfYear = Spring;
    private boolean backgroundImageLoaded = false;
    private static ArrayList<UIObject> objectsList = new ArrayList<>();
    private AnimationTimer animationTimer;
    private boolean isPaused = false;
    private long lastUpdateTime = 0;
    private long lastLifeUpdate = 0;
    private long lastSeasonUpdate = 0;
    private long lastYearsUpdate = 0;
    private double alpha = 0.0;
    private final long LIFE_UPDATE_INTERVAL = 1_000_000_000L;
    private final long SEASON_INTERVAL = SECOND_IN_SEASON * 1_000_000_000L;
    private final long YEAR_INTERVAL = SECOND_IN_SEASON * 4_000_000_000L;

    public static SeasonsOfYear getSeasonOfYear() {
        return seasonOfYear;
    }

    @FXML
    private void initialize() {
        gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(true);  //сглаживание
        loadBackgroundImage();
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
        }
        else if (seasonOfYear == Summer) {
            seasonOfYear = Autumn;
            currentBackgroundImage = autumnBackgroundImage;
        }
        else if (seasonOfYear == Autumn) {
            seasonOfYear = Winter;
            currentBackgroundImage = winterBackgroundImage;
        }
        else {
            seasonOfYear = Spring;
            currentBackgroundImage = springBackgroundImage;}
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
            case 5: return 0.025 * (1/longevity);
            case 6: return 0.05 * (1/longevity);
            case 7: return 0.075 * (1/longevity);
            case 8: return 0.1 * (1/longevity);
            case 9: return 0.125 * (1/longevity);
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
            gc.setFill(Color.FORESTGREEN);
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }

    private void setupAnimationTimer() {
        animationTimer = new AnimationTimer() {
            private long lastFrameTime = 0;
            private long accumulatedTime = 0;

            @Override
            public void handle(long now) {

                if (isPaused) return;

                if (lastFrameTime == 0) {
                    lastFrameTime = now;
                    lastUpdateTime = now;
                    lastLifeUpdate = now;
                    lastSeasonUpdate = now;
                    lastYearsUpdate = now;
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
        lastUpdateTime = currentTime;
        lastLifeUpdate = currentTime;
        lastSeasonUpdate = currentTime;
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
            searchTargetDanger(object, objectsList, object.getRadiusVision()); // проверка окружения и идентификация целей и опасностей
            object.selectObjectMode();      // выбор поведения
            object.giveBuffDebuff();        // баффы дебаффы на скорость
            object.actionObject();        // опредедение направления движения и движение по факту
            interact(object, objectsList, BASE_SIZE); // взаимодействия (втч убийства)
            object.getInfo();
        }
        deleteCorpses();                  // удаление мертвых
    }

    public void searchTargetDanger(UIObject thisObject, ArrayList<UIObject> objectsList, double radiusVision) {
        if (thisObject instanceof APlant || thisObject.getObjectMode() == Dead ) {return;}
        boolean foundDanger = false;

        for (UIObject other : objectsList) {
            if (other != thisObject && other.getObjectMode() != Dead && thisObject.isInRadius(other, radiusVision)) {
                double distance = Engines.calculateDistance(thisObject.getCenterX(), thisObject.getCenterY(), other.getCenterX(), other.getCenterY());

                if (thisObject.isTarget(other) && (distance <= thisObject.getTargetDistance())) { // идентификация увиденного
                    thisObject.setTarget(other.getCenterX(), other.getCenterY(), distance);
                }

                if (thisObject.isItPartner(other) && (distance <= thisObject.getPartnerDistance())) { // идентификация увиденного
                    thisObject.setPartner(other.getCenterX(), other.getCenterY(), distance);
                }

                if (thisObject.isDanger(other)) {
                    thisObject.setDanger(other.getCenterX(), other.getCenterY());
                    thisObject.setDangerState(Danger);
                    foundDanger = true;
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

    public static void shareFood(UIObject thisObject, int satyety) {
        for (UIObject other : objectsList) {
            if (other != thisObject &&  thisObject.getClass().equals(other.getClass()) && thisObject.isInRadius(other, SHARE_FOOD_DISTANCE)) {
                other.addSatiety(satyety);
                System.out.println("Разделили добычу");
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
    private Button PauseButton;

    @FXML
    private Button AddCarnivoraButton;

    @FXML
    private Button AddCarnivoraButtonX5;

    @FXML
    private Button AddHerbivoryButton;

    @FXML
    private Button AddHerbivoryButtonX5;

    @FXML
    private Button AddPlantButton;

    @FXML
    private Button AddPlantButtonX5;

    @FXML
    private Button AddPlantButtonX25;

    @FXML
    private Button DeleteLastObjectButton;

    @FXML
    private Button TestCopyButton;

    @FXML
    private BorderPane root;

    @FXML
    void togglePause() {
        if (isPaused) {
            resumeAnimation();
        } else {
            pauseAnimation();
        }
    }

    @FXML
    void addCarnivora(ActionEvent event) {
        addCarnivoras(1);
    }

    @FXML
    void addHerbivory(ActionEvent event) {
        addHerbivores(1);
    }

    @FXML
    void addPlant(ActionEvent event) {
        addPlants(1);

    }
    @FXML
    void AddCarnivoraX5(ActionEvent event) {
        addCarnivoras(5);
    }

    @FXML
    void AddHerbivoryX5(ActionEvent event) {
        addHerbivores(5);
    }
    @FXML
    void addPlantX5(ActionEvent event) {
        addPlants(5);
    }

    @FXML
    void addPlantX25(ActionEvent event) {
        addPlants(25);
    }

    @FXML
    void copyCarnivora(ActionEvent event) {
        reproductionSimulation();
    }

    @FXML
    void deleteLastObject(ActionEvent event) {
        if (UIObject.getCounterObjects() != 0) {
            objectsList.remove(UIObject.getCounterObjects() - 1);
            UIObject.decrementCounterObject();
        }
    }

    void addCarnivoras(int countCarnivoras) {
        for (int i = 0; i < countCarnivoras; i++) {
            objectsList.add(UIObject.getCounterObjects(), new CarnivoraWolf());
        }
    }

    void addHerbivores(int countHerbivores) {
        for (int i = 0; i < countHerbivores; i++) {
            objectsList.add(UIObject.getCounterObjects(), new HerbivoryDeer());
        }
    }

    void addPlants(int countPlants) {
        for (int i = 0; i < countPlants; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantTriangle());
        }
    }
}
