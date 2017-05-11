package net.felsing.client_cert;


//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import javax.servlet.*;
import java.io.IOException;


public class CharsetFilter implements Filter {
    //private static final Logger logger = LoggerFactory.getLogger(CharsetFilter.class);
    private String encoding;


    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");
        if (encoding == null) encoding = "UTF-8";
    }


    public void doFilter(ServletRequest request, ServletResponse response, FilterChain next)
            throws IOException, ServletException {

        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(encoding);
        }

        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        next.doFilter(request, response);
    }


    public void destroy() {

    }

} // class
