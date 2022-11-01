package com.ishaan.app2022.server.controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.ishaan.app2022.server.config.JwtTokenUtil;
import com.ishaan.app2022.server.config.JwtUserDetailsService;
import com.ishaan.app2022.server.objects.UserObject;
import com.ishaan.app2022.server.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Value("${google.oauth.clientid}")
    private String GOOGLE_ID;

    @Value("${google.oauth.serverid}")
    private String SERVER_ID;

    public static final String EMPTY_STRING = "";

    private static final HttpTransport TRANSPORT = new NetHttpTransport();

    private static final JsonFactory FACTORY = new GsonFactory();

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticateUserWithGoogle(@RequestBody AuthRequest authBody) throws GeneralSecurityException, IOException {
        String googleTok = authBody.getGoogleToken();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(TRANSPORT, FACTORY)
                .setAudience(Arrays.asList(GOOGLE_ID, SERVER_ID)).build();

        GoogleIdToken idTok = verifier.verify(googleTok);
        if (Objects.isNull(idTok)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("msg", "Invalid google ID Token"));
        }

        GoogleIdToken.Payload payload = idTok.getPayload();

        String email = payload.getEmail();

        try {
            UserDetails user = jwtUserDetailsService.loadUserByUsername(email);
            // since this user exists, we can now use the data to generate the token
            String jwtTok = jwtTokenUtil.generateJWT(user);
            return ResponseEntity.ok(Collections.singletonMap("token", jwtTok));
        } catch (UsernameNotFoundException e) {
            // If the username was not found, we must register the user
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("last_name");

            UserObject createdUser = new UserObject(email, firstName, lastName);
            userRepository.save(createdUser);

            // Now we can generate the token
            String jwtTok = jwtTokenUtil.generateJWT(new User(email, EMPTY_STRING, Collections.emptyList()));
            return ResponseEntity.status(HttpStatus.CREATED).body(Collections.singletonMap("token", jwtTok));
        }
    }

    @NoArgsConstructor
    @AllArgsConstructor
    private static class AuthRequest {
        @Getter
        private String googleToken;
    }
}
