package ie.atu.sw;

import java.io.File;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Runner
{
    static JFrame f;

    static Scanner input;

    static File inputFile = new File("./1984Orwell.txt");
    static File dictionaryFile = new File("./dictionary.csv");
    static File commonWordsFile = new File("./google-1000.txt");
    static File outputFile = new File("./output.txt");

    public static void main(String[] args) throws Exception
    {
        f = new JFrame();
        f.setVisible(false);

        input = new Scanner(System.in);

        System.out.println("************************************************************");
        System.out.println("*       ATU - Dept. Computer Science & Applied Physics     *");
        System.out.println("*                                                          *");
        System.out.println("*              Virtual Threaded Text Indexer               *");
        System.out.println("*                        G00393941                         *");
        System.out.println("************************************************************");

        while (true)
        {
            // You should put the following code into a menu or Menu class
            System.out.println("");
            System.out.println("1. Execute");
            System.out.println("2. Pick Input File (" + inputFile + ")");
            System.out.println("3. Pick Dictionary (" + dictionaryFile + ")");
            System.out.println("4. Pick Common Words (" + commonWordsFile + ")");
            System.out.println("5. Pick Output File (" + outputFile + ")");
            System.out.println("6. Quit");
            System.out.println("");

            // Output a menu of options and solicit text from the user
            int option = PromptRange("Select Option", 1, 6);

            HandleMenu(option);

        }
    }

    private static void HandleMenu(int option)
    {
        switch (option)
        {
        case 1:
            execute();
            break;

        case 2:
            inputFile = PickFile("Pick Input Text File...", inputFile);
            break;

        case 3:
            dictionaryFile = PickFile("Pick Dictionary File...", dictionaryFile);
            break;

        case 4:
            commonWordsFile = PickFile("Pick Common Words File...", commonWordsFile);
            break;

        case 5:
            outputFile = PickFile("Pick Output File...", outputFile);
            break;

        case 6:
            System.exit(0);
            break;
        }
    }

    private static void execute()
    {
        while (true)
        {

            int option;
            FileProcessor processor = new FileProcessor(inputFile, dictionaryFile, commonWordsFile, outputFile);

            processor.load1000CommonWords();

            processor.execute();

            System.out.println("");
            System.out.println("1. Output all words ascending");
            System.out.println("2. Output all words descending");
            System.out.println("3. Output total number of unique words");
            System.out.println("4. The top n most frequent / infrequent words");
            System.out.println("5. Exit");
            System.out.println("");

            // Output a menu of options and solicit text from the user
            option = PromptRange("Select Option", 1, 5);

            switch (option)
            {
            case 1:
                processor.outputAllWords(true);
                break;

            case 2:
                processor.outputAllWords(false);
                break;

            case 3:
                processor.outputUniqueWordsCount();
                break;

            case 4:
                int freq = PromptRange("Input n frequent", 1);
                processor.outputMostFrequentWords(freq);
                break;

            case 5:
                return;
            }
        }
    }

    private static File PickFile(String prompt, File origonal)
    {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setCurrentDirectory(new File("./"));// set the default director for the file dialog
        fileChooser.setDialogTitle(prompt);
        int result = fileChooser.showOpenDialog(f);

        if (result == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile();
        }
        else
        {
            return origonal;
        }
    }

    static int PromptRange(String prompt, int min, int max)
    {
        int option = 1;
        Boolean condition = true;
        while (condition)
        {
            System.out.print(prompt + " [" + min + "-" + max + "]> ");

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

    static int PromptRange(String prompt, int min)
    {
        int option = 1;
        Boolean condition = true;
        while (condition)
        {
            System.out.print(prompt + " > ");

            try
            {
                option = Integer.parseInt(input.nextLine());
            }
            catch (Exception e)
            {
                option = -1;
            }

            condition = option < min;
        }

        return option;
    }
}
