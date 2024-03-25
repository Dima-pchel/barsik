package ru.kets.barsik.repo.pojo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class RouletteGame {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String channelId;
    private String description;
    private Integer shootsNumbers;
    private boolean active;

    public RouletteGame(String description, Integer shootsNumbers, String channelId) {
        this.description = description;
        this.shootsNumbers = shootsNumbers;
        this.channelId = channelId;
        this.active = true;
    }

    public RouletteGame() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getShootsNumbers() {
        return shootsNumbers;
    }

    public void setShootsNumbers(Integer shootsNumbers) {
        this.shootsNumbers = shootsNumbers;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return String.format("%s \nFail chance = 1/%s",getDescription(), getShootsNumbers());
    }
}
