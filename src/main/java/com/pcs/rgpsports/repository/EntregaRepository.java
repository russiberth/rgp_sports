package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    Optional<Entrega> findByPedidoId(Long pedidoId);

    Optional<Entrega> findByCodigoRastreio(String codigoRastreio);

    List<Entrega> findByStatusEntrega(String statusEntrega);
}
