package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import model.*;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class RezervacijeController implements Initializable {
    public RezervacijeController() throws SQLException {
        this.agencija = new Agencija();
    }

    private Agencija agencija;
    @FXML
    private Label status;
    @FXML
    private Label status1;
    private Klijent trenutniKlijent;

    @FXML
    private ListView<Rezervacija> listaRezervacija = new ListView<>();



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        RezervacijeFilter.oznaciRezervacije(agencija.getRezervacije());

    }
    public Klijent setKlijent(Klijent klijent) {
        trenutniKlijent = klijent;
        return trenutniKlijent;
    }

    public static List<Rezervacija> getRezervacije(List<Rezervacija> rezervacije, Klijent klijent) {
        return rezervacije
                .stream()
                .filter(rezervacija -> rezervacija.clientMatch(klijent))
                .toList();
    }
    @FXML
    private void prikazi_aktivne(ActionEvent event) {
        List<Rezervacija> aktivneRezervacije = RezervacijeFilter.filtrirajRezervacije(
                agencija.getRezervacije(), TipRezervacije.AKTIVNA, trenutniKlijent.getId()
        );
        listaRezervacija.getItems().setAll(aktivneRezervacije);
    }
    @FXML
    private void prikazi_istekle(ActionEvent event) {
        List<Rezervacija> istekleRezervacije = RezervacijeFilter.filtrirajRezervacije(
                agencija.getRezervacije(), TipRezervacije.ISTEKLA, trenutniKlijent.getId()
        );
        listaRezervacija.getItems().setAll(istekleRezervacije);
    }
    @FXML
    private void prikazi_otkazane(ActionEvent event){
        listaRezervacija.getItems().setAll(
                RezervacijeFilter.filtrirajRezervacije(
                        agencija.getRezervacije(),  TipRezervacije.OTKAZANA, trenutniKlijent.getId()
                )
        );
    }
    //otkazivanje rezervacije
    public BankovniRacun getBankovniRacunKlijenta(Klijent klijent){
        BankovniRacun bankovniRacun = null;
        List<BankovniRacun> racuni = agencija.getBankovniRacuni();
        for(BankovniRacun b: racuni){
            if(b.getBrojRacuna().equalsIgnoreCase(klijent.getBrojBankovnogRacuna())){
                bankovniRacun = b;
            }
        }
        return bankovniRacun;
    }
    public void otkaziRezervacijuBtn(ActionEvent event) {

        Rezervacija rezervacija =listaRezervacija.getSelectionModel().getSelectedItem();
        BankovniRacun bankovniRacun = getBankovniRacunKlijenta(trenutniKlijent);

        if (rezervacija == null)
            //poruka da ako nije izabrana rezervacija- kreirati label
            status.setText("Izaberite rezervaciju koju želite otkazati!");
        else {
            if (!rezervacija.isCancellationAvailable())
                status.setText("Ne možete otkazati rezervaciju!");
            else {
                try {
                    // otkazujemo rezervaciju i vracamo klijentu novac
                    RezervacijeFilter.klijentOtkazujeRezervaciju(rezervacija, bankovniRacun, agencija.getRacunAgencije());
                    RezervacijeFilter.vratiNovacAgenciji(agencija.getRezervacije(), trenutniKlijent, getBankovniRacunKlijenta(trenutniKlijent),agencija.getRacunAgencije());
                    // poruka o otkazivanju
                    //bankovniRacun.getBalance() i ispisujemo koliko je para sad na racunu klijenta
                    status.setText("Otkazivanje uspješno!\nStanje na računu: " + bankovniRacun.getStanje_racuna());
                } catch (SQLException e) {
                    status.setText("Doslo je do greske u bazi...");
                }
            }
        }
    }
    @FXML
    public void platiRezervacijuBtn(ActionEvent event) {
        status1.setText(" "+ ukupnoPotroseno(trenutniKlijent) + " " +
                ""+ ukupnoZaUplatu(trenutniKlijent.getId()));
        BankovniRacun bankovniRacun = getBankovniRacunKlijenta(trenutniKlijent);
        // Provera da li je selektovana rezervacija iz liste rezervacija, ako nije prikazujemo poruku
        Rezervacija rezervacija = listaRezervacija.getSelectionModel().getSelectedItem();

        if (rezervacija == null) {
            // Ako nije selektovana rezervacija - prikazujemo poruku
            status.setText("Izaberite rezervaciju koju želite uplatiti!");
        } else {
            System.out.println(rezervacija.getPlacenaCijena());
            System.out.println(rezervacija.getUkupnaCijena());
            // da li je rezervacija aktivna- klijent ne moe uplatiti otkazane
            // Ako nije aktivna, prikazujemo poruku
            if (!rezervacija.isActiveReservation()) {
                status.setText("Rezervacija nije aktivna, uplata nije moguća!");
            } else {
                if (rezervacija.isPaidInTotal()) {
                    status.setText("Rezervacija je uplaćena do potpunog iznosa!");
                    // prikazujemo poruku
                } else {
                    status.setText("Za uplatu ove rezervacije ostalo je: " + rezervacija.leftToPay());


                    Dialog<Pair<String, String>> dialog = new Dialog<>();
                    dialog.setTitle("Potvrda rezervacije");
                    dialog.setHeaderText("Unesite vašu lozinku i iznos uplate:");


                    ButtonType potvrdiButtonType = new ButtonType("Potvrdi", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(potvrdiButtonType, ButtonType.CANCEL);

                    // Kreiranje polja za unos
                    PasswordField lozinkaField = new PasswordField();
                    lozinkaField.setPromptText("Lozinka");

                    TextField iznosField = new TextField();
                    iznosField.setPromptText("Iznos uplate");

                    // Postavljanje layouta sa poljima za unos
                    GridPane grid = new GridPane();
                    grid.setHgap(10);
                    grid.setVgap(10);
                    grid.add(new Label("Lozinka:"), 0, 0);
                    grid.add(lozinkaField, 1, 0);
                    grid.add(new Label("Iznos uplate:"), 0, 1);
                    grid.add(iznosField, 1, 1);

                    dialog.getDialogPane().setContent(grid);

                    // Dohvatanje rezultata
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == potvrdiButtonType) {
                            return new Pair<>(lozinkaField.getText(), iznosField.getText());
                        }
                        return null;
                    });


                    Optional<Pair<String, String>> rezultat = dialog.showAndWait();

                    rezultat.ifPresent(unosi -> {
                        String lozinka = unosi.getKey();
                        String iznosString = unosi.getValue();


                        if (!trenutniKlijent.podudaranjeLozinka(lozinka)) {
                            status.setText("Netačna lozinka!");
                            return;
                        }


                        try {
                            double iznos = Double.parseDouble(iznosString);
                            double leftToPay = rezervacija.leftToPay();

                            if (iznos <= 0 || iznos > leftToPay) {
                                status.setText("Unesite validan iznos manji ili jednak preostalom iznosu za uplatu (" + leftToPay + ").");
                                return;
                            }

                            // Logika za uplatu iznosa
                            RezervacijeFilter.platiRezervaciju(rezervacija, bankovniRacun, agencija.getRacunAgencije(), iznos);

                            status.setText("Uspješno uplaćen iznos: " + iznos + ". Preostali iznos za uplatu: " + rezervacija.leftToPay());
                            listaRezervacija.refresh(); // Osvježavanje liste nakon uplate

                        } catch (NumberFormatException e) {
                            status.setText("Unesite validan broj za iznos uplate.");
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        } catch (UnsuccessfulReservationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        }
    }
    public String ukupnoPotroseno(Klijent klijent) {
        return "Potroseno : " + RezervacijeFilter.potrosenNovac(agencija.getRezervacije(), trenutniKlijent);
    }

    public String ukupnoZaUplatu(int id) {
        return "Za uplatu: " + RezervacijeFilter.preostaliIznos(agencija.getRezervacije(), id);
    }

}





/*


 */
//OVAJ DIO JE ZA KLIJENTSKU STARNU REZERVACIJA
