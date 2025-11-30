package eco.kosova.infrastructure.services;

import org.springframework.stereotype.Service;

@Service
public class SMSService {
    
    public void sendSMS(String phoneNumber, String message) {
        // Simulation - në realitet do të përdorte API si Twilio
        System.out.println("📱 SMS dërguar në " + phoneNumber + ": " + message);
    }
    
    public void sendBulkSMS(String[] phoneNumbers, String message) {
        for (String phone : phoneNumbers) {
            sendSMS(phone, message);
        }
    }
}