package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dto.TaskDTO;
import util.DBConnection;

public class TaskDAO {
    
    public TaskDAO() {
        System.out.println("TaskDAO initialized");
    }
    
    // FAHIM's method: Add a new task to database
    public boolean addTask(TaskDTO taskDTO) {
        String sql = "INSERT INTO tasks (title, description, priority, status, due_date) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, taskDTO.getTitle());
            pstmt.setString(2, taskDTO.getDescription());
            pstmt.setString(3, taskDTO.getPriority());
            pstmt.setString(4, taskDTO.getStatus());
            pstmt.setDate(5, taskDTO.getDueDate());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error adding task: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // FAHIM's method: Update an existing task
    public boolean updateTask(TaskDTO taskDTO) {
        String sql = "UPDATE tasks SET title = ?, description = ?, priority = ?, status = ?, due_date = ? WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, taskDTO.getTitle());
            pstmt.setString(2, taskDTO.getDescription());
            pstmt.setString(3, taskDTO.getPriority());
            pstmt.setString(4, taskDTO.getStatus());
            pstmt.setDate(5, taskDTO.getDueDate());
            pstmt.setInt(6, taskDTO.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error updating task: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // FAHIM's method: Check if task exists
    public boolean taskExists(int taskId) {
        String sql = "SELECT id FROM tasks WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // FAHIM's method: Get task by ID (for edit form)
    public TaskDTO getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE id = ?";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                // Convert Timestamp to LocalDateTime
                if(rs.getTimestamp("created_at") != null) {
                    task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                } else {
                    task.setCreatedAt(null);
                }
                return task;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // deleteTask - mehedi
    public boolean deleteTask(Integer taskId) {
        String sql = "DELETE FROM tasks WHERE id = ?";
 
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement prepstmt = conn.prepareStatement(sql)) {
 
            prepstmt.setInt(1, taskId);
            int rowsAffected = prepstmt.executeUpdate();
            return rowsAffected > 0;
 
        } catch (SQLException e) {
            System.err.println("TaskDAO: Error deleting task ID " + taskId + " — " + e.getMessage());
            return false;
        }
    }
    
    // For Dashboard - getAllTasks
    public List<TaskDTO> getAllTasks() {
        List<TaskDTO> tasks = new ArrayList<>();
        String sql = "SELECT * FROM tasks ORDER BY created_at DESC";
        
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while(rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                // Convert Timestamp to LocalDateTime
                if(rs.getTimestamp("created_at") != null) {
                    task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                } else {
                    task.setCreatedAt(null);
                }
                tasks.add(task);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    
    // Search tasks
    public List<TaskDTO> searchTasks(String column, String value) {
        List<TaskDTO> list = new ArrayList<>();
        String sql;

        if (column.equals("id")) {
            sql = "SELECT * FROM tasks WHERE id = ?";
        } else if (column.equals("due_date")) {
            sql = "SELECT * FROM tasks WHERE due_date = ?";
        } else {
            sql = "SELECT * FROM tasks WHERE " + column + " LIKE ?";
        }

        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (column.equals("id")) {
                ps.setInt(1, Integer.parseInt(value));
            } else if (column.equals("due_date")) {
                ps.setDate(1, java.sql.Date.valueOf(value)); 
            } else {
                ps.setString(1, "%" + value + "%");
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                TaskDTO task = new TaskDTO();
                task.setId(rs.getInt("id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setPriority(rs.getString("priority"));
                task.setStatus(rs.getString("status"));
                task.setDueDate(rs.getDate("due_date"));
                // Convert Timestamp to LocalDateTime
                if(rs.getTimestamp("created_at") != null) {
                    task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                } else {
                    task.setCreatedAt(null);
                }
                list.add(task);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}