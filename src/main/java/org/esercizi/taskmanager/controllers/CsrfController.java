package org.esercizi.taskmanager.controllers;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CsrfController {

    @GetMapping("/csrf")
    public CsrfToken getToken(
            CsrfToken csrfToken
    ) {
        return csrfToken;
    }

}
