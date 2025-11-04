package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.*;

public class ImageLoader {

    public static void loadObjectsImages() {
        CarnivoraWolf.loadImages("/wolf_male.png", "/wolf_female.png","/wolf_dead.png");
        HerbivoryRabbit.loadImages("/rabbit_male.png", "/rabbit_female.png","/rabbit_dead.png");
        HerbivoryDeer.loadImages("/deer_male.png", "/deer_female.png","/deer_dead.png");
        PlantTree.loadImages("/tree_summer.png", "/tree_autumn.png","/tree_dead.png");
        PlantMediumShrub.loadImages("/mediumShrub_summer.png", "/mediumShrub_autumn.png","/mediumShrub_dead.png");
        PlantSmallShrub.loadImages("/smallShrub_summer.png", "/smallShrub_autumn.png","/smallShrub_dead.png");
    }
}