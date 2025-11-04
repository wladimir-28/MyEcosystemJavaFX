package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.MyEcosysSeasonManager;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Constants.*;
import static com.example.myecosystemjavafx.Constants.ObjectMode.Dead;
import static com.example.myecosystemjavafx.Constants.ObjectMode.Rest;
import static com.example.myecosystemjavafx.Constants.SeasonsOfYear.Autumn;
import static com.example.myecosystemjavafx.Constants.SeasonsOfYear.Winter;

public class PlantMediumShrub extends APlant {

    protected static  Image summerImage;
    protected static boolean imageLoaded = false;
    protected static Image autumnImage;
    protected static Image deadImage;
    protected final Color OBJECT_COLOR = Color.SEAGREEN;
    protected final Color AUTUMN_OBJECT_COLOR = Color.ORANGE;

    protected double longevity = 1;

    protected final double width = BASE_SIZE * 0.5;
    protected final double height = BASE_SIZE * 0.5;
    protected final double length = BASE_SIZE * 0.4;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double babyLength = 0.8 * length;
    protected final double imageCorrection = 3.6 * TEMP_K;

    protected double satietyModifier = 0; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 50; //сытность, хар-т питательность как жертвы
    protected int strongScore = 0; //сила
    protected int agilityScore = 10; //ловкость

    public PlantMediumShrub(){super();}
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public PlantMediumShrub(PlantMediumShrub original) {super(original);}

    public PlantMediumShrub cloneObject() {
        return new PlantMediumShrub(this);
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
    public boolean getPregnant() {
        return age != 0 && age % 3 == 0 && Math.random() < 0.33;
    }

    @Override
    public double getLongevity() {return longevity;}

    @Override
    public void deadAction() {
        if (corpseTime == 0) {
            objectMode = Rest;
            corpseTime = PLANT_REBIRTH_TIME;
        } else {corpseTime -= 1;}
    }

    @Override
    public boolean isCorpseDelete() {
        if (objectMode == Dead && age > 3) {
            return corpseTime == 1 && Math.random() < 0.5;
        } else {return false;}
    }

    public static void loadImages(String summerImagePath, String autumnImagePath, String deadImagePath) {
        try {
            summerImage = new Image(PlantMediumShrub.class.getResourceAsStream(summerImagePath));
            autumnImage = new Image(PlantMediumShrub.class.getResourceAsStream(autumnImagePath));
            deadImage = new Image(PlantMediumShrub.class.getResourceAsStream(deadImagePath));
            imageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения куста: " + e.getMessage());
            imageLoaded = false;
        }
    }

    @Override
    public void printObject(GraphicsContext gc, double alpha) {
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
        
        // ТОЛЬКО контур без заливки для мертвых животных
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(centerX, centerY - rectLength);
        gc.lineTo(centerX - rectLength, centerY + rectLength);
        gc.lineTo(centerX + rectLength, centerY + rectLength);
        gc.closePath();
        gc.stroke();
    }


}
