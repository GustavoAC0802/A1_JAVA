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

import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.modelo.DocumentoAtualizador;
import com.autobots.automanager.modelo.DocumentoSelecionador;
import com.autobots.automanager.repositorios.DocumentoRepositorio;

@RestController
@RequestMapping("/documento")
public class DocumentoControle {

    @Autowired
    private DocumentoRepositorio repositorio;

    @Autowired
    private DocumentoSelecionador selecionador;

    @GetMapping("/{id}")
    public ResponseEntity<?> obterDocumento(@PathVariable long id) {
        List<Documento> documentos = repositorio.findAll();
        Documento documento = selecionador.selecionar(documentos, id);
        if (documento != null) {
            return ResponseEntity.ok(documento);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
        }
    }

    @GetMapping("/documentos")
    public ResponseEntity<List<Documento>> obterDocumentos() {
        List<Documento> documentos = repositorio.findAll();
        return ResponseEntity.ok(documentos);
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrarDocumento(@RequestBody Documento documento) {
        repositorio.save(documento);
        return ResponseEntity.status(HttpStatus.CREATED).body("Documento cadastrado com sucesso.");
    }

    @PutMapping("/atualizar")
    public ResponseEntity<String> atualizarDocumento(@RequestBody Documento atualizacao) {
        if (repositorio.existsById(atualizacao.getId())) {
            Documento documento = repositorio.getById(atualizacao.getId());
            DocumentoAtualizador atualizador = new DocumentoAtualizador();
            atualizador.atualizar(documento, atualizacao);
            repositorio.save(documento);
            return ResponseEntity.ok("Documento atualizado com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
        }
    }

    @DeleteMapping("/excluir")
    public ResponseEntity<String> excluirDocumento(@RequestBody Documento exclusao) {
        if (repositorio.existsById(exclusao.getId())) {
            Documento documento = repositorio.getById(exclusao.getId());
            repositorio.delete(documento);
            return ResponseEntity.ok("Documento excluído com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Documento não encontrado.");
        }
    }
}
