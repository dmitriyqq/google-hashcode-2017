package com.dimasmemas;

import processing.core.PApplet;
import processing.core.PImage;

public class Grid {
    boolean[] grid;
    int w;

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    int h;
    PApplet p;

    Grid(String[] s, PApplet p){
        grid = new boolean[s.length * s[0].length()];

        this.p = p;

        w = s[0].length();
        h = s.length;

        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){

                grid[i*w + j] = (s[i].charAt(j) == 'T');
                p.println(grid[i*w + j]);
            }
        }
    }

    public void set(int x, int y, boolean value){
        grid[y*w + x] = value;
    }

    public boolean get(int x, int y){
        return grid[y*w+x];
    }

    public void draw(){
        for(int i = 0; i < h; i++){
            for(int j = 0; j < w; j++){
                PImage image = (grid[i*w + j]) ? Config.tomatoImage : Config.mushroomImage;
                p.image(image,j*Config.cellSize,i*Config.cellSize,
                        Config.cellSize, Config.cellSize);

            }
        }
    }

}
