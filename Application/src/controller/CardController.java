package controller;

import dao.CardDAO;
import dao.DAO;
import dao.DAOFactory;
import dao.PriceDAO;
import model.Card;
import jdbc.*;
import model.Price;
import org.json.*;


import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "CardController", urlPatterns = {"/card",
                                                    "/card/create",
                                                    "/card/update",
                                                    "/card/delete",
                                                    "/card/read",
                                                    "/card/pricejson",
                                                    "/card/cardupdateform"
})
@MultipartConfig
public class CardController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        CardDAO dao;
        Card card;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {


            case"/card/create":
                card = new Card();
                card.setName(request.getParameter("name"));
                card.setCost(request.getParameter("cost"));
                card.setDescription(request.getParameter("description"));
                card.setType(request.getParameter("type"));
                card.setRarity(request.getParameter("rarity").charAt(0));

                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getCardDAO();
                    dao.create(card);

                    response.sendRedirect(request.getContextPath() + "/card");

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/card");
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case "/card/update":
                card = new Card();
                card.setId(Integer.parseInt(request.getParameter("idCard")));
                card.setName(request.getParameter("name"));
                card.setCost(request.getParameter("cost"));
                card.setDescription(request.getParameter("description"));
                card.setType(request.getParameter("type"));
                card.setRarity(request.getParameter("rarity").charAt(0));

                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getCardDAO();
                    dao.update(card);

                    response.sendRedirect(request.getContextPath() + "/card");

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/card");
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


        }


    }






    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        RequestDispatcher dispatcher;
        CardDAO dao;

        switch (request.getServletPath()) {
            case "/card":
                try (DAOFactory daoFactory = new DAOFactory()){
                    dao = daoFactory.getCardDAO();
                    List<Card> cardList = dao.all();

                    request.setAttribute("cardList", cardList);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }


                dispatcher = request.getRequestDispatcher("/view/card.jsp");
                dispatcher.forward(request, response);
                break;

            case "/card/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getCardDAO();
                    dao.delete(Integer.parseInt(request.getParameter("idCard")));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/card");

                break;

            case "/card/cardupdateform":

                try (DAOFactory daoFactory = new DAOFactory()){
                    dao = daoFactory.getCardDAO();
                    Card card = dao.read(Integer.parseInt(request.getParameter("idCard")));

                    request.setAttribute("card", card);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                dispatcher = request.getRequestDispatcher("/view/cardUpdate.jsp");
                dispatcher.forward(request, response);
                break;

        }


        }

        }
