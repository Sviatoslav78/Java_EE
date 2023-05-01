package dao;

import dal.dao.LocalityDao;
import entity.Locality;
import jakarta.ejb.Singleton;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Locale;
@Stateless
public class JpaLocalityDao extends AbstractJpaDao<Locality> implements LocalityDao {

    public JpaLocalityDao() {
        super(Locality.class);
    }


    @Override
    public List<Locality> findAllLocaliseLocalitiesWithoutConnection(Locale locale) {
        // SELECT id, name_en AS name FROM locality
        return entityManager.createQuery("SELECT l FROM Locality l", Locality.class).getResultList();
    }

    @Override
    public List<Locality> findLocaliseLocalitiesGetByLocalitySendId(Locale locale, long id) {
//SELECT localityGet.id, localityGet.name_en AS name FROM locality AS localitySend JOIN
// way ON localitySend.id=way.locality_send_id JOIN locality AS localityGet ON way.locality_get_id=localityGet.id
// WHERE localitySend.id= ?

        TypedQuery<Locality> query = entityManager.createQuery("SELECT get FROM Locality send JOIN FETCH send.waysWhereThisLocalityIsSend way JOIN FETCH way.localityGet get WHERE send.id = :id", Locality.class);
      return query.setParameter("id", id).getResultList();
    }
}
