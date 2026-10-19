package com.chidicivok.notificationservice.services.implementations;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailTemplateService {

    private final TemplateEngine templateEngine;

    public EmailTemplateService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String buildEmail(String name,  String message) {

        Context context = new Context();

        context.setVariable("name", name);
        context.setVariable("message", message);

        return templateEngine.process("email/custom-mail", context);
    }
}