package eco.kosova.infrastructure.services;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service për dërgimin e email-ave.
 */
@Service
public class EmailService {
    
    private static final Logger logger = Logger.getLogger(EmailService.class.getName());
    
    /**
     * Dërgo email tek një adresë
     */
    public void sendEmail(String to, String subject, String body) {
        logger.info(String.format(
            "📧 Email sent to %s | Subject: %s",
            maskEmail(to), subject
        ));
        
        // Në sistem real:
        // javaMailSender.send(createMessage(to, subject, body));
    }
    
    /**
     * Dërgo bulk email
     */
    public void sendBulkEmail(String[] recipients, String subject, String body) {
        logger.info(String.format(
            "📧 Sending bulk email to %d recipients | Subject: %s",
            recipients.length, subject
        ));
        
        for (String recipient : recipients) {
            sendEmail(recipient, subject, body);
        }
    }
    
    /**
     * Mask email për privacy
     */
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "***@***.***";
        }
        String[] parts = email.split("@");
        return parts[0].charAt(0) + "***@" + parts[1];
    }
}