package com.example.weatherweb;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Controller
public class CustomErrorController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {

        // Convert HttpServletRequest to WebRequest
        WebRequest webRequest = new ServletWebRequest(request);

        // Safely get error attributes
        Map<String, Object> errors = errorAttributes.getErrorAttributes(
            webRequest, ErrorAttributeOptions.of(
                ErrorAttributeOptions.Include.MESSAGE,
                ErrorAttributeOptions.Include.EXCEPTION
            )
        );

        model.addAttribute("timestamp", errors.get("timestamp"));
        model.addAttribute("status", errors.get("status"));
        model.addAttribute("error", errors.get("error"));
        model.addAttribute("message", errors.get("message"));
        model.addAttribute("path", errors.get("path"));

        return "error"; // Should match your error.html or error.jsp
    }
}
