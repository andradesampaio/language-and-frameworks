/*
 * ENUNCIADO:
 * Elabore uma aplicação Java que faça a geração de 5 listas (faça isso forma assíncrona e paralela) contendo números aleatórios e únicos.
 * Após a criação das listas, faça um processo de consolidação unindo todas elas em uma única lista maior e imprima o resultado final.
 * 
 * ALUNOS:
 * Antonio Augusto Gaspar Merlini - 1904938
 * Andrade Alves Sampaio - 1904936
 * 
 * 
 * 
 */

package edu.br.impacta.tarefa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

public class ListGenerator {

	private static final ExecutorService executor = Executors.newWorkStealingPool(5);

	public static void main(String[] args) {

		ArrayList<Future<HashSet<Integer>>> result = new ArrayList<Future<HashSet<Integer>>>();
		result.add(asyncList());
		result.add(asyncList());
		result.add(asyncList());
		result.add(asyncList());
		result.add(asyncList());

		try {
			while (result.stream().anyMatch(x -> !x.isDone())) {
				Thread.sleep(100);
			}

			List<Integer> finalList = result.stream().map(x -> {
				try {
					return x.get();
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
				return new HashSet<Integer>();
			}).collect(Collectors.flatMapping(Collection::stream, Collectors.toList()));

			System.out.println();
			System.out.println("Uniao das listas com 5 elementos: ");
			System.out.println(finalList);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			executor.shutdown();
		}
	}

	private static Future<HashSet<Integer>> asyncList() {
		Random randNum = new Random();
		HashSet<Integer> set = new HashSet<>();

		return executor.submit(() -> {
			while (set.size() < 5) {
				try {
					set.add(randNum.nextInt(100) + 1);
					System.out.println(set);
					Thread.sleep(500);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
					e.printStackTrace();
				}
			}

			return set;
		});
	}
}
