package com.dimasmemas;

import java.util.ArrayList;

public class Slice {
    public int x, y, w, h;

    Slice(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public String toString(){
        String s = "(";
        s += x + ", ";
        s += y + ", ";
        s += w + ", ";
        s += h + ")";
        return s;
    }

    public static int calculateScore(ArrayList<Slice> slices, Grid grid){
        int totalScore = 0;
        for (int i = slices.size()-1; i >= 0; i--) {

            Slice s1 = slices.get(i);
            if (s1.x >= 0 && s1.x < Config.w && s1.y >= 0 && s1.y < Config.h &&
               (s1.x + s1.w) <= Config.w && (s1.y + s1.h) <= Config.h) {

            }else{
                System.out.println("Removing slice out of grid:" + s1.toString());
                slices.remove(s1);
            }
        }
        int[][] array = new int[grid.getH()][grid.getW()];
        for (int i = 0; i < grid.getH(); i++) {
            for (int j = 0; j < grid.getW(); j++) {
                array[i][j] = 0;
            }
        }

        for (int q = slices.size()-1; q >= 0; q--) {
            Slice s1 = slices.get(q);
            int numT = 0, numM = 0;
            for (int i = s1.y; i < s1.y + s1.h; i++) {
                for (int j = s1.x; j < s1.x + s1.w; j++) {
                    if (grid.get(j, i)) {
                        numT++;
                    } else {
                        numM++;
                    }
                }
            }

            if ((s1.w * s1.h > Config.r)|| (Math.min(numT, numM) < Config.l)) {
                System.out.println("Not enough ingredients on slice " +  s1.toString());
                slices.remove(s1);
            } else {
                boolean failed = false;
                for (int i = s1.x; i < s1.x + s1.w; i++) {
                    for (int j = s1.y; j < s1.y + s1.h; j++) {
                        if (array[j][i] != 0) {
                            System.out.println("Slice intersect " + s1.toString() + " at " + i + "," +j);
                            slices.remove(s1);
                            failed = true;
                        }
                        if(failed) break;
                        array[j][i]++;
                    }
                    if(failed) break;
                }
                if(!failed){
                    totalScore+=s1.w*s1.h;
                }
            }
        }
        return totalScore;
    }

}
