package org.example.controllers;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import org.example.exceptions.ProductNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import org.example.model.Brand;
import org.example.services.BrandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Stateless
@Path("/brands")
public class BrandController {

    private static final Logger logger = LoggerFactory.getLogger(BrandController.class);

    @Inject
    private BrandService brandService;

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBrandById(@PathParam("id") Long id) {
        Brand brand = brandService.getBrandById(id);
        return Response.ok(brand).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return Response.ok(brands).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createBrand(@Valid Brand brand, @Context UriInfo uriInfo) {
        validateBrandName(brand.getName());
        Brand createdBrand = brandService.addBrand(brand);
        UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder().path("/{id}");
        URI location = uriBuilder.build(createdBrand.getId());
        return Response.created(location).entity(createdBrand).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBrand(@PathParam("id") Long id, @Valid Brand brand) {
        validateBrandName(brand.getName());
        Brand updatedBrand = brandService.updateBrand(id, brand);
        return Response.ok(updatedBrand).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteBrand(@PathParam("id") Long id) {
        brandService.deleteBrand(id);
        return Response.noContent().build();
    }

    private void validateBrandName(@NotBlank @Pattern(regexp = "^[A-Za-z0-9]+$") String brandName) {
        if (brandService.isBrandNameExists(brandName)) {
            throw new IllegalArgumentException("Brand with this name already exists");
        }
    }

    @GET
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id) {
        if (brandService.isProductNotFound(id)) {
            throw new ProductNotFoundException("Product not found");
        }
        return Response.ok("Product found").build();
    }
}