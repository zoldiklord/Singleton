package com.company;

public class Main {
    public static void main(String[] args) {
        //Terre
        Terre Terre1 = Terre.getInstance(10,11);
        Terre Terre2 = Terre.getInstance(12,15);
        Terre.afficher(Terre1);
        Terre.afficher(Terre2);

        /////////
        //JRE
        Singleton_JRE JRE = new Singleton_JRE();
        JRE.init(1222,1111);
        JRE.afficher();


    }
}
