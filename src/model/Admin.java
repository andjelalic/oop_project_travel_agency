package model;

public class Admin extends Korisnik {

    public Admin(int id, String firstName, String lastName, String username, String password) {
        super(id, firstName, lastName, username, password);
    }

    public boolean isDefaultPassword() {
        return lozinka.equals("12345678");
    }
}
