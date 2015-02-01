import java.util.*;

/**
 * Created by Hugh on 2015/1/30 0030.
 * 内容：小猫钓鱼JudgeTable类
 * 描述：实现包括裁判（初始发牌、判定）和桌子（放置牌，展示牌）的功能
 *      本来应该写在两个类中的，我认为我的桌子很高端，有自动麻将桌类
 *      似的功能 = =（其实就是偷懒）。
 * 备注：目前默认startAGame函数的参数只能为2
 *      判断桌面上是否有相同面值牌的方法：设定一个boolean数组，对13
 *      个数值进行动态修改
 */
public class JudgeTable {
    private int playerAmount;
    private int stepAccount;
    private LinkedList<Player> players;
    private Stack<String> pokerOnTheTable;
    private boolean[] list;                     //用来记录桌面上当前 各数值 牌的有无

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
        playCards();

        System.out.println("最终获胜的玩家是："+players.getFirst().getName());
        System.out.println("出牌次数总计："+stepAccount);
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
        players = new LinkedList<Player>();
        for (int i = 0; i < playerAmount; i++) {
            Player player = new Player();
            players.add(player);
        }

        setPlayerName();

        return true;
    }
    private void setPlayerName(){
        System.out.println("Set Players' name:");
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < playerAmount; i++) {
            System.out.print("Player "+ (i+1) + ": ");
            players.get(i).setName(scanner.next());
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
            players.get(i).getInitialPokers(pokerForPlayers[i]);
        }
    }

    private LinkedList<String> initialADeckOfCards(){
        LinkedList<String> aDeckOfCards = new LinkedList<String>();
        for (int i = 1; i <= 13; i++) {
            String number;
            switch (i){
                case 1:
                    number = new String("A");
                    break;
                case 11:
                    number = new String("J");
                    break;
                case 12:
                    number = new String("Q");
                    break;
                case 13:
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
        int playerLeaved = playerAmount;
        stepAccount = 0;
        pokerOnTheTable = new Stack<String>();
        list = new boolean[16];             //1~13位用来记录当前桌上 各数字 牌的有无
        for (int i = 0; i < 16; i++) {      //初始化boolean数组
            list[i] = false;
        }

        System.out.println("出牌记录：");  //提示语
        while (playerLeaved>1){                                 //游戏直到场上只有一个玩家时结束
            for (int i = 0; i < playerLeaved; i++,stepAccount++) {            //玩家轮流出牌
                Player player = players.get(i);                 //该轮出牌的玩家
                String playedCard = player.playACard();         //记录该回合玩家出的牌

                System.out.println(playedCard+"\t"+player.getName());  //输出出的牌到屏幕上

                tableJudge(player, playedCard);                  //进行判决，决定是牌放到桌子上还是玩家赢得牌
                if (player.getPokerAmount()==0){
                    players.remove(i);
                    playerLeaved--;
                    i--;                    //如果该玩家退出了游戏，下一个出牌玩家序号应该为i而不是i+1
                }
                if (playerLeaved<=1){
                    break;
                }
            }
        }
    }

    private void tableJudge(Player player,String playedCard){
        int number = getNumberFromPokerString(playedCard);
        if (number==-1){
            System.out.println("出错啦~");
            System.exit(1);
        }
        if (list[number]){      //如果桌上已经有相同面值的牌了
            Queue<String> winningPokers = new LinkedList<String>();
            winningPokers.add(playedCard);
            while (true){
                String poker = pokerOnTheTable.pop();
                winningPokers.add(poker);
                list[getNumberFromPokerString(poker)]=false;
                if (getNumberFromPokerString(poker)==number){
                    break;
                }
            }

            System.out.println(player.getName()+"赢得"+winningPokers.size()+"张牌");
            player.winCards(winningPokers);
        }else {
            pokerOnTheTable.push(playedCard);
            list[number]=true;
        }
    }

    private int getNumberFromPokerString(String poker){
        if (poker.endsWith("A")){return 1;}
        if (poker.endsWith("2")){return 2;}
        if (poker.endsWith("3")){return 3;}
        if (poker.endsWith("4")){return 4;}
        if (poker.endsWith("5")){return 5;}
        if (poker.endsWith("6")){return 6;}
        if (poker.endsWith("7")){return 7;}
        if (poker.endsWith("8")){return 8;}
        if (poker.endsWith("9")){return 9;}
        if (poker.endsWith("10")){return 10;}
        if (poker.endsWith("J")){return 11;}
        if (poker.endsWith("Q")){return 12;}
        if (poker.endsWith("K")){return 13;}
        return -1;
    }

    public static void main(String[] args){
        JudgeTable judgeTable = new JudgeTable();
        judgeTable.startAGame();
    }
}
