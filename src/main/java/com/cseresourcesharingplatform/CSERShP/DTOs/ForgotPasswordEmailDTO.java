package com.cseresourcesharingplatform.CSERShP.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswordEmailDTO {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
