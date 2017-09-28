package main;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

	public static int INF = 101;
	
	public class Vertice {
		private int valor;
		private List<Vertice> adjacentes;

		private Vertice pai;

		public Vertice(int valor) {
			this.valor = valor;
			this.adjacentes = new ArrayList<>();
		}

		public int getValor() {
			return valor;
		}

		public List<Vertice> getAdjacentes() {
			return adjacentes;
		}
		
		public void adicionarAresta(Vertice destino) {
			this.adjacentes.add(destino);
		}

		public Vertice getPai() {
			return pai;
		}

		public void setPai(Vertice pai) {
			this.pai = pai;
		}
		
	}
	
	
	
	public class Grafo {
		private List<Vertice> vertices;
		private int verticeInicial;
		private int tamanhoMatriz;
		private int[][] distancias;
		private int[][] pesos;
		
		public Grafo(int numeroVertices, int verticeInicial) {
			this.vertices = new ArrayList<>();
			this.verticeInicial = verticeInicial;
			tamanhoMatriz = numeroVertices + verticeInicial;
			distancias = new int[tamanhoMatriz][tamanhoMatriz];
			pesos = new int[tamanhoMatriz][tamanhoMatriz];
			for (int i = verticeInicial; i < tamanhoMatriz; i++) {
				this.vertices.add(new Vertice(i));
				for (int j = verticeInicial; j < tamanhoMatriz; j++) {
					if (i != j){
						distancias[i][j] = INF;
						pesos[i][j] = INF;
					}
					else{
						distancias[i][j] = 0;
						pesos[i][j] = 0;
					}
				}
			}
		}

		public List<Vertice> getVertices() {
			return vertices;
		}

		public void setVertices(List<Vertice> vertices) {
			this.vertices = vertices;
		}

		public int getDistanciaMinima(Vertice origem, Vertice destino) {
			return distancias[origem.getValor()][destino.getValor()];
		}
		
		public void setDistanciaRaiz(Vertice verticeRaiz, Vertice verticeAtual, int distancia) {
			distancias[verticeRaiz.getValor()][verticeAtual.getValor()] = distancia;
		}

		public void inicializarVertices() {
			for (Vertice vertice : this.vertices) {
				vertice.setPai(null);
			}
		}

		public void adicionarAresta(int origem, int destino) {
			Vertice verticeOrigem = this.vertices.get(origem - verticeInicial);
			Vertice verticeDestino = this.vertices.get(destino - verticeInicial);
			verticeOrigem.adicionarAresta(verticeDestino);
			pesos[origem][destino] = 1;
		}

		public int minimo(int numero1, int numero2) {
			return Integer.min(numero1, numero2);
		}
		
		public void carregarDistancias() {
			int [][][] m = new int[this.tamanhoMatriz][this.tamanhoMatriz][this.tamanhoMatriz];
			m[0] = this.pesos.clone();
			for (int k = 1; k < this.tamanhoMatriz; k++) {
				for (int i = this.verticeInicial; i < this.tamanhoMatriz; i++ ) {
					for (int j = this.verticeInicial; j < this.tamanhoMatriz; j++ ) {
						m[k][i][j] = minimo(m[k-1][i][j],m[k-1][i][k]+m[k-1][k][j]);
					}
				}
			}
			this.distancias = m[this.tamanhoMatriz-1];
		}
		
		public String getMediaDistancia(Integer numeroCaso) {
			int qtdePares = 0;
			Integer somaDistancias = 0;
			for (int i = this.verticeInicial; i < this.tamanhoMatriz; i++) {
				for (int j = this.verticeInicial; j < this.tamanhoMatriz; j++) {
					if (i != j && this.distancias[i][j] < INF) {
						somaDistancias += this.distancias[i][j];
						qtdePares++;
					}
				}
			}
			return "Case "+ numeroCaso.toString() + ": average length between pages = " + String.format("%.3f", somaDistancias.doubleValue() / qtdePares) + " clicks";
		}

		
	}
	
	public static void main(String[] args) {
		Main mainObject = new Main();
		Scanner scanner = new Scanner(System.in);
		Grafo grafo;
		int paginaOrigem, paginaDestino;
		boolean grafoVazio;
		int numeroCaso = 1;
		// La�o externo - uma iteracao para cada grafo
		while (true) {
			grafo = mainObject.new Grafo(100, 1);
			grafoVazio = true;
			
			//Laco para carregar as arestas de um grafo
			// Teremos um grafo de 100 vertices para esse problema, contando a partir do 1
			while (true) {
				paginaOrigem = scanner.nextInt();
				paginaDestino = scanner.nextInt();

				if (paginaOrigem == 0 && paginaDestino == 0) {
					if (grafoVazio) {
						return;
					} else {
						break;
					}
				}
				// Carregar o grafo com as arestas
				else {
					grafoVazio = false;
					grafo.adicionarAresta(paginaOrigem, paginaDestino);
				}
			}
			//Se chegar aqui, o grafo possui arestas e ja pode ser trabalhado com o algoritmo
			grafo.carregarDistancias();
			System.out.println(grafo.getMediaDistancia(numeroCaso++));

		}
	}
	
}
