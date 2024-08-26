package pl.kurs.service;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PeselConversionTaskChange implements CustomTaskChange {

    @Override
    public void execute(Database database) {
        String selectSQL = "SELECT pesel FROM person";
        String updateSQL = "UPDATE person SET birth_date = ?, gender = ? WHERE pesel = ?";

        try (
                Connection connection = ((JdbcConnection) database.getConnection()).getWrappedConnection();
                PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
                PreparedStatement updateStmt = connection.prepareStatement(updateSQL)
        ) {
            ResultSet rs = selectStmt.executeQuery();

            while (rs.next()) {
                String pesel = rs.getString("pesel");

                LocalDate birthDate = PeselConverter.convertPeselToBirthDate(pesel);
                String gender = PeselConverter.convertPeselToGender(pesel);

                updateStmt.setDate(1, java.sql.Date.valueOf(birthDate)); // Konwersja LocalDate na java.sql.Date
                updateStmt.setString(2, gender);
                updateStmt.setString(3, pesel);
                updateStmt.addBatch();
            }

            updateStmt.executeBatch();

        } catch (SQLException e) {
            throw new RuntimeException("Błąd podczas aktualizacji danych: " + e.getMessage(), e);
        }
    }

    @Override
    public String getConfirmationMessage() {
        return "Migracja PESEL na datę urodzenia i płeć zakończona pomyślnie.";
    }

    @Override
    public void setUp() {
    }

    @Override
    public void setFileOpener(ResourceAccessor resourceAccessor) {
    }

    @Override
    public ValidationErrors validate(Database database) {
        return new ValidationErrors();
    }
}