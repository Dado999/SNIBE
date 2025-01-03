package com.example.models.entities;

import com.example.repositories.CommentRepository;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "`user`")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iduser", nullable = false)
    private Integer iduser;

    @Basic
    @Column(name = "name", nullable = false, length = 45)
    private String name;

    @Basic
    @Column(name = "surname", nullable = false, length = 45)
    private String surname;

    @Basic
    @Column(name = "username", nullable = false, length = 45)
    private String username;

    @Basic
    @Column(name = "password", nullable = false, length = 45)
    private String password;

    @Basic
    @Column(name = "email", nullable = false, length = 45)
    private String email;

    @Basic
    @Column(name = "reguser", nullable = false)
    private Byte reguser;

    @Basic
    @Column(name = "permission", nullable = false, length = 50)
    private String permission;

    @Basic
    @Column(name = "role", nullable = false, length = 50)
    private String role;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(iduser, user.iduser) && Objects.equals(name, user.name) && Objects.equals(surname, user.surname) && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email) && Objects.equals(reguser, user.reguser) && Objects.equals(permission, user.permission) && Objects.equals(role, user.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iduser, name, surname, username, password, email, reguser, permission, role);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(getRole()));
    }
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return getReguser() == 1;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setId(Integer id){
        this.iduser = id;
    }
}
