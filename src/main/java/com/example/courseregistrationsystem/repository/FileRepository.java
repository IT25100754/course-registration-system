package com.example.courseregistrationsystem.repository;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Generic repository interface for all CRUD file operations.
 *
 * OOP Concepts:
 * - ABSTRACTION: Defines the contract without implementation
 * - POLYMORPHISM: All repositories implement these same methods
 *   but with their own entity-specific logic
 *
 * @param <T>  Entity type
 * @param <ID> Identifier type
 */
public interface FileRepository<T, ID> {

    // ─── CREATE ─────────────────────────────────────────────────────────────────
    T save(T entity) throws IOException;

    // ─── READ ────────────────────────────────────────────────────────────────────
    Optional<T> findById(ID id);
    List<T>     findAll();

    // ─── UPDATE ──────────────────────────────────────────────────────────────────
    T update(T entity) throws IOException;

    // ─── DELETE ──────────────────────────────────────────────────────────────────
    boolean deleteById(ID id) throws IOException;

    // ─── UTILITY ─────────────────────────────────────────────────────────────────
    boolean existsById(ID id);
    long    count();
}
