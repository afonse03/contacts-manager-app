import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
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
            List<String> contactsInTheFile = new ArrayList<>();
            try {
                contactsInTheFile = Files.readAllLines(pathToOurFile);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            for (String contact : contactsInTheFile){
                System.out.println(contact);
            }
//            Adds a contact
        } else if (input == 2){
            System.out.println("Enter the first and last name (FirstName Lastname): ");
            String fullName;
            sc.nextLine();
            fullName = sc.nextLine();
            if (fullName.contains(" ")){
                System.out.println("Enter the phone number: ");
                long number;
                try{
                    number = sc.nextLong();
                    contactsList.add(new Contact(fullName, number));
                    System.out.println("Successfully added contact!");
                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("Not a number!");
                    mainMenu();
                    return;
                };
                try {
                    Files.writeString(pathToOurFile, "\n" + fullName + " " + number, StandardOpenOption.APPEND);
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
            List<String> contactsInTheFile = new ArrayList<>();
            try {
                contactsInTheFile = Files.readAllLines(pathToOurFile);
            } catch (IOException ioe){
                ioe.printStackTrace();
            }
            int count = 1;
            boolean nameFound = false;
            for (String contact : contactsInTheFile){
                if (contact.contains(searchName)) {
                    System.out.println("Contact " + count + ": " + contact);
                    count++;
                    nameFound = true;
                }
            }
            if (!nameFound) {
                System.out.println("Name not found.");
            }
        } else if (input == 4){
            System.out.println("Enter the name of the person who you want to delete: ");
            sc.nextLine(); // fixes the scanner bug
            String searchName = sc.nextLine();
            int lineIndex = 0;
            HashMap<Integer, String>searchContactIndex = new HashMap<>();
            boolean nameFound = false;
            for (Contact contact : contactsList){
                if (contact.getName().contains(searchName)) {
                    nameFound = true;
                    searchContactIndex.put(lineIndex, contact.getName() + " " + contact.getNumber());
                }
                lineIndex++;
            }
            if (!nameFound) {
                System.out.println("Name not found.");
            } else {
                int count2 = 1;
                for (int index : searchContactIndex.keySet()){
                    System.out.println("[" + index + "] " + searchContactIndex.get(index));
                }
                System.out.println("Who do you want to delete?");
                int selection = sc.nextInt();
                if (searchContactIndex.containsKey(selection)){
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
                    System.out.println("Removing " + searchContactIndex.get(selection));
                    contactsList.remove(selection);
                    int count3 = 0;
                    for (Contact contact : contactsList) {
                        if (count3 == 0){
                            try {
                                Files.writeString(pathToOurFile, contact.getName() + " " + contact.getNumber(), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                Files.writeString(pathToOurFile, "\n" + contact.getName() + " " + contact.getNumber(), StandardOpenOption.APPEND);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        count3 ++;
                    }
                } else {
                    System.out.println("Name not found!");
                }
            }
        } else if (input == 5){
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
