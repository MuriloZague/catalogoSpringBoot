package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<CategoriaModel> listarTodas() {
        return categoriaRepository.findAll();
    }

    public CategoriaModel buscarPorId(Long idCategoria) {
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada: " + idCategoria));
    }

    @Transactional
    public void salvar(CategoriaModel categoria) {
        String nome = categoria.getNome() == null ? "" : categoria.getNome().trim();

        if (nome.length() < 3 || nome.length() > 80) {
            throw new IllegalArgumentException("Nome da categoria deve ter entre 3 e 80 caracteres.");
        }

        var existente = categoriaRepository.findByNomeIgnoreCase(nome);

        if (existente.isPresent() && !existente.get().getIdCategoria().equals(categoria.getIdCategoria())) {
            throw new IllegalArgumentException("Ja existe uma categoria com esse nome.");
        }

        categoria.setNome(nome);
        categoriaRepository.save(categoria);
    }

    @Transactional
    public void excluir(Long idCategoria) {
        long totalProdutos = produtoRepository.countByCategoria_IdCategoria(idCategoria);

        if (totalProdutos > 0) {
            throw new IllegalArgumentException("Nao e possivel excluir categoria em uso por produtos.");
        }

        categoriaRepository.deleteById(idCategoria);
    }
}
