package com.bugwarsBackend.bugwars.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TickSummary {
    List<ActionSummary> summary;
    boolean lastSwarmStanding;

    public TickSummary(Object actionsTaken, boolean lastSwarmStanding) {
    }
}
