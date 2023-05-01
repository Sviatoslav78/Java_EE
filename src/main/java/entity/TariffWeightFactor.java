package entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;
/**
 * Represent TariffWeightFactor table in db
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tariff_weight_factor")
public class TariffWeightFactor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private long id;
    @Column(name = "min_weight_range", nullable = false)
    private int minWeightRange;
    @Column(name = "max_weight_range", nullable = false)
    private int maxWeightRange;
    @Column(name = "over_pay_on_kilometer", nullable = false)
    private int overPayOnKilometer;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "wayTariffs")
    private List<Way> waysWhereUsed;

    public TariffWeightFactor(int minWeightRange, int maxWeightRange, int overPayOnKilometer) {
        this.minWeightRange = minWeightRange;
        this.maxWeightRange = maxWeightRange;
        this.overPayOnKilometer = overPayOnKilometer;
    }
}
