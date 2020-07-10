package io.biggestbombs.helioss.heliossdemocraticweather.Util.Vote;

import java.util.ArrayList;
import java.util.UUID;

public class HeliossVoteOption {

    public final String voteName;
    private final String name;
    private final ArrayList<UUID> votes = new ArrayList<>();

    public HeliossVoteOption(String voteName, String name) {
        this.voteName = voteName;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void vote(UUID uniqueId) {
        this.votes.add(uniqueId);
    }

    public boolean hasVoted(UUID uniqueId) {
        return this.votes.contains(uniqueId);
    }

    public long getVotes() {
        return this.votes.size();
    }
}
