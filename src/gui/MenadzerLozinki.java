package gui;

import baza.BazaKonekcija;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextInputDialog;
import model.Agencija;
import model.Korisnik;

import java.sql.SQLException;
import java.util.Optional;

public class MenadzerLozinki {

    public static void promijeniLozinku(Korisnik korisnik, String lozinka, String table) {
        korisnik.setLozinka(lozinka);
        try {
            BazaKonekcija.changePassword(korisnik.getId(), lozinka, table);
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText(Agencija.DATABASE_ERROR_MESSAGE);
            alert.showAndWait();
        }
    }

    public static void promijeniLozinkuEvnt(Korisnik korisnik, String table) {
        // Kreiranje dijaloga za unos lozinke
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Promjena lozinke");
        dialog.setHeaderText(null);
        dialog.setContentText("Unesite novu lozinku:");

        // Dodavanje polja za unos lozinke (sakriven tekst)
        PasswordField passwordField = new PasswordField();
        dialog.getEditor().setPromptText("Lozinka");

        // Prikazivanje dijaloga i preuzimanje unosa lozinke
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(lozinka -> {
            if (!lozinka.isEmpty()) {
                promijeniLozinku(korisnik, lozinka, table);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Upozorenje");
                alert.setHeaderText(null);
                alert.setContentText("Lozinka ne može biti prazna!");
                alert.showAndWait();
            }
        });
    }
}
