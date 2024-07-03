package model;

public class BankovniRacun {
    private int id;
    private String jmbg;
    private String brojRacuna;
    private double stanje_racuna;

    public BankovniRacun(int id, String jmbg, String brojRacuna, double stanje_racuna) {
        this.id = id;
        this.jmbg = jmbg;
        this.brojRacuna = brojRacuna;
        this.stanje_racuna = stanje_racuna;
    }

    public int getId() {
        return id;
    }

    public String getJmbg() {
        return jmbg;
    }

    public String getBrojRacuna() {
        return brojRacuna;
    }

    public double getStanje_racuna() {
        return stanje_racuna;
    }

    public void setStanje_racuna(double stanje_racuna) {
        this.stanje_racuna = stanje_racuna;
    }

    public boolean provjeraBroja(String s) {
        return brojRacuna.equals(s);
    }
    public boolean isAgencyBankAccount() {
        return jmbg.length() == 10;
    }
    public boolean jmbgProvjera(String s) {
        return jmbg.equals(s);
    }
}