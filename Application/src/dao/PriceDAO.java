package dao;

import model.Price;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class PriceDAO extends DAO<Price> {

    private static final String CREATE_QUERY = "INSERT INTO price(idCard, date, min, med, max) VALUES(?, ?, ?, ?, ?);";

    private static final String READ_QUERY = "SELECT min, med, max FROM price WHERE idCard = ? AND date = ?;";

    private static final String UPDATE_QUERY = "UPDATE price SET min = ?, med = ?, max = ? WHERE idCard = ? AND date = ?;";

    private static final String DELETE_QUERY = "DELETE FROM price WHERE idCard = ? AND date = ?;";

    private static final String ALL_QUERY = "SELECT * FROM price ORDER BY date;";

    private static final String ALL_PRICE_QUERY = "SELECT * FROM price WHERE idCard=? ORDER BY date;";

    private static final String DATE_QUERY =      "SELECT date FROM price WHERE idcard = ?\n" +
            "UNION ALL\n" +
            "SELECT date FROM championship\n" +
            "UNION ALL\n" +
            "SELECT date FROM ban\n" +
            "UNION ALL\n" +
            "SELECT date FROM magicset ORDER BY date ASC;";








    public PriceDAO(Connection connection){
        super(connection);
    }


    @Override
    public void create(Price price) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, price.getIdCard());
            statement.setDate(2, Date.valueOf(price.getDate()));
            statement.setInt(3, price.getMin());
            statement.setInt(4, price.getMed());
            statement.setInt(5, price.getMax());

            try {
                statement.executeUpdate();

            }catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();

                if (ex.getMessage().contains("uq_usuario_login")) {
                    throw new SQLException("Erro ao inserir usuário: login já existente.");
                } else if (ex.getMessage().contains("not-null")) {
                    throw new SQLException("Erro ao inserir usuário: pelo menos um campo está em branco.");
                } else {
                    throw new SQLException("Erro ao inserir usuário.");
                }
            }
        }
    }

    @Override
    public Price read(Integer id) throws SQLException {
        return null;
    }

    public Price readPrice(Integer idCard, String date) throws SQLException {
        Price price = new Price();

        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, idCard);
            statement.setString(2, date);

            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    price.setIdCard(idCard);
                    price.setDate(date);
                    price.setMin(result.getInt("min"));
                    price.setMed(result.getInt("med"));
                    price.setMax(result.getInt("max"));
                }
            }catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();

                if (ex.getMessage().contains("uq_usuario_login")) {
                    throw new SQLException("Erro ao inserir usuário: login já existente.");
                } else if (ex.getMessage().contains("not-null")) {
                    throw new SQLException("Erro ao inserir usuário: pelo menos um campo está em branco.");
                } else {
                    throw new SQLException("Erro ao inserir usuário.");
                }
            }
        }
        return price;
    }

    @Override
    public void update(Price price) throws SQLException {
        String query;
        query = UPDATE_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, price.getMin());
            statement.setInt(2, price.getMed());
            statement.setInt(3, price.getMax());
            statement.setInt(4, price.getIdCard());
            statement.setString(5, price.getDate());

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao editar: usuário não encontrado.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            if (ex.getMessage().equals("Erro ao editar: usuário não encontrado.")) {
                throw ex;
            } else if (ex.getMessage().contains("uq_usuario_login")) {
                throw new SQLException("Erro ao editar usuário: login já existente.");
            } else if (ex.getMessage().contains("not-null")) {
                throw new SQLException("Erro ao editar usuário: pelo menos um campo está em branco.");
            } else {
                throw new SQLException("Erro ao editar usuário.");
            }
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
    }

    public void deletePrice(Integer idCard, String date) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {

            statement.setInt(1, idCard);
            statement.setDate(2, Date.valueOf(date));

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Price not found.");
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            if (ex.getMessage().equals("Erro ao excluir: usuário não encontrado.")) {
                throw ex;
            } else {
                throw new SQLException("Erro ao excluir usuário.");
            }
        }
    }

    @Override
    public List<Price> all() throws SQLException {
        List<Price> priceList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Price price = new Price();
                price.setIdCard(result.getInt("idCard"));
                price.setDate(result.getDate("date").toString());
                price.setMin(result.getInt("min"));
                price.setMed(result.getInt("med"));
                price.setMax(result.getInt("max"));
                priceList.add(price);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return priceList;
    }

    public List<Price> allPrice(Integer idCard) throws SQLException {
        List<Price> priceList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_PRICE_QUERY)){
             statement.setInt(1, idCard);
             ResultSet result = statement.executeQuery();
            while (result.next()) {
                Price price = new Price();
                price.setIdCard(result.getInt("idCard"));

                Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("BRT"));
                Long time = result.getDate("date", calendar).getTime();

                calendar.setTimeInMillis(time);

                Date date = result.getDate("date", calendar);




                price.setDate(result.getDate("date").toString());
                price.setMin(result.getInt("min"));
                price.setMed(result.getInt("med"));
                price.setMax(result.getInt("max"));
                priceList.add(price);
            }
        }catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return priceList;
    }


    public List<String> allDates(int id) throws SQLException {
        List<String> dateList = new ArrayList<>();


        try(PreparedStatement statement = connection.prepareStatement(DATE_QUERY)) {
            statement.setInt(1, id);
            try {
                ResultSet result = statement.executeQuery();
                while (result.next()){
                    dateList.add(result.getDate("date").toString());
                }
            }catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return dateList;
    }


}
