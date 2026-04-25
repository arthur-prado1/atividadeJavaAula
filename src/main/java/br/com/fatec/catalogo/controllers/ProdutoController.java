package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @GetMapping
    public String listarProdutos(@RequestParam(required = false) String nome,
                                 @RequestParam(required = false) Long categoriaId,
                                 Model model) {
        model.addAttribute("produtos", service.buscar(nome, categoriaId));
        model.addAttribute("termoBusca", nome);
        model.addAttribute("categoriaSelecionadaId", categoriaId);
        popularCategorias(model);
        return "lista-produtos";
    }

    @GetMapping("/novo")
    public String exibirFormulario(Model model) {
        model.addAttribute("produto", new ProdutoModel());
        popularCategorias(model);
        return "cadastro-produto";
    }

    @PostMapping("/novo")
    public String salvarProduto(@Valid @ModelAttribute("produto") ProdutoModel produto,
                                BindingResult result,
                                Model model) {

        if (result.hasErrors()) {
            popularCategorias(model);
            return "cadastro-produto";
        }

        try {
            service.salvar(produto);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("categoria")) {
                result.rejectValue("categoria", "erro.categoria", e.getMessage());
            } else {
                result.rejectValue("nome", "erro.nome", e.getMessage());
            }
            popularCategorias(model);
            return "cadastro-produto";
        }

        return "redirect:/produtos";
    }

    @GetMapping("/editar/{id}")
    public String exibirEdicao(@PathVariable Long id, Model model) {
        model.addAttribute("produto", service.buscarPorId(id));
        popularCategorias(model);
        return "editar-produto";
    }

    @PostMapping("/editar/{id}")
    public String atualizarProduto(@PathVariable Long id,
                                   @Valid @ModelAttribute("produto") ProdutoModel produto,
                                   BindingResult result,
                                   Model model) {

        if (result.hasErrors()) {
            popularCategorias(model);
            return "editar-produto";
        }

        try {
            produto.setIdProduto(id);
            service.salvar(produto);
        } catch (IllegalArgumentException e) {
            if (e.getMessage() != null && e.getMessage().toLowerCase().contains("categoria")) {
                result.rejectValue("categoria", "erro.categoria", e.getMessage());
            } else {
                result.rejectValue("nome", "erro.nome", e.getMessage());
            }
            popularCategorias(model);
            return "editar-produto";
        }

        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluirProduto(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
        } catch (Exception e) {
            model.addAttribute("erro", "Nao foi possivel excluir o produto. Verifique se ele ainda existe e tente novamente.");
            model.addAttribute("produtos", service.buscar(null, null));
            model.addAttribute("termoBusca", null);
            model.addAttribute("categoriaSelecionadaId", null);
            popularCategorias(model);
            return "lista-produtos";
        }

        return "redirect:/produtos";
    }

    private void popularCategorias(Model model) {
        List<CategoriaModel> categorias = categoriaRepository.findAll();
        model.addAttribute("categorias", categorias);
    }
}
