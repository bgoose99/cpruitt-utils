package data.problems;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javautils.Utils;
import data.AbstractProblem;

public class Problem022 extends AbstractProblem
{
    public Problem022()
    {
        setNumber( 22 );
        setDescription( "<htm>Using names.txt (right click and 'Save Link/Target As...'),<br>"
                + "a 46K text file containing over five-thousand first<br>"
                + "names, begin by sorting it into alphabetical order.<br>"
                + "Then working out the alphabetical value for each name,<br>"
                + "multiply this value by its alphabetical position in the<br>"
                + "list to obtain a name score.<br><br>"
                + "For example, when the list is sorted into alphabetical<br>"
                + "order, COLIN, which is worth 3 + 15 + 12 + 9 + 14 = 53,<br>"
                + "is the 938th name in the list. So, COLIN would obtain a<br>"
                + "score of 938 × 53 = 49714.<br><br>"
                + "What is the total of all the name scores in the file?</html>" );
    }

    @Override
    public String getSolution()
    {
        long scoreSum = 0;
        List<String> names = readNames();
        Collections.sort( names );

        for( int i = 0; i < names.size(); i++ )
        {
            scoreSum += ( i + 1 ) * alphaScore( names.get( i ) );
        }

        return "Score sum: " + scoreSum;
    }

    private List<String> readNames()
    {
        List<String> names = null;

        BufferedReader in = null;
        try
        {
            in = new BufferedReader( new FileReader( new File( Utils
                    .loadResourceURL( "/resources/names.txt" ).toURI() ) ) );
            String s = in.readLine();
            s = s.trim();
            s = s.toUpperCase();
            String[] sArray = s.split( "[,]" );
            names = Arrays.asList( sArray );
        } catch( Exception e )
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                in.close();
            } catch( Exception e )
            {
                e.printStackTrace();
            }
        }

        return names;
    }

    private int alphaScore( String s )
    {
        int score = 0;
        for( int i = 0; i < s.length(); i++ )
        {
            if( s.charAt( i ) == '"' )
                continue;
            score += (int)s.charAt( i ) - 64; // 64 is the index of A in the
                                              // ASCII table
        }
        return score;
    }
}
