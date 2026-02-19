package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.AAEntity;

import java.util.Random;

import static java.lang.Math.sqrt;

public class MathFunc {
    
    public static int randomNumberOfChildren(AAEntity parent) {
        Random random = new Random();
        return random.nextInt(parent.getMaxNumberOfChildren() + 1); // от 1 до max включительно
    }
    
    public static double calculateDistance(double thisX, double thisY, double otherX, double otherY) {
        double dx = otherX - thisX;
        double dy = otherY - thisY;
        return sqrt((dx * dx + dy * dy));
    }

    public static double randomValueNearby() {
        return (Math.random() < 0.5) ?
                (-2.0 * Constants.BASE_SIZE + (Math.random() * Constants.BASE_SIZE)) :
                (1.5 * Constants.BASE_SIZE + (Math.random() * Constants.BASE_SIZE));}

    public static Constants.ObjectGender getRandomGender() {
        return Math.random() < 0.5 ? Constants.ObjectGender.Male : Constants.ObjectGender.Female;
    }
    
    public static double getDeathProbability(int age, double longevity) {
        return switch (age) {
            case 1, 2, 3, 4 ->  (1 / longevity) * 0.015;
            case 5 ->           (1 / longevity) * 0.02;
            case 6 ->           (1 / longevity) * 0.03;
            case 7 ->           (1 / longevity) * 0.05;
            case 8 ->           (1 / longevity) * 0.08;
            case 9 ->           (1 / longevity) * 0.11;
            case 10 ->          (1 / longevity) * 0.15;
            case 11 ->          (1 / longevity) * 0.175;
            case 12 ->          (1 / longevity) * 0.2;
            default ->          (1 / longevity) * 0.225;
        };
    }
}