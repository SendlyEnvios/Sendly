package com.netlify.sendlyenvios.sendly;

import com.resend.core.exception.ResendException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Map;
import java.util.UUID;

import static com.netlify.sendlyenvios.sendly.Controller.*;

@RestController
@CrossOrigin("*")
public class Service {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/cadastro")
    public ResponseEntity<?> cadastro(
            @RequestParam String email,
            @RequestParam String password) {

        try {
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

            String sql = """
                SELECT id, email, password FROM users
                WHERE email = ?
                """;

            Map<String, Object> user = jdbcTemplate.queryForMap(sql, email);

            if (encoder.matches(password, (String) user.get("password"))) {
                user.remove("password");
                return ResponseEntity.ok(user);
            }

            return ResponseEntity.ok(noUser());

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
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String passwordHash = encoder.encode(password);

            String sql = """
                INSERT INTO users(name, email, password, telefone) VALUES(?,?,?,?)
                """;



            return ResponseEntity.ok(jdbcTemplate.update(sql, name, email, passwordHash, telefone));
        }catch(Exception e){
            return ResponseEntity.status(500).body("Erro ao criar usuário: " + e.getMessage());
        }
    }

    @PostMapping("/cadastroUpdate")
    public ResponseEntity<?> cadastroUpdate(
            @RequestParam String email) {
        try {
            String token = UUID.randomUUID().toString();
            String sql = """
                    UPDATE users SET token = ? WHERE email = ?
                    """;
            jdbcTemplate.update(sql, token, email);
            enviarEmail(email, token);
            return ResponseEntity.ok("index");
        } catch (ResendException e) {
            return ResponseEntity.status(500).body("Erro ao enviar email" + e.getMessage());
        }
    }
    @GetMapping("/cadastroUpdate2")
    public ResponseEntity<?> cadastroUpdate2(
            @RequestParam String token) {
        try {
            String sql = """
                    SELECT id FROM users WHERE token = ?
                    """;
            Map<String, Object> user = jdbcTemplate.queryForMap(sql, token);

            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Token inválido");
        }
    }
}