package ie.atu.sw;

import java.util.ArrayList;
import java.util.List;

public class WordDescriptions
{
    private List<String> descriptions = new ArrayList<>();

    public void addDescription(String description)
    {
        descriptions.add(description);
    }

    @Override
    public String toString()
    {
        return String.join("\n\t\t", descriptions);
    }
}
