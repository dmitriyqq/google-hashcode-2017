package com.dimasmemas;

import java.util.ArrayList;
import java.util.Random;

public class Slice {
    public int x,y,w,h;

    boolean correct = false;

    public static Random generator = new Random(0);
    Slice(){}

    Slice(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    Slice(Slice other){
        this.x = other.x;
        this.y = other.y;
        this.w = other.w;
        this.h = other.h;
    }

    public static Slice getRandom(){

        int x = (int) Math.round(generator.nextFloat()*(Config.w-1));
        int y = (int) Math.round(generator.nextFloat()*(Config.h-1));

        int maxW = Math.min(Config.w - x - 1, Config.r / 2);
        int w = 1 +  (int) Math.round(generator.nextFloat()*(maxW));
        int maxH = Math.min(Config.h - y - 1, Config.r / w);

        int h = 1 + (int) Math.round(generator.nextFloat()*(maxH));

        //System.out.println("New com.dimasmemas.Slice : " + x + " " + y +
        //                " " + (x + w) + " " + (y  + h) );

        return new Slice(x, y, w, h);

    }

    public static ArrayList<Slice> getRandomList(int n){
        ArrayList <Slice> al = new ArrayList<Slice>();
        for(int i = 0; i< n; i++)
            al.add(Slice.getRandom());
        return al;
    }



    boolean intersect(Slice other){
        Vector2i[] corners = new Vector2i[4];

        // top left;
        corners[0] = new Vector2i(this.x, this.y);

        // top right
        corners[1] = new Vector2i(this.x + this.w, this.y);

        // bottom right
        corners[2] = new Vector2i(this.x + this.w, this.y + this.h);

        // bottom left
        corners[3] = new Vector2i(this.x, this.y + this.h);

        if(this.x == other.x &&
           this.y == other.y &&
           this.w == other.w &&
           this.h == other.h)
            return true;


        for(int i = 0; i < 4; i++){
            if(((corners[i].x > other.x && corners[i].x < other.x + other.w)
             &&(corners[i].y > other.y && corners[i].y < other.y + other.h)
                    ||(((corners[i].x == other.x || corners[i].x == other.x+other.w))
                    &&(corners[i].y > other.y && corners[i].y < other.y + other.h)))
                    ||(((corners[i].y == other.y || corners[i].y == other.y+other.h))
                    &&(corners[i].x > other.x && corners[i].x < other.x + other.w))
                    )
                return true;
        }



        return false;
    }

    public void setCorrect(boolean c){
        correct = c;
    }

    public boolean isCorrect(){
        return correct;
    }

    public int getScore(){
        if(correct){
            return w*h;
        }else{
            return -5;
        }
    }


    public static void testCorrect(ArrayList<Slice> slices, Grid grid){
        int[][] array = new int[grid.getH()][grid.getW()];

        for(int i = 0; i < grid.getH(); i++){
            for(int j = 0; j < grid.getW(); j++){
                array[i][j] = 0;
            }
        }

        for(Slice s1: slices){

            int numT = 0, numM = 0;
            for(int i = s1.y; i < s1.y + s1.h; i++){
                for(int j = s1.x; j < s1.x + s1.w; j++){
                    if(grid.get(j,i)){
                        numT++;
                    }else{
                        numM++;
                    }
                }
            }

            boolean intersect = false;
            if((s1.w * s1.h > Config.r)
                    ||( Math.min(numT, numM) < Config.l)){
                    s1.setCorrect(false);
                System.out.println(s1.w*s1.h + " " + numM + " " + numT);

            }else {
                for(int i = s1.x; i < s1.x+s1.w; i++){
                    for(int j = s1.y; j < s1.y+s1.h; j++){
                        if(array[j][i] != 0)
                            intersect = true;
                        array[j][i]++;
                    }
                }

                if(!intersect)
                    s1.setCorrect(true);
            }

        }

    }

}
