package gui;

import baza.BazaKonekcija;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.Admin;
import model.Agencija;
import model.Korisnik;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label imePrezimeLbl;
    @FXML
    private Label LblKorisnickoIme;
    private Agencija agencija;
    private Admin admin;

    private Label listaRezervacijaLbl;

    public Admin setAdmin(Admin admin) {
        Admin trenutniAdmin = admin;
        if (admin != null) {
            LblKorisnickoIme.setText(trenutniAdmin.getKorisnickoIme());
            imePrezimeLbl.setText("Zdravo, "+ trenutniAdmin.getIme() +  " " + trenutniAdmin.getPrezime());
        } else {
            LblKorisnickoIme.setText("");
        }
        return trenutniAdmin;
    }

    public static int NaredniAdminID(List< Korisnik> korisnici) {
        return brojAdmina(korisnici) + 1;
    }
    public static int brojAdmina(List<Korisnik> korisnici) {
        return (int) korisnici
                .stream()
                .filter(korisnik -> korisnik instanceof Admin)
                .count();
    }
    public static void dodajNovogAdmina(List<Korisnik> korisnici, Admin admin) throws SQLException {
        korisnici.add(admin);
        BazaKonekcija.registerAdmin(
                admin.getId(),
                admin.getIme(),
                admin.getPrezime(),
                admin.getKorisnickoIme(),
                admin.getLozinka()
        );
    }


    public void switchToLogIn(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
   private void switchToDodajAdmina(ActionEvent event) throws IOException{
       AnchorPane view = FXMLLoader.load(getClass().getResource("admin_dodaj.fxml"));
       borderPane.setCenter(view);
   }
   @FXML
    private void switchToRezervacije(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("rezervacije.fxml"));
        borderPane.setCenter(view);
    }
    @FXML
    private void switchToDodajIzlet(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("dodaj_izlet.fxml"));
        borderPane.setCenter(view);
    }


    public void switchToAdminPocetak(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        setAdmin(admin);
    }

    @FXML
    private void switchToDodajPutovanje(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("dodaj_putovanje.fxml"));
        borderPane.setCenter(view);
    }

}
