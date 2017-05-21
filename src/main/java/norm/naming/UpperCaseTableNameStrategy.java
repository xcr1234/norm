package norm.naming;


public class UpperCaseTableNameStrategy implements TableNameStrategy{
    private static final long serialVersionUID = -3380871581929404752L;

    @Override
    public String format(String className) {
        return className.toUpperCase();
    }
}
