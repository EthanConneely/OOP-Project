package ie.atu.sw;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Runner
{
    static JFrame f = new JFrame();

    static Scanner input;

    static File inputFile = new File("./1984Orwell.txt");
    static File dictionaryFile = new File("./dictionary.csv");
    static File commonWordsFile = new File("./google-1000.txt");
    static File outputFile = new File("./output.txt");

    public static void main(String[] args) throws Exception
    {
        input = new Scanner(System.in);

        while (true)
        {
            // You should put the following code into a menu or Menu class
            System.out.println("");
            System.out.println("************************************************************");
            System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
            System.out.println("*                                                          *");
            System.out.println("*              Virtual Threaded Text Indexer               *");
            System.out.println("*                        G00393941                         *");
            System.out.println("************************************************************");
            System.out.println("(1) Specify Text File (" + inputFile + ")");
            System.out.println("(2) Configure Dictionary (" + dictionaryFile + ")");
            System.out.println("(3) Configure Common Words (" + commonWordsFile + ")");
            System.out.println("(4) Specify Output File (" + outputFile + ")");
            System.out.println("(5) Execute");
            System.out.println("(6) Quit");
            System.out.println("");

            // Output a menu of options and solicit text from the user
            int option = PromptRange("Select Option [1-6]> ", 1, 6);

            System.out.println("");

            HandleMenu(option);
        }
    }

    static int PromptRange(String prompt, int min, int max)
    {
        int option = 1;
        Boolean condition = true;
        while (condition)
        {
            System.out.print(prompt);

            try
            {
                option = Integer.parseInt(input.nextLine());
            }
            catch (Exception e)
            {
                option = -1;
            }

            condition = option < min || option > max;
        }

        return option;
    }

    private static void HandleMenu(int option)
    {
        switch (option)
        {
        case 1:
            inputFile = PickFile("Pick Input File...");
            break;

        case 2:
            dictionaryFile = PickFile("Pick Dictionary File...");
            break;

        case 3:
            commonWordsFile = PickFile("Pick Common Words File...");
            break;

        case 4:
            outputFile = PickFile("Pick Output File...");
            break;

        case 5:
            var processor = new FileProcessor(inputFile, dictionaryFile, commonWordsFile, outputFile);
            try
            {
                processor.execute();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            break;

        case 6:
            System.exit(0);
            break;
        }
    }

    private static File PickFile(String prompt)
    {
        f.setTitle(prompt);
        f.setVisible(true);
        f.setAlwaysOnTop(true);

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setDialogTitle(prompt);
        int result = fileChooser.showOpenDialog(f);

        f.setVisible(false);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }

        return null;
    }
}
