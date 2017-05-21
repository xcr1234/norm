package norm.page;


import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Locale;

public class DerbyPageFactory extends AbstractPageFactory {
    @Override
    public Page create(int pageNumber, int pageSize) {
        return new DerbyPage(pageNumber,pageSize);
    }

    protected static class DerbyPage extends AbstractPage{

        private static final long serialVersionUID = 2739106183373897357L;

        public DerbyPage(int pageNumber, int pageSize) {
            super(pageNumber, pageSize);
        }

        @Override
        public String pageSelect(String query) {
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

            if ( offset() == 0 ) {
                sb.append( " fetch first " );
            }
            else {
                sb.append( " offset " ).append( offset() ).append( " rows fetch next " );
            }

            sb.append( limit() ).append( " rows only" );

            if ( hasForUpdateClause( forUpdateIndex ) ) {
                sb.append( ' ' );
                sb.append( query.substring( forUpdateIndex ) );
            }
            else if ( hasWithClause( normalizedSelect ) ) {
                sb.append( ' ' ).append( query.substring( getWithIndex( query ) ) );
            }
            return sb.toString();
        }

        @Override
        public void setState(PreparedStatement ps, int index) throws SQLException {

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
}
