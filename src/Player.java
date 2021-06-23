public class Player implements Comparable<Player> {
    public String Name;
    public int timeTaken;
    public int moves;

    Player(String name, int timeTaken, int moves) {
        this.Name = name;
        this.timeTaken = timeTaken;
        this.moves = moves;
    }


    @Override
    public int compareTo(Player player) {
        return this.moves-player.moves;
    }

    public String toString() {
        return ("Name: "+ this.Name + " Time taken: " + this.timeTaken + " Moves: " + this.moves );
    }
}
