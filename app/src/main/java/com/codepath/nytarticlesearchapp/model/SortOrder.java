package com.codepath.nytarticlesearchapp.model;

/**
 * Created by mallikaa on 11/18/16.
 */

public class SortOrder {
    private final String newest = "newest";
    private final String oldest = "oldest";
    private int value;
    private final int NEWEST = 1;
    private final int OLDEST = 2;


    public  SortOrder(int value) {
        this.value = value;
    }

    public String getSortOrder() {
        switch(value) {
            case NEWEST:
                return newest;
            case OLDEST:
                return oldest;
            default:
                return newest;
        }
    }
}
