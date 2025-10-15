# PJBL Hash Tables - Análise Comparativa de Tabelas Hash

## Descrição do Projeto

Este projeto implementa e analisa o desempenho de diferentes técnicas de tabelas hash em Java, comparando métodos de **encadeamento** e **rehashing** com diferentes funções hash.

## Implementações

### 1. Tabelas Hash Implementadas

#### a) Encadeamento (ChainingHashTable)
- **Tratamento de Colisões**: Listas encadeadas
- **Função Hash**: Multiplicação com primo (37)
- **Vantagens**: Suporta qualquer fator de carga, nunca overflow
- **Desvantagens**: Uso adicional de memória para ponteiros

#### b) Linear Probing (LinearProbingHashTable)
- **Tratamento de Colisões**: Sondagem linear
- **Função Hash**: Multiplicação de Knuth (constante áurea)
- **Vantagens**: Boa localidade de cache, simples implementação
- **Desvantagens**: Agrupamento primário

#### c) Quadratic Probing (QuadraticProbingHashTable)
- **Tratamento de Colisões**: Sondagem quadrática (i²)
- **Função Hash**: Polinomial com regra de Horner
- **Vantagens**: Evita agrupamento primário
- **Desvantagens**: Agrupamento secundário, pode não encontrar posição

#### d) Double Hashing (DoubleHashingHashTable)
- **Tratamento de Colisões**: Duas funções hash diferentes
- **Funções Hash**: Somatória simples + multiplicação com primo
- **Vantagens**: Melhor distribuição, evita agrupamentos
- **Desvantagens**: Duas funções hash, mais complexo

### 2. Tamanhos Testados

#### Tamanhos das Tabelas Hash:
- **1.000** posições
- **10.000** posições  
- **100.000** posições

#### Conjuntos de Dados:
- **100.000** registros (100k)
- **1.000.000** registros (1M)
- **10.000.000** registros (10M)

#### Fatores de Carga Resultantes:
- 0.1 a 100.0 (dependendo da combinação)
- Rehashing testado apenas para fator ≤ 0.75

## Estrutura do Projeto

```
PJBL-HASH1/
├── src/
│   ├── Main.java                      # Classe principal
│   ├── HashTable.java                 # Interface comum
│   ├── ChainingHashTable.java         # Implementação encadeamento
│   ├── LinearProbingHashTable.java    # Implementação linear probing
│   ├── QuadraticProbingHashTable.java # Implementação quadratic probing
│   ├── DoubleHashingHashTable.java    # Implementação double hashing
│   ├── Registro.java                  # Classe do registro (9 dígitos)
│   ├── ResultadoExperimento.java      # Classe para armazenar resultados
│   └── TabelaCheiaException.java      # Exceção para overflow
├── data/
│   ├── dataset_100k.txt              # 100 mil registros
│   ├── dataset_1000k.txt             # 1 milhão registros
│   └── dataset_10000k.txt            # 10 milhões registros
├── resultados/
│   ├── resultados_completos.csv       # Dados em CSV
│   ├── relatorio_detalhado.txt        # Relatório completo
│   └── analise_comparativa.txt        # Análise e conclusões
└── FUNCOES_HASH_IMPLEMENTADAS.md     # Documentação das funções
```

## Métricas Coletadas

### 1. Desempenho
- **Tempo de inserção** (milissegundos)
- **Tempo de busca** (milissegundos)
- **Número de colisões** (critério específico do trabalho)

### 2. Distribuição
- **Fator de carga** (elementos/tamanho)
- **Gaps entre elementos** (menor, maior, médio)
- **Três maiores listas** (encadeamento)

### 3. Critério de Colisões
- **Encadeamento**: Cada elemento já na lista = 1 colisão
- **Rehashing**: Cada tentativa de probe = 1 colisão

## Como Executar

### Compilação
```bash
cd PJBL-HASH1
javac src/*.java
```

### Execução
```bash
java -cp src Main
```

### Saída
O programa irá:
1. Gerar dados aleatórios com seed fixa (12345)
2. Executar todos os testes de combinação
3. Exportar resultados para pasta `resultados/`
4. Exibir estatísticas no console

## Resultados e Análises

### Principais Descobertas

1. **Encadeamento**:
   - Melhor para fatores altos (> 1.0)
   - Performance linear com fator de carga
   - Nunca falha por overflow

2. **Linear Probing**:
   - Rápido para fatores baixos (< 0.5)
   - Sofre muito com agrupamento primário
   - Boa localidade de cache

3. **Quadratic Probing**:
   - Melhor que linear para fatores médios
   - Evita agrupamento primário
   - Pode não encontrar posição livre

4. **Double Hashing**:
   - Melhor distribuição estatística
   - Menos colisões que outros rehashing
   - Mais complexo de implementar

### Recomendações

- **Fator ≤ 0.5**: Double Hashing ou Quadratic Probing
- **Fator 0.5-0.75**: Encadeamento ou Double Hashing  
- **Fator > 0.75**: Apenas Encadeamento

## Garantia de Reprodutibilidade

- **Seed fixa**: 12345 para geração de dados
- **Dados persistidos**: Arquivos txt para validação
- **Algoritmos determinísticos**: Mesmas funções hash sempre

## Requisitos Atendidos

✅ **3 tamanhos de tabela** (1k, 10k, 100k)  
✅ **3 tamanhos de dados** (100k, 1M, 10M)  
✅ **Seed fixa** para reprodutibilidade  
✅ **Registro com 9 dígitos**  
✅ **Medição de tempo** (inserção e busca)  
✅ **Contagem de colisões** (critério específico)  
✅ **Estatísticas detalhadas** (gaps, maiores listas)  
✅ **Funções hash diversificadas**  
✅ **Encadeamento + Rehashing**  
✅ **Relatórios em CSV e texto**  
✅ **Análise comparativa**  
✅ **Código comentado**

## Limitações

- Rehashing não testado para fator > 0.75 (evitar overflow)
- Memória limitada para conjuntos muito grandes
- Apenas tipos primitivos e estruturas básicas conforme especificado

## Autores

Projeto desenvolvido como PJBL (Project-Based Learning) para análise de estruturas de dados hash.

---
*Implementação em Java utilizando apenas bibliotecas básicas conforme especificação do trabalho.*