package org.norm.result;

import java.util.List;

public class ResultMap {

    private Class<?> type;

    private List<Result> results;

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
