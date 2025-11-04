package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Constants.*;
import static com.example.myecosystemjavafx.Constants.ObjectGender.*;
import static com.example.myecosystemjavafx.Constants.ObjectMode.*;

public class CarnivoraWolf extends ACarnivora {

    protected static  Image maleImage;
    protected static boolean imageLoaded = false;
    protected static Image femaleImage;
    protected static Image deadImage;

    protected double longevity = 1;

    protected final int maxSatiety = 70;

    protected final double width = BASE_SIZE;
    protected final double height = BASE_SIZE;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double imageCorrection = 1.9 * TEMP_K;

    protected int maxNumberOfChildren = 2;

    protected double satietyModifier = 1; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 40; //сытность, хар-т питательность как жертвы
    protected int strongScore = 30; //сила
    protected int agilityScore = 20; //ловкость

    public CarnivoraWolf(){super();}
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public CarnivoraWolf(CarnivoraWolf original) {super(original);}

    @Override
    public CarnivoraWolf cloneObject() {
        return new CarnivoraWolf(this);
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
    public int getMaxSatiety() {return maxSatiety;}

    @Override
    public double getLongevity() {return longevity;}


    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(CarnivoraWolf.class.getResourceAsStream(maleImagePath));
            femaleImage = new Image(CarnivoraWolf.class.getResourceAsStream(femaleImagePath));
            deadImage = new Image(CarnivoraWolf.class.getResourceAsStream(deadImagePath));
            imageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения волка: " + e.getMessage());
            imageLoaded = false;
        }
    }

    // Постоянные цвета в полях класса
    protected final Color OBJECT_COLOR = Color.DIMGRAY;


    @Override
    public void printObject(GraphicsContext gc, double alpha) {
        interpolatedX(alpha);
        interpolatedY(alpha);

        if (objectMode == Dead) {
            drawDeadObject(gc);
            return;
        }

        if (getAge() < GROWING_UP_AGE) {
            drawBaby(gc);
        } else {
            drawAdult(gc);
        }
    }

    protected void drawDeadObject(GraphicsContext gc) {
        boolean isBaby = getAge() < GROWING_UP_AGE;

        double currentWidth = isBaby ? babyWidth : width;
        double currentHeight = isBaby ? babyHeight : height;

        if (imageLoaded) {
            drawImage(gc, deadImage, currentWidth, currentHeight);
        } else {
            drawDeadCircle(gc, currentWidth, currentHeight);
        }
    }

    protected void drawBaby(GraphicsContext gc) {
        if (imageLoaded) {
            drawImage(gc, femaleImage, babyWidth, babyHeight);
        } else {
            drawFallbackCircle(gc, babyWidth, babyHeight, false);
        }
    }

    protected void drawAdult(GraphicsContext gc) {
        boolean isFemale = getGender() == Female;

        if (imageLoaded) {
            drawImage(gc, isFemale ? femaleImage : maleImage, width, height);
        } else {
            drawFallbackCircle(gc, width, height, !isFemale);
        }
    }

    protected void drawImage(GraphicsContext gc, Image image, double imgWidth, double imgHeight) {
        double centerX = getInterX() - imgWidth * HALF;
        double centerY = getInterY() - imgHeight * HALF;
        double scaledWidth = imageCorrection * imgWidth;
        double scaledHeight = imageCorrection * imgHeight;

        gc.drawImage(image, centerX, centerY, scaledWidth, scaledHeight);
    }

    protected void drawFallbackCircle(GraphicsContext gc, double circleWidth, double circleHeight, boolean isMale) {
        double centerX = getInterX();
        double centerY = getInterY();
        double radius = circleWidth * HALF; // Используем ширину как диаметр

        // Залитый круг
        gc.setFill(OBJECT_COLOR);
        gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Контур
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);

        // Точка для самца
        if (isMale) {
            double dotRadius = radius * 0.2;
            gc.setFill(STROKE_COLOR);
            gc.fillOval(centerX - dotRadius, centerY - dotRadius, dotRadius * 2, dotRadius * 2);
        }
    }

    protected void drawDeadCircle(GraphicsContext gc, double circleWidth, double circleHeight) {
        double centerX = getInterX();
        double centerY = getInterY();
        double radius = circleWidth * HALF;

        // ТОЛЬКО контур без заливки для мертвых животных
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
    }
}
