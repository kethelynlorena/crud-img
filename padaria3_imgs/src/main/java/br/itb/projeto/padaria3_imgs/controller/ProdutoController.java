package br.itb.projeto.padaria3_imgs.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import br.itb.projeto.padaria3_imgs.model.entity.Produto;
import br.itb.projeto.padaria3_imgs.model.entity.ProdutoFoto;
import br.itb.projeto.padaria3_imgs.service.CategoriaService;
import br.itb.projeto.padaria3_imgs.service.ProdutoService;

@Controller		// DEFINE O ENDEREÇO NA URL PARA ACESSAR OS "END_POINTS" DA CLASSE
@RequestMapping("/api/produto")
public class ProdutoController {

	private ProdutoService produtoService;
	private CategoriaService categoriaService;
		
	public ProdutoController(ProdutoService produtoService, CategoriaService categoriaService) {
		super();
		this.produtoService = produtoService;
		this.categoriaService = categoriaService;
	}

	private String serverMessage = null;
	private String foto = "";
	// CASO O PRODUTO NÃO TENHA UMA IMAGEM CADASTRADA NO BANCO DE DADOS
	private String noImage = "/images/sem-imagem.png";
	
	//DEFINE O CAMINHO PADRÃO DAS IMAGENS NO SISTEMA
	private final String IMAGES_PATH = "D:/_PADARIA_/IMAGENS/";
	
	// LISTA TODOS OS PRODUTOS PARA O ADMIN
	// @GetMapping("/todos" --> END_POINT)
	// END_POINT -> EXECUTA UMA AÇÃO QUANDO É INFORMADO NA URL
	@GetMapping("/todos")
	public String verTodosProdutos(ModelMap model) {

		List<Produto> produtos = produtoService.findAll();

		// DEFINE UM ATRIBUTO NA PÁGINA QUE IRÁ REPRESENTAR OS DADOS VINDOS DA APLICAÇÃO 
		model.addAttribute("produtos", produtos);
		serverMessage = null;

		// INDICA A PÁGINA QUE SERÁ CARREGADA NA EXECUÇÃO DO MÉTODO
		return "produtos";
	}
	
	@GetMapping("/todos-filtro")
	public String verProdutosPorNome(ModelMap model,
			@RequestParam(value = "nome", required = false) String nome) {
		
		List<Produto> produtos = null;
		
		if (nome == null) {
			produtos = produtoService.findAll();
			model.addAttribute("produtos", produtos);
		} else {
			produtos = produtoService.findAllByNome(nome);
			model.addAttribute("produtos", produtos);
		}

		serverMessage = null;

		// INDICA A PÁGINA QUE SERÁ CARREGADA NA EXECUÇÃO DO MÉTODO
		return "produtos-filtro";
	}
	
	// LISTA TODOS OS PRODUTOS QUE SERÃO EXIBIDOS NA PÁGINA INICIAL
	@GetMapping("/lista")
	public String verListaProdutos(ModelMap model) {

		List<Produto> produtos = produtoService.findAll();
		model.addAttribute("produtos", produtos);
		model.addAttribute("noImage", noImage);
		serverMessage = null;

		return "produtos-lista";
	}
	
	// NAVEGA PARA PÁGINA PARA CADASTRO DE UM NOVO PRODUTO
	@GetMapping("/novo")
	public String novoProduto(ModelMap model) {

		model.addAttribute("produto", new Produto());
		model.addAttribute("categorias", categoriaService.findAll());
		model.addAttribute("serverMessage", serverMessage);
		serverMessage = null;
		return "produto-novo";
	}
	
	// GRAVA AS INFORMAÇÕES DO PRODUTO
	// MultipartFile file, NECESSÁRIO PARA GRAVAR A IMAGEM DO PRODUTO
	@PostMapping("/salvar")
	public String salvar(@ModelAttribute("produto") Produto produto) {
		
		Produto p = produtoService.saveNew(produto);
		
		serverMessage = "Produto cadastrado com sucesso!!!";

		// REDIRECIONA PARA O END_POINT INDICADO
		return "redirect:/api/produto/ver/" + p.getId();
	}
	
	// GRAVA AS ALTERAÇÕES DO PRODUTO
	@PostMapping("/editar/{id}")
	public String editar(@PathVariable("id") long id, 
			@ModelAttribute("produto") Produto produto) {
		
		produtoService.update(produto);
		serverMessage = "Dados alterados com sucesso!!!";

		return "redirect:/api/produto/ver/" + id;
	}
	
