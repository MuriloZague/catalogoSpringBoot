package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository repository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<ProdutoModel> listarTodos() {
        return repository.findAll();
    }

    public List<ProdutoModel> buscarPorNome(String nome){
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public List<ProdutoModel> filtrar(String busca, Long idCategoria) {
        boolean temBusca = busca != null && !busca.isBlank();
        boolean temCategoria = idCategoria != null;

        if (temBusca && temCategoria) {
            return repository.findByNomeContainingIgnoreCaseAndCategoria_IdCategoria(busca.trim(), idCategoria);
        }

        if (temBusca) {
            return repository.findByNomeContainingIgnoreCase(busca.trim());
        }

        if (temCategoria) {
            return repository.findByCategoria_IdCategoria(idCategoria);
        }

        return repository.findAll();
    }

    public ProdutoModel buscarPorId(long id){
        return repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Produto não encontrado: " + id));
    }

    @Transactional
    public void salvar(ProdutoModel produto){
        if (produto.getCategoria() == null || produto.getCategoria().getIdCategoria() == null) {
            throw new IllegalArgumentException("Selecione uma categoria para o produto.");
        }

        var categoria = categoriaRepository.findById(produto.getCategoria().getIdCategoria())
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada."));

        produto.setCategoria(categoria);

        var existente = repository.findByNomeIgnoreCase(produto.getNome());

        if (existente.isPresent() &&
                !existente.get().getIdProduto().equals(produto.getIdProduto())) {

            throw new IllegalArgumentException("Já existe um produto com esse nome.");
        }

        repository.save(produto);
    }

    @Transactional
    public void excluir(long id){
        repository.deleteById(id);
    }
}
