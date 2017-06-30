package com.example.moiz_ihs.contracttracing.models;

/**
 * Created by Moiz-IHS on 5/30/2017.
 */

public class ContactDetail {

    private String contactName;
    private String contactId;
    private String indexId;
    private String indexName;
    private String gender;
    private String age;
    private Boolean isContactFormCompleted;


    public String getIndexName() {
        return indexName;
    }

    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
    }

    public Boolean getContactFormCompleted() {
        return isContactFormCompleted;
    }

    public void setContactFormCompleted(Boolean contactFormCompleted) {
        isContactFormCompleted = contactFormCompleted;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
