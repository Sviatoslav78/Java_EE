package dao;

import dal.dao.WayDao;
import dal.dto.DeliveryCostAndTimeDto;
import entity.TariffWeightFactor;
import entity.Way;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class JpaWayDao extends AbstractJpaDao<Way> implements WayDao {
    public JpaWayDao() {
        super(Way.class);
    }

    @Override
    public Optional<DeliveryCostAndTimeDto> findByLocalitySandIdAndLocalityGetId(long localitySandID, long localityGetID, int weight) {
        //SELECT time_on_way_in_days, (distance_in_kilometres*(price_for_kilometer_in_cents+over_pay_on_kilometer)) AS price FROM way JOIN way_tariff_weight_factor ON way.id=way_tariff_weight_factor.way_id JOIN tariff_weight_factor ON way_tariff_weight_factor.tariff_weight_factor_id=tariff_weight_factor.id WHERE locality_send_id = ? AND locality_get_id = ? AND min_weight_range < ? AND max_weight_range >= ?
        TypedQuery<Way> query = entityManager.createQuery("SELECT w FROM Way w JOIN FETCH TariffWeightFactor twf WHERE w.localitySand= :localitySandID AND w.localityGet= :localityGetID AND twf.minWeightRange < :weight AND  twf.maxWeightRange >= :weight", Way.class);
        query.setParameter("localitySandID", localitySandID);
        query.setParameter("localityGetID", localityGetID);
        query.setParameter("weight", weight);
        Optional<Way> optionalWay = Optional.ofNullable(query.getSingleResult());
        if (optionalWay.isPresent()) {
            Way way = optionalWay.get();
            int distanceInKilometres = way.getDistanceInKilometres();
            int priceForKilometerInCents = way.getPriceForKilometerInCents();
            List<Integer> overPayOnKilometerList = way.getWayTariffs().stream().map(TariffWeightFactor::getOverPayOnKilometer).collect(Collectors.toList());
            Integer overPayOnKilometer = overPayOnKilometerList.get(0);
            long cost = (long) distanceInKilometres * (priceForKilometerInCents + overPayOnKilometer);
            DeliveryCostAndTimeDto deliveryCostAndTimeDto = new DeliveryCostAndTimeDto(cost, way.getTimeOnWayInDays());
            return Optional.of(deliveryCostAndTimeDto);
        }

        return Optional.empty();
    }
}
