package co.edu.icesi.pollafutbolera.model;

import java.io.Serializable;

import java.util.Objects;

public class UserScoresPollaId implements Serializable {
    private Long user;
    private Long polla;

    public UserScoresPollaId() {}

    public UserScoresPollaId(Long user, Long polla) {
        this.user = user;
        this.polla = polla;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserScoresPollaId that = (UserScoresPollaId) o;
        return Objects.equals(user, that.user) && Objects.equals(polla, that.polla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, polla);
    }
}