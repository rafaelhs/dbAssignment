package controller;

import dao.*;
import model.Ban;
import model.Card;
import model.Championship;
import model.MagicSet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "BanController", urlPatterns = {"/ban",
        "/ban/create",
        "/ban/update",
        "/ban/delete",
        "/ban/read"
})

public class BanController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        BanDAO dao;
        Ban ban;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {


            case"/ban/create":
                ban = new Ban();
                ban.setDate(request.getParameter("date"));
                System.out.println(request.getParameter("date"));

                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getBanDAO();
                    dao.create(ban);

                    response.sendRedirect(request.getContextPath() + "/ban");

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/ban");
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;



        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        BanDAO dao;

        switch (request.getServletPath()) {
            case "/ban":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getBanDAO();
                    List<Ban> banList = dao.all();
                    request.setAttribute("banList", banList);


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dispatcher = request.getRequestDispatcher("/view/ban.jsp");
                dispatcher.forward(request, response);
                break;

            case"/ban/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getBanDAO();
                    dao.delete(Integer.parseInt(request.getParameter("banId")));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/ban");

                break;
        }
    }
}
