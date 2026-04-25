package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
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

    public List<ProdutoModel> buscar(String nome, Long categoriaId) {
        boolean temNome = nome != null && !nome.trim().isEmpty();
        boolean temCategoria = categoriaId != null;

        if (temNome && temCategoria) {
            return repository.findByNomeContainingIgnoreCaseAndCategoria_Id(nome.trim(), categoriaId);
        }

        if (temCategoria) {
            return repository.findByCategoria_Id(categoriaId);
        }

        if (!temNome) {
            return repository.findAll();
        }

        return repository.findByNomeContainingIgnoreCase(nome.trim());
    }

    public ProdutoModel buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto nao encontrado"));
    }

    public void salvar(ProdutoModel produto) {
        boolean isNovo = produto.getIdProduto() == null;

        if (isNovo && repository.existsByNome(produto.getNome())) {
            throw new IllegalArgumentException("Ja existe um produto com o nome \"" + produto.getNome() + "\"");
        }

        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("A categoria do produto e obrigatoria");
        }

        CategoriaModel categoria = categoriaRepository.findById(produto.getCategoria().getId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada"));

        produto.setCategoria(categoria);
        repository.save(produto);
    }

    public void excluir(Long id) {
        ProdutoModel produto = buscarPorId(id);
        repository.delete(produto);
    }
}
