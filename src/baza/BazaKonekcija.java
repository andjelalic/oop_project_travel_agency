package baza;

import model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BazaKonekcija {
    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "agencija";
    private static Connection connection;

    public static void DBConnect() throws SQLException /*, ClassNotFoundException*/ {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }

    public static <T> List<T> getObjects(String tableName, RowMapper<T> rowMapper) throws SQLException {
        DBConnect();
        List<T> objects = new ArrayList<>();

        ResultSet resultSet = null;
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM " + tableName;
        resultSet = statement.executeQuery(SQLQuery);

        while (resultSet.next())
            objects.add(rowMapper.mapRow(resultSet));


        statement.close();
        connection.close();

        return objects;
    }

    interface RowMapper<T> {
        T mapRow(ResultSet resultSet) throws SQLException;
    }

    public static List<BankovniRacun> getBankAccounts() throws SQLException {
        return getObjects("bankovni_racun", resultSet ->
                new BankovniRacun(
                        resultSet.getInt(1),
                        resultSet.getString(3),
                        resultSet.getString(2),
                        resultSet.getDouble(4)
                )
        );
    }


    public static List<Klijent> getClients() throws SQLException {
        return getObjects("klijent", resultSet ->
                new Klijent(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(7),
                        resultSet.getString(8),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6)
                )
        );
    }

    public static List<Admin> getAdmins() throws SQLException {
        return getObjects("admin", resultSet ->
                new Admin(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5)
                )
        );
    }

    public static List<Korisnik> getUsers() throws SQLException {
        List<Klijent> klijenti = getClients();
        List<Admin> admins = getAdmins();
        List<Korisnik> korisnici = new ArrayList<>();
        korisnici.addAll(admins);
        korisnici.addAll(klijenti);
        return korisnici;
    }


    public static List<Smjestaj> getAccommodations() throws SQLException {
        return getObjects("smjestaj", resultSet ->
                new Smjestaj(
                        resultSet.getInt(1),
                        Integer.parseInt(resultSet.getString(3)),
                        resultSet.getString(2),
                        resultSet.getString(4),
                        Double.parseDouble(resultSet.getString(5))
                )
        );
    }

    public static List<Aranzman> getArrangements() throws SQLException {
        return getObjects("aranzman", resultSet ->
                new Aranzman(
                        resultSet.getInt(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        LocalDate.parse(resultSet.getString(5)),
                        LocalDate.parse(resultSet.getString(6)),
                        Double.parseDouble(resultSet.getString(7)),
                        getAccommodationByID(resultSet.getInt(8))
                )
        );
    }

    private static Smjestaj getAccommodationByID(int id) throws SQLException {
        return getAccommodations()
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static List<Rezervacija> getReservations() throws SQLException {
        return getObjects("rezervacija", resultSet ->
                new Rezervacija(
                        getClientByID(resultSet.getInt(1)),
                        getArrangementByID(Integer.parseInt(resultSet.getString(2))),
                        TipRezervacije.fromInt(1),
                        Double.parseDouble(resultSet.getString(4)),
                        Double.parseDouble(resultSet.getString(3))
                )
        );
    }

    public static BankovniRacun getAgencyBankAccount() throws SQLException {
        return getBankAccounts()
                .stream()
                .filter(BankovniRacun::isAgencyBankAccount)
                .findFirst()
                .orElse(null);
    }

    private static Klijent getClientByID(int id) throws SQLException {
        return getClients()
                .stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private static Aranzman getArrangementByID(int id) throws SQLException {
        return getArrangements()
                .stream()
                .filter(a -> a.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public static void changePassword(int id, String newPassword, String table) throws SQLException {
        DBConnect();
        String SQLUpdate = "UPDATE " + table + " SET lozinka=? where id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLUpdate);
        preparedStatement.setString(1, newPassword);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void updateBalance(int id, double newBalance) throws SQLException {
        DBConnect();
        String SQLUpdate = "UPDATE bankovni_racun SET stanje=? where id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLUpdate);
        preparedStatement.setDouble(1, newBalance);
        preparedStatement.setInt(2, id);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void updateReservationPaidAmount(int id, int arrID, double price) throws SQLException {
        DBConnect();
        String SQLUpdate = "UPDATE rezervacija SET placena_cijena=? where Klijent_id=? AND Aranzman_id=?";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLUpdate);
        preparedStatement.setString(1, price + "");
        preparedStatement.setInt(2, id);
        preparedStatement.setInt(3, arrID);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void registerClient(int id, String firstName, String lastName, String phoneNumber, String jmbg, String bankAccNumber, String username, String password) throws SQLException {
        DBConnect();
        String SQLQuery = "INSERT INTO klijent (id, ime, prezime, broj_telefona, jmbg, broj_racuna, korisnicko_ime, lozinka) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, phoneNumber);
        preparedStatement.setString(5, jmbg);
        preparedStatement.setString(6, bankAccNumber);
        preparedStatement.setString(7, username);
        preparedStatement.setString(8, password);

        preparedStatement.executeUpdate();
        connection.close();
    }


    public static void addReservation(int clientID, int arrangementID, double totalPrice, double paidAmount) throws SQLException {
        DBConnect();
        String SQLQuery = "INSERT INTO rezervacija (Klijent_id, Aranzman_id, ukupna_cijena, placena_cijena) VALUES (?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, clientID);
        preparedStatement.setString(2, "" + arrangementID);
        preparedStatement.setString(3, "" + totalPrice);
        preparedStatement.setString(4, "" + paidAmount);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void registerAdmin(int id, String firstName, String lastName, String username, String password) throws SQLException {
        DBConnect();
        String SQLQuery = "INSERT INTO admin (id, ime, prezime, korisnicko_ime, lozinka) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, firstName);
        preparedStatement.setString(3, lastName);
        preparedStatement.setString(4, username);
        preparedStatement.setString(5, password);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void addAccommodation(int id, String name, int starReview, String roomType, double pricePerNight) throws SQLException {
        DBConnect();
        String SQLQuery = "INSERT INTO smjestaj (id, naziv, broj_zvjezdica, vrsta_sobe, cjena_po_nocenju) VALUES (?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, starReview + "");
        preparedStatement.setString(4, roomType);
        preparedStatement.setString(5, pricePerNight + "");

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void addArrangement(int id, String name, String destination, String transport, LocalDate tripDate, LocalDate arrivalDate, double price, Integer accommodationID) throws SQLException{
        DBConnect();
        String SQLQuery = "INSERT INTO aranzman (id, naziv_putovanja, destinacija, prevoz, datum_polaska, datum_dolaska, cijena_aranzmana, Smjestaj_id)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.setString(3, destination);
        preparedStatement.setString(4, transport);
        preparedStatement.setDate(5, Date.valueOf(tripDate));
        preparedStatement.setDate(6, Date.valueOf(arrivalDate));
        preparedStatement.setString(7, price + "");
        preparedStatement.setObject(8, accommodationID);

        preparedStatement.executeUpdate();
        connection.close();
    }

    public static void deleteObject(int id, String table, String where) throws SQLException {
        DBConnect();
        String SQLQuery = "DELETE FROM " + table + " WHERE " + where + " = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(SQLQuery);
        preparedStatement.setInt(1, id);

        preparedStatement.executeUpdate();
        connection.close();
    }
}