package ie.atu.sw;

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

    @Override
    public String toString()
    {
        return word + " " + pages.toString() + " " + count;
    }
}
