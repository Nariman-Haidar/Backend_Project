package server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.PathContainer;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.ReactiveAuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authorization.AuthorizationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.util.pattern.PathPattern;
import org.springframework.web.util.pattern.PathPatternParser;
import reactor.core.publisher.Mono;
import server.service.AccessService;
import server.service.AdministratorService;

@Component
public class CourseAccessManager implements ReactiveAuthorizationManager<AuthorizationContext> {

    private final AccessService accessService;
    private final AdministratorService administratorService;
    @Autowired
    public CourseAccessManager(AccessService accessService, AdministratorService administratorService) {
        this.accessService = accessService;
        this.administratorService = administratorService;
    }

    @Override
    public Mono<AuthorizationDecision> check(Mono<Authentication> authentication, AuthorizationContext context) {
        return authentication
                .flatMap(auth -> checkCourseAccess(auth, context))
                .map(hasAccess -> new AuthorizationDecision(hasAccess.isGranted()))
                .defaultIfEmpty(new AuthorizationDecision(false));
    }
    private Mono<AuthorizationDecision> checkCourseAccess(Authentication auth, AuthorizationContext context) {
        PathContainer pathContainer = context.getExchange().getRequest().getPath().pathWithinApplication();

        PathPatternParser pathPatternParser = new PathPatternParser();
        PathPattern pathPattern = pathPatternParser.parse("/api/queue-items/{courseId}");

        PathPattern.PathMatchInfo pathMatchInfo = pathPattern.matchAndExtract(pathContainer);

        if (pathMatchInfo != null) {
            String courseId = pathMatchInfo.getUriVariables().get("courseId");

        

            return accessService.hasCourseAccess(auth, courseId)
                    .flatMap(hasUserAccess -> {
                        boolean hasUserRole  = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"));
                        boolean hasAdminRole = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

                        if (hasUserRole) {
                            return accessService.hasCourseAccess(auth, courseId)
                                    .map(AuthorizationDecision::new);
                        } else if (hasAdminRole) {
                            return administratorService.hasCourseAccess(auth, courseId)
                                     .map(AuthorizationDecision::new);

                        } else {
                            return Mono.just(new AuthorizationDecision(false));
                        }
                    })
                    .switchIfEmpty(Mono.just(new AuthorizationDecision(false)));

        } else {
            System.out.println("Invalid path. Unable to extract courseId.");
            return Mono.just(new AuthorizationDecision(false));
        }
    }

}

