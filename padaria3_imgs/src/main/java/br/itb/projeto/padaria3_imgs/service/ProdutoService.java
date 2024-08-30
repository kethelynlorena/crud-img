package br.itb.projeto.padaria3_imgs.service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.itb.projeto.padaria3_imgs.model.entity.Produto;
import br.itb.projeto.padaria3_imgs.model.entity.ProdutoFoto;
import br.itb.projeto.padaria3_imgs.model.repository.ProdutoFotoRepository;
import br.itb.projeto.padaria3_imgs.model.repository.ProdutoRepository;

@Service
public class ProdutoService {
	
	private ProdutoRepository produtoRepository;
	
	private ProdutoFotoRepository produtoFotoRepository;
 
	public ProdutoService(ProdutoRepository produtoRepository, ProdutoFotoRepository produtoFotoRepository) {
		super();
		this.produtoRepository = produtoRepository;
		this.produtoFotoRepository = produtoFotoRepository;
	}

	final String IMAGES_PATH = "D:/_PADARIA_/IMAGENS/";
	
	private final Path root = Paths.get(IMAGES_PATH);
	
	public List<Produto> findAll() {
		List<Produto> produtos = produtoRepository.findAll();
		return produtos;
	}
	
	public List<Produto> findAllByNome(String nome) {
		List<Produto> produtos = produtoRepository.findByNomeContaining(nome);
		return produtos;
	}

	public Produto findById(long id) {
		return produtoRepository.findById(id).get();
	}
	
	@Transactional
	public Produto saveNew(Produto produto) {

		produto.setStatusProduto("ATIVO");
		
		return produtoRepository.save(produto);
	}
	
	@Transactional
	public Produto update(Produto produto) {

		produto.setStatusProduto("ATIVO");
		
		return produtoRepository.save(produto);
	}
	
	
	@Transactional
	public Produto salvarFotoProduto(Produto produto, MultipartFile[] files) {

		ProdutoFoto produtoFoto = null;
		for (MultipartFile multipartFile : files) {
			produtoFoto = new ProdutoFoto();
			produtoFoto.setProduto(produto);
			produtoFoto.setNome("pID_"+ produto.getId() +"_" + multipartFile.getOriginalFilename());
			produtoFoto.setCaminho(IMAGES_PATH + "pID_"+ produto.getId() +"_" + multipartFile.getOriginalFilename());
			produtoFoto.setStatusFoto(1);

			produtoFotoRepository.save(produtoFoto);
		}
		
		return produto;
		
	}	
	
	public Resource load(String filename) {
		try {
			Path file = root.resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read file: " + filename);
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}
	
	public List<ProdutoFoto> findAllFotos(Produto produto) {
		
		return produtoFotoRepository.findByProduto(produto);
		
	}
	
	public ProdutoFoto findByFotoId(long id) {
		return produtoFotoRepository.findById(id).get();
	}

/*
	@Transactional
	public void update(MultipartFile file, Produto produto, byte[] foto) {
		
		if (file.getSize() == 0 && foto.length == 0) {
			produto.setFoto(null);
		} 
		
		if (file.getSize() == 0 && foto.length > 0) {
			produto.setFoto(foto);
		} 

		if (file != null && file.getSize() > 0 ) {
			try {
				produto.setFoto(file.getBytes());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		produto.setStatusProduto("ATIVO");
		produtoRepository.save(produto);
	} 
	*/


}
