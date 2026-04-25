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
    private CategoriaService categoriaService;

    @GetMapping("/nova")
    public String novaCategoria(Model model) {
        model.addAttribute("categoria", new CategoriaModel());
        return "cadastro-categoria";
    }

    @PostMapping("/salvar")
    public String salvarCategoria(@Valid @ModelAttribute("categoria") CategoriaModel categoria,
                                  BindingResult result,
                                  Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            return "cadastro-categoria";
        }

        try {
            categoriaService.salvar(categoria);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "cadastro-categoria";
        }

        return "redirect:/dashboard?categoriaSucesso";
    }

    @GetMapping("/editar/{id}")
    public String editarCategoria(@PathVariable Long id, Model model) {
        model.addAttribute("categoria", categoriaService.buscarPorId(id));
        return "editar-categoria";
    }

    @PostMapping("/atualizar")
    public String atualizarCategoria(@Valid @ModelAttribute("categoria") CategoriaModel categoria,
                                     BindingResult result,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categoria", categoria);
            return "editar-categoria";
        }

        try {
            categoriaService.salvar(categoria);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            return "editar-categoria";
        }

        return "redirect:/dashboard?categoriaAtualizada";
    }

    @GetMapping("/excluir/{id}")
    public String excluirCategoria(@PathVariable Long id) {
        try {
            categoriaService.excluir(id);
            return "redirect:/dashboard?categoriaExcluida";
        } catch (IllegalArgumentException e) {
            return "redirect:/dashboard?categoriaErro";
        }
    }
}
