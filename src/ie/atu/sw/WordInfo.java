package ie.atu.sw;

import java.util.HashSet;
import java.util.Set;

public class WordInfo
{
    public WordInfo(String word)
    {
        this.word = word;
    }

    private Integer count = 0;
    private String word;
    private Set<Integer> pages = new HashSet<>();
    private WordDescriptions descriptions = new WordDescriptions();

    /**
     * A getter for the word
     *
     * @return the word
     */
    public String getWord()
    {
        return word;
    }

    /**
     * Setter for the word
     *
     * @param word set the word
     */
    public void setWord(String word)
    {
        this.word = word;
    }

    /**
     * Add a page number this word is in
     *
     * @param pageNumber the page number the word is in
     */
    public void addPage(int pageNumber)
    {
        pages.add(pageNumber);
    }

    /**
     * This method is thread safe because of synchronized
     */
    public synchronized void incrementCount()
    {
        count++;
    }

    /**
     * The getter for the number of times this word appears
     *
     * @return number of times this word appears
     */
    public Integer getCount()
    {
        return count;
    }

    /**
     * Set the words descriptions
     *
     * @param descriptions The words descriptions
     */
    public void setDescription(WordDescriptions descriptions)
    {
        this.descriptions = descriptions;
    }

    @Override
    public String toString()
    {
        return word + "\n\tCount: " + count + "\n\tPages: " + pages.toString() + "\n\tDescriptions: \n\t\t" + descriptions;
    }
}
