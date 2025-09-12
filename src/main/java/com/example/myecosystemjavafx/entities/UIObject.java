package com.example.myecosystemjavafx.entities;

import com.example.myecosystemjavafx.Engines;
import com.example.myecosystemjavafx.MyEcosystemController;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Random;

import static com.example.myecosystemjavafx.Engines.EmotionsType.*;
import static com.example.myecosystemjavafx.Engines.*;
import static com.example.myecosystemjavafx.Engines.EnergyState.*;
import static com.example.myecosystemjavafx.Engines.HungryState.*;
import static com.example.myecosystemjavafx.Engines.ObjectGender.*;
import static com.example.myecosystemjavafx.Engines.ObjectMode.*;
import static java.lang.Math.sqrt;

public class UIObject {
    private static int counterId = 1;
    protected static int counterObjects = 0;
    protected int id;
    protected ObjectGender objectGender;
    protected int age;
    protected double longevity = 1;
    protected boolean isPregnant = false;
    protected int maxNumberOfChildren = 1;
    protected final Color STROKE_COLOR = Color.BLACK;

    protected double centerX;
    protected double centerY;

    protected double currentDirX = 0;
    protected double currentDirY = 0;

    protected double interX;
    protected double interY;

    protected double previousX;
    protected double previousY;

    protected double targetX = 0;
    protected double targetY = 0;

    protected double dangerX = 0;
    protected double dangerY = 0;


    protected double partnerX = 0;
    protected double partnerY = 0;
    protected double partnerDistance = 2 * BIG_RADIUS_VISION;

    protected int maxSatiety = 60;
    protected int satiety = 40;// пока что хардкод
    protected final int MAX_ENERGY = 30;
    protected int energy = 30;// пока что хардкод

    protected double satietyModifier; //модификатор насыщения (для крупных животных - штраф)
    protected int nutritionValue; //сытность, хар-т питательность как жертвы
    protected int strongScore; //сила
    protected int agilityScore; //ловкость

    protected double speedModBase;
    protected double speedModFromState = 1;
    protected double speed;

    protected double bigRadiusVision = BIG_RADIUS_VISION;
    protected double smallRadiusModBase;
    protected double smallRadiusModState = 1;
    protected double smallRadiusVision = SMALL_RADIUS_VISION * smallRadiusModBase * smallRadiusModState;

    protected int corpseTime;

    protected Engines.HungryState hungryState;
    protected Engines.EnergyState energyState;
    protected Engines.DangerState dangerState;
    protected Engines.ObjectMode objectMode;
    protected EmotionsType emotion = None;


    public UIObject() {
        id = counterId++;
        counterObjects++;
        objectGender = Engines.getRandomGender();
        age = 0;
        //System.out.println("new counterObjects = " + counterObjects);

        double safeMarginX = CANVAS_WIDTH * EDGE_AVOIDANCE_PERCENT_X * 0.01;
        double safeMarginY = CANVAS_HEIGHT * EDGE_AVOIDANCE_PERCENT_Y * 0.01;
        centerX = safeMarginX + (int)(Math.random() * (CANVAS_WIDTH - 2 * safeMarginX));
        centerY = safeMarginY + (int)(Math.random() * (CANVAS_HEIGHT - 2 * safeMarginY));
        previousX = centerX;
        previousY = centerY;
    }

    public UIObject(UIObject original) {
        id = counterId++;
        counterObjects++;
        objectGender = Engines.getRandomGender();
        age = 0;
        //System.out.println("copy counterObjects = " + counterObjects);

        this.centerX = original.centerX + Engines.randomValueNearby();
        this.centerY = original.centerY + Engines.randomValueNearby();
        previousX = centerX;
        previousY = centerY;
    }

    public UIObject copy() {
        return new UIObject(this);
    }

//    protected Color getColor() {
//        return Color.WHITE;
//    }

    public static int getCounterObjects() {
        return counterObjects;
    }

    public static int getCounterId() {
        return counterId;
    }

    public int getId() { return id; }

    public void getInfo() {}

    public ObjectGender getGender() {return objectGender;}

