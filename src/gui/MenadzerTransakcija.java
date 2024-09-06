package gui;

import baza.BazaKonekcija;
import model.BankovniRacun;

import java.sql.SQLException;
import java.util.List;

public class MenadzerTransakcija {
    public static void dovoljnoNovca(BankovniRacun account, double amount) throws UnsuccessfulReservationException {
        if (account.getStanje_racuna() < amount)
            throw new UnsuccessfulReservationException("Nemate dovoljno novca!");
    }
    private static void azurirajStanjeNaRacunu(BankovniRacun klijentBanka, BankovniRacun agencijaBanka) throws SQLException {
        BazaKonekcija.updateBalance(klijentBanka.getId(), klijentBanka.getStanje_racuna());
        BazaKonekcija.updateBalance(agencijaBanka.getId(), agencijaBanka.getStanje_racuna());
    }
    public static BankovniRacun getBankovniRacun(List<BankovniRacun> racuni, String brojRacuna) {
        return racuni
                .stream()
                .filter(a -> a.provjeraBroja(brojRacuna))
                .findFirst()
                .orElse(null);
    }


    public static void izvrsiTransakciju(BankovniRacun klijentBanka, BankovniRacun agencijaBanka, double suma, boolean zaKlijenta) throws SQLException {
        if (zaKlijenta) {
            klijentBanka.setStanje_racuna(klijentBanka.getStanje_racuna() + suma);
            agencijaBanka.setStanje_racuna(agencijaBanka.getStanje_racuna() - suma);
        } else {
            klijentBanka.setStanje_racuna(klijentBanka.getStanje_racuna() - suma);
            agencijaBanka.setStanje_racuna(agencijaBanka.getStanje_racuna() + suma);
        }
        azurirajStanjeNaRacunu(klijentBanka, agencijaBanka);
    }
}
