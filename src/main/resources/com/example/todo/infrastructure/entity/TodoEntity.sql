FIND_BY_USERID =
select
  *
FROM
  todo
WHERE
  user_id = :userId
ORDER BY
  todo_id

FIND_BY_TODOID_USERID =
select
  *
FROM
  todo
WHERE
  todo_id = :todoId
  AND user_id = :userId
ORDER BY
  todo_id