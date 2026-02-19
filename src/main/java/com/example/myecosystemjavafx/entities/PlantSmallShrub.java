package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.MyEcosysSeasonManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Constants.*;
import static com.example.myecosystemjavafx.Constants.ObjectMode.Dead;
import static com.example.myecosystemjavafx.Constants.SeasonsOfYear.Autumn;
import static com.example.myecosystemjavafx.Constants.SeasonsOfYear.Winter;

public class PlantSmallShrub extends APlant {

    protected static  Image summerImage;
    protected static boolean imageLoaded = false;
    protected static Image autumnImage;
    protected static Image deadImage;
    protected final Color OBJECT_COLOR = Color.SEAGREEN;
    protected final Color AUTUMN_OBJECT_COLOR = Color.ORANGE;

    protected double longevity = 0.5;

    protected final double width = BASE_SIZE * 0.3;
    protected final double height = BASE_SIZE * 0.3;
    protected final double length = BASE_SIZE * 0.7 * 0.3;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double babyLength = 0.8 * length;
    protected double imagePersKoef = 5;
    protected double imageCorrection;

    protected double satietyModifier = 0; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 25; //сытность, хар-т питательность как жертвы
    protected int strongScore = 0; //сила
    protected int agilityScore = 5; //ловкость

    protected int maxNumberOfChildren = 2;

    public PlantSmallShrub(){super();}
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public PlantSmallShrub(PlantSmallShrub original) {super(original);}

    public PlantSmallShrub cloneObject() {
        return new PlantSmallShrub(this);
    }

    @Override
    public double getSatietyModifier() {return satietyModifier;}

    @Override
    public int getNutritionValue() {return nutritionValue;}

    @Override
    public int getStrongScore() {return strongScore;}

    @Override
    public int getAgilityScore() {return agilityScore;}

    @Override
    public int getMaxNumberOfChildren() {return maxNumberOfChildren;}

    @Override
    public boolean getPregnant() {
        return age != 0 && Math.random() < 0.5;
    }

    @Override
    public double getLongevity() {return longevity;}

    public static void loadImages(String summerImagePath, String autumnImagePath, String deadImagePath) {
        try {
            summerImage = new Image(PlantSmallShrub.class.getResourceAsStream(summerImagePath));
            autumnImage = new Image(PlantSmallShrub.class.getResourceAsStream(autumnImagePath));
            deadImage = new Image(PlantSmallShrub.class.getResourceAsStream(deadImagePath));
            imageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения кустика: " + e.getMessage());
            imageLoaded = false;
        }
    }
    
    @Override
    protected void refreshImageCorrection() {imageCorrection = imagePersKoef * temp_k;}
    
    @Override
    public void printObject(GraphicsContext gc, double alpha) {
        refreshImageCorrection();
        if (objectMode == Dead || MyEcosysSeasonManager.getSeasonOfYear() == Winter) {
            drawDeadObject(gc);
            return;
        }
            drawAdult(gc);
    }
    
    protected void drawDeadObject(GraphicsContext gc) {
        boolean isBaby = getAge() < PLANT_GROWING_UP_AGE;

        double currentWidth = isBaby ? babyWidth : width;
        double currentHeight = isBaby ? babyHeight : height;
        double currentLength = isBaby ? babyLength : length;


        if (imageLoaded) {
            drawImage(gc, deadImage, currentWidth, currentHeight);
        } else {
            drawDeadTriangle(gc, currentWidth, currentHeight, currentLength);
        }
    }
    
    protected void drawAdult(GraphicsContext gc) {
        boolean isAutumn = MyEcosysSeasonManager.getSeasonOfYear() == Autumn;

        if (imageLoaded) {
            drawImage(gc, isAutumn ? autumnImage : summerImage, width, height);
        } else {
            drawFallbackTriangle(gc, width, height, length, isAutumn);
        }
    }
    
    protected void drawImage(GraphicsContext gc, Image image, double imgWidth, double imgHeight) {
        double centerX = getCenterX() - imgWidth * HALF;
        double centerY = getCenterY() - imgHeight * HALF;
        double scaledWidth = imageCorrection * imgWidth;
        double scaledHeight = imageCorrection * imgHeight;

        gc.drawImage(image, centerX, centerY, scaledWidth, scaledHeight);
    }
    
    protected void drawFallbackTriangle(GraphicsContext gc, double rectWidth, double rectHeight, double rectLength, boolean isAutumn) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        
        
        if (isAutumn) {
            gc.setFill(AUTUMN_OBJECT_COLOR);
        } else {
            gc.setFill(OBJECT_COLOR);
        }


        gc.beginPath();
        gc.moveTo(centerX, centerY - rectLength);
        gc.lineTo(centerX - rectLength, centerY + rectLength);
        gc.lineTo(centerX + rectLength, centerY + rectLength);
        gc.closePath();
        gc.fill();

        // Контур треугольника
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(centerX, centerY - rectLength);
        gc.lineTo(centerX - rectLength, centerY + rectLength);
        gc.lineTo(centerX + rectLength, centerY + rectLength);
        gc.closePath();
        gc.stroke();
    }
    
    protected void drawDeadTriangle(GraphicsContext gc, double rectWidth, double rectHeight, double rectLength) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(centerX, centerY - rectLength);
        gc.lineTo(centerX - rectLength, centerY + rectLength);
        gc.lineTo(centerX + rectLength, centerY + rectLength);
        gc.closePath();
        gc.stroke();
    }

    @Override
    public boolean isCorpseDelete() {
        if (objectMode == Dead) {
            if (corpseTime == 0) {
                return true;
            } else {
                corpseTime -= 1;
                return false;
            }
        } else {return false;}
    }
}