    public static void decrementCounterObject() {
        counterObjects--;
    }

    public void printObject(GraphicsContext gc, double updateInterval) { }

    public void printThisObject(Class<?> Loader, GraphicsContext gc, double updateInterval) { }

    protected Color getStrokeColor() {return Color.BLACK;}

    /// ////////////////////////////////////////////////////////
    //public int getSatiety() {return satiety;}

    public double getCenterX() {return centerX;}
    public double getCenterY() {return centerY;}

    public double getPartnerX() {return partnerX;}
    public double getPartnerY() {return partnerY;}

    public double getTargetX() {return targetX;}
    public double getTargetY() {return targetY;}

    public double getBigRadiusVision() {return bigRadiusVision;}

    public double getSmallRadiusVision() {return smallRadiusVision;}

    public boolean getPregnant() {return isPregnant;}

    public int getAge() {return age;}

    protected int getMaxNumberOfChildren() {return maxNumberOfChildren;}

    public double getLongevity() {return longevity;}

    public int getMaxSatiety() {
        return maxSatiety;
    }

    public double getSatietyModifier() {return satietyModifier;}
    public int getNutritionValue() {return nutritionValue;}
    public int getStrongScore() {return strongScore;}
    public int getAgilityScore() {return agilityScore;}

    public void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }

    public void setPartner(double x, double y, double distance) {
        partnerX = x;
        partnerY = y;
        partnerDistance = distance;
    }

    public void setPregnant(boolean value) {isPregnant = value;}

    public double getPartnerDistance() {return partnerDistance;}

    public void setDanger(double x, double y) {
        dangerX = x;
        dangerY = y;
    }

    public Engines.ObjectMode getObjectMode() {return objectMode;}

    public Engines.EmotionsType getEmotion() {return emotion;}

    public void setEmotion(EmotionsType newEmotion) {emotion = newEmotion;}

    public void setObjectMode(Engines.ObjectMode newMode) {objectMode = newMode;}

    public void setDangerState(Engines.DangerState newDangerState) {dangerState = newDangerState;}

    public void addEnergy(int term) {
        energy = energy + term;
        if (energy < 0) { energy = 0;}
        if (energy > MAX_ENERGY) { energy = MAX_ENERGY;}
    }

    public void addSatiety(int term) {
        satiety = satiety + term;
        if (satiety < 0) { satiety = 0;}
        if (satiety > getMaxSatiety()) { satiety = getMaxSatiety();}
    }

    public void addAge(int term) {age += term;}

    /// ///////////////////////////////////////////

    public int randomNumberOfChildren() {
        Random random = new Random();
        return random.nextInt(getMaxNumberOfChildren() + 1); // от 1 до max включительно
    }

    public boolean isItPartner(UIObject other) {
        if (this.getClass().equals(other.getClass()) && other.getAge() >= 1 && this.getGender() != other.getGender()) {
            //System.out.println("Партнер!!!");
            return true;
        }
        else return false;
    }

    public boolean checkHungryDead() {return satiety == 0;}

    public void checkState() {
        if (checkHungryDead())                                                  {
            objectMode = Dead;
            System.out.println("Умер от голода");
        }

        if (satiety < getMaxSatiety() * 0.2)                                        {hungryState = VeryHungry;}
        else if (satiety >= getMaxSatiety() * 0.2 && satiety < getMaxSatiety() * 0.6)   {hungryState = Hungry;}
        else if (satiety >= getMaxSatiety() * 0.6 )                                 {hungryState = Full;}
        //System.out.println(hungryState);

        if (energy < MAX_ENERGY * 0.2)                                      {energyState = VeryLowEnergy;}
        else if (energy >= MAX_ENERGY * 0.2 && energy < MAX_ENERGY * 0.5)   {energyState = LowEnergy;}
        else if (energy >= MAX_ENERGY * 0.5 && energy < MAX_ENERGY * 0.75)  {energyState = HighEnergy;}
        else if (energy >= MAX_ENERGY * 0.75)                               {energyState = VeryHighEnergy;}
        //System.out.println(energyState);
    }

    public void giveBuffDebuff() { } // у каждого дочернего своё

    public void selectObjectMode() { } // у каждого дочернего своё

    public boolean isInRadius(UIObject other, double radius) {
        double dx = other.centerX - this.centerX;
        double dy = other.centerY - this.centerY;
        double distanceSquared = dx * dx + dy * dy;
        return (distanceSquared <= (radius * radius) && distanceSquared != 0);
    }

    private void randomDirection() { // Генерируем случайное направление
        double angle = Math.random() * 2 * Math.PI;
        currentDirX = Math.cos(angle);
        currentDirY = Math.sin(angle);
    }

    private void avoidEdges() { // Определяем безопасную зону % от canvas
        double marginX = CANVAS_WIDTH * EDGE_AVOIDANCE_PERCENT_X * 0.01;
        double marginY = CANVAS_HEIGHT * EDGE_AVOIDANCE_PERCENT_Y * 0.01;

        // Если приближаемся к левому краю
        if (centerX < marginX && currentDirX < 0) {
            currentDirX = Math.abs(currentDirX) * 0.5 + 0.5; // Смещаем направление вправо
        }
        // Если приближаемся к правому краю
        else if (centerX > CANVAS_WIDTH - marginX && currentDirX > 0) {
            currentDirX = -Math.abs(currentDirX) * 0.5 - 0.5; // Смещаем направление влево
        }

        // Если приближаемся к верхнему краю
        if (centerY < marginY && currentDirY < 0) {
            currentDirY = Math.abs(currentDirY) * 0.5 + 0.5; // Смещаем направление вниз
        }
        // Если приближаемся к нижнему краю
        else if (centerY > CANVAS_HEIGHT - marginY && currentDirY > 0) {
            currentDirY = -Math.abs(currentDirY) * 0.5 - 0.5; // Смещаем направление вверх
        }

        // Нормализуем вектор направления после изменений
        double length = sqrt(currentDirX * currentDirX + currentDirY * currentDirY);
        if (length > 0) {
            currentDirX /= length;
            currentDirY /= length;
        }
    }

    public void interpolatedX(double alpha) {
        interX = previousX + ((centerX - previousX) * alpha);
    }

    public double getInterX() {return interX;}

    public double getInterY(){return interY;}

    public void interpolatedY(double alpha) {
        interY = previousY + ((centerY - previousY) * alpha);
    }

    public void walkAction() {
        addEnergy(ENERGY_FOR_WALKING);
        if (getGender() == Male && getPartnerX() != 0 && getPartnerY() != 0)
        {
            currentDirX = partnerX - this.centerX;
            currentDirY = partnerY - this.centerY;
            // Нормализуем вектор (делаем длину = 1)
            double distance = getPartnerDistance();
            if (distance > BASE_SIZE) {
                // Нормализуем вектор
                if (distance > 0) {
                    currentDirX /= distance;
                    currentDirY /= distance;
                }
            } else {
                // Если уже на нужном расстоянии или ближе, останавливаемся
                currentDirX = 0;
                currentDirY = 0;
            }
        }

        else if (Math.random() < 0.2) {
            randomDirection();
        }
        avoidEdges();
        moveObject(currentDirX, currentDirY);
        emotion = None;
    }

    public void restAction() {
        previousX = centerX;
        previousY = centerY;
        addEnergy(ENERGY_FROM_REST);
        emotion = SleepEmotion;
    }

    public void huntingAction() {
            //System.out.println("Включен режим охоты");
            emotion = None;
            if ( targetX == 0 && targetY == 0) {
                walkAction();
            } else {
                currentDirX = targetX - this.centerX;
                currentDirY = targetY - this.centerY;
                // Нормализуем вектор (делаем длину = 1)
                double length = sqrt(currentDirX * currentDirX + currentDirY * currentDirY);
                if (length > 0) {
                    currentDirX /= length;
                    currentDirY /= length;
                }
                addEnergy(ENERGY_FOR_HUNTING);
                avoidEdges();
                moveObject(currentDirX, currentDirY);
            }
    }

    public void fleeingAction() {
        emotion = None;
        addEnergy(ENERGY_FOR_FLEEING);
        currentDirX = this.centerX - dangerX;
        currentDirY = this.centerY - dangerY;

        // Нормализуем вектор (делаем длину = 1)
        double length = sqrt(currentDirX * currentDirX + currentDirY * currentDirY);
        if (length > 0) {
            currentDirX /= length;
            currentDirY /= length;
        }
        avoidEdges();
        moveObject(currentDirX, currentDirY);
    }

    public void deadAction() {
        emotion = None;
        previousX = centerX;
        previousY = centerY;
    }

    public void actionObject() {
        addSatiety(HUNGRY_IN_SECOND);
        switch (objectMode) {
            case Rest:
                restAction();
                break;
            case Walk:
                walkAction();
                break;
            case Hunting:
                huntingAction();
                break;
            case Fleeing:
                fleeingAction();
                break;
            case Dead:
                deadAction();
                break;
        }
    };

    // медведь
