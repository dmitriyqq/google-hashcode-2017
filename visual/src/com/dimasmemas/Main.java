package com.dimasmemas;
import processing.core.PApplet;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends PApplet {

    private Grid myGrid;

    private static String slicesInputPath;
    private static String gridInputPath;

    ArrayList <Slice> slices = new ArrayList<Slice>();

    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++){
            System.out.println(i+" "+ args[i]);
        }
        if(args.length < 2) {
            System.out.println("No parameters. Specify path to grid input data and slices data.");
            return;
        }else{
            gridInputPath = args[0];
            slicesInputPath = args[1];
        }

        PApplet.main("com.dimasmemas.Main", args);
    }

    public void settings() {
        size(1000, 1000);
    }

    public void setup(){
        Config.tomatoImage = loadImage("res/T.png");
        Config.mushroomImage = loadImage("res/M.png");

        String[] strings;

        try{
            System.out.println(gridInputPath);
            File fl = new File(gridInputPath);
            Scanner input = new Scanner(fl);

            Config.h = input.nextInt(); // number of rows
            Config.w = input.nextInt(); // number of columns
            Config.l = input.nextInt(); // min ingredient on each slice
            Config.r = input.nextInt(); // maximum cells of slice

            Config.cellSize = min(width / Config.w, height / Config.h);

            strings = new String[Config.h];

            for(int i = 0; i < Config.h; i++){
                strings[i] = input.next();
                println(strings[i]);
            }

            input.close();

            myGrid = new Grid(strings, this);

            importSlices();
            export(slices);
        }catch(FileNotFoundException e){
            println("Grid file not found");
        }

    }

    public void draw(){
        background(255);
        myGrid.draw();
        drawSlices(slices);
    }

    public void drawSlice(Slice s){
        strokeWeight(2);
        stroke(0);

        fill(0,255,0,50);

        rect(s.x * Config.cellSize, s.y * Config.cellSize,
                s.w * Config.cellSize, s.h * Config.cellSize);
    }

    private void drawSlices(ArrayList<Slice> al){
        for(Slice s: al){
            drawSlice(s);
        }
    }

    private void importSlices(){
        try{
            Scanner sc = new Scanner(new File(slicesInputPath));
            int n = sc.nextInt();
            System.out.println( n + " slices in file");
            for(int i = 0; i< n; i++) {

                int y = sc.nextInt(),
                    x = sc.nextInt(),
                    y2 = sc.nextInt(),
                    x2 = sc.nextInt();

                //println(x + " " + y + " " + w + " " + h);
                slices.add(new Slice(x,y,x2 - x+1,y2 - y+1));
            }

            int score = Slice.calculateScore(slices, myGrid);
            System.out.println("Total score: " + score);

        } catch (FileNotFoundException e){
            println("File not found");
        }
    }

    private void export(ArrayList<Slice> sl){
        try{
            File out = new File(slicesInputPath);
            PrintWriter writer = new PrintWriter(out);
            int count = 0;
            writer.write((sl.size()) + "\n");
                for (int i = slices.size()-1; i >= 0; i--) {
                    // test valid
                    Slice s1 = slices.get(i);
                    count++;
                    writer.write(s1.y + " " + s1.x + " " + (s1.y +s1.h - 1) + " " + (s1.x + s1.w -1 ) + "\n");
            }

            writer.flush();
            System.out.println(count + " slices have been written");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
