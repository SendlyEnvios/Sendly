package com.netlify.sendlyenvios.sendly;

import org.springframework.mail.SimpleMailMessage;

import java.util.HashMap;
import java.util.Map;

public class Controller {

    public static SimpleMailMessage enviarEmail(String email, String token) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("matheussetacursos@gmail.com");
        message.setTo(email);
        message.setSubject("Redefinição de senha - Sendly");

        message.setText(
                "Olá, você solicitou uma recuperação da sua conta Sendly?.\n\n" +
                        "Recebemos uma solicitação para redefinir sua senha.\n\n" +
                        "Clique no link abaixo para criar uma nova senha:\n\n" +
                        "https://pwjob-production-1606.up.railway.app/cadastroUpdate?token=" + token + "\n\n" +
                        "Este link é temporário.\n\n" +
                        "Se você não solicitou a redefinição de senha, ignore este e-mail.\n\n" +
                        "Equipe Sendly"
        );
        return message;
    }

    public static Object noUser(){
        Map<String, String> user = new HashMap<>();
        user.put("mensagem", "usuário ou senha incorretos");

        return user;
    }
}
