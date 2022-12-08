package ie.atu.sw;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class FileProcessor
{
    File inputFile;
    File dictionaryFile;
    File commonWordsFile;
    File outputFile;

    public FileProcessor(File inputFile, File dictionaryFile, File commonWordsFile, File outputFile)
    {
        this.inputFile = inputFile;
        this.dictionaryFile = dictionaryFile;
        this.commonWordsFile = commonWordsFile;
        this.outputFile = outputFile;
    }

    public void execute() throws InterruptedException
    {
        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor())
        {
            try (Scanner fileScanner = new Scanner(inputFile))
            {
                extractLine(pool, fileScanner);
            }
            catch (FileNotFoundException e)
            {
                e.printStackTrace();
            }

            pool.shutdown();
            pool.awaitTermination(5, TimeUnit.SECONDS);
            pool.shutdownNow();
        }
    }

    private void extractLine(ExecutorService pool, Scanner fileScanner)
    {
        while (fileScanner.hasNextLine())
        {
            String line = fileScanner.nextLine();

            // No need to process an empty line
            if (line.isEmpty())
            {
                continue;
            }

            Future<Integer> t = pool.submit(new LineExtractor(line));
        }
    }

}
