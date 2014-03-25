import java.util.Scanner;
import java.util.Arrays;

public class PlayerSkeleton {

	//implement this function to have a working system
	public int pickMove(State s, int[][] legalMoves) {
		int[][] field = new int[21][10];
		field = s.getField();
		for(int i=20;i>=0;i--){
			for(int j=0;j<10;j++){
				System.out.print(field[i][j]+" ");
			}
			System.out.println();
		}
		System.out.println("Next piece : " + s.nextPiece);
		
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
