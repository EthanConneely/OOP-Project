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
    private Map<String, WordInfo> words = new ConcurrentHashMap<>(); // Average O(1)
    private Set<String> commonWords = new ConcurrentSkipListSet<>(); // O(log n)
    private Map<String, WordDescriptions> descriptionDictionary = new ConcurrentHashMap<>(); // Average O(1)

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
        System.out.println("Loading Common Words...");

        try
        {
            commonWords.addAll(Files.lines(commonWordsFile.toPath()).toList());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void loadDictionary()
    {
        System.out.println("Loading Dictionary...");

        try
        {
            Files.lines(dictionaryFile.toPath()).forEach(line ->
            {
                var parts = line.split(",");
                if (parts.length >= 3)
                {
                    var word = descriptionDictionary.getOrDefault(parts[0].toLowerCase(), new WordDescriptions());
                    word.addDescription(parts[1] + " " + parts[2]);
                    descriptionDictionary.put(parts[0].toLowerCase(), word);
                }
            });
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

        insertDescriptions();

        System.out.println("Finished Indexing!");
    }

    private void insertDescriptions()
    {
        for (var word : words.values())
        {
            word.setDescription(descriptionDictionary.get(word.getWord().toLowerCase()));
        }
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

            outputStreamWriter.close();
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

    /**
     * Will output to the output file the most frequntly occuring n words
     *
     * @param freq number of words to show
     */
    public void outputMostFrequentWords(int freq)
    {
        System.out.println("Outputting Most Frequent Words");

        try (OutputStream outputStream = new FileOutputStream(outputFile))
        {
            Writer outputStreamWriter = new OutputStreamWriter(outputStream);

            // Sort the words
            Comparator<WordInfo> compare = (a, b) ->
            {
                return b.getCount().compareTo(a.getCount());
            };

            // Output the most n frequent words using an iterator because limit was blank
            var wordIterator = words.values().stream().sorted(compare).iterator();

            for (int i = 0; i < freq && wordIterator.hasNext(); i++)
            {
                var word = wordIterator.next();

                try
                {
                    outputStreamWriter.write(word.toString() + "\n");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            outputStreamWriter.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
