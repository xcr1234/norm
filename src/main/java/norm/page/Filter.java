package norm.page;

public interface Filter<T> {
    boolean accept(T t);
}
