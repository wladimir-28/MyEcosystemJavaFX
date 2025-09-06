package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.MyEcosystemController;
import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.DangerState.*;
import static com.example.myecosystemjavafx.Engines.EmotionsType.*;
import static com.example.myecosystemjavafx.Engines.EnergyState.*;
import static com.example.myecosystemjavafx.Engines.HungryState.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;

public class ACarnivora extends UIObject {

    protected double speedModBase = 1.3;
    protected double visionModBase = 1.4;
    protected double speedModFromState = 1;
    protected double speed = SPEED * speedModBase * speedModFromState;
    protected double vision = RADIUS_SMELL_HEAR * visionModBase;
    protected int corpseTime = CORPSE_TIME;
    protected EmotionsType emotion = None;

    public ACarnivora() {
        super();
    }

    public ACarnivora(ACarnivora original) {
        super(original);
    }

    @Override
    public ACarnivora copy() {
        return new ACarnivora(this);
    }

    @Override
    public double getRadiusSmellHear() {
        return vision;
    }

    @Override
    public void getInfo() {
        //System.out.println("ID: " + getId());
        //System.out.println("speedModFromState: " + speedModFromState);
        //System.out.println("Mode: " + objectMode);
        //System.out.println("danger x y: " + "\t" + dangerX + "\t" + dangerY);
        //System.out.println("Скорость: " + speed);

    }

    @Override
    public void giveBuffDebuff() {
        double dangerMod = 1;
        double hungryMod = 1;
        double energyMod = 1;
        double seasonMod = 1;

        if (MyEcosystemController.getSeasonOfYear() == SeasonsOfYear.Winter) {
            seasonMod = 0.9;
        } else {seasonMod = 1;}
        if (dangerState == Danger) {dangerMod = 1.4;}
        if (hungryState == VeryHungry) {hungryMod = 1.4;}
        else if (hungryState == Hungry) {hungryMod = 1.2;}
        if (energyState == VeryLowEnergy) {energyMod = 0.6;}
        else if (energyState == LowEnergy) {energyMod = 0.8;}

        speedModFromState = dangerMod * hungryMod * energyMod;
        speed = SPEED * speedModBase * seasonMod * speedModFromState;
    }

    @Override
    public void selectObjectMode() {
        if (objectMode == Dead || satiety == 0)                             {objectMode = Dead;}
        else if (dangerState == Danger)                                     {objectMode = Fleeing;}
        else if ((hungryState == VeryHungry && energy != 0) ||
                (hungryState == Hungry && energyState != VeryLowEnergy))    {objectMode = Hunting;}
        else if ((objectMode == Rest && energy != MAX_ENERGY) ||
                energyState == VeryLowEnergy ||
                (hungryState == Full && energyState != VeryHighEnergy))     {objectMode = Rest;}
        else                                                                {objectMode = Walk;}
    }


    @Override
    public void moveObject(double dirX, double dirY) {
        previousX = centerX;
        previousY = centerY;
        double dx = dirX * speed;
        double dy = dirY * speed;
        centerX += dx;
        centerY += dy;
    }

    @Override
    public boolean isTarget(UIObject other) {return other instanceof AHerbivory;}

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