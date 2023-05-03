package dao;

import dal.dao.UserDao;
import dal.exeption.AskedDataIsNotCorrect;
import entity.RoleType;
import entity.User;
import jakarta.ejb.Singleton;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import logiclayer.exeption.ToMachMoneyException;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Singleton
public class JpaUserDao extends AbstractJpaDao<User> implements UserDao {

    private static final String FIND_ENABLED_USER_BY_EMAIL = "SELECT u FROM User u WHERE u.email = :email AND u.password = :password AND u.accountNonExpired = true AND u.accountNonLocked = true AND u.credentialsNonExpired = true AND u.enabled = true";

    public JpaUserDao() {
        super(User.class);
    }

    @Override
    public Optional<User> findByEmailAndPasswordWithPermissions(String email, String password) {
        //SELECT * FROM user WHERE user.email = ? AND user.password = ? AND user.account_non_expired = true AND user.account_non_locked = true AND user.credentials_non_expired = true AND user.enabled = true
        TypedQuery<User> query = entityManager.createQuery(FIND_ENABLED_USER_BY_EMAIL, User.class);
        query.setParameter("email", email);
        query.setParameter("password", password);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }

    @Override
    public boolean save(String email, String password) throws AskedDataIsNotCorrect {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setRoleType(RoleType.ROLE_USER);
        user.setEnabled(true);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setCredentialsNonExpired(true);
        super.create(user);
        return true;
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public boolean replenishUserBalance(long userId, long amountMoneyToAdd) throws ToMachMoneyException {
        User user = super.findById(userId);
        long currentMoneyInCents = user.getUserMoneyInCents();
        long newAmountOfMoney = currentMoneyInCents + amountMoneyToAdd;
        if (newAmountOfMoney <= 0) {
            throw new ToMachMoneyException();
        }
        user.setUserMoneyInCents(newAmountOfMoney);
        update(user);
        return true;
    }

    @Override
    public boolean withdrawUserBalanceOnSumIfItPossible(long userId, long sumWhichUserNeed) throws SQLException {
//        update user set user.user_money_in_cents = user.user_money_in_cents-? where user.id=? and user.user_money_in_cents > ?
        User user = super.findById(userId);
        long userMoneyInCents = user.getUserMoneyInCents();
        if (userMoneyInCents > sumWhichUserNeed) {
            long userNewAmountOfMoney = userMoneyInCents - sumWhichUserNeed;
            user.setUserMoneyInCents(userNewAmountOfMoney);
            super.update(user);
            return true;
        }
        return false;
    }

    @Override
    public long getUserBalanceByUserID(long userId) throws AskedDataIsNotCorrect {
        User byId = super.findById(userId);
        return byId.getUserMoneyInCents();
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        TypedQuery<User> query = entityManager.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        User user = query.getSingleResult();
        return Optional.ofNullable(user);
    }



}
