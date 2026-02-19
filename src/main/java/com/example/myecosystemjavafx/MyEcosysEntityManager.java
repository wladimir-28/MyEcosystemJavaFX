package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.*;
import java.util.ArrayList;
import java.util.Comparator;

public class MyEcosysEntityManager {
    private ArrayList<AAEntity> entitiesList = new ArrayList<>();
    
    public ArrayList<AAEntity> getEntitiesList() {return entitiesList;}
    
    public ArrayList<AAEntity> getYSortEntitiesList() {
        entitiesList.sort(Comparator.comparingDouble(AAEntity::getCenterY));
        return entitiesList;
    }
    
    public void addWolfs(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new CarnivoraWolf());
        }
    }
    
    public void addDeers(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new HerbivoryDeer());
        }
    }
    
    public void addRabbits(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new HerbivoryRabbit());
        }
    }
    
    public void addTrees(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new PlantTree());
        }
    }
    
    public void addMediumShrubs(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new PlantMediumShrub());
        }
    }
    
    public void addSmallShrubs(int count) {
        for (int i = 0; i < count; i++) {
            entitiesList.add(AAEntity.getCounterObjects(), new PlantSmallShrub());
        }
    }
    
    public void deleteLastObject() {
        if (AAEntity.getCounterObjects() != 0) {
            entitiesList.remove(AAEntity.getCounterObjects() - 1);
            AAEntity.decrementCounterObject();
        }
    }
}
