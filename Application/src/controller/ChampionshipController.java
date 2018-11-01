package controller;

import dao.CardDAO;
import dao.ChampionshipDAO;
import dao.DAOFactory;
import model.Card;
import model.Championship;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet(name = "ChampionshipController", urlPatterns = {"/championship",
                                                            "/championship/create",
                                                            "/championship/update",
                                                            "/championship/delete",
                                                            "/championship/read"
                                                    })

public class ChampionshipController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ChampionshipDAO dao;
        Championship championship;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {


            case"/championship/create":
                championship = new Championship();
                championship.setName(request.getParameter("name"));
                championship.setDate(request.getParameter("date"));

                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getChampionshipDAO();
                    dao.create(championship);

                    response.sendRedirect(request.getContextPath() + "/championship");

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/championship");
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;



        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        ChampionshipDAO dao;

        switch (request.getServletPath()) {
            case "/championship":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getChampionshipDAO();
                    List<Championship> championshipList = dao.all();

                    request.setAttribute("championshipList", championshipList);


                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dispatcher = request.getRequestDispatcher("/view/championship.jsp");
                dispatcher.forward(request, response);
                break;

            case"/championship/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getChampionshipDAO();
                    dao.delete(Integer.parseInt(request.getParameter("idChampionship")));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/championship ");

                break;
        }
    }
}
