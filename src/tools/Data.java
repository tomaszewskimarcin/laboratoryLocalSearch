package tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Data {

	private int size;
	private ArrayList<Integer> values;
	private int[][] matrixA;
	private int[][] matrixB;

	public Data(String path) {
		
		size = 0;
		values = new ArrayList<>();
		
		readData(path);

		matrixA = new int[size][size];
		matrixB = new int[size][size];
		
		if (values.size() == size * size * 2) {
			fillWithValues();
		} else
			fillWithZeros(size);

	}

	private void fillWithValues() {

		for (int i = 0; i < size; i++) {

			for (int j = 0; j < size; j++) {
				matrixA[i][j] = values.get(size * i + j);
				matrixB[i][j] = values.get(size * i + size * size + j);
			}
		}

	}

	private void readData(String path) {

		BufferedReader br = null;
		String curLine;

		try {

			String[] row;

			br = new BufferedReader(new FileReader(path));

			size = Integer.parseInt(br.readLine().trim());

			while ((curLine = br.readLine()) != null) {
				if (curLine.isEmpty());

				row = curLine.trim().split("\\s+");

				if (row.length > 1) {

					for (int j = 0; j < row.length; j++) {
						try {
							values.add(Integer.parseInt(row[j]));
						} catch (NumberFormatException nfe) {
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public int getSize() {
		return size;
	}

	public void displayMatrixA() {
		for (int[] row : matrixA) {
			System.out.println(Arrays.toString(row));
		}
	}

	public void displayMatrixB() {
		for (int[] row : matrixB) {
			System.out.println(Arrays.toString(row));
		}
	}

	public int getMatrixA(int i, int j) {

		return matrixA[i][j];
	}

	public int getMatrixB(int i, int j) {

		return matrixB[i][j];
	}

	private void fillWithZeros(int size) {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++){
				matrixA[i][j] = matrixB[i][j] = 0;
			}
		}
	}

	public static void main(String[] args) {
		
		Data data = new Data("path");
		data.displayMatrixA();
		System.out.println("\n");
		data.displayMatrixB();
	}
	
}
