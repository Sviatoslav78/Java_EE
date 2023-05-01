package logiclayer.service.impl;


import dal.dao.UserDao;
import entity.User;
import dal.exeption.AskedDataIsNotCorrect;
import dto.LoginInfoDto;
import dto.RegistrationInfoDto;
import dto.UserStatisticDto;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import logiclayer.exeption.NoSuchUserException;
import logiclayer.exeption.OccupiedLoginException;
import logiclayer.exeption.ToMachMoneyException;
import logiclayer.service.PasswordEncoderService;
import logiclayer.service.UserService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import web.exception.OnClientSideProblemException;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements an interface for work with users
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@Stateless
public class UserServiceImpl implements UserService {
    private static final Logger log = LogManager.getLogger(UserService.class);
    @EJB
    private PasswordEncoderService passwordEncoderService;
    @EJB
    private UserDao userDao;


    @Override
    public User loginUser(LoginInfoDto loginInfoDto) throws NoSuchUserException {
        log.debug("loginInfoDto -" + loginInfoDto);

        return userDao.findByEmailAndPasswordWithPermissions(loginInfoDto.getUsername(),
                        passwordEncoderService.encode(loginInfoDto.getPassword()))
                .orElseThrow(NoSuchUserException::new);
    }

    @Transactional
    @Override
    public boolean addNewUserToDB(RegistrationInfoDto registrationInfoDto) throws OccupiedLoginException {
        log.debug("registrationInfoDto -" + registrationInfoDto);

        try {
            return userDao.save(registrationInfoDto.getUsername(), passwordEncoderService.encode(registrationInfoDto.getPassword()));
        } catch (AskedDataIsNotCorrect askedDataIsNotCorrect) {
            log.error("login is occupied", askedDataIsNotCorrect);

            throw new OccupiedLoginException();
        }

    }

    /**
     * @throws ToMachMoneyException if after replenish will be overload long
     */
    @Override
    //@Transaction
    public void replenishAccountBalance(long userId, long amountMoney) throws NoSuchUserException, ToMachMoneyException {
        log.debug("userId -" + userId + " amountMoney -" + amountMoney);

        try {
            userDao.replenishUserBalance(userId, amountMoney);
        } catch (EntityNotFoundException ex) {
            log.error("no user", ex);
            throw new NoSuchUserException();

        }
    }

    @Override
    public long getUserBalance(long userId) {
        try {
            return userDao.getUserBalanceByUserID(userId);
        } catch (AskedDataIsNotCorrect | EntityNotFoundException ex) {
            log.error("Problems with db user must be correct", ex);
            throw new OnClientSideProblemException();
        }
    }

    @Override
    public List<UserStatisticDto> getAllUsers() {
        return userDao.getAllUsers().stream()
                .map(getUserUserStatisticDtoMapper())
                .collect(Collectors.toList());
    }

    private Function<User, UserStatisticDto> getUserUserStatisticDtoMapper() {
        return user -> UserStatisticDto.builder()
                .email(user.getEmail())
                .password(user.getPassword())
                .roleType(user.getRoleType().name())
                .build();
    }
}
