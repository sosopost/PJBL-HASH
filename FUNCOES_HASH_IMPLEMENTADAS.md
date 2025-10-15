# Funções Hash Implementadas

## 1. Encadeamento (ChainingHashTable)
**Função Hash**: Multiplicação com primo
```java
private int funcaoHash(String codigo) {
    int hash = 0;
    for (int i = 0; i < codigo.length(); i++) {
        hash = (hash * 37 + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
    }
    return hash % tabela.length;
}
```
- **Tipo**: Método da multiplicação
- **Características**: Usa multiplicação por primo (37) para distribuição uniforme
- **Adequado para**: Encadeamento, pois suporta qualquer fator de carga

## 2. Linear Probing (LinearProbingHashTable)
**Função Hash**: Multiplicação de Knuth
```java
private int funcaoHash(String codigo) {
    long hash = 0;
    long A = 2654435761L; // (sqrt(5) - 1) / 2 * 2^32
    for (int i = 0; i < codigo.length(); i++) {
        hash = hash * A + (codigo.charAt(i) - '0');
    }
    return (int)((hash & 0x7FFFFFFF) % tabela.length);
}
```
- **Tipo**: Método multiplicativo de Knuth
- **Características**: Usa constante áurea para boa distribuição
- **Adequado para**: Linear probing com distribuição uniforme

## 3. Quadratic Probing (QuadraticProbingHashTable)
**Função Hash**: Polinomial (Horner)
```java
private int funcaoHash(String codigo) {
    int hash = 0;
    int p = 31; // primo pequeno
    for (int i = 0; i < codigo.length(); i++) {
        hash = (hash * p + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
    }
    return hash % tabela.length;
}
```
- **Tipo**: Método polinomial usando regra de Horner
- **Características**: Eficiente e com boa distribuição
- **Adequado para**: Quadratic probing, evita agrupamento primário

## 4. Double Hashing (DoubleHashingHashTable)
**Primeira Função Hash**: Somatória simples
```java
private int hash1(String codigo) {
    int hash = 0;
    for (int i = 0; i < codigo.length(); i++) {
        hash = (hash + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
    }
    return hash % tabela.length;
}
```

**Segunda Função Hash**: Multiplicação por primo
```java
private int hash2(String codigo) {
    int hash = 1;
    for (int i = 0; i < codigo.length(); i++) {
        hash = (hash * 7 + (codigo.charAt(i) - '0')) & 0x7FFFFFFF;
    }
    int step = 1 + (hash % (tabela.length - 1));
    return step;
}
```
- **Tipo**: Combinação de duas funções diferentes
- **Características**: hash1 simples, hash2 garante step != 0
- **Adequado para**: Double hashing, melhor distribuição que probing simples

## Critério de Colisões
O cálculo de colisões segue o especificado no trabalho:
- **Encadeamento**: Cada elemento já na lista = 1 colisão
- **Rehashing**: Cada tentativa de inserção = 1 colisão

## Vantagens de Cada Abordagem
1. **Encadeamento**: Nunca há overflow, funciona com qualquer fator de carga
2. **Linear Probing**: Simples, boa localidade de cache, mas sofre agrupamento primário
3. **Quadratic Probing**: Evita agrupamento primário, mas pode ter agrupamento secundário
4. **Double Hashing**: Melhor distribuição, evita ambos agrupamentos, mais complexo