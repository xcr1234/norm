package org.norm.naming;


public class UpperCaseTableNameStrategy implements NameStrategy {

    @Override
    public String format(String className) {
        return className.toUpperCase();
    }
}
