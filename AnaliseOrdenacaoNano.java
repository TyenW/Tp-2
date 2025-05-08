import java.util.Arrays;
import java.util.Random;

public class AnaliseOrdenacaoNano {
    // Variáveis para contagem de operações
    private static long comparacoes;
    private static long movimentacoes;

    public static void main(String[] args) {
        int[] tamanhos = {100, 1000, 10000, 100000};
        
        // Cabeçalho do CSV
        System.out.println("Tamanho,Algoritmo,Tempo(ns),Comparacoes,Movimentacoes");
        
        for (int tamanho : tamanhos) {
            int[] original = gerarArrayAleatorio(tamanho);
            
            // Testar cada algoritmo
            testarAlgoritmo("SelectionSort", Arrays.copyOf(original, original.length));
            testarAlgoritmo("InsertionSort", Arrays.copyOf(original, original.length));
            testarAlgoritmo("BubbleSort", Arrays.copyOf(original, original.length));
            testarAlgoritmo("QuickSort", Arrays.copyOf(original, original.length));
        }
    }
    
    private static void testarAlgoritmo(String algoritmo, int[] array) {
        // Aquecer a JVM (warm-up) para melhor precisão
        warmUp(algoritmo, Arrays.copyOf(array, Math.min(array.length, 1000)));
        
        comparacoes = 0;
        movimentacoes = 0;
        
        long inicio = System.nanoTime();
        
        switch(algoritmo) {
            case "SelectionSort":
                selectionSort(array);
                break;
            case "InsertionSort":
                insertionSort(array);
                break;
            case "BubbleSort":
                bubbleSort(array);
                break;
            case "QuickSort":
                quickSort(array, 0, array.length-1);
                break;
        }
        
        long fim = System.nanoTime();
        long tempo = fim - inicio;
        
        // Verificar se a ordenação está correta (para debug)
        if (!estaOrdenado(array)) {
            System.err.println("Erro: array não ordenado corretamente pelo " + algoritmo);
        }
        
        // Saída no formato CSV para facilitar a plotagem
        System.out.printf("%d,%s,%d,%d,%d%n", 
                          array.length, algoritmo, tempo, comparacoes, movimentacoes);
    }
    
    private static void warmUp(String algoritmo, int[] array) {
        // Executar uma vez sem medir para aquecer a JVM
        switch(algoritmo) {
            case "SelectionSort":
                selectionSort(array);
                break;
            case "InsertionSort":
                insertionSort(array);
                break;
            case "BubbleSort":
                bubbleSort(array);
                break;
            case "QuickSort":
                quickSort(array, 0, array.length-1);
                break;
        }
    }
    
    private static boolean estaOrdenado(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    private static int[] gerarArrayAleatorio(int tamanho) {
        Random random = new Random();
        int[] array = new int[tamanho];
        for (int i = 0; i < tamanho; i++) {
            array[i] = random.nextInt(tamanho * 10);
        }
        return array;
    }
    
    // Algoritmos de ordenação
    
    public static void selectionSort(int[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            int indiceMenor = i;
            for (int j = i + 1; j < array.length; j++) {
                comparacoes++;
                if (array[j] < array[indiceMenor]) {
                    indiceMenor = j;
                }
            }
            if (indiceMenor != i) {
                int temp = array[i];
                array[i] = array[indiceMenor];
                array[indiceMenor] = temp;
                movimentacoes += 3; // 3 movimentações (swap)
            }
        }
    }
    
    public static void insertionSort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int chave = array[i];
            int j = i - 1;
            
            movimentacoes++; // movimentação da chave
            
            while (j >= 0 && array[j] > chave) {
                comparacoes++;
                array[j + 1] = array[j];
                movimentacoes++;
                j--;
            }
            comparacoes++; // última comparação que falha
            
            array[j + 1] = chave;
            movimentacoes++;
        }
    }
    
    public static void bubbleSort(int[] array) {
        boolean trocou;
        for (int i = 0; i < array.length - 1; i++) {
            trocou = false;
            for (int j = 0; j < array.length - 1 - i; j++) {
                comparacoes++;
                if (array[j] > array[j + 1]) {
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    movimentacoes += 3; // 3 movimentações (swap)
                    trocou = true;
                }
            }
            if (!trocou) break;
        }
    }
    
    public static void quickSort(int[] array, int inicio, int fim) {
        if (inicio < fim) {
            int indicePivo = particionar(array, inicio, fim);
            quickSort(array, inicio, indicePivo - 1);
            quickSort(array, indicePivo + 1, fim);
        }
    }
    
    private static int particionar(int[] array, int inicio, int fim) {
        int pivo = array[fim];
        int i = inicio - 1;
        
        movimentacoes++; // movimentação do pivo
        
        for (int j = inicio; j < fim; j++) {
            comparacoes++;
            if (array[j] <= pivo) {
                i++;
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                movimentacoes += 3; // 3 movimentações (swap)
            }
        }
        
        int temp = array[i + 1];
        array[i + 1] = array[fim];
        array[fim] = temp;
        movimentacoes += 3; // 3 movimentações (swap)
        
        return i + 1;
    }
}