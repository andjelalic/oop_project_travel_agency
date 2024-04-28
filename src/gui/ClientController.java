package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
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
    @FXML
    private BorderPane borderPaneClient;
    private Agencija agencija;
    private Klijent trenutniKlijent;

    @FXML
    private Label LblKorisnickoIme;
    @FXML
    private Label LblImePrezime;
    @FXML
    private ComboBox<String> brojZvjezdica = new ComboBox<>();

    @FXML
    private TextField aranzmaniCijenaOd;

    @FXML
    private TextField aranzmaniCijenaDo;

    @FXML
    private DatePicker datumPolaska;

    @FXML
    private DatePicker datumPovratka;

    @FXML
    private ComboBox<String> tipPrevoza = new ComboBox<>();

    @FXML
    private ComboBox<String> vrstaAranzmana = new ComboBox<>();

    @FXML
    private ComboBox<String> vrstaSobe = new ComboBox<>();

    @FXML
    private ListView<String> listaAranzmana;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        vrstaAranzmana.getItems().add(null);
        vrstaAranzmana.getItems().add("Izlet");
        vrstaAranzmana.getItems().add("Putovanje");

        brojZvjezdica.getItems().add(null);
        brojZvjezdica.getItems().add("Tri");
        brojZvjezdica.getItems().add("Cetiri");
        brojZvjezdica.getItems().add("Pet");

        vrstaSobe.getItems().add(null);
        vrstaSobe.getItems().add("Jednokrevetna");
        vrstaSobe.getItems().add("Dvokrevetna");
        vrstaSobe.getItems().add("Trokrevetna");

        tipPrevoza.getItems().add(null);
        tipPrevoza.getItems().add("Avion");
        tipPrevoza.getItems().add("Autobus");
        tipPrevoza.getItems().add("Samostalan");
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
    @FXML
    private void switchToNapraviRezervaciju(ActionEvent event) throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("napravi_rezervaciju.fxml"));
        borderPaneClient.setCenter(view);



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
