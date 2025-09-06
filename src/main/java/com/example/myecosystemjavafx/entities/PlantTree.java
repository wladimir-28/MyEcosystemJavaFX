package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.BASE_SIZE;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;

public class PlantTree extends APlant {

    protected static  Image maleImage;
    protected static boolean maleImageLoaded = false;
    protected static Image femaleImage;
    protected static boolean femaleImageLoaded = false;
    protected static Image deadImage;
    protected static boolean deadImageLoaded = false;

    protected final double length = BASE_SIZE * 0.5;
    protected final double width = BASE_SIZE * 1.6;
    protected final double height = BASE_SIZE * 1.6;

    protected double satietyModifier = 0; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 100; //сытность, хар-т питательность как жертвы
    protected int strongScore = 0; //сила
    protected int agilityScore = 20; //ловкость

    public PlantTree(){super();}

    public PlantTree(PlantTree original) {super(original);}

    @Override
    public double getSatietyModifier() {return satietyModifier;}

    @Override
    public int getNutritionValue() {return nutritionValue;}

    @Override
    public int getStrongScore() {return strongScore;}

    @Override
    public int getAgilityScore() {return agilityScore;}

    @Override
    public PlantTree copy() {
        return new PlantTree(this);
    }

    @Override
    protected Color getColor() {return Color.SEAGREEN;}

    public static void loadImages(String maleImagePath, String femaleImagePath, String deadImagePath) {
        try {
            maleImage = new Image(PlantTree.class.getResourceAsStream(maleImagePath));
            maleImageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самца: " + e.getMessage());
        }
        try {
            femaleImage = new Image(PlantTree.class.getResourceAsStream(femaleImagePath));
            femaleImageLoaded = true;

        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображение самки: " + e.getMessage());
        }
        try {
            deadImage = new Image(PlantTree.class.getResourceAsStream(deadImagePath));
            deadImageLoaded = true;
        } catch (Exception e) {
            System.err.println("Не удалось загрузить изображения трупа: " + e.getMessage());
        }
    }

    @Override
    public void printObject(GraphicsContext gc, double alpha) {
        //System.out.println("pre x: " + previousX + "  centr X: " + centerX + "  int X: " + interpolatedX(alpha));
        if (objectMode != Dead) {
            if (maleImageLoaded == true) {
                gc.drawImage(
                        maleImage,
                        getCenterX() - width / 2,
                        getCenterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setStroke(getStrokeColor());
                gc.setLineWidth(1);
                gc.beginPath();
                gc.moveTo(centerX, centerY - length);
                gc.lineTo(centerX - length, centerY + length);
                gc.lineTo(centerX + length, centerY + length);
                gc.closePath();
                gc.stroke();
                gc.setFill(getColor());
                gc.beginPath();
                gc.moveTo(centerX, (centerY - length));    // Начальная точка
                gc.lineTo((centerX - length), (centerY + length));    // Линия к точке 2
                gc.lineTo((centerX + length), (centerY + length));    // Линия к точке 3
                gc.closePath();         // Замыкаем путь (возврат к начальной точке)
                gc.fill();
            }
        }
        else {
            if (deadImageLoaded == true) {
                gc.drawImage(
                        deadImage,
                        getCenterX() - width / 2,
                        getCenterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setStroke(getStrokeColor());
                gc.setLineWidth(1);
                gc.beginPath();
                gc.moveTo(centerX, centerY - length);
                gc.lineTo(centerX - length, centerY + length);
                gc.lineTo(centerX + length, centerY + length);
                gc.closePath();
                gc.stroke();
            }
        }
    }
}


