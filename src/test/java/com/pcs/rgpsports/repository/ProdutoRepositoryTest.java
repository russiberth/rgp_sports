package com.pcs.rgpsports.repository;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.pcs.rgpsports.model.Produto;

@DataJpaTest
class ProdutoRepositoryTest {

    @Autowired
    private TestEntityManager em;

    @Autowired
    private ProdutoRepository repository;

    private Produto novoProduto(String time, String modalidade, Integer estoque, String status) {
        return Produto.builder()
                .time(time)
                .modalidade(modalidade)
                .estoque(estoque)
                .tamanho("M")
                .genero("Masculino")
                .preco(new BigDecimal("199.90"))
                .status(status)
                .build();
    }

    @Test
    void deveSalvarEBuscarPorId() {
        Produto salvo = repository.save(novoProduto("Flamengo", "Futebol", 10, "ativo"));
        Produto encontrado = repository.findById(salvo.getId()).orElseThrow();
        assertThat(encontrado.getTime()).isEqualTo("Flamengo");
        assertThat(encontrado.getPreco()).isEqualByComparingTo("199.90");
    }

    @Test
    void deveBuscarPorStatus() {
        em.persist(novoProduto("Flamengo", "Futebol", 10, "ativo"));
        em.persist(novoProduto("Lakers", "Basquete", 5, "inativo"));
        em.flush();

        List<Produto> ativos = repository.findByStatus("ativo");
        assertThat(ativos).hasSize(1);
        assertThat(ativos.get(0).getStatus()).isEqualTo("ativo");
    }

    @Test
    void deveBuscarPorModalidade() {
        em.persist(novoProduto("Flamengo", "Futebol", 10, "ativo"));
        em.persist(novoProduto("Lakers", "Basquete", 5, "ativo"));
        em.flush();

        List<Produto> futebol = repository.findByModalidade("Futebol");
        assertThat(futebol).hasSize(1);
        assertThat(futebol.get(0).getModalidade()).isEqualTo("Futebol");
    }

    @Test
    void deveBuscarPorTimeContendoIgnorandoCase() {
        em.persist(novoProduto("Flamengo", "Futebol", 10, "ativo"));
        em.flush();

        List<Produto> r = repository.findByTimeContainingIgnoreCase("flamengo");
        assertThat(r).hasSize(1);
    }

    @Test
    void deveListarProdutosSemEstoque() {
        em.persist(novoProduto("Flamengo", "Futebol", 0, "ativo"));
        em.persist(novoProduto("Lakers", "Basquete", 7, "ativo"));
        em.flush();

        List<Produto> semEstoque = repository.findProdutosSemEstoque();
        assertThat(semEstoque).hasSize(1);
        assertThat(semEstoque.get(0).getEstoque()).isZero();
    }
}