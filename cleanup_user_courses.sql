
-- Limpiar registros huérfanos de user_courses
-- Ejecutar en PostgreSQL (pgAdmin, DBeaver, etc.)

-- Eliminar referencias a courses que no existen
DELETE FROM user_courses
WHERE course_id NOT IN (SELECT id FROM courses);

-- Eliminar referencias a users que no existen
DELETE FROM user_courses
WHERE user_id NOT IN (SELECT id FROM users);

-- Verificar resultado
SELECT * FROM user_courses;