package com.example.myecosystemjavafx.entities;

import javafx.scene.paint.Color;

import static com.example.myecosystemjavafx.Engines.ObjectMode.*;
import static com.example.myecosystemjavafx.Engines.PLANT_REBIRTH_TIME;

public class APlant extends UIObject {

    protected int corpseTime = PLANT_REBIRTH_TIME;
    protected boolean isPregnant = false;

    protected Color getPlantColor() {
        return Color.SEAGREEN;
    }

    public APlant() {
        super();
    }

    public APlant(APlant original) {
        super(original);
    }

    @Override
    public APlant copy() {
        return new APlant(this);
    }


    @Override
    public void selectObjectMode() {
        if (objectMode != Dead) {
            objectMode = Rest;
        }
    }

    @Override
    public void actionObject() {
        switch (objectMode) {
            case Rest:
                restAction();
                break;
            case Dead:
                deadAction();;
                break;
        }
    };

    @Override
    public void restAction() { }

    @Override
    public void deadAction() {
        if (corpseTime == 0) {
            objectMode = Rest;
            corpseTime = PLANT_REBIRTH_TIME;
        } else {
            corpseTime -= 1;
        }
    }
}