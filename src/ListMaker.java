
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ListMaker
{
    static ArrayList<String> myList = new ArrayList<>();
    static Path currentFile = null;
    static boolean needsToBeSaved = false;

    public static void main(String[] args)
    {
        boolean quit = false;

        while (!quit)
        {
            displayMenu();

            String choice = SafeInput.getString("Enter your choice: ").toUpperCase();

            try
            {
                switch (choice)
                {
                    case "A":
                        addItem();
                        break;

                    case "D":
                        deleteItem();
                        break;

                    case "I":
                        insertItem();
                        break;

                    case "M":
                        moveItem();
                        break;

                    case "O":
                        openFile();
                        break;

                    case "S":
                        saveFile();
                        break;

                    case "C":
                        clearList();
                        break;

                    case "V":
                        viewList();
                        break;

                    case "Q":
                        if (!checkUnsavedChanges())
                        {
                            break;
                        }

                        quit = true;
                        System.out.println("Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid choice. Try again.");
                }
            }
            catch (IOException e)
            {
                System.out.println("File error: " + e.getMessage());
            }
        }
    }

    public static void displayMenu()
    {
        System.out.println("\n===== LIST MAKER =====");
        System.out.println("A - Add an item");
        System.out.println("D - Delete an item");
        System.out.println("I - Insert an item");
        System.out.println("M - Move an item");
        System.out.println("O - Open a list file");
        System.out.println("S - Save the list");
        System.out.println("C - Clear the list");
        System.out.println("V - View the list");
        System.out.println("Q - Quit");
        System.out.println("======================");
    }

    public static void addItem()
    {
        String item = SafeInput.getString("Enter the item to add: ");

        myList.add(item);
        needsToBeSaved = true;

        System.out.println("Item added.");
    }

    public static void deleteItem()
    {
        if (myList.isEmpty())
        {
            System.out.println("The list is empty.");
            return;
        }

        viewList();

        int position = SafeInput.getInt(
                "Enter the item number to delete: ", 1, myList.size());

        myList.remove(position - 1);
        needsToBeSaved = true;

        System.out.println("Item deleted.");
    }

    public static void insertItem()
    {
        int position = SafeInput.getInt(
                "Enter the position to insert the item: ",
                1, myList.size() + 1);

        String item = SafeInput.getString("Enter the item: ");

        myList.add(position - 1, item);
        needsToBeSaved = true;

        System.out.println("Item inserted.");
    }

    public static void moveItem()
    {
        if (myList.size() < 2)
        {
            System.out.println("You need at least two items to move.");
            return;
        }

        viewList();

        int oldPosition = SafeInput.getInt(
                "Enter the item number to move: ", 1, myList.size());

        int newPosition = SafeInput.getInt(
                "Enter the new position: ", 1, myList.size());

        String item = myList.remove(oldPosition - 1);

        myList.add(newPosition - 1, item);

        needsToBeSaved = true;

        System.out.println("Item moved.");
    }

    public static void viewList()
    {
        System.out.println("\n===== YOUR LIST =====");

        if (myList.isEmpty())
        {
            System.out.println("Your list is empty.");
        }
        else
        {
            for (int i = 0; i < myList.size(); i++)
            {
                System.out.println((i + 1) + ". " + myList.get(i));
            }
        }

        System.out.println("=====================");
    }

    public static void clearList()
    {
        if (myList.isEmpty())
        {
            System.out.println("The list is already empty.");
            return;
        }

        boolean confirm = SafeInput.getYNConfirm(
                "Are you sure you want to clear the list?");

        if (confirm)
        {
            myList.clear();
            needsToBeSaved = true;

            System.out.println("List cleared.");
        }
    }

    public static void openFile() throws IOException
    {
        if (!checkUnsavedChanges())
        {
            return;
        }

        String fileName = SafeInput.getString(
                "Enter the file name to open (without .txt): ");

        Path file = getFilePath(fileName);

        if (!Files.exists(file))
        {
            System.out.println("File not found.");
            return;
        }

        ArrayList<String> loadedList = new ArrayList<>(
                Files.readAllLines(file, StandardCharsets.UTF_8));

        myList.clear();
        myList.addAll(loadedList);

        currentFile = file;
        needsToBeSaved = false;

        System.out.println("File loaded successfully.");
        viewList();
    }

    public static void saveFile() throws IOException
    {
        if (currentFile == null)
        {
            String fileName = SafeInput.getString(
                    "Enter a name for your file (without .txt): ");

            currentFile = getFilePath(fileName);
        }

        Files.write(
                currentFile,
                myList,
                StandardCharsets.UTF_8
        );

        needsToBeSaved = false;

        System.out.println("List saved successfully as: "
                + currentFile.getFileName());
    }

    public static Path getFilePath(String fileName)
    {
        if (!fileName.toLowerCase().endsWith(".txt"))
        {
            fileName += ".txt";
        }

        return Paths.get(fileName);
    }

    public static boolean checkUnsavedChanges() throws IOException
    {
        if (!needsToBeSaved)
        {
            return true;
        }

        System.out.println("\nYou have unsaved changes.");

        String choice = SafeInput.getString(
                "Save before continuing? (Y = Yes, N = No, C = Cancel): "
        ).toUpperCase();

        if (choice.equals("Y"))
        {
            saveFile();
            return !needsToBeSaved;
        }
        else if (choice.equals("N"))
        {
            return true;
        }
        else
        {
            System.out.println("Operation cancelled.");
            return false;
        }
    }
}
