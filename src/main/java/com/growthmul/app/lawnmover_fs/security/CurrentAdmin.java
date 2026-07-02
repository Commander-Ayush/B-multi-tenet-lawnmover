package com.growthmul.app.lawnmover_fs.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/**
 * The single place any /admin/** controller or service asks "who's making
 * this request, and which company do they belong to". Always reads from
 * the JWT-derived security context — never from a request parameter,
 * header, or anything else the client could set.
 */
public final class CurrentAdmin {

    private CurrentAdmin() {}

    public static AdminPrincipal get() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof AdminPrincipal principal)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        return principal;
    }

    public static Long companyId() {
        return get().getCompanyId();
    }
}
