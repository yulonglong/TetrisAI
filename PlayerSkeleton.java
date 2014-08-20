//@author Steven Kester Yuwono
//PlayerSkeleton.java

import java.util.Scanner;
import java.util.Arrays;


public class PlayerSkeleton {

	static int INF1 = 2000000000;
	static int INF2 = 2100000000;
	final double EPS = 1e-9;
	int globalINF = INF1;

	double lineWeight = 3.781057045291721;
	double heightWeight = -0.000001;
	double holeWeight = -8.533854039893987;
	double blockageWeight = 0;
	double bumpinessWeight = 0;
	double wellWeight = -7.053276659088376;
	double columnTransitionWeight = -7.708336085296365;
	double rowTransitionWeight = -2.5799680732117745;
	double heightLandingWeight = -3.136943472726582;
	double lookAheadWeight = 1;
	
	double lineExponent = 9.763232628810648;
	double heightExponent = 0;
	double holeExponent = 1.6993720935711176;
	double blockageExponent = 0;
	double bumpinessExponent = 0;
	double wellExponent = 5.333692367852307;
	double columnTransitionExponent = 1.1603835054247467;
	double rowTransitionExponent = 1.0796379646719207;
	double heightLandingExponent = 1.3696465899099395;
	
	//ninth factor of priority, landingHeight
	private static int getLandingHeight(int[][] field,int customINF){
		int maxheight=-1;
		int minheight=25;
		for(int i=20;i>=0;i--){
			for(int j=0;j<10;j++){
				if(customINF==field[i][j]){
					if(maxheight<i){
						maxheight=i;
					}
					if(minheight>i){
						minheight=i;
					}
				}
			}
		}
		maxheight++;
		minheight++;
		return ((maxheight+minheight)/2);
	}
	
	//eightth factor of priority, rowTransitions
	private static int getRowTransitions(int[][] field){
		int counter=0;
		int curr=0;
		int prev=1;
		for(int i=0;i<21;i++){
			prev=1;
			for(int j=0;j<10;j++){
				curr=field[i][j];
				if(curr>1){
					curr=1;
				}
				if(curr!=prev){
					counter++;
				}
				prev=curr;
			}
			if(curr==0){
				counter++;
			}
		}
		return counter;
	}
	
	//seventh factor of priority, columnTransitions
	private static int getColumnTransitions(int[][] field){
		int counter=0;
		int curr=0;
		int prev=1;
		for(int j=0;j<10;j++){
			prev=1;
			for(int i=20;i>=0;i--){
				curr=field[i][j];
				if(curr>1){
					curr=1;
				}
				if(curr!=prev){
					counter++;
				}
				prev=curr;
			}
		}
		return counter;
	}
		
	
	//sixth factor of priority, num of wells
	private static int getNumOfWells(int[][] field){
		int counter=0;
		for(int i=0;i<19;i++){
			
			//check most left
			if((field[i][0]==0)&&(field[i+1][0]==0)&&(field[i+2][0]==0)){
				if((field[i][1]!=0)&&(field[i+1][1]!=0)&&(field[i+2][1]!=0)){
					counter++;
				}
			}
			
			for(int j=1;j<9;j++){
				//if center is empty
				if((field[i][j]==0)&&(field[i+1][j]==0)&&(field[i+2][j]==0)){
					//if left occupied
					if((field[i][j-1]!=0)&&(field[i+1][j-1]!=0)&&(field[i+2][j-1]!=0)){
						//if right occupied
						if((field[i][j+1]!=0)&&(field[i+1][j+1]!=0)&&(field[i+2][j+1]!=0)){
							counter++;
						}
					}
				}
			}
			
			//check most right
			if((field[i][9]==0)&&(field[i+1][9]==0)&&(field[i+2][9]==0)){
				if((field[i][8]!=0)&&(field[i+1][8]!=0)&&(field[i+2][8]!=0)){
					counter++;
				}
			}
			
		}
		return counter;
	}
	
	//fifth factor of priority, index of Bumpiness
	private static int getBumpinessIndex(int[][] field){
		int[] height = new int[10];
		for(int i=0;i<10;i++){
			height[i]=0;
		}
		
		for(int j=0;j<10;j++){
			for(int i=20;i>=0;i--){
				if(field[i][j]!=0){
					height[j]=i+1;
					break;
				}
			}
		}
		int bumpiness = 0;
		for(int i=0;i<9;i++){
			bumpiness = bumpiness + Math.abs(height[i]-height[i+1]);
		}
		return bumpiness;
	}
	
