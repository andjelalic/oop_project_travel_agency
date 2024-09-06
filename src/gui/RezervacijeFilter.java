package gui;

import baza.BazaKonekcija;
import model.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class RezervacijeFilter {
    public static List<Rezervacija> filtrirajRezervacije(List<Rezervacija> rezervacije, TipRezervacije tipRezervacije, int id) {
        return rezervacije
                .stream()
                .filter(rezervacija -> rezervacija.getReservationByClientIdAndType(id, tipRezervacije))
                .toList();
    }
    public static void oznaciRezervacije(List<Rezervacija> rezervacije) {
        for (Rezervacija rezervacija : rezervacije)
            if (rezervacija.isPastReservation())
                rezervacija.setTipRezervacije(TipRezervacije.ISTEKLA);
            else if (rezervacija.isCanceledReservation())
                rezervacija.setTipRezervacije(TipRezervacije.OTKAZANA);
            else
                rezervacija.setTipRezervacije(TipRezervacije.AKTIVNA);
    }
    public static void dodajRezervacije(List<Rezervacija> rezervacije, Rezervacija rezervacija) throws SQLException {
        rezervacije.add(rezervacija);
        BazaKonekcija.addReservation(
                rezervacija.getKlijent().getId(),
                rezervacija.getAranzman().getId(),
                rezervacija.getAranzman().punaCijena(),
                rezervacija.getPlacenaCijena()
        );
    }
    //ove dvije metode su za pocetak, ako uspijem inicijalizovati status1
    public static double preostaliIznos(List<Rezervacija> rezervacije, int id) {
        return rezervacije
                .stream()
                .filter(r -> r.getReservationByClientIdAndType(id, TipRezervacije.AKTIVNA))
                .mapToDouble(Rezervacija::leftToPay)
                .sum();
    }
    public static double potrosenNovac(List<Rezervacija> rezervacije, Klijent klijent) {
        double novac = 0;

        for (Rezervacija rezervacija : rezervacije) {
            if (rezervacija.clientMatch(klijent)) {
                if (rezervacija.getTipRezervacije() == TipRezervacije.OTKAZANA && rezervacija.getPlacenaCijena() != 0)
                    novac += rezervacija.getAranzman().polaCijene(); // Ako je otkazana onda pola cijene, jer je ostalo vraćeno
                else
                    novac += rezervacija.getPlacenaCijena(); // Inače iznos koji je klijent uplatio za rezervaciju
            }
        }

        return novac;
    }
    public static void klijentOtkazujeRezervaciju(Rezervacija rezervacija, BankovniRacun bankovniRacun, BankovniRacun bankovniRacunAgencije) throws SQLException {
        MenadzerTransakcija.izvrsiTransakciju(bankovniRacun, bankovniRacunAgencije, rezervacija.getPlacenaCijena(), true);
        rezervacija.setPlacenaCijena(0);
        rezervacija.setTipRezervacije(TipRezervacije.OTKAZANA);
        BazaKonekcija.updateReservationPaidAmount(rezervacija.getKlijent().getId(), rezervacija.getAranzman().getId(), 0);
    }

    public static void vratiNovacAgenciji(List<Rezervacija> rezervacije, Klijent klijent, BankovniRacun bankovniRacun, BankovniRacun bankovniRacunAgencije) throws SQLException {
        for (Rezervacija r : rezervacije)
            if (r.clientMatch(klijent) && r.isCanceledReservation() && !r.isCanceledByClient()) {
                //50% cijene
                double pola_cijene = r.getAranzman().polaCijene();

                if (r.getPlacenaCijena() != pola_cijene)
                    r.setPlacenaCijena(r.getPlacenaCijena() - pola_cijene);
                MenadzerTransakcija.izvrsiTransakciju(bankovniRacun, bankovniRacunAgencije, r.getPlacenaCijena(), true);
            }
    }

    public static void platiRezervaciju(Rezervacija rezervacija, BankovniRacun bankovniRacun, BankovniRacun bankovniRacunAgencije, double iznos) throws UnsuccessfulReservationException, SQLException {

        double vrijednost = rezervacija.leftToPay();
        if (iznos > vrijednost)
            iznos = vrijednost;

        MenadzerTransakcija.dovoljnoNovca(bankovniRacun, iznos);
        rezervacija.setPlacenaCijena(rezervacija.getPlacenaCijena() + iznos);
        MenadzerTransakcija.izvrsiTransakciju(bankovniRacun, bankovniRacunAgencije, iznos, false);
        BazaKonekcija.updateReservationPaidAmount(rezervacija.getKlijent().getId(), rezervacija.getAranzman().getId(), rezervacija.getPlacenaCijena());
    }
}
