package com.autobots.automanager.controles;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.modelo.TelefoneAtualizador;
import com.autobots.automanager.modelo.TelefoneSelecionador;
import com.autobots.automanager.repositorios.TelefoneRepositorio;

@RestController
@RequestMapping("/telefone")
public class TelefoneControle {

    @Autowired
    private TelefoneRepositorio repositorio;

    @Autowired
    private TelefoneSelecionador selecionador;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterTelefone(@PathVariable long id) {
        List<Telefone> telefones = repositorio.findAll();
        Telefone telefone = selecionador.selecionar(telefones, id);
        if (telefone != null) {
            return ResponseEntity.ok(telefone);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado.");
        }
    }

    @GetMapping("/telefones")
    public ResponseEntity<List<Telefone>> obterTelefones() {
        List<Telefone> telefones = repositorio.findAll();
        return ResponseEntity.ok(telefones);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarTelefone(@RequestBody Telefone telefone) {
        repositorio.save(telefone);
        return ResponseEntity.status(HttpStatus.CREATED).body("Telefone cadastrado com sucesso.");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarTelefone(@RequestBody Telefone atualizacao) {
        if (repositorio.existsById(atualizacao.getId())) {
            Telefone telefone = repositorio.getById(atualizacao.getId());
            TelefoneAtualizador atualizador = new TelefoneAtualizador();
            atualizador.atualizar(telefone, atualizacao);
            repositorio.save(telefone);
            return ResponseEntity.ok("Telefone atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado.");
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluirTelefone(@RequestBody Telefone exclusao) {
        if (repositorio.existsById(exclusao.getId())) {
            Telefone telefone = repositorio.getById(exclusao.getId());
            repositorio.delete(telefone);
            return ResponseEntity.ok("Telefone excluído com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Telefone não encontrado.");
        }
    }
}
