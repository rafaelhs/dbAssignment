package dao;

import model.Frequency;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FrequencyDAO extends DAO<Frequency> {

    private static final String CREATE_QUERY = "INSERT INTO frequency"
            + "(idCard, idChampionship, quantity)"
            + "VALUES(?, ?, ?);";

    private static final String READ_QUERY = "SELECT quantity FROM frequency WHERE idCard = ? AND idChampionship = ?;";

    private static final String UPDATE_QUERY = "UPDATE frequency SET quantity = ? WHERE idCard = ? AND idChampionship = ?;";

    private static final String DELETE_QUERY = "DELETE FROM frequency WHERE idCard = ? AND idChampionship = ?;";

    private static final String ALL_QUERY = "SELECT id, date FROM frequency;";

    private static final String ALL_FQUERY = "SELECT idCard, idChampionship, quantity, name\n" +
            "FROM frequency, card\n" +
            "WHERE idChampionship = ?\n" +
            "AND idCard = id\n" +
            "ORDER BY quantity ASC;";



    private static final String ALL_CQUERY = "SELECT idCard, idChampionship, quantity, name\n" +
            "FROM frequency, championship\n" +
            "WHERE idCard = ?\n" +
            "AND idChampionship = id\n" +
            "ORDER BY name ASC;";







    public FrequencyDAO(Connection connection){
        super(connection);
    }


    @Override
    public void create(Frequency frequency) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setInt(1, frequency.getIdCard());
            statement.setInt(2, frequency.getIdChampionship());
            statement.setInt(3, frequency.getQuantity());

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
    public Frequency read(Integer id) throws SQLException {
        return null;
    }


    public Frequency readFrequency(Integer idCard, Integer idChampionship) throws SQLException {
        Frequency frequency = new Frequency();

        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setInt(1, idCard);
            statement.setInt(2, idChampionship);

            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    frequency.setIdCard(idCard);
                    frequency.setIdChampionship(idChampionship);
                    frequency.setQuantity(result.getInt("quantity"));
                }else{
                    return null;
                }
            }catch (SQLException ex) {
                System.err.println(ex.getMessage());
                ex.printStackTrace();
                return null;
                /*
                if (ex.getMessage().contains("uq_usuario_login")) {
                    throw new SQLException("Erro ao inserir usuário: login já existente.");
                } else if (ex.getMessage().contains("not-null")) {
                    throw new SQLException("Erro ao inserir usuário: pelo menos um campo está em branco.");
                } else {
                    throw new SQLException("Erro ao inserir usuário.");
                }*/
            }
        }
        return frequency;
    }

    @Override
    public void update(Frequency frequency) throws SQLException {
        String query;
        query = UPDATE_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, frequency.getQuantity());
            statement.setInt(2, frequency.getIdCard());
            statement.setInt(3, frequency.getIdChampionship());

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

    public void deleteFrequency(Integer idCard, Integer idChampionship) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, idCard);
            statement.setInt(2, idChampionship);

            if (statement.executeUpdate() < 1) {
                throw new SQLException("Erro ao excluir: usuário não encontrado.");
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
    public List<Frequency> all() throws SQLException {
        List<Frequency> frequencyList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Frequency frequency = new Frequency();
                frequency.setIdCard(result.getInt("idCard"));
                frequency.setIdChampionship(result.getInt("idChampionship"));
                frequency.setQuantity(result.getInt("quantity"));
                frequencyList.add(frequency);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return frequencyList;
    }


    public List<Frequency> allFrequency(int championshipId) throws SQLException {
        List<Frequency> frequencyList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_FQUERY)){
             statement.setInt(1, championshipId);

             ResultSet result = statement.executeQuery();
            while (result.next()) {
                Frequency frequency = new Frequency();
                frequency.setIdCard(result.getInt("idCard"));
                frequency.setIdChampionship(result.getInt("idChampionship"));
                frequency.setQuantity(result.getInt("quantity"));
                frequency.setName(result.getString("name"));
                frequencyList.add(frequency);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return frequencyList;
    }

    public List<Frequency> allCard(int cardId) throws SQLException {
        List<Frequency> frequencyList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_CQUERY)){
            statement.setInt(1, cardId);

            ResultSet result = statement.executeQuery();
            while (result.next()) {
                Frequency frequency = new Frequency();
                frequency.setIdCard(result.getInt("idCard"));
                frequency.setIdChampionship(result.getInt("idChampionship"));
                frequency.setQuantity(result.getInt("quantity"));
                frequency.setName(result.getString("name"));
                frequencyList.add(frequency);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return frequencyList;
    }
}
