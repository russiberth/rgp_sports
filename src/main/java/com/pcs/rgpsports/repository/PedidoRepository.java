package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByUsuarioId(Long usuarioId);

    List<Pedido> findByStatus(String status);

    List<Pedido> findByUsuarioIdAndStatus(Long usuarioId, String status);
}
