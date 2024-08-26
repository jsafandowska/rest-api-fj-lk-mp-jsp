package pl.kurs.service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PeselConverter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static LocalDate convertPeselToBirthDate(String pesel) {
        if (pesel == null || pesel.length() != 11) {
            throw new IllegalArgumentException("Invalid PESEL number");
        }

        int year = Integer.parseInt(pesel.substring(0, 2));
        int month = Integer.parseInt(pesel.substring(2, 4));
        int day = Integer.parseInt(pesel.substring(4, 6));

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

        return LocalDate.of(year, month, day);
    }

    public static String convertPeselToGender(String pesel) {
        if (pesel == null || pesel.length() != 11) {
            throw new IllegalArgumentException("Invalid PESEL number");
        }

        int genderDigit = Character.getNumericValue(pesel.charAt(9));
        return (genderDigit % 2 == 0) ? "female" : "male";
    }

    public static void main(String[] args) {
        String pesel = "44051401359";
        LocalDate birthDate = convertPeselToBirthDate(pesel);
        String gender = convertPeselToGender(pesel);

        System.out.println("Birth Date: " + birthDate.format(DATE_FORMATTER));
        System.out.println("Gender: " + gender);
    }
}