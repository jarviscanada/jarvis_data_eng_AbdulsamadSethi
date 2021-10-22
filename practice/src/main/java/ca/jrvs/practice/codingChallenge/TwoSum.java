package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;

public class TwoSum {

    //Time Complexity: O(n)
    public int[] findTwoSum(int [] num, int target) {
        Map<Integer, Integer> numMap = new HashMap<>();
        for (int i = 0; i < num.length; i++) {
            int complement = target - num[i];
            if(numMap.containsKey(complement)) {
                return new int { numMap.get(complement), i};
            } else {
                numMap.put(num[i], i);
            }
        }
        return new int[] {};
    }
}
