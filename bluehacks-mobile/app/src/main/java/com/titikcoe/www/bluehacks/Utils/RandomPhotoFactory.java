package com.titikcoe.www.bluehacks.Utils;

import com.titikcoe.www.bluehacks.R;

import java.util.Random;

/**
 * Created by Adrian Mark Perea on 28/01/2018.
 */

public class RandomPhotoFactory {

    public static int getRandomPhoto() {
        Random rand = new Random();
        int num = rand.nextInt(10);
        switch (num) {
            case (0):
                return R.drawable.zero;
            case (1):
                return R.drawable.one;
            case (2):
                return R.drawable.two;
            case (3):
                return R.drawable.three;
            case (4):
                return R.drawable.four;
            case (5):
                return R.drawable.five;
            case (6):
                return R.drawable.six;
            case (7):
                return R.drawable.seven;
            case (8):
                return R.drawable.eight;
            case (9):
                return R.drawable.nine;
        }
        return -1;
    }
}
