package com.company;

public class Terre {
    private int age;
    private int population;
    private static Terre instance;

    private Terre(int population,int age) {
        this.population=population;
        this.age=age;
    }

    public static Terre getInstance(int population , int age) {
        synchronized (Terre.class){
            if(instance == null){
                instance = new Terre(population,age);
            }
            instance.age=age;
            instance.population=population;
            return instance;
        }
    }
    public static void afficher(Terre obj){
        if(obj != null) {
            System.out.println(obj.age+" "+ obj.population);
        }
    }
}


