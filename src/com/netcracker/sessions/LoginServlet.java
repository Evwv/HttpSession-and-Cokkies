package com.netcracker.sessions;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        signIn(login,password,req,resp);
    }

    public void signIn(String login, String password,HttpServletRequest req,HttpServletResponse resp) throws IOException {
        checkActiveSession(login, password,req,resp);
    }

    public void toWelclomePage(String login,HttpServletResponse resp) throws IOException {
        PrintWriter out = resp.getWriter();
        out.print("<html><body>");
        out.print("<h1>Welcome my dear, " + login + " </h1></br>");
        out.print("</body></html>");
        out.close();
    }

    public boolean checkValid(String login, String password,HttpServletResponse resp) throws IOException {

        boolean hasLogin = false;

        BufferedReader br = new BufferedReader(new FileReader(getServletContext().getRealPath("/users.csv")));
        String line;

        while ((line = br.readLine()) != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(line, ",");
            while (stringTokenizer.hasMoreTokens()) {
                if (login.equals(stringTokenizer.nextToken())) {
                    hasLogin = true;
                    if (password.equals(stringTokenizer.nextToken())) {
                        return true;
                    } else {
                        resp.sendError(404,"Password not correct");
                    }
                }
            }
        }
        if(!hasLogin){
            addUser(resp);
        }
        return false;
    }

    public void addUser(HttpServletResponse resp) throws IOException{
        resp.sendRedirect("/createNewUser.html");
    }

    public void checkActiveSession(String login, String password,HttpServletRequest req, HttpServletResponse resp) throws IOException{
        HttpSession session = req.getSession(true);


        if (!session.isNew() && password==""){
            toWelclomePage((String) session.getAttribute("login"),resp);
        }
        else if (checkValid(login,password,resp)){
                session.setAttribute("login",login);
                session.setAttribute("password",password);
                toWelclomePage(login,resp);
        }
    }
}


