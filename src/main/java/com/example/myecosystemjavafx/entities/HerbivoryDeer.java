package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Constants.*;
import static com.example.myecosystemjavafx.Constants.ObjectGender.*;
import static com.example.myecosystemjavafx.Constants.ObjectMode.*;


public class HerbivoryDeer extends AHerbivory {

    protected static  Image maleImage;
    protected static boolean imageLoaded = false;
    protected static Image femaleImage;
    protected static Image deadImage;

    protected double longevity = 1;

    protected final double width = BASE_SIZE * 1.1;
    protected final double height = BASE_SIZE * 1.1;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double imageCorrection = 1.9 * TEMP_K;

    protected int maxNumberOfChildren = 2;

    protected double satietyModifier = 0.75; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 60; //сытность, хар-т питательность как жертвы
    protected int strongScore = 22; //сила
    protected int agilityScore = 25; //ловкость

    public HerbivoryDeer(){
        super();
    }
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public HerbivoryDeer(HerbivoryDeer original) {
        super(original);
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
    public double getLongevity() {return longevity;}

    @Override
    public HerbivoryDeer cloneObject() {
        return new HerbivoryDeer(this);
    }

    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(HerbivoryDeer.class.getResourceAsStream(maleImagePath));
            femaleImage = new Image(HerbivoryDeer.class.getResourceAsStream(femaleImagePath));
            deadImage = new Image(HerbivoryDeer.class.getResourceAsStream(deadImagePath));
            imageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения оленя: " + e.getMessage());
            imageLoaded = false;
        }
    }


    // Постоянные цвета в полях класса
    protected final Color OBJECT_COLOR = Color.SANDYBROWN;


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
            drawDeadSquare(gc, currentWidth, currentHeight);
        }
    }

    protected void drawBaby(GraphicsContext gc) {
        if (imageLoaded) {
            drawImage(gc, femaleImage, babyWidth, babyHeight);
        } else {
            drawFallbackSquare(gc, babyWidth, babyHeight, false);
        }
    }

    protected void drawAdult(GraphicsContext gc) {
        boolean isFemale = getGender() == Female;

        if (imageLoaded) {
            drawImage(gc, isFemale ? femaleImage : maleImage, width, height);
        } else {
            drawFallbackSquare(gc, width, height, !isFemale);
        }
    }

    protected void drawImage(GraphicsContext gc, Image image, double imgWidth, double imgHeight) {
        double centerX = getInterX() - imgWidth * HALF;
        double centerY = getInterY() - imgHeight * HALF;
        double scaledWidth = imageCorrection * imgWidth;
        double scaledHeight = imageCorrection * imgHeight;

        gc.drawImage(image, centerX, centerY, scaledWidth, scaledHeight);
    }

    protected void drawFallbackSquare(GraphicsContext gc, double rectWidth, double rectHeight, boolean isMale) {
        double x = getInterX() - rectWidth * HALF;
        double y = getInterY() - rectHeight * HALF;
        double w = rectWidth;
        double h = rectHeight;

        // Залитый квадрат
        gc.setFill(OBJECT_COLOR);
        gc.fillRect(x, y, w, h);

        // Контур
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, w, h);

        // Точка для самца
        if (isMale) {
            double centerX = x + w * HALF;
            double centerY = y + h * HALF;
            double dotRadius = w * 0.1; // Уменьшен радиус, так как теперь w в 2 раза больше

            gc.setFill(STROKE_COLOR);
            gc.fillOval(centerX - dotRadius, centerY - dotRadius, dotRadius * 2, dotRadius * 2);
        }
    }

    protected void drawDeadSquare(GraphicsContext gc, double rectWidth, double rectHeight) {
        double x = getInterX() - rectWidth * HALF;
        double y = getInterY() - rectHeight * HALF;
        double w = rectWidth;
        double h = rectHeight;

        // ТОЛЬКО контур без заливки для мертвых животных
        gc.setStroke(STROKE_COLOR);
        gc.setLineWidth(1);
        gc.strokeRect(x, y, w, h);
    }

}
