package pl.kurs.inheritance.service;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.DatabaseException;
import liquibase.exception.SetupException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.DateTimeException;
import java.time.LocalDate;


public class PeselToBirthdateAndGenderChange implements CustomTaskChange {
    @Override
    public void execute(Database database) throws CustomChangeException {
        try {
            JdbcConnection jdbcConnection = (JdbcConnection) database.getConnection();
            String selectSql = "SELECT id, pesel FROM person WHERE date_of_birth IS NULL";
            try (PreparedStatement selectStatement = jdbcConnection.prepareStatement(selectSql)) {
                ResultSet resultSet = selectStatement.executeQuery();
                while (resultSet.next()) {
                    int personId = resultSet.getInt("id");
                    String pesel = resultSet.getString("pesel");
                    Date birthdate = parsePeselToBirthdate(pesel);
                    String gender = extractGenderFromPesel(pesel);

                    String updateSql = "UPDATE person SET date_of_birth = ?, gender = ? WHERE id = ?";
                    try (PreparedStatement updateStatement = jdbcConnection.prepareStatement(updateSql)) {
                        updateStatement.setDate(1, java.sql.Date.valueOf(String.valueOf(birthdate)));
                        updateStatement.setString(2, gender);
                        updateStatement.setInt(3, personId);
                        updateStatement.executeUpdate();
                    }
                }
            } catch (DatabaseException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new CustomChangeException("Błąd podczas przetwarzania numerów PESEL.", e);
        }
    }

    private String extractGenderFromPesel(String pesel) {
        int genderDigit = Character.getNumericValue(pesel.charAt(9));
        return (genderDigit % 2 == 0) ? "Female" : "Male";
    }

    private void validatePesel(String pesel) {
        if (pesel == null || pesel.length() != 11) {
            throw new IllegalArgumentException("Invalid PESEL length: " + pesel);
        }

        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        // Miesiąc musi być w odpowiednich zakresach dla różnych stuleci
        if (!((month >= 1 && month <= 12) || (month >= 21 && month <= 32) || (month >= 41 && month <= 52) || (month >= 61 && month <= 72) || (month >= 81 && month <= 92))) {
            throw new IllegalArgumentException("Invalid month in PESEL: " + pesel);
        }

        // Dzień musi być w zakresie 1-31
        if (day < 1 || day > 31) {
            throw new IllegalArgumentException("Invalid day in PESEL: " + pesel);
        }
    }

    private Date parsePeselToBirthdate(String pesel) {
        validatePesel(pesel);

        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

        // Kodowanie stuleci w PESEL-u
        if (month > 80) {
            year += 1800;
            month -= 80;
        } else if (month > 60) {
            year += 2200;
            month -= 60;
        } else if (month > 40) {
            year += 2100;
            month -= 40;
        } else if (month > 20) {
            year += 2000;
            month -= 20;
        } else {
            year += 1900;
        }

        try {
            LocalDate birthDate = LocalDate.of(year, month, day);
            return Date.valueOf(birthDate);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid date in PESEL: " + pesel, e);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return null;
    }

    @Override
    public void setUp() throws SetupException {

    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {

    }

    @Override
    public ValidationErrors validate(Database database) {
        return null;
    }
}