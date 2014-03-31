import java.util.Scanner;
import java.util.Arrays;

public class PlayerSkeleton {
	static int INF = 2000000000;
	
	double lineWeight =3.4181268101392694;
	double heightWeight= 0;
	double holeWeight=-7.899265427351652;
	double blockageWeight=0;
	double bumpinessWeight=0;
	double wellWeight=-3.3855972247263626;
	double columnTransitionWeight = -9.348695305445199;
	double rowTransitionWeight = -3.2178882868487753;
	double heightLandingWeight = -4.500158825082766;
	
	double lineExponent = 1.1;
	double heightExponent = 1.1;
	double holeExponent = 1.1;
	double blockageExponent = 1.1;
	double bumpinessExponent = 1.1;
	double wellExponent=1.1;
	double columnTransitionExponent = 1.1;
	double rowTransitionExponent = 1.1;
	double heightLandingExponent = 1.1;
	
	//ninth factor of priority, landingHeight
	private static int getLandingHeight(int[][] field){
		int maxheight=-1;
		int minheight=25;
		for(int i=20;i>=0;i--){
			for(int j=0;j<10;j++){
				if(INF==field[i][j]){
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
		for(int i=0;i<21;i++){
			for(int j=0;j<10;j++){
				if(field[i][j]!=0){
					if(max<i){
						max=i;
					}
				}
			}
		}
		return max+1;
	}
	
	//priority, set the weight here
	private double getPriority(int[][] field){
		
		double line = getLinesFormed(field);
		double height = getResultantHeight(field);
		double hole = getNumOfHoles(field);
		double blockage = getNumOfBlockages(field);
		double bumpiness = getBumpinessIndex(field);
		double well = getNumOfWells(field);
		double rowTransition = getRowTransitions(field);
		double columnTransition = getColumnTransitions(field);
		double heightLanding = getLandingHeight(field);
		
		/*
		line = Math.pow(lineExponent,line);
		height = Math.pow(heightExponent, height);
		hole = Math.pow(holeExponent, hole);
		blockage = Math.pow(blockageExponent,blockage);
		bumpiness = Math.pow(bumpinessExponent,bumpiness);
		well = Math.pow(wellExponent,well);
		rowTransition = Math.pow(rowTransitionExponent,rowTransition);
		columnTransition = Math.pow(columnTransitionExponent,columnTransition);
		heightLanding = Math.pow(heightLandingExponent,heightLanding);
		*/
		
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
	
	private static boolean topIsClear(int index, int height, int width,int[][] field){
		for(int i=height;i<21;i++){
			for(int k=index;k<index+width;k++){
				if(field[i][k]!=0){
					return false;
				}
			}
		}
		return true;
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
	
	
	//VERY LONG METHOD, WARNING
	private static int[][] exeMove(int[][] field,int orient,int index, int type){
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

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int[] storeIndex = new int[100];
		double[] priority = new double[100];
		int counter=0;
		
		int maxHorz=20;
		int maxVert=19;
		int currHeight = 0;
		int nextPiece = s.nextPiece;
		int[][] field = new int[21][10];
		field = s.getField();
		int[][] copyField = copyArray(field);
		int[][] newField = copyArray(field);
		
		
		if(nextPiece==0){
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,0,j,0);
				storeIndex[counter]=j;
				priority[counter]=getPriority(newField);
				counter++;
			}
		}
		else if(nextPiece==1){
				for(int j=0;j<=9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,1);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					counter++;
				}
				for(int j=0;j<=5;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,1);
					storeIndex[counter]=10+j;
					priority[counter]=getPriority(newField);
					counter++;
				}
		}
		else if(nextPiece==2){
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,0,j,2);
				storeIndex[counter]=j;
				priority[counter]=getPriority(newField);
				counter++;
			}
		
			for(int j=0;j<8;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,1,j,2);
				storeIndex[counter]=j+9;
				priority[counter]=getPriority(newField);
				counter++;
			}
		
			for(int j=0;j<9;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,2,j,2);
				storeIndex[counter]=j+17;
				priority[counter]=getPriority(newField);
				counter++;
			}
		
			for(int j=0;j<8;j++){
				newField = copyArray(copyField);
				newField = exeMove(newField,3,j,2);
				storeIndex[counter]=j+26;
				priority[counter]=getPriority(newField);
				counter++;
			}
			
		}
		else if(nextPiece==3){
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,3);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,3);
					storeIndex[counter]=j+9;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,2,j,3);
					storeIndex[counter]=j+17;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,3,j,3);
					storeIndex[counter]=j+26;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
		}
		else if(nextPiece==4){
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,4);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,4);
					storeIndex[counter]=j+9;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,2,j,4);
					storeIndex[counter]=j+17;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,3,j,4);
					storeIndex[counter]=j+26;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
		}
		else if(nextPiece==5){
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,5);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,5);
					storeIndex[counter]=j+8;
					priority[counter]=getPriority(newField);
					counter++;
				}

		}
		else if(nextPiece==6){
				for(int j=0;j<8;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,0,j,6);
					storeIndex[counter]=j;
					priority[counter]=getPriority(newField);
					counter++;
				}
			
		
				for(int j=0;j<9;j++){
					newField = copyArray(copyField);
					newField = exeMove(newField,1,j,6);
					storeIndex[counter]=j+8;
					priority[counter]=getPriority(newField);
					counter++;
				}
		}	
		double maximum=-Double.MAX_VALUE;
		int indexRes=-1;
		for(int i=0;i<counter;i++){
			if(priority[i]>maximum){
				maximum=priority[i];
				indexRes=i;
			}
		}
		if(indexRes>-1){
			return storeIndex[indexRes];
		}
		
		/*
		for(int i=0;i<legalMoves.length;i++){
			System.out.print("legalMoves[" + i + "] : ");
			System.out.println(Arrays.toString(legalMoves[i]));
		}*/
		
		
		Scanner cin = new Scanner(System.in);
		int test = cin.nextInt();
		return test;
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
		}
	}
	
	public static void main(String[] args) {
		
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		p.setWeight();//comment this part to disable setting weight
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
