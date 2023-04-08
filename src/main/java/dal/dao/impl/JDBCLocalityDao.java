package dal.dao.impl;

import dal.dao.LocalityDao;
import dal.dao.maper.ResultSetToEntityMapper;
import dal.entity.Locality;
import jakarta.ejb.Singleton;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Locale;

/**
 * Implements an interface for work with {@link Locality}
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */

@Singleton
public class JDBCLocalityDao extends JDBCAbstractGenericDao<Locality> implements LocalityDao {
    private static final String LOCALITY_FIND_ALL_RU = "locality.find.all.ru";
    private static final String LOCALITY_FIND_ALL_EN = "locality.find.all.en";
    private static final String LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_RU = "locality.get.find.by.locality.send.id.ru";
    private static final String LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_EN = "locality.get.find.by.locality.send.id.en";
    private static final String ID = "id";
    private static final String LOCALITY_NAME = "name";
    private static final Logger log = LogManager.getLogger(JDBCLocalityDao.class);


    @Override
    public List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
//        log.debug("findAllLocaliseLocalitiesWithoutConnection");
//
//        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
//        String localedQuery;
//        localedQuery = locale.getLanguage().equals(RUSSIAN_LANG_COD) ?
//                resourceBundleRequests.getString(LOCALITY_FIND_ALL_RU) :
//                resourceBundleRequests.getString(LOCALITY_FIND_ALL_EN);
//        try (ConnectionProxy connection = connector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
//            List<Locality> result;
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                result = new ArrayList<>();
//                while (resultSet.next()) {
//                    result.add(mapper.map(resultSet));
//                }
//            }
//            return result;
//        } catch (SQLException e) {
//            log.error("SQLException", e);
//            throw new DBRuntimeException();
//        }
        return null;
    }

    @Override
    public List<Locality> findLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id) {
//        ResultSetToEntityMapper<Locality> mapper = getLocaliseLocalityMapper(locale);
//        String localedQuery;
//        localedQuery = locale.getLanguage().equals(RUSSIAN_LANG_COD) ?
//                resourceBundleRequests.getString(LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_RU) :
//                resourceBundleRequests.getString(LOCALITY_GET_FIND_BY_LOCALITY_SEND_ID_EN);
//
//        try (ConnectionProxy connection = connector.getConnection();
//             PreparedStatement preparedStatement = connection.prepareStatement(localedQuery)) {
//            preparedStatement.setLong(1, id);
//            List<Locality> result;
//            try (ResultSet resultSet = preparedStatement.executeQuery()) {
//                result = new ArrayList<>();
//                while (resultSet.next()) {
//                    result.add(mapper.map(resultSet));
//                }
//            }
//            return result;
//        } catch (SQLException e) {
//            log.error("SQLException", e);
//            throw new DBRuntimeException();
//        }
        return null;
    }

    private ResultSetToEntityMapper<Locality> getLocaliseLocalityMapper(Locale locale) {
        return resultSet -> Locality.builder()
                .id(resultSet.getLong(ID))
                .nameRu(locale.getLanguage().equals(RUSSIAN_LANG_COD) ? resultSet.getString(LOCALITY_NAME) : null)
                .nameEn(!locale.getLanguage().equals(RUSSIAN_LANG_COD) ? resultSet.getString(LOCALITY_NAME) : null)
                .build();
    }

}