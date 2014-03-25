import java.util.Scanner;
import java.util.Arrays;

public class PlayerSkeleton {
	
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

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int[] storeIndex = new int[3000];
		int[] height = new int[3000];
		int priority = 3;
		int counter=0;
		
		int maxHorz=20;
		int maxVert=19;
		int currHeight = 0;
		int nextPiece = s.nextPiece;
		int[][] field = new int[21][10];
		field = s.getField();
		for(int i=0;i<21;i++){
			for(int j=0;j<10;j++){
				if(field[i][j]!=0){
					currHeight = i;
				}
			}
		}
		
		if(nextPiece==0){
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,2,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==1){
			for(int i=0;i<18;i++){
				for(int j=0;j<9;j++){
					if((topIsClear(j,i,1,field))&&(field[i][j+1]!=0)){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
					else if((field[i][j]!=0)&&(topIsClear(j+1,i,1,field))){
						storeIndex[counter]=j+1;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==2){
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,2,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j]==0)&&(field[i+1][j]==0)&&(field[i+1][j+1]==0)&&(field[i+1][j+2]==0)&&(field[i][j+1]!=0)&&(field[i][j+2]!=0)){
						storeIndex[counter]=j+9;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i+2][j]==0)&&(field[i+2][j+1]==0)&&(field[i+1][j+1]==0)&&(field[i][j+1]==0)&&(field[i][j]!=0)&&(field[i+1][j]!=0)){
						storeIndex[counter]=j+17;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(field[i][j+2]==0)&&(topIsClear(j,i,3,field))){
						storeIndex[counter]=j+26;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==3){
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,2,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(field[i][j+2]==0)&&(topIsClear(j,i,3,field))){
						storeIndex[counter]=j+9;
						height[counter]=i;
						counter++;
					}
				}
			}
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i+1][j]==0)&&(field[i+2][j]==0)&&(field[i+2][j+1]==0)&&(field[i+1][j]!=0)&&(field[i+1][j+1]!=0)){
						storeIndex[counter]=j+17;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j+2]==0)&&(field[i+1][j+2]==0)&&(field[i+1][j+1]==0)&&(field[i+1][j]==0)&&(field[i][j]!=0)&&(field[i][j+1]!=0)){
						storeIndex[counter]=j+26;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==4){
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i+1][j]==0)&&(field[i+1][j+1]==0)&&(field[i+2][j]==0)&&(field[i][j+1]!=0)){
						storeIndex[counter]=j;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j+1]==0)&&(field[i+1][j]==0)&&(field[i+1][j+1]==0)&&(field[i+1][j+2]==0)&&(field[i][j]!=0)&&(field[i][j+2]!=0)){
						storeIndex[counter]=j+9;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j+1]==0)&&(field[i+1][j+1]==0)&&(field[i+1][j]==0)&&(field[i+2][j+1]==0)&&(field[i][j]!=0)){
						storeIndex[counter]=j+17;
						height[counter]=i-priority;
						counter++;
					}
				}
			}
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(field[i][j+2]==0)&&(topIsClear(j,i,3,field))){
						storeIndex[counter]=j+26;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==5){
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(field[i+1][j+1]==0)&&(field[i+1][j+2]==0)&&(field[i][j+2]!=0)){
						storeIndex[counter]=j;
						height[counter]=i-priority;
						counter++;
					}
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,3,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i+1][j]==0)&&(field[i+2][j]==0)&&(field[i+1][j+1]==0)&&(field[i][j+1]==0)&&(field[i][j]!=0)){
						storeIndex[counter]=j+8;
						height[counter]=i-priority;
						counter++;
					}
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,2,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
		else if(nextPiece==6){
			for(int i=0;i<maxHorz;i++){
				for(int j=0;j<8;j++){
					if((field[i+1][j]==0)&&(field[i+1][j+1]==0)&&(field[i][j+1]==0)&&(field[i][j+2]==0)&&(field[i][j]!=0)){
						storeIndex[counter]=j;
						height[counter]=i-priority;
						counter++;
					}
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(field[i][j+2]==0)&&(topIsClear(j,i,3,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
			for(int i=0;i<maxVert;i++){
				for(int j=0;j<9;j++){
					if((field[i][j]==0)&&(field[i+1][j]==0)&&(field[i+1][j+1]==0)&&(field[i+2][j+1]==0)&&(field[i][j+1]!=0)){
						storeIndex[counter]=j+8;
						height[counter]=i-priority;
						counter++;
					}
					if((field[i][j]==0)&&(field[i][j+1]==0)&&(topIsClear(j,i,2,field))){
						storeIndex[counter]=j;
						height[counter]=i;
						counter++;
					}
				}
			}
		}
			
		int minimum=2000;
		int indexRes=-1;
		for(int i=0;i<counter;i++){
			if(height[i]<minimum){
				minimum=height[i];
				indexRes=i;
			}
		}
		if(indexRes>-1){
			return storeIndex[indexRes];
		}
		
		
		for(int i=0;i<legalMoves.length;i++){
			System.out.print("legalMoves[" + i + "] : ");
			System.out.println(Arrays.toString(legalMoves[i]));
		}
		
		
		Scanner cin = new Scanner(System.in);
		int test = cin.nextInt();
		return test;
	}
	
	public static void main(String[] args) {
		State s = new State();
		new TFrame(s);
		PlayerSkeleton p = new PlayerSkeleton();
		while(!s.hasLost()) {
			s.makeMove(p.pickMove(s,s.legalMoves()));
			s.draw();
			s.drawNext(0,0);
			try {
				Thread.sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("You have completed "+s.getRowsCleared()+" rows.");
	}
	
}
