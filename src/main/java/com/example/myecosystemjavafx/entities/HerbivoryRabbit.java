package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.HALF;
import static com.example.myecosystemjavafx.Engines.ObjectGender.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.Dead;


public class HerbivoryRabbit extends AHerbivory {

    protected static  Image maleImage;
    protected static boolean imageLoaded = false;
    protected static Image femaleImage;
    protected static Image deadImage;

    protected double longevity = 1.5;

    protected final double width = BASE_SIZE * 0.7; //
    protected final double height = BASE_SIZE * 0.7; //
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;
    protected final double imageCorrection = 2.2 * TEMP_K;

    protected int maxNumberOfChildren = 3;

    protected double satietyModifier = 1.5; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 25; //сытность, хар-т питательность как жертвы
    protected int strongScore = 5; //сила
    protected int agilityScore = 35; //ловкость


    public HerbivoryRabbit(){super();}

    public HerbivoryRabbit(HerbivoryRabbit original) {super(original);}

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
    public double getLongevity() {return longevity;}

    @Override
    public HerbivoryRabbit copy() {
        return new HerbivoryRabbit(this);
    }


    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(HerbivoryRabbit.class.getResourceAsStream(maleImagePath));
            femaleImage = new Image(HerbivoryRabbit.class.getResourceAsStream(femaleImagePath));
            deadImage = new Image(HerbivoryRabbit.class.getResourceAsStream(deadImagePath));
            imageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения кролика: " + e.getMessage());
            imageLoaded = false;
        }
    }


    // Постоянные цвета в полях класса
    private final Color OBJECT_COLOR = Color.LIGHTGREY;


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

    private void drawDeadObject(GraphicsContext gc) {
        boolean isBaby = getAge() < GROWING_UP_AGE;

        double currentWidth = isBaby ? babyWidth : width;
        double currentHeight = isBaby ? babyHeight : height;

        if (imageLoaded) {
            drawImage(gc, deadImage, currentWidth, currentHeight);
        } else {
            drawDeadSquare(gc, currentWidth, currentHeight);
        }
    }

    private void drawBaby(GraphicsContext gc) {
        if (imageLoaded) {
            drawImage(gc, femaleImage, babyWidth, babyHeight);
        } else {
            drawFallbackSquare(gc, babyWidth, babyHeight, false);
        }
    }

    private void drawAdult(GraphicsContext gc) {
        boolean isFemale = getGender() == Female;

        if (imageLoaded) {
            drawImage(gc, isFemale ? femaleImage : maleImage, width, height);
        } else {
            drawFallbackSquare(gc, width, height, !isFemale);
        }
    }

    private void drawImage(GraphicsContext gc, Image image, double imgWidth, double imgHeight) {
        double centerX = getInterX() - imgWidth * HALF;
        double centerY = getInterY() - imgHeight * HALF;
        double scaledWidth = imageCorrection * imgWidth;
        double scaledHeight = imageCorrection * imgHeight;

        gc.drawImage(image, centerX, centerY, scaledWidth, scaledHeight);
    }

    private void drawFallbackSquare(GraphicsContext gc, double rectWidth, double rectHeight, boolean isMale) {
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

    private void drawDeadSquare(GraphicsContext gc, double rectWidth, double rectHeight) {
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
