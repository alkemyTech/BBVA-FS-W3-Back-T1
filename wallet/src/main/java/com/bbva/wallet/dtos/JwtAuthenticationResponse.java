package com.bbva.wallet.dtos;

import com.bbva.wallet.entities.User;
import lombok.*;


@Builder
public record JwtAuthenticationResponse (String token, User user) {
}
