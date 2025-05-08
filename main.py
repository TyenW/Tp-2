import pandas as pd
import matplotlib.pyplot as plt

# Ler os dados do CSV
df = pd.read_csv('resultados.csv')

# Gráfico de tempo de execução
plt.figure(figsize=(10, 6))
for algoritmo in df['Algoritmo'].unique():
    subset = df[df['Algoritmo'] == algoritmo]
    plt.plot(subset['Tamanho'], subset['Tempo(ms)'], label=algoritmo, marker='o')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Tamanho do Array (log)')
plt.ylabel('Tempo de Execução (ms) (log)')
plt.title('Tempo de Execução por Algoritmo')
plt.legend()
plt.grid()
plt.savefig('tempo_execucao.png')
plt.show()

# Gráfico de comparações
plt.figure(figsize=(10, 6))
for algoritmo in df['Algoritmo'].unique():
    subset = df[df['Algoritmo'] == algoritmo]
    plt.plot(subset['Tamanho'], subset['Comparacoes'], label=algoritmo, marker='o')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Tamanho do Array (log)')
plt.ylabel('Número de Comparações (log)')
plt.title('Comparações por Algoritmo')
plt.legend()
plt.grid()
plt.savefig('comparacoes.png')
plt.show()

# Gráfico de movimentações
plt.figure(figsize=(10, 6))
for algoritmo in df['Algoritmo'].unique():
    subset = df[df['Algoritmo'] == algoritmo]
    plt.plot(subset['Tamanho'], subset['Movimentacoes'], label=algoritmo, marker='o')
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Tamanho do Array (log)')
plt.ylabel('Número de Movimentações (log)')
plt.title('Movimentações por Algoritmo')
plt.legend()
plt.grid()
plt.savefig('movimentacoes.png')
plt.show()