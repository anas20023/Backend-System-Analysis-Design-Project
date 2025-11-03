package com.cseresourcesharingplatform.CSERShP.Repository;

import com.cseresourcesharingplatform.CSERShP.DTOs.ForgotPasswordEmailDTO;

public interface EmailRepository {
    void sendHtmlMail(String to, String subject, String htmlContent);
}
