//package com.bbva.wallet.utils;
//
//import com.bbva.wallet.entities.User;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//
//public class ExtractUser {
//    public static User extract(){
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return (User) authentication.getPrincipal();
//    }
//}
