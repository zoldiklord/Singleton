package com.company;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Startup
@Singleton
public class Singleton_JRE {

    private int population ;
    private int age ;
    @PostConstruct
    void init (int pop , int ag){
        population=pop;
        age=ag;
    }

    void afficher(){
            System.out.println(this.age+" "+ this.population);
    }
}