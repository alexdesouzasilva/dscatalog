package br.com.devsenior.dscatalog.resources.exceptions;

import java.time.Instant;

public record StandardError(
    Instant timestamp,
    int status,
    String message,
    String path) {} 
    


