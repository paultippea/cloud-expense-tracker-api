package com.paultippe.expense.api;

import com.paultippe.expense.domain.Expense;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Path("/api/expenses")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ExpenseResource {

    @GET
    public List<Expense> listAll() {
        return Expense.listAll();
    }

    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long id) {
        Expense expense = Expense.findById(id);
        if (expense == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.ok(expense).build();
    }

    @POST
    @Transactional
    public Response create(Expense request) {
        if (request.date == null) {
            request.date = LocalDate.now();
        }
        if (request.amount == null) {
            request.amount = BigDecimal.ZERO;
        }
        request.persist();
        return Response.status(Response.Status.CREATED).entity(request).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response update(@PathParam("id") Long id, Expense request) {
        Expense existing = Expense.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        existing.description = request.description;
        existing.amount = request.amount != null ? request.amount : existing.amount;
        existing.category = request.category;
        existing.date = request.date != null ? request.date : existing.date;

        return Response.ok(existing).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(@PathParam("id") Long id) {
        Expense existing = Expense.findById(id);
        if (existing == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        existing.delete();
        return Response.noContent().build();
    }
}
