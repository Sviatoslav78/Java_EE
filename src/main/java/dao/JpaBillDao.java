package dao;

import dal.dao.BillDao;
import dal.exeption.AskedDataIsNotCorrect;
import entity.Bill;
import entity.Delivery;
import entity.User;
import jakarta.ejb.Singleton;
import jakarta.persistence.TypedQuery;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
@Singleton
public class JpaBillDao extends AbstractJpaDao<Bill> implements BillDao {

    public JpaBillDao() {
        super(Bill.class);
    }

    @Override
    public List<Bill> getInfoToPayBillByUserId(long userId, Locale locale) {
//      select bill.id as bill_id, bill.cost_in_cents as price, delivery.id as delivery_id, delivery.weight,
//      user.email as addressee_email, localiti_send.name_en as locality_sand_name,
//      localiti_get.name_en as locality_get_name from bill
//      join delivery on bill.delivery_id=delivery.id join way on delivery.way_id=way.id
//      join user on delivery.addressee_id=user.id join locality as localiti_send on way.locality_send_id=localiti_send.id
//      join  locality as localiti_get on way.locality_get_id=localiti_get.id
//      where bill.user_id = ? and bill.is_delivery_paid = false

        //to do check Query
        TypedQuery<Bill> query = entityManager.createQuery("SELECT b FROM Bill b JOIN FETCH Delivery  JOIN FETCH Way JOIN FETCH User JOIN FETCH Locality " +
                "WHERE b.user.id= :userId AND b.isDeliveryPaid=false", Bill.class);
        return query.setParameter("userId", userId).getResultList();


    }

    @Override
    public long getBillCostIfItIsNotPaid(long billId, long userId) throws AskedDataIsNotCorrect {
//      select bill.cost_in_cents from bill where bill.id= ? and bill.is_delivery_paid=false and bill.user_id = ?
        TypedQuery<Long> query = entityManager.createQuery("SELECT b.costInCents  FROM Bill b WHERE b.id=:billId AND b.user.id= :userId", Long.class);
        query.setParameter("billId", billId);
        query.setParameter("userId", userId);

        return query.getSingleResult();


    }

    @Override
    public List<Bill> getHistoricBillsByUserId(long userId, Integer offset, Integer limit) {
//SELECT bill.id as bill_id, bill.delivery_id, bill.is_delivery_paid, bill.cost_in_cents, bill.date_of_pay FROM bill
// where bill.user_id = ? and bill.is_delivery_paid=true ORDER BY bill.id DESC LIMIT ?, ?
        TypedQuery<Bill> query = entityManager.createQuery("SELECT b FROM Bill b WHERE b.user.id=:userId " +
                "AND  b.isDeliveryPaid=true ORDER BY b.id DESC ", Bill.class);
        query.setParameter("userId", userId);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @Override
    public boolean murkBillAsPayed(long billId) throws SQLException {
//        update bill set bill.is_delivery_paid=true, bill.date_of_pay = now() where bill.id = ?
        Bill bill = super.findById(billId);
        bill.setDeliveryPaid(true);
        bill.setDateOfPay(LocalDate.now());
        super.update(bill);
        return true;
    }

    @Override
    public boolean createBill(Delivery delivery, long costInCents, User user) {
//    public boolean createBill(long deliveryId, long userId, long localitySandID, long localityGetID, int weight) throws SQLException {
//    INSERT INTO bill (bill.cost_in_cents,bill.delivery_id, bill.user_id)
//    VALUES((SELECT (distance_in_kilometres*(price_for_kilometer_in_cents+over_pay_on_kilometer)) AS price
//    FROM way JOIN way_tariff_weight_factor ON way.id=way_tariff_weight_factor.way_id
//    JOIN tariff_weight_factor ON way_tariff_weight_factor.tariff_weight_factor_id=tariff_weight_factor.id
//    WHERE locality_send_id = ? AND locality_get_id = ? AND min_weight_range < ? AND max_weight_range >= ?),?,?);
        Bill bill = new Bill();
        super.create(bill);
        bill.setDelivery(delivery);
        delivery.setBill(bill);
        bill.setCostInCents(costInCents);
        bill.setUser(user);
        super.update(bill);
        return true;
    }

    @Override
    public long countAllBillsByUserId(long userId) {
//        SELECT COUNT(*) FROM bill WHERE bill.user_id=? and bill.is_delivery_paid=true
        TypedQuery<Long> query = entityManager.createQuery("SELECT COUNT(b) FROM Bill b WHERE b.user.id=:userId and b.isDeliveryPaid=true", Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }

}
