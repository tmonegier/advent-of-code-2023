package org.example.days.day7;

public enum HandType {
    HIGH_CARD(1),
    ONE_PAIR(2),
    TWO_PAIRS(3),
    THREE_KIND(4),
    FULL_HOUSE(5),
    FOUR_KIND(6),
    FIVE_KIND(7);

    private int rankValue;

    HandType(int rankValue) {
        this.rankValue = rankValue;
    }

    public int getRankValue() {
        return rankValue;
    }
}
