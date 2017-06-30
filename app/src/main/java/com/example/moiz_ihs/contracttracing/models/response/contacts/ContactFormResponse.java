package com.example.moiz_ihs.contracttracing.models.response.contacts;

import java.util.List;

/**
 * Created by Moiz-IHS on 6/17/2017.
 */

public class ContactFormResponse {
    private List<Result> results = null;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}
