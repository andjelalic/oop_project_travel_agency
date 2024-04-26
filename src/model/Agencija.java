package model;

import baza.BazaKonekcija;

import java.sql.SQLException;
import java.util.List;

public class Agencija {
    protected List<Korisnik> korisnici;
    protected List<Aranzman> aranzmani;
    protected List<Smjestaj> smjestaj;
    protected List<BankovniRacun> bankovniRacuni;
    protected List<Rezervacija> rezervacije;
    protected BankovniRacun racunAgencije;

    public final static String DATABASE_ERROR_MESSAGE = "Greška u bazi podataka!";


    public Agencija() throws SQLException {
        // Inicijalizacija liste korisnika, aranžmana, smještaja, bankovnih računa, rezervacija i bankovnog računa agencije iz baze podataka
        korisnici = BazaKonekcija.getUsers();
        aranzmani = BazaKonekcija.getArrangements();
        smjestaj = BazaKonekcija.getAccommodations();
        bankovniRacuni = BazaKonekcija.getBankAccounts();
        rezervacije = BazaKonekcija.getReservations();
        racunAgencije = BazaKonekcija.getAgencyBankAccount();
    }

    public List<Aranzman> getAranzmani() {
        aranzmani.sort(Aranzman.uporediID);
        return aranzmani;
    }

    public List<Korisnik> getKorisnici() {
        return korisnici;
    }

    public List<Rezervacija> getRezervacije() {
        return rezervacije;
    }

    public List<Smjestaj> getSmestaj() {
        return smjestaj;
    }


    public List<BankovniRacun> getBankovniRacuni() {
        return bankovniRacuni;
    }

    public BankovniRacun getRacunAgencije() {
        return racunAgencije;
    }
}
