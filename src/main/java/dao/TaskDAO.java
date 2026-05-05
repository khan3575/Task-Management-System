package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.TaskDTO;
import util.DBConnection;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TaskDAO {

    private static final Logger logger = LogManager.getLogger(TaskDAO.class);

    public boolean addTask(TaskDTO taskDTO) {
        String sql = "INSERT INTO tasks (title, description, priority, status, due_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, taskDTO.getTitle());
            pstmt.setString(2, taskDTO.getDescription());
            pstmt.setString(3, taskDTO.getPriority());
            pstmt.setString(4, taskDTO.getStatus());
            pstmt.setDate(5, taskDTO.getDueDate());

            int rows = pstmt.executeUpdate();

            logger.debug("Executed INSERT task, rows affected: {}", rows);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("DB error while adding task", e);
            return false;
        }
    }

    public boolean updateTask(TaskDTO taskDTO) {
        String sql = "UPDATE tasks SET title=?, description=?, priority=?, status=?, due_date=? WHERE id=?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, taskDTO.getTitle());
            pstmt.setString(2, taskDTO.getDescription());
            pstmt.setString(3, taskDTO.getPriority());
            pstmt.setString(4, taskDTO.getStatus());
            pstmt.setDate(5, taskDTO.getDueDate());
            pstmt.setInt(6, taskDTO.getId());

            int rows = pstmt.executeUpdate();

            logger.debug("Executed UPDATE task ID {}, rows affected: {}", taskDTO.getId(), rows);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("DB error while updating task ID {}", taskDTO.getId(), e);
            return false;
        }
    }

    public boolean taskExists(int taskId) {
        String sql = "SELECT id FROM tasks WHERE id=?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            logger.error("DB error checking existence for task ID {}", taskId, e);
            return false;
        }
    }

    public TaskDTO getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE id=?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                return task;
            }

        } catch (SQLException e) {
            logger.error("DB error fetching task ID {}", taskId, e);
        }

        return null;
    }

    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id=?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, taskId);
            int rows = pstmt.executeUpdate();

            logger.debug("Executed DELETE task ID {}, rows affected: {}", taskId, rows);
            return rows > 0;

        } catch (SQLException e) {
            logger.error("DB error deleting task ID {}", taskId, e);
            return false;
        }
    }

    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tasks.add(task);
            }

        } catch (SQLException e) {
            logger.error("DB error fetching all tasks", e);
        }

        return tasks;
    }

    public int getTaskCount() {
        String sql = "SELECT COUNT(*) FROM tasks";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (SQLException e) {
            logger.error("DB error counting tasks", e);
        }

        return 0;
    }

    public List<TaskDTO> searchTasks(String column, String value) {
        List<TaskDTO> list = new ArrayList<>();

        String sql;

        if (column.equals("id")) {
            sql = "SELECT * FROM tasks WHERE id=?";
        } else if (column.equals("due_date")) {
            sql = "SELECT * FROM tasks WHERE due_date=?";
        } else {
            sql = "SELECT * FROM tasks WHERE " + column + " LIKE ?";
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (column.equals("id")) {
                ps.setInt(1, Integer.parseInt(value));
            } else if (column.equals("due_date")) {
                ps.setDate(1, Date.valueOf(value));
            } else {
                ps.setString(1, "%" + value + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

                list.add(task);
            }

        } catch (Exception e) {
            logger.error("DB error during search (column={}, value={})", column, value, e);
        }

        return list;
    }

    public List<TaskDTO> findPaginated(int page, int size) {
        List<TaskDTO> list = new ArrayList<>();

        String sql = "SELECT * FROM tasks ORDER BY id DESC LIMIT ? OFFSET ?";

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, size);
            ps.setInt(2, (page - 1) * size);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));

                list.add(task);
            }

        } catch (Exception e) {
            logger.error("DB error during pagination (page={}, size={})", page, size, e);
        }

        return list;
    }
}