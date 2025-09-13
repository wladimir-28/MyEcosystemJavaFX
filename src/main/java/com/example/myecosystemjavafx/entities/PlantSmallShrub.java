package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.MyEcosystemController;
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
    private final Color OBJECT_COLOR = Color.SEAGREEN;
    private final Color AUTUMN_OBJECT_COLOR = Color.ORANGE;

    protected double longevity = 0.5;

    protected final double width = BASE_SIZE * 0.3;
    protected final double height = BASE_SIZE * 0.3;
    protected final double length = BASE_SIZE * 0.7 * 0.3;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double babyLength = 0.8 * length;
    protected final double imageCorrection = 5 * TEMP_K;

    protected double satietyModifier = 0; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 25; //сытность, хар-т питательность как жертвы
    protected int strongScore = 0; //сила
    protected int agilityScore = 5; //ловкость

    protected int maxNumberOfChildren = 2;

    public PlantSmallShrub(){super();}

    public PlantSmallShrub(PlantSmallShrub original) {super(original);}

    @Override
    public double getSatietyModifier() {return satietyModifier;}

    @Override
    public int getNutritionValue() {return nutritionValue;}

    @Override
    public int getStrongScore() {return strongScore;}

    @Override
    public int getAgilityScore() {return agilityScore;}

    @Override
    protected int getMaxNumberOfChildren() {return maxNumberOfChildren;}

    @Override
    public boolean getPregnant() {
        if (age != 0  && Math.random() < 0.5) {
            return true;
        } else {return false;}
    }

    @Override
    public double getLongevity() {return longevity;}

    @Override
    public PlantSmallShrub copy() {
        return new PlantSmallShrub(this);
    }


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
    public void printObject(GraphicsContext gc, double alpha) {
        if (objectMode == Dead || MyEcosystemController.getSeasonOfYear() == Winter) {
            drawDeadObject(gc);
            return;
        }
            drawAdult(gc);
    }

    private void drawDeadObject(GraphicsContext gc) {
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

    private void drawAdult(GraphicsContext gc) {
        boolean isAutumn = MyEcosystemController.getSeasonOfYear() == Autumn;

        if (imageLoaded) {
            drawImage(gc, isAutumn ? autumnImage : summerImage, width, height);
        } else {
            drawFallbackTriangle(gc, width, height, length, isAutumn);
        }
    }

    private void drawImage(GraphicsContext gc, Image image, double imgWidth, double imgHeight) {
        double centerX = getCenterX() - imgWidth * HALF;
        double centerY = getCenterY() - imgHeight * HALF;
        double scaledWidth = imageCorrection * imgWidth;
        double scaledHeight = imageCorrection * imgHeight;

        gc.drawImage(image, centerX, centerY, scaledWidth, scaledHeight);
    }

    private void drawFallbackTriangle(GraphicsContext gc, double rectWidth, double rectHeight, double rectLength, boolean isAutumn) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        double length = rectLength;


        if (isAutumn) {
            gc.setFill(AUTUMN_OBJECT_COLOR);
        } else {
            gc.setFill(OBJECT_COLOR);
        }


        gc.beginPath();
        gc.moveTo(centerX, centerY - length);
        gc.lineTo(centerX - length, centerY + length);
        gc.lineTo(centerX + length, centerY + length);
        gc.closePath();
        gc.fill();

        // Контур треугольника
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(centerX, centerY - length);
        gc.lineTo(centerX - length, centerY + length);
        gc.lineTo(centerX + length, centerY + length);
        gc.closePath();
        gc.stroke();
    }

    private void drawDeadTriangle(GraphicsContext gc, double rectWidth, double rectHeight, double rectLength) {
        double centerX = getCenterX();
        double centerY = getCenterY();
        double length = rectLength;

        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.beginPath();
        gc.moveTo(centerX, centerY - length);
        gc.lineTo(centerX - length, centerY + length);
        gc.lineTo(centerX + length, centerY + length);
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


