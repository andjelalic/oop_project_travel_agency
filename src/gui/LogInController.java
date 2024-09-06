package gui;
import baza.BazaKonekcija;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import model.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class LogInController implements Initializable{
    public LogInController() throws SQLException {
        this.agencija = new Agencija();
    }

    @FXML
    private Label logInMessageLabel;
    @FXML
    private ImageView brandingImageView;
    @FXML
    private ImageView themeImageView;
    @FXML
    private TextField korisnickoImeLogIn;
    @FXML
    private PasswordField lozinkaLogIn;
    @FXML
    private TextField ime;
    @FXML
    private TextField prezime;
    @FXML
    private TextField brojTelefona;
    @FXML
    private TextField jmbg;
    @FXML
    private TextField bankovniRacun;
    @FXML
    private TextField korisnickoImeR;
    @FXML
    private PasswordField lozinkaRegistracija;
    @FXML
    private PasswordField lozinkaPotvrda;
    @FXML
    private Label registrationMessageLabel;
    private Stage stage;
    private Scene scene;
    private Parent root;

    private Agencija agencija = new Agencija();


    public void switchToRegistration(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("registration.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLogIn(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToAdmin(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToKlijent(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("klijent.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        File themeFile = new File("Images/palmtrees.jpg");
        Image themeImage = new Image(themeFile.toURI().toString());
        themeImageView.setImage(themeImage);

        File brandingFile = new File("Images/logo.jpg");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        brandingImageView.setImage(brandingImage);
    }
    public boolean postojeciRacun(String jmbg, String brojRacuna){
        List <BankovniRacun> bankovniRacuni = agencija.getBankovniRacuni();
        for(BankovniRacun br : bankovniRacuni){
            //jmbg provjera gleda da li je jmbg od agencije (duzine 10)
            if(br.getJmbg().equals(jmbg)&& br.jmbgProvjera(jmbg)
                    && br.getBrojRacuna().equals(brojRacuna)){
                return true;
            }

        }
        return false;
    }
    public static int klijentID(List<Korisnik> korisnici) {
        return (int) korisnici
                .stream()
                .filter(korisnik -> korisnik instanceof Klijent)
                .count() + 1;
    }

    public Korisnik vratiKorisnikaPoImenu(String korisnicko_ime){
        List <Korisnik> korisnici = agencija.getKorisnici();
        for(Korisnik k : korisnici){
            if(k.podudaranjeKorisnickoIme(korisnicko_ime)){
                return k;
            }
        }
        return null;
    }
    public boolean postojeceKorisnickoIme(String korisnickoIme){
        List <Korisnik> korisnici = agencija.getKorisnici();
        for(Korisnik k : korisnici){
            if(k.podudaranjeKorisnickoIme(korisnickoIme)){
                return true;
            }
        }
        return false;
    }
    public boolean validnaPrijava(String korisnicko_ime, String lozinka){
        List <Korisnik> korisnici = agencija.getKorisnici();
        for(Korisnik k : korisnici){
            if(k.podudaranjeLozinka(lozinka) && k.podudaranjeKorisnickoIme(korisnicko_ime)){
                return true;
            }
        }
        return false;
    }

    public void loginButtonOnAction(ActionEvent event) throws SQLException, IOException {
        if (korisnickoImeLogIn.getText().isBlank() || lozinkaLogIn.getText().isBlank()) {
            logInMessageLabel.setText("Nedovoljno podataka za prijavu, popunite sva polja");
        } else if (!validnaPrijava(korisnickoImeLogIn.getText(), lozinkaLogIn.getText())) {
            logInMessageLabel.setText("Unijeli ste nepostojece korisničko ime ili lozinku");
        }
         else if (vratiKorisnikaPoImenu(korisnickoImeLogIn.getText()) instanceof Admin) {
            // Dohvatimo referencu na AdminController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
            Parent root = loader.load();
            AdminController adminController = loader.getController();

            // Postavimo podatke o adminu u AdminController
            Admin admin = (Admin) vratiKorisnikaPoImenu(korisnickoImeLogIn.getText());
            adminController.setAdmin(admin);

            // Promijeni prikaz na AdminPage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("klijent.fxml"));
            Parent root = loader.load();
            ClientController klijentController = loader.getController();


            Klijent klijent = (Klijent) vratiKorisnikaPoImenu(korisnickoImeLogIn.getText());
            klijentController.setKlijent(klijent);

            // Promijeni prikaz na AdminPage
            Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }

    public boolean jeIspravanUnos(){
        if(ime.getText().isBlank() || prezime.getText().isBlank() ||
                jmbg.getText().isBlank() || bankovniRacun.getText().isBlank() ||
                brojTelefona.getText().isBlank() || korisnickoImeR.getText().isBlank() ||
                lozinkaRegistracija.getText().isBlank() || lozinkaPotvrda.getText().isBlank()) {
            return false;
        }
        return true;
    }
    public void registrationOnAction(ActionEvent event) throws SQLException, IOException{
        if( !jeIspravanUnos()){
            registrationMessageLabel.setText("Nedovoljno podataka za registraciju, popunite sva polja");
        }else if(postojeceKorisnickoIme(korisnickoImeR.getText())){
            registrationMessageLabel.setText("Korisničko ime je zauzeto");
        }
        else if(!postojeciRacun(jmbg.getText(), bankovniRacun.getText())){
            registrationMessageLabel.setText("Nepostojeći bankovni račun");
        } else if(!lozinkaRegistracija.getText().equals(lozinkaPotvrda.getText())){
            registrationMessageLabel.setText("Netačna potvrda lozinke");
        }
        else{
            registrationMessageLabel.setText("");
            BazaKonekcija.registerClient(
                    klijentID(agencija.getKorisnici()),
                    ime.getText(),
                    prezime.getText(),
                    brojTelefona.getText(),
                    jmbg.getText(),
                    bankovniRacun.getText(),
                    korisnickoImeR.getText(),
                    lozinkaRegistracija.getText()
            );
            switchToLogIn(event);
        }


        //to do (uslov za provjeru poklapanja lozinke)
        //da li je zauzeto korisnicko ime
        //i onda tek bez poruke
    }
}