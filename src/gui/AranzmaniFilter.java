package gui;

import model.Aranzman;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class AranzmaniFilter {
    public static List<Aranzman> aranzmaniUPonudi(List<Aranzman> aranzmani) {
        return aranzmani
                .stream()
                .filter(Aranzman:: jeUPonudi)
                .toList();
    }



    public static List<Aranzman> filtrirajAranzmane(List<Aranzman> aranzmani, String cijena, String destinacija, String broj_zvjezdica, String vrstaSobe, String prevoz, LocalDate datumPolaska, LocalDate datumDoaska) {
        return aranzmaniUPonudi(aranzmani)
                .stream()
                .filter(a -> cijena.isEmpty() || a.manjaOdDatogIznosa(Double.parseDouble(cijena)))
                .filter(a -> destinacija.isEmpty() || a.podudaranjeDestinacije(destinacija))
                .filter(a -> vrstaSobe == null || pomocniFilterVrstaSobe(a, vrstaSobe))
                .filter(a -> broj_zvjezdica == null || pomocniFilterZvjezdice(a, broj_zvjezdica))
                .filter(a -> prevoz == null || a.podudaranjePrevoza(prevoz))
                .filter(a -> datumPolaska == null || a.jePoslije(datumPolaska))
                .filter(a -> datumDoaska == null || a.jePrije(datumDoaska))
                .toList();
    }

    private static boolean pomocniFilterVrstaSobe(Aranzman aranzmani, String vrstaSobe) {
        return Optional.ofNullable(aranzmani.getSmjestaj())
                .map(s -> s.getTipSobe().equals(vrstaSobe))
                .orElse(false);
    }


    private static boolean pomocniFilterZvjezdice(Aranzman aranzman, String brojZvjezdicaa
    ) {
        return Optional.ofNullable(aranzman.getSmjestaj())
                .map(s -> s.getBrojZvjezdica() == Integer.parseInt(brojZvjezdicaa))
                .orElse(false);
    }
}
