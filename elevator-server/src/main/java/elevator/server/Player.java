package elevator.server;

class Player {

    final String email;
    String pseudo;

    Player(String email, String pseudo) {
        this.email = email;
        this.pseudo = pseudo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player email = (Player) o;

        return this.email.equals(email.email);

    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }

    @Override
    public String toString() {
        return email;
    }

}
