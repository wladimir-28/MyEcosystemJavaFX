package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;

public class CarnivoraWolf extends ACarnivora {

    protected final double radius = BASE_SIZE * 0.5;

    protected final double width = BASE_SIZE * 1.4; //
    protected final double height = BASE_SIZE * 1.4; //

    protected double satietyModifier = 1; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue = 40; //сытность, хар-т питательность как жертвы
    protected int strongScore = 30; //сила
    protected int agilityScore = 20; //ловкость

    public CarnivoraWolf(){
        super();
    }


    public CarnivoraWolf(CarnivoraWolf original) {
        super(original);
    }

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
    public void printObject(GraphicsContext gc, double alpha) {
        interpolatedX(alpha);
        interpolatedY(alpha);
        if (objectMode != Dead) {
            Image deerImage = new Image(getClass().getResourceAsStream("/wolf.png"));

            gc.drawImage(
                    deerImage,
                    getInterX() - width / 2,  // X
                    getInterY() - height / 2, // Y
                    width,                // Ширина
                    height                // Высота
            );
        }
    }

//    @Override
//    public void printObject(GraphicsContext gc) {
//        if (objectMode != Dead) {
//            gc.setFill(getCarnivoraColor());
//            gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
//        }
//
//        gc.setStroke(getStrokeColor());  // Чёрный цвет
//        gc.setLineWidth(1);        // Толщина линии
//        gc.strokeOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
//    }
}
