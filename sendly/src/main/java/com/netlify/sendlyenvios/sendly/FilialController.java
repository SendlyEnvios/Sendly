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
@RequestMapping("/api/filiais")
public class FilialController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            String sql = "SELECT id, codigo, nome, cidade, created_at, updated_at FROM filiais ORDER BY id DESC";
            List<Map<String, Object>> filiais = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(filiais);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar filiais: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            String sql = "SELECT id, codigo, nome, cidade, created_at, updated_at FROM filiais WHERE id = ?";
            Map<String, Object> filial = jdbcTemplate.queryForMap(sql, id);
            return ResponseEntity.ok(filial);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Filial não encontrada.");
            return ResponseEntity.status(404).body(erro);
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (codigo == null && body.containsKey("codigo")) codigo = body.get("codigo");
                if (nome == null && body.containsKey("nome")) nome = body.get("nome");
                if (cidade == null && body.containsKey("cidade")) cidade = body.get("cidade");
            }

            if (codigo == null || codigo.trim().isEmpty() || nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Código e nome da filial são obrigatórios.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "INSERT INTO filiais (codigo, nome, cidade) VALUES (?, ?, ?)";
            jdbcTemplate.update(sql, codigo.trim(), nome.trim(), cidade != null ? cidade.trim() : null);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Filial cadastrada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao cadastrar filial: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestParam(required = false) String codigo,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cidade,
            @RequestBody(required = false) Map<String, String> body) {
        try {
            if (body != null) {
                if (codigo == null && body.containsKey("codigo")) codigo = body.get("codigo");
                if (nome == null && body.containsKey("nome")) nome = body.get("nome");
                if (cidade == null && body.containsKey("cidade")) cidade = body.get("cidade");
            }

            if (codigo == null || codigo.trim().isEmpty() || nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Código e nome da filial são obrigatórios.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "UPDATE filiais SET codigo = ?, nome = ?, cidade = ? WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, codigo.trim(), nome.trim(), cidade != null ? cidade.trim() : null, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Filial não encontrada para atualização.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Filial atualizada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar filial: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            String checkSql = "SELECT COUNT(*) FROM stretchadeiras WHERE filial_id = ?";
            Integer count = jdbcTemplate.queryForObject(checkSql, Integer.class, id);
            if (count != null && count > 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Não é possível excluir: filial vinculada a stretchadeira em processamento.");
                return ResponseEntity.badRequest().body(erro);
            }

            String sql = "DELETE FROM filiais WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Filial não encontrada.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Filial excluída com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao excluir filial: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }
}
