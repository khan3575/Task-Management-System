CREATE DATABASE task_management_system;
USE task_management_system;
 

CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
 

CREATE TABLE tasks (
    id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    priority VARCHAR(10) DEFAULT 'MEDIUM',
    status VARCHAR(20) DEFAULT 'PENDING',
    due_date DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, email, password) VALUES 
('admin', 'admin@example.com', 'admin123'),
('mehedi', 'mehedi@example.com', '123456'),
('mahmud', 'mahmud@example.com', '123456'),
('fahim', 'fahim@example.com', '123456'),
('neky', 'neky@example.com', '123456'),
('sakib', 'sakib@example.com', '123456');
 
INSERT INTO tasks (title, description, priority, status, due_date) VALUES
('Complete Project Structure', 'Design the complete MVC architecture', 'HIGH', 'COMPLETED', '2024-01-15'),
('Setup Database', 'Create database schema and tables', 'HIGH', 'COMPLETED', '2024-01-16'),
('Implement Login', 'Create login servlet with session', 'HIGH', 'IN_PROGRESS', '2024-01-20'),
('Design Dashboard', 'Create dashboard with pagination', 'MEDIUM', 'PENDING', '2024-01-25'),
('Add Task Feature', 'Implement add task functionality', 'MEDIUM', 'PENDING', '2024-01-22'),
('Search Feature', 'Multi-field search implementation', 'LOW', 'PENDING', '2024-01-30');

