package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CupomRepository extends JpaRepository<Cupom, Long> {

    Optional<Cupom> findByCodigo(String codigo);

    List<Cupom> findByStatus(String status);

    boolean existsByCodigo(String codigo);
}
