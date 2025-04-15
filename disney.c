#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ctype.h>
#include <stdbool.h>

typedef struct {
    char show_id[50];
    char type[20];
    char title[200];
    char director[100];
    char **cast;
    int cast_count;
    char country[50];
    int time[3]; // time[0] = dia, time[1] = mês, time[2] = ano
    int release_year;
    char rating[10];
    char duration[20];
    char **listed_in;
    int listed_in_count;
} Disney;

Disney lista[1368];
int total = 0;

void ler_csv(char line[1000]){
    int i=0;
    char tmp[500];
    sscanf(line + i, "%[^,],", lista[total].show_id);
    i += strlen(lista[total].show_id) + 1;
    printf("%s\n", lista[total].show_id);
    // Lê o tipo do show
    sscanf(line + i, "%[^,],", lista[total].type);
    i += strlen(lista[total].type) + 1;
    printf("%s\n", lista[total].type);
    
    while (i < strlen(line)) {
       

        // Lê o título do show

        if(i== '"'){
            int z=0;
            while ((line[z]!= '"'&& line[z+1] != ',')) {
                tmp[z] = line[i + z];
                z++;
            }
            tmp[z] = '\0';
             i+=z+2;
            strcpy(lista[total].title, tmp);
           
            
        }
        else{
            sscanf(line + i, "%[^,],", lista[total].title);
            i += strlen(lista[total].title) + 1;
        }   
        if (line[i] == ',')
        {
            lista[total].director[0] = '\0';
            i++;
        }
        else if(line[i]== '"'){
            int z=0;
            while ((line[z]!= '"'&& line[z+1] != ',')) {
                tmp[z] = line[i + z];
                z++;
            }
            tmp[z] = '\0';
             i+=z+2;
            // Divide os diretores separados por vírgula e ordena
            char *diretores[100];
            int count = 0;
            char *token = strtok(tmp, ",");
            while (token != NULL) {
                diretores[count++] = token;
                token = strtok(NULL, ",");
            }

            // Ordena os diretores em ordem alfabética
            for (int x = 0; x < count - 1; x++) {
                for (int y = x + 1; y < count; y++) {
                    if (strcmp(diretores[x], diretores[y]) > 0) {
                        char *temp = diretores[x];
                        diretores[x] = diretores[y];
                        diretores[y] = temp;
                    }
                }
            }

            // Concatena os diretores ordenados em uma única string
            tmp[0] = '\0';
            for (int x = 0; x < count; x++) {
                strcat(tmp, diretores[x]);
                if (x < count - 1) {
                    strcat(tmp, ",");
                }
            }

            strcpy(lista[total].director, tmp);
        }
        else{
            sscanf(line + i, "%[^,],", lista[total].director);
            i += strlen(lista[total].director) + 1;
        }
        
        
        
    }
    
}

void imprimir(){
    for (int i = 0; i < total; i++) {
        printf("ID: %s\n", lista[i].show_id);
        printf("Tipo: %s\n", lista[i].type);
        printf("Título: %s\n", lista[i].title);
        printf("Diretor: %s\n", lista[i].director);
        printf("Elenco: ");
        for (int j = 0; j < lista[i].cast_count; j++) {
            printf("%s ", lista[i].cast[j]);
        }
        printf("\nPaís: %s\n", lista[i].country);
        printf("Data de lançamento: %02d/%02d/%04d\n", lista[i].time[0], lista[i].time[1], lista[i].time[2]);
        printf("Ano de lançamento: %d\n", lista[i].release_year);
        printf("Classificação: %s\n", lista[i].rating);
        printf("Duração: %s\n", lista[i].duration);
        printf("Gêneros: ");
        for (int j = 0; j < lista[i].listed_in_count; j++) {
            printf("%s ", lista[i].listed_in[j]);
        }
        printf("\n");
    }
        printf("Total de shows: %d\n", total);
}


int main() {
    
    FILE *file = fopen("disneyplus.csv", "r");
    char id[10];
char line[1000];
fgets(line, sizeof(line), file); // Skip the header line
while (fgets(id, sizeof(id), stdin) != NULL) {
    id[strcspn(id, "\n")] = '\0'; // Remove newline character from input
    rewind(file); // Reset file pointer to the beginning
    fgets(line, sizeof(line), file); // Skip the header line again
    while (fgets(line, sizeof(line), file) != NULL) {
        if (strncmp(line, id, strlen(id)) == 0) { // Check if the ID matches the start of the line
            ler_csv(line); // Process the line
            break;
        }
    }

}
   
}