package dao;

import model.Championship;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChampionshipDAO extends DAO<Championship> {

    private static final String CREATE_QUERY = "INSERT INTO championship"
            + "(date, name)"
            + "VALUES(?, ?);";

    private static final String READ_QUERY = "SELECT date, name FROM championship WHERE id = ?;";

    private static final String READ_NQUERY = "SELECT * FROM championship WHERE name = ?;";

    private static final String UPDATE_QUERY = "UPDATE championship SET "
            + "date = ?, name = ? "
            + "WHERE id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM championship WHERE id = ?;";

    private static final String ALL_QUERY = "SELECT * FROM championship ORDER BY date;";



    public ChampionshipDAO(Connection connection){
        super(connection);
    }


    @Override
    public void create(Championship championship) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setDate(1, Date.valueOf(championship.getDate()));
            statement.setString(2, championship.getName());

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
    public Championship read(Integer id) throws SQLException {
        Championship championship = new Championship();

        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, id.toString());
            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    championship.setId(id);
                    championship.setName(result.getString("name"));
                    championship.setDate(result.getDate("date").toString());
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
        return championship;
    }


    public Championship readName(String name) throws SQLException {
        Championship championship = new Championship();

        try(PreparedStatement statement = connection.prepareStatement(READ_NQUERY)) {
            statement.setString(1, name);
            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    championship.setId(Integer.parseInt(result.getString("id")));
                    championship.setName(result.getString("name"));
                    championship.setDate(result.getDate("date").toString());
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
        return championship;
    }



    @Override
    public void update(Championship championship) throws SQLException {
        String query;
        query = UPDATE_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, championship.getDate());
            statement.setString(2, championship.getName());
            statement.setInt(3, championship.getId());

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
        try (PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);

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
    public List<Championship> all() throws SQLException {
        List<Championship> championshipList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Championship championship = new Championship();
                championship.setId(result.getInt("id"));
                championship.setDate(result.getDate("date").toString());
                championship.setName(result.getString("name"));

                championshipList.add(championship);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return championshipList;
    }
}
