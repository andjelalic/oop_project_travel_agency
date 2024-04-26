package model;

/**
 * Klasa koja predstavlja klijenta agencije.
 * Nasleđuje osnovne karakteristike i funkcionalnosti korisnika iz klase User.
 */
public class Klijent extends Korisnik {
    private String brojTelefona;
    private String jmbg;
    private String brojBankovnogRacuna;

    /**
     * Konstruktor za inicijalizaciju klijenta.
     * @param id            Identifikacioni broj klijenta.
     * @param ime     Ime klijenta.
     * @param prezime      Prezime klijenta.
     * @param korisnickoIme      Korisničko ime klijenta.
     * @param lozinka      Lozinka klijenta.
     * @param brojTelefona   Broj telefona klijenta.
     * @param jmbg          JMBG klijenta.
     * @param brojBankovnogRacuna Broj bankovnog računa klijenta.
     */
    public Klijent(int id, String ime, String prezime, String korisnickoIme, String lozinka, String brojTelefona, String jmbg, String brojBankovnogRacuna) {
        super(id, ime, prezime, korisnickoIme, lozinka);
        this.brojTelefona = brojTelefona;
        this.jmbg = jmbg;
        this.brojBankovnogRacuna = brojBankovnogRacuna;
    }

    /**
     * Metoda koja vraća broja telefona klijenta.
     * @return Broj telefona klijenta.
     */
    public String getBrojTelefona() {
        return brojTelefona;
    }

    /**
     * Metoda koja vraća JMBG klijenta.
     * @return JMBG klijenta.
     */
    public String getJmbg() {
        return jmbg;
    }

    /**
     * Metoda koja vraća broja bankovnog računa klijenta.
     * @return Broj bankovnog računa klijenta.
     */
    public String getBrojBankovnogRacuna() {
        return brojBankovnogRacuna;
    }
}