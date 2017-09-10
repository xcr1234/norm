package norm.generator;

import impl.DefaultConverter;



public class Main {
    public static void main(String[] args) {
        Converter converter = new DefaultConverter();
        System.out.println(converter.getJavaName("user_info"));
    }
}
