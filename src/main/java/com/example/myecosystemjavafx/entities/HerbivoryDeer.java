package com.example.myecosystemjavafx.entities;

import javafx.scene.canvas.GraphicsContext;

import static com.example.myecosystemjavafx.Engines.BASE_SIZE;
import static com.example.myecosystemjavafx.Engines.ObjectGender.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;


public class HerbivoryDeer extends AHerbivory {
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
        loadImage("/deer_male.png", "/deer_female.png","/deadDeer.png");
    }

    public HerbivoryDeer(HerbivoryDeer original) {
        super(original);
        loadImage("/deer_male.png", "/deer_female.png","/deadDeer.png");
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
    public void printObject(GraphicsContext gc, double alpha) {
        interpolatedX(alpha);
        interpolatedY(alpha);
        if (objectMode != Dead) {
            if (getAge() < 1 && femaleImageLoaded == true) {
                gc.drawImage(
                        femaleObjectImage,
                        getInterX() - babyWidth / 2,
                        getInterY() - babyHeight / 2,
                        babyWidth,
                        babyHeight
                );
            }
            else if (getGender() == Male && maleImageLoaded == true) {
                gc.drawImage(
                        maleObjectImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else if (getGender() == Female && femaleImageLoaded == true) {
                gc.drawImage(
                        femaleObjectImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setFill(getHerbivoryColor());
                gc.fillRect((centerX - height / 2), (centerY - width / 2), width / 2, height /2);
            }
        }
        else {
            if (deadImageLoaded == true) {
                gc.drawImage(
                        deadObjectImage,
                        getInterX() - width / 2,
                        getInterY() - height / 2,
                        width,
                        height
                );
            } else {
                gc.setStroke(getStrokeColor());
                gc.setLineWidth(1);
                gc.strokeRect(centerX - height / 2, centerY - width / 2, width / 2, height / 2);
            }
        }
    }
}
