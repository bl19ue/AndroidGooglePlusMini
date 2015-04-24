package com.my.sumit.models;

import com.google.api.services.plusDomains.model.Circle;

import java.util.List;

/**
 * Created by Ken on 3/16/2015.
 */
public class CurrentCircle {
    private static List<Circle> circles;

    public CurrentCircle(List<Circle> circles){
        this.circles = circles;
    }

    public static List<Circle> getCircles(){
        return circles;
    }
}
