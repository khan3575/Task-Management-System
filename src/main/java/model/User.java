package model;
import java.io.Serializable;
import java.time.LocalDateTime;


public class User implements Serializable{
	
 	
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String username;
	private String email;
	private String password;
	private LocalDateTime createdAt;
	
	public User()	{	} //no argument constructor to avoid issue.

	public User(String username, String email, String password)
	{
		this.username = username;
		this.email = email;
		this.password = password;
	}
	
	// Getter
	public Integer getId()
	{
		return id;
	}
	public String getUsername()
	{
		return username;
	}
	public String getEmail()
	{
		return email;
	}
	public LocalDateTime getCreate_At()
	{
		return createdAt;
	}
	
	
	// setters
	public void setId(Integer id)
	{
		this.id = id;
	}
	public void setUsername(String username)
	{
		this.username =username;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public void setCreated_at(LocalDateTime createdAt)
	{
		this.createdAt = createdAt;
	}
	
	
	
	//matches password 
	public Boolean passwordVerification(String currentPass)
	{
		return password != null && this.password.equals(currentPass);
		
	}
	public Boolean isPasswordNull()
	{
		return password != null;
	}
	
	
}
