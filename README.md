# java-filmorate
На изображении представленна тестовая БД для учебного проекта Filmorate.

Ниже приведены несколько тестовых запросов к представляемой БД. 

1)Получение жанров указанного фильма: 
  SELECT g.*
  FROM genres AS g
  JOIN film_genre AS fg ON g.genre_id = fg.genre_id
  WHERE fg.film_id = ?;
  
2)Получение списка друзей пользователя:
  SELECT u.*
  FROM users AS u
  JOIN friendships AS f ON u.user_id = f.friend_id
  WHERE f.user_id = ?;
  
3)Получение общих друзей двух пользователей:
  SELECT u.*
  FROM users AS u
  INNER JOIN friendships AS f1 ON u.user_id = f1.friend_id AND f1.user_id = ?
  INNER JOIN friendships AS f2 ON u.user_id = f2.friend_id AND f2.user_id = ?
  WHERE f1.user_id != f2.user_id;
  
4)Получение наиболее популярных фильмов по количеству лайков: 
  SELECT  f.*, COUNT(l.user_id) AS likes_count
  FROM films AS f
  LEFT JOIN likes AS l ON f.film_id = l.film_id
  GROUP BY f.film_id
  ORDER BY likes_count DESC
  LIMIT 10;

<img width="786" height="744" alt="2025-10-26_20-19-30" src="https://github.com/user-attachments/assets/5a32c970-f13b-452a-aefb-e5e29c961274" />
