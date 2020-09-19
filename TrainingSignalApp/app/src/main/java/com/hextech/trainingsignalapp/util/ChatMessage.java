package com.hextech.trainingsignalapp.util;

import java.util.Objects;

public class ChatMessage {
    public String author, message, dateTime;

    public ChatMessage(String author, String message, String dateTime) {
        this.author = author;
        this.message = message;
        this.dateTime = dateTime;
    }

    public ChatMessage(){}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatMessage that = (ChatMessage) o;
        return author.equals(that.author) &&
                message.equals(that.message) &&
                dateTime.equals(that.dateTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, message, dateTime);
    }
}
