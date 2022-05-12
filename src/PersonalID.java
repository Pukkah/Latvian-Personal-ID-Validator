import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PersonalID {

    /*
    https://viss.gov.lv/-/media/Files/VRAA/Dokumentacija/Vadlinijas/E_pakalpojumi/Programmetaja_rokasgramatas/VISS2014REKPKMEPAK.ashx
     */
    public static boolean isValid(String id) throws InvalidPersonalIDException {
        // 1. Simbolu virkne neatbilst personas koda formātam,
        // ja tā nav 11 vai 12 simbolus gara, turpmākas pārbaudes neveic;
        if (id.length() < 11 || id.length() > 12) {
            throw new InvalidPersonalIDException("Wrong input length");
        }
        // 2. Simbolu virkne neatbilst personas koda formātam,
        // ja tā satur kaut vienu simbolu, kas nav atļauto sarakstā:
        // „0”; „1”; „2”; „3”; „4”; „5”; „6”; „7”; „8”; „9”; „-”,
        // turpmākas pārbaudes neveic;
        if (!id.matches("^([0-9]{6})(-{0,1})([0-9]{5})$")) {
            throw new InvalidPersonalIDException("Wrong input format");
        }

        // 3. Simbolu virkne atbilst personas koda formātam,
        // ja tā sākas ar kādu no skaitļiem:
        // „32”; „33”; „34”; „35”; „36”; „37”; „38”; „39”,
        // turpmākas pārbaudes neveic;
        int firstTwoDigits = Integer.parseInt(id.substring(0, 2));
        if (firstTwoDigits <= 0 || firstTwoDigits >= 40) {
            throw new InvalidPersonalIDException("Wrong ID sequence");
        }

        // 4. Simbolu virkne neatbilst personas koda formātam,
        // ja tā nesatur korektu informāciju par dzimšanas datumu,
        // turpmākas pārbaudes neveic;
        id = id.replace("-", "");
        if (firstTwoDigits <= 31) {
            String date = id.substring(0, 4) + (1800 + Integer.parseInt(id.substring(4, 6)) + Integer.parseInt(id.substring(6, 7)) * 100);
            DateFormat dateFormatter = new SimpleDateFormat("ddMMyyyy");
            dateFormatter.setLenient(false);
            try {
                dateFormatter.parse(date);
            } catch (ParseException e) {
                throw new InvalidPersonalIDException("Wrong date format");
            }
        }

        // 5. Simbolu virkne neatbilst personas koda formātam,
        // ja tā neatbilst noteiktai kontrolsummai,
        // turpmākas pārbaudes neveic;
        int[] idNums = new int[id.length()];
        for (int i = 0; i < idNums.length; i++) {
            idNums[i] = Integer.parseInt(id.substring(i, i + 1));
        }
        // PK[11] = (1101 – (1*PK[1] + 6*PK[2] + 3*PK[3] + 7*PK[4] + 9*PK[5] + 10*PK[6] + 5*PK[7] + 8*PK[8] + 4*PK[9] + 2*PK[10])) mod 11
        int checkSum = 1101; // Initial checksum salt
        int[] checkNums = {1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        for (int i = 0; i < checkNums.length; i++) {
            checkSum -= checkNums[i] * idNums[i];
        }
        checkSum %= 11;
        if (checkSum != idNums[idNums.length - 1]) {
            throw new InvalidPersonalIDException("Wrong checksum");
        }

        // 6. Simbolu virkne atbilst personas koda formātam.
        return true;
    }

}
