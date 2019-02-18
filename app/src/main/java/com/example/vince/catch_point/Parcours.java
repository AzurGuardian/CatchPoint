package com.example.vince.catch_point;

public class Parcours {

    String nom;
    double distance;
    String difficulte;
    int id;

    public Parcours(int id, String nom, double distance, String difficulte ) {
        this.nom = nom;
        this.distance = distance;
        this.difficulte = difficulte;
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDifficulte() {
        return difficulte;
    }

    public void setDifficulte(String difficulte) {
        this.difficulte = difficulte;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
