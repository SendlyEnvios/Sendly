package com.netlify.sendlyenvios.sendly;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/boxes")
public class BoxController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            String sql = "SELECT id, nome, descricao, created_at, updated_at FROM boxes ORDER BY id ASC";
            List<Map<String, Object>> boxes = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(boxes);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar boxes: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            String sql = "SELECT id, nome, descricao, created_at, updated_at FROM boxes WHERE id = ?";
            Map<String, Object> box = jdbcTemplate.queryForMap(sql, id);
            return ResponseEntity.ok(box);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "BOX não encontrado.");
            return ResponseEntity.status(404).body(erro);
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (nome == null && body.containsKey("nome")) nome = body.get("nome");
                if (descricao == null && body.containsKey("descricao")) descricao = body.get("descricao");
            }

            if (nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "O nome/identificação do BOX é obrigatório.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "INSERT INTO boxes (nome, descricao) VALUES (?, ?)";
            jdbcTemplate.update(sql, nome.trim(), descricao != null ? descricao.trim() : null);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "BOX cadastrado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao cadastrar BOX: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (nome == null && body.containsKey("nome")) nome = body.get("nome");
                if (descricao == null && body.containsKey("descricao")) descricao = body.get("descricao");
            }

            if (nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "O nome/identificação do BOX é obrigatório.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "UPDATE boxes SET nome = ?, descricao = ? WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, nome.trim(), descricao != null ? descricao.trim() : null, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "BOX não encontrado para atualização.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "BOX atualizado com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar BOX: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM stretchadeiras WHERE box_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            if (count != null && count > 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Não é possível excluir: BOX vinculado a stretchadeira em processamento.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "DELETE FROM boxes WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "BOX não encontrado.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "BOX excluído com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao excluir BOX: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }
}
