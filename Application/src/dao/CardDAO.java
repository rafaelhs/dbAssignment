package dao;

import com.mysql.cj.protocol.Resultset;
import model.Card;
import jdbc.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CardDAO extends DAO<Card> {

    private static final String CREATE_QUERY = "INSERT INTO card"
            + "(name, cost, description, type, rarity)"
            + "VALUES(?, ?, ?, ?, ?);";

    private static final String READ_QUERY = "SELECT name, cost, description, type, rarity FROM card WHERE id = ? " +
            "ORDER BY name ASC;";

    private static final String UPDATE_QUERY = "UPDATE card SET "
            + "name = ?, cost = ?, description = ?, type = ?, rarity = ? "
            + "WHERE id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM card WHERE id = ?;";

    private static final String ALL_QUERY = "SELECT * FROM card ORDER BY name;";

    private static final String EXISTS_QUERY = "SELECT id FROM card WHERE name = ?;";









    public CardDAO(Connection connection) {
        super(connection);
    }


    @Override
    public void create(Card card) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, card.getName());
            statement.setString(2, card.getCost());
            statement.setString(3, card.getDescription());
            statement.setString(4, card.getType());
            statement.setString(5, Character.toString(card.getRarity()));

            try {
                statement.executeUpdate();

            } catch (SQLException ex) {
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
    public Card read(Integer id) throws SQLException {
        Card card = new Card();

        try (PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, id);
            try {
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    card.setId(id);
                    card.setName(result.getString("name"));
                    card.setCost(result.getString("cost"));
                    card.setDescription(result.getString("description"));
                    card.setType(result.getString("type"));
                    card.setRarity(result.getString("rarity").charAt(0));
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return card;
    }

    @Override
    public void update(Card card) throws SQLException {
        String query;
        query = UPDATE_QUERY; //"UPDATE card SET name = ?, cost = ?, description = ?, type = ?, rarity = ? WHERE id = ?;"

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card.getName());
            statement.setString(2, card.getCost());
            statement.setString(3, card.getDescription());
            statement.setString(4, card.getType());
            statement.setString(5, Character.toString(card.getRarity()));
            statement.setInt(6, (card.getId()));

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Card not found.");
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public void delete(Integer id) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Card not found.");
            }
        } catch (SQLException ex) {
            throw ex;
        }
    }

    @Override
    public List<Card> all() throws SQLException {
        List<Card> cardList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            
            while (result.next()) {
                Card card = new Card();
                card.setId(result.getInt("id"));
                card.setName(result.getString("name"));
                card.setCost(result.getString("cost"));
                card.setDescription(result.getString("description"));
                card.setType(result.getString("type"));
                card.setRarity(result.getString("rarity").charAt(0));

                cardList.add(card);

            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return cardList;
    }



    public Card readName(String name) throws SQLException {
        Card card = null;

        try (PreparedStatement statement = connection.prepareStatement(EXISTS_QUERY)) {
            statement.setString(1, name);
            try {
                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    card = new Card();
                    card.setId(result.getInt("id"));
                    card.setName(name);
                }
            } catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
            }
        }
        return card;
    }




}
