import java.util.Scanner;

public class ValidatorApp {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.println("This Java Class validates Latvian Personal ID.");
        System.out.print("Enter ID number: ");
        String rawID = input.nextLine();
        try {
            if (PersonalID.isValid(rawID)) {
                System.out.println("Personal ID Number is valid!");
            }
        } catch (InvalidPersonalIDException e) {
            System.out.println("Personal ID Number is invalid: " + e.getMessage());
        }
    }

}
