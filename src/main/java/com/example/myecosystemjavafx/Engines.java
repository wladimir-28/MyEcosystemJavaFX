package com.example.myecosystemjavafx;

import static java.lang.Math.sqrt;

public class Engines {
    public static final int CANVAS_WIDTH = 1520; // ширина поля
    public static final int CANVAS_HEIGHT = 800; // высота поля
    public static final int SECOND_IN_SEASON = 5;
    public static final double EDGE_AVOIDANCE_PERCENT_X = 2.5; // защитная зона по краям поля по X
    public static final double EDGE_AVOIDANCE_PERCENT_Y = 5.0; //  защитная зона по краям поля по Y
    public static final double BASE_SIZE = 11; // базовый размер объекта
    public static final double SPEED = 9;
    public static final double RADIUS_VISION = BASE_SIZE * 18;
    public static final double SHARE_FOOD_DISTANCE = 3 * BASE_SIZE;
    public static final int CORPSE_TIME = 3; // время существования трупа (циклов/секунд)
    public static final int PLANT_REBIRTH_TIME = 15; // время до возрождения растений
    public static final int ENERGY_FOR_WALKING = -1;
    public static final int ENERGY_FOR_HUNTING = -2;
    public static final int ENERGY_FOR_FLEEING = -2;
    public static final int ENERGY_FROM_REST = +4;
    public static final int HUNGRY_IN_SECOND = -1;
    /// модификаторы скорости в методах giveBuffDebuff() каждого подкласса


    public static double calculateDistance(double thisX, double thisY, double otherX, double otherY) {
        double dx = otherX - thisX;
        double dy = otherY - thisY;
        double distance = sqrt((dx * dx + dy * dy));
        return distance;
    }

    public static double randomValueNearby() {
        return (Math.random() < 0.5) ?
                (-1.5 * BASE_SIZE + (Math.random() * BASE_SIZE)) :
                (0.5 * BASE_SIZE + (Math.random() * BASE_SIZE));}

    public enum SeasonsOfYear {
        Spring,
        Summer,
        Autumn,
        Winter

    }

    public enum ObjectGender {
        Male,
        Female
    }

    public static ObjectGender getRandomGender() {
        return Math.random() < 0.4 ? ObjectGender.Male : ObjectGender.Female;
    }

    public enum HungryState {
        Full,       //
        Hungry,     //
        VeryHungry  //
    }

    public enum DangerState {
        Danger,
        Safety
    }

    public enum EnergyState {
        VeryHighEnergy,
        HighEnergy,
        LowEnergy,
        VeryLowEnergy
    }

    public enum ObjectMode {
        Rest,       // отдых
        Walk,       // прогулка
        Hunting,     // Поиск пищи
        Fleeing,    // Бегство от хищника
        Dead       // смерть
    }

    public enum EmotionsType {
        None,
        SleepEmotion,
        FuryEmotion,
        SprintEmotion,
        JawsEmotion,
        LoveEmotion
    }
}
