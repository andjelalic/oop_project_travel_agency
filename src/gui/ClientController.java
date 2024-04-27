package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Admin;
import model.Agencija;
import model.Klijent;
import model.Korisnik;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ClientController() throws SQLException {
        this.agencija = new Agencija();
    }
    private Stage stage;
    private Scene scene;
    private Agencija agencija;
    private Klijent trenutniKlijent;

    @FXML
    private Label LblKorisnickoIme;
    @FXML
    private Label LblImePrezime;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public Klijent setKlijent(Klijent klijent) {
        trenutniKlijent = klijent;
        if (klijent != null) {
            LblKorisnickoIme.setText(trenutniKlijent.getKorisnickoIme());
            LblImePrezime.setText("Zdravo, "+ trenutniKlijent.getIme() +  " " + trenutniKlijent.getPrezime());
        } else {
            LblKorisnickoIme.setText("");
        }
        return trenutniKlijent;
    }
    public void switchToKlijentPocetak(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("klijent.fxml"));
        Parent root = loader.load();

        ClientController klijentController = loader.getController();
        klijentController.setKlijent(trenutniKlijent);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void switchToLogInK(ActionEvent event) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public static int brojKlijenata(List<Korisnik> korisnici) {
        return (int) korisnici
                .stream()
                .filter(korisnik -> korisnik instanceof Klijent)
                .count();
    }
    public static int sljedeciKlijentID(List<Korisnik> korisnici) {
        return brojKlijenata(korisnici) + 1;
    }
}
