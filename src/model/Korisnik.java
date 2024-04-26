package model;

public abstract class Korisnik {
    protected int id;
    protected String ime;
    protected String prezime;
    protected String korisnickoIme;
    protected String lozinka;

    public Korisnik(int id, String ime, String prezime, String korisnickoIme, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.korisnickoIme = korisnickoIme;
        this.lozinka = lozinka;
    }

    public int getId() {
        return id;
    }


    public String getIme() {
        return ime;
    }


    public String getPrezime() {
        return prezime;
    }


    public String getKorisnickoIme() {
        return korisnickoIme;
    }


    public String getLozinka() {
        return lozinka;
    }


    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }


    @Override
    public String toString() {
        return ime + " " + prezime + " Korisničko ime: " + korisnickoIme;
    }


    public boolean podudaranjeKorisnickoIme(String s) {
        return korisnickoIme.equals(s);
    }


    public boolean podudaranjeLozinka(String s) {
        return lozinka.equals(s);
    }


}
