package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.List;
import java.util.Objects;

/**
 * Represent User table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "user",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    @Column(name = "role", nullable = false, columnDefinition = "VARCHAR(255) default 'ROLE_USER'")
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false, columnDefinition = "BIGINT default 0")
    private long userMoneyInCents;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "addressee")
    private List<Delivery> deliveriesForUser;

    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean accountNonExpired;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean accountNonLocked;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean credentialsNonExpired;
    @Column(nullable = false, columnDefinition = "BIT(1) default 1")
    private boolean enabled;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
    private List<Bill> bills;

    public String getUsername() {
        return email;
    }

    @Override
    public String toString() {
        return "User{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id &&
                accountNonExpired == user.accountNonExpired &&
                accountNonLocked == user.accountNonLocked &&
                credentialsNonExpired == user.credentialsNonExpired &&
                enabled == user.enabled &&
                email.equals(user.email) &&
                roleType == user.roleType &&
                userMoneyInCents == user.userMoneyInCents &&
                password.equals(user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, roleType, password, userMoneyInCents, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }
}
