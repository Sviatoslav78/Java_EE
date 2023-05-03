package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
/**
 * Represent Bill table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill {

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_id", referencedColumnName = "id", nullable = false)
    private Delivery delivery;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @Column(name="is_delivery_paid", columnDefinition = "BIT(1) default 0")
    private boolean isDeliveryPaid;

    @Column(name="cost_in_cents", nullable = false)
    private long costInCents;

    @Column(name = "date_of_pay")
    private LocalDate dateOfPay;

    @Override
    public String toString() {
        return "Bill{}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bill bill = (Bill) o;
        return id == bill.id &&
                isDeliveryPaid == bill.isDeliveryPaid &&
                costInCents == bill.costInCents &&
                user.equals(bill.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, isDeliveryPaid, costInCents, dateOfPay);
    }
}
