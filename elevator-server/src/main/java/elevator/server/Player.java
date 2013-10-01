package elevator.server;

import elevator.server.security.Password;
import elevator.server.security.RandomPassword;

import java.util.Date;

class Player implements Comparable<Player> {

    final String email;
    final String pseudo;
    final Password password;

    Player(String email, String pseudo) {
        this.email = email;
        this.pseudo = pseudo;
        this.password = new RandomPassword();
    }

    Player(String email) {
        this.email = email;
        this.pseudo = null;
        this.password = null;
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
