package model;

import baza.Identifiable;

/**
 * Klasa koja predstavlja smještaj.
 */
public class Smjestaj implements Identifiable {
    private int id;
    private int brojZvjezdica;
    private String ime;
    private String tipSobe;
    private double cijena;

    /**
     * Konstruktor za inicijalizaciju smještaja.
     * @param id         Identifikator smještaja.
     * @param brojZvjezdica Broj zvjezdica za ocjenu smještaja.
     * @param ime       Naziv smještaja.
     * @param tipSobe   Vrsta sobe.
     * @param cijena      Cijena po noćenju.
     */
    public Smjestaj(int id, int brojZvjezdica, String ime, String tipSobe, double cijena) {
        this.id = id;
        this.brojZvjezdica = brojZvjezdica;
        this.ime = ime;
        this.tipSobe = tipSobe;
        this.cijena = cijena;
    }

    /**
     * Metoda koja vraća String reprezentaciju smještaja.
     * @return Tekstualni prikaz informacija o smještaju.
     */
    @Override
    public String toString() {
        return "[Smještaj] Naziv: " + ime + ", Broj zvjezdica: " + brojZvjezdica + ", Vrsta sobe: " + tipSobe + ", Cijena po noćenju: " + cijena;
    }

    /**
     * Metoda koja vraća identifikacioni broj smještaja.
     * @return Identifikator smještaja.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Metoda koja vraća broj zvjezdica za ocjenu smještaja.
     * @return Broj zvjezdica za ocjenu smještaja.
     */
    public int getBrojZvjezdica() {
        return brojZvjezdica;
    }

    /**
     * Metoda koja vraća naziv smještaja.
     * @return Naziv smještaja.
     */
    public String getIme() {
        return ime;
    }

    /**
     * Metoda koja vraća vrstu sobe smještaja.
     * @return Vrsta sobe.
     */
    public String getTipSobe() {
        return tipSobe;
    }

    /**
     * Metoda koja vraća cijenu po noćenju smještaja.
     * @return Cijena po noćenju.
     */
    public double getCijena() {
        return cijena;
    }
}