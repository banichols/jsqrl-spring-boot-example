package org.jsqrl.security;

import org.jsqrl.model.SqrlUser;
import org.jsqrl.service.SqrlAuthenticationService;
import org.jsqrl.service.SqrlUserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Brent Nichols on 7/10/2016.
 *
 * The credentials of this auth provider should be the nut. As soon as the server has verified the nut
 * then the user can be considered authenticated.
 */
public class SqrlAuthenticationProvider implements AuthenticationProvider {

    private SqrlAuthenticationService authenticationService;
    private SqrlUserService userService;

    public SqrlAuthenticationProvider(SqrlAuthenticationService authenticationService,
                                      SqrlUserService userService){
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String nut = (String) authentication.getCredentials();
        String identityKey = authenticationService.getAuthenticatedSqrlIdentityKey(nut, request.getRemoteAddr());

        if(identityKey != null){
            //We found an authenticated SQRL user
            SqrlUser user = userService.getUserBySqrlKey(identityKey);
            List<GrantedAuthority> grantedAuthorities = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
            return new UsernamePasswordAuthenticationToken(user, authentication.getCredentials(), grantedAuthorities);
        }

        else return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
