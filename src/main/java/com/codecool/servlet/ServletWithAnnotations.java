package com.codecool.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Vector;

@WebServlet(name = "simpleServlet", urlPatterns = {"/"}, loadOnStartup = 1)
public class ServletWithAnnotations extends HttpServlet {
    private Vector entries = new Vector();
    private long lastModified = 0;



    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        PrintWriter out = response.getWriter();

        response.setContentType("text/html");

        printHeader(out);
        printForm(out);
        printMessages(out);
        printFooter(out);

    }
    // Add a new entry, then dispatch back to doGet()
    public void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        handleForm(req, res);
        doGet(req, res);
    }

    private void printHeader(PrintWriter out) throws ServletException {
        out.println("<html><head><title>Guestbook</title></head>");
        out.println("<body>");
    }

    private void printForm(PrintWriter out) throws ServletException {
        out.println("<form method=post>");  // posts to itself
        out.println("<h3>Please submit your feedback:</h3>");
        out.println("Your name: <input type=text name=name><br>");
        out.println("Your email: <input type=text name=email><br>");
        out.println("Comment: <input type=text SIZE=50 NAME=comment><br>");
        out.println("<input type=submit value=\"Send Feedback\"><br>");
        out.println("</form>");
        out.println("<hr>");
    }

    private void printMessages(PrintWriter out) throws ServletException {
        String name, email, comment;

        Enumeration e = entries.elements();
        while (e.hasMoreElements()) {
            GuestbookEntry entry = (GuestbookEntry) e.nextElement();
            name = entry.name;
            if (name == null) name = "Unknown user";
            email = entry.email;
            if (name == null) email = "Unknown email";
            comment = entry.comment;
            if (comment == null) comment = "No comment";
            out.println("<dl>");
            out.println("<dt><b>" + name + "</b> (" + email + ") says");
            out.println("<dd><pre>" + comment + "</pr>");
            out.println("</dl>");

            // Sleep for half a second to simulate a slow data source
            try { Thread.sleep(500); } catch (InterruptedException ignored) { }
        }
    }

    private void printFooter(PrintWriter out) throws ServletException {
        out.println("</body>");
    }

    private void handleForm(HttpServletRequest req,
                            HttpServletResponse res) {
        GuestbookEntry entry = new GuestbookEntry();

        entry.name = req.getParameter("name");
        entry.email = req.getParameter("email");
        entry.comment = req.getParameter("comment");

        entries.addElement(entry);

        // Make note with a new last modified time
        lastModified = System.currentTimeMillis();
    }

    public long getLastModified(HttpServletRequest req) {
        return lastModified;
    }
    class GuestbookEntry {
        public String name;
        public String email;
        public String comment;
    }
}

