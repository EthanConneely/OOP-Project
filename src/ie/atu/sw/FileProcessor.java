package ie.atu.sw;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class FileProcessor
{
    private File inputFile;
    private File commonWordsFile;

    // http://infotechgems.blogspot.com/2011/11/java-collections-performance-time.html
    private Map<String, WordInfo> words = new ConcurrentHashMap<>(); // Average O(1) other wise if there are collisions it uses a linked list
    private Set<String> commonWords = new ConcurrentSkipListSet<>(); // O(log n) uses a tree acording to google

    private File dictionaryFile;

    private File outputFile;

    private AtomicInteger pageNumber = new AtomicInteger(1);

    public FileProcessor(File inputFile, File dictionaryFile, File commonWordsFile, File outputFile)
    {
        this.inputFile = inputFile;
        this.dictionaryFile = dictionaryFile;
        this.commonWordsFile = commonWordsFile;
        this.outputFile = outputFile;
    }

    public void load1000CommonWords()
    {
        System.out.println("Loading Dictionary...");
        try
        {
            commonWords.addAll(Files.lines(commonWordsFile.toPath()).toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void execute()
    {
        System.out.println("Started Indexing...");

        try (ExecutorService pool = Executors.newVirtualThreadPerTaskExecutor())
        {
            try
            {
                Files.lines(inputFile.toPath()).forEach(line ->
                {
                    pool.execute(() -> processLineThreaded(line, pageNumber.getAndIncrement()));
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            pool.shutdown();

            try
            {
                pool.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            pool.shutdownNow();
        }

        System.out.println("Finished Indexing!");
    }

    private void processLineThreaded(String line, int page)
    {
        Arrays.stream(line.split("\\s")).forEach(word ->
        {
            // Cleanup the words removing any white space and
            // making it lowercase and removing none letters
            String processedWord = word.trim().toLowerCase().replaceAll("[^a-z]", "");

            if (!commonWords.contains(processedWord) && !processedWord.isEmpty())
            {
                WordInfo info = words.getOrDefault(processedWord, new WordInfo(processedWord));
                info.addPage(page);
                info.incrementCount();
                words.put(processedWord, info);
            }
        });
    }

    public void outputAllWords(boolean ascending)
    {
        System.out.println("Outputting All Words");

        try (OutputStream outputStream = new FileOutputStream(outputFile))
        {
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

            // Sort the words
            Comparator<WordInfo> compare = (a, b) ->
            {
                return a.getWord().compareToIgnoreCase(b.getWord());
            };

            if (!ascending)
            {
                compare = (a, b) ->
                {
                    return b.getWord().compareToIgnoreCase(a.getWord());
                };
            }

            // Output the words
            words.values().stream().sorted(compare).forEach(word ->
            {
                try
                {
                    outputStreamWriter.write(word.getWord() + "\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void outputUniqueWordsCount()
    {
        System.out.println("Outputting Words Count");

        try
        {
            Files.writeString(outputFile.toPath(), "Words: " + words.size());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void outputMostFrequentWords(int freq)
    {
        System.out.println("Outputting Most Frequent Words");

        try (OutputStream outputStream = new FileOutputStream(outputFile))
        {
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

            // Sort the words
            Comparator<WordInfo> compare = (a, b) ->
            {
                return a.getCount().compareTo(b.getCount());
            };

            // Output the most n frequent words
            words.values().stream().sorted(compare).limit(freq).forEach(word ->
            {
                try
                {
                    outputStreamWriter.write(word.getWord() + "\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
