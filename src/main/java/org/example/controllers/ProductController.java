package org.example.controllers;

import org.example.exceptions.BrandNotFoundException;
import org.example.model.Product;
import org.example.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.URI;
import java.util.List;

@Stateless
@Path("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Inject
    private ProductService productService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addProduct(@Valid Product product, @Context UriInfo uriInfo) {
        validateProductName(product.getName());
        Product createdProduct = productService.addProduct(product);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path("/{id}");
        URI location = uriBuilder.build(createdProduct.getId());
        return Response.created(location).entity(createdProduct).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product getProductById(@PathParam("id") Long id) {
        return productService.getProductById(id);
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Product updateProduct(@PathParam("id") Long id, @Valid Product product) {
        validateProductName(product.getName());
        return productService.updateProduct(id, product);
    }

    @DELETE
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id) {
        productService.deleteProduct(id);
        return Response.noContent().build();
    }

    private void validateProductName(@NotBlank @Pattern(regexp = "^[A-Za-z0-9]+$") String productName) {
        if (productService.isProductNameExists(productName)) {
            throw new IllegalArgumentException("Product with this name already exists");
        }
    }

    @GET
    @Path("/{id}")
    public Response getBrandById(@PathParam("id") Long id) {
        if (productService.isBrandNotFound(id)) {
            throw new BrandNotFoundException("Brand not found");
        }
        return Response.ok("Brand found").build();
    }
}