package norm.page.impl;
import norm.page.Page;
import norm.page.PageModel;
import norm.page.PageSql;

import java.util.Locale;

public class DerbyPage implements PageSql {
    @Override
    public PageModel buildSql(Page page, String query) {
        Integer first = null;
        Integer second = null;

        final StringBuilder sb = new StringBuilder(query.length() + 50);
        final String normalizedSelect = query.toLowerCase(Locale.ROOT).trim();
        final int forUpdateIndex = normalizedSelect.lastIndexOf( "for update") ;

        if ( hasForUpdateClause( forUpdateIndex ) ) {
            sb.append( query.substring( 0, forUpdateIndex-1 ) );
        }
        else if ( hasWithClause( normalizedSelect ) ) {
            sb.append( query.substring( 0, getWithIndex( query ) - 1 ) );
        }
        else {
            sb.append( query );
        }

        if ( page.offset() == 0 ) {
            sb.append( " fetch first " );
        }
        else {
            sb.append( " offset ? rows fetch next" );
            first = page.offset();
        }
        sb.append( "? rows only" );
        if(first == null){
            first = page.limit();
        }else{
            second = page.limit();
        }

        if ( hasForUpdateClause( forUpdateIndex ) ) {
            sb.append( ' ' );
            sb.append( query.substring( forUpdateIndex ) );
        }
        else if ( hasWithClause( normalizedSelect ) ) {
            sb.append( ' ' ).append( query.substring( getWithIndex( query ) ) );
        }
        return new PageModelImpl(sb.toString(),first,second);
    }


    @Override
    public String database() {
        return "derby";
    }


    private static boolean hasForUpdateClause(int forUpdateIndex) {
        return forUpdateIndex >= 0;
    }

    private static boolean hasWithClause(String normalizedSelect){
        return normalizedSelect.startsWith( "with ", normalizedSelect.length() - 7 );
    }

    private static int getWithIndex(String querySelect) {
        int i = querySelect.lastIndexOf( "with " );
        if ( i < 0 ) {
            i = querySelect.lastIndexOf( "WITH " );
        }
        return i;
    }
}
