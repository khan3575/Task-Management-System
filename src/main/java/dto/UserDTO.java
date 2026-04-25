package dto;

public class UserDTO {

    private Integer id;
    private String username;
    private String email;
    private String lastLoginDisplay; 

    public UserDTO() {}

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getLastLoginDisplay() {
        return lastLoginDisplay;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLastLoginDisplay(String lastLoginDisplay) {
        this.lastLoginDisplay = lastLoginDisplay;
    }
}