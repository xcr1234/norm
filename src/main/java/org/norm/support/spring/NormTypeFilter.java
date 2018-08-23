package org.norm.support.spring;


import org.norm.CrudDao;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;

import java.io.IOException;


public class NormTypeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
        ClassMetadata metadata = metadataReader.getClassMetadata();
        return metadata.isInterface() && contains(metadata.getInterfaceNames(), CrudDao.class.getName());
    }

    private static boolean contains(Object[] array,Object value){
        if(array == null){
            return false;
        }
        for(Object object : array){
            if(object == null ? value == null : object.equals(value)){
                return true;
            }
        }
        return false;
    }


}