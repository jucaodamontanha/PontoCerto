package com.PontoCerto.controller;

import com.PontoCerto.dto.UsuarioRequest;
import com.PontoCerto.dto.UsuarioResponse;
import com.PontoCerto.models.Empresa;
import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.EmpresaRepository;
import com.PontoCerto.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmpresaRepository empresaRepository;

    // Cadastrar usuário
    @PostMapping("/cadastrar")
    public ResponseEntity<?> cadastrar(@RequestBody UsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        // Buscar a empresa pelo nome
        Empresa empresa = empresaRepository.findByNome(request.getNomeEmpresa())
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada"));

        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());
        usuario.setEmpresa(empresa); // Associa o usuário à empresa

        usuarioRepository.save(usuario);

        return ResponseEntity.ok("Usuário cadastrado com sucesso");
    }

    @GetMapping("/listar")
    public ResponseEntity<List<UsuarioResponse>> listarUsuarios(Principal principal) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        List<Usuario> usuariosDaMesmaEmpresa = usuarioRepository.findByEmpresa(usuarioLogado.getEmpresa());

        List<UsuarioResponse> response = usuariosDaMesmaEmpresa.stream()
                .map(usuario -> new UsuarioResponse(
                        usuario.getId(),
                        usuario.getNome(),
                        usuario.getEmail(),
                        usuario.getRole().name(),
                        usuario.getEmpresa().getNome()
                ))
                .toList();

        return ResponseEntity.ok(response);
    }

    // Listar usuários de uma empresa específica
    @GetMapping("/listar/{empresaId}")
    public ResponseEntity<List<Usuario>> listarUsuariosPorEmpresa(@PathVariable Long empresaId) {
        List<Usuario> usuarios = usuarioRepository.findByEmpresa_Id(empresaId);
        return ResponseEntity.ok(usuarios);
    }

    // Buscar usuário por ID
    @GetMapping("/buscar/{id}")
    public ResponseEntity<Usuario> buscarUsuarioPorId(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return ResponseEntity.ok(usuario);
    }

    // Atualizar usuário
    @PutMapping("/atualizar/{id}")
    public ResponseEntity<Usuario> atualizarUsuario(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setRole(request.getRole());

        usuarioRepository.save(usuario);

        return ResponseEntity.ok(usuario);
    }

    // Deletar usuário
    @DeleteMapping("/deletar/{id}")
    public ResponseEntity<String> deletarUsuario(@PathVariable Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuarioRepository.delete(usuario);
        return ResponseEntity.ok("Usuário deletado com sucesso");
    }
}
