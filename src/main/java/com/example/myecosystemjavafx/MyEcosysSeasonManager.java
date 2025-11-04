package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.UIObject;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import static com.example.myecosystemjavafx.Constants.SeasonsOfYear.*;

public class MyEcosysSeasonManager {
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
    private static Constants.SeasonsOfYear seasonOfYear = Spring;
    private boolean backgroundImageLoaded = false;
    
    public void loadBackgroundImages() {
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
    
    public void nextSeason() {
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
    
    public void nextYear(ArrayList<UIObject> objectsList) {
        for (UIObject object : objectsList) {
            object.addAge(1);
        }
    }
    
    public void oldDeadSimulation(ArrayList<UIObject> objectsList) {
        for (UIObject object : objectsList) {
            if (object.getAge() < (int)(5 * object.getLongevity())) {
                continue;
            }
            double random = Math.random();
            double deathProbability = MathFunc.getDeathProbability(object.getAge(), object.getLongevity());
            
            if (random < deathProbability) {
                object.setObjectMode(Constants.ObjectMode.Dead);
                object.setEmotion(Constants.EmojiType.OldDeadEmoji);
            }
        }
    }
    
    public Image getCurrentBackgroundImage() {
        return currentBackgroundImage;
    }
    
    public Color getCurrentReserveColor() {
        return currentReserveColor;
    }
    
    public boolean isBackgroundImageLoaded() {
        return backgroundImageLoaded;
    }
    
    public static Constants.SeasonsOfYear getSeasonOfYear() {
        return seasonOfYear;
    }
}
