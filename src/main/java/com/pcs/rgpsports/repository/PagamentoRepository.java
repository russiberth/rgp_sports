package com.pcs.rgpsports.repository;

import com.pcs.rgpsports.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {

    Optional<Pagamento> findByPedidoId(Long pedidoId);

    Optional<Pagamento> findByIdTransacaoGateway(String idTransacaoGateway);

    List<Pagamento> findByStatusTransacao(String statusTransacao);

    List<Pagamento> findByMetodo(String metodo);
}
