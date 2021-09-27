import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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
//    Create contacts list from contacts.txt
    public static List<Contact> createContactsObject(){
        List<Contact> contacts = new ArrayList<>();
        try {
            Path pathToOurFile = Paths.get("src/data", "contacts.txt");
            List<String> contactsStringList = Files.readAllLines(pathToOurFile);

            for (String contact : contactsStringList){
                Contact contact1 = new Contact(contact.substring(0, contact.lastIndexOf(" ")),
                        Long.parseLong(contact.substring(contact.lastIndexOf(" ") + 1, contact.length())));
                contacts.add(contact1);
            }
        } catch (IOException ioe){
            ioe.printStackTrace();
        }
        return contacts;
    }
    public static void mainMenu(){
        Path pathToOurFile = Paths.get("src/data", "contacts.txt");
//        Creates contact list by looping through the contact.txt file
        List<Contact> contactsList = createContactsObject();

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
            return;
        };
//        Display all contacts
        if (input == 1){
            System.out.println("");
            System.out.printf("%-19s%s%s\n","Name", "| ", "Phone number");
            System.out.println("--------------------------------");
            for (Contact contact : contactsList){
//                System.out.println(contact.returnContact());
                System.out.printf( "%-19s%s%s\n", contact.getName(),"| ", contact.getNumber() );
            }
//            Adds a contact
        } else if (input == 2){
            System.out.println("Enter the first and last name (FirstName Lastname): ");
            Contact contact = new Contact();
            sc.nextLine();
            contact.setName(sc.nextLine());
            if (contact.getName().contains(" ")){
                System.out.println("Enter the phone number: ");
                try{
                    contact.setNumber(sc.nextLong());
                    contactsList.add(contact);
                    System.out.println("Successfully added contact!");
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Not a number!");
                    mainMenu();
                    return;
                };
                try {
                    Files.writeString(pathToOurFile, "\n" + contact.returnContact(), StandardOpenOption.APPEND);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            } else {
                System.out.println("Not a valid name!");
                mainMenu();
                return;
            }
//            Searches for contact
        } else if (input == 3){
            System.out.println("Enter the name of who you want to search for: ");
            sc.nextLine(); // fixes the scanner bug
            String searchName = sc.nextLine();
            int count = 0;
            for (Contact contact : contactsList){
                if (contact.getName().toLowerCase().contains(searchName.toLowerCase())) {
                    count++;
                    System.out.println("Contact " + count + ": " + contact.returnContact());
                }
            }
            if (count == 0) {
                System.out.println("Name not found.");
            }
//            Delete a contact
        } else if (input == 4) {
            System.out.println("Name  | Phone number");
            System.out.println("--------------------");
            int indexNumber = 0;
            for (Contact contact : contactsList) {
                System.out.println("[" + indexNumber + "]" + contact.returnContact());
                indexNumber++;
            }
            System.out.println("Enter the number of the person you would like to delete: ");
            sc.nextLine(); // fixes the scanner bug
            int selection;
            try {
                selection = sc.nextInt();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Not a valid selection!");
                mainMenu();
                return;
            }
            try {
                System.out.println("Removing " + contactsList.get(selection).getName());
                contactsList.remove(selection);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Invalid selection!");
                mainMenu();
                return;
            }
            try {
                Files.delete(pathToOurFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Files.createFile(pathToOurFile);
            } catch (IOException ioe) {
                System.out.println("There was a problem!");
                ioe.printStackTrace();
            }
            int count3 = 0;
            for (Contact contact : contactsList) {
                if (count3 == 0) {
                    try {
                        Files.writeString(pathToOurFile, contact.returnContact(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Files.writeString(pathToOurFile, "\n" + contact.returnContact(), StandardOpenOption.APPEND);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                count3++;
            }
        } else if (input == 5){
            System.out.println("Exiting...");
            return;
        } else {
            System.out.println("Invalid input.");
            mainMenu();
            return;
        }
        mainMenu();
    }

    public static void main(String[] args) {
        initializeFiles();
        mainMenu();
    }
}
