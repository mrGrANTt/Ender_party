package org.ven.mrg.ender_party.custom;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.NotNull;
import org.ven.mrg.ender_party.Values;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BlockDB {
    private static final String DB_URL = "jdbc:sqlite:plugins/Ender_party/blocks.sqlite";
    private static Connection conn;

    static {
        Values.logWithType(1, "Loading block's database!");
        try {
            conn = DriverManager.getConnection(DB_URL);
            Class.forName("org.h2.Driver");
            initializeDatabase();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializeDatabase() throws SQLException {
        try (Statement sts = conn.createStatement()) {
            sts.execute("CREATE TABLE IF NOT EXISTS blocks (world TEXT, x INT, y INT, z INT, data TEXT)");
            sts.execute("CREATE TABLE IF NOT EXISTS protected_blocks (world TEXT, x INT, y INT, z INT)");
            Values.logWithType(0, "Database table created!");
        }
    }

    public static List<BlockInfo> getAllBlocks() throws SQLException {
        List<BlockInfo> blocks = new ArrayList<>();

        try (Statement sts = conn.createStatement();
             ResultSet res = sts.executeQuery("SELECT * FROM blocks")) {
            Values.logWithType(0, "Select all block from database");
            while (res.next()) {
                BlockInfo block = new BlockInfo(new Location(
                        Values.getPlg().getServer().getWorld(res.getString(1)),
                        res.getInt(2),
                        res.getInt(3),
                        res.getInt(4)
                ), res.getString(5));
                blocks.add(block);
            }
        }
        return blocks;
    }

    public static void addBlock(BlockInfo block) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO blocks (world, x, y, z, data) VALUES (?, ?, ?, ?, ?)")) {

            st.setString(1, block.loc.getWorld().getName());
            st.setInt(2, block.loc.getBlockX());
            st.setInt(3, block.loc.getBlockY());
            st.setInt(4, block.loc.getBlockZ());
            st.setString(5, block.json);

            st.executeUpdate();
            Values.logWithType(0, "Added block to database");
        }
    }

    public static void updateBlock(BlockInfo block) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("UPDATE blocks SET data = ? WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

            st.setString(0, block.json);
            st.setString(1, block.loc.getWorld().getName());
            st.setInt(2, block.loc.getBlockX());
            st.setInt(3, block.loc.getBlockY());
            st.setInt(4, block.loc.getBlockZ());

            st.executeUpdate();
            Values.logWithType(0, "Block updated in database");
        }
    }

    public static void deleteBlock(BlockInfo block) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("DELETE FROM blocks WHERE world = ? AND x = ? AND y = ? AND z = ?")) {

            st.setString(0, block.loc.getWorld().getName());
            st.setInt(1, block.loc.getBlockX());
            st.setInt(2, block.loc.getBlockY());
            st.setInt(3, block.loc.getBlockZ());

            st.executeUpdate();
            Values.logWithType(0, "Block removed from database");
        }
    }

    public static void addProtectedBlock(Location location) throws SQLException {
        try (PreparedStatement st = conn.prepareStatement("INSERT INTO protected_blocks (world, x, y, z) VALUES (?, ?, ?, ?)")) {
            st.setString(1, location.getWorld().getName());
            st.setInt(2, location.getBlockX());
            st.setInt(3, location.getBlockY());
            st.setInt(4, location.getBlockZ());

            st.executeUpdate();
            Values.logWithType(0, "Added protected block to database");
        }
    }

    public static void LodeAllProtectedBlocks() throws SQLException {
        try (Statement sts = conn.createStatement();
             ResultSet res = sts.executeQuery("SELECT * FROM protected_blocks")) {
            Values.logWithType(0, "Select all protected block from database");
            while (res.next()) {
                Block block = new Location(
                        Values.getPlg().getServer().getWorld(res.getString(1)),
                        res.getInt(2),
                        res.getInt(3),
                        res.getInt(4)
                ).getBlock();
                block.setMetadata("protected-block", new FixedMetadataValue(Values.getPlg(), true));
            }
        }
    }

    static void clearDB() throws SQLException {
        try (Statement sts = conn.createStatement()) {
            sts.execute("DELETE FROM blocks");
            sts.execute("DELETE FROM protected_blocks");
            Values.logWithType(0, "Database table cleared!");
        }
    }
}