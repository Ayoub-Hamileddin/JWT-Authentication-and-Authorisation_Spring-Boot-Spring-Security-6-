package com.eyuup.security.service.impl;



import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eyuup.security.config.JwtService;
import com.eyuup.security.payload.request.LoginRequest;
import com.eyuup.security.payload.request.RegisterRequest;
import com.eyuup.security.payload.response.LoginResponse;
import com.eyuup.security.payload.response.RegisterResponse;
import com.eyuup.security.repository.UserRepository;
import com.eyuup.security.service.AuthService;
import com.eyuup.security.user.Role;
import com.eyuup.security.user.User;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    @Override
    public RegisterResponse register(RegisterRequest request) {

        var user=User.builder()
        .firstName(request.getFirstName())
        .LastName(request.getLastName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.USER)
        .build();

        userRepository.save(user);

        String jwttoken=jwtService.generateToken(null, user);
      return RegisterResponse.builder()
                .token(jwttoken)
            .build();
    }




    @Override
    public LoginResponse Login(LoginRequest request) {
        authenticationManager.authenticate(
           new UsernamePasswordAuthenticationToken(
             request.getEmail(),
            request.getPassword())
        );
          var user =userRepository.findByEmail(request.getEmail()).orElseThrow();
          String jwttoken=jwtService.generateToken(null, user);
      return LoginResponse.builder()
                .token(jwttoken)
            .build();
    }

}
