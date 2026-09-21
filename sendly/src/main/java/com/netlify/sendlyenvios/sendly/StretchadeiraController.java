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
@RequestMapping("/api/stretchadeiras")
public class StretchadeiraController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping
    public ResponseEntity<?> listar() {
        try {
            String sql = """
                    SELECT s.id, s.nome, s.descricao, s.ativo, s.status_operacional,
                           s.filial_id, f.codigo AS filial_codigo, f.nome AS filial_nome,
                           s.box_id, b.nome AS box_nome,
                           s.created_at, s.updated_at
                    FROM stretchadeiras s
                    LEFT JOIN filiais f ON s.filial_id = f.id
                    LEFT JOIN boxes b ON s.box_id = b.id
                    ORDER BY s.id ASC
                    """;
            List<Map<String, Object>> lista = jdbcTemplate.queryForList(sql);
            return ResponseEntity.ok(lista);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao listar stretchadeiras: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable int id) {
        try {
            String sql = """
                    SELECT s.id, s.nome, s.descricao, s.ativo, s.status_operacional,
                           s.filial_id, f.codigo AS filial_codigo, f.nome AS filial_nome,
                           s.box_id, b.nome AS box_nome,
                           s.created_at, s.updated_at
                    FROM stretchadeiras s
                    LEFT JOIN filiais f ON s.filial_id = f.id
                    LEFT JOIN boxes b ON s.box_id = b.id
                    WHERE s.id = ?
                    """;
            Map<String, Object> item = jdbcTemplate.queryForMap(sql, id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Stretchadeira não encontrada.");
            return ResponseEntity.status(404).body(erro);
        }
    }

    @PostMapping
    public ResponseEntity<?> criar(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Integer ativo,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            if (body != null) {
                if (nome == null && body.containsKey("nome")) nome = (String) body.get("nome");
                if (descricao == null && body.containsKey("descricao")) descricao = (String) body.get("descricao");
                if (ativo == null && body.containsKey("ativo")) {
                    Object val = body.get("ativo");
                    if (val instanceof Boolean) ativo = (Boolean) val ? 1 : 0;
                    else if (val instanceof Number) ativo = ((Number) val).intValue();
                }
            }

            if (nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "O nome da stretchadeira é obrigatório.");
                return ResponseEntity.badRequest().body(erro);
            }

            int ativoFinal = (ativo != null) ? ativo : 1;
            String sql = "INSERT INTO stretchadeiras (nome, descricao, ativo, status_operacional) VALUES (?, ?, ?, 'LIVRE')";
            jdbcTemplate.update(sql, nome.trim(), descricao != null ? descricao.trim() : null, ativoFinal);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Stretchadeira cadastrada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao cadastrar stretchadeira: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(
            @PathVariable int id,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String descricao,
            @RequestParam(required = false) Integer ativo,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            if (body != null) {
                if (nome == null && body.containsKey("nome")) nome = (String) body.get("nome");
                if (descricao == null && body.containsKey("descricao")) descricao = (String) body.get("descricao");
                if (ativo == null && body.containsKey("ativo")) {
                    Object val = body.get("ativo");
                    if (val instanceof Boolean) ativo = (Boolean) val ? 1 : 0;
                    else if (val instanceof Number) ativo = ((Number) val).intValue();
                }
            }

            if (nome == null || nome.trim().isEmpty()) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "O nome da stretchadeira é obrigatório.");
                return ResponseEntity.badRequest().body(erro);
            }

            int ativoFinal = (ativo != null) ? ativo : 1;
            String sql = "UPDATE stretchadeiras SET nome = ?, descricao = ?, ativo = ? WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, nome.trim(), descricao != null ? descricao.trim() : null, ativoFinal, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Stretchadeira não encontrada para atualização.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Stretchadeira atualizada com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar stretchadeira: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatusOperacional(
            @PathVariable int id,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer filialId,
            @RequestParam(required = false) Integer boxId,
            @RequestBody(required = false) Map<String, Object> body) {
        try {
            if (body != null) {
                if (status == null && body.containsKey("status")) status = (String) body.get("status");
                if (filialId == null && body.containsKey("filialId")) {
                    Object fVal = body.get("filialId");
                    if (fVal instanceof Number) filialId = ((Number) fVal).intValue();
                    else if (fVal instanceof String && !((String) fVal).isEmpty()) filialId = Integer.parseInt((String) fVal);
                }
                if (boxId == null && body.containsKey("boxId")) {
                    Object bVal = body.get("boxId");
                    if (bVal instanceof Number) boxId = ((Number) bVal).intValue();
                    else if (bVal instanceof String && !((String) bVal).isEmpty()) boxId = Integer.parseInt((String) bVal);
                }
            }

            if (status == null || (!status.equalsIgnoreCase("LIVRE") && !status.equalsIgnoreCase("OCUPADA"))) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Status operacional inválido. Deve ser 'LIVRE' ou 'OCUPADA'.");
                return ResponseEntity.badRequest().body(erro);
            }

            status = status.toUpperCase();

            // Verificar se a stretchadeira está ATIVA
            String checkAtivoSql = "SELECT ativo FROM stretchadeiras WHERE id = ?";
            Integer ativo = jdbcTemplate.queryForObject(checkAtivoSql, Integer.class, id);
            if (ativo != null && ativo == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Não é possível alterar o status de uma stretchadeira INATIVA.");
                return ResponseEntity.badRequest().body(erro);
            }

            if (status.equals("OCUPADA")) {
                if (filialId == null || boxId == null) {
                    Map<String, String> erro = new HashMap<>();
                    erro.put("erro", "Para marcar como OCUPADA, informe a filial e o BOX de destino.");
                    return ResponseEntity.badRequest().body(erro);
                }

                String sql = "UPDATE stretchadeiras SET status_operacional = 'OCUPADA', filial_id = ?, box_id = ? WHERE id = ?";
                jdbcTemplate.update(sql, filialId, boxId, id);
            } else {
                // LIVRE -> desvincula filial e box
                String sql = "UPDATE stretchadeiras SET status_operacional = 'LIVRE', filial_id = NULL, box_id = NULL WHERE id = ?";
                jdbcTemplate.update(sql, id);
            }

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Status da stretchadeira atualizado para " + status + ".");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao atualizar status: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @PutMapping("/{id}/ativar")
    public ResponseEntity<?> alternarAtivacao(@PathVariable int id) {
        try {
            String checkSql = "SELECT ativo FROM stretchadeiras WHERE id = ?";
            Integer ativoAtual = jdbcTemplate.queryForObject(checkSql, Integer.class, id);

            if (ativoAtual == null) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Stretchadeira não encontrada.");
                return ResponseEntity.status(404).body(erro);
            }

            int novoAtivo = (ativoAtual == 1) ? 0 : 1;
            // Se estiver desativando e estava ocupada, desocupa e desvincula
            String sql = (novoAtivo == 0)
                    ? "UPDATE stretchadeiras SET ativo = 0, status_operacional = 'LIVRE', filial_id = NULL, box_id = NULL WHERE id = ?"
                    : "UPDATE stretchadeiras SET ativo = 1, status_operacional = 'LIVRE' WHERE id = ?";

            jdbcTemplate.update(sql, id);

            Map<String, Object> resposta = new HashMap<>();
            resposta.put("mensagem", "Stretchadeira " + (novoAtivo == 1 ? "ativada" : "desativada") + " com sucesso.");
            resposta.put("ativo", novoAtivo);
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao alternar ativação: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> excluir(@PathVariable int id) {
        try {
            String sql = "DELETE FROM stretchadeiras WHERE id = ?";
            int linhas = jdbcTemplate.update(sql, id);

            if (linhas == 0) {
                Map<String, String> erro = new HashMap<>();
                erro.put("erro", "Stretchadeira não encontrada.");
                return ResponseEntity.status(404).body(erro);
            }

            Map<String, String> resposta = new HashMap<>();
            resposta.put("mensagem", "Stretchadeira excluída com sucesso.");
            return ResponseEntity.ok(resposta);
        } catch (Exception e) {
            Map<String, String> erro = new HashMap<>();
            erro.put("erro", "Erro ao excluir stretchadeira: " + e.getMessage());
            return ResponseEntity.status(500).body(erro);
        }
    }
}
