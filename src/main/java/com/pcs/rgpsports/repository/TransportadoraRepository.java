package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Transportadora;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransportadoraRepository extends JpaRepository<Transportadora, Long> {

    Optional<Transportadora> findByCnpj(String cnpj);

    boolean existsByCnpj(String cnpj);
}
