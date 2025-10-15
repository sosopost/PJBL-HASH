import java.util.*;
import java.io.*;

public class Main {
    private static final int[] TAMANHOS_TABELA = {1000, 10000, 100000};
    private static final int[] TAMANHOS_DADOS = {100000, 1000000, 10000000};
    private static List<ResultadoExperimento> todosResultados = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("** INICIANDO EXPERIMENTOS DE TABELAS HASH **");

        new File("data").mkdirs();
        new File("resultados").mkdirs();

        gerarDados();
        executarTestesCompletos();
        exportarResultados();
        gerarRelatorio();

        System.out.println("\n** EXPERIMENTOS CONCLUÍDOS **");
        System.out.println("Resultados exportados para a pasta 'resultados/'");
    }

    private static void executarTestesCompletos() {
        System.out.println("\nExecutando testes de desempenho...");

        for (int tamanhoTabela : TAMANHOS_TABELA) {
            for (int tamanhoDados : TAMANHOS_DADOS) {
                double fatorCarga = (double) tamanhoDados / tamanhoTabela;

                System.out.println("\n" + "=".repeat(70));
                System.out.println("** TESTE: Tabela=" + tamanhoTabela +
                        ", Dados=" + tamanhoDados +
                        " (Fator=" + String.format("%.2f", fatorCarga) + ") **");

                String arquivoDados = "data/dataset_" + (tamanhoDados/1000) + "k.txt";
                List<Registro> dados = carregarDados(arquivoDados);

                if (dados.isEmpty()) {
                    System.out.println("AVISO: Nenhum dado carregado para " + arquivoDados);
                    continue;
                }

                testarHashTable(new ChainingHashTable(tamanhoTabela), dados, "Encadeamento");

                if (fatorCarga <= 0.75) {
                    testarHashTable(new LinearProbingHashTable(tamanhoTabela), dados, "Linear-Probing");
                    testarHashTable(new QuadraticProbingHashTable(tamanhoTabela), dados, "Quadratic-Probing");
                    testarHashTable(new DoubleHashingHashTable(tamanhoTabela), dados, "Double-Hashing");
                } else {
                    System.out.println("Rehashing não testado - fator de carga muito alto: " + String.format("%.2f", fatorCarga));
                    System.out.println("Somente encadeamento é eficiente para fator > 0.75");
                }
            }
        }
    }

    private static void testarHashTable(HashTable hashTable, List<Registro> dados, String nome) {
        try {
            System.out.println("\n--- Testando " + nome + " ---");

            ResultadoExperimento resultado = new ResultadoExperimento(
                    nome, hashTable.getTamanho(), dados.size()
            );

            long inicio = System.currentTimeMillis();
            for (Registro registro : dados) {
                hashTable.inserir(registro);
            }
            long fim = System.currentTimeMillis();
            resultado.setTempoInsercao(fim - inicio);

            inicio = System.currentTimeMillis();
            for (Registro registro : dados) {
                hashTable.buscar(registro);
            }
            fim = System.currentTimeMillis();
            resultado.setTempoBusca(fim - inicio);

            resultado.setColisoes(hashTable.getColisoes());
            resultado.setFatorCarga(hashTable.getFatorCarga());
            
            resultado.setMaiorLista(hashTable.getMaiorLista());
            List<Integer> tresMaiores = hashTable.getTresMaioresListas();
            List<String> tresMaioresStr = new ArrayList<>();
            for (Integer tamanho : tresMaiores) {
                tresMaioresStr.add(tamanho.toString());
            }
            resultado.setTresMaioresListas(tresMaioresStr);
            
            int[] gaps = hashTable.getGaps();
            resultado.setMenorGap(gaps[0]);
            resultado.setMaiorGap(gaps[1]);
            resultado.setMediaGaps(gaps[2]);

            System.out.println("Tempo inserção: " + resultado.getTempoInsercao() + "ms");
            System.out.println("Tempo busca: " + resultado.getTempoBusca() + "ms");
            System.out.println("Colisões: " + resultado.getColisoes());
            System.out.println("Fator de carga: " + String.format("%.4f", resultado.getFatorCarga()));

            hashTable.estatisticas();

            todosResultados.add(resultado);

        } catch (Exception e) {
            System.out.println("ERRO em " + nome + ": " + e.getMessage());
        }
    }

    private static void gerarDados() {
        System.out.println("Gerando conjuntos de dados...");
        Random random = new Random(12345);

        for (int tamanho : TAMANHOS_DADOS) {
            String filename = "data/dataset_" + (tamanho/1000) + "k.txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (int i = 0; i < tamanho; i++) {
                    String codigo = String.format("%09d", random.nextInt(1000000000));
                    writer.println(codigo);
                }
                System.out.println("Gerado: " + filename + " com " + tamanho + " registros");
            } catch (IOException e) {
                System.out.println("Erro ao gerar " + filename + ": " + e.getMessage());
            }
        }
    }

    private static List<Registro> carregarDados(String filename) {
        List<Registro> registros = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                registros.add(new Registro(linha.trim()));
            }
            System.out.println("Carregados " + registros.size() + " registros de " + filename);
        } catch (IOException e) {
            System.out.println("Erro ao carregar " + filename + ": " + e.getMessage());
        }
        return registros;
    }

    private static void exportarResultados() {
        System.out.println("\nExportando resultados...");

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados/resultados_completos.csv"))) {
            writer.println(ResultadoExperimento.getCSVHeader());
            for (ResultadoExperimento resultado : todosResultados) {
                writer.println(resultado.toCSV());
            }
            System.out.println("CSV exportado: resultados/resultados_completos.csv");
        } catch (IOException e) {
            System.out.println("Erro ao exportar CSV: " + e.getMessage());
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados/relatorio_detalhado.txt"))) {
            writer.println("RELATÓRIO DETALHADO - EXPERIMENTOS TABELAS HASH");
            writer.println("=".repeat(80));
            writer.println();

            for (ResultadoExperimento resultado : todosResultados) {
                writer.println(resultado.toString());
                writer.println("Maior lista: " + resultado.getMaiorLista());
                writer.println("Gaps - Menor: " + resultado.getMenorGap() +
                        ", Maior: " + resultado.getMaiorGap() +
                        ", Média: " + String.format("%.2f", resultado.getMediaGaps()));
                writer.println("3 maiores listas: " + resultado.getTresMaioresListas());
                writer.println("-".repeat(60));
            }
            System.out.println("Relatório exportado: resultados/relatorio_detalhado.txt");
        } catch (IOException e) {
            System.out.println("Erro ao exportar relatório: " + e.getMessage());
        }
    }

    private static void gerarRelatorio() {
        System.out.println("\nGerando análise comparativa...");

        try (PrintWriter writer = new PrintWriter(new FileWriter("resultados/analise_comparativa.txt"))) {
            writer.println("ANÁLISE COMPARATIVA - DESEMPENHO DAS TABELAS HASH");
            writer.println("=".repeat(80));
            writer.println();
            writer.println("FUNÇÕES HASH IMPLEMENTADAS:");
            writer.println("- Encadeamento: Multiplicação com primo (37)");
            writer.println("- Linear Probing: Multiplicação de Knuth (áurea)");
            writer.println("- Quadratic Probing: Polinomial com Horner (primo 31)");
            writer.println("- Double Hashing: Duas funções complementares");
            writer.println();

            Map<String, List<ResultadoExperimento>> resultadosPorTipo = new HashMap<>();
            for (ResultadoExperimento res : todosResultados) {
                String tipo = res.getTipoHash();
                resultadosPorTipo.computeIfAbsent(tipo, k -> new ArrayList<>()).add(res);
            }

            for (Map.Entry<String, List<ResultadoExperimento>> entry : resultadosPorTipo.entrySet()) {
                writer.println(entry.getKey().toUpperCase() + ":");
                writer.println("-".repeat(40));

                List<ResultadoExperimento> resultados = entry.getValue();
                resultados.sort((a, b) -> {
                    int cmp = Integer.compare(a.getTamanhoTabela(), b.getTamanhoTabela());
                    if (cmp == 0) {
                        cmp = Integer.compare(a.getTamanhoDados(), b.getTamanhoDados());
                    }
                    return cmp;
                });

                for (ResultadoExperimento res : resultados) {
                    writer.printf("  Tabela %6d | Dados %7d | Ins: %4dms | Busca: %4dms | Colisões: %7d | Fator: %.2f%n",
                            res.getTamanhoTabela(), res.getTamanhoDados(),
                            res.getTempoInsercao(), res.getTempoBusca(),
                            res.getColisoes(), res.getFatorCarga());
                }
                writer.println();
            }

            writer.println("ANÁLISE DETALHADA:");
            writer.println("-".repeat(40));
            
            ResultadoExperimento melhorInsercao = todosResultados.stream()
                .min((a, b) -> Long.compare(a.getTempoInsercao(), b.getTempoInsercao()))
                .orElse(null);
            
            ResultadoExperimento melhorBusca = todosResultados.stream()
                .min((a, b) -> Long.compare(a.getTempoBusca(), b.getTempoBusca()))
                .orElse(null);
                
            ResultadoExperimento menorColisoes = todosResultados.stream()
                .min((a, b) -> Integer.compare(a.getColisoes(), b.getColisoes()))
                .orElse(null);

            if (melhorInsercao != null) {
                writer.printf("Melhor inserção: %s (%dms)%n", 
                    melhorInsercao.getTipoHash(), melhorInsercao.getTempoInsercao());
            }
            if (melhorBusca != null) {
                writer.printf("Melhor busca: %s (%dms)%n",
                    melhorBusca.getTipoHash(), melhorBusca.getTempoBusca());
            }
            if (menorColisoes != null) {
                writer.printf("Menor colisões: %s (%d colisões)%n",
                    menorColisoes.getTipoHash(), menorColisoes.getColisoes());
            }
            writer.println();

            writer.println("CONCLUSÕES:");
            writer.println("-".repeat(40));
            writer.println("1. Encadeamento: Melhor para fatores de carga altos (> 1.0)");
            writer.println("   - Nunca falha por overflow");
            writer.println("   - Performance degrada linearmente com fator de carga");
            writer.println();
            writer.println("2. Linear Probing: Simples mas sofre agrupamento primário");
            writer.println("   - Boa localidade de cache");
            writer.println("   - Performance degrada rapidamente com fator alto");
            writer.println();
            writer.println("3. Quadratic Probing: Evita agrupamento primário");
            writer.println("   - Melhor que linear para fatores médios");
            writer.println("   - Pode sofrer agrupamento secundário");
            writer.println();
            writer.println("4. Double Hashing: Melhor distribuição estatística");
            writer.println("   - Menos colisões que outros métodos de rehashing");
            writer.println("   - Distribuição mais uniforme");
            writer.println();
            writer.println("5. Fator de carga recomendado:");
            writer.println("   - Rehashing: 0.5 - 0.75 (ótimo: 0.65)");
            writer.println("   - Encadeamento: 1.0 - 3.0 (ótimo: 1.0-2.0)");
            writer.println();
            writer.println("6. Escolha da técnica:");
            writer.println("   - Use encadeamento se: fator > 0.75 ou inserções frequentes");
            writer.println("   - Use double hashing se: fator ≤ 0.75 e busca é crítica");
            writer.println("   - Use linear probing se: simplicidade é importante");

            System.out.println("Análise comparativa exportada: resultados/analise_comparativa.txt");

        } catch (IOException e) {
            System.out.println("Erro ao gerar análise: " + e.getMessage());
        }
    }
}
