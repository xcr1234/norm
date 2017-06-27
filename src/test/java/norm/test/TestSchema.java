package norm.test;

import norm.Norm;
import norm.impl.Meta;
import norm.impl.SQLBuilder;
import norm.test.entity.Role;


public class TestSchema {
    public static void main(String[] args) {
        Norm norm = new Norm();
        norm.setSchema("public");
        Meta meta = Meta.parse(Role.class,norm.getConfiguration());
        System.out.println(meta.getTableName());
        System.out.println(SQLBuilder.findAll(meta));
    }
}
