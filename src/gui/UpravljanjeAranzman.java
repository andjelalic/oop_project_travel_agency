package gui;

import baza.BazaKonekcija;
import model.Aranzman;
import model.BankovniRacun;
import model.Rezervacija;
import model.Smjestaj;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class UpravljanjeAranzman {
    public static void dodajAranzman(List<Aranzman> aranzmani, Aranzman aranzman) throws SQLException {
        aranzmani.add(aranzman);
        BazaKonekcija.addArrangement(
                aranzman.getId(),
                aranzman.getNaziv(),
                aranzman.getDestinacija(),
                aranzman.getPrevoz(),
                aranzman.getDatumPolaska(),
                aranzman.getDatumDolaska(),
                aranzman.getCijena(),
                aranzman.getSmjestaj() != null ? aranzman.getSmjestaj().getId() : null
        );
    }
    public static void dodajSmjestaj(List<Smjestaj> smjestaji, Smjestaj smjestaj) throws SQLException {
        smjestaji.add(smjestaj);
        BazaKonekcija.addAccommodation(
                smjestaj.getId(),
                smjestaj.getIme(),
                smjestaj.getBrojZvjezdica(),
                smjestaj.getTipSobe(),
                smjestaj.getCijena()
        );
    }
    public static void povratNovca(List<BankovniRacun> bankovniRacuni, List<Rezervacija> rezervacije, Aranzman aranzman, BankovniRacun bankovniRacunAgencije) throws SQLException {
        for (Rezervacija rezervacija : rezervacije)
            if (rezervacija.checkForRefund(aranzman))
                MenadzerTransakcija.izvrsiTransakciju(
                        MenadzerTransakcija.getBankovniRacun(bankovniRacuni, rezervacija.getKlijent().getBrojBankovnogRacuna()),
                        bankovniRacunAgencije,
                        rezervacija.getPlacenaCijena(),
                        true
                );
    }
    public static void obrisiAranzman(List<Rezervacija> rezervacije, List<Aranzman> aranzmani, List<Smjestaj> smjestaj, Aranzman aranzman) throws SQLException {
        Iterator<Rezervacija> it = rezervacije.iterator();
        boolean removed = false;

        // Provjerava svaku rezervaciju i uklanja one koje se odnose na zadani aranžman
        while (it.hasNext()) {
            Rezervacija rezervacija = it.next();
            if (rezervacija.arrangementMatch(aranzman)) {
                it.remove();
                removed = true;
            }
        }

        // Ako su postojale rezervacije za taj aranžman, uklanjaju se iz baze podataka
        if (removed)
            BazaKonekcija.deleteObject(aranzman.getId(), "rezervacija", "Aranzman_id");

        // Uklanja aranžman iz liste aranžmana i iz baze podataka
        aranzmani.remove(aranzman);
        BazaKonekcija.deleteObject(aranzman.getId(), "aranzman", "id");

        // Ako aranžman ima pridruženi smještaj, uklanja se smještaj iz liste i iz baze podataka
        if (aranzman.getSmjestaj() != null) {
            smjestaj.remove(aranzman.getSmjestaj());
            BazaKonekcija.deleteObject(aranzman.getSmjestaj().getId(), "smjestaj", "id");
        }
    }
    public static boolean validanDatum(LocalDate s) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(s.toString(), formatter);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean ispravanUnosDatuma(LocalDate s1, LocalDate s2) {
        LocalDate datum1 = LocalDate.parse(s1.toString());
        LocalDate datum2 = LocalDate.parse(s2.toString());
        return !datum1.isBefore(datum2);
    }


}
