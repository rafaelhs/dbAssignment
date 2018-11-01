package controller;

import dao.*;
import model.*;
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
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;

@WebServlet(name = "PriceController", urlPatterns = {"/price",
        "/price/create",
        "/price/update",
        "/price/delete",
        "/price/read",
        "/price/json"
        })
@MultipartConfig
public class PriceController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        PriceDAO dao;
        Price price;
        RequestDispatcher dispatcher;



        switch (request.getServletPath()) {

            case "/price/json":
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
                    CardDAO cardDao = daoFactory.getCardDAO();
                    jArray = new JSONArray(json);
                    for(int i = 0; i < jArray.length(); i++){
                        obj = jArray.getJSONObject(i);



                        String name = obj.getJSONArray("nome").getString(0);
                        Card card = cardDao.readName(name);
                        if(card!=null){
                            NumberFormat format = NumberFormat.getInstance(Locale.FRANCE);
                            price = new Price();
                            int id = card.getId();

                            Number nMin = format.parse(obj.getJSONArray("menor").getString(0).substring(3));
                            Number nMed = format.parse(obj.getJSONArray("medio").getString(0).substring(3));
                            Number nMax = format.parse(obj.getJSONArray("maior").getString(0).substring(3));



                            String date = obj.getString("time").substring(0, 10);
                            int min = (int)(nMin.doubleValue()*100);
                            int med = (int)(nMed.doubleValue()*100);
                            int max = (int)(nMax.doubleValue()*100);


                            price.setIdCard(id);
                            price.setDate(date);
                            price.setMin(min);
                            price.setMed(med);
                            price.setMax(max);

                            dao = daoFactory.getPriceDAO();
                            dao.create(price);
                        }
                    }
                    response.sendRedirect(request.getContextPath() + "/card");
                } catch (JSONException e) {
                    response.sendRedirect(request.getContextPath() + "/card");
                    e.printStackTrace();
                } catch (SQLException e) {
                    response.sendRedirect(request.getContextPath() + "/card");
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;




            case "/price/create":
                price = new Price();
                price.setIdCard(Integer.parseInt(request.getParameter("idCard")));
                price.setDate(request.getParameter("date"));
                price.setMin((int)(Double.parseDouble(request.getParameter("min"))*100));
                price.setMed((int)(Double.parseDouble(request.getParameter("med"))*100));
                price.setMax((int)(Double.parseDouble(request.getParameter("max"))*100));


                System.out.println(request.getParameter("min"));
                System.out.println(price.getMin());


                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getPriceDAO();
                    dao.create(price);

                    response.sendRedirect(request.getContextPath() + "/price?idCard="+request.getParameter("idCard"));

                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    ex.printStackTrace();
                    //session.setAttribute("error", ex.getMessage());
                    response.sendRedirect(request.getContextPath() + "/price?idCard="+request.getParameter("idCard"));
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher;
        PriceDAO dao;

        switch (request.getServletPath()) {
            case "/price":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    ChampionshipDAO championshipDAO = daoFactory.getChampionshipDAO();
                    BanDAO banDAO = daoFactory.getBanDAO();
                    MagicSetDAO magicSetDAO = daoFactory.getMagicSetDAO();
                    FrequencyDAO frequencyDAO = daoFactory.getFrequencyDAO();
                    dao = daoFactory.getPriceDAO();
                    CardDAO cardDAO = daoFactory.getCardDAO();

                    List<String> dateList = dao.allDates(Integer.parseInt(request.getParameter("idCard")));
                    List<Championship> championshipList = championshipDAO.all();
                    List<Ban> banList = banDAO.all();
                    List<MagicSet> setList = magicSetDAO.all();
                    List<Frequency> frequencyList = frequencyDAO.allCard(Integer.parseInt(request.getParameter("idCard")));
                    List<Price> priceList = dao.allPrice(Integer.parseInt(request.getParameter("idCard")));


                    request.setAttribute("dateList", dateList);
                    request.setAttribute("championshipList", championshipList);
                    request.setAttribute("banList", banList);
                    request.setAttribute("setList", setList);
                    request.setAttribute("frequencyList", frequencyList);
                    request.setAttribute("priceList", priceList);
                    request.setAttribute("idCard", request.getParameter("idCard"));

                    Card card = cardDAO.read(Integer.parseInt(request.getParameter("idCard")));

                    request.setAttribute("card", card);

                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dispatcher = request.getRequestDispatcher("/view/price.jsp?idCard="+request.getParameter("idCard"));
                dispatcher.forward(request, response);
                break;

            case"/price/delete":
                try (DAOFactory daoFactory = new DAOFactory()) {
                    dao = daoFactory.getPriceDAO();
                    dao.deletePrice(Integer.parseInt(request.getParameter("idCard")), request.getParameter("date"));
                } catch (ClassNotFoundException | IOException | SQLException ex) {
                    request.getSession().setAttribute("error", ex.getMessage());
                    ex.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                response.sendRedirect(request.getContextPath() + "/price?idCard="+request.getParameter("idCard"));

                break;
        }

    }


}
