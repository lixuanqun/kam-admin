-- kam-admin 模块：数据库用户与权限（示例）
-- 使用前请修改用户名、主机与密码；生产环境请使用强密码并限制主机

-- MySQL 示例
CREATE USER IF NOT EXISTS 'kamailio_admin'@'localhost' IDENTIFIED BY 'your_secure_password';
GRANT ALL PRIVILEGES ON kamailio.* TO 'kamailio_admin'@'localhost';
FLUSH PRIVILEGES;

-- 若需远程连接（谨慎开放）
-- CREATE USER IF NOT EXISTS 'kamailio_admin'@'%' IDENTIFIED BY 'your_secure_password';
-- GRANT ALL PRIVILEGES ON kamailio.* TO 'kamailio_admin'@'%';
-- FLUSH PRIVILEGES;
