import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ContactsApp {
    public static void initializeFiles(){
//        Check directory. If it doesnt exist, create it.
        Path pathToOurDataDir = Paths.get("src/data");
        try {
           if (Files.notExists(pathToOurDataDir)){
               System.out.println("Creating directory...");
               Files.createDirectories(pathToOurDataDir);
               System.out.println("Directory created!");
           } else {
               System.out.println("The " + pathToOurDataDir + " directory already exists!");
           }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Path pathToOurFile = Paths.get("src/data", "contacts.txt");
        try {
            System.out.println("Checking file system...");
            if (Files.notExists(pathToOurFile)){
                Files.createFile(pathToOurFile);
                System.out.println("Your file is created!");
            } else {
                System.out.println("The " + pathToOurFile + " file already exists!");
            }
        } catch (IOException ioe) {
            System.out.println("There was a problem!");
            ioe.printStackTrace();
        }
    }
    public static void mainMenu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("1. View contacts.");
        System.out.println("2. Add a new contact.");
        System.out.println("3. Search a contact by name.");
        System.out.println("4. Delete an existing contact.");
        System.out.println("5. Exit.");
        System.out.println("Enter an option (1, 2, 3, 4 or 5):");
        int input = 0;
        try{
            input = sc.nextInt();
        }catch (Exception e){
            e.printStackTrace();
            mainMenu();
        };
        if (input == 1){
            Path pathToOurFile = Paths.get("src/data", "contacts.txt");
            List<String> contactsInTheFile = new ArrayList<>();
            try {
                contactsInTheFile = Files.readAllLines(pathToOurFile);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            for (String contact : contactsInTheFile){
                System.out.println(contact);
            }
        } else if (input == 2){

        } else if (input == 3){

        } else if (input == 4){

        } else if (input == 5){

        } else {
            System.out.println("Invalid input.");
            mainMenu();
        }
    }
    public static void main(String[] args) {
        initializeFiles();
        mainMenu();
    }
}
