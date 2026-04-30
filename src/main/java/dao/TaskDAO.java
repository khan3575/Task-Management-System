package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.TaskDTO;
import model.Task;
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
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return task;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    //deleteTask-mehedi
    public boolean deleteTask(int taskId) {
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
    
    // For Dashboard 
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
                task.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                tasks.add(task);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return tasks;
    }
    
    public int getTaskCount()
    {
    	String sql = "SELECT COUNT(*) FROM tasks";

    	try (Connection conn = DBConnection.getInstance().getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) return rs.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
    
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
            }else if (column.equals("due_date")) {
                ps.setDate(1, java.sql.Date.valueOf(value)); 
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
            e.printStackTrace();
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
            e.printStackTrace();
        }

        return list;
    }
}
