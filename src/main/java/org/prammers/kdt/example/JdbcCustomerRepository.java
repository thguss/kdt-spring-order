package org.prammers.kdt.example;

import org.prammers.kdt.customer.model.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcCustomerRepository {

    private static final Logger logger = LoggerFactory.getLogger(JdbcCustomerRepository.class);

    private final String SELECT_BY_NAME_SQL = "select * from customers where name = ?";
    private final String SELECT_ALL_SQL = "select * from customers";
    private final String INSERT_SQL = "insert into customers(customer_id, name, email) values (UUID_TO_BIN(?), ?, ?)";
    private final String UPDATE_BY_ID_SQL = "update customers set name = ? where customer_id = UUID_TO_BIN(?)";
    private final String DELETE_ALL_SQL = "delete from customers";

    public List<String> findNames(String name) {
        List<String> names = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                PreparedStatement statement = connection.prepareStatement(SELECT_BY_NAME_SQL)
        ) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String customerName = resultSet.getString("name");
                    UUID customer_id = UUID.nameUUIDFromBytes(resultSet.getBytes("customer_id"));
                    LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                    logger.info("customer id -> {}, name -> {}, created_at -> {}", customer_id, customerName, createdAt);
                    names.add(customerName);
                }
            }
        } catch (SQLException sqlException) {
            logger.error("Got error while closing connection", sqlException);
        }

        return names;
    }

    public List<String> findAllNames() {
        List<String> names = new ArrayList<>();

        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL);
                ResultSet resultSet = statement.executeQuery()
        ) {
            while (resultSet.next()) {
                String customerName = resultSet.getString("name");
                UUID customer_id = toUUID(resultSet.getBytes("customer_id"));
                LocalDateTime createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();
                logger.info("customer id -> {}, name -> {}, created_at -> {}", customer_id, customerName, createdAt);
                names.add(customerName);
            }
        } catch (SQLException sqlException) {
            logger.error("Got error while closing connection", sqlException);
        }

        return names;
    }

    public int insertCustomer(UUID customerId, String name, String email) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                PreparedStatement statement = connection.prepareStatement(INSERT_SQL)
        ) {
            statement.setBytes(1, customerId.toString().getBytes());
            statement.setString(2, name);
            statement.setString(3, email);

            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.error("Got error while closing connection", sqlException);
        }
        return 0;
    }

    public int updateCustomerName(UUID customerId, String name) {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                PreparedStatement statement = connection.prepareStatement(UPDATE_BY_ID_SQL)
        ) {
            statement.setString(1, name);
            statement.setBytes(2, customerId.toString().getBytes());

            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.error("Got error while closing connection", sqlException);
        }
        return 0;
    }

    public int deleteAllCustomers() {
        try (
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
                PreparedStatement statement = connection.prepareStatement(DELETE_ALL_SQL)
        ) {
            return statement.executeUpdate();
        } catch (SQLException sqlException) {
            logger.error("Got error while closing connection", sqlException);
        }
        return 0;
    }

    public void transactionTest(Customer customer) {
        String updateNameSql = "update customers set name = ? where customer_id = UUID_TO_BIN(?)";
        String updateEmailSql = "update customers set email = ? where customer_id = UUID_TO_BIN(?)";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/order_mgmt", "root", "root1234!");
            connection.setAutoCommit(false);
            try (
                    PreparedStatement updateNameStatement = connection.prepareStatement(updateNameSql);
                    PreparedStatement updateEmailStatement = connection.prepareStatement(updateEmailSql);
            ) {
                updateNameStatement.setString(1, customer.getName());
                updateNameStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateNameStatement.executeUpdate();

                updateEmailStatement.setString(1, customer.getEmail());
                updateEmailStatement.setBytes(2, customer.getCustomerId().toString().getBytes());
                updateEmailStatement.executeUpdate();

                connection.setAutoCommit(true);
            }
        } catch (SQLException sqlException) {
            if (connection != null) {
                try {
                    connection.rollback();
                    connection.close();
                } catch (SQLException exception) {
                    logger.error("Got error while closing connection", exception);
                    throw new RuntimeException(sqlException);
                }
            }
            logger.error("Got error while closing connection", sqlException);
            throw new RuntimeException(sqlException);
        }
    }

    static UUID toUUID(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        return new UUID(byteBuffer.getLong(), byteBuffer.getLong());
    }

    public static void main(String[] args) {
        JdbcCustomerRepository jdbcCustomerRepository = new JdbcCustomerRepository();

        jdbcCustomerRepository.deleteAllCustomers();

        jdbcCustomerRepository.insertCustomer(UUID.randomUUID(), "new-user", "new-user@gmail.com");

        UUID customer2Id = UUID.randomUUID();
        jdbcCustomerRepository.insertCustomer(customer2Id, "new-user2", "new-user2@gmail.com");

        jdbcCustomerRepository.updateCustomerName(customer2Id, "updated-user2");

        List<String> names = jdbcCustomerRepository.findAllNames();
        names.forEach(v -> logger.info("Found name: {}", v));

    }
}
