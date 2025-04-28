package com.PontoCerto.controller;

import com.PontoCerto.dto.AuthRequest;
import com.PontoCerto.dto.AuthResponse;
import com.PontoCerto.dto.ForgotPasswordRequest;
import com.PontoCerto.dto.ResetPasswordRequest;
import com.PontoCerto.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        String token = authService.login(request.getEmail(), request.getSenha());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/esqueci-senha")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.enviarTokenRedefinicaoSenha(request.getEmail());
        return ResponseEntity.ok("Se o e-mail existir, um link de redefinição foi enviado.");
    }

    @PostMapping("/resetar-senha")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetarSenhaComToken(request.getToken(), request.getNovaSenha());
        return ResponseEntity.ok("Senha redefinida com sucesso.");
    }
}
