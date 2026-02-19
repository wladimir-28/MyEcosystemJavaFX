package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.AAEntity;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.ArrayList;
import static com.example.myecosystemjavafx.Constants.*;

public class MyEcosysUIManager {
    private final GraphicsContext gc;
    private final Canvas canvas;
    private final MyEcosysSeasonManager seasonManager;
    
    public MyEcosysUIManager(GraphicsContext gc, Canvas canvas, MyEcosysSeasonManager seasonManager) {
        this.gc = gc;
        this.canvas = canvas;
        this.seasonManager = seasonManager;
    }
    
    public void redrawCanvas() {
        if (seasonManager.isBackgroundImageLoaded()) {
            gc.clearRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
            gc.drawImage(seasonManager.getCurrentBackgroundImage(), 0, 0, canvas.getWidth(), canvas.getHeight());
        } else {
            gc.setFill(seasonManager.getCurrentReserveColor());
            gc.fillRect(0, 0, CANVAS_WIDTH, CANVAS_HEIGHT);
        }
    }
    
    public void drawObjects(ArrayList<AAEntity> objectsList, double alpha) {
        if (objectsList == null) return;
        
        for (AAEntity object : objectsList) {
            object.printObject(gc, alpha);
            EmojiLoader.drawEmotion(gc, object);
        }
    }
    
    public void drawPauseIndicator() {
        gc.setStroke(Color.RED);
        gc.setLineWidth(3);
        gc.strokeRect(2, 2, CANVAS_WIDTH - 4, CANVAS_HEIGHT - 4);
        
        double size = 8;
        double spacing = 4;
        double x = CANVAS_WIDTH - 30;
        double y = 15;
        gc.setFill(Color.RED);
        gc.fillRect(x, y, size, 2.5 * size);
        gc.fillRect(x + size + spacing, y, size, 2.5 * size);
    }
}