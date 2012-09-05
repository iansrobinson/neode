package org.neo4j.neode.statistics;

import java.util.Map;

import org.neo4j.neode.logging.Log;

public class PlainTextFormatter
{
    private final Log log;

    public PlainTextFormatter( Log log )
    {
        this.log = log;
    }

    public void describe( GraphStatistics graphStatistics )
    {
        int maxLengthNodeLabel = 14;
        int maxLengthNumber = 7;

        for ( NodeStatistic ns : graphStatistics.nodeStatistics() )
        {
            maxLengthNodeLabel = ns.label().length() > maxLengthNodeLabel ?
                    ns.label().length() :
                    maxLengthNodeLabel;

            for ( RelationshipStatistic rs : ns.relationshipStatistics() )
            {
                maxLengthNodeLabel = rs.label().length() > maxLengthNodeLabel ?
                        rs.label().length() :
                        maxLengthNodeLabel;

                maxLengthNumber = updateMaxLength( maxLengthNumber, lenInt( rs.incoming().average() ) );
                maxLengthNumber = updateMaxLength( maxLengthNumber, lenInt( rs.outgoing().average() ) );
                maxLengthNumber = updateMaxLength( maxLengthNumber, lenInt( rs.outgoing().high() ) );
                maxLengthNumber = updateMaxLength( maxLengthNumber, lenInt( rs.outgoing().high() ) );
            }
        }

        int lineLength = maxLengthNodeLabel + (3 * maxLengthNumber) + 13;

        log.write( String.format( "Graph statistics for '%s':\n", graphStatistics.description() ) );
        for ( NodeStatistic nodeStatistic : graphStatistics.nodeStatistics() )
        {
            log.write( repeat( "=", lineLength ) );
            log.write( String.format( "| %s | %s |",
                    padRight( nodeStatistic.label(), maxLengthNodeLabel ),
                    padRight( nodeStatistic.count(), 6 + (3 * maxLengthNumber) ) ) );
            log.write( repeat( "=", lineLength ) );
            log.write( String.format( "| %s | %s | %s | %s |",
                    repeat( " ", maxLengthNodeLabel ),
                    padLeft( "Average", maxLengthNumber ),
                    padLeft( "High", maxLengthNumber ),
                    padLeft( "Low", maxLengthNumber ) ) );
            log.write( repeat( "-", lineLength ) );
            for ( RelationshipStatistic relationshipStatistic : nodeStatistic.relationshipStatistics() )
            {
                log.write( String.format( "| %s|", padRight( relationshipStatistic.label(), lineLength - 3 ) ) );
                log.write( String.format( "| %s | %s | %s | %s |",
                        padLeft( "OUTGOING", maxLengthNodeLabel ),
                        padLeft( relationshipStatistic.outgoing().average(), maxLengthNumber ),
                        padLeft( relationshipStatistic.outgoing().high(), maxLengthNumber ),
                        padLeft( relationshipStatistic.outgoing().low(), maxLengthNumber ) ) );

                log.write( String.format( "| %s | %s | %s | %s |",
                        padLeft( "incoming", maxLengthNodeLabel ),
                        padLeft( relationshipStatistic.incoming().average(), maxLengthNumber ),
                        padLeft( relationshipStatistic.incoming().high(), maxLengthNumber ),
                        padLeft( relationshipStatistic.incoming().low(), maxLengthNumber ) ) );

                log.write( repeat( "-", lineLength ) );
            }
            log.write( "" );
        }
        log.write( repeat( "=", lineLength ) );
        log.write( String.format( "| %s |", padRight( "Totals", lineLength - 4 )));
        log.write( repeat( "=", lineLength ) );
        log.write( String.format( "| %s | %s |",padRight( "Nodes", maxLengthNodeLabel ),
                padRight( graphStatistics.totalNodes(), 6 + (3 * maxLengthNumber) ) ) );
        log.write( String.format( "| %s | %s |",padRight( "Relationships", maxLengthNodeLabel ),
                padRight( graphStatistics.totalRelationships(), 6 + (3 * maxLengthNumber) ) ) );
        log.write( repeat( "-", lineLength ) );

        Map<String,Integer> relationshipTotals = graphStatistics.totalsPerRelationship();
        for ( String label : relationshipTotals.keySet() )
        {
            log.write( String.format( "| %s | %s |",padRight( label, maxLengthNodeLabel ),
                    padRight( relationshipTotals.get( label ), 6 + (3 * maxLengthNumber) ) ) );
        }
        log.write( repeat( "-", lineLength ) );

    }

    private static int updateMaxLength( int maxLength, int value )
    {
        return value > maxLength ? value : maxLength;
    }

    private static int lenInt( int i )
    {
        String value = String.valueOf( i );
        return value.length();
    }

    private static String padRight( Object s, int n )
    {
        return String.format( "%1$-" + n + "s", String.valueOf( s ) );
    }

    private static String padLeft( Object s, int n )
    {
        return String.format( "%1$" + n + "s", String.valueOf( s ) );
    }

    private static String repeat( String s, int i )
    {
        return String.format( String.format( "%%0%dd", i ), 0 ).replace( "0", s );
    }

}
