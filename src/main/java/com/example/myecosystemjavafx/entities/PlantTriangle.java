package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;

import static com.example.myecosystemjavafx.Engines.BASE_SIZE;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;

public class PlantTriangle extends APlant {

    protected final double length = BASE_SIZE * 0.5;
    protected final double width = BASE_SIZE * 1.6;
    protected final double height = BASE_SIZE * 1.6;

    protected double satietyModifier = 0; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 100; //сытность, хар-т питательность как жертвы
    protected int strongScore = 0; //сила
    protected int agilityScore = 20; //ловкость

    public PlantTriangle(){
        super();
        loadImage("/tree.png", "/tree.png","/deadTree.png");
    }

    public PlantTriangle(PlantTriangle original) {
        super(original);
        loadImage("/tree.png", "/tree.png","/deadTree.png");
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
    public PlantTriangle copy() {
        return new PlantTriangle(this);
    }

    @Override
    public void printObject(GraphicsContext gc, double alpha) {
        //System.out.println("pre x: " + previousX + "  centr X: " + centerX + "  int X: " + interpolatedX(alpha));
        if (objectMode != Dead) {
            if (maleImageLoaded == true) {
                gc.drawImage(
                        maleObjectImage,
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
                gc.setFill(getPlantColor());
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
                        deadObjectImage,
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


