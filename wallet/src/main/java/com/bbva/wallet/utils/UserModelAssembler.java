package com.bbva.wallet.utils;

import com.bbva.wallet.controllers.UserController;
import com.bbva.wallet.entities.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler extends RepresentationModelAssemblerSupport<User, UserModel> {
    public UserModelAssembler() {
        super(UserController.class, UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel userModel = instantiateModel(entity);

        userModel.setId(entity.getId());
        userModel.setFirstName(entity.getFirstName());
        userModel.setLastName(entity.getLastName());
        userModel.setEmail(entity.getEmail());

//        // Agrega enlaces (links) relevantes al modelo de representación
//        userModel.add(linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel());
//        // Agrega más enlaces según sea necesario

        return userModel;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
        CollectionModel<UserModel> collectionModel = super.toCollectionModel(entities);

        // Modificar los enlaces en el CollectionModel
        collectionModel.removeLinks();
//        collectionModel.add(linkTo(methodOn(UserController.class).getAll(null)).withSelfRel());

        // Otros enlaces o modificaciones adicionales según sea necesario
        // ...

        return collectionModel;
    }
}
