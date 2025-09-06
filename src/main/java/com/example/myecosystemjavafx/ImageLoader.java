package com.example.myecosystemjavafx;

import com.example.myecosystemjavafx.entities.CarnivoraWolf;
import com.example.myecosystemjavafx.entities.HerbivoryDeer;
import com.example.myecosystemjavafx.entities.HerbivoryRabbit;
import com.example.myecosystemjavafx.entities.PlantTree;

public class ImageLoader {

    public static void loadObjectsImages() {
        CarnivoraWolf.loadImages("/wolf_male.png", "/wolf_female.png","/wolf_dead.png");
        HerbivoryRabbit.loadImages("/rabbit_male.png", "/rabbit_female.png","/rabbit_dead.png");
        HerbivoryDeer.loadImages("/deer_male.png", "/deer_female.png","/deer_dead.png");
        PlantTree.loadImages("/tree.png", "/tree.png","/deadTree.png");
    }

}
