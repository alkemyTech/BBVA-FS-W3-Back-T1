package com.bbva.wallet.dtos;

import java.sql.Timestamp;

public record FixedTermSimulateResponseDTO(Timestamp fechaCreacion, Timestamp fechaFinalizacion, Double montoInverito, Double interes, Double montoTotal) {
}
