package norm.generator.impl;

import norm.generator.Entity;
import norm.generator.EntityWriter;

public abstract class AbsEntity implements Entity {


    private EntityWriter writer;

    @Override
    public EntityWriter getWriter() {
        if(writer == null){
            return DefaultEntityWriter.getInstance();
        }
        return writer;
    }

    @Override
    public void setWriter(EntityWriter writer) {
        this.writer = writer;
    }

    @Override
    public String getBasePackageDir() {
        return getBasePackage().replace('.','/');
    }
}
