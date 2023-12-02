import java.util.*;

public class Gamer {
	public Card hit(Dealer dealer, ArrayList<Card> deck) {
		Card card = dealer.getCard(deck);
		return card;
	}
	
}
