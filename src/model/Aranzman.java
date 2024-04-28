package model;

import baza.Identifiable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

public class Aranzman implements Identifiable {
    private int id;
    private String naziv;
    private String destinacija;
    private String prevoz;
    private LocalDate datumPolaska;
    private LocalDate datumDolaska;
    private double cijena;
    private Smjestaj smjestaj;



    public final static int POCETAK_ROKA_UPLATE = 16;
    public final static int KRAJ_ROKA_UPLATE = 14;


    public final static Comparator<Aranzman> uporediID = (i1, i2) -> Integer.compare(i1.id, i2.id);


    public Aranzman(int id, String naziv, String destinacija, String prevoz, LocalDate datumPolaska, LocalDate datumDolaska, double cijena, Smjestaj smjestaj) {
        this.id = id;
        this.naziv = naziv;
        this.destinacija = destinacija;
        this.prevoz = prevoz;
        this.datumPolaska = datumPolaska;
        this.datumDolaska = datumDolaska;
        this.cijena = cijena;
        this.smjestaj = smjestaj;
    }

    public static int razmakIzmedjuDatuma(LocalDate d1, LocalDate d2) {
        return (int) ChronoUnit.DAYS.between(d1, d2);
    }

    public boolean jeUPonudi() {
        return datumPolaska.isAfter(LocalDate.now());
    }

    /**
     * Metoda koja vraća identifikator aranžmana.
     * @return Identifikator aranžmana.
     */
    @Override
    public int getId() {
        return id;
    }

    /**
     * Metoda koja vraća naziv putovanja.
     * @return Naziv putovanja
     */
    public String getNaziv() {
        return naziv;
    }

    /**
     * Metoda koja vraća destinaciju.
     * @return Destinacija
     */
    public String getDestinacija() {
        return destinacija;
    }

    /**
     * Metoda koja vraća tip prevoza.
     * @return Tip prevoza
     */
    public String getPrevoz() {
        return prevoz;
    }

    /**
     * Metoda koja vraća datum polaska.
     * @return Datum polaska
     */
    public LocalDate getDatumPolaska() {
        return datumPolaska;
    }

    /**
     * Metoda koja vrača datum dolaska.
     * @return Datum dolaska
     */
    public LocalDate getDatumDolaska() {
        return datumDolaska;
    }

    /**
     * Metoda koja vraća cijenu aranžmana
     * @return Cijena aranžmana
     */
    public double getCijena() {
        return cijena;
    }

    /**
     * Metoda koja vraća smještaj.
     * @return Smještaj
     */
    public Smjestaj getSmjestaj() {
        return smjestaj;
    }

    /**
     * Metoda koja vraća broj noćenja.
     * @return Broj noćenja.
     */
    private int brojNocenja() {
        return razmakIzmedjuDatuma(datumPolaska, datumDolaska) - 1;
    }

    /**
     * Metoda koja računa punu cijenu aranžmana.
     * Ako je aranžman putovanje računamo cijenu aranžmana + (broj noćenja * cijena smještaja po noćenju),
     * inače samo cijenu aranžmana.
     * @return Puna cijena putovanja-
     */
    public double punaCijena() {
        return smjestaj == null ? cijena : cijena + (brojNocenja()*getCijena());
    }

    /**
     * Meotda koja vraća 50% cijene aranžmana.
     * @return 50% cijene
     */
    public double polaCijene() {
        return punaCijena() / 2;
    }

    /**
     * Metoda koja vraća stringovnu reprezentaciju aranžmana.
     * @return Stringovna reprezentacija aranžmana.
     */
    @Override
    public String toString() {
        if (smjestaj != null)
            return naziv + " Destinacija: " + destinacija + " Prevoz: " + prevoz + " Polazak: " + datumPolaska + " Dolazak: " + datumDolaska + " Cijena: " + cijena + " " + smjestaj;
        else
            return naziv + " Destinacija: " + destinacija + " Prevoz: " + prevoz + " Polazak: " + datumPolaska + " Dolazak: " + datumDolaska + " Cijena: " + cijena;
    }



    public double iznosUplate() {
        return jeProsaoRokPlacanja() ? punaCijena() : polaCijene();
    }


    public boolean jeProsaoRokPlacanja() {
        return daniDoPolaska() < KRAJ_ROKA_UPLATE;
    }


    public int daniDoPolaska() {
        return razmakIzmedjuDatuma(LocalDate.now(), datumPolaska);
    }


    public boolean jeURoku() {
        return daniDoPolaska() >= KRAJ_ROKA_UPLATE && daniDoPolaska() <= POCETAK_ROKA_UPLATE;
    }


    public boolean podudaranjeDestinacije(String s) {
        return destinacija.equals(s);
    }


    public boolean podudaranjePrevoza(String s) {
        return prevoz.equals(s);
    }


    public boolean manjaOdDatogIznosa(Double iznos) {
        return punaCijena() <= iznos;
    }


    public boolean jePoslije(LocalDate date) {
        return !datumPolaska.isBefore(date);
    }


    public boolean jePrije(LocalDate datum) {
        return !datumDolaska.isAfter(datum);
    }
    public final static Comparator<Aranzman> porediCijenu = (c1, c2) -> Double.compare(c1.punaCijena(), c2.punaCijena());
    public final static Comparator<Aranzman> porediPremaDatumuPolaska = (o1, o2) -> o1.getDatumPolaska().compareTo(o2.getDatumPolaska());


}