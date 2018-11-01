package dao;

import jdbc.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory implements AutoCloseable {

    private Connection connection = null;

    public DAOFactory() throws ClassNotFoundException, IOException, SQLException {
        connection = ConnectionFactory.getInstance().getConnection();
    }
/*
    public void beginTransaction() throws SQLException {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            throw new SQLException("Erro ao abrir transação.");
        }
    }

    public void commitTransaction() throws  SQLException {
        try {
            connection.commit();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao finalizar transação.");
        }
    }
    public void rollbackTransaction() throws SQLException {
        try {
            connection.rollback();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao executar transação.");
        }
    }

    public void endTransaction() throws SQLException {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao finalizar transação.");
        }
    }

    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao fechar conexão ao banco de dados.");
        }
    }
    */


    public BanDAO getBanDAO() {
        return new BanDAO(connection);
    }
    public CardDAO getCardDAO() {
        return new CardDAO(connection);
    }
    public ChampionshipDAO getChampionshipDAO() {
        return new ChampionshipDAO(connection);
    }
    public FrequencyDAO getFrequencyDAO() {
        return new FrequencyDAO(connection);
    }
    public MagicSetDAO getMagicSetDAO() {
        return new MagicSetDAO(connection);
    }
    public PriceDAO getPriceDAO() {
        return new PriceDAO(connection);
    }


    public void closeConnection() throws SQLException {
        try {
            connection.close();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());

            throw new SQLException("Erro ao fechar conexão ao banco de dados.");
        }
    }

    @Override
    public void close() throws Exception {
        closeConnection();
    }
}
