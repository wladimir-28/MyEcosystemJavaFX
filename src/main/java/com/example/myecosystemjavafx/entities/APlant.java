package com.example.myecosystemjavafx.entities;

import static com.example.myecosystemjavafx.Constants.ObjectMode.*;
import static com.example.myecosystemjavafx.Constants.PLANT_REBIRTH_TIME;

public class APlant extends UIObject {

    protected int corpseTime = PLANT_REBIRTH_TIME;

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
        if (objectMode != Dead) {objectMode = Rest;}
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
}