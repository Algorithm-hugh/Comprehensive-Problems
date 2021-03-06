/**
 * Created by Hugh on 2015/1/30 0030.
 * 内容：小猫钓鱼Player类
 * 描述：实现玩小猫钓鱼的人的相关操作，主要使用队列
 * 备注：java包中，Queue为接口，LinkedList实现了Queue接口，使用形如：
 *      Queue<String> queue = new LinkedList<String>();
 *      的方法来获得一个队列，queue只能使用Queue接口中声明的方法。
 */

import java.util.LinkedList;
import java.util.Queue;

public class Player {
    private Queue<String> pokers;
    private int pokerAmount;
    private String name;

    public int getPokerAmount() {
        return pokerAmount;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    public Player() {}

    //小猫钓鱼游戏中，得到初始牌
    public void getInitialPokers(Queue<String> initialCards){
        pokers = initialCards;
        pokerAmount = pokers.size();
    }
    //小猫钓鱼游戏中，出一张牌
    public String playACard(){
        String playedCard = pokers.poll();
        if (playedCard != null){
            pokerAmount--;
        }
        return playedCard;
    }

    //小猫钓鱼游戏中赢得卡片，将赢得的卡片加入己方牌堆的队尾，改变卡片数量
    public void winCards(Queue<String> winningCards){
        int winningCardsAmount = winningCards.size();
        for (int i = 0; i < winningCardsAmount; i++) {
            pokers.add(winningCards.poll());
        }
        pokerAmount +=winningCardsAmount;
    }

    public static void main(String[] args){
        //初始牌堆队列
        Queue<String> queue = new LinkedList<String>();
        queue.add("梅花A");     //queue.add(new String('a'));    这应该是完整的写法
        queue.add("梅花2");
        queue.add("梅花3");
        queue.add("梅花4");
        //赢牌队列
        Queue<String> winningCards = new LinkedList<String>();
        winningCards.add("黑桃J");
        winningCards.add("黑桃Q");
        winningCards.add("黑桃K");
        //实例化Member，设置名字，得到初始牌，赢得卡片
        Player player = new Player();
        player.setName("Hugh");
        player.getInitialPokers(queue);
        player.winCards(winningCards);
        //member出完手中所有牌
        while (true){
            String card = player.playACard();
            if (card == null){
                break;
            }

            System.out.println(card);
            System.out.println(player.getName()+"还剩 " + player.getPokerAmount() + " 张牌");
        }
    }
}