	//fourth factor of priority,number of Blockages 
	private static int getNumOfBlockages(int[][] field){
		int counter=0;
		boolean valid=false;
		for(int j=0;j<10;j++){
			valid=false;
			for(int i=1;i<21;i++){
				if((field[i][j]!=0)&&(field[i-1][j]==0)){
					valid=true;
				}
				if((valid)&&(field[i][j]!=0)){
					counter++;
				}
			}
		}
		return counter;
	}
	
	//third factor of priority, number of holes
	private static int getNumOfHoles(int[][] field){
		int[][] tempField;
		tempField = copyArray(field);
		int counter=0;
		for(int i=20;i>=1;i--){
			for(int j=0;j<10;j++){
				if((tempField[i][j]!=0)&&(tempField[i-1][j]==0)){
					counter++;
					tempField[i][j]=1;
				}
			}
		}
		return counter;
	}
	
	//first factor of priority, Lines Formed
	private static int getLinesFormed(int[][] field){
		boolean valid=true;
		int counter=0;
		for(int i=0;i<21;i++){
			valid=true;
			for(int j=0;j<10;j++){
				if(field[i][j]==0){
					valid=false;
				}
			}
			if(valid){
				counter++;
			}
		}
		return counter;
	}
	
	//second factor of priority, resultant height
	private static int getResultantHeight(int[][] field){
		int max=0;
		for(int i=20;i>=0;i--){
			for(int j=0;j<10;j++){
				if(field[i][j]!=0){
					if(i==20){
						return (INF2);
					}
					return i+1;
				}
			}
		}
		return 0;
	}
	
	//priority, set the weight here
	private double getPriority(int[][] field){
		double line = 0;
		double height = 0;
		double hole = 0;
		double blockage = 0;
		double bumpiness = 0;
		double well = 0;
		double rowTransition = 0;
		double columnTransition = 0;
		double heightLanding = 0;
		
		if((lineWeight>EPS)||(lineWeight<-EPS)){
			line = getLinesFormed(field);
		}
		if((heightWeight>EPS)||(heightWeight<-EPS)){
			height = getResultantHeight(field);
		}
		if((holeWeight>EPS)||(holeWeight<-EPS)){
			hole = getNumOfHoles(field);
		}
		if((blockageWeight>EPS)||(blockageWeight<-EPS)){
			blockage = getNumOfBlockages(field);
		}
		if((bumpinessWeight>EPS)||(bumpinessWeight<-EPS)){
			bumpiness = getBumpinessIndex(field);
		}
		if((wellWeight>EPS)||(wellWeight<-EPS)){
			well = getNumOfWells(field);
		}
		if((rowTransitionWeight>EPS)||(rowTransitionWeight<-EPS)){
			rowTransition = getRowTransitions(field);
		}
		if((columnTransitionWeight>EPS)||(columnTransitionWeight<-EPS)){
			columnTransition = getColumnTransitions(field);
		}
		if((heightLandingWeight>EPS)||(heightLandingWeight<-EPS)){
			heightLanding = getLandingHeight(field,globalINF);
		}
		
		
		//line = Math.pow(lineExponent,line);
		//height = Math.pow(heightExponent, height);
		//hole = Math.pow(holeExponent, hole);
		//blockage = Math.pow(blockageExponent,blockage);
		//bumpiness = Math.pow(bumpinessExponent,bumpiness);
		//well = Math.pow(wellExponent,well);
		//rowTransition = Math.pow(rowTransitionExponent,rowTransition);
		//columnTransition = Math.pow(columnTransitionExponent,columnTransition);
		//heightLanding = Math.pow(heightLandingExponent,heightLanding);
		
		
		double score = lineWeight*line;
		score += heightWeight*height;
		score += holeWeight*hole;
		score += blockageWeight*blockage;
		score += bumpinessWeight*bumpiness;
		score += wellWeight*well;
		score += rowTransitionWeight*rowTransition;
		score += columnTransitionWeight*columnTransition;
		score += heightLandingWeight*heightLanding;
		
		return score;
	}
	
	private static int[][] copyArray(int[][] arr){
		int[][] newArr = new int[21][10];
		for(int i=0;i<21;i++){
			for(int j=0;j<10;j++){
				newArr[i][j]=arr[i][j];
			}
		}
		return newArr;
	}

