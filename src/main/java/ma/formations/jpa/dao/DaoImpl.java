package ma.formations.jpa.dao;

import ma.formations.jpa.dtos.UserDto;
import ma.formations.jpa.dtos.UsersByAgeAndCityDto;
import ma.formations.jpa.dtos.UsersByCityDto;
import ma.formations.jpa.model.Address;
import ma.formations.jpa.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class DaoImpl implements IDao {
    private static final Logger log = LogManager.getLogger(DaoImpl.class);
    private EntityManager session;
    private EntityTransaction tx;

    @Override
    public void save(User user) {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            tx = session.getTransaction();
            tx.begin();
            session.merge(user);
            tx.commit();
            log.info("{} ajouté avec succés", user);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("erreur dans save() : {}", e);
            if (tx != null)
                tx.rollback();
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public List<UserDto> findUsersByRole(String role) {
        ModelMapper modelMapper = new ModelMapper();

        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();

            //Obtenir le Builder de la requête.
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            //Définir la structure de la requête en précisant le type des données à récupérer.
            // Ici, c'est les instances de type User.
            CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);

            // Définir la racine de la requête
            Root<User> userRoot = criteriaQuery.from(User.class);

            // Sélectionner tous les utilisateurs
            criteriaQuery.select(userRoot);

            //Ajouter une condition WHERE pour filtrer les résultats en fonction du rôle de l'utilisateur
            criteriaQuery.where(criteriaBuilder.equal(userRoot.get("role"), role));

            // Créer la requête typée.
            TypedQuery<User> query = session.createQuery(criteriaQuery);
            log.info("findUsersByRole({}) OK", role);
            List<User> bos = query.getResultList();
            if (bos == null || bos.isEmpty())
                return null;
            return bos.stream().map(bo -> modelMapper.map(bo, UserDto.class)).toList();
        } catch (Exception e) {
            log.error("Erreur dans findUsersByRole : {} ", e);
            return null;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public Double getAverageAge() {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();

            //Définir la structure de la requête en précisant le type des données à récupérer.
            // Ici, c'est les instances de type Double.
            CriteriaQuery<Double> criteriaQuery = criteriaBuilder.createQuery(Double.class);

            Root<User> userRoot = criteriaQuery.from(User.class);

            //Permet de sélectionner la moyenne (AVG) de l'attribut age de l'entité User.
            //Son équivalent en SQL : SELECT AVG(age) FROM user;
            criteriaQuery.select(criteriaBuilder.avg(userRoot.get("age")));

            TypedQuery<Double> query = session.createQuery(criteriaQuery);
            log.info("getAverageAge() OK");
            return query.getSingleResult();
        } catch (Exception e) {
            log.error("Erreur dans getAverageAge : {} ", e);
            return null;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public List<UsersByCityDto> countUsersByCity() {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UsersByCityDto> criteriaQuery = criteriaBuilder.createQuery(UsersByCityDto.class);
            Root<User> userRoot = criteriaQuery.from(User.class);

            //Joindre la table ADDRESS à la table USER
            Join<User, Address> addressJoin = userRoot.join("addresses");

            // Sélectionner la ville et le COUNT des utilisateurs
            criteriaQuery.multiselect(addressJoin.get("city"), criteriaBuilder.count(userRoot));

            // Ajouter la condition WHERE pour filtrer par ville si vous voulez chercher par une ville donnée
            //     criteriaQuery.where(criteriaBuilder.equal(addressJoin.get("city"), city));

            // Grouper par ville
            criteriaQuery.groupBy(addressJoin.get("city"));

            TypedQuery<UsersByCityDto> query = session.createQuery(criteriaQuery);
            log.info("countUsersByCity() OK");
            return query.getResultList();
        } catch (Exception e) {
            log.error("Erreur dans countUsersByCity : {} ", e);
            return null;
        } finally {
            if (session != null)
                session.close();
        }
    }

    @Override
    public List<UsersByAgeAndCityDto> usersByAgeAndCity(Integer minAge, String city) {
        try {
            session = SessionBuilder.getSessionfactory().createEntityManager();
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UsersByAgeAndCityDto> criteriaQuery = criteriaBuilder.createQuery(UsersByAgeAndCityDto.class);

            Root<User> userRoot = criteriaQuery.from(User.class);

            //Joindre la table ADDRESS à la table USER
            Join<User, Address> addressJoin = userRoot.join("addresses");

            //Créer deux parameteres
            ParameterExpression<Integer> ageParam = criteriaBuilder.parameter(Integer.class);
            ParameterExpression<String> cityParam = criteriaBuilder.parameter(String.class);

            //Créer un prédicat pour définir la condition suivante :
            //Sélectionner les utilisateurs dont l'âge est supérieur à une valeur donnée.
            Predicate agePredicate = criteriaBuilder.gt(userRoot.get("age"), ageParam);

            //Créer un prédicat pour définir la condition suivante :
            //Sélectionner les utilisateurs qui habitent dans une ville donnée.
            Predicate cityPredicate = criteriaBuilder.equal(addressJoin.get("city"), cityParam);

            //Appliquer deux conditions (WHERE) en les combinant avec un AND par défaut
            criteriaQuery.where(agePredicate, cityPredicate);

            //  Définir les colonnes à sélectionner
            criteriaQuery.multiselect(
                    userRoot.get("firstName"),
                    userRoot.get("lastName"),
                    addressJoin.get("city"),
                    userRoot.get("age"));

            criteriaQuery.groupBy(userRoot.get("firstName"), userRoot.get("lastName"), addressJoin.get("city"));
            criteriaQuery.orderBy(criteriaBuilder.asc(addressJoin.get("city")));

            // Créer la requête typée et lui passer les parametres.
            TypedQuery<UsersByAgeAndCityDto> query = session.createQuery(criteriaQuery);
            query.setParameter(ageParam, minAge);
            query.setParameter(cityParam, city);

            log.info("usersByAgeAndCity({},{}) OK", minAge, city);
            return query.getResultList();

        } catch (Exception e) {
            log.error("Erreur dans usersByAgeAndCity : {} ", e);
            return null;
        } finally {
            if (session != null)
                session.close();
        }
    }
}
