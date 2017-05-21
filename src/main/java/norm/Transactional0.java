package norm;


public final class Transactional0 {
    public static void begin(){
        Norms.getNorm().getTransactional().begin();
    }

    public static boolean hasBegin(){
        return Norms.getNorm().getTransactional().hasBegin();
    }

    public static void rollback() {
        Norms.getNorm().getTransactional().rollback();
    }

    public static void commit(){
        Norms.getNorm().getTransactional().commit();
    }
}
