package org.example.repository;

import org.example.model.Brand;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@javax.ejb.LocalBean
@javax.ejb.Stateless
public class BrandRepository {

    @PersistenceContext(unitName = "example-pu")
    private EntityManager em;

    public List<Brand> findAll() {
        String jpql = "SELECT b FROM Brand b";
        TypedQuery<Brand> query = em.createQuery(jpql, Brand.class);
        return query.getResultList();
    }
    public Brand save(Brand brand) {
        if (brand.getId() == null) {
            em.persist(brand);
            return brand;
        } else {
            throw new IllegalArgumentException("New brand cannot already have an ID.");
        }
    }

    public Optional<Brand> findById(Long id) {
        return Optional.ofNullable(em.find(Brand.class, id));
    }

    public boolean existsByName(String name) {
        Query query = em.createQuery("SELECT COUNT(b) FROM Brand b WHERE b.name = :name");
        query.setParameter("name", name);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    public void deleteById(Long id) {
        Brand brand = em.find(Brand.class, id);
        if (brand != null) {
            em.remove(brand);
        }
    }

    public void customDeleteById(Long id) {
        Query query = em.createQuery("DELETE FROM Brand b WHERE b.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}
