package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.MyEcosysSeasonManager;

import static com.example.myecosystemjavafx.Constants.*;
import static com.example.myecosystemjavafx.Constants.DangerState.Danger;
import static com.example.myecosystemjavafx.Constants.EmojiType.None;
import static com.example.myecosystemjavafx.Constants.EnergyState.*;
import static com.example.myecosystemjavafx.Constants.HungryState.*;
import static com.example.myecosystemjavafx.Constants.ObjectMode.*;

public abstract class AHerbivory extends UIObject {

    protected double speedModBase = HERBIVORY_SPEED_MOD_BASE;
    protected double speedModFromState = 1;
    protected double speed = SPEED * speedModBase * speedModFromState;


    protected double smallRadiusModBase = HERBIVORY_VISION_MOD_BASE;
    protected double smallRadiusModState = 1;
    protected double smallRadiusVision = SMALL_RADIUS_VISION * smallRadiusModBase * smallRadiusModState;

    protected int corpseTime = CORPSE_TIME;
    protected EmojiType emotion = None;

    public AHerbivory () {super();}
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public AHerbivory (AHerbivory  original) {super(original);}

    public abstract AHerbivory cloneObject();
    
    @Override
    public double getSmallRadiusVision() {
        return smallRadiusVision;
    }

    public void getInfo() {}

    @Override
    public void giveBuffDebuff() {
        double dangerMod = 1;
        double hungryMod = 1;
        double energyMod = 1;
        double seasonMod = 1;

        if (MyEcosysSeasonManager.getSeasonOfYear() == SeasonsOfYear.Winter) {seasonMod = 0.9;}
        if (dangerState == Danger) {dangerMod = 1.4;}
        if (hungryState == VeryHungry) {hungryMod = 0.6;}
        else if (hungryState == Hungry) {hungryMod = 0.9;}
        if (energyState == VeryLowEnergy) {energyMod = 0.6;}
        else if (energyState == LowEnergy) {energyMod = 0.8;}

        speedModFromState = dangerMod * hungryMod * energyMod;
        speed = SPEED * speedModBase * seasonMod * speedModFromState;

        smallRadiusModState = dangerMod * hungryMod * energyMod;
        smallRadiusVision = SMALL_RADIUS_VISION * smallRadiusModBase * smallRadiusModState;
    }

    @Override
    public void selectObjectMode() {
        if (objectMode == Dead || satiety == 0)                             {objectMode = Dead;}
        else if (dangerState == Danger && hungryState != VeryHungry)        {objectMode = Fleeing;}
        else if ((hungryState == VeryHungry && energyState != VeryLowEnergy) ||
                (hungryState == Hungry && energyState != LowEnergy))        {objectMode = Hunting;}
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
    public boolean isDanger(UIObject other) {return other instanceof ACarnivora;}

    @Override
    public boolean isTarget(UIObject other) {
        return other instanceof APlant;
    }

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
