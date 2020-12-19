package KG.Neobis.FMS.Entities.Users;

import KG.Neobis.FMS.Entities.BaseEntity;
import KG.Neobis.FMS.Entities.BaseEntityAudit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(name = "SEQ_ID", sequenceName = "SEQ_USERS", allocationSize = 1)
public class AppUsers extends BaseEntityAudit implements UserDetails {

    private String username;
    private String password;

    @Email
    private String email;


    private String number;
    private String recoveryCode;

    private Date expRecover;

    public AppUsers(String username, String password, @Email String email, Collection<Roles> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Roles> roles = new ArrayList<>();

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<Privilege> getAuthorities() {
        List<Privilege> privileges = new ArrayList<>();
        roles.forEach(role -> {
            privileges.addAll(role.getPrivileges());
        });
        return privileges;
    }
}
