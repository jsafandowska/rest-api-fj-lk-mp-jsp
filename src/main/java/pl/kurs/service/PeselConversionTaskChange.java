package pl.kurs.service;
import liquibase.change.custom.CustomTaskChange;
import liquibase.database.Database;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.CustomChangeException;
import liquibase.exception.ValidationErrors;
import liquibase.resource.ResourceAccessor;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class PeselConversionTaskChange implements CustomTaskChange {


    @Override
    public void execute(Database database) throws CustomChangeException {
        String selectSQL = "SELECT pesel FROM person";

        Connection connection = null;

        try {
            connection = ((JdbcConnection) database.getConnection()).getWrappedConnection();

            DSLContext dslContext = DSL.using(connection);

            ResultSet rs = dslContext.fetch(selectSQL).intoResultSet();

            while (rs.next()) {
                String pesel = rs.getString("pesel");
                LocalDate birthDate = PeselConverter.convertPeselToBirthDate(pesel);
                String gender = PeselConverter.convertPeselToGender(pesel);

                dslContext.update(DSL.table("person"))
                        .set(DSL.field("birth_date", SQLDataType.LOCALDATE), birthDate)
                        .set(DSL.field("gender", SQLDataType.VARCHAR), gender)
                        .where(DSL.field("pesel", SQLDataType.VARCHAR).eq(pesel))
                        .execute();
            }

        } catch (Exception e) {
            throw new CustomChangeException("Błąd podczas aktualizacji danych: " + e.getMessage(), e);
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
        return null;
    }
}
//    @Override
//    public void execute(Database database) {
//        String selectSQL = "SELECT pesel FROM person";
//        String updateSQL = "UPDATE person SET birth_date = ?, gender = ? WHERE pesel = ?";
//
//        Connection connection = null;
//        PreparedStatement selectStmt = null;
//        PreparedStatement updateStmt = null;
//
//        try {
//            connection = ((JdbcConnection) database.getConnection()).getWrappedConnection();
//            selectStmt = connection.prepareStatement(selectSQL);
//            updateStmt = connection.prepareStatement(updateSQL);
//
//            connection.setAutoCommit(false);
//            ResultSet rs = selectStmt.executeQuery();
//
//            while (rs.next()) {
//                String pesel = rs.getString("pesel");
//                LocalDate birthDate = PeselConverter.convertPeselToBirthDate(pesel);
//                String gender = PeselConverter.convertPeselToGender(pesel);
//                updateStmt.setDate(1, java.sql.Date.valueOf(birthDate));
//                updateStmt.setString(2, gender);
//                updateStmt.setString(3, pesel);
//                updateStmt.addBatch();
//            }
//
//            updateStmt.executeBatch();
//            connection.commit();
//
//        } catch (SQLException e) {
//            try {
//                if (connection != null && !connection.isClosed()) {
//                    connection.rollback();
//                }
//            } catch (SQLException rollbackEx) {
//                throw new RuntimeException("Błąd podczas wycofywania transakcji: " + rollbackEx.getMessage(), rollbackEx);
//            }
//            throw new RuntimeException("Błąd podczas aktualizacji danych: " + e.getMessage(), e);
//        } finally {
//            try {
//                if (updateStmt != null) {
//                    updateStmt.close();
//                }
//                if (selectStmt != null) {
//                    selectStmt.close();
//                }
//                if (connection != null) {
//                    connection.setAutoCommit(true);
//                    connection.close();
//                }
//            } catch (SQLException closeEx) {
//                throw new RuntimeException("Błąd podczas zamykania zasobów: " + closeEx.getMessage(), closeEx);
//            }
//        }
//    }
//
//    @Override
//    public String getConfirmationMessage() {
//        return "Migracja PESEL na datę urodzenia i płeć zakończona pomyślnie.";
//    }
//
//    @Override
//    public void setUp() {
//    }
//
//    @Override
//    public void setFileOpener(ResourceAccessor resourceAccessor) {
//    }
//
//    @Override
//    public ValidationErrors validate(Database database) {
//        return new ValidationErrors();
//    }
//}


//    @Override
//    public void execute(Database database) {
//        String selectSQL = "SELECT pesel FROM person";
//        String updateSQL = "UPDATE person SET birth_date = ?, gender = ? WHERE pesel = ?";
//
//        try (
//                Connection connection = ((JdbcConnection) database.getConnection()).getWrappedConnection();
//                PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
//                PreparedStatement updateStmt = connection.prepareStatement(updateSQL)
//        ) {
//
//            connection.setAutoCommit(false);
//            ResultSet rs = selectStmt.executeQuery();
//
//            while (rs.next()) {
//                String pesel = rs.getString("pesel");
//                LocalDate birthDate = PeselConverter.convertPeselToBirthDate(pesel);
//                String gender = PeselConverter.convertPeselToGender(pesel);
//                updateStmt.setDate(1, java.sql.Date.valueOf(birthDate));
//                updateStmt.setString(2, gender);
//                updateStmt.setString(3, pesel);
//                updateStmt.addBatch();
//            }
//            updateStmt.executeBatch();
//            connection.commit();
//
//        } catch (SQLException e) {
//            try {
//                database.getConnection().rollback();
//            } catch (DatabaseException rollbackEx) {
//                throw new RuntimeException("Błąd podczas wycofywania transakcji: " + rollbackEx.getMessage(), rollbackEx);
//            }
//            throw new RuntimeException("Błąd podczas aktualizacji danych: " + e.getMessage(), e);
//        }
//    }
//
//    @Override
//    public String getConfirmationMessage() {
//        return "Migracja PESEL na datę urodzenia i płeć zakończona pomyślnie.";
//    }
//
//    @Override
//    public void setUp() {
//    }
//
//    @Override
//    public void setFileOpener(ResourceAccessor resourceAccessor) {
//    }
//
//    @Override
//    public ValidationErrors validate(Database database) {
//        return new ValidationErrors();
//    }
//}