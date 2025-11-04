package com.cseresourcesharingplatform.CSERShP.Repository;

public interface EmailRepository {
    void sendHtmlMail(String to, String subject, String htmlContent);
}
