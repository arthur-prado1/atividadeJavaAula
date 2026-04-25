package br.com.fatec.catalogo.services;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository repository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<CategoriaModel> listarTodas() {
        return repository.findAll(Sort.by("nome").ascending());
    }

    public CategoriaModel buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria nao encontrada"));
    }

    public void salvar(CategoriaModel categoria) {
        String nome = categoria.getNome() == null ? "" : categoria.getNome().trim();
        categoria.setNome(nome);

        Optional<CategoriaModel> categoriaComMesmoNome = repository.findByNomeIgnoreCase(nome);
        if (categoriaComMesmoNome.isPresent()
                && !categoriaComMesmoNome.get().getId().equals(categoria.getId())) {
            throw new IllegalArgumentException("Ja existe uma categoria com esse nome.");
        }

        repository.save(categoria);
    }

    public void excluir(Long id) {
        CategoriaModel categoria = buscarPorId(id);

        if (produtoRepository.existsByCategoria_Id(id)) {
            throw new IllegalArgumentException("Nao e possivel excluir uma categoria vinculada a produtos.");
        }

        repository.delete(categoria);
    }
}