	private static void copyArrayByReference(int[][] newArr,int[][] arr){
		for(int i=0;i<21;i++){
			for(int j=0;j<10;j++){
				newArr[i][j]=arr[i][j];
			}
		}
		return;
	}
	
	
	//VERY LONG METHOD, WARNING
	private static int[][] exeMove(int[][] field,int orient,int index, int type, int INF){
		//type 0
		if(type==0){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			return field;
		}
		//type 1 orient 0
		else if((type==1)&&(orient==0)){
			for(int i=16;i>=0;i--){
				if(field[i][index]==0){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+2][index]=INF;
					field[i+3][index]=INF;
					field[i+4][index]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[1][index]=INF;
			field[2][index]=INF;
			field[3][index]=INF;
			return field;
		}
		//type 1 orient 1
		else if((type==1)&&(orient==1)){
			for(int i=19;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)&&(field[i][index+2]==0)&&(field[i][index+3]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+1][index+2]=INF;
					field[i+1][index+3]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[0][index+2]=INF;
			field[0][index+3]=INF;
			return field;
		}
		//type 2 orient 0
		else if((type==2)&&(orient==0)){
			for(int i=17;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+2][index]=INF;
					field[i+3][index]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[1][index]=INF;
			field[2][index]=INF;
			return field;
		}
		//type 2 orient 1
		else if((type==2)&&(orient==1)){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i+1][index]==0)&&(field[i+1][index+1]==0)&&(field[i+1][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index+2]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			field[1][index+2]=INF;
			return field;
		}
		//type 2 orient 2
		else if((type==2)&&(orient==2)){
			for(int i=17;i>=0;i--){
				if((field[i+2][index]==0)&&(field[i+2][index+1]==0)&&(field[i+1][index+1]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+3][index]=INF;
					field[i+3][index+1]=INF;
					field[i+2][index+1]=INF;
					field[i+1][index+1]=INF;
					return field;
				}
			}
			field[2][index]=INF;
			field[2][index+1]=INF;
			field[1][index+1]=INF;
			field[0][index+1]=INF;
			return field;
		}
		//type 2 orient 3
		else if((type==2)&&(orient==3)){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)&&(field[i][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+1][index+2]=INF;
					field[i+2][index+2]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[0][index+2]=INF;
			field[1][index+2]=INF;
			return field;
		}
		//type 3 orient 0
		else if((type==3)&&(orient==0)){
			for(int i=17;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+2][index+1]=INF;
					field[i+3][index+1]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[1][index+1]=INF;
			field[2][index+1]=INF;
			return field;
		}
		//type 3 orient 1
		else if((type==3)&&(orient==1)){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)&&(field[i][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+1][index+2]=INF;
					field[i+2][index]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[0][index+2]=INF;
			field[1][index]=INF;
			return field;
		}
		//type 3 orient 2
		else if((type==3)&&(orient==2)){
			for(int i=17;i>=0;i--){
				if((field[i+2][index+1]==0)&&(field[i+2][index]==0)&&(field[i+1][index]==0)&&(field[i][index]==0)){
					continue;
				}
				else{
					field[i+3][index]=INF;
					field[i+3][index+1]=INF;
					field[i+2][index]=INF;
					field[i+1][index]=INF;
					return field;
				}
			}
			field[2][index]=INF;
			field[2][index+1]=INF;
			field[1][index]=INF;
			field[0][index]=INF;
			return field;
		}
		//type 3 orient 3
		else if((type==3)&&(orient==3)){
			for(int i=18;i>=0;i--){
				if((field[i][index+2]==0)&&(field[i+1][index]==0)&&(field[i+1][index+1]==0)&&(field[i+1][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index+2]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index+2]=INF;
					return field;
				}
			}
			field[0][index+2]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			field[1][index+2]=INF;
			return field;
		}
		//type 4 orient 0
		else if((type==4)&&(orient==0)){
			for(int i=17;i>=0;i--){
				if((field[i+1][index+1]==0)&&(field[i+2][index]==0)&&(field[i+1][index]==0)&&(field[i][index]==0)){
					continue;
				}
				else{
					field[i+3][index]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index]=INF;
					field[i+1][index]=INF;
					return field;
				}
			}
			field[2][index]=INF;
			field[1][index+1]=INF;
			field[1][index]=INF;
			field[0][index]=INF;
			return field;
		}
		//type 4 orient 1
		else if((type==4)&&(orient==1)){
			for(int i=18;i>=0;i--){
				if((field[i][index+1]==0)&&(field[i+1][index]==0)&&(field[i+1][index+1]==0)&&(field[i+1][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index+1]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index+2]=INF;
					return field;
				}
			}
			field[0][index+1]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			field[1][index+2]=INF;
			return field;
		}
		//type 4 orient 2
		else if((type==4)&&(orient==2)){
			for(int i=17;i>=0;i--){
				if((field[i+1][index]==0)&&(field[i+2][index+1]==0)&&(field[i+1][index+1]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+2][index]=INF;
					field[i+3][index+1]=INF;
					field[i+2][index+1]=INF;
					field[i+1][index+1]=INF;
					return field;
				}
			}
			field[1][index]=INF;
			field[2][index+1]=INF;
			field[1][index+1]=INF;
			field[0][index+1]=INF;
			return field;
		}
		//type 4 orient 3
		else if((type==4)&&(orient==3)){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)&&(field[i][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+1][index+2]=INF;
					field[i+2][index+1]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[0][index+2]=INF;
			field[1][index+1]=INF;
			return field;
		}
		//type 5 orient 0
		else if((type==5)&&(orient==0)){
			for(int i=18;i>=0;i--){
				if((field[i][index]==0)&&(field[i][index+1]==0)&&(field[i+1][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+1][index+1]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index+2]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[0][index+1]=INF;
			field[1][index+1]=INF;
			field[1][index+2]=INF;
			return field;
		}
		//type 5 orient 1
		else if((type==5)&&(orient==1)){
			for(int i=17;i>=0;i--){
				if((field[i+1][index]==0)&&(field[i+1][index+1]==0)&&(field[i][index+1]==0)){
					continue;
				}
				else{
					field[i+1][index+1]=INF;
					field[i+2][index+1]=INF;
					field[i+2][index]=INF;
					field[i+3][index]=INF;
					return field;
				}
			}
			field[0][index+1]=INF;
			field[1][index+1]=INF;
			field[1][index]=INF;
			field[2][index]=INF;
			return field;
		}
		//type 6 orient 0
		else if((type==6)&&(orient==0)){
			for(int i=18;i>=0;i--){
				if((field[i+1][index]==0)&&(field[i][index+1]==0)&&(field[i][index+2]==0)){
					continue;
				}
				else{
					field[i+1][index+1]=INF;
					field[i+1][index+2]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					return field;
				}
			}
			field[0][index+1]=INF;
			field[0][index+2]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			return field;
		}
		else if((type==6)&&(orient==1)){
			for(int i=17;i>=0;i--){
				if((field[i][index]==0)&&(field[i+1][index+1]==0)&&(field[i+1][index+1]==0)){
					continue;
				}
				else{
					field[i+1][index]=INF;
					field[i+2][index]=INF;
					field[i+2][index+1]=INF;
					field[i+3][index+1]=INF;
					return field;
				}
			}
			field[0][index]=INF;
			field[1][index]=INF;
			field[1][index+1]=INF;
			field[2][index+1]=INF;
			return field;
		}
		return field;
	}

	private int searchAllMoves(int[][] copyField, double[] priority, int[] storeIndex,int nextPiece,int customINF,int[][][] storeField,boolean mustStoreField){
		int counter=0;
		int[][] newField = new int[21][10];

		if(nextPiece==0){
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,0,j,0,customINF);
				storeIndex[counter]=j;
				priority[counter]=getPriority(newField);
				if(mustStoreField){
					storeField[counter]= newField;
				}
				counter++;
			}
		}
		else if(nextPiece==1){
				for(int j=0;j<=9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,1,customINF);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
				for(int j=0;j<=5;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,1,customINF);
					storeIndex[counter]=10+j;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
		}
		else if(nextPiece==2){
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,0,j,2,customINF);
				storeIndex[counter]=j;
				priority[counter]=getPriority(newField);
				if(mustStoreField){
					storeField[counter]= newField;
				}
				counter++;
			}
		
			for(int j=0;j<8;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,1,j,2,customINF);
				storeIndex[counter]=j+9;
				priority[counter]=getPriority(newField);
				if(mustStoreField){
					storeField[counter]= newField;
				}
				counter++;
			}
		
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,2,j,2,customINF);
				storeIndex[counter]=j+17;
				priority[counter]=getPriority(newField);
				if(mustStoreField){
					storeField[counter]= newField;
				}
				counter++;
			}
		
			for(int j=0;j<8;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,3,j,2,customINF);
				storeIndex[counter]=j+26;
				priority[counter]=getPriority(newField);
				if(mustStoreField){
					storeField[counter]= newField;
				}
				counter++;
			}
			
		}
		else if(nextPiece==3){
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,3,customINF);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					storeField[counter]= newField;
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,3,customINF);
					storeIndex[counter]=j+9;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,2,j,3,customINF);
					storeIndex[counter]=j+17;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,3,j,3,customINF);
					storeIndex[counter]=j+26;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
		}
		else if(nextPiece==4){
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,4,customINF);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,4,customINF);
					storeIndex[counter]=j+9;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,2,j,4,customINF);
					storeIndex[counter]=j+17;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,3,j,4,customINF);
					storeIndex[counter]=j+26;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
		}
		else if(nextPiece==5){
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,5,customINF);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,5,customINF);
					storeIndex[counter]=j+8;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}

		}
		else if(nextPiece==6){
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,6,customINF);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
			
		
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,6,customINF);
					storeIndex[counter]=j+8;
					priority[counter]=getPriority(newField);
					if(mustStoreField){
						storeField[counter]= newField;
					}
					counter++;
				}
		}	
		return counter;
	}


	private double searchAndGetMax(int[][] copyField,int nextPiece){
		int counter=0;
		int[] storeIndex = new int[100];
		double[] priority = new double[100];
		int[][][] storeField = new int [2000][21][10];

		//this is the massive search
		globalINF = INF2;
		counter = searchAllMoves(copyField,priority,storeIndex,nextPiece,INF2,storeField,false);

		double maximum=-Double.MAX_VALUE;
		int indexRes=-1;

		for(int i=0;i<counter;i++){
			if(priority[i]>maximum){
				maximum=priority[i];
				indexRes=i;
			}
		}
		return maximum;
	}


	private int searchAndGetMaxWithLookAhead(int[][] copyField,int nextPiece){
		int counter=0;
		int[] storeIndex = new int[100];
		double[] priority = new double[100];
		int[][][] storeField = new int [2000][21][10];

		//this is the massive search
		globalINF = INF1;
		counter = searchAllMoves(copyField,priority,storeIndex,nextPiece,INF1,storeField,true);
		
		
		double[] maximum = new double[3];
		int[] indexRes = new int[3];
		for(int i=0;i<3;i++){
			maximum[i] = -Double.MAX_VALUE;
			indexRes[i] = -1;
		}
		

		for(int i=0;i<counter;i++){
			if(priority[i]>maximum[0]){
				maximum[2]=maximum[1];
				indexRes[2]=indexRes[1];
				maximum[1]=maximum[0];
				indexRes[1]=indexRes[0];
				maximum[0]=priority[i];
				indexRes[0]=i;
			}
			else if(priority[i]>maximum[1]){
				maximum[2]=maximum[1];
				indexRes[2]=indexRes[1];
				maximum[1]=priority[i];
				indexRes[1]=i;
			}
			else if(priority[i]>maximum[2]){
				maximum[2]=priority[i];
				indexRes[2]=i;
			}
		}
		
		for(int i=0;i<3;i++){
				int index = indexRes[i];
				double minLookAheadScore=INF2;
				for(int j=0;j<=6;j++){
					double tempScore = searchAndGetMax(storeField[index],j);
					minLookAheadScore=Math.min(tempScore,minLookAheadScore);
				}
				maximum[i]+=(minLookAheadScore*lookAheadWeight);
		}
		
		double maxResult = -Double.MAX_VALUE;
		int maxResultIndex = -1;
		for(int i=0;i<3;i++){
			if(maximum[i]>maxResult){
				maxResult = maximum[i];
				maxResultIndex = indexRes[i];
			}
		}

		return storeIndex[maxResultIndex];

	}

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int nextPiece = s.nextPiece;
		int[][] field = new int[21][10];
		field = s.getField();
		int[][] copyField = copyArray(field);
		int bestMove = searchAndGetMaxWithLookAhead(copyField,nextPiece);
		return bestMove;
	}
	
	public void setWeight(){
		Scanner cin = new Scanner(System.in);
		
		System.out.println("Do you want to set the heuristics weight? (Y/N) : ");
		String choice = cin.next();
		if (choice.equalsIgnoreCase("Y")){
			System.out.println("Please Enter lineWeight : ");
			lineWeight = cin.nextDouble();
			System.out.println("Please Enter heightWeight : ");
			heightWeight= cin.nextDouble();
			System.out.println("Please Enter holeWeight : ");
			holeWeight=cin.nextDouble();
			System.out.println("Please Enter blockageWeight : ");
			blockageWeight=cin.nextDouble();
			System.out.println("Please Enter bumpinessWeight : ");
			bumpinessWeight=cin.nextDouble();
			System.out.println("Please Enter wellWeight : ");
			wellWeight=cin.nextDouble();
			System.out.println("Please Enter columnTransitionWeight : ");
			columnTransitionWeight = cin.nextDouble();
			System.out.println("Please Enter rowTransitionWeight : ");
			rowTransitionWeight = cin.nextDouble();
			System.out.println("Please Enter heightLandingWeight : ");
			heightLandingWeight = cin.nextDouble();
			System.out.println("Please Enter LookAheadWeight : ");
			lookAheadWeight = cin.nextDouble();
		}
	}
	
	public static void main(String[] args) {
		
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		//uncomment this part to enable setting weight
		//p.setWeight();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