	// CARREGA AS INFORMAÇÕES DO PRODUTO
	@GetMapping("/ver/{id}")
	public String verProduto(@PathVariable("id") long id, ModelMap model) {

		Produto produto = produtoService.findById(id);
		
		List<ProdutoFoto> fotos = produtoService.findAllFotos(produto);
				
		model.addAttribute("produto", produto);
		model.addAttribute("fotos", fotos);
		model.addAttribute("noImage", noImage);
		model.addAttribute("categorias", categoriaService.findAll());
		model.addAttribute("serverMessage", serverMessage);

		return "produto-editar";
	}
	
	@GetMapping("/addFoto/{id}")
	public String addFotoProduto(
			@PathVariable("id") long id, ModelMap model) {

		Produto produto = produtoService.findById(id);
		
		List<ProdutoFoto> fotos = produtoService.findAllFotos(produto);
		
		List<MultipartFile> files = new ArrayList<MultipartFile>();
		model.addAttribute("files", files);
		
		model.addAttribute("produto", produto);
		model.addAttribute("fotos", fotos);
		model.addAttribute("serverMessage", serverMessage);

		return "produto-addfoto";
	}
	
	// GRAVA AS FOTOS DO PRODUTO
	// MultipartFile file, NECESSÁRIO PARA GRAVAR A IMAGEM DO PRODUTO
	@PostMapping("/salvarFotos/{id}")
	public String salvarFotos(@PathVariable("id") long id, 
			@RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
		
		Produto produto = produtoService.findById(id);
		
//		List<String> fileNames = Arrays
//	            .stream(files)
//	            .map(MultipartFile::getOriginalFilename)
//	            .collect(Collectors.toList());
//		
//		fileNames.forEach(System.out::println);
		
		if (files.length > 0) {
			for (MultipartFile multipartFile : files) {
				try {
					multipartFile.transferTo(new File(IMAGES_PATH + "pID_"+ produto.getId() +"_" + multipartFile.getOriginalFilename()));
				} catch (IllegalStateException e) {
					e.printStackTrace();
				}
			}
		}

		produtoService.salvarFotoProduto(produto, files);
		
		serverMessage = "Fotos do Produto cadastradas com sucesso!!!";

		// REDIRECIONA PARA O END_POINT INDICADO
		return "redirect:/api/produto/ver/" + produto.getId();
	}
	
	@GetMapping("/showImage/{id}")
	@ResponseBody
	public void showImage(@PathVariable("id") long id, HttpServletResponse response, ProdutoFoto produtoFoto)
			throws ServletException, IOException {

		produtoFoto = produtoService.findByFotoId(id);

		byte[] byteImage = null;
		BufferedImage bi = null;
		File file = new File(produtoFoto.getCaminho());

		if (file.exists()) {
			bi = ImageIO.read(file);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byteImage = baos.toByteArray();
			
			response.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			response.getOutputStream().write(byteImage);
		}  else {
			Resource resource = new ClassPathResource("/static/" + noImage);
			InputStream stream = resource.getInputStream();		
			response.getOutputStream().write(stream.readAllBytes());
		}

		response.getOutputStream().close();
	}
		
	@GetMapping("/foto/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = produtoService.load(filename);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION,
						"attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}
	
	
	/*
	 
	@PostMapping("/uploadMultipleFiles")
	public String uploadMultipleFiles(@RequestParam("file") MultipartFile[] files) {
		
		List<String> fileNames = Arrays
	            .stream(files)
	            .map(MultipartFile::getOriginalFilename)
	            .collect(Collectors.toList());

		if (files.length == 0)
			return "Mismatching no of files are equal to description";

		String status = "";
		File dir = new File(IMAGES_PATH);
		for (int i = 0; i < files.length; i++) {
			MultipartFile file = files[i];
			try {
				byte[] bytes = file.getBytes();

				if (!dir.exists())
					dir.mkdirs();

				File uploadFile = new File(dir.getPath() + File.separator + file.getOriginalFilename());
				BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(uploadFile));
				outputStream.write(bytes);
				outputStream.close();

				status = status + "You successfully uploaded file=" + file.getOriginalFilename();
				
			} catch (Exception e) {
				status = status + "Failed to upload " + file.getOriginalFilename() + " " + e.getMessage();
			}
		}
		
		return "redirect:/api/produto/todos-filtro";
	}
		
	 
	 * 
	 */

	
	private byte[] imagePathtoByte(String imagePath) throws IOException {
		Path source = Paths.get(imagePath);
		BufferedImage bi = ImageIO.read(source.toFile());

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, "png", baos);
		return baos.toByteArray();
	}

}
