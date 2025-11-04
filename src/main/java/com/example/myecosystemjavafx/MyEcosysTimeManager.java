package com.example.myecosystemjavafx;

public class MyEcosysTimeManager {
    private long lastUpdateTime = 0;
    private long lastLifeUpdate = 0;
    private long lastSeasonUpdate = 0;
    private long lastYearsUpdate = 0;
    private long lastFrameTime = 0;
    private long accumulatedTime = 0;
    private long lastRenderTime = 0;
    private double alpha = 0.0;
    
    private final long LIFE_UPDATE_INTERVAL = 1_000_000_000L;
    private final long SEASON_INTERVAL = Constants.SECOND_IN_SEASON * 1_000_000_000L;
    private final long YEAR_INTERVAL = Constants.SECOND_IN_SEASON * 4_000_000_000L;
    private static final long TARGET_FRAME_TIME = 22_222_222L; // 45 FPS
    
    public void initializeTime(long now) {
        lastFrameTime = now;
        lastUpdateTime = now;
        lastLifeUpdate = now;
        lastSeasonUpdate = now;
        lastYearsUpdate = now;
        lastRenderTime = now;
    }
    
    public void updateTime(long now) {
        long deltaTime = now - lastFrameTime;
        accumulatedTime += deltaTime;
        alpha = Math.min(1.0, (double) accumulatedTime / LIFE_UPDATE_INTERVAL);
        
        lastFrameTime = now;
        lastRenderTime = now;
    }
    
    public boolean shouldUpdateLife(long now) {
        if (accumulatedTime >= LIFE_UPDATE_INTERVAL) {
            accumulatedTime -= LIFE_UPDATE_INTERVAL;
            lastLifeUpdate = now;
            return true;
        }
        return false;
    }
    
    public boolean shouldRender(long now) {return now - lastRenderTime >= TARGET_FRAME_TIME;}
    
    public boolean shouldUpdateSeason(long now) {return now - lastSeasonUpdate >= SEASON_INTERVAL;}
    
    public void updateSeasonTime(long now) {lastSeasonUpdate = now;}
    
    public boolean shouldUpdateYear(long now) {return now - lastYearsUpdate >= YEAR_INTERVAL;}
    
    public void updateYearTime(long now) {lastYearsUpdate = now;}
    
    public double getAlpha() {return alpha;}
    
    public long getLastFrameTime() {return lastFrameTime;}
}