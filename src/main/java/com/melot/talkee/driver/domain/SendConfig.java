package com.melot.talkee.driver.domain;

import java.util.List;

/**
 * Created by mn on 2017/5/15.
 */
public class SendConfig {

    private List<String> emailTo;

    private String subject;

    public List<String> getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(List<String> emailTo) {
        this.emailTo = emailTo;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    private String content;


}
