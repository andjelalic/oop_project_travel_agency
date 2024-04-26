package model;

import baza.Identifiable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;

/**
 * Klasa koja predstavlja aranžman.
 */
public class Aranzman implements Identifiable {
    private int id;
    private String ime;
    private String destinacija;
    private String prevoz;
    private LocalDate datumPolaska;
    private LocalDate datumDolaska;
    private double cijena;
    private Smjestaj smjestaj;


    // Konstante za rok uplate
    public final static int POCETAK_ROKA_UPLATE = 16;
    public final static int KRAJ_ROKA_UPLATE = 14;

    // Komparatori za poređenje
    public final static Comparator<Aranzman> uporediID = (i1, i2) -> Integer.compare(i1.id, i2.id);

    /**
     * Konstruktor za inicijalizaciju aranžmana.
     * @param id              Identifikator aranžmana
     * @param ime            Naziv putovanja
     * @param destinacija     Destinacija
     * @param prevoz       Tip prevoza
     * @param datumPolaska        Datum polaska
     * @param datumDolaska     Datum dolaska
     * @param cijena           Cijena aranžmana
     * @param accommodation   Smještaj
     */
    public Aranzman(int id, String ime, String destinacija, String prevoz, LocalDate datumPolaska, LocalDate datumDolaska, double cijena, Smjestaj accommodation) {
        this.id = id;
        this.ime = ime;
        this.destinacija = destinacija;
        this.prevoz = prevoz;
        this.datumPolaska = datumPolaska;
        this.datumDolaska = datumDolaska;
        this.cijena = cijena;
        this.smjestaj = accommodation;
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
    public String getIme() {
        return ime;
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
            return ime + " Destinacija: " + destinacija + " Prevoz: " + prevoz + " Polazak: " + datumPolaska + " Dolazak: " + datumDolaska + " Cijena: " + cijena + " " + smjestaj;
        else
            return ime + " Destinacija: " + destinacija + " Prevoz: " + prevoz + " Polazak: " + datumPolaska + " Dolazak: " + datumDolaska + " Cijena: " + cijena;
    }


    /**
     * Metoda koja vraća koliki je iznos za uplatu.
     * Ako je prošao rok za uplatu, klijent koji želi da rezerviše ovaj aranžman,
     * mora da plati punu cijenu, inače se plaća 50% cijene.
     * @return Iznos za uplatu aranžmana
     */
    public double iznosUplate() {
        return jeProsaoRokPlacanja() ? punaCijena() : polaCijene();
    }

    /**
     * Metoda koja vraća da li je prošao rok za uplatu.
     * @return True ako jeste prošao rok, False inače.
     */
    public boolean jeProsaoRokPlacanja() {
        return daniDoPolaska() < KRAJ_ROKA_UPLATE;
    }

    /**
     * Metoda koja računa koliko je još dana ostalo do polaska na put.
     * @return Broj dana do polaska
     */
    public int daniDoPolaska() {
        return razmakIzmedjuDatuma(LocalDate.now(), datumPolaska);
    }

    /**
     * Metoda koja provjerava da li je ostalo 3 dana do polaska,
     * da bi se klijenti za koji su rezervisali taj aranžman mogli obavjestiti.
     * @return True ako je ostalo 3 dana, False inače.
     */
    public boolean jeURoku() {
        return daniDoPolaska() >= KRAJ_ROKA_UPLATE && daniDoPolaska() <= POCETAK_ROKA_UPLATE;
    }
}