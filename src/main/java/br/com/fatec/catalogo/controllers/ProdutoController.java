package br.com.fatec.catalogo.controllers;

import br.com.fatec.catalogo.models.ProdutoModel;
import br.com.fatec.catalogo.services.CategoriaService;
import br.com.fatec.catalogo.services.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService service;

    @Autowired
    private CategoriaService categoriaService;


    @GetMapping("/novo")
    public String exibirFormulario(Model model){
        model.addAttribute("produto", new ProdutoModel());
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "cadastro-produto";
    }

    @GetMapping("/editar/{id}")
    public String exibirEdicao(@PathVariable long id, Model model){
        model.addAttribute("produto", service.buscarPorId(id));
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "editar-produto";
    }

    @PostMapping("/salvar")
    public String salvarProduto(
            @Valid @ModelAttribute("produto") ProdutoModel produto,
            BindingResult result,
            Model model){

        if (result.hasErrors()){
            model.addAttribute("produto", produto);
            model.addAttribute("categorias", categoriaService.listarTodas());
            return produto.getIdProduto() == null ? "cadastro-produto" : "editar-produto";
        }

        try {
            service.salvar(produto);
        } catch (IllegalArgumentException e) {
            model.addAttribute("erro", e.getMessage());
            model.addAttribute("categorias", categoriaService.listarTodas());
            return produto.getIdProduto() == null ? "cadastro-produto" : "editar-produto";
        }

        return "redirect:/produtos";
    }

    @GetMapping("/excluir/{id}")
    public String excluir(@PathVariable long id){
        service.excluir(id);
        return "redirect:/produtos";
    }

    @GetMapping
    public String listarProdutos(@RequestParam(required = false) String busca,
                                 @RequestParam(required = false) Long categoriaId,
                                 Model model){

        model.addAttribute("produtos", service.filtrar(busca, categoriaId));
        model.addAttribute("categorias", categoriaService.listarTodas());
        model.addAttribute("busca", busca);
        model.addAttribute("categoriaId", categoriaId);
        return "lista-produtos";
    }
}
