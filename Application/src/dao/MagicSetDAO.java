package dao;

import model.MagicSet;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MagicSetDAO extends DAO<MagicSet> {

    private static final String CREATE_QUERY = "INSERT INTO magicSet"
            + "(name, date)"
            + "VALUES(?, ?);";

    private static final String READ_QUERY = "SELECT date FROM magicSet WHERE id = ?;";

    private static final String UPDATE_QUERY = "UPDATE magicSet SET name = ?, date = ? WHERE id = ?;";

    private static final String DELETE_QUERY = "DELETE FROM magicSet WHERE id = ?";

    private static final String ALL_QUERY = "SELECT * FROM magicSet ORDER BY date";



    public MagicSetDAO(Connection connection){
        super(connection);
    }


    @Override
    public void create(MagicSet magicSet) throws SQLException {
        try(PreparedStatement statement = connection.prepareStatement(CREATE_QUERY)) {
            statement.setString(1, magicSet.getName());
            statement.setDate(2, Date.valueOf(magicSet.getDate()));


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
    public MagicSet read(Integer id) throws SQLException {
        MagicSet magicSet = new MagicSet();

        try(PreparedStatement statement = connection.prepareStatement(READ_QUERY)) {
            statement.setString(1, id.toString());
            try {
                ResultSet result = statement.executeQuery();
                if(result.next()){
                    magicSet.setId(id);
                    magicSet.setName(result.getString("name"));
                    magicSet.setDate(result.getDate("date").toString());
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
        return magicSet;
    }


    @Override
    public void update(MagicSet magicSet) throws SQLException {
        String query;
        query = UPDATE_QUERY;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, magicSet.getName());
            statement.setString(2, magicSet.getDate());
            statement.setInt(3, magicSet.getId());

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
    public List<MagicSet> all() throws SQLException {
        List<MagicSet> magicSetList = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(ALL_QUERY);
             ResultSet result = statement.executeQuery()) {
            while (result.next()) {
                MagicSet magicSet = new MagicSet();
                magicSet.setId(result.getInt("id"));
                magicSet.setName(result.getString("name"));
                magicSet.setDate(result.getDate("date").toString());

                magicSetList.add(magicSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new SQLException("Erro ao listar usuários.");
        }

        return magicSetList;
    }
}
