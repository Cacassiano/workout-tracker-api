package dev.cacassiano.notification_service.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import dev.cacassiano.notification_service.DTOs.User;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EmailService implements NotificationService{

    @Value("${spring.mail.username}")
    private String applicationEmail;

    @Autowired
    private JavaMailSender mailSender;
    

    @Override
    public void sendNotification(User user) {
        log.info("Sending notification to: "+ user.email());
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.email());
        mailMessage.setFrom(applicationEmail);

        final String pendingWorkoutSubject = "🏋️‍♂️ **Lembrete de Treino**";
        mailMessage.setSubject(pendingWorkoutSubject);
        
        final String body = "Olá, "+user.username()+"!\n" + //
            "\n" + //
            "Você possui treinos pendentes para hoje no CalisTracker.\n" + //
            "\n" + //
            "Acesse a plataforma e conclua suas atividades para manter sua evolução em dia e não perder o ritmo!\n" + //
            "\n" + //
            "Bons treinos 💪\n";
        mailMessage.setText(body);
        // Send email
        try{
            mailSender.send(mailMessage);
            log.info("Email sended succesfully");
        }catch(MailException ex) {
            log.error("Error while sending email for "+ user.email(), ex);
        }
        
    }
    
}
