package com.example.todo.infrastructure;

import com.example.todo.application.TodoRepository;
import com.example.todo.domain.*;
import com.example.todo.infrastructure.entity.TodoEntity;
import com.example.todo.infrastructure.entity.TodoIdSequence;
import nablarch.common.dao.EntityList;
import nablarch.common.dao.NoDataException;
import nablarch.common.dao.UniversalDao;
import nablarch.core.repository.di.config.externalize.annotation.SystemRepositoryComponent;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SystemRepositoryComponent
public class JdbcTodoRepository implements TodoRepository {

    @Override
    public List<Todo> list(UserId userId) {
        Map<String, String> condition = Map.of("userId", userId.value());
        EntityList<TodoEntity> todoEntities = UniversalDao.findAllBySqlFile(TodoEntity.class, "FIND_BY_USERID", condition);

        return todoEntities.stream().map(this::createTodo).collect(Collectors.toList());
    }

    @Override
    public TodoId nextId() {
        TodoIdSequence todoIdSequence = UniversalDao.findBySqlFile(TodoIdSequence.class, "NEXT_TODO_ID", new Object[0]);
        return new TodoId(todoIdSequence.getTodoId());
    }

    @Override
    public void add(Todo todo) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodoId(todo.id().value());
        todoEntity.setText(todo.text().value());
        todoEntity.setCompleted(todo.status() == TodoStatus.COMPLETED);
        todoEntity.setUserId(todo.userId().value());
        UniversalDao.insert(todoEntity);
    }

    private Todo createTodo(TodoEntity entity) {
        return new Todo(
                new TodoId(entity.getTodoId()),
                new TodoText(entity.getText()),
                entity.getCompleted() ? TodoStatus.COMPLETED : TodoStatus.INCOMPLETE,
                new UserId(entity.getUserId()));
    }

    @Override
    public Todo get(TodoId todoId, UserId userId) {

        Map<String, Object> condition = Map.of("todoId", todoId.value(), "userId", userId.value());
        EntityList<TodoEntity> todoEntities = UniversalDao.findAllBySqlFile(TodoEntity.class, "FIND_BY_TODOID_USERID", condition);
        if(todoEntities.size() <= 0 ) {
            throw new NoDataException();
        }

        return createTodo(todoEntities.get(0));

    }

    @Override
    public void update(Todo todo) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodoId(todo.id().value());
        todoEntity.setText(todo.text().value());
        todoEntity.setCompleted(todo.status() == TodoStatus.COMPLETED);
        todoEntity.setUserId(todo.userId().value());
        UniversalDao.update(todoEntity);
    }

    @Override
    public void delete(TodoId todoId, UserId userId) {
        TodoEntity todoEntity = new TodoEntity();
        todoEntity.setTodoId(todoId.value());
        todoEntity.setUserId(userId.value());
        UniversalDao.delete(todoEntity);
    }

}
