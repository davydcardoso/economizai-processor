package com.economizai.Processor.jobs.Processor;

import com.economizai.Processor.mail.EmailSender;
import com.economizai.Processor.mail.FreemakerConfig;
import com.economizai.Processor.queues.dto.RecoverPassword;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@AutoConfiguration
public class ProcessSendRecoverPasswordEmail {

    @Autowired
    private Configuration config;

    @Autowired
    private FreemakerConfig freemakerConfig;

    public void run(RecoverPassword recoverPassword) {
        try {
            EmailSender emailSender = new EmailSender();
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

            simpleMailMessage.setFrom("contato@economizai.tech");
            simpleMailMessage.setTo(recoverPassword.getEmail());
            simpleMailMessage.setSubject("EconomizAI - Recuperação de Senha");

            MimeMessage message = emailSender.javaMailSender().createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name()
            );

            Template template = config.getTemplate("recover-password-template.ftl");

            Map<String, Object> model = new HashMap<>();
            model.put("CODIGO_DE_CONFIRMACAO", recoverPassword.getCode());

            String html = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);

            helper.setTo(Objects.requireNonNull(simpleMailMessage.getTo()));
            helper.setText(html, true);
            helper.setSubject(Objects.requireNonNull(simpleMailMessage.getSubject()));
            helper.setFrom(Objects.requireNonNull(simpleMailMessage.getFrom()));

            emailSender.javaMailSender().send(message);

        }catch (MessagingException | IOException | TemplateException e)  {
            System.out.println("Error in send email recover password: " + e.getMessage());
        }
    }
}
