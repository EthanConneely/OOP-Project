package ie.atu.sw;

public interface Processor
{
    /**
     * Runs the threaded executor service for processing the lines with virtual
     * threads
     */
    public void execute();

    /**
     * Load the 1000 most common words
     */
    public void load1000CommonWords();

    /**
     * Load the word dictionary
     */
    public void loadDictionary();

    /**
     * Ouput all the words sorted from a to z or z to a
     *
     * @param ascending sort ascending or descending
     */
    public void outputAllWords(boolean ascending);

    /**
     * Output the Unique Words Count to the File
     */
    public void outputUniqueWordsCount();

    /**
     * Will output to the output file the most frequntly occuring words
     *
     * @param freq number of words to show
     */
    public void outputMostFrequentWords(int freq);
}
