package de.openended.cloudurlwatcher.web.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.appengine.api.xmpp.JID;
import com.google.appengine.api.xmpp.Message;
import com.google.appengine.api.xmpp.MessageBuilder;
import com.google.appengine.api.xmpp.MessageType;
import com.google.appengine.api.xmpp.Presence;
import com.google.appengine.api.xmpp.XMPPService;

/**
 * JID: app-id@appspot.com or anything@app-id.appspotchat.com
 * 
 * @author jfischer
 * 
 */
@Controller
public class XmppReceiverController {
    @Autowired
    private XMPPService xmppService;

    @RequestMapping(value = { "/_ah/xmpp/message/chat" }, method = RequestMethod.POST)
    public String receive(HttpServletRequest request) throws IOException {
        Message message = xmppService.parseMessage(request);

        if (message.getBody().startsWith("/askme")) {
            handleAskMeCommand(message);
        } else if (message.getBody().startsWith("/tellme ")) {
            String questionText = message.getBody().replaceFirst("/tellme ", "");
            handleTellMeCommand(message, questionText);
        } else if (message.getBody().startsWith("/")) {
            // handleUnrecognizedCommand(message);
        } else {
            handleAnswer(message);
        }

        return null;
    }

    /**
     * Handles requests to be asked a question without providing one.
     */
    private void handleAskMeCommand(Message message) {
        // QuestionService questionService = new QuestionService();
        //
        // JID sender = message.getFromJid();
        // Question newlyAssigned = questionService.assignQuestion(sender);
        //
        // if (newlyAssigned != null) {
        // replyToMessage(message, "While I'm thinking, perhaps you can answer "
        // +
        // "me this: " + newlyAssigned.getQuestion());
        // } else {
        // replyToMessage(message, "Sorry, I don't have anything to ask you at "
        // +
        // "the moment.");
        // }
    }

    /**
     * Handles answers to questions we've asked the user.
     */
    private void handleAnswer(Message message) {
        // QuestionService questionService = new QuestionService();
        //
        // JID sender = message.getFromJid();
        // Question currentlyAnswering = questionService.getAnswering(sender);
        //
        // if (currentlyAnswering != null) {
        // // Answering a question
        // currentlyAnswering.setAnswer(message.getBody());
        // currentlyAnswering.setAnswered(new Date());
        // currentlyAnswering.setAnswerer(sender);
        //
        // questionService.storeQuestion(currentlyAnswering);
        //
        // // Send the answer to the asker
        // sendMessage(currentlyAnswering.getAsker(), "I have thought long and "
        // +
        // "hard, and concluded: " + currentlyAnswering.getAnswer());
        //
        // // Send acknowledgment to the answerer
        // Question previouslyAsked = questionService.getAsked(sender);
        //
        // if (previouslyAsked != null) {
        // replyToMessage(message, "Thank you for your wisdom. I'm still " +
        // "thinking about your question.");
        // } else {
        // replyToMessage(message, "Thank you for your wisdom.");
        // }
        // } else {
        // handleUnrecognizedCommand(message);
        // }
    }

    // ...

    private void sendMessage(String recipient, String body) {
        Message message = new MessageBuilder().withRecipientJids(new JID(recipient)).withMessageType(MessageType.NORMAL).withBody(body)
                .build();

        xmppService.sendMessage(message);
    }

    /**
     * Handles /tellme requests, asking the Guru a question.
     */
    private void handleTellMeCommand(Message message, String questionText) {
        // QuestionService questionService = new QuestionService();
        //
        // JID sender = message.getFromJid();
        // Question previouslyAsked = questionService.getAsked(sender);
        //
        // if (previouslyAsked != null) {
        // // Already have a question, and they're not answering one
        // replyToMessage(message,
        // "Please! One question at a time! You can ask "
        // + "me another once you have an answer to your current question.");
        // } else {
        // // Asking a question
        // Question question = new Question();
        // question.setQuestion(questionText);
        // question.setAsked(new Date());
        // question.setAsker(sender);
        //
        // questionService.storeQuestion(question);
        //
        // // Try and find one for them to answer
        // Question assigned = questionService.assignQuestion(sender);
        //
        // if (assigned != null) {
        // replyToMessage(message, "While I'm thinking, perhaps you can " +
        // "answer me this: " + assigned.getQuestion());
        // } else {
        // replyToMessage(message, "Hmm. Let me think on that a bit.");
        // }
        // }
    }

    // ...

    private void replyToMessage(Message message, String body) {
        Message reply = new MessageBuilder().withRecipientJids(message.getFromJid()).withMessageType(MessageType.NORMAL).withBody(body)
                .build();

        xmppService.sendMessage(reply);
    }

    @RequestMapping(value = "/_ah/xmpp/subscription/subscribe", method = RequestMethod.POST)
    public String subscribe(HttpServletRequest request) {
        return "subscribe";
    }

    @RequestMapping(value = "/_ah/xmpp/subscription/subscribed", method = RequestMethod.POST)
    public String subscribed(HttpServletRequest request) {
        return "subscribed";
    }

    @RequestMapping(value = "/_ah/xmpp/subscription/unsubscribe", method = RequestMethod.POST)
    public String unsubscribe(HttpServletRequest request) {
        return "unsubscribe";
    }

    @RequestMapping(value = "/_ah/xmpp/subscription/unsubscribed", method = RequestMethod.POST)
    public String unsubscribed(HttpServletRequest request) {
        return "unsubscribed";
    }

    @RequestMapping(value = "/_ah/xmpp/presence/available", method = RequestMethod.POST)
    public String available(HttpServletRequest request) throws IOException {
        Presence presence = xmppService.parsePresence(request);
        return "available";
    }

    @RequestMapping(value = "/_ah/xmpp/presence/unavailable", method = RequestMethod.POST)
    public String unavailable(HttpServletRequest request) throws IOException {
        Presence presence = xmppService.parsePresence(request);
        return "unavailable";
    }

    @RequestMapping(value = "/_ah/xmpp/presence/probe", method = RequestMethod.POST)
    public String probe(HttpServletRequest request) throws IOException {
        Presence presence = xmppService.parsePresence(request);
        return "probe";
    }

}
