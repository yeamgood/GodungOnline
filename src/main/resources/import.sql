INSERT INTO `godung_online`.`rolelogin` (`role`) VALUES ('ADMIN');
INSERT INTO `godung_online`.`rolelogin` (`role`) VALUES ('USER');

INSERT INTO `godung_online`.`role` (`version`, `system`, `name`) VALUES ('1', '1', 'ADMIN');
INSERT INTO `godung_online`.`role` (`version`, `system`, `name`) VALUES ('1', '1', 'SUPER_USER');

INSERT INTO `godung_online`.`user` (`active`, `email`, `name`, `password`, `role_id`) VALUES ('1', 'admin@gmail.com', 'admin', '$2a$10$z3LIFllUl2jRgzEBl2JYnucHH8v7Dp64A11du7Ehmo6drTf7jeOKq', '1');
INSERT INTO `godung_online`.`user_rolelogin` (`user_id`, `role_id`) VALUES ('1', '1');

INSERT INTO `godung_online`.`menu` (`action`, `active`, `icon`, `name`, `priority`) VALUES ('/admin/users', '1', 'fa fa-home', 'Home', '1');
INSERT INTO `godung_online`.`menu` (`action`, `active`, `icon`, `name`, `priority`) VALUES ('/admin/users', '1', 'fa fa-user', 'Users', '2');
INSERT INTO `godung_online`.`menu` (`action`, `active`, `icon`, `name`, `priority`) VALUES ('/admin/users', '1', 'fa fa-desktop', 'Menu', '3');

INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '1');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '2');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '3');
