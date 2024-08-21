package com.homeycraft.product_catalog_service.filter;

import java.io.IOException;

import com.homeycraft.product_catalog_service.entity.UserContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class ProductFilter extends OncePerRequestFilter
{
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException
    {

        String header = request.getHeader("Authorization");
        System.out.println(header);
        //Bearer eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MjIzMzI2MzQsInN1YiI6InRvbUBnbWFpbC5jb20ifQ.Q7-YJhIAu6igGLgR1EpcY7XH_pjcBPl5YzVAigu5-Zs

        if (header!=null  &&  !header.isBlank()  && header.length()>8 &&  header.startsWith("Bearer ")){
            String token =header.substring(7);

            try
            {

                Claims  claims = Jwts.parser().setSigningKey("FYNDNA-SUCCESS").parseClaimsJws(token).getBody();
                String emailId= claims.getSubject();
                String role = claims.get("role" , String.class);

                UserContext userContext = new UserContext(emailId , role);
                request.setAttribute("userContext", userContext);
                filterChain.doFilter(request, response);

                if (emailId!=null  && !emailId.isBlank())
                {
                    System.out.println("success");

                    filterChain.doFilter(request, response);
                }
                else
                {
                    response.sendError(HttpStatus.UNAUTHORIZED.value(),"Invalid Token...... ");

                }

            }
            catch (Exception e) {
                response.sendError(HttpStatus.UNAUTHORIZED.value(),"Invalid Token ");

            }

        }
        else {
            response.sendError(HttpStatus.UNAUTHORIZED.value(), "Token is Missing .......");
        }


    }

}
