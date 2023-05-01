package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;
/**
 * Represent Way table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "way",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"locality_send_id", "locality_get_id"})})
public class Way {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_send_id", nullable = false)
    private Locality localitySand;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "locality_get_id", nullable = false)
    private Locality localityGet;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "way")
    private List<Delivery> deliveries;
    @Column(name = "distance_in_kilometres", nullable = false)
    private int distanceInKilometres;
    @Column(name = "time_on_way_in_days", nullable = false)
    private int timeOnWayInDays;
    @Column(name = "price_for_kilometer_in_cents", nullable = false)
    private int priceForKilometerInCents;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "way_tariff_weight_factor",
            joinColumns = @JoinColumn(name = "way_id"),
            inverseJoinColumns = @JoinColumn(name = "tariff_weight_factor_id"))
    private List<TariffWeightFactor> wayTariffs;

    @Override
    public String toString() {
        return "Way{";
    }
}
