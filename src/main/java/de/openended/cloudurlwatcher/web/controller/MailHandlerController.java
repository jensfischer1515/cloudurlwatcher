package de.openended.cloudurlwatcher.web.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Receive incoming email at emailAddress@cloudurlwatcher.appspotmail.com
 * addresses.
 * 
 * @author jfischer
 */
@Controller
public class MailHandlerController {
    private static final Logger logger = LoggerFactory.getLogger(MailHandlerController.class);

    @RequestMapping(value = { "/_ah/mail/{emailAddress}@{appId}.appspotmail.com" }, method = RequestMethod.POST)
    @ResponseBody
    public String receiveMail(@PathVariable String emailAddress, InputStream inputStream) throws IOException, MessagingException {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage message = new MimeMessage(session, inputStream);
        logger.info("Incoming email to '{}' from '{}' with subject '{}'",
                new Object[] { message.getRecipients(RecipientType.TO), message.getFrom(), message.getSubject() });
        return "Mail received at " + emailAddress;
    }
}
