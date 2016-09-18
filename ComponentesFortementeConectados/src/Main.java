import  java.util.Scanner;
class Main
{
    private static Scanner entrada = new Scanner(System.in);
    
    public static void main(String[] args)
    {
        int ordem, i, j;
        Grafo g;
        // leitura da ordem da matriz
        ordem = entrada.nextInt();
        g = new Grafo(ordem);
        // leitura da matriz de adjacencias
        for(i = 1 ; i <= ordem ; i++)
        {
            for(j = 1 ; j <= ordem ; j++)
            {
                g.AddAresta(i, j, entrada.nextInt());
            }
        }
        System.out.println(g.GetComponentesFortementeConectados());
        //g.ImprimirAdjacencias();
    }
    
    private static class Grafo
    {
        private static final String BRANCO = "BRANCO";
        private static final String CINZA = "CINZA";
        private static final String PRETO = "PRETO";
        
        private int adj[][];
        private int vertices = 0;
        private String cor[];
        private int anterior[];
        private int chegada[];
        private int saida[];
        private int tempo = 0;
        private int verticesSaidaCrescente[];
        private int cfc = 0;

        public Grafo(int quantidade)
        {
            vertices = quantidade;
            adj = new int [quantidade + 1][quantidade + 1];
            cor = new String[quantidade + 1];
            anterior = new int[quantidade + 1];
            chegada = new int[quantidade + 1];
            saida = new int[quantidade + 1];
            verticesSaidaCrescente = new int[quantidade + 1];
        }
        public int getAresta(int origem, int destino)
        {
            return adj[origem][destino];
        }
        public void AddAresta(int linha, int coluna, int value)
        {
            adj[linha][coluna] = value;
        }
        private void ordenarVerticesComMaiorTempoDeSaida()
        {
            int copiaSaida[] = saida.clone();
            int aux;
            
            int minimun;
            for(int i = 1 ; i <= vertices ; i++)
            {
                minimun = i;
                for(int j = 1; j <= vertices ; j++)
                {
                    if(copiaSaida[j] < copiaSaida[minimun])
                    {
                        minimun = j;
                    }
                }
                verticesSaidaCrescente[i] = minimun;
                copiaSaida[minimun] = Integer.MAX_VALUE;
            }
            
            //verticesSaidaDecrescente = saida.clone();
            //Mergesort_ND(verticesSaidaDecrescente, 1, vertices);
           
            //ImprimirVetor(verticesSaidaDecrescente);
            //Mergesort_ND(saida, 1, vertices);
            //ImprimirVetor(saida);
        }
        private void Merge(int A[], int p, int q, int r)
        {
            int a, b, i, j;
            
            a = q - p + 1;
            b = r - q;
            
            int esq[] = new int[a+1], dir[] = new int[b+1];
            
            for(i = 0 ; i < a ; i++)
            {
                esq[i] = A[p+i];
            }
            for(j = 0 ; j < b ; j++)
            {
                dir[j] = A[q+j+1];
            }
            esq[a] = Integer.MAX_VALUE;
            dir[b] = Integer.MAX_VALUE;
            i = j = 0;
            for(int k = p ; k <= r ; k++)
            {
                if(esq[i] <= dir[j])
                {
                    A[k] = esq[i];
                    i++;
                }
                else
                {
                    A[k] = dir[j];
                    j++;
                }
            }
        }
        private void Mergesort_ND(int A[], int p, int r)
        {
            int q;
            if(p < r)
            {
                q = Math.floorDiv(p + r, 2);
                //q = floor((p + r) / 2);
                Mergesort_ND(A, p, q);
                Mergesort_ND(A, q + 1, r);
                Merge(A, p, q, r);
            }
        }
        public void DFS()
        {
            for(int u = 1 ; u <= vertices ; u++)
            {
                cor[u] = BRANCO;
                anterior[u] = -1;
            }
            tempo = 0;
            for(int u = 1 ; u <= vertices ; u++)
            {
                if(cor[u].equals(BRANCO))
                {
                    DFS_Visita(u);
                }
            }
        }
        public void DFS_Visita(int u)
        {
            tempo++;
            chegada[u] = tempo;
            cor[u] = CINZA;
            for(int v = 1 ; v <= vertices ; v++)    //para cada vertice adjacente a u
            {
                if(getAresta(u, v) == 1)    //se tiver aresta de u para i
                {
                    if(cor[v].equals(BRANCO))
                    {
                        anterior[u] = v;
                        DFS_Visita(v);  //visita v
                    }
                }
            }
            cor[u] = PRETO;     //marca como visitado
            tempo++;
            saida[u] = tempo;   //marca o tempo de saida
        }
        
        public void DFS_Gt()
        {
            for(int u = vertices ; u > 0 ; u--)
            {
                cor[u] = BRANCO;
                //anterior[verticesSaidaCrescente[u]] = -1;
            }
            //tempo = 0;
            for(int u = vertices ; u > 0 ; u--)
            {
                
                if(cor[verticesSaidaCrescente[u]].equals(BRANCO))
                {
                    System.out.println("o vertice "+verticesSaidaCrescente[u]+" eh branco");
                    cfc++;
                    DFS_Visita_Gt(verticesSaidaCrescente[u]);
                }
            }
        }
        public void DFS_Visita_Gt(int u)
        {
            //tempo++;
            //chegada[u] = tempo;
            cor[u] = CINZA;
            for(int v = 1 ; v <= vertices ; v++)    //para cada vertice adjacente a u
            {
                if(getAresta(v, u) == 1)    //se tiver aresta de v para u
                {
                    if(cor[v].equals(BRANCO))
                    {
                        //anterior[u] = v;
                        DFS_Visita(v);  //visita v
                    }
                }
            }
            cor[u] = PRETO;     //marca como visitado
            //tempo++;
            //saida[u] = tempo;   //marca o tempo de saida
        }
        public void ImprimirVetor(int A[])
        {
            System.out.print("\n<!------------------------------------------------------->\n");
            for(int i = 1 ; i < A.length ; i++)
            {
                System.out.print(A[i] + "  ");
            }
        }
        public void ImprimirAdjacencias()
        {
            System.out.print("\n<!------------------------------------------------------->\n");
            int i, j;
            for(i = 1 ; i <= vertices ; i++)
            {
                for(j = 1 ; j <= vertices ; j++)
                {
                    System.out.print(adj[i][j] + " ");
                }
                System.out.println("");
            }
        }
        public int GetComponentesFortementeConectados()
        {
            DFS();
            ordenarVerticesComMaiorTempoDeSaida();
            DFS_Gt();
            //ImprimirVetor(verticesSaidaCrescente);
            //ImprimirVetor(saida);
            return cfc;
        }
    }
}
 