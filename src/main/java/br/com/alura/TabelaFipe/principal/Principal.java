package br.com.alura.TabelaFipe.principal;

import br.com.alura.TabelaFipe.model.Dados;
import br.com.alura.TabelaFipe.model.Modelos;
import br.com.alura.TabelaFipe.model.Veiculo;
import br.com.alura.TabelaFipe.service.ConverteDados;
import br.com.alura.tabelafipe.service.ConsumoApi;

import javax.sound.midi.Soundbank;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumo = new ConsumoApi();

    private final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public void exibeMenu() {
        while (true) {
        var menu = """
                
                ***  🚦 OPÇÕES 🚦 ***
                1- Carro 🚗
                2- Moto 🛵 
                3- Caminhão 🚚
                0- Sair 💥
                Digite uma das opções:
                """;

        System.out.println(menu);
        var opcao = leitura.nextLine();

        String endereco;

            switch (opcao) {
                case "1":
                    endereco = URL_BASE + "carros/marcas";
                    break;
                case "2":
                    endereco = URL_BASE + "motos/marcas";
                    break;
                case "3":
                    endereco = URL_BASE + "caminhoes/marcas";
                    break;
                case "0":
                    exibirMensagemDeSaida();
                    return;
                default:
                    System.out.println("⚠️ Opção inválida! Por favor, tente novamente. ⚠️");
                    continue;
            }

        var json = consumo.obterDados(endereco);
        System.out.println(json);

        var marcas = conversor.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("Informe o código da marca para consulta: " );
        var codigoMarca = leitura.nextLine();

        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = consumo.obterDados(endereco);
        var modeloLista = conversor.obterDados(json, Modelos.class);

        System.out.println("\n Modelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\n Digite um trecho do nome do carro a ser buscado: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());


        System.out.println("\n Modelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        System.out.println("Digite o código do modelo para buscar os valores de avaliação: ");
        var codigoModelo = leitura.nextLine();

        endereco = endereco + "/" + codigoModelo + "/anos";
        json = consumo.obterDados(endereco);
        List<Dados> anos = conversor.obterLista(json, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size(); i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(json, Veiculo.class);
            veiculos.add(veiculo);
        }
        System.out.println("\n Todos os veiculos filtrados com avaliações por ano: ");
        veiculos.forEach(System.out::println);

    }

}
    public static void exibirMensagemDeSaida() {
        System.out.println("\n" + "=".repeat(22) + " 🐱✨ Programa encerrado! ✨🐱 " + "=".repeat(20));
        System.out.println("Desenvolvido por: Jeisa Boaventura");
        System.out.println("GitHub: https://github.com/Caaarolb");
        System.out.println("LinkedIn: https://www.linkedin.com/in/-caroline-boaventura/");
        System.out.println("=" + "=".repeat(68) + "\n");
    }
}
