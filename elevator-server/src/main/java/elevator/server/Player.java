package elevator.server;

class Player implements Comparable<Player> {

    final String email;
    final String pseudo;

    Player(String email, String pseudo) {
        this.email = email;
        this.pseudo = pseudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        return email.equals(player.email);
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return email;
    }

    @Override
    public int compareTo(Player player) {
        return email.compareTo(player.email);
    }

}
