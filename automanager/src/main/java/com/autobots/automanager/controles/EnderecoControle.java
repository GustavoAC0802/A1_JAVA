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

import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.modelo.EnderecoAtualizador;
import com.autobots.automanager.modelo.EnderecoSelecionador;
import com.autobots.automanager.repositorios.EnderecoRepositorio;

@RestController
@RequestMapping("/endereco")
public class EnderecoControle {

    @Autowired
    private EnderecoRepositorio repositorio;

    @Autowired
    private EnderecoSelecionador selecionador;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterEndereco(@PathVariable long id) {
        List<Endereco> enderecos = repositorio.findAll();
        Endereco endereco = selecionador.selecionar(enderecos, id);
        if (endereco != null) {
            return ResponseEntity.ok(endereco);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado.");
        }
    }

    @GetMapping("/enderecos")
    public ResponseEntity<List<Endereco>> obterEnderecos() {
        List<Endereco> enderecos = repositorio.findAll();
        return ResponseEntity.ok(enderecos);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarEndereco(@RequestBody Endereco endereco) {
        repositorio.save(endereco);
        return ResponseEntity.status(HttpStatus.CREATED).body("Endereço cadastrado com sucesso.");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarEndereco(@RequestBody Endereco atualizacao) {
        if (repositorio.existsById(atualizacao.getId())) {
            Endereco endereco = repositorio.getById(atualizacao.getId());
            EnderecoAtualizador atualizador = new EnderecoAtualizador();
            atualizador.atualizar(endereco, atualizacao);
            repositorio.save(endereco);
            return ResponseEntity.ok("Endereço atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado.");
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluirEndereco(@RequestBody Endereco exclusao) {
        if (repositorio.existsById(exclusao.getId())) {
            Endereco endereco = repositorio.getById(exclusao.getId());
            repositorio.delete(endereco);
            return ResponseEntity.ok("Endereço excluído com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endereço não encontrado.");
        }
    }
}