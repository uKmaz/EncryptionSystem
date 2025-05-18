import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountCreation {
    static EncoderC enc = new EncoderC();

    static final String DB_FILE = "ADB.txt";
    static final String CODE_FILE = "code";
    static Scanner sc = new Scanner(System.in);
    static StringBuilder code = new StringBuilder();
    static String password;
    static String id;

    public static void main(String[] args) {
        if (code.length() == 0) {
            readPi();
        }
        entrance();
    }

    public static void entrance() {

        System.out.println("Choose an option : ");
        System.out.println("1. Register");
        System.out.println("2. Login");
        int choice = sc.nextInt();
        if(choice == 1) {
            register();
        }
        else if(choice == 2)
        {
            login();
        }
        else
            return;
    }

    public static void register() {

        System.out.print("Enter your ID: ");
        id = sc.next();



        // Check if the ID already exists
        if (!checkDatabaseForId(id)) {
            System.out.print("Enter your Password: ");
            password = sc.next();
            String encodedPassword = encoder(password);
            saveAccount(id, encodedPassword);
            System.out.println("Your ID and encrypted password have been saved. Account successfully created.");
        } else {
            System.out.println("This ID already exists. Please try again with a different one.");
        }

    }
    public static void login() {

        System.out.print("Enter your ID: ");
        String inputId = sc.next();



        // Veritabanında ID kontrolü
        if (checkDatabaseForId(inputId)) {
            System.out.print("Enter your Password: ");
            String inputPassword = sc.next();
            // Veritabanındaki şifreyi al
            String storedEncryptedPassword = getPasswordFromDatabase(inputId);

            // Girilen şifreyi encode et
            String encodedInputPassword = encoder(inputPassword);

            // Karşılaştır
            if (storedEncryptedPassword.equals(encodedInputPassword)) {
                System.out.println("Login successful. Welcome!");
            } else {
                System.out.println("Incorrect password. Try again.");
            }
        } else {
            System.out.println("This ID does not exist. Please register first.");
        }
    }



    public static String encoder(String str){
        return EncoderC.encode(str);
    }
    public static List<PartMatch> smartFinder(String str){
        return EncoderC.smartFind(str);
    }


    public static String reconstructFromParts(List<PartMatch> parts) {
        StringBuilder result = new StringBuilder();
        for (PartMatch part : parts) {
            // Get characters from pi starting at `index` and for `length` characters
            for (int i = 0; i < part.length; i++) {
                result.append(code.charAt(part.index + i));
            }
        }
        return result.toString();
    }



    public static void checkIndex() {
        List<PartMatch> matches = smartFinder(password);
        System.out.println(        code.length());
        if (matches.isEmpty()) {
            System.out.println("No piece is found.");
        }
        else {
            for (PartMatch pm : matches) {
                System.out.println(pm);
            }

            // Reconstruct the data from the parts
            String reconstructed = reconstructFromParts(matches);
            System.out.println("Reconstructed data: " + reconstructed);
        }
    }

    public static void readPi() {
        try (BufferedReader br = new BufferedReader(new FileReader(CODE_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Only get the digits
                line = line.replaceAll("[^0-9]", "");
                code.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkDatabaseForId(String id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length >= 1 && parts[0].equals(id)) {
                    return true; // ID exists
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading database: " + e.getMessage());
        }
        return false; // ID not found
    }

    public static String getPasswordFromDatabase(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("ADB.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2); // ID:password formatında şuan
                if (parts.length == 2 && parts[0].equals(id)) {
                    return parts[1];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveAccount(String id, String encodedPassword) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DB_FILE, true))) {
            writer.write(id + ":" + encodedPassword);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error saving to database: " + e.getMessage());
        }
    }






}
