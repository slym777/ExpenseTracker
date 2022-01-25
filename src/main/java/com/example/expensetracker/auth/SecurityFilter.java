package com.example.expensetracker.auth;

import com.example.expensetracker.auth.models.Credentials;
import com.example.expensetracker.auth.models.SecurityProperties;
import com.example.expensetracker.auth.models.UserPrincipal;
import com.example.expensetracker.utils.CookieUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    @Autowired
    SecurityService securityService;

    @Autowired
    SecurityProperties securityProperties;

    @Autowired
    CookieUtils cookieUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        verifyToken(request);
        filterChain.doFilter(request, response);
    }

    private void verifyToken(HttpServletRequest request) {
        String session = null;
        FirebaseToken decodedToken = null;
        Credentials.CredentialType credentialType = null;

        boolean strictServerSessionEnabled = securityProperties.getFirebaseProperties().isEnableStrictServerSession();
        Cookie sessionCookie = cookieUtils.getCookie("session");
        String token = securityService.getBearerToken(request);
        logger.info(token);
        try {
            if (sessionCookie != null) {
                session = sessionCookie.getValue();
                decodedToken = FirebaseAuth.getInstance().verifySessionCookie(session,
                        securityProperties.getFirebaseProperties().isEnableCheckSessionRevoked());
                credentialType = Credentials.CredentialType.SESSION;
            } else if (!strictServerSessionEnabled) {

                if (token != null && !token.equalsIgnoreCase("undefined")) {
                    decodedToken = FirebaseAuth.getInstance().verifyIdToken(token);
                    credentialType = Credentials.CredentialType.ID_TOKEN;
                }
            }
        } catch (FirebaseAuthException e) {
            e.printStackTrace();
            logger.error("Firebase exception: " + e.getLocalizedMessage());
        }

        UserPrincipal userPrincipal = firebaseTokenToUserDto(decodedToken);
        if (userPrincipal != null) {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userPrincipal, new Credentials(credentialType, decodedToken, token, session), null
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }

    private UserPrincipal firebaseTokenToUserDto(FirebaseToken decodedToken) {
        UserPrincipal userPrincipal = null;
        if (decodedToken != null) {
            userPrincipal = new UserPrincipal(
                    decodedToken.getUid(),
                    decodedToken.getName(),
                    decodedToken.getEmail(),
                    decodedToken.isEmailVerified(),
                    decodedToken.getIssuer(),
                    decodedToken.getPicture()
            );
        }

        return userPrincipal;
    }
}
