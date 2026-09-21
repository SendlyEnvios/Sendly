package com.netlify.sendlyenvios.sendly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            // NUNCA expor password, token ou dados sensíveis
            String sql = "SELECT id, name, firstName, email, telefone, endereco, iconPerfil FROM users ORDER BY id ASC";
            List<Map<String, Object>> usuarios = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(usuarios);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar usuários: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            String sql = "SELECT id, name, firstName, email, telefone, endereco, iconPerfil FROM users WHERE id = ?";
            Map<String, Object> usuario = jdbcTemplate.queryForMap(sql, id);
            return ResponseEntity.ok(usuario);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Usuário não encontrado.");
            return ResponseEntity.status(404).body(erro);
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String endereco,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (name == null && body.containsKey("name")) name = body.get("name");
                if (email == null && body.containsKey("email")) email = body.get("email");
                if (password == null && body.containsKey("password")) password = body.get("password");
                if (telefone == null && body.containsKey("telefone")) telefone = body.get("telefone");
                if (endereco == null && body.containsKey("endereco")) endereco = body.get("endereco");
            }

            if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty() || password == null || password.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Nome, email e senha são obrigatórios.");
                return ResponseEntity.badRequest().body(erro);
            }

            // Verificar se o email já existe
            String checkSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, email.trim());
            if (count != null && count > 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Já existe um usuário cadastrado com este e-mail.");
                return ResponseEntity.badRequest().body(erro);
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashSenha = encoder.encode(password.trim());

            String sql = "INSERT INTO users (name, email, password, telefone, endereco) VALUES (?, ?, ?, ?, ?)";
            jdbcTemplate.update(sql, name.trim(), email.trim(), hashSenha, telefone != null ? telefone.trim() : null, endereco != null ? endereco.trim() : null);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário cadastrado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao cadastrar usuário: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password,
            @RequestParam(required = false) String telefone,
            @RequestParam(required = false) String endereco,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (name == null && body.containsKey("name")) name = body.get("name");
                if (email == null && body.containsKey("email")) email = body.get("email");
                if (password == null && body.containsKey("password")) password = body.get("password");
                if (telefone == null && body.containsKey("telefone")) telefone = body.get("telefone");
                if (endereco == null && body.containsKey("endereco")) endereco = body.get("endereco");
            }

            if (name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Nome e e-mail são obrigatórios.");
                return ResponseEntity.badRequest().body(erro);
            }

            int linhas;
            if (password != null && !password.trim().isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                String hashSenha = encoder.encode(password.trim());
                String sql = "UPDATE users SET name = ?, email = ?, password = ?, telefone = ?, endereco = ? WHERE id = ?";
                linhas = jdbcTemplate.update(sql, name.trim(), email.trim(), hashSenha, telefone != null ? telefone.trim() : null, endereco != null ? endereco.trim() : null, id);
            } else {
                String sql = "UPDATE users SET name = ?, email = ?, telefone = ?, endereco = ? WHERE id = ?";
                linhas = jdbcTemplate.update(sql, name.trim(), email.trim(), telefone != null ? telefone.trim() : null, endereco != null ? endereco.trim() : null, id);
            }

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário atualizado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar usuário: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            String sql = "DELETE FROM users WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Usuário não encontrado.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Usuário excluído com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao excluir usuário: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }
}
