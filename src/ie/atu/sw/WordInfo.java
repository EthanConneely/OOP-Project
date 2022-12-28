package ie.atu.sw;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class WordInfo
{
    private String word;

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    private List<Integer> pages = new ArrayList<>();

    public void addPage(int pageNumber)
    {
        pages.add(pageNumber);
    }

    private Integer count = 0;

    // This method is thread safe thanks to synchronized
    public synchronized void incrementCount()
    {
        count++;
    }

    public Integer getCount()
    {
        return count;
    }

    public WordInfo(String word)
    {
        this.word = word;
    }

    private WordDescriptions descriptions = new WordDescriptions();

    public void setDescription(WordDescriptions descriptions)
    {
        this.descriptions = descriptions;
    }

    @Override
    public String toString()
    {
        return word + "\n\tCount: " + count + "\n\tPages: " + pages.toString() + "\n\tDescriptions: \n\t\t" + descriptions;
    }

    @Override
    public int hashCode()
    {
        return this.word.hashCode();
    }
}
