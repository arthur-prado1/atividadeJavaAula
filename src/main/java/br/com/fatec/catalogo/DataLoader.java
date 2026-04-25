package br.com.fatec.catalogo.config;

import br.com.fatec.catalogo.models.CategoriaModel;
import br.com.fatec.catalogo.models.UsuarioModel;
import br.com.fatec.catalogo.repositories.CategoriaRepository;
import br.com.fatec.catalogo.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner carregarDadosPadrao(
            UsuarioRepository usuarioRepository,
            CategoriaRepository categoriaRepository,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (!usuarioRepository.existsByUsername("joao")) {
                UsuarioModel joao = new UsuarioModel();
                joao.setUsername("joao");
                joao.setPassword(passwordEncoder.encode("12345"));
                joao.setRole("ROLE_USER");
                usuarioRepository.save(joao);
            }

            if (!usuarioRepository.existsByUsername("arthur")) {
                UsuarioModel arthur = new UsuarioModel();
                arthur.setUsername("arthur");
                arthur.setPassword(passwordEncoder.encode("12345"));
                arthur.setRole("ROLE_ADMIN");
                usuarioRepository.save(arthur);
            }

            criarCategoriaSeNaoExistir(categoriaRepository, "Informatica");
            criarCategoriaSeNaoExistir(categoriaRepository, "Perifericos");
            criarCategoriaSeNaoExistir(categoriaRepository, "Monitores");
        };
    }

    private void criarCategoriaSeNaoExistir(CategoriaRepository categoriaRepository, String nomeCategoria) {
        if (!categoriaRepository.existsByNomeIgnoreCase(nomeCategoria)) {
            CategoriaModel categoria = new CategoriaModel();
            categoria.setNome(nomeCategoria);
            categoriaRepository.save(categoria);
        }
    }
}
