package dal.dao;

import dao.JpaCrudDao;
import entity.Delivery;
import dal.exeption.AskedDataIsNotCorrect;
import entity.User;
import entity.Way;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Declares an interface for work with {@link Delivery}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface DeliveryDao extends JpaCrudDao<Delivery> {

    List<Delivery> getDeliveryInfoToGet(long userId, Locale locale);

    boolean confirmGettingDelivery(long userId, long deliveryId);

    /**
     * @throws AskedDataIsNotCorrect if noting found
     */
    Delivery createDelivery(User user, Way way, int weight) throws AskedDataIsNotCorrect;

}
