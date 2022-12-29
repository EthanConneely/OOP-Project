package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

/**
 * WordDescriptions is a list of descriptions for a single word
 */
public class WordDescriptions
{
    private List<String> descriptions = new ArrayList<>();

    /**
     * Add a description for the word it is assosiated for
     *
     * @param description the description for the word
     */
    public void addDescription(String description)
    {
        descriptions.add(description);
    }

    public String toString()
    {
        return String.join("\n\t\t", descriptions);
    }
}
