package com.damda.back.service.Impl;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CodeServiceImpl {


    public String codeCheckPublish(){
        /**
         * @TODO: 아래에서 코드를 발행하여 DB에 중복환인 후 아니면 다시 발행
         * */
        return null;
    }

    public String codePublish(){
        char[] characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();

        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        HashSet<Character> set = new HashSet<>();

        while (sb.length() < 6) {
            int index = random.nextInt(characters.length);
            char c = characters[index];

            if (set.contains(c)) {
                continue;
            }

            sb.append(c);
            set.add(c);
        }

        return sb.toString();
    }
}
