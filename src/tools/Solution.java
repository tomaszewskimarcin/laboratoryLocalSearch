package tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	private static Data data;
	
	static FileWriter fw;
	
	private static int time;
	private static long nanoS = 0;
	private static long nanoG = 0;
	private static long nanoR = 0;
	
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
		try {
			long end = System.nanoTime()+time;
			 do{
				int[] dane = randomPermutation(data.getSize());
				double actualCost = calculateCost(dane, data);
				if(actualCost<minCost){
					minCost = actualCost;
				}
				Thread.sleep(10);
			}while(System.nanoTime() < end);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return minCost;
		
	}

	public static final int[] swap (int[] a, int i, int j) {
		  int t = a[i];
		  a[i] = a[j];
		  a[j] = t;
		  return a;
		}
	
	private static double algorithmLSS(int time, Data data){
		
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
						int[] tab = swap(tmp,i,j);
						double calculated = calculateCost(tab, data);
						if(calculated<minCost){
							minCost = calculated;
							lepsze = tab;
						}
					}
				}
				if(lepsze != tmp){
					tmp = lepsze;
				}else{
					break;
				}
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return minCost;
		
	}

	private static double algorithmLSG(int time, Data data){
		
		double minCost = Double.MAX_VALUE;
		
		int[] tmp = randomPermutation(data.getSize());
		minCost = calculateCost(tmp, data);
		int[] lepsze = new int[data.getSize()];
		boolean breakerFlag = false;
		long t= System.currentTimeMillis();
		long end = t+time*1000;
		try {
			while(System.currentTimeMillis() < end) {
				for(int i = 0;i<data.getSize();i++){
					for(int j = 0;j<data.getSize();j++){
						int[] tab = swap(tmp,i,j);
						double calculated = calculateCost(tab, data);
						if(calculated<minCost){
							minCost = calculated;
							lepsze = tab;
							breakerFlag = true;
							break;
						}
						if(breakerFlag){
							break;
						}
					}
				}
				if(tmp != lepsze){
					tmp = lepsze;
				}else{
					break;
				}
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return minCost;
		
	}
	
	private static double doSteepest(int time, Data data){
		long start = System.nanoTime();
		double minValue = Double.MAX_VALUE;
		
		for(int i = 0;i<10;i++){
			double tmp = algorithmLSS(time, data);
			if(tmp<minValue){
				minValue = tmp;
			}
		}
		long elapsedTime = System.nanoTime()-start;
		nanoS = elapsedTime;
		return minValue;
	}
	
	private static double doGreedy(int time, Data data){
		long start = System.nanoTime();
		double minValue = Double.MAX_VALUE;
		
		for(int i = 0;i<10;i++){
			double tmp = algorithmLSG(time, data);
			if(tmp<minValue){
				minValue = tmp;
			}
		}
		long elapsedTime = System.nanoTime()-start;
		nanoG = elapsedTime;		
		return minValue;
	}
	
	private static double doRandom(int Time, Data data){
		long start = System.nanoTime();
		double minValue = Double.MAX_VALUE;
		
		for(int i = 0;i<10;i++){
			double tmp = algorithmR(time, data);
			if(tmp<minValue){
				minValue = tmp;
			}
		}
		long elapsedTime = System.nanoTime()-start;
		nanoR = elapsedTime;		
		return minValue;
	}
	
	private static void performTests(int time, String path){
		
		try {
			String tmp = "";
			String name = "";
			if(path.indexOf('/')>0){
				name = path.substring(path.lastIndexOf('/')+1, path.length());
				name = name.substring(0,name.lastIndexOf('.'));
			}else{
				name = path.substring(0,path.lastIndexOf('.'));
			}
			fw = new FileWriter("output_"+name+".txt");
			fw.write("LP\tFile\tSteepest\tSteepestTime\tGreedy\tGreedyTime\tRandom\tRandomTime");
			
			for(int j = 0;j<10;j++){
				if(path.indexOf('/')>0){
					tmp = "\n"+(j+1)+"\t"+path.substring(path.lastIndexOf('/')+1, path.length())+"\t";
				}else{
					tmp = "\n"+(j+1)+"\t"+path+"\t";
				}
				System.out.println("PATH: "+path);
				data = new Data(path);
				System.out.println("---WORKING---");
				double steepest = doSteepest(time, data);
				System.out.println("Steepest: "+steepest);
				System.out.println("Time: "+nanoS+"ns");
				
				tmp+=steepest+"\t"+nanoS+"\t";
				
				data = new Data(path);
				System.out.println("---WORKING---");
				double greedy = doGreedy(time, data);
				System.out.println("Greedy: "+greedy);
				System.out.println("Time: "+nanoG+"ns");

				tmp+=greedy+"\t"+nanoG+"\t";

				data = new Data(path);
				int avgTime = (int)(nanoS+nanoG)/2;
				System.out.println("---WORKING---");
				double random = doRandom(avgTime, data);
				System.out.println("Random: "+random);
				System.out.println("Time: "+nanoR+"ns");

				tmp+=random+"\t"+nanoR+"\t";
				
				fw.write(tmp);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		time = 15;
		String path = args[0];

		performTests(time, path);
	    
	}
}
