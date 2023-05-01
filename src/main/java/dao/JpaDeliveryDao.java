package dao;

import dal.dao.DeliveryDao;
import dal.exeption.AskedDataIsNotCorrect;
import entity.Delivery;
import entity.User;
import entity.Way;
import jakarta.ejb.Singleton;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
@Singleton
public class JpaDeliveryDao extends AbstractJpaDao<Delivery> implements DeliveryDao {
    public JpaDeliveryDao() {
        super(Delivery.class);
    }

    @Override
    public List<Delivery> getDeliveryInfoToGet(long userId, Locale locale) {
        //     select delivery.id, user.email, localiti_sand.name_ru as locality_sand_name, localiti_get.name_ru as locality_get_name
        //     from delivery join way on delivery.way_id=way.id join bill on bill.delivery_id= delivery.id join user
        //     on bill.user_id=user.id join locality as localiti_sand on way.locality_send_id=localiti_sand.id join
        //     locality as localiti_get on way.locality_get_id=localiti_get.id where delivery.addressee_id = ?
        //     and delivery.is_package_received = false and bill.is_delivery_paid=true
        //----language is ignored
        TypedQuery<Delivery> allDeliveryQuery = entityManager.createQuery("SELECT d FROM Delivery d JOIN FETCH Bill b where b.user.id =:userId AND d.isPackageReceived=false AND b.isDeliveryPaid=true", Delivery.class);
        allDeliveryQuery.setParameter("userId", userId);
        return allDeliveryQuery.getResultList();
    }

    @Override
    public boolean confirmGettingDelivery(long userId, long deliveryId) {
        // update delivery set delivery.is_package_received=true where delivery.addressee_id=? and delivery.id=?
        // addressee_id NOT NEEDED. delivery_id already unique record!!!!
        Delivery delivery = super.findById(deliveryId);
        delivery.setPackageReceived(true);
        super.update(delivery);
        return true;
    }

    @Override
    public long createDelivery(User user, Way way, int weight) throws AskedDataIsNotCorrect {
        // INSERT INTO delivery (delivery.addressee_id, delivery.way_id, delivery.weight) VALUES ((SELECT user.id FROM user WHERE user.email= ? ), (SELECT way.id FROM way WHERE way.locality_send_id= ? AND way.locality_get_id= ?), ?)

        Delivery delivery = new Delivery();
        delivery.setAddressee(user);
        delivery.setWay(way);
        delivery.setWeight(weight);
        super.create(delivery);
        entityManager.flush();
        // orElseThrow(AskedDataIsNotCorrect::new) can be removed, need to change interface
        return Optional.of(delivery.getId()).orElseThrow(AskedDataIsNotCorrect::new);
    }
}
