package br.com.fatec.catalogo.repositories;

import br.com.fatec.catalogo.models.CategoriaModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaModel, Long> {
    Optional<CategoriaModel> findByNomeIgnoreCase(String nome);
}
