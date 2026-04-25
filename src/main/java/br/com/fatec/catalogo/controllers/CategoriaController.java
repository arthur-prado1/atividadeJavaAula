package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.services.CategoriaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    @GetMapping
    public String listarCategorias(Model model) {
        prepararTela(model, new CategoriaModel(), false);
        return "lista-categorias";
    }

    @PostMapping
    public String salvarCategoria(@Valid @ModelAttribute("categoria") CategoriaModel categoria,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            prepararTela(model, categoria, false);
            return "lista-categorias";
        }

        try {
            service.salvar(categoria);
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "erro.nome", e.getMessage());
            prepararTela(model, categoria, false);
            return "lista-categorias";
        }

        return "redirect:/categorias";
    }

    @GetMapping("/editar/{id}")
    public String exibirEdicao(@PathVariable Long id, Model model) {
        prepararTela(model, service.buscarPorId(id), true);
        return "lista-categorias";
    }

    @PostMapping("/editar/{id}")
    public String atualizarCategoria(@PathVariable Long id,
                                     @Valid @ModelAttribute("categoria") CategoriaModel categoria,
                                     BindingResult result,
                                     Model model) {
        categoria.setId(id);

        if (result.hasErrors()) {
            prepararTela(model, categoria, true);
            return "lista-categorias";
        }

        try {
            service.salvar(categoria);
        } catch (IllegalArgumentException e) {
            result.rejectValue("nome", "erro.nome", e.getMessage());
            prepararTela(model, categoria, true);
            return "lista-categorias";
        }

        return "redirect:/categorias";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id, Model model) {
        try {
            service.excluir(id);
        } catch (IllegalArgumentException e) {
            prepararTela(model, new CategoriaModel(), false);
            model.addAttribute("erro", e.getMessage());
            return "lista-categorias";
        }

        return "redirect:/categorias";
    }

    private void prepararTela(Model model, CategoriaModel categoria, boolean editando) {
        model.addAttribute("categoria", categoria);
        model.addAttribute("categorias", service.listarTodas());
        model.addAttribute("editando", editando);
    }
}
