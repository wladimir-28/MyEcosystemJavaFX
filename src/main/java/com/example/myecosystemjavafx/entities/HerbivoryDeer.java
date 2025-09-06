package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.BASE_SIZE;
import static com.example.myecosystemjavafx.Engines.ObjectGender.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;


public class HerbivoryDeer extends AHerbivory {

    protected static  Image maleImage;
    protected static boolean maleImageLoaded = false;
    protected static Image femaleImage;
    protected static boolean femaleImageLoaded = false;
    protected static Image deadImage;
    protected static boolean deadImageLoaded = false;

    protected final double width = BASE_SIZE * 1.8;
    protected final double height = BASE_SIZE * 1.8;
    protected final double babyWidth = 0.8 * width;
    protected final double babyHeight = 0.8 * height;

    protected double satietyModifier = 0.75; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 60; //сытность, хар-т питательность как жертвы
    protected int strongScore = 20; //сила
    protected int agilityScore = 25; //ловкость

    public HerbivoryDeer(){
        super();
    }

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
    public HerbivoryDeer copy() {
        return new HerbivoryDeer(this);
    }

    @Override
    protected Color getColor() {return Color.SANDYBROWN;}

    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(HerbivoryDeer.class.getResourceAsStream(maleImagePath));
            maleImageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самца оленя: " + e.getMessage());
        }
        try {
            femaleImage = new Image(HerbivoryDeer.class.getResourceAsStream(femaleImagePath));
            femaleImageLoaded = true;


        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самки оленя: " + e.getMessage());
        }
        try {
            deadImage = new Image(HerbivoryDeer.class.getResourceAsStream(deadImagePath));
            deadImageLoaded = true;
        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения трупа оленя: " + e.getMessage());
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
                // контур:
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
