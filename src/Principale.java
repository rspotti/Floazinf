import java.util.Scanner;



public class Principale {

	public static int binToDec(String binval) {
		int val=0;
		for(int i=0;i<binval.length();i++) {
			val=2*val+binval.charAt(i)-'0';
		}
	
		return val;
	}
	public static double binToFloat(String binval) {
		double val=0;
		for(int i=binval.length()-1;i>=0;i--) {
			val=(val+binval.charAt(i)-'0')/2;
		}
		return val;
	}
	public static double ieeeTofloat(String ieee,boolean visual,int lim) {
		int sgn=(ieee.charAt(0)=='0')?1:-1;		
	    int exp=binToDec(ieee.substring(1,9));
	    if (exp!=0) exp=exp-127;
	    int intval;
	    String intBin;
	    if (exp>0) {
	    	intBin="1"+ieee.substring(9,9+exp);
	    } else if (exp==0) {
	    	intBin="1";
	    } else {
	    	intBin="0";
	    }
	    intval=binToDec(intBin);
	    String floatbin=ieee.substring(9+intBin.length()-1);
	    if (exp<0) {
	    	floatbin=zeroes(-exp-1)+"1"+floatbin;
	    }
	    
	    double floatval=binToFloat(floatbin); 
	    		                 
	    if (visual) System.out.println("Sgn: "+sgn);
	    if (visual) System.out.println("Exp: "+ieee.substring(1,9)+" "+exp+" ("+(exp+127)+")");
	    if (visual) System.out.println("INT: "+intBin+" "+intval);
	    if (visual) {
	    	if (0==lim) {
	    		System.out.println("DEC: "+floatbin+" "+floatval);
	    	} else {
	    		System.out.println("DEC: "+floatbin.substring(0,lim)+" "+floatval);
	    	}
	    }
        
		return sgn*intval+floatval;
	}
	public static String zeroes(int n) {
		String zero="";
		for (int i=0;i<n;i++) {
			zero=zero+"0";
		}
		return zero;
	}
	
	public static String decToBin(int val) {
		String binVal="";
		while (val>0) {
			binVal=(val % 2)+binVal;
			val=val/2;
		}
		return (binVal.length()==0)? "0": binVal;
	}
	
	public static String decToBin(int val,int digit) {
		String binVal=decToBin(val);
		binVal=zeroes(digit-binVal.length())+binVal;
		return binVal;
	}
	public static String floatToBin(double val) {
		String binVal="";
		double intVal;
		
		for (int i=0; i<32 && val>0.0; i++) {
			val*=2;
		    intVal=Math.floor(val);
		    
			binVal=binVal+(int)intVal;
			val-=intVal;
		}
		return (binVal.length()==0)? "0": binVal;
	}
	
	public static void main(String[] args) {
		String expon="";
		String mantx="";
		String signz="";
		Scanner sc=new Scanner(System.in);
		String inval;
		double outval;
		double error;
		
		inval =sc.nextLine();
		boolean neg=(inval.charAt(0)=='-');
		if (neg) inval=inval.substring(1);
		
		String [] iv=inval.split(",");
		iv[1]="0."+iv[1];
	
	    double mantissa=Double.parseDouble(iv[1]);
		int intval = Integer.parseInt(iv[0]);
		String intBin=decToBin(intval);
		String floBin=floatToBin(mantissa);
		
	//	System.out.println(((neg)?"1":"0")+" "+intBin+" "+floBin);
		
		int exp=0;
		if (intBin.charAt(0) == '1') {
			exp=intBin.length()-1;
		} else {
			int posUno=floBin.indexOf("1");
			exp = -1-posUno;
			floBin=floBin.substring(-exp);
		}
		intBin=intBin.substring(1);
		
		String totBin= intBin+floBin;
	  //   	System.out.println("ROW: "+totBin);
		totBin=totBin+zeroes(23-totBin.length());
		//System.out.println("Exponent: "+exp+" "+ decToBin(exp+127,8));
		
		String ieee=((neg)?"1":"0")+decToBin(exp+127,8)+totBin.substring(0,23);
		String tronky=((neg)?"1":"0")+decToBin(exp+127,8)+totBin.substring(0,15)+"00000000";
		if (inval.equals("0,0")) {
			ieee="00000000000000000000000000000000";
			tronky="00000000000000000000000000000000";
		}
		
		System.out.print("IEEE  : "+ieee +"  ");
		System.out.println(ieeeTofloat(ieee,false,0));
		System.out.println();
		ieeeTofloat(ieee,true,0);
		System.out.println();
		System.out.println("Tronky: "+tronky+"  "+ieeeTofloat(tronky,false,0));
		System.out.println("        ------------------------");
		System.out.println();
		ieeeTofloat(tronky,true,10);
	
		}
}
