package com.example.pasta.Model;

public class Gonderi {

    //firebaseden gonderi ile ilgili verileri çekebilmek için

    private String gonderiId;
    private String gonderiResmi;
    private String gonderen;
    private String yemekadi;
    private String malzemeler;
    private String yapilis;


    public Gonderi() {
    }

    public Gonderi(String gonderiId, String gonderiResmi, String gonderen, String yemekadi, String malzemeler, String yapilis) {
        this.gonderiId = gonderiId;
        this.gonderiResmi = gonderiResmi;
        this.gonderen = gonderen;
        this.yemekadi = yemekadi;
        this.malzemeler = malzemeler;
        this.yapilis = yapilis;
    }


    public String getGonderiId() {
        return gonderiId;
    }

    public void setGonderiId(String gonderiId) {
        this.gonderiId = gonderiId;
    }

    public String getGonderiResmi() {
        return gonderiResmi;
    }

    public void setGonderiResmi(String gonderiResmi) {
        this.gonderiResmi = gonderiResmi;
    }

    public String getGonderen() {
        return gonderen;
    }

    public void setGonderen(String gonderen) {
        this.gonderen = gonderen;
    }

    public String getYemekadi() {
        return yemekadi;
    }

    public void setYemekadi(String yemekadi) {
        this.yemekadi = yemekadi;
    }

    public String getMalzemeler() {
        return malzemeler;
    }

    public void setMalzemeler(String malzemeler) {
        this.malzemeler = malzemeler;
    }

    public String getYapilis() {
        return yapilis;
    }

    public void setYapilis(String yapilis) {
        this.yapilis = yapilis;
    }
}
