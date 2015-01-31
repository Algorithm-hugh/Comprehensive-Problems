import java.util.*;

/**
 * Created by Hugh on 2015/1/30 0030.
 * 内容：小猫钓鱼JudgeTable类
 * 描述：实现包括裁判（初始发牌、判定）和桌子（放置牌，展示牌）的功能
 *      本来应该写在两个类中的，我认为我的桌子很高端，有自动麻将桌类
 *      似的功能 = =（其实就是偷懒）。
 * 备注：目前默认startAGame函数的参数只能为2
 */
public class JudgeTable {
    private int playerAmount;
    private Member[] members;
    private Stack<Character> pokerOnTheTable;

    public JudgeTable() {}

    public boolean startAGame(){
        System.out.print("How many player?(Only 2 now): ");
        Scanner scanner = new Scanner(System.in);

        //设置玩家
        while(!setPlayer(scanner.nextInt())){}
        scanner.close();

        //发牌
        giveInitalPokers();

        //出牌阶段

        return true;
    }
    //设置玩家
    //包括实例化和设置玩家昵称
    //playerAmount 玩家数
    //玩家数不符合要求时设置失败（原意针对平分一副牌对人数的限制）
    private boolean setPlayer(int playerAmount){
        if (playerAmount!=2){
            return false;
        }
//        if (playerAmount<=0||52%playerAmount!=0){
//            return false;
//        }
        this.playerAmount = playerAmount;
        members = new Member[playerAmount];
        for (int i = 0; i < playerAmount; i++) {
            members[i] = new Member();
        }

        setPlayerName();

        return true;
    }
    private void setPlayerName(){
        System.out.println("Set Players' name:");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < playerAmount; i++) {
            System.out.print("Player "+ (i+1) + ": ");
            members[i].setName(scanner.next());
        }
        scanner.close();
    }
    //发牌
    private void giveInitalPokers(){
        LinkedList<String> aDeckOfCards = initialADeckOfCards();
        Queue<String> pokerForPlayers[] = new Queue[playerAmount];
        for (int i = 0; i < playerAmount; i++) {
            pokerForPlayers[i] = new LinkedList<String>();
        }

        Random random = new Random();
        for (int i = 0; i < 52 / playerAmount; i++) {
            for (int j = 0; j < playerAmount; j++) {
                int index = random.nextInt(aDeckOfCards.size());    //between 0(inclusive) and size(exclusive)
                pokerForPlayers[j].add(aDeckOfCards.remove(index));
            }
        }

        for (int i = 0; i < playerAmount; i++) {
            members[i].getInitialPokers(pokerForPlayers[i]);
        }
    }

    private LinkedList<String> initialADeckOfCards(){
        LinkedList<String> aDeckOfCards = new LinkedList<String>();
        for (int i = 0; i < 13; i++) {
            String number;
            switch (i){
                case 0:
                    number = new String("A");
                    break;
                case 10:
                    number = new String("J");
                    break;
                case 11:
                    number = new String("Q");
                    break;
                case 12:
                    number = new String("K");
                    break;
                default:
                    number = new String(""+i);
            }

            String spade = new String("黑桃"+number);
            String heart = new String("红桃"+number);
            String diamond = new String("方片"+number);
            String club = new String("梅花"+number);

            aDeckOfCards.add(spade);
            aDeckOfCards.add(heart);
            aDeckOfCards.add(diamond);
            aDeckOfCards.add(club);
        }

        return aDeckOfCards;
    }

    private void playCards(){
        //TODO int player
    }

    public static void main(String[] args){
        JudgeTable judgeTable = new JudgeTable();
        judgeTable.startAGame();
    }
}
