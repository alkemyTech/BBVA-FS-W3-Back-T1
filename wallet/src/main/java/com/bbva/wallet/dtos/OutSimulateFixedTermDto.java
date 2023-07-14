package com.bbva.wallet.dtos;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public record OutSimulateFixedTermDto (Timestamp fechaCreacion, Timestamp fechaFinalizacion, Double montoInverito, Double interes, Double montoTotal) {
}
