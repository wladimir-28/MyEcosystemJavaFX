package com.example.myecosystemjavafx;

public class Constants {
    public static final int     CANVAS_WIDTH = 1520; // ширина поля
    public static final int     CANVAS_HEIGHT = 800; // высота поля
    public static final int     WINDOW_WIDTH = 1600; // ширина окна
    public static final int     WINDOW_HEIGHT = 950; // высота окна
    public static final int     SECOND_IN_SEASON = 10;
    public static final double  EDGE_AVOIDANCE_PERCENT_X = 2.5; // защитная зона по краям поля по X
    public static final double  EDGE_AVOIDANCE_PERCENT_Y = 5.0; //  защитная зона по краям поля по Y
    public static final double  HALF = 0.5;
    public static double        temp_k = 1.5; // для масштабирования изображений

    public static final double  BASE_SIZE = 10; // базовый размер объекта
    public static final double  BIG_RADIUS_VISION = BASE_SIZE * 65;
    public static final double  SMALL_RADIUS_VISION = BASE_SIZE * 18;
    public static final int     CHANCE_OF_SENSING_DANGER = 20;
    public static final double  RADIUS_INTERACTION = BASE_SIZE; // радиус взаимодействия объектов
    public static final double  SHARE_FOOD_DISTANCE = 4 * BASE_SIZE; // радиус на котором объект может поделиться добычей
    public static final int     MAX_FOOD_SHARING_PARTNERS = 2; // максимальное количество особей, с которыми можно поделиться едой

    public static final double  SPEED = 9;
    public static final double  CARNIVORA_SPEED_MOD_BASE = 1.3;
    public static final double  CARNIVORA_VISION_MOD_BASE = 1.4;
    public static final double  HERBIVORY_SPEED_MOD_BASE = 1;
    public static final double  HERBIVORY_VISION_MOD_BASE = 1;

    public static final int     GROWING_UP_AGE = 1;
    public static final int     PLANT_GROWING_UP_AGE = 5;
    public static final int     CORPSE_TIME = 3; // время существования трупа (циклов/секунд)
    public static final int     PLANT_REBIRTH_TIME = 10; // время до возрождения растений

    public static final int     ENERGY_FOR_WALKING = -1;
    public static final int     ENERGY_FOR_HUNTING = -2;
    public static final int     ENERGY_FOR_FLEEING = -2;
    public static final int     ENERGY_FROM_REST = 4;
    public static final int     HUNGRY_IN_SECOND = -1;

    // модификаторы скорости в методах giveBuffDebuff() каждого подкласса

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

    public enum EmojiType {
        None,
        SleepEmoji,
        FuryEmoji,
        SprintEmoji,
        JawsEmoji,
        LoveEmoji,
        OldDeadEmoji
    }
}