package net.java.webflux_security.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User model
 */
@Getter @Setter
@AllArgsConstructor@NoArgsConstructor
public class Userdata implements UserDetails {
    @Id
    private Long id;
    @NotBlank(message = "Firstname should not be empty")
    private String firstName;
    @NotBlank(message = "Lastname should not be empty")
    private String lastName;
    @NotBlank(message = "Password should not be empty")
    private String password;
    @Email(message = "Invalid email address")
    //@UniqueElements(message = "Should be unique")
    private String email;
    @NotBlank(message = "Address should not be empty")
    private String address;
    @NotEmpty( message = "Roles should not be empty")
    private List<@NotBlank(message = "Role should not be empty")String> roles;
    private Boolean enabled;

    private List<String> readAccess = new ArrayList<>();
    private List<String> editAccess = new ArrayList<>();
    private List<String> readPermission = new ArrayList<>();
    private List<String> editPermission = new ArrayList<>();

    public Userdata(Long id, String firstName, String lastName, String password, String email, String address, List<String> roles ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.address = address;
        this.roles = roles;
        this.enabled = true;
    }

    public Userdata(String email, String password, boolean enabled, List<String> asList) {
        this.enabled = enabled;
        this.email = email;
        this.password = password;
        this.roles = asList;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void addReadUserAccess(String username) {
        this.getReadAccess().add(username);
    }

    public void addEditorUserAccess(String username) {
        this.getEditAccess().add(username);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(authority -> new SimpleGrantedAuthority(authority)).collect(Collectors.toList());
    }

    @JsonIgnore
    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Userdata{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", roles=" + roles +
                ", enabled=" + enabled +
                '}';
    }

    public void removeReadUserAccess(String toUsername) {
        this.getReadAccess().remove(toUsername);
    }

    public void removeEditorUserAccess(String toUsername) {
        this.getEditAccess().remove(toUsername);
    }

    public void addReadUserPermission(String fromUsername) {
        this.getReadPermission().add(fromUsername);
    }

    public void addEditorUserPermission(String fromUsername) {
        this.getEditPermission().add(fromUsername);
    }

    public void removeReadUserPermission(String fromUsername) { this.getReadPermission().remove(fromUsername); }

    public void removeEditorUserPermission(String fromUsername) { this.getEditPermission().remove(fromUsername);
    }
}
