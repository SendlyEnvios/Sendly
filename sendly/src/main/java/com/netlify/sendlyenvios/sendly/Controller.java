package com.netlify.sendlyenvios.sendly;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    public static void enviarEmail(String email, String token) throws ResendException {

        Resend resend = new Resend(System.getenv("RESEND_API_KEY"));

        CreateEmailOptions params = CreateEmailOptions.builder()
                .from("onboarding@resend.dev")
                .to(email)
                .subject("Recuperação de senha - Sendly")
                .html("""
                    <h1>Recuperação de senha</h1>
                    <p>Seu código de recuperação é:</p>
                    <h2>%s</h2>
                    """.formatted(token))
                .build();

        CreateEmailResponse data = resend.emails().send(params);

        System.out.println("E-mail enviado!");
        System.out.println("ID: " + data.getId());
    }
    
    public static Object noUser(){
        Map<String, String> user = new HashMap<>();
        user.put("mensagem", "usuário ou senha incorretos");

        return user;
    }
}
