package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

import java.util.Objects;

/**
 * Represent Delivery table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "delivery")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "way_id", nullable = false)
    private Way way;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn(name = "addressee_id", nullable = false)
    private User addressee;
    @Column(name = "is_package_received", columnDefinition = "BIT(1) default 0")
    private boolean isPackageReceived;
    @Column(nullable = false)
    private int weight;
    @OneToOne(cascade = CascadeType.REFRESH, mappedBy = "delivery", fetch = FetchType.LAZY)
    private Bill bill;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Delivery delivery = (Delivery) o;
        return getId() == delivery.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
