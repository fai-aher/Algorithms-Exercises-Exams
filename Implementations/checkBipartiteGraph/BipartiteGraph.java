/* 
 *  Practical homework 5 - Algorithms' Analysis and Design
 *  Point 3: Algorithm to identify if a graph (represented with a adjacency list)
 *  is bipartite or not. If it is, it returns the two disjoint sets of vertex.
 *  
 *  Section 3 - Group 9
 * 
 *  It is considered that these structures are used in a non-directed graph with
 *  n vertex and m edges.
 */

 import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.util.ArrayList;
 import java.util.LinkedList;
 import java.util.Queue;
 
 public class BipartiteGraph {

    /* En esta funcion, se leen los casos de prueba apartir del archivo de texto de entrada (entrada.IN),
     * se revisa que la linea leída no sea vacía y se intenta procesar esa linea al llamar a la función processInput()
     * dentro de una estructura try – catch.
     */
     public static void main(String[] args) {
         try {
             BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
             String input;

            // Tiempo actual en milisegundos antes de ejecutar las pruebas
            long startTime = System.currentTimeMillis();

            // Se lee la entrada línea por línea
             while ((input = reader.readLine()) != null) {

                if (input.isEmpty()) {
                    continue;
                }
                 try {
                     processInput(input);
                 } catch (Exception e) {
                     System.out.println(e.getMessage());
                 }
                 System.out.println(); // Se agrega una línea en blanco entre cada caso de prueba
             }

            // Tiempo actual en milisegundos luego de ejecutar las pruebas
             long endTime = System.currentTimeMillis();
 
             // Diferencia entre los dos tiempos para obtener el tiempo total de ejecución
             long totalTime = endTime - startTime;
 
             System.out.println("Tiempo total de ejecución: " + totalTime + " milisegundos");
 
         } catch (IOException e) {
             System.out.println("Error al leer la entrada.");
         }
     }


     /*
      * Esta funcion se encarga de organizar toda la información contenida dentro de la linea que se le pasó
      * como parámetro teniendo en cuenta la estructura de los input utilizados (más adelante se explica con más detalle).
      *
      * Esa función también revisa que:

        •	La cantidad de aristas especificadas coincida con la cantidad dada por parametro (m).
        •	Ningún vértice de los especificados en las aristas sea mayor al numero máximo que puede tener un vértice en ese grafo.

      * Una tarea muy importante de esta función es revisar los pares de nodos que denotan las aristas del grafo y realizar
      * las operaciones de añadir el primer nodo en la lista de adyacencia del segundo y viceversa.
      *
      * Finalmente, esta función llama al método checkBipartite() para que se de una respuesta.
      */
 
     public static void processInput(String input) throws Exception {
         String[] splitInput = input.split(" ");
         int n = Integer.parseInt(splitInput[0]);
         int m = Integer.parseInt(splitInput[1]);
 
         if (splitInput.length != 2 * m + 2) {

            // Se verifica si la cantidad de aristas dada es consistente con la entrada
             throw new Exception("La cantidad de aristas especificadas no coincide con la cantidad dada por parametro (m).");
         }
 
        // Se crea la lista de adyacencia para representar el grafo
         ArrayList<ArrayList<Integer>> adjacencyList = new ArrayList<>();
 
         for (int i = 0; i <= n; i++) {
             adjacencyList.add(new ArrayList<>());
         }

         // Se llena la lista de adyacencia con las aristas dadas en la entrada
         for (int i = 2; i < splitInput.length; i += 2) {
             int u = Integer.parseInt(splitInput[i]);
             int v = Integer.parseInt(splitInput[i + 1]);

             if (u == 0 || v == 0) {
                throw new Exception("Los vértices en las aristas deben ser mayores que 0.");
            }
 
             if (u > n || v > n) {
                 throw new Exception("Uno de los vértices especificados en las aristas es mayor al numero maximo que puede tener un vertice en ese grafo.");
             }
 
             adjacencyList.get(u).add(v);
             adjacencyList.get(v).add(u);
         }

         // Se verifica si el grafo es bipartito y, de ser así, se imprimen los conjuntos de vértices
         checkBipartite(adjacencyList, n);
     }

     /* Esta funcion se encarga de dar el veredicto final sobre si el grafo es o no bipartito. Al inicio,
      * se crea un arreglo de enteros llamado ‘color’, el cual almacena la información del color de cada vértice. 

        Los colores utilizados fueron:
        •	0, para los vértices que se visitan pero que aún no se colorean para compararlos con sus adyacentes.
        •	1, como uno de los dos colores que permite caracterizar el grafo como bipartito o no.
        •	2, como el segundo color que ayuda en la comparación y decisión final.

      * Para analizar si el grafo cumple con la propiedad de bipartición, se utilizaron dos estrategias relevantes:

        1.	Realizar un recorrido con un algoritmo de búsqueda BFS(Breadth-First Search) o búsqueda en anchura para moverse sobre los nodos del grafo, pintarlos y revisar que no hayan dos adyacentes del mismo color.
        2.	Implementar una cola (queue) por medio de una estructura LinkedList de java para ir guardando los vértices cuya lista de adyacencia falta colorear y revisar. 

      * El proceso de revisar y colorear los nodos se realiza de manera iterativa, al inicio se toma un primer vértice,
      * se colorea con 1 y se añade a la cola para que luego sus adyacentes sean revisados.
      * Si los adyacentes no están coloreados (color = 0), se colorean con el color opuesto del vértice actual.
      * Si los adyacentes sí están coloreados, se revisa que el color no sea igual al vértice actual, si lo es,
      * se define que el grafo no es bipartito y se termina la revisión de ese caso.
      */
 
     public static void checkBipartite(ArrayList<ArrayList<Integer>> adjacencyList, int n) {
         int[] color = new int[n + 1];
         boolean isBipartite = true;
         Queue<Integer> queue = new LinkedList<>();

        // Se recorre el grafo usando BFS y se asignan colores a los vértices
         for (int i = 1; i <= n && isBipartite; i++) {
             if (color[i] == 0) {
                 queue.add(i);
                 color[i] = 1;
 
                 while (!queue.isEmpty() && isBipartite) {
                     int current = queue.poll();
 
                     for (int neighbor : adjacencyList.get(current)) {
                         if (color[neighbor] == 0) {
                             color[neighbor] = 3 - color[current];
                             queue.add(neighbor);
                         } else if (color[neighbor] == color[current]) {
                             isBipartite = false;
                             break;
                         }
                     }
                 }
             }
         }

         // Se imprime el resultado según si el grafo es bipartito o no
 
         if (isBipartite) {
             System.out.println("El grafo especificado es bipartito.");
             printSets(color);
         } else {
             System.out.println("El grafo especificado no es bipartito.");
         }
     }
 

     /* la cual se encarga de filtrar el array de colores (color) para separar los nodos
      * de un mismo color en dos listas diferentes e imprimirlas.
      */
     public static void printSets(int[] color) {
         ArrayList<Integer> set1 = new ArrayList<>();
         ArrayList<Integer> set2 = new ArrayList<>();
 
        // Se separan los vértices en dos conjuntos según su color
         for (int i = 1; i < color.length; i++) {
             if (color[i] == 1) {
                 set1.add(i);
             } else {
                 set2.add(i);
             }
         }
         // Se imprimen los conjuntos de vertices por medio del estandar output
         System.out.println("Conjunto de vertices 1: " + set1);
         System.out.println("Conjunto de vertices 2: " + set2);
     }
 }