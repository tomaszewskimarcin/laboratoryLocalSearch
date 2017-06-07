package tools;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
	
	private static Data data;
	
	static FileWriter fw;
	
	private static int time;
	private static long nanoS = 0;
	private static long nanoG = 0;
	
	double stepsS = 0;
	double stepsG = 0;
	
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
	
	private static Object[] algorithmLSS(int time, Data data){
		
		double minCost = Double.MAX_VALUE;
		
		int steps = 0;
		int checked = 0;
		
		int[] tmp = randomPermutation(data.getSize());
		minCost = calculateCost(tmp, data);
		int[] lepsze = new int[data.getSize()];
		long t= System.currentTimeMillis();
		long end = t+time*1000;
		boolean flag = false;
		try {
			while(System.currentTimeMillis() < end) {
				steps++;
				for(int i = 0;i<data.getSize();i++){
					for(int j = 0;j<data.getSize();j++){
						checked++;
						int[] tab = swap(tmp,i,j);
						double calculated = calculateCost(tab, data);
						if(calculated<minCost){
							minCost = calculated;
							lepsze = Arrays.copyOf(tab, tab.length);
							flag = true;
						}
					}
				}
				if(compareArrays(tmp,lepsze)||!flag){
					break;
				}
				if(!compareArrays(tmp, lepsze)&&flag){
					tmp = Arrays.copyOf(lepsze, lepsze.length);
					lepsze = new int[data.getSize()];
					flag = false;
				}
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object[] tab = {minCost,steps,checked};
		
		return tab;
		
	}

	private static Object[] algorithmLSG(int time, Data data){
		
		double minCost = Double.MAX_VALUE;
		
		int steps = 0;
		int checked = 0;
		
		int[] tmp = randomPermutation(data.getSize());
		minCost = calculateCost(tmp, data);
		int[] lepsze = new int[data.getSize()];
		long t= System.currentTimeMillis();
		long end = t+time*1000;
		boolean flag = false;
		boolean breakerFlag = false;
		try {
			while(System.currentTimeMillis() < end) {
				steps++;
				for(int i = 0;i<data.getSize();i++){
					for(int j = 0;j<data.getSize();j++){
						checked++;
						int[] tab = swap(tmp,i,j);
						double calculated = calculateCost(tab, data);
						if(calculated<minCost){
							minCost = calculated;
							lepsze = Arrays.copyOf(tab, tab.length);
							flag = true;
							breakerFlag = true;
							break;
						}
						if(breakerFlag){
							break;
						}
					}
				}
				if(compareArrays(tmp,lepsze)||!flag){
					break;
				}
				if(!compareArrays(tmp, lepsze)&&flag){
					tmp = Arrays.copyOf(lepsze, lepsze.length);
					lepsze = new int[data.getSize()];
					flag = false;
				}
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Object[] tab = {minCost,steps,checked};
		
		return tab;
		
	}
	
	private static double[] doSteepest(int time, Data data){
		long start = System.nanoTime();
		double minValue = Double.MAX_VALUE;
		int sumSteps = 0;
		int sumChecked = 0;
		double avgSteps = 0;
		double avgChecked = 0;
		
		for(int i = 0;i<10;i++){
			Object[] tmp = algorithmLSS(time, data);
			if((double)tmp[0]<minValue){
				minValue = (double)tmp[0];
			}
			sumSteps += (int)tmp[1];
			sumChecked += (int)tmp[2];
		}
		
		avgSteps = sumSteps/10;
		avgChecked = sumChecked/10;
		
		long elapsedTime = System.nanoTime()-start;
		nanoS = elapsedTime;
		
		double[] tab = {minValue,avgSteps,avgChecked};
		
		return tab;
	}
	
	private static double[] doGreedy(int time, Data data){
		long start = System.nanoTime();
		double minValue = Double.MAX_VALUE;
		int sumSteps = 0;
		int sumChecked = 0;
		double avgSteps = 0;
		double avgChecked = 0;
		
		for(int i = 0;i<10;i++){
			Object[] tmp = algorithmLSG(time, data);
			if((double)tmp[0]<minValue){
				minValue = (double)tmp[0];
			}
			sumSteps += (int)tmp[1];
			sumChecked += (int)tmp[2];
		}
		
		avgSteps = sumSteps/10;
		avgChecked = sumChecked/10;
		
		long elapsedTime = System.nanoTime()-start;
		nanoG = elapsedTime;
		
		double[] tab = {minValue,avgSteps,avgChecked};
		
		return tab;
	}
	
	private static double doRandom(int Time, Data data){
		double minValue = Double.MAX_VALUE;
		
		for(int i = 0;i<10;i++){
			double tmp = algorithmR(time, data);
			if(tmp<minValue){
				minValue = tmp;
			}
		}	
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
			fw = new FileWriter("output_"+name+".csv");
			fw.write("LP\tFile\tSteepest\tSteepestTime\tSSteps\tSChecked\tGreedy\tGreedyTime\tGSteps\tGChecked\tRandom\t");
			System.out.println("---Start---");
			for(int j = 0;j<10;j++){
				if(path.indexOf('/')>0){
					tmp = "\n"+(j+1)+"\t"+path.substring(path.lastIndexOf('/')+1, path.length())+"\t";
				}else{
					tmp = "\n"+(j+1)+"\t"+path+"\t";
				}
				data = new Data(path);
				double[] steepest = doSteepest(time, data);
				
				tmp+=steepest[0]+"\t"+nanoS+"\t"+steepest[1]+"\t"+steepest[2]+"\t";
				
				data = new Data(path);
				double greedy[] = doGreedy(time, data);

				tmp+=greedy[0]+"\t"+nanoG+"\t"+greedy[1]+"\t"+greedy[2]+"\t";

				data = new Data(path);
				int avgTime = (int)(nanoS+nanoG)/2;
				double random = doRandom(avgTime, data);

				tmp+=random+"\t";
				
				fw.write(tmp);
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("---END---");
	}
	
	private static boolean compareArrays(int[] a1, int[] a2) {
        boolean b = true;
        if (a1 != null && a2 != null){
          if (a1.length != a2.length)
              b = false;
          else
              for (int i = 0; i < a2.length; i++) {
                  if (a2[i] != a1[i]) {
                      b = false;    
                  }                 
            }
        }else{
          b = false;
        }
        return b;
    }
	
	public static void main(String[] args) {
		
		time = 60;
		String path = args[0];

		performTests(time, path);
	    
	}
}
