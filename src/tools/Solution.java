package tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Solution {
	
	private static boolean random = false;
	private static boolean steepest = false;
	private static boolean greedy = false;
	
	private static Data data;
	
	private static int time;
	
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
	
	private static double algorithmLSR(int time, Data data){
		
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
				tmp = lepsze;
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
				tmp = lepsze;
				Thread.sleep(10);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return minCost;
		
	}
	
	private static void checkParameters(String[] args){
		if(args.length>0 && !args[0].equals("")){
			switch(args[0]){
			case "-a":
				random = true;
				steepest = true;
				greedy = true;
				break;
			case "-r":
				random = true;
				break;
			case "-s":
				steepest = true;
				break;
			case "-g":
				greedy = true;
				break;
			default:
				System.out.println("Wrong argument 1!\nChoose:\n- '-a' for all algorithms\n- '-r' for random"
						+ "\n- '-s' for steepest\n- '-g' for greedy");
				return;
			}
		}else{
			System.out.println("Missing argument 1!\nChoose:\n- '-a' for all algorithms\n- '-r' for random"
					+ "\n- '-s' for steepest\n- '-g' for greedy");
			return;
		}
		if(args.length>1&&!args[1].equals("")){
			File f = new File(args[1]);
			if(f.exists() && !f.isDirectory()) { 
			    data = new Data(args[1]);
			}else{
				System.out.println("Wrong argument 2!\nFile doesn't exist!");
				random = false;
				steepest = false;
				greedy = false;
				return;
			}
		}else{
			System.out.println("Missing argument 2!\nSpecify path to data file.");
			random = false;
			steepest = false;
			greedy = false;
			return;
		}
		
		if(args.length>2&&!args[2].equals("")&&isValidSeconds(args[2])){
			time = Integer.parseInt(args[2]);
		}else{
			System.out.println("Missing or wrong argument 3!\nSpecify time limit in seconds.\n"
					+ "Time limit must be greater than zero");
			random = false;
			steepest = false;
			greedy = false;
			return;
		}
		
	}
	
	private static boolean isValidSeconds( String input ) {
	    try {
	        int i = Integer.parseInt( input );
	        if(i>0){
	        	return true;
	        }else{
	        	return false;
	        }
	    }
	    catch( Exception e ) {
	        return false;
	    }
	}
	
	public static void main(String[] args) {
	
		checkParameters(args);
		
		if(random){
			System.out.println("---WORKING---");
			System.out.println("Random: "+algorithmLSR(time, data));
		}
		
		if(steepest){
			System.out.println("---WORKING---");
			System.out.println("Steepest: "+algorithmLSS(time,data));
		}
		
		if(greedy){
			System.out.println("---WORKING---");
			System.out.println("Greedy: "+algorithmLSG(time,data));
		}
		
		System.out.println("---END---");
	}
}
