package model;

/**
 * Klasa koja predstavlja rezervaziju za aranžman.
 */
public class Rezervacija {
    private Klijent klijent;
    private Aranzman aranzman;
    private TipRezervacije tipRezervacije;
    private double placenaCijena;
    private double ukupnaCijena;

    /**
     * Konstruktor za inicijalizaciju rezervacije.
     * @param klijent           Klijent
     * @param aranzman      Aranžman
     * @param tipRezervacije  Tip rezervacije
     * @param placenaCijena       Plaćena cijena
     * @param ukupnaCijena       Ukupna cijena
     */
    public Rezervacija(Klijent klijent, Aranzman aranzman, TipRezervacije tipRezervacije, double placenaCijena, double ukupnaCijena) {
        this.klijent = klijent;
        this.aranzman = aranzman;
        this.tipRezervacije = tipRezervacije;
        this.placenaCijena = placenaCijena;
        this.ukupnaCijena = ukupnaCijena;
    }

    /**
     * Metoda koja vraća klijenta.
     * @return Klijent
     */
    public Klijent getKlijent() {
        return klijent;
    }

    /**
     * Metoda koja vraća aranžman koji se rezerviše.
     * @return Rezervisani aranžman
     */
    public Aranzman getAranzman() {
        return aranzman;
    }

    /**
     * Metoda koja vraća tip rezervacije.
     * @return Tip rezervacije
     */
    public TipRezervacije getTipRezervacije() {
        return tipRezervacije;
    }

    /**
     * Metoda koja vraća iznos koji je klijent do sad uplatio.
     * @return Uplaćeni iznos
     */
    public double getPlacenaCijena() {
        return placenaCijena;
    }

    /**
     * Metoda koja vraća ukupnu cijenu.
     * @return Ukupna cijena
     */

    public double getUkupnaCijena() {
        return ukupnaCijena;
    }

    /**
     * Metoda koja postavlja tip rezervacije.
     * @param tipRezervacije Tip rezervacije
     */
    public void setTipRezervacije(TipRezervacije tipRezervacije) {
        this.tipRezervacije = tipRezervacije;
    }

    /**
     * Metoda koja ažurira uplaćenu cijenu.
     * @param placenaCijena Nova uplaćena cijena
     */
    public void setPlacenaCijena(double placenaCijena) {
        this.placenaCijena = placenaCijena;
    }

    /**
     * Metoda koja vraća stringovnu reprezentaciju rezervacije
     * @return Stringovna reprezentacija rezervacije
     */
    @Override
    public String toString() {
        return klijent.getKorisnickoIme() + " " + aranzman.getIme() + " Ukupna cijena: " +  aranzman.punaCijena();
    }

    /**
     * Metoda koja provjerava da li je rezervacija u potpunosti plaćena.
     * @return True ako je u potpunosti plaćena, inače False
     */
    public boolean isPaidInTotal() {
        return placenaCijena == ukupnaCijena;
    }


    /**
     * Metoda koja provjerava da li je rezervacija protekla.
     * Rezervacija je protekla ako je klijent u potpunosti plaito rezervaciju,
     * i ako je došao/prošao dan polaska.
     * @return True ako je rezervacija protekla, False inače.
     */
    public boolean isPastReservation() {
        return isPaidInTotal() && !aranzman.jeUPonudi();
    }

    /**
     * Metoda koja provjerava da li je rezervacija otkazana.
     * Rezervacija je otkazana ako je sam klijent otkazao rezervaciju,
     * ili ako je prošao rok za plaćanje i klijent nije u potpunosti platio rezervaciju.
     * @return True ako je rezervacija otkazana, False inače.
     */
    public boolean isCanceledReservation() {
        return isCanceledByClient() || (!isPaidInTotal() && aranzman.jeProsaoRokPlacanja());
    }

