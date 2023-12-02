// 멀티플레이 기능 추가
// 베팅 기능 추가
// 랭킹 관리 기능 추가

import java.util.ArrayList;
import java.util.HashMap;
import java.io.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BlackJack {
	
	//파일경로
    public static String filePath = "../../gamerINFO.txt";     
    public static String userId;
    public static String playerNum;
    private static double playerMoney = 1000; // 초기 플레이어의 금액
    public static HashMap<String,Integer> info = new HashMap<String,Integer>(); //파일내용 임시저장소
    
    public static void main(String[] args) throws IOException {

        System.out.println("유저 아이디를 입력하세요 : ");
        Scanner sc1 = new Scanner(System.in);             
        userId = sc1.nextLine().trim();

        while (true) {
        	if (playerMoney < 0) {
        		System.out.println("금액 소진으로 게임 오버 되었습니다.");
        		break;
        	}
            playGame();
            replaceScore();   //파일에 금액 업뎃
            System.out.println("계속 진행하시려면 아무 키나 입력하세요.\tQ를 입력해 게임을 종료하세요.");
            Scanner sc = new Scanner(System.in);
            String answer = sc.nextLine().trim().toLowerCase();
            if (answer.equals("q")) {
                System.out.println("게임이 종료되었습니다. 랭킹을 보기 원하면 R을 입력하세요.");
                Scanner sc2 = new Scanner(System.in);
                String answer2 = sc2.nextLine().trim().toLowerCase();
                if (answer2.equals("r")) {
                	showRanking();  //랭킹 보여주기
                	break;
                }
                break;
            }
        }
    }
    
    public static void showRanking() {
    	System.out.println("================유저 랭킹==================");
    	
    	List<Map.Entry<String, Integer>> entries = info.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toList());
    	
    	entries.sort(Entry.<String, Integer>comparingByValue().reversed());
        for (Map.Entry<String, Integer> entry : entries) {
            System.out.println("UserId: " + entry.getKey() + ", "
                    + "Money: " + entry.getValue());
        }
    	
    }
    
  //사용자 금액 업데이트
    public static void replaceScore() throws IOException {
    	
    	info.put(userId,(int)playerMoney);
//    	System.out.println(info.get(userId));
    	//파일을 삭제하고 다시 새로 씀
    	try {
    		File file = new File(filePath);
    		FileWriter writer = new FileWriter(file);
//    		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
    		for(String key : info.keySet()) {
    			writer.write(key + "," + info.get(key) + "\n");
    		}
    		writer.flush();
        	writer.close();
        	
    	} catch(IOException e) {
    		e.printStackTrace();
    	}
    }

    public static void playGame() throws IOException{
        Dealer dealer = new Dealer();
        Gamer gamer = new Gamer();
        Rule rule = new Rule();
        
        
        // 덱 생성
        ArrayList<Card> deck = dealer.setCardDeck();

        // 딜러와 게이머 카드 받을 배열 생성
        ArrayList<Card> dealerCard = new ArrayList<Card>();
        ArrayList<Card> gamerCard = new ArrayList<Card>();

        try{
        	File file = new File(filePath);
        	if(!file.exists()){ 
                file.createNewFile(); 
            }
        	
            FileReader fileReader = new FileReader(file);
            BufferedReader bufReader = new BufferedReader(fileReader);

            String line = "";
            while ((line = bufReader.readLine()) != null) {
            	String[] tmp1 = line.split(",");
//            	System.out.println(tmp1[0]);
//            	System.out.println(tmp1[1]);
            	String a = tmp1[0];
            	int b = Integer.parseInt(tmp1[1]);
            	info.put(a,b);
            }
            bufReader.close();
         }catch (FileNotFoundException e) {
            e.getStackTrace();
         }catch(IOException e){
            e.getStackTrace();
         }

        System.out.println("------------BLACKJACK------------");

        //기존 유저가 아니면
        if(info.get(userId)!=null) {
        	playerMoney = info.get(userId);
        } else {
        	//해시맵에만 일단 새로쓰기
        	info.put(userId, 1000);
        }
        
        System.out.println("현재 보유 금액: " + playerMoney);
        
        double betAmount = 0.0;
        boolean validBet = false;
        while (!validBet) {
            try {
                System.out.print("베팅할 금액을 입력하세요: ");
                Scanner sc = new Scanner(System.in);
                betAmount = sc.nextDouble();
                
                if (betAmount > playerMoney || betAmount <= 0) {
                    System.out.println("현재 금액 내에서 유효한 금액을 베팅하세요.");
                } else validBet = true;
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력하세요.");
            }
        }

        playerMoney -= betAmount;
        
        // 양쪽 다 카드를 받음
        System.out.println("카드를 나누겠습니다.");

        dealerCard.add(dealer.getCard(deck));
        dealerCard.add(dealer.getCard(deck));
        gamerCard.add(dealer.getCard(deck));
        gamerCard.add(dealer.getCard(deck));

        int dealerSum = rule.printCard("dealer", dealerCard);
        int gamerSum = rule.printCard("gamer", gamerCard);

        System.out.println("---------------------------------");
        
        // 블랙잭 여부 체크
        int isBlackJack = rule.checkifBlackjack(dealerSum, gamerSum);
        switch (isBlackJack) {
        	case (1):
        		System.out.println("블랙잭으로 베팅 금액의 2.5배를 얻었습니다.");
        		playerMoney += betAmount * 2.5;
        		System.out.println("현재 보유 금액: " + playerMoney);
        		return;
        	case (0): 
        		System.out.println("무승부로 베팅 금액을 그대로 가져갑니다.");
        		playerMoney += betAmount;
        		System.out.println("현재 보유 금액: " + playerMoney);
        	    return;
        	case (2):
        		System.out.println("딜러의 블랙잭으로 베팅 금액을 잃었습니다!");
        		System.out.println("현재 보유 금액: " + playerMoney);
        		return;
        }

        while (true) {
        	
            System.out.println("1.HIT 2.STAND");

            Scanner sc = new Scanner(System.in);
            
            int hitOrStand = 0;
            
            try {
            	hitOrStand = sc.nextInt();
            } catch(InputMismatchException e) {
            	System.out.println("1 또는 2를 입력해주세요.");
            	continue;
            }
            
            // hit 선택한 경우
            if (hitOrStand == 1) {
                Card card = gamer.hit(dealer, deck);
                gamerCard.add(card);
                gamerSum = rule.printCard("gamer", gamerCard);
                if (rule.isBust("Gamer", gamerSum)) {
                	System.out.println("버스트로 베팅 금액을 잃었습니다.");
                    System.out.println("현재 보유 금액: " + playerMoney);
                    break;
                }
            } else if (hitOrStand == 2) {
                // stand 선택한 경우
                dealerCard = dealer.dealerGetCard(dealerSum, deck, dealerCard);
                dealerSum = rule.getSum(dealerCard);
                if (rule.isBust("Dealer", dealerSum)) {
                	System.out.println("딜러의 버스트로 베팅 금액의 2배를 얻었습니다.");
                	playerMoney += betAmount * 2;
                    System.out.println("현재 보유 금액: " + playerMoney);
                    break;
                }
                // 딜러, 게이머 모두 버스트하지 않은 경우
                int whoWin = rule.whoWin(dealerSum, gamerSum);
                switch (whoWin) {
                case 0: 	// 승리
                	System.out.println("승리로 금액의 2배를 얻었습니다.");
                	playerMoney += betAmount * 2;
                    System.out.println("현재 보유 금액: " + playerMoney);
                    break;
                case 1:
                	System.out.println("무승부로 베팅 금액을 그대로 가져갑니다.");
                	playerMoney += betAmount;
                    System.out.println("현재 보유 금액: " + playerMoney);
                    break;
                case 2:
                	System.out.println("패배로 베팅 금액을 잃었습니다.");
                    System.out.println("현재 보유 금액: " + playerMoney);
                    break;
                }
                break;
            }
            else System.out.println("1 또는 2를 입력해주세요.");
        }
    }
}
