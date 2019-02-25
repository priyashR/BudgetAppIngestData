package com.gmail.ramawthar.priyash.rabbit;

import org.springframework.stereotype.Component;

@Component
public class Receiver {


    public void receiveMessage(Object message) {
        System.out.println("Received <" + message.toString() + ">");
    }


}
