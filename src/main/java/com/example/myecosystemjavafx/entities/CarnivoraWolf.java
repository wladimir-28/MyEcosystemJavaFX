package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.ObjectGender.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;

public class CarnivoraWolf extends ACarnivora {

    protected static  Image maleImage;
    protected static boolean maleImageLoaded = false;
    protected static Image femaleImage;
    protected static boolean femaleImageLoaded = false;
    protected static Image deadImage;
    protected static boolean deadImageLoaded = false;

    protected final int maxSatiety = 75;

    protected int maxNumberOfChildren = 2;

    protected final double radius = BASE_SIZE * 0.5;
    protected final double width = BASE_SIZE * 1.8;
    protected final double height = BASE_SIZE * 1.8;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;

    protected double satietyModifier = 1; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 40; //сытность, хар-т питательность как жертвы
    protected int strongScore = 30; //сила
    protected int agilityScore = 20; //ловкость

    public CarnivoraWolf(){super();}

    public CarnivoraWolf(CarnivoraWolf original) {super(original);}

    @Override
    public CarnivoraWolf copy() {
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
    protected int getMaxNumberOfChildren() {return maxNumberOfChildren;}

    @Override
    public int getMaxSatiety() {return maxSatiety;}

    @Override
    protected Color getColor() {return Color.DIMGRAY;}


    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(CarnivoraWolf.class.getResourceAsStream(maleImagePath));
            maleImageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самца волка: " + e.getMessage());
        }
        try {
            femaleImage = new Image(CarnivoraWolf.class.getResourceAsStream(femaleImagePath));
            femaleImageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самки волка: " + e.getMessage());
        }
        try {
            deadImage = new Image(CarnivoraWolf.class.getResourceAsStream(deadImagePath));
            deadImageLoaded = true;
        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения трупа волка: " + e.getMessage());
        }
    }

    @Override
    public void printObject(GraphicsContext gc, double alpha) {
        interpolatedX(alpha);
        interpolatedY(alpha);

        if (objectMode != Dead) {
            if (getAge() < 1 && femaleImageLoaded == true) {
                gc.drawImage(
                        femaleImage,
                        getInterX() - babyWidth / 2,
                        getInterY() - babyHeight / 2,
                        babyWidth,
                        babyHeight
                );
            }
            else if (getGender() == Male && maleImageLoaded == true) {
                gc.drawImage(
                        maleImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else if (getGender() == Female && femaleImageLoaded == true) {
                gc.drawImage(
                        femaleImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setFill(getColor());
                gc.fillRect((getInterX() - height / 2), (getInterY() - width / 2), width / 2, height /2);
                //
                gc.setStroke(getStrokeColor());
                gc.setLineWidth(1);
                gc.strokeRect(getInterX() - height / 2, getInterY() - width / 2, width / 2, height / 2);
            }
        }
        else {
            if (deadImageLoaded == true) {
                gc.drawImage(
                        deadImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setStroke(getStrokeColor());
                gc.setLineWidth(1);
                gc.strokeRect(getInterX() - height / 2, getInterY() - width / 2, width / 2, height / 2);
            }
        }
    }
}
