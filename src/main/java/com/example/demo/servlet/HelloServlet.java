package com.example.demo.servlet;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

import java.io.PrintWriter;

@WebServlet(urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {

        try {
            response.setStatus(200);
            PrintWriter writer = response.getWriter();
            writer.write("Hello from Servlet!");
            writer.flush();
        } catch (Exception e) {
            // ignore
        }
    }
}