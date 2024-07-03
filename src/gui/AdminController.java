package gui;

import baza.BazaKonekcija;
import baza.Identifiable;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.*;
import javafx.fxml.Initializable;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AdminController{
    public AdminController() throws SQLException {
        this.agencija = new Agencija();
    }
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private BorderPane borderPane;
    @FXML
    private Label imePrezimeLbl;
    @FXML
    private Label LblKorisnickoIme;
    @FXML
    private Label brojAdminaLbl;
    @FXML
    private Label dodajAdminaErrorLbl;
    @FXML
    private Label dodajAdminaStatusLbl;

    @FXML
    private TextField imeTf;
    @FXML
    private TextField prezimeTf;
    @FXML
    private TextField korisnickoImeTf;
    @FXML
    private Label rezervacijeLbl;
    @FXML
    private TextField nazivPutovanjaTf;
    @FXML
    private TextField destinacijaPutovanjaTf;

    @FXML
    private DatePicker datumPolaskaPutovanjaTf;
    @FXML
    private DatePicker datumPovratkaPutovanjaTf;
    @FXML
    private TextField cijenaPutovanjaTf;
    @FXML
    private TextField nazivSmjestajaTf;
    @FXML
    private TextField cijenaPoNocenjuTf;

    @FXML
    private TextField brojZvjezdicaTf;
    @FXML
    private Label dodajPutovanjeStatusLbl;
    @FXML
    private Label dodajPutovanjeErrorLbl;
    @FXML
    private ComboBox vrstaSobeTf;
    @FXML
    private ComboBox tipPrevozaTf;
    @FXML
    private Button dodajPutovanje;
    @FXML
    private TextField nazivIzletaTf;
    @FXML
    private TextField destinacijaIzletaTf;
    @FXML
    private DatePicker datumPolaskaIzletaTf;
    @FXML
    private TextField cijenaIzletaTf;
    @FXML
    private Label dodajIzletStatusLbl;
    @FXML
    private Label dodajIzletErrorLbl;




    private Agencija agencija;
    private Admin trenutniAdmin;


    public Admin setAdmin(Admin admin) {
        trenutniAdmin = admin;
        if (admin != null) {
            LblKorisnickoIme.setText(trenutniAdmin.getKorisnickoIme());
            imePrezimeLbl.setText("Zdravo, "+ trenutniAdmin.getIme() +  " " + trenutniAdmin.getPrezime());
            brojAdminaLbl.setText("Broj admina: " + brojAdmina(agencija.getKorisnici()));
        } else {
            LblKorisnickoIme.setText("");
        }
        return trenutniAdmin;
    }

    private static <T extends Identifiable> int pronadjiID(List<T> list) {
        // Sortira listu po ID-u
        list.sort((a, b) -> Integer.compare(a.getId(), b.getId()));

        // Ako je lista prazna ili prvi ID nije 1, vraća se ID 1
        if (list.isEmpty() || list.get(0).getId() != 1)
            return 1;

        // Pretražuje listu kako bi pronašao prazan ID između postojećih ID-eva
        for (int i = 0; i < list.size() - 1; i++)
            if (list.get(i + 1).getId() - list.get(i).getId() > 1)
                return list.get(i).getId() + 1;

        // Ako nema praznih ID-eva, vraća se sljedeći ID nakon posljednjeg u listi
        return list.size() + 1;
    }

    public static int brojAdmina(List<Korisnik> korisnici) {
        return (int) korisnici
                .stream()
                .filter(korisnik -> korisnik instanceof Admin)
                .count();
    }
    public static int AdminID(List<Korisnik> korisnici) {
        return brojAdmina(korisnici) + 1;
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
    private void switchToRezervacije(ActionEvent event) throws IOException, SQLException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("rezervacije.fxml"));
        borderPane.setCenter(view);

        Label rezervacijeLbl = (Label) view.lookup("#rezervacijeLbl");

        if (agencija.getRezervacije().isEmpty()) {
            rezervacijeLbl.setText("Nema rezervacija");
        } else {
            rezervacijeLbl.setText(agencija.getRezervacije().toString());
        }
    }

    public void naDugme(ActionEvent event) throws SQLException, IOException{
        if(agencija.getRezervacije().isEmpty()){
            rezervacijeLbl.setText("Lista prazna");
        }
        rezervacijeLbl.setText(agencija.getRezervacije().toString());
    }

    @FXML
    private void switchToDodajIzlet(ActionEvent event) throws IOException{
        AnchorPane view = FXMLLoader.load(getClass().getResource("dodaj_izlet.fxml"));
        borderPane.setCenter(view);
    }


    public void switchToAdminPocetak(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin.fxml"));
        Parent root = loader.load();

        AdminController adminController = loader.getController();
        adminController.setAdmin(trenutniAdmin);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void switchToDodajPutovanje(ActionEvent event) throws IOException{

        AnchorPane view = FXMLLoader.load(getClass().getResource("dodaj_putovanje.fxml"));
        borderPane.setCenter(view);
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

    public void dodajAdminaBtnOnAction(ActionEvent event) throws SQLException, IOException{
        if(imeTf.getText().isBlank() || prezimeTf.getText().isBlank() || korisnickoImeTf.getText().isBlank()){
            dodajAdminaErrorLbl.setText("Nedovoljno podataka za prijavu, popunite sva polja");
        }else if(postojeceKorisnickoIme(korisnickoImeTf.getText())){
            dodajAdminaErrorLbl.setText("Korisničko ime je zauzeto");
        }else{
            dodajAdminaErrorLbl.setText("");
            BazaKonekcija.registerAdmin(
                    AdminID(agencija.getKorisnici()),
                    imeTf.getText(),
                    prezimeTf.getText(),
                    korisnickoImeTf.getText(),
                    "12345678");
            dodajAdminaStatusLbl.setText("Admin je uspješno dodat!");
        }
    }
    public boolean jePotpunUnosIzleta(){
        if(nazivIzletaTf.getText().isBlank() || destinacijaIzletaTf.getText().isBlank() ||
                datumPolaskaIzletaTf.getValue() == null || cijenaIzletaTf.getText().isBlank()) {
            return false;
        }
        return true;
    }
    public boolean jePotpunUnosPutovanja(){
        if(nazivPutovanjaTf.getText().isBlank() || destinacijaPutovanjaTf.getText().isBlank() ||
                nazivSmjestajaTf.getText().isBlank() || datumPolaskaPutovanjaTf.getValue() == null ||
                vrstaSobeTf.getValue() == null || tipPrevozaTf.getValue() == null ||
                datumPovratkaPutovanjaTf.getValue() == null || brojZvjezdicaTf.getText().isBlank() ||
                cijenaPutovanjaTf.getText().isBlank() || cijenaPoNocenjuTf.getText().isBlank()) {
            return false;
        }
        return true;
    }

    public void dodajPutovanjeOnAction(ActionEvent event) throws SQLException, IOException{
        if(!jePotpunUnosPutovanja()) {
            dodajPutovanjeErrorLbl.setText("Nedovoljno podataka, popunite sva polja");

        } else if (!UpravljanjeAranzman.validanDatum(datumPolaskaPutovanjaTf.getValue())
                || !UpravljanjeAranzman.validanDatum(datumPovratkaPutovanjaTf.getValue())){
            dodajPutovanjeErrorLbl.setText("Unijeli ste neispravan format datuma (zahtijeva se yyyy-mm-dd)");
        }

        else if (UpravljanjeAranzman.ispravanUnosDatuma(datumPolaskaPutovanjaTf.getValue(), datumPovratkaPutovanjaTf.getValue())){
            dodajPutovanjeErrorLbl.setText("Neispravan unos datuma!");
        }
        else{
            dodajPutovanjeErrorLbl.setText("");
            Smjestaj smjestaj = new Smjestaj(
                    pronadjiID(agencija.getSmestaji()),
                    Integer.parseInt(brojZvjezdicaTf.getText()),
                    nazivSmjestajaTf.getText(),
                    (String) vrstaSobeTf.getValue(),
                    Double.parseDouble(cijenaPoNocenjuTf.getText())
            );
            UpravljanjeAranzman.dodajSmjestaj(agencija.getSmestaji(), smjestaj);
            UpravljanjeAranzman.dodajAranzman(agencija.getAranzmani(),
                    new Aranzman(
                            pronadjiID(agencija.getAranzmani()),
                            nazivPutovanjaTf.getText(),
                            destinacijaPutovanjaTf.getText(),
                            (String) tipPrevozaTf.getValue(),
                            datumPolaskaPutovanjaTf.getValue(),
                            datumPovratkaPutovanjaTf.getValue(),
                            Double.parseDouble(cijenaPutovanjaTf.getText()),
                            smjestaj
                    )
            );
            dodajPutovanjeStatusLbl.setText("Putovanje je uspješno dodano");
        }
    }
    public void dodajIzletOnAction(ActionEvent event) throws SQLException, IOException {
        if(!jePotpunUnosIzleta()) {
            dodajIzletErrorLbl.setText("Nedovoljno podataka, popunite sva polja");
        } else if (!UpravljanjeAranzman.validanDatum(datumPolaskaIzletaTf.getValue())) {
            dodajIzletErrorLbl.setText("Unijeli ste neispravan format datuma (zahtijeva se yyyy-mm-dd)");
        }
        else {
            dodajIzletErrorLbl.setText("");
            UpravljanjeAranzman.dodajAranzman(agencija.getAranzmani(),
                    new Aranzman(
                            pronadjiID(agencija.getAranzmani()),
                            nazivIzletaTf.getText(),
                            destinacijaIzletaTf.getText(),
                            "Autobus",
                            datumPolaskaIzletaTf.getValue(),
                            datumPolaskaIzletaTf.getValue(),
                            Double.parseDouble(cijenaIzletaTf.getText()),
                            null
                    ));
            dodajIzletStatusLbl.setText("Izlet je uspješno dodan");
        }
    }
}
