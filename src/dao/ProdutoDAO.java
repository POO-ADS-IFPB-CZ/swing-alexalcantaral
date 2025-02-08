package dao;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class ProdutoDAO {
    private final File arquivo;

    public ProdutoDAO(){
        arquivo = new File("Produtos");
        if(!arquivo.exists()){
            try{
                arquivo.createNewFile();
            } catch (IOException e){
                throw new RuntimeException("Falha ao criar arquivo",e);
            }
        }
    }

    public Set<model.Produto> getProdutos() throws IOException, ClassNotFoundException {
        if (arquivo.length() == 0) {
            return new HashSet<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(arquivo))) {
            return (Set<model.Produto>) in.readObject();
        }
    }

    private void atualizarArquivo(Set<model.Produto> produtos) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(arquivo))) {
            out.writeObject(produtos);
        }
    }

    public boolean adicionarProduto(model.Produto produto) throws IOException, ClassNotFoundException{
        Set<model.Produto> produtos = getProdutos();
        if (produtos.add(produto)){
            atualizarArquivo(produtos);
            return true;
        }
        return false;
    }

    public boolean atualizarProduto(model.Produto produtoAtualizado) throws IOException, ClassNotFoundException {
        Set<model.Produto> produtos = getProdutos(); // Lê os produtos

        for (model.Produto p : produtos) {
            if (p.getId() == produtoAtualizado.getId()) {

                p.setDescricao(produtoAtualizado.getDescricao());
                p.setPreco(produtoAtualizado.getPreco());
                p.setValidade(produtoAtualizado.getValidade());

                atualizarArquivo(produtos);
                return true;
            }
        }
        return false;
    }

    public boolean removerProduto(int id) throws IOException, ClassNotFoundException {
        Set<model.Produto> produtos = getProdutos(); // Lê os produtos do arquivo

        boolean removido = produtos.removeIf(p -> p.getId() == id); // Remove o produto pelo ID

        if (removido) {
            atualizarArquivo(produtos); // Atualiza o arquivo após a remoção
        }

        return removido;
    }
}