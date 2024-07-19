public abstract class Player {
    String name;
    Kingdom kingdom;

    Player(String name){
        this.name = name;
        kingdom = new Kingdom(this);
    }

}
