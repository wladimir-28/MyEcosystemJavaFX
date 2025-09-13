package com.example.myecosystemjavafx;

import javafx.scene.canvas.GraphicsContext;
import com.example.myecosystemjavafx.entities.UIObject;
import javafx.scene.image.Image;
import static com.example.myecosystemjavafx.Constants.*;

public class Emotions {

    private static Image sleepEmotionImage;
    private static Image sprintEmotionImage;
    private static Image furyEmotionImage;
    private static Image jawsEmotionImage;
    private static Image loveEmotionImage;
    private static Image oldDeadEmotionImage;
    private static boolean emotionsImageLoaded = false;
    private static double emotionSize = BASE_SIZE * 1.3;


    public static void loadEmotionImages() {
        try {
            sleepEmotionImage = new Image("/sleepEmotion.png");
            sprintEmotionImage = new Image("/sprintEmotion.png");
            furyEmotionImage = new Image("/furyEmotion.png");
            jawsEmotionImage = new Image("/jawsEmotion.png");
            loveEmotionImage = new Image("/loveEmotion.png");
            oldDeadEmotionImage = new Image("/oldDeadEmotion.png");
            emotionsImageLoaded = true;
        } catch (Exception e) {
            System.err.println("Не удалось загрузить эмоции: " + e.getMessage());
            emotionsImageLoaded = false;
        }
    }

    public static void playEmotion(GraphicsContext gc, UIObject object, Image emotionImage) {
        long time = System.currentTimeMillis();
        double shakeX = Math.sin(time * 0.01) * 1.5;
        double shakeY = Math.cos(time * 0.008) * 1.5;

        gc.drawImage(
                emotionImage,
                object.getInterX() - 0.1 * emotionSize + shakeX,
                object.getInterY() - 1.5 * emotionSize + shakeY,
                emotionSize,
                emotionSize
        );
    }

    public static void drawEmotion(GraphicsContext gc, UIObject object) {
        switch (object.getEmotion()) {
            case SleepEmotion:
                playEmotion(gc, object, sleepEmotionImage);
                break;
            case FuryEmotion:
                playEmotion(gc, object, furyEmotionImage);
                break;
            case SprintEmotion:
                playEmotion(gc, object, sprintEmotionImage);
                break;
            case JawsEmotion:
                playEmotion(gc, object, jawsEmotionImage);
                break;
            case LoveEmotion:
                playEmotion(gc, object, loveEmotionImage);
                break;
            case OldDeadEmotion:
                playEmotion(gc, object, oldDeadEmotionImage);
                break;
            default:
                break;
        }
    }
}
