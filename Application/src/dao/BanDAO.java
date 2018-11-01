package dao;

import model.Ban;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BanDAO extends DAO<Ban> {

    private static final String CREATE_QUERY = "INSERT INTO ban"
            + "(date)"
            + "VALUES(?);";

    private static final String READ_QUERY = "SELECT date FROM ban WHERE id = ?;";

    private static final String UPDATE_QUERY = "UPDATE ban SET "
            + "date = ? "
            + "WHERE id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM ban WHERE id = ?";

    private static final String ALL_QUERY = "SELECT * FROM ban ORDER BY date";



    public BanDAO(Connection connection){
        super(connection);
    }


    @Override
    public void create(Ban ban) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setDate(1, Date.valueOf(ban.getDate()));


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
    public Ban read(Integer id) throws SQLException {
        Ban ban = new Ban();

        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, id.toString());
            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    ban.setId(id);
                    ban.setDate(result.getDate("date").toString());
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
        return ban;
    }


    @Override
    public void update(Ban ban) throws SQLException {
        String query;
        query = UPDATE_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, ban.getDate());
            statement.setInt(2, ban.getId());

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
    public List<Ban> all() throws SQLException {
        List<Ban> banList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                Ban ban = new Ban();
                ban.setId(result.getInt("id"));
                ban.setDate(result.getDate("date").toString());

                banList.add(ban);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao listar usuários.");
        }

        return banList;
    }
}
