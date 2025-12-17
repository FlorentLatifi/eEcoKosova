package eco.kosova.infrastructure.services;

import org.springframework.stereotype.Service;

import java.util.logging.Logger;

/**
 * Service për dërgimin e SMS-ave.
 */
@Service
public class SMSService {
    
    private static final Logger logger = Logger.getLogger(SMSService.class.getName());
    
    public void sendSMS(String phoneNumber, String message) {
        // Simulation - në realitet do të përdorte API si Twilio
        logger.info(String.format("📱 SMS dërguar në %s: %s", maskPhoneNumber(phoneNumber), message));
    }
    
    public void sendBulkSMS(String[] phoneNumbers, String message) {
        logger.info(String.format("📱 Dërgohet bulk SMS tek %d numra", phoneNumbers.length));
        for (String phone : phoneNumbers) {
            sendSMS(phone, message);
        }
    }
    
    /**
     * Mask phone number për privacy
     */
    private String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return "***";
        }
        return phoneNumber.substring(0, 2) + "***" + phoneNumber.substring(phoneNumber.length() - 2);
    }
}