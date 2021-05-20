package com.example.todo.api.response;

import com.example.todo.domain.TodoId;
import com.example.todo.domain.TodoStatus;
import com.example.todo.domain.TodoText;

public class TodoResponse {

    public final Long id;

    public final String text;

    public final Boolean completed;

    public TodoResponse(TodoId id, TodoText text, TodoStatus status) {
        this.id = id.value();
        this.text = text.value();
        this.completed = status == TodoStatus.COMPLETED;
    }
    
}


