package gui;

import baza.BazaKonekcija;
import model.Aranzman;
import model.Smjestaj;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
