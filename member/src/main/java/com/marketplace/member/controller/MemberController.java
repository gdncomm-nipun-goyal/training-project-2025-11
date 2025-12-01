package com.marketplace.member.controller;

import com.marketplace.member.dto.LoginRequest;
import com.marketplace.member.dto.MemberResponse;
import com.marketplace.member.dto.RegisterRequest;
import com.marketplace.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    public ResponseEntity<MemberResponse> register(@Valid @RequestBody RegisterRequest request) {
        MemberResponse response = memberService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberResponse> login(@Valid @RequestBody LoginRequest request) {
        MemberResponse response = memberService.login(request);
        return ResponseEntity.ok(response);
    }
}

