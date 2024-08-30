package br.itb.projeto.padaria3_imgs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Startup  implements CommandLineRunner{


	
	public static void main(String[] args) {
		SpringApplication.run(Startup.class, args);
	}
	
	@Override
	public void run(String... arg) throws Exception {
		Path root = null;
		try {
			if (new File("D:").isDirectory() == false) {
				root = Paths.get("C:/_PADARIA_/IMAGENS/");
			} else {
				root = Paths.get("D:/_PADARIA_/IMAGENS/");
			}
			Files.createDirectories(root);
		} catch (IOException e) {
			throw new RuntimeException("Erro ao criar a pasta!");
		}
	}

}
