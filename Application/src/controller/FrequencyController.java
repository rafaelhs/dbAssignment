package controller;

import dao.*;
import model.Card;
import model.Championship;
import model.Frequency;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

@WebServlet(name = "FrequencyController", urlPatterns = {"/frequency",
        "/frequency/create",
        "/frequency/update",
        "/frequency/delete",
        "/frequency/read",
        "/frequency/json"
})
@MultipartConfig
public class FrequencyController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        FrequencyDAO dao;
        Frequency frequency;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {



            case "/frequency/create":

                try (DAOFactory daoFactory = new DAOFactory()){
                    Card card = daoFactory.getCardDAO().readName(request.getParameter("cardName"));

                    if(card!=null){
                        frequency = new Frequency();
                        frequency.setIdCard(card.getId());
                        frequency.setIdChampionship(Integer.parseInt(request.getParameter("championshipId")));
                        frequency.setQuantity(Integer.parseInt(request.getParameter("quantity")));

                        dao = daoFactory.getFrequencyDAO();
                        dao.create(frequency);
                    }

                    response.sendRedirect(request.getContextPath() + "/frequency?championshipId="+request.getParameter("championshipId"));

                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    response.sendRedirect(request.getContextPath() + "/frequency?championshipId="+request.getParameter("championshipId"));
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;


            case "/frequency/json":
                // Getting the file inputstream
                Part filePart = request.getPart("json");
                InputStream inputStream = filePart.getInputStream();

                //Getting the file content fom the input stream
                StringBuilder stringBuilder = new StringBuilder();
                String line = null;
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                }

                String json = stringBuilder.toString();


                JSONArray jArray = null;
                JSONObject obj = null;
                try {
                    DAOFactory daoFactory = new DAOFactory();
                    ChampionshipDAO championshipDAO = daoFactory.getChampionshipDAO();
                    CardDAO cardDAO = daoFactory.getCardDAO();
                    FrequencyDAO frequencyDAO = daoFactory.getFrequencyDAO();

                    jArray = new JSONArray(json);
                    for(int i = 0; i < jArray.length(); i++){
                        obj = jArray.getJSONObject(i);

                        String championshipName = obj.getJSONArray("eventName").getString(0);
                        JSONArray nameArray = obj.getJSONArray("name");
                        JSONArray quantityArray = obj.getJSONArray("quantity");


                        Championship championship = championshipDAO.readName(championshipName);
                        if(championship!=null){
                            List<Card> cardList = cardDAO.all();
                            for(int j = 0; j < nameArray.length(); j++){
                                for (Card card:cardList) {
                                    if(card.getName().equals(nameArray.getString(j))){

                                        frequency = new Frequency();
                                        frequency.setIdChampionship(championship.getId());
                                        frequency.setIdCard(card.getId());
                                        frequency.setQuantity(Integer.parseInt(quantityArray.getString(j)));


                                        Frequency frequencyAux = frequencyDAO.readFrequency(frequency.getIdCard(), frequency.getIdChampionship());
                                        if(frequencyAux== null){
                                            frequencyDAO.create(frequency);
                                        }else{
                                            frequency.setQuantity(frequency.getQuantity() + frequencyAux.getQuantity());
                                            frequencyDAO.update(frequency);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    response.sendRedirect(request.getContextPath() + "/card");
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;



        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        RequestDispatcher dispatcher;
        FrequencyDAO dao;

        switch (request.getServletPath()) {
            case "/frequency":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getFrequencyDAO();

                    List<Frequency> frequencyList = dao.allFrequency(Integer.parseInt(request.getParameter("championshipId")));

                    request.setAttribute("frequencyList", frequencyList);
                    request.setAttribute("championshipId", request.getParameter("championshipId"));

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dispatcher = request.getRequestDispatcher("/view/frequency.jsp?championshipId=" + request.getParameter("championshipId"));
                dispatcher.forward(request, response);
                break;

            case"/frequency/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getFrequencyDAO();
                    dao.deleteFrequency(Integer.parseInt(request.getParameter("idCard")), Integer.parseInt(request.getParameter("championshipId")));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/frequency?championshipId=" + request.getParameter("championshipId"));

                break;
        }
    }
}
