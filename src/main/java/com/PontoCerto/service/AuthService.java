package com.PontoCerto.service;

import com.PontoCerto.models.Usuario;
import com.PontoCerto.repository.UsuarioRepository;
import com.PontoCerto.security.JwtUtil;
import com.PontoCerto.security.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private enviarEmail emailService;

    public String login(String email, String senha) throws AuthenticationException {
        System.out.println("Tentando autenticar: " + email);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, senha)
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails.getUsername());
    }
    public void enviarTokenRedefinicaoSenha(String email) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        if (usuarioOpt.isEmpty()) return; // Segurança: não revela se email existe

        Usuario usuario = usuarioOpt.get();
        String token = UUID.randomUUID().toString();

        usuario.setTokenResetSenha(token);
        usuario.setTokenExpiracao(LocalDateTime.now().plusHours(1));
        usuarioRepository.save(usuario);

        // Carrega o HTML do template (supondo que está em src/main/resources/templates/reset-password.html)
        String htmlTemplate;
        try {
            htmlTemplate = Files.readString(Path.of("src/main/resources/templates/reset-password.html"));
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar template de e-mail", e);
        }

        emailService.enviarEmail(
                email,
                "Redefinição de senha - Ponto Certo",
                token,
                htmlTemplate
        );

        System.out.println("Token de redefinição de senha: " + token);
    }

    public void resetarSenhaComToken(String token, String novaSenha) {
        Usuario usuario = usuarioRepository.findByTokenResetSenha(token)
                .orElseThrow(() -> new RuntimeException("Token inválido."));

        if (usuario.getTokenExpiracao().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expirado.");
        }

        usuario.setSenha(passwordEncoder.encode(novaSenha));
        usuario.setTokenResetSenha(null);
        usuario.setTokenExpiracao(null);
        usuarioRepository.save(usuario);
    }
}
