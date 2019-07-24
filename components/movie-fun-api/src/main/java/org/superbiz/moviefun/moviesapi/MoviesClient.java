package org.superbiz.moviefun.moviesapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.EntityType;
import java.util.List;

public class MoviesClient {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager entityManager;

    public MovieInfo find(Long id) {

        return entityManager.find(MovieInfo.class, id);
    }

    @Transactional
    public void addMovie(MovieInfo movieInfo) {
        logger.debug("Creating movie with title {}, and year {}", movieInfo.getTitle(), movieInfo.getYear());

        entityManager.persist(movieInfo);
    }

    @Transactional
    public void updateMovie(MovieInfo movieInfo) {
        entityManager.merge(movieInfo);
    }

    @Transactional
    public void deleteMovie(MovieInfo movieInfo) {
        entityManager.remove(movieInfo);
    }

    @Transactional
    public void deleteMovieId(long id) {
        MovieInfo movieInfo = entityManager.find(MovieInfo.class, id);
        deleteMovie(movieInfo);
    }

    public List<MovieInfo> getMovies() {
        CriteriaQuery<MovieInfo> cq = entityManager.getCriteriaBuilder().createQuery(MovieInfo.class);
        cq.select(cq.from(MovieInfo.class));
        return entityManager.createQuery(cq).getResultList();
    }

    public List<MovieInfo> findAll(int firstResult, int maxResults) {
        CriteriaQuery<MovieInfo> cq = entityManager.getCriteriaBuilder().createQuery(MovieInfo.class);
        cq.select(cq.from(MovieInfo.class));
        TypedQuery<MovieInfo> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public int countAll() {
        CriteriaQuery<Long> cq = entityManager.getCriteriaBuilder().createQuery(Long.class);
        Root<MovieInfo> rt = cq.from(MovieInfo.class);
        cq.select(entityManager.getCriteriaBuilder().count(rt));
        TypedQuery<Long> q = entityManager.createQuery(cq);
        return (q.getSingleResult()).intValue();
    }

    public int count(String field, String searchTerm) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = qb.createQuery(Long.class);
        Root<MovieInfo> root = cq.from(MovieInfo.class);
        EntityType<MovieInfo> type = entityManager.getMetamodel().entity(MovieInfo.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.select(qb.count(root));
        cq.where(condition);

        return entityManager.createQuery(cq).getSingleResult().intValue();
    }

    public List<MovieInfo> findRange(String field, String searchTerm, int firstResult, int maxResults) {
        CriteriaBuilder qb = entityManager.getCriteriaBuilder();
        CriteriaQuery<MovieInfo> cq = qb.createQuery(MovieInfo.class);
        Root<MovieInfo> root = cq.from(MovieInfo.class);
        EntityType<MovieInfo> type = entityManager.getMetamodel().entity(MovieInfo.class);

        Path<String> path = root.get(type.getDeclaredSingularAttribute(field, String.class));
        Predicate condition = qb.like(path, "%" + searchTerm + "%");

        cq.where(condition);
        TypedQuery<MovieInfo> q = entityManager.createQuery(cq);
        q.setMaxResults(maxResults);
        q.setFirstResult(firstResult);
        return q.getResultList();
    }

    public void clean() {
        entityManager.createQuery("delete from Movie").executeUpdate();
    }
}