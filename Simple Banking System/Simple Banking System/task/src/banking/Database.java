package banking;

import java.sql.*;
import java.util.Objects;

public class Database {

    String databaseUrl;
    public Database(String databaseUrl) {
        this.databaseUrl = databaseUrl;
        createNewTable();
    }

    private Connection connect(){
        String url = "jdbc:sqlite:" + databaseUrl;
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void createNewTable() {

        String sql = "CREATE TABLE IF NOT EXISTS card ("
                + "	id INTEGER PRIMARY KEY,"
                + "	number TEXT,"
                + "	pin TEXT,"
                + " balance INTEGER DEFAULT 0"
                + ");";
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage() + "createNewTableFailed");
        }
    }

    public void insertNew(Account account) {
        String sql = "INSERT INTO card VALUES(?,?,?,?);";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,account.hashCode());
            pstmt.setString(2, String.valueOf(account.getNumber()));
            pstmt.setString(3, String.valueOf(account.getPin()));
            pstmt.setInt(4,account.getBalance());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage() + "insertNew " + account.hashCode());
        }
    }

    public boolean checkIfExist(String number) {
        String sql = "SELECT * FROM card WHERE number = ?;";
        boolean result = false;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,number);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                if(rs.getString("number").equals(number)) {
                    result = true;
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public boolean checkNumPin(String number, String pin) {
        String sql = "SELECT * FROM card WHERE number = ?;";
        boolean result = false;
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1,number);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                if(rs.getString("number").equals(number)) {
                    if(rs.getString("pin").equals(pin)) {
                        result = true;
                        break;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public int getBalance(String number) {
        String sql = "SELECT * FROM card WHERE number LIKE " +  number;
        int result = 0;
        try (Connection conn = this.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while(rs.next()) {
                if(rs.getString("number").equals(number)) {
                    result = rs.getInt("balance");
                    break;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void addBalance(String number,Integer amount) {
        String sql = "UPDATE card SET balance = balance+? WHERE number = ?;";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,amount);
            pstmt.setString(2,number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public void minusBalance(String number,Integer amount) {
        String sql = "UPDATE card SET balance = balance-? WHERE number = ?;";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1,amount);
            pstmt.setString(2,number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteAccount(String number) {
        String sql = "DELETE FROM card WHERE number = ?;";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1,number);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}