package model;

/**
 * Enum koja predstavlja tipove rezervacija.
 */
public enum TipRezervacije {
    AKTIVNA(1),
    ISTEKLA(2),
    OTKAZANA(3);

    private int op;

    /**
     * Konstruktor za enum ReservationType.
     * @param op Numerička vrijednost tipa rezervacije
     */
    private TipRezervacije(int op) {
        this.op = op;
    }

    /**
     * Metoda koja vraća odgovarajući ReservationType na osnovu datog broja.
     * @param i Broj koji predstavlja tip rezervacije
     * @return Odgovarajući ReservationType
     */
    public static TipRezervacije fromInt(int i) {
        switch (i) {
            case 1: return AKTIVNA;
            case 2: return ISTEKLA;
            case 3: return OTKAZANA;
            default: return null;
        }
    }
}