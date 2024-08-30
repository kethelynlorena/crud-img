package br.itb.projeto.padaria3_imgs.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.itb.projeto.padaria3_imgs.model.entity.Produto;
import br.itb.projeto.padaria3_imgs.model.entity.ProdutoFoto;

								// ESTENDE A INTERFACE JpaRepository
@Repository						// <Tabela_que_ela_faz_referência, Tipo_de_dado_do_atributo_@Id_da_tabela_referedia>
public interface ProdutoFotoRepository extends JpaRepository<ProdutoFoto, Long> {
	
	List<ProdutoFoto> findByProduto(Produto produto);
	
	ProdutoFoto findByNome(String nome);

}
