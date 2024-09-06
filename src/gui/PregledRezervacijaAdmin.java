package gui;

import model.Aranzman;
import model.Klijent;
import model.Rezervacija;

import java.util.List;

public class PregledRezervacijaAdmin {
    public static List<Klijent> pronadjiKlijente(List<Rezervacija> rezervacije, Aranzman aranzman) {
        return rezervacije
                .stream()
                .filter(r -> r.arrangementMatch(aranzman))
                .map(Rezervacija::getKlijent)
                .toList();
    }
    public static String prikaziInformacije(Rezervacija rezervacija) {
        if (rezervacija.isPaidInTotal())
            return "Klijent " + rezervacija.getKlijent().getKorisnickoIme() + " je u potpunosti platio aranžman!";
        else if (rezervacija.isCanceledByClient())
            return "Klijent " + rezervacija.getKlijent().getKorisnickoIme() + " je otkazao ovu rezervaciju!";
        else if (rezervacija.isCanceledReservation() && !rezervacija.isCanceledByClient())
            return "Rezervacija otkazana! Klijent " + rezervacija.getKlijent().getKorisnickoIme() + " nije uplatio novac na vrijeme!";
        else if (rezervacija.threeDaysLeft())
            return "Uplaćeno: " + rezervacija.getPlacenaCijena() + " Ostalo da plati: " + rezervacija.leftToPay() + " Kontakt telefon: " + rezervacija.getKlijent().getBrojTelefona();
        else
            return "Uplaćeno: " + rezervacija.getPlacenaCijena() + " Ostalo da plati: " + rezervacija.leftToPay();
    }
}
