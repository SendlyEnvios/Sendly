package com.netlify.sendlyenvios.sendly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.netlify.sendlyenvios.sendly.Controller.*;

@RestController
@CrossOrigin("*")
public class Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(
            @RequestParam String email,
            @RequestParam String password) {

        try {
            String sql = """
                    SELECT id, email FROM users WHERE email = ? AND password = ?
                    """;

            return ResponseEntity.ok(jdbcTemplate.queryForMap(sql, email, password));
        }catch(Exception e){
            return ResponseEntity.ok(noUser());
        }
    }

    @GetMapping("/cadastro")
    public ResponseEntity<?> cadastro(
            @RequestParam int id) {

        try {
            String sql = """
                    SELECT id, name, endereco, entregasAtivas, entregasFeitas, entregasSolicitadas, statusEntregaRecente, estimativaER, iconPerfil, firstName, observacao  FROM users WHERE id = ?
                    """;

            return ResponseEntity.ok(jdbcTemplate.queryForMap(sql, id));
        }catch(Exception e){
            return ResponseEntity.ok(noUser());
        }
    }

    @PostMapping("/cadastroNew")
    public ResponseEntity<?> cadastroNew(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String name,
            @RequestParam String telefone) {

        try {
            String sql = """
                    INSERT INTO users(name, email, password, telefone) VALUES(?,?,?,?)
                    """;

            return ResponseEntity.ok(jdbcTemplate.update(sql, name, email, password, telefone));
        }catch(Exception e){
            return ResponseEntity.ok(noUser());
        }
    }

    @PostMapping("/cadastroUpdate")
    public ResponseEntity<?> cadastroUpdate(
            @RequestParam String email) {

        String token = UUID.randomUUID().toString();

        mailSender.send(enviarEmail(email, token));
        return ResponseEntity.ok("E-mail para recuperação enviado");
    }


    @GetMapping("/teste")
    public ResponseEntity<?> Batata(
        //@RequestParam String email //Vira @GetMapping(/teste?email=teste@gmail.com)
    ){
        //com @ResquestParam para ?
        String sql = """
                SELECT email FROM users WHERE email = ?
                """;

        String sql2 = """
                SELECT email FROM users WHERE id = 2
                """;
        return ResponseEntity.ok(jdbcTemplate.queryForObject(sql2, String.class));//, email));
    }
}