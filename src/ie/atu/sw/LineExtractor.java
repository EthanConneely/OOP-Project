package ie.atu.sw;

import java.util.concurrent.Callable;

public class LineExtractor implements Callable<Integer>
{
    private String line;

    public LineExtractor(String line)
    {
        this.line = line;
    }

    @Override
    public Integer call() throws Exception
    {
        return line.length();
    }

}
