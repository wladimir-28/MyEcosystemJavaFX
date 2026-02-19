package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.*;
import java.util.ArrayList;

public class MyEcosysObjectManager {
    private ArrayList<AAEntity> objectsList = new ArrayList<>();
    
    public ArrayList<AAEntity> getObjectsList() {return objectsList;}
    
    public void addWolfs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new CarnivoraWolf());
        }
    }
    
    public void addDeers(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new HerbivoryDeer());
        }
    }
    
    public void addRabbits(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new HerbivoryRabbit());
        }
    }
    
    public void addTrees(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new PlantTree());
        }
    }
    
    public void addMediumShrubs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new PlantMediumShrub());
        }
    }
    
    public void addSmallShrubs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(AAEntity.getCounterObjects(), new PlantSmallShrub());
        }
    }
    
    public void deleteLastObject() {
        if (AAEntity.getCounterObjects() != 0) {
            objectsList.remove(AAEntity.getCounterObjects() - 1);
            AAEntity.decrementCounterObject();
        }
    }
}
