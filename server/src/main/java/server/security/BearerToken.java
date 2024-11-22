package server.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
public class BearerToken extends AbstractAuthenticationToken {
    final private String tocken;

    public BearerToken(String tocken) {
        super(AuthorityUtils.NO_AUTHORITIES);
        this.tocken = tocken;
    }

    @Override
    public String getCredentials() {

        return this.tocken;
    }

    @Override
    public String getPrincipal() {
        return this.tocken;
    }
}