    /**
     * Metoda koja poredi tip rezervacije da vidi da li je aktivna.
     * @return True ako jeste aktivna, False inače.
     */
    public boolean isActiveReservation() {
        return tipRezervacije == TipRezervacije.AKTIVNA;
    }

    /**
     * Metoda koja računa koliko je klijent još treba da uplati novca.
     * @return Preostali iznos za uplatu
     */
    public double leftToPay() {
        return ukupnaCijena - placenaCijena;
    }

    /**
     * Metoda koja provjerava da li je ističe rok za uplatu.
     * Provjerava se da li je ostalo tri dana do polaska na put, i da li je rezervacija plaćena.
     * @return True ako ističe rok, False inače.
     */
    public boolean threeDaysLeft() {
        return aranzman.jeURoku() && !isPaidInTotal();
    }

    /**
     * Metoda koja provjerava da li klijent može da otkaže rezervaciju.
     * Može da otkaže ako još nije prošao rok za uplatu i ako je rezervacija aktivna.
     * @return True ako može otkazati rezervaciju, False inače.
     */
    public boolean isCancellationAvailable() {
        return aranzman.daniDoPolaska() >= Aranzman.KRAJ_ROKA_UPLATE && isActiveReservation();
    }

    /**
     * Metoda koja provjerava da li je klijent otkazao rezervaciju.
     * Klijent je otkazao rezervaciju ako je sav novac koji je uplatio vraćen.
     * @return True ako je rezervacija otkazana od strane klijenta, False inače.
     */
    public boolean isCanceledByClient() {
        return placenaCijena == 0;
    }

    /**
     * Metoda koja provjerava da li se određeni aranžman poklapa sa aranžmanom rezervacije.
     * @param a Aranžman za provjeru
     * @return True ako se poklapa, False inače
     */
    public boolean arrangementMatch(Aranzman a) {
        return aranzman.getId() == a.getId();
    }

    /**
     * Metoda koja provjerava dali se određeni klijent poklapa sa klijentom rezervacije.
     * @param c Klijent za provjeru
     * @return True ako se poklapa, False inače.
     */
    public boolean clientMatch(Klijent c) {
        return klijent.getId() == c.getId();
    }

    /**
     * Metoda koja provjerava da li je rezervacija već rezervisana.
     * Rezervacija je već rezervisana ako se poklapaju i klijent i aranžman.
     * @param c Klijent za provjeru
     * @param arr Aranžman za provjeru
     * @return True ako je rezervacija već rezervisana, False inače.
     */
    public boolean isAlreadyReserved(Klijent c, Aranzman arr) {
        return clientMatch(c) && arrangementMatch(arr);
    }

    /**
     * Metoda koja provjerava da li se klijent i tip rezervacije poklapaju sa određenim klijentom i tipom.
     * @param id Identifikator klijenta
     * @param rt Tip rezervacije
     * @return True ako se poklapaju, False inače.
     */
    public boolean getReservationByClientIdAndType(int id, TipRezervacije rt) {
        return klijent.getId() == id && tipRezervacije == rt;
    }

    /**
     * Metoda koja provjerava da li se klijent mora obavjestiti za rok plaćanja rezervacije.
     * Klijent se mora obavjestiti ako je rezervacija aktivna i ako je ostalo tri dana za uplatu.
     * @param id Identifikator klijenta
     * @return True ako se mora obavjestiti, False inače.
     */
    public boolean needToBeAlerted(int id) {
        return klijent.getId() == id && threeDaysLeft() && isActiveReservation();
    }

    /**
     * Metoda koja provjerava da li se za ovu rezervaciju mora vratiti novac, kada admin otkaže rezervaciju.
     * Novac se vraća ako se poklapa aranžman koji se otkazuje i ako je rezervacija aktivna.
     * @param arr Aranžman koji se otkazuje
     * @return True ako treba vratiti novac, False inače.
     */
    public boolean checkForRefund(Aranzman arr) {
        return arrangementMatch(arr) && !isCanceledReservation();
    }
}