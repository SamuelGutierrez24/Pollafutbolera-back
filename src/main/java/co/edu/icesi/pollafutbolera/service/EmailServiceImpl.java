package co.edu.icesi.pollafutbolera.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import co.edu.icesi.pollafutbolera.dto.EmailPasswordDTO;
import co.edu.icesi.pollafutbolera.exception.DuplicateEmailsException;
import co.edu.icesi.pollafutbolera.model.User;
import co.edu.icesi.pollafutbolera.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import co.edu.icesi.pollafutbolera.dto.EmailDTO;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final UserRepository userRepository;

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final JwtService jwtService;
    private final EncryptionService encripter;
	
	@Value("${frontend.base.url}")
	private String frontendBaseUrl;
	@Value("${app.url.change-password:defaultChangePasswordUrl}")
    private String changePasswordUrl;


	@Override
	public String send(EmailDTO email) throws MessagingException {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,true,"UTF-8");
			
			helper.setTo(email.getEmailaddressee());
			helper.setSubject(email.getSubject());
			
			Context context = new Context();
	        context.setVariable("username", email.getUsername());
	        String html = templateEngine.process("email", context);
			
	        helper.setText(html, true);
	        javaMailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("Error to send email: "+e.getMessage());
		}
		return null;
	}
	
	@Override
	public String send(List<EmailDTO> emails) throws MessagingException {
	    try {
	    	 validateUniqueEmails(emails);
	        // Crear un arreglo de MimeMessage para almacenar todos los mensajes
	        MimeMessage[] messages = new MimeMessage[emails.size()];

	        // Iterar sobre la lista de EmailDTO para crear cada mensaje
	        for (int i = 0; i < emails.size(); i++) {
	            EmailDTO email = emails.get(i);
	            MimeMessage message = javaMailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

	            helper.setTo(email.getEmailaddressee());
	            helper.setSubject(email.getSubject());
	            String encriptedId=encripter.encrypt(email.getId());
	            Context context = new Context();
	            context.setVariable("username", email.getUsername());
	            context.setVariable("email", email.getEmailaddressee());
	            context.setVariable("baseUrl", frontendBaseUrl+"/auth/sign_up/"+encriptedId);
	            String html = templateEngine.process("invitacion", context);

	            helper.setText(html, true);
	            messages[i] = message; // Almacenar el mensaje en el arreglo
	        }

	        // Enviar todos los mensajes de una sola vez
	        javaMailSender.send(messages);
	    } catch (DuplicateEmailsException e) {
	        throw e;
	        
	    } catch (Exception e) {
	        throw new RuntimeException("Error to send email: " + e.getMessage());
	    }
	    return null;
	}
	
	private void validateUniqueEmails(List<EmailDTO> emails) {
        if (emails == null) return ;
        
        Set<String> uniqueEmails = new HashSet<>();
        List<String> duplicates = emails.stream()
        	.map(EmailDTO::getEmailaddressee)
            .map(String::toLowerCase)
            .filter(e -> !uniqueEmails.add(e))
            .collect(Collectors.toList());
        
        if (!duplicates.isEmpty()) {
        	throw new DuplicateEmailsException("Duplicate emails found: " + duplicates);
        }
	}


	public void sendEmailPassword(EmailPasswordDTO emailPasswordDTO) throws MessagingException {

        List<User> user0 = userRepository.findByMail(emailPasswordDTO.addressee());
        String tokenSent = "";

        if(!user0.isEmpty()) {
            tokenSent = jwtService.generateToken(user0.get(0));
            try {
                MimeMessage message = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setTo(emailPasswordDTO.addressee());
                helper.setSubject("Recuperar Contraseña");
                Context context = new Context();
                context.setVariable("message", "");
                context.setVariable("link", changePasswordUrl+ tokenSent);
                String contentHtml = templateEngine.process("changePassword", context);
                helper.setText(contentHtml, true);
                javaMailSender.send(message);
            }catch (Exception e){
                throw new MessagingException("Error sending email" + e.getMessage());
            }
        }
    }

    public void sendEmailConfirmRegistration(String email, String nickname) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("Bienvenido " + nickname);
            Context context = new Context();
            context.setVariable("nickname", nickname);
            String contentHtml = templateEngine.process("welcome", context);
            helper.setText(contentHtml, true);
            javaMailSender.send(message);
        }catch (Exception e){
            throw new MessagingException("Error sending email" + e.getMessage());
        }
    }

    public void sendNotificationEmail(String to, String subject, String content) throws MessagingException {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(to);
            helper.setSubject(subject);
            Context context = new Context();
            context.setVariable("message", content);
            String contentHtml = templateEngine.process("email", context); 
            helper.setText(contentHtml, true);
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new MessagingException("Error sending notification email: " + e.getMessage());
        }
    }
}
