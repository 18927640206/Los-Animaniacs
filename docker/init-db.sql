CREATE DATABASE IF NOT EXISTS uamishop_catalogo;
CREATE DATABASE IF NOT EXISTS uamishop_ventas;
CREATE DATABASE IF NOT EXISTS uamishop_ordenes;

-- Darle permisos al usuario en las nuevas bases de datos
GRANT ALL PRIVILEGES ON uamishop_catalogo.* TO 'uamishop'@'%';
GRANT ALL PRIVILEGES ON uamishop_ventas.* TO 'uamishop'@'%';
GRANT ALL PRIVILEGES ON uamishop_ordenes.* TO 'uamishop'@'%';
FLUSH PRIVILEGES;