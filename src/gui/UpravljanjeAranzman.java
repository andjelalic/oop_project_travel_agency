package gui;

import baza.BazaKonekcija;
import model.Aranzman;
import model.Smjestaj;

import java.sql.SQLException;
import java.util.List;

public class UpravljanjeAranzman {
    public static void dodajAranzman(List<Aranzman> aranzmani, Aranzman aranzman) throws SQLException {
        aranzmani.add(aranzman);
        BazaKonekcija.addArrangement(
                aranzman.getId(),
                aranzman.getIme(),
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
}
