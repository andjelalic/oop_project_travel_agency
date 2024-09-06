package gui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class NapraviRezervacijuController implements Initializable {
    public NapraviRezervacijuController() throws SQLException {
        this.agencija = new Agencija();
    }
    private Agencija agencija;
    private Klijent trenutniKlijent;
    @FXML
    private ComboBox<String> brojZvjezdica = new ComboBox<>();


    @FXML
    private TextField aranzmaniCijenaDo;

    @FXML
    private TextField aranzmaniDestinacija;

    @FXML
    private ComboBox<String> tipPrevoza = new ComboBox<>();

    @FXML
    private ComboBox<String> vrstaAranzmana = new ComboBox<>();

    @FXML
    private ComboBox<String> vrstaSobe = new ComboBox<>();
    @FXML
    private DatePicker datumPolaska;
    @FXML
    private DatePicker datumPovratka;
    @FXML
    private ListView<Aranzman> listaAranzmana = new ListView<>();
    @FXML
    private RadioButton rb1;
    @FXML
    private RadioButton rb2;
    @FXML
    private Label lblPorukaGreska;




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        listaAranzmana.getItems().addAll(AranzmaniFilter.aranzmaniUPonudi(agencija.getAranzmani()));

        vrstaAranzmana.getItems().add(null);
        vrstaAranzmana.getItems().add("Izlet");
        vrstaAranzmana.getItems().add("Putovanje");

        brojZvjezdica.getItems().add(null);
        brojZvjezdica.getItems().add("3");
        brojZvjezdica.getItems().add("4");
        brojZvjezdica.getItems().add("5");

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
        return trenutniKlijent;
    }   @FXML
    private void prikaziRezervacije(ActionEvent event) throws IOException, SQLException{
        rb1.setOnAction(event1 -> {
            if (rb1.isSelected()) listaAranzmana.getItems().sort(Aranzman.porediCijenu);
        });
        rb2.setOnAction(event1 -> {
            if (rb2.isSelected()) listaAranzmana.getItems().sort(Aranzman.porediPremaDatumuPolaska);
        });
        listaAranzmana.getItems().setAll(AranzmaniFilter.filtrirajAranzmane(
                        agencija.getAranzmani(),
                        aranzmaniCijenaDo.getText(),
                        aranzmaniDestinacija.getText(),
                        brojZvjezdica.getValue(),
                        vrstaSobe.getValue(),
                        tipPrevoza.getValue(),
                        datumPolaska.getValue(),
                        datumPovratka.getValue()
                )
        );

    }
    private boolean vecRezervisan(List<Rezervacija> rezervacije, Klijent klijent, Aranzman aranzman){
        for (Rezervacija rezervacija : rezervacije)
            if (rezervacija.isAlreadyReserved(klijent, aranzman)) {
                lblPorukaGreska.setText("Aranžman je već rezervisan!");
                return true;
            }
        return false;
    }

    @FXML
    private void rezervisi(ActionEvent event) throws SQLException, UnsuccessfulReservationException {
        Aranzman aranzman = listaAranzmana.getSelectionModel().getSelectedItem();
        if (aranzman == null) {
            lblPorukaGreska.setText("Odaberite aranžman koji želite rezervisati!");
        } else if(vecRezervisan(agencija.getRezervacije(), trenutniKlijent, aranzman)) {

            vecRezervisan(agencija.getRezervacije(), trenutniKlijent, aranzman);
        }else{
            lblPorukaGreska.setText("");
            // Pop-up za unos lozinke
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Potvrda rezervacije");
            dialog.setHeaderText(null);
            dialog.setContentText("Unesite vašu lozinku:");

            // Prikaz dijaloga i obrada unosa
            dialog.showAndWait().ifPresent(lozinka -> {
                // Provjera dužine lozinke
                if (!trenutniKlijent.podudaranjeLozinka(lozinka)) {
                    lblPorukaGreska.setText("Netacna lozinka!");
                    return; // Prekida izvršenje ako lozinka nije ispravna
                }

                // Ovdje dodajte logiku za provjeru ispravnosti unesene lozinke
                // Na primjer, ako je lozinka ispravna, nastavite s rezervacijom
                try {
                    // Provjera dovoljnosti novca
                    MenadzerTransakcija.dovoljnoNovca(
                            MenadzerTransakcija.getBankovniRacun(agencija.getBankovniRacuni(), trenutniKlijent.getBrojBankovnogRacuna()),
                            aranzman.iznosUplate()
                    );
                    RezervacijeFilter.dodajRezervacije(
                            agencija.getRezervacije(),
                            new Rezervacija(
                                    trenutniKlijent,
                                    aranzman,
                                    TipRezervacije.AKTIVNA,
                                    aranzman.iznosUplate(),
                                    aranzman.punaCijena()
                            )
                    );

                    // Izvršenje transakcije
                    MenadzerTransakcija.izvrsiTransakciju(
                            MenadzerTransakcija.getBankovniRacun(agencija.getBankovniRacuni(), trenutniKlijent.getBrojBankovnogRacuna()),
                            agencija.getRacunAgencije(),
                            aranzman.iznosUplate(),
                            false
                    );


                    lblPorukaGreska.setText("Uspješno ste rezervisali aranžman!");
                } catch (UnsuccessfulReservationException e) {
                    lblPorukaGreska.setText("Nemate dovoljno novca za rezervaciju!");
                    // Ovdje možete dodati dodatnu logiku ako transakcija nije uspješna
                } catch (SQLException ex) {
                    lblPorukaGreska.setText("Došlo je do greške prilikom obrade transakcije.");
                    ex.printStackTrace(); // Ispis greške u konzolu za dijagnostiku
                }
            });
        }
    }









}
