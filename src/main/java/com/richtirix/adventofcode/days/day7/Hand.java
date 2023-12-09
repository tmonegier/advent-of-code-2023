package com.richtirix.adventofcode.days.day7;

import java.util.*;

public class Hand implements Comparable {
    private final Long bid;
    private final HandType handType;
    private final List<Integer> cardValues;
    private int numberOfJ=0;

    public Hand(String cards, Long bid, boolean isPart2) {
        this.bid = bid;
        cardValues = new ArrayList<>();
        Map<Integer, Integer> cardOccurrences = new HashMap<>();
        for(Character card : cards.toCharArray()){
            int cardValue = getCardValue(card, isPart2);
            cardValues.add(cardValue);
            if(cardValue != 0) {
                cardOccurrences.putIfAbsent(cardValue, 0);
                cardOccurrences.put(cardValue, cardOccurrences.get(cardValue)+1);
            } else {
                numberOfJ++;
            }
        }
        handType = getHandType(cardOccurrences, numberOfJ);
    }

    private static HandType getHandType(Map<Integer, Integer> cardOccurrences , int numberOfJ) {
        if(cardOccurrences.isEmpty()) {
            return HandType.FIVE_KIND;
        }
        var orderedCardOccurrences = cardOccurrences.values()
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();
        return switch (orderedCardOccurrences.get(0) + numberOfJ) {
            case 1 -> HandType.HIGH_CARD;
            case 2 -> {
                if (orderedCardOccurrences.get(1) == 2) {
                    yield HandType.TWO_PAIRS;
                }
                yield HandType.ONE_PAIR;
            }
            case 3 -> {
                if (orderedCardOccurrences.get(1) == 2) {
                    yield HandType.FULL_HOUSE;
                }
                yield HandType.THREE_KIND;
            }
            case 4 -> HandType.FOUR_KIND;
            case 5 -> HandType.FIVE_KIND;
            default ->
                    throw new IllegalStateException("Unexpected value: " + orderedCardOccurrences.get(0) + numberOfJ);
        };
    }

    private static int getCardValue(Character card, boolean isPart2) {
        if(Character.isDigit(card)) {
            return Character.digit(card, 10);
        }
        if(card == 'T') {
            return 10;
        }
        if(card == 'J') {
            if(isPart2) {
                return 0;
            }
            return 11;
        }
        if(card == 'Q') {
            return 12;
        }
        if(card == 'K') {
            return 13;
        }
        if(card == 'A') {
            return 14;
        }
        return 0;
    }

    public Long getBid() {
        return bid;
    }

    @Override
    public int compareTo(Object o) {
        if (this == o) return 0;
        if(o instanceof Hand otherHand) {
            if(otherHand.handType.equals(handType)) {
                for(int i = 0; i< cardValues.size(); i++) {
                    int compare = Integer.compare(cardValues.get(i), otherHand.cardValues.get(i));
                    if(compare != 0) {
                        return compare;
                    }
                }
            }
            return Integer.compare(handType.getRankValue(), otherHand.handType.getRankValue());
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        return compareTo(o) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(handType, cardValues);
    }
}