package com.example.myecosystemjavafx.entities;

import static com.example.myecosystemjavafx.Constants.ObjectMode.*;
import static com.example.myecosystemjavafx.Constants.PLANT_REBIRTH_TIME;

public abstract class APlant extends AAEntity {

    protected int corpseTime = PLANT_REBIRTH_TIME;

    public APlant() {
        super();
    }
    
    
    @SuppressWarnings("CopyConstructorMissesField") // точная копия не требуется
    public APlant(APlant original) {
        super(original);
    }

    public abstract APlant cloneObject();

    public void getInfo() {}
    
    @Override
    public void selectObjectMode() {
        if (objectMode != Dead) {objectMode = Rest;}
    }
    
    public void giveBuffDebuff() {}
    
    @Override
    public void actionObject() {
        switch (objectMode) {
            case Rest:
                restAction();
                break;
            case Dead:
                deadAction();
                break;
        }
    }

    @Override
    public void restAction() { }
}