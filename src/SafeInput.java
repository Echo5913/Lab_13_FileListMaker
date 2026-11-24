
import java.util.Scanner;

public class SafeInput
{
    static Scanner scanner = new Scanner(System.in);

    public static String getString(String prompt)
    {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    public static int getInt(String prompt, int min, int max)
    {
        int number = 0;
        boolean valid = false;

        while (!valid)
        {
            System.out.print(prompt);

            if (scanner.hasNextInt())
            {
                number = scanner.nextInt();
                scanner.nextLine();

                if (number >= min && number <= max)
                {
                    valid = true;
                }
                else
                {
                    System.out.println(
                            "Enter a number between " + min + " and " + max);
                }
            }
            else
            {
                System.out.println("Please enter a valid whole number.");
                scanner.nextLine();
            }
        }

        return number;
    }

    public static boolean getYNConfirm(String prompt)
    {
        while (true)
        {
            System.out.print(prompt + " (Y/N): ");

            String answer = scanner.nextLine().trim();

            if (answer.equalsIgnoreCase("Y"))
            {
                return true;
            }
            else if (answer.equalsIgnoreCase("N"))
            {
                return false;
            }
            else
            {
                System.out.println("Please enter Y or N.");
            }
        }
    }
}

