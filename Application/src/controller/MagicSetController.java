package controller;

import dao.CardDAO;
import dao.ChampionshipDAO;
import dao.DAOFactory;
import dao.MagicSetDAO;
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

@WebServlet(name = "MagicSetController", urlPatterns = {"/magicset",
        "/magicset/create",
        "/magicset/update",
        "/magicset/delete",
        "/magicset/read"
})

public class MagicSetController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        MagicSetDAO dao;
        MagicSet magicSet;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {


            case"/magicset/create":
                magicSet = new MagicSet();
                magicSet.setName(request.getParameter("name"));
                magicSet.setDate(request.getParameter("date"));

                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getMagicSetDAO();
                    dao.create(magicSet);

                    response.sendRedirect(request.getContextPath() + "/magicset");

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/magicset");
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;



        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        MagicSetDAO dao;

        switch (request.getServletPath()) {
            case "/magicset":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getMagicSetDAO();
                    List<MagicSet> magicSetList = dao.all();
                    request.setAttribute("magicSetList", magicSetList);


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dispatcher = request.getRequestDispatcher("/view/magicSet.jsp");
                dispatcher.forward(request, response);
                break;

            case"/magicset/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getMagicSetDAO();
                    dao.delete(Integer.parseInt(request.getParameter("magicSetId")));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/magicset ");

                break;
        }
    }
}
