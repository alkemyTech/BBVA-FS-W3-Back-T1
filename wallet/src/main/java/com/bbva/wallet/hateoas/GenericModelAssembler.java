package com.bbva.wallet.hateoas;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class GenericModelAssembler<T, M extends RepresentationModel<M>> extends RepresentationModelAssemblerSupport<T, M> {

    private Class<?> controllerClass;
    private Class<M> modelClass;

    public GenericModelAssembler(Class<?> controllerClass, Class<M> modelClass) {
        super(controllerClass, modelClass);
        this.controllerClass = controllerClass;
        this.modelClass = modelClass;
    }
    @Override
    public M toModel(T entity) {
        M model = instantiateModel(entity);
        // Map entity properties to model properties
        BeanUtils.copyProperties(entity, model);
        return model;
    }


    @Override
    public CollectionModel<M> toCollectionModel(Iterable<? extends T> entities) {

        List<M> entityModels = new ArrayList<>();
        entities.forEach(entity -> entityModels.add(toModel(entity)));

        CollectionModel<M> collectionModel = CollectionModel.of(entityModels);

        if (entities instanceof Page) {
            Page<T> page = (Page<T>)  entities;



            // Add link for the previous page if it exists
            if (page.hasPrevious()) {
                collectionModel.add(linkTo(controllerClass).slash("?page=" + page.previousPageable().getPageNumber()).withRel("prev"));
            }

            // Add link for the next page if it exists
            if (page.hasNext()) {
                collectionModel.add(linkTo(controllerClass).slash("?page=" + page.nextPageable().getPageNumber()).withRel("next"));

            }

        }

        return collectionModel;
    }
}