//    protected double satietyModifier = 0.6; //модификатор насыщения (для крупных животных - штраф)
//    protected int nutritionValue = 60; //сытность, хар-т питательность как жертвы
//    protected int strongScore = 45; //сила
//    protected int agilityScore = 20; //ловкость


    public static double calcDefenseModifier(double attackStrong, double defendStrong) {
        if (defendStrong == 0) {return 0.0;}
        double difference = Math.abs(attackStrong - defendStrong);
        double result = 0.3 * Math.exp(-(difference * difference) / (2 * 5.5 * 5.5));
        return Math.round(result * 100.0) / 100.0;
    }

    public static double calcEscapeModifier(double attackAgility, double defendAgility) {
        {
            if (attackAgility == defendAgility) {return 0.15;}
            double result;
            double difference = Math.abs(attackAgility - defendAgility);
            if (attackAgility > defendAgility) {
                result = 1 + 14 * Math.exp(-difference / 13.0);
            } else {
                result = 75 - 75 * Math.exp(-difference / 10.5);
            }
            return Math.round((result / 100) * 100.0) / 100.0;
        }
    }

    public void lovePartner(UIObject other) {
        if (getGender() == Female) {
            setPregnant(true);
            //System.out.println("Беременна");
        }
        setEmotion(LoveEmotion);
    }

    public void catchTarget(UIObject other) {
        double DefMod = calcDefenseModifier(this.getStrongScore(), other.getStrongScore());
        double EscMod = calcEscapeModifier(this.getAgilityScore(), other.getAgilityScore());

        double criticalFailProbability = Math.min(DefMod, 1.0);
        double failProbability = Math.min(EscMod + DefMod, 1.0);

        double battleResult = Math.random();
        if (battleResult >= failProbability) {
            double satietyGain = this.getSatietyModifier() * other.getNutritionValue();
            this.addSatiety((int)satietyGain); // + 50 было
            other.setObjectMode(Dead);
            MyEcosystemController.shareFood(this, (int)(satietyGain)); //поделить добычу !
            if (this instanceof ACarnivora) {
                this.setEmotion(JawsEmotion);
            }
            //System.out.println("Атака успешна");

        } else if (battleResult >= criticalFailProbability) {
            this.addEnergy(-30);
            other.addEnergy(20);
            other.setEmotion(SprintEmotion);
            //System.out.println("Жертва дала отпор");

        } else {
            this.setObjectMode(Dead);
            System.out.println("Жертва дала смертельный отпор!");
            other.setEmotion(FuryEmotion);
        }

        this.setTarget(0, 0);
    }

    public void moveObject(double dirX, double dirY) { }

    public boolean isTarget(UIObject other) { return false;}

    public boolean isDanger(UIObject other) { return false;}

    public boolean isSenseDanger() {
        if (CHANCE_OF_SENSING_DANGER  <= 0) {return false;}
        if (CHANCE_OF_SENSING_DANGER  >= 100) {return true;}

        int randomValue = (int)(Math.random() * 100);
        return randomValue < CHANCE_OF_SENSING_DANGER ;
    }

    public boolean isCorpseDelete() {return false;}
}
