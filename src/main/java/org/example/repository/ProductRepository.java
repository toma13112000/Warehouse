package org.example.repository;

import org.example.model.Product;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;

@javax.ejb.LocalBean
@javax.ejb.Stateless
public class ProductRepository {

    @PersistenceContext(unitName = "example-pu")
    private EntityManager em;

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(em.find(Product.class, id));
    }

    public List<Product> findAll() {
        return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
    }

    public Product save(Product product) {
        if (product.getId() == null) {
            em.persist(product);
            return product;
        } else {
            return em.merge(product);
        }
    }

    public boolean existsByName(String name) {
        Query query = em.createQuery("SELECT COUNT(p) FROM Product p WHERE p.name = :name");
        query.setParameter("name", name);
        Long count = (Long) query.getSingleResult();
        return count > 0;
    }

    public void deleteById(Long id) {
        Product product = em.find(Product.class, id);
        if (product != null) {
            em.remove(product);
        }
    }

    public void customDeleteById(Long id) {
        Query query = em.createQuery("DELETE FROM Product p WHERE p.id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
    }
}