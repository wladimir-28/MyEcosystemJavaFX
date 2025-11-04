package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.APlant;
import com.example.myecosystemjavafx.entities.UIObject;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ListIterator;
import static com.example.myecosystemjavafx.Constants.*;

public class MyEcosysLifeSim {
    private static ArrayList<UIObject> objectsList;
    
    public MyEcosysLifeSim(ArrayList<UIObject> objectsList) {
        this.objectsList = objectsList;
    }
    
    public void reproductionSimulation() {
        ListIterator<UIObject> iterator = objectsList.listIterator();
        while(iterator.hasNext()) {
            UIObject object = iterator.next();
            if (object.getPregnant()) {
                for (int i = 0; i < object.randomNumberOfChildren(); i++) {
                    iterator.add(object.cloneObject());
                }
                object.setPregnant(false);
            }
        }
    }
    
    public void lifeSimulation() {
        for (UIObject object : objectsList) {
            object.checkState();
            searchTargetDanger(object, objectsList);
            object.selectObjectMode();
            object.giveBuffDebuff();
            object.actionObject();
            interact(object, objectsList, RADIUS_INTERACTION);
            object.getInfo();
        }
        deleteCorpses();
    }
    
    public void searchTargetDanger(UIObject thisObject, ArrayList<UIObject> objectsList) {
        if (thisObject instanceof APlant || thisObject.getObjectMode() == Constants.ObjectMode.Dead) {
            return;
        }
        boolean foundDanger = false;
        
        double minTargetDistance = BIG_RADIUS_VISION;
        double minPartnerDistance = BIG_RADIUS_VISION;
        for (UIObject other : objectsList) {
            if (other != thisObject && other.getObjectMode() != Constants.ObjectMode.Dead &&
                    thisObject.isInRadius(other, thisObject.getBigRadiusVision())) {
                double distance = MathFunc.calculateDistance(thisObject.getCenterX(), thisObject.getCenterY(),
                        other.getCenterX(), other.getCenterY());
                
                if (thisObject.isTarget(other) && distance < minTargetDistance) {
                    minTargetDistance = distance;
                    thisObject.setTarget(other.getCenterX(), other.getCenterY());
                }
                
                if (thisObject.isItPartner(other) && distance <= minPartnerDistance) {
                    minPartnerDistance = distance;
                    thisObject.setPartner(other.getCenterX(), other.getCenterY(), distance);
                }
                
                if (thisObject.isDanger(other)) {
                    thisObject.setDanger(other.getCenterX(), other.getCenterY());
                    if (thisObject.isInRadius(other, SMALL_RADIUS_VISION) && other.isSenseDanger()) {
                        thisObject.setDangerState(Constants.DangerState.Danger);
                        foundDanger = true;
                    }
                }
            }
        }
        
        if (!foundDanger) {
            thisObject.setDangerState(Constants.DangerState.Safety);
        }
    }
    
    public void interact(UIObject thisObject, ArrayList<UIObject> objectsList, double radius) {
        if (thisObject instanceof APlant || thisObject.getObjectMode() == Constants.ObjectMode.Dead) {
            return;
        }
        
        for (UIObject other : objectsList) {
            if (other != thisObject && other.getObjectMode() != Constants.ObjectMode.Dead &&
                    thisObject.isInRadius(other, radius)) {
                if (thisObject.getObjectMode() == Constants.ObjectMode.Hunting && thisObject.isTarget(other)) {
                    thisObject.catchTarget(other);
                }
                
                if ((thisObject.getObjectMode() == Constants.ObjectMode.Walk ||
                        thisObject.getObjectMode() == Constants.ObjectMode.Rest) && thisObject.isItPartner(other)) {
                    thisObject.lovePartner(other);
                }
            }
        }
    }
    
    public static void shareFood(UIObject thisObject, int satiety) {
        int counter = 0;
        for (UIObject other : objectsList) {
            if (counter == MAX_FOOD_SHARING_PARTNERS) {
                break;
            }
            if (other != thisObject && thisObject.getClass().equals(other.getClass()) &&
                    thisObject.isInRadius(other, SHARE_FOOD_DISTANCE)) {
                if (counter == 0) {
                    other.addSatiety((int) (satiety * 0.5));
                } else if (counter == 1) {
                    other.addSatiety((int) (satiety * 0.25));
                } else {
                    other.addSatiety((int) (satiety * 0.1));
                }
                counter += 1;
            }
        }
    }
    
    public void deleteCorpses() {
        Iterator<UIObject> iterator = objectsList.iterator();
        while (iterator.hasNext()) {
            UIObject object = iterator.next();
            
            if (object != null && object.isCorpseDelete()) {
                iterator.remove();
                UIObject.decrementCounterObject();
            }
        }
    }
}
