INSERT INTO `godung_online`.`rolelogin` (`role_id`,`role`) VALUES ('1','ADMIN');
INSERT INTO `godung_online`.`rolelogin` (`role_id`,`role`) VALUES ('2','USER');

INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('1', '1', '1', 'ADMIN_SYSTEM');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('2', '1', '1', 'ADMIN_FREE_1');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('3', '1', '1', 'ADMIN_FREE_2');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('4', '1', '1', 'ADMIN_FREE_3');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('5', '1', '1', 'ADMIN_FREE_4');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('6', '1', '1', 'ADMIN_FREE_5');

INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`) VALUES ('1','/admin/home', '1', 'fa fa-home', 'Home', '1');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`) VALUES ('2','/admin/user', '1', 'fa fa-user', 'Users', '2');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`) VALUES ('3','/admin/role', '1', 'fa fa-tasks', 'Roles', '3');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`) VALUES ('4','/admin/menu', '1', 'fa fa-desktop', 'Menu', '4');

INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`) VALUES ('5','#', '1', 'fa fa-cogs', 'Setting', '4');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `priority`,`parent_id`) VALUES ('6','/user/profile', '1', 'fa fa-user', 'Profile', '4','5');


INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '1');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '2');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '3');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '4');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '5');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '6');

-- ADD USER ADMIN --
INSERT INTO `godung_online`.`user` (`user_id`,`active`, `email`, `name`, `password`) VALUES ('1','1', 'admin@gmail.com', 'admin', '$2a$10$z3LIFllUl2jRgzEBl2JYnucHH8v7Dp64A11du7Ehmo6drTf7jeOKq');
INSERT INTO `godung_online`.`user_rolelogin` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `godung_online`.`godung` (`godung_id`, `godung_name`, `create_date`, `create_user`, `version`, `active`) VALUES ('1', 'admin', SYSDATE() , 'SYSTEM', '0', '1');
INSERT INTO `godung_online`.`godung_user_role` (`id`, `godung_id`, `role_id`, `user_id`) VALUES ('1', '1', '1', '1');

-- ADD USER USER --
INSERT INTO `godung_online`.`user` (`user_id`,`active`, `email`, `name`, `password`) VALUES ('2','1', 'user@gmail.com', 'user', '$2a$10$MdTHdzf7fafXyH72gWn5XuPYj1GsPUYUqF5tSt6bmw75LVMsONEl.');
INSERT INTO `godung_online`.`user_rolelogin` (`user_id`, `role_id`) VALUES ('2', '2');
INSERT INTO `godung_online`.`godung` (`godung_id`, `godung_name`, `create_date`, `create_user`, `version`, `active`) VALUES ('2', 'user', SYSDATE() , 'SYSTEM', '0', '1');
INSERT INTO `godung_online`.`godung_user_role` (`id`, `godung_id`, `role_id`, `user_id`) VALUES ('2', '2', '2', '2');
