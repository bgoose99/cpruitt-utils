package data;

import java.util.ArrayList;
import java.util.List;

import data.problems.*;

/*******************************************************************************
 * 
 ******************************************************************************/
public final class ProblemFactory
{
    private static List<AbstractProblem> problems;

    /***************************************************************************
     * Private constructor prevents instantiation.
     **************************************************************************/
    private ProblemFactory()
    {
    }

    /***************************************************************************
     * Static initializer.
     **************************************************************************/
    static
    {
        problems = new ArrayList<AbstractProblem>();
        problems.add( new Problem001() );
        problems.add( new Problem002() );
        problems.add( new Problem003() );
        problems.add( new Problem004() );
        problems.add( new Problem005() );
        problems.add( new Problem006() );
        problems.add( new Problem007() );
        problems.add( new Problem008() );
        problems.add( new Problem009() );
        problems.add( new Problem010() );
        problems.add( new Problem011() );
        problems.add( new Problem012() );
        problems.add( new Problem013() );
        problems.add( new Problem014() );
        problems.add( new Problem015() );
        problems.add( new Problem016() );
        problems.add( new Problem017() );
        problems.add( new Problem018() );
        problems.add( new Problem019() );
        problems.add( new Problem020() );
        problems.add( new Problem021() );
        problems.add( new Problem022() );
        problems.add( new Problem023() );
        problems.add( new Problem024() );
        problems.add( new Problem025() );
        problems.add( new Problem026() );
        // problems.add( new Problem027() );
        // problems.add( new Problem028() );
        // problems.add( new Problem029() );
        // problems.add( new Problem030() );
        // problems.add( new Problem031() );
        // problems.add( new Problem032() );
        // problems.add( new Problem033() );
        // problems.add( new Problem034() );
        // problems.add( new Problem035() );
        // problems.add( new Problem036() );
        // problems.add( new Problem037() );
        // problems.add( new Problem038() );
        // problems.add( new Problem039() );
        // problems.add( new Problem040() );
    }

    /***************************************************************************
     * Returns the list of all available problems.
     * 
     * @return
     **************************************************************************/
    public static List<AbstractProblem> getProblems()
    {
        return problems;
    }
}
