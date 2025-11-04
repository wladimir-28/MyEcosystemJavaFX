package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.*;
import java.util.ArrayList;

public class MyEcosysObjectManager {
    private ArrayList<UIObject> objectsList = new ArrayList<>();
    
    public ArrayList<UIObject> getObjectsList() {return objectsList;}
    
    public void addWolfs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new CarnivoraWolf());
        }
    }
    
    public void addDeers(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new HerbivoryDeer());
        }
    }
    
    public void addRabbits(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new HerbivoryRabbit());
        }
    }
    
    public void addTrees(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantTree());
        }
    }
    
    public void addMediumShrubs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantMediumShrub());
        }
    }
    
    public void addSmallShrubs(int count) {
        for (int i = 0; i < count; i++) {
            objectsList.add(UIObject.getCounterObjects(), new PlantSmallShrub());
        }
    }
    
    public void deleteLastObject() {
        if (UIObject.getCounterObjects() != 0) {
            objectsList.remove(UIObject.getCounterObjects() - 1);
            UIObject.decrementCounterObject();
        }
    }
}
