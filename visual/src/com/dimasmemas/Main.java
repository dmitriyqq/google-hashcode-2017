package com.dimasmemas;
import processing.core.PApplet;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main extends PApplet {

    Grid myGrid;
    static String slicesInputPath = "slices.txt";
    static String gridInputPath = "grid.txt";
    ArrayList <Slice> slices = new ArrayList<Slice>();


    public static void main(String[] args) {
        for(int i = 0; i < args.length; i++){
            System.out.println(i+" "+ args[i]);
        }
        if(args.length < 2) {
            System.out.println("no parameters");
            return;
        }
        gridInputPath = args[0];
        slicesInputPath = args[1];
        PApplet.main("com.dimasmemas.Main", args);
    }

    public void settings() {
        size(1000, 1000);
    }

    public void setup(){
        Config.tomatoImage = loadImage("T.png");
        Config.mushroomImage = loadImage("M.png");

        randomSeed(0);
        String[] strings;

        try{
            String[] file = new File("/home/dima/hashcode/visual/").list();
            for(String s: file){
                System.out.println(s);
            }
            System.out.println(gridInputPath);
            File fl = new File(gridInputPath);
            Scanner input = new Scanner(fl);

            Config.h = input.nextInt(); // number of rows
            Config.w = input.nextInt(); // number of columns
            Config.l = input.nextInt(); // min ingredient on each slice
            Config.r = input.nextInt(); // maximum cells of slice

            Config.cellSize = min(width/Config.w, height/Config.h);
            strings = new String[Config.h];

            for(int i = 0; i < Config.h; i++){
                strings[i] = input.next();
                println(strings[i]);
            }

            input.close();

            myGrid = new Grid(strings, this);
            //slices.add(new (0,0, 1, 1));
            //testCorrect(slices, myGrid);
        }catch(FileNotFoundException e){
            println("Grid file not found");
        }
        importSlices("slice.txt");
        export(slices);
    }

    public void draw(){
        background(255);
        myGrid.draw();
        drawSlices(slices);
    }

    public void drawSlice(Slice s){
        strokeWeight(2);
        stroke(0);

        if(s.isCorrect())
            fill(0,255,0,50);
        else
            fill(255,0,0,50);


        rect(s.x * Config.cellSize, s.y * Config.cellSize,
                s.w * Config.cellSize, s.h * Config.cellSize);

    }

    public void drawSlices(ArrayList<Slice> al){
        for(Slice s: al){
            drawSlice(s);
        }
    }

    public void importSlices(String path){

        try{
            Scanner sc = new Scanner(new File(slicesInputPath));
            int n = sc.nextInt();
            System.out.println( n + " slices in file");
            for(int i = 0; i< n; i++) {
                int x,y,x2,y2;
                if(!sc.hasNext()) break;
                y = sc.nextInt();
                if(!sc.hasNext()) break;
                x = sc.nextInt();
                if(!sc.hasNext()) break;
                y2 = sc.nextInt();
                if(!sc.hasNext()) break;
                x2 = sc.nextInt();
                //println(x + " " + y + " " + w + " " + h);
                slices.add(new Slice(x,y,x2 - x+1,y2 - y+1));
            }

            Slice.testCorrect(slices, myGrid);

        } catch (FileNotFoundException e){
            println("File not found");
        }
    }

    public void export(ArrayList<Slice> sl){
        try{


        File out = new File(slicesInputPath);
            PrintWriter writer = new PrintWriter(out);

        int count = 0;
        ArrayList <Slice> o = new ArrayList<Slice>();
        for(Slice e : sl){
                o.add(e);
        }
        System.out.println("ol.size = " + o.size());
        writer.write((o.size()) + "\n");
            for (int i = slices.size()-1; i >= 0; i--) {
                // test valid
                Slice s1 = slices.get(i);
                count++;
                writer.write(s1.y + " " + s1.x + " " + (s1.y +s1.h - 1) + " " + (s1.x + s1.w -1 ) + "\n");
            }
            writer.flush();
            System.out.println("writed " + count + " slices");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
