package co.edu.icesi.pollafutbolera.model;

import java.io.Serializable;
import java.util.Objects;

public class UserSubpollaId implements Serializable {
    private Long user;
    private Long subpolla;

    public UserSubpollaId() {}

    public UserSubpollaId(Long user, Long subpolla) {
        this.user = user;
        this.subpolla = subpolla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSubpollaId)) return false;
        UserSubpollaId that = (UserSubpollaId) o;
        return Objects.equals(user, that.user) && Objects.equals(subpolla, that.subpolla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, subpolla);
    }
}
