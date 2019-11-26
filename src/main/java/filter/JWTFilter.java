package filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import model.RestErrorResponse;
import model.User;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang3.StringUtils;
import util.JwtTokenProvider;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JWTFilter implements Filter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private JwtTokenProvider tokenProvider = new JwtTokenProvider();

    @Override
    public void init(FilterConfig arg0) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);

        if (!StringUtils.isBlank(jwt)) {
            try {
                tokenProvider.validateToken(jwt);
            } catch (ExpiredJwtException e) {
                returnError(HttpStatus.SC_NOT_ACCEPTABLE, 408, "Token Expired", servletResponse);
                return;
            } catch (Exception e) {
                returnError(HttpStatus.SC_UNAUTHORIZED, 401, "Unauthorized", servletResponse);
                return;
            }

            User user = tokenProvider.parseJwtToken(jwt);

            servletRequest.setAttribute("user", user);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }


    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        return !StringUtils.isBlank(bearerToken) && bearerToken.startsWith("Bearer ") ?
                bearerToken.substring(7)
                : null;
    }

    private void returnError(int status,
                             int errorCode,
                             String message,
                             ServletResponse response) throws IOException {
        RestErrorResponse errorResponse = new RestErrorResponse(errorCode, message);

        byte[] responseToSend = new ObjectMapper().writeValueAsString(errorResponse).getBytes(StandardCharsets.UTF_8);
        ((HttpServletResponse) response).setHeader("Content-Type", "application/json; charset=UTF-8");
        ((HttpServletResponse) response).setStatus(status);
        response.getOutputStream().write(responseToSend);
    }
}
