package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.TaskDTO;
import util.DBConnection;


public class TaskDAO {
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
                task.setCreatedAt(rs.getString("created_at"));
                return task;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // For Dashboard (helping Mahmud)
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
                task.setCreatedAt(rs.getString("created_at"));
                tasks.add(task);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }

}
