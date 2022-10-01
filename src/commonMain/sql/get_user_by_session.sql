SELECT sessions.until, users.id, users.name, users.auto_patrol, users.admin
FROM users
         INNER JOIN sessions ON users.id = sessions.user
WHERE sessions.key = ?