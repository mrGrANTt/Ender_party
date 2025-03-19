package org.ven.mrg.ender_party.custom;

import org.bukkit.block.data.BlockData;
import org.ven.mrg.ender_party.Values;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockDB {
    private static final String DB_URL = "jdbc:sqlite:/plugins/Ender_party/blocks.sqlite";

    static {
        Values.logWithType(1, "Loading block's database!");
        try {
            Class.forName("org.h2.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement sts = conn.createStatement()) {
            sts.execute("CREATE TABLE IF NOT EXISTS blocks (int x notnull, int y notnull, ing z notnull)");
            Values.logWithType(0, "Database table created!");
        }
    }

    private static List<BlockData> getAllBlocks() throws SQLException {
        List<BlockData> blocks = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement sts = conn.createStatement();
             ResultSet res = sts.executeQuery("SELECT * FROM blocks")) {
            Values.logWithType(0, "Select all block from database");
            while (res.next()) {
                BlockData block = null;// TODO:
                blocks.add(block);
            }
        }
        return blocks;
    }

    public static void addBlock(BlockData block) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement st = conn.prepareStatement("INSERT INTO blocks (title, description, competed) VALUES (?, ?, ?)")) {

            st.executeUpdate();
            Values.logWithType(0, "Added block to database");
        }
    }

    public static void updateBlock(BlockData block) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement st = conn.prepareStatement("UPDATE blocks SET title = ?, description = ?, competed = ? WHERE id = ?")) {

            st.executeUpdate();
            Values.logWithType(0, "Block updated in database");
        }
    }

    public static void deleteBlock(BlockData block) throws SQLException {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement st = conn.prepareStatement("DELETE FROM blocks WHERE id = ?")) {

            st.executeUpdate();
            Values.logWithType(0, "Block removed in database");
        }
    }
}