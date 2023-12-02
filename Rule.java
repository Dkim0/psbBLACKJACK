
import java.util.ArrayList;

	public class Rule {

		// 카드 출력
		public int printCard(String who, ArrayList<Card> deck) {
			
			Rule rule = new Rule(); 
			int sum = 0; 
			for(int i = 0; i < deck.size(); i++) {
				
				if(i == 0){
					if(who.equals("dealer")) {
						System.out.print("딜러 카드 : ? ");	
					}else {
						System.out.print("게이머 카드 : (" + deck.get(0).getPattern() + ", " + deck.get(0).getNumber() + ") ");
					}
				}else {
					System.out.print("(" + deck.get(i).getPattern() + ", " + deck.get(i).getNumber() + ") ");
					if(who.equals("dealer")) {
						System.out.println();
					}
				}
				
				if(i == deck.size() - 1 && who.equals("gamer")) {
					sum = rule.getSum(deck);
					System.out.println("총합 : " + sum);
				}
			
			}
			
			return sum; 
			
		}
		
		// 카드 합 구하기
		public int getSum(ArrayList<Card> deck) {
		    int sum = 0;
		    int aceCount = 0; // A 카드 개수 세기

		    for (Card card : deck) {
		        String number = card.getNumber();
		        if (number.equals("A")) {
		            aceCount++;
		            sum += 11; // 우선 A를 11로 계산
		        } else if (number.equals("J") || number.equals("Q") || number.equals("K")) {
		            sum += 10;
		        } else {
		            sum += Integer.parseInt(number);
		        }
		    }

		    // A를 11로 계산했을 때 합이 21을 넘으면 A를 1로 처리
		    while (aceCount > 0 && sum > 21) {
		        sum -= 10;
		        aceCount--;
		    }

		    return sum;
		}
		
		// 버스트
		public boolean isBust(String who, int sum) {
			if(sum > 21) {
				System.out.println(who + " Bust (총합 : " + sum + ")");
				return true;
			}else {
				return false; 
			}
		}
		
		// 버스트
		public boolean isBust(int sum) {
			if(sum > 21) {
				return true;
			} else {
				return false; 
			}
		}
		
		// 블랙잭인지 체크, 게이머의 승리는 1, 딜러, 게이머 모두 블랙잭 시 0, 딜러의 블랙잭은 2를 반환
		public int checkifBlackjack (int dealerSum, int gamerSum) {
			
			int dealer = 21 - dealerSum;
			int gamer = 21 - gamerSum;
			boolean blackjack = false;
			int result = -1;
					
			if (dealerSum == 21 && gamerSum == 21) {
				System.out.println("It's a DRAW with both having Blackjack!");
				result = 0;
			} else if (dealerSum == 21 || gamerSum == 21) {
				blackjack = true;
			}
			if (blackjack == true && dealer == 0) {
				System.out.println("DEALER, BLACKJACK !");
				blackjack = false;
				result = 2;
			} else if (blackjack == true && gamer == 0) {
				System.out.println("Congratulations! BLACKJACK !");
				blackjack = false;
				result = 1;
			}
			return result;
		}
		
		// 누가 21이랑 가까운지 비교, 게이머의 승리는 0, 무승부는 1, 패배는 2를 반환
		public int whoWin(int dealerSum, int gamerSum) {
		    
			int result = -1;
		    int dealer = 21 - dealerSum;
		    int gamer = 21 - gamerSum;
		    String str = "Winner is ";
		    if (dealer > gamer) {
		        str += "GAMER";
		        result = 0;
		    } else if (dealer == gamer) {
		        str = "DRAW ";
		        result = 1;
		    } else {
		        str += "DEALER";
		        result = 2;
		    }
		    
		    System.out.println(str + "(딜러 총합 : " + dealerSum + ", 게이머 총합 : " + gamerSum + ")");
		    return result;
		}
		
		
}