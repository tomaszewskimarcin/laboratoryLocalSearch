package tools;

import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	public static int[] randomPermutation(int size)
	{
		int [] permutacja = new int[size];
		
		List<Integer> list = new ArrayList<Integer>();
		for(int i = 1;i<=size;i++){
			list.add(i);
		}
		java.util.Collections.shuffle(list);
		
		permutacja = list.stream().mapToInt(i -> i).toArray();
		
		return permutacja;
	}
		
	public static double calculateCost(int[] solution, Data data)
	{		
		int cost = 0;
      	
		for(int i = 0; i<data.getSize(); i++){
			for(int j = 0; j<data.getSize(); j++){
				cost += data.getMatrixA(i, j)*data.getMatrixB(solution[i]-1, solution[j]-1);
			}
		}
		
		return cost;
		
	}
	
	private static double algorithmR(int time, Data data){
		
		double minCost = Double.MAX_VALUE;
		
		long t= System.currentTimeMillis();
		long end = t+time*1000;
		try {
			while(System.currentTimeMillis() < end) {
				int[] dane = randomPermutation(data.getSize());
				double actualCost = calculateCost(dane, data);
				if(actualCost<minCost){
					minCost = actualCost;
				}
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return minCost;
		
	}

	private static int[] swap(int[] table,int i,int j){
		int[] newTable = table;
		int[] temp = table;
		
		newTable[i] = temp[j];
		newTable[j] = temp[i];
		
		return newTable;
	}
	
	private static double algorithmLS(int time, Data data){
		
		double minCost = Double.MAX_VALUE;
		
		int[] tmp = randomPermutation(data.getSize());
		minCost = calculateCost(tmp, data);
		int[] lepsze = new int[data.getSize()];
		long t= System.currentTimeMillis();
		long end = t+time*1000;
		try {
			while(System.currentTimeMillis() < end) {
				for(int i = 0;i<data.getSize();i++){
					for(int j = 0;j<data.getSize();j++){
						tmp = swap(tmp,i,j);
						

						System.out.println();
						for(int x = 0; x<tmp.length;x++){
							System.out.print(tmp[x]);
							break;
						}
						double calculated = calculateCost(tmp, data);
						if(calculated<minCost){
							minCost = calculated;
							lepsze = tmp;
							System.out.println(minCost);
						}
					}
				}
				tmp = lepsze;
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return minCost;
		
	}
	
	public static void main(String[] args) {
		
		int time = 60;
		//int[] opt = {18,14,10,3,9,4,2,12,11,16,19,15,20,8,13,17,5,7,1,6};
		Data data = new Data("path/nug20.dat");
		//System.out.println(calculateCost(opt, data));
		
		
		System.out.println(algorithmLS(time,data));
		
	}
}
