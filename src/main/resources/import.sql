INSERT INTO `godung_online`.`rolelogin` (`role_id`,`role`) VALUES ('1','ADMIN');
INSERT INTO `godung_online`.`rolelogin` (`role_id`,`role`) VALUES ('2','USER');

INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('1', '1', '1', 'ADMIN_SYSTEM');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('2', '1', '1', 'ADMIN_FREE_1');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('3', '1', '1', 'ADMIN_FREE_2');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('4', '1', '1', 'ADMIN_FREE_3');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('5', '1', '1', 'ADMIN_FREE_4');
INSERT INTO `godung_online`.`role` (`role_id`, `system`, `version`, `name`) VALUES ('6', '1', '1', 'ADMIN_FREE_5');

INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('1','/admin/home', '1', 'fa fa-home', 'Home', 'menu.name.home', '1');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('2','/admin/user', '1', 'fa fa-user', 'Users', 'menu.name.user', '2');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('3','/admin/role', '1', 'fa fa-tasks', 'Roles', 'menu.name.role', '3');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('4','/admin/menu', '1', 'fa fa-desktop', 'Menu', 'menu.name.menu', '4');

INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('5','/user/home', '1', 'fa fa-home', 'Home', 'menu.name.home', '1');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('6','javascript:;', '1', 'fa fa-cogs', 'Setting', 'menu.name.setting', '60');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('7','/user/profile', '1', 'fa fa-user', 'Profile', 'menu.name.profile', '4','6');

INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '1');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '2');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '3');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('1', '4');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '5');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '6');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '7');

INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`) VALUES ('8','javascript:;', '1', 'fa fa-table', 'Product Management', 'menu.name.productmanagement', '40');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('9','/user/warehouse', '1', 'fa fa-home', 'Werehouse', 'menu.name.werehouse', '1','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('10','/user/product', '1', 'fa fa-cubes', 'Product', 'menu.name.product', '2','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('11','/user/brand', '1', 'fa fa-tags', 'Brand', 'menu.name.brand', '3','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('12','/user/measure', '1', 'fa fa-book', 'Measure', 'menu.name.measure', '4','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('13','/user/category', '1', 'fa fa-sitemap', 'Category', 'menu.name.category', '5','8');

INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '8');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '9');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '10');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '11');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '12');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '13');

-- COMMON CODE
INSERT INTO `godung_online`.`common` (`common_id`, `common_type`, `common_key`, `common_value`, `description`,`sequence`) VALUES ('1', 'LANGUAGE', 'th', 'ภาษาไทย', 'language thai','1');
INSERT INTO `godung_online`.`common` (`common_id`, `common_type`, `common_key`, `common_value`, `description`,`sequence`) VALUES ('2', 'LANGUAGE', 'en', 'English', 'language Englist','2');

-- ADD USER ADMIN --
INSERT INTO `godung_online`.`user` (`user_id`,`active`, `email`, `name`, `password`,`language`) VALUES ('1','1', 'admin@gmail.com', 'admin', '$2a$10$z3LIFllUl2jRgzEBl2JYnucHH8v7Dp64A11du7Ehmo6drTf7jeOKq','en');
INSERT INTO `godung_online`.`user_rolelogin` (`user_id`, `role_id`) VALUES ('1', '1');
INSERT INTO `godung_online`.`godung` (`godung_id`, `godung_name`, `create_date`, `create_user`, `version`, `active`) VALUES ('1', 'admin', SYSDATE() , 'SYSTEM', '0', '1');
INSERT INTO `godung_online`.`godung_user_role` (`id`, `godung_id`, `role_id`, `user_id`) VALUES ('1', '1', '1', '1');

-- ADD USER USER --
INSERT INTO `godung_online`.`user` (`user_id`,`active`, `email`, `name`, `password`,`language`) VALUES ('2','1', 'user@gmail.com', 'user niyomthrong', '$2a$10$MdTHdzf7fafXyH72gWn5XuPYj1GsPUYUqF5tSt6bmw75LVMsONEl.','en');
INSERT INTO `godung_online`.`user_rolelogin` (`user_id`, `role_id`) VALUES ('2', '2');
INSERT INTO `godung_online`.`godung` (`godung_id`, `godung_name`, `create_date`, `create_user`, `version`, `active`) VALUES ('2', 'userGodung', SYSDATE() , 'SYSTEM', '0', '1');
INSERT INTO `godung_online`.`godung_user_role` (`id`, `godung_id`, `role_id`, `user_id`) VALUES ('2', '2', '2', '2');
INSERT INTO `godung_online`.`category` (`category_id`,`category_code`, `create_date`, `create_user`, `version`, `category_name`, `godung_id`, `description`) VALUES ('1', 'CAT20170830-00001', SYSDATE(), 'SYSTEM', '0', 'เหล็ก', '2','อุปกรณ์เหล็ก');
INSERT INTO `godung_online`.`category` (`category_id`,`category_code`, `create_date`, `create_user`, `version`, `category_name`, `godung_id`, `description`) VALUES ('2', 'CAT20170830-00002', SYSDATE(), 'SYSTEM', '0', 'กระจก', '2','อุปกรณ์กระจก');
INSERT INTO `godung_online`.`category` (`category_id`,`category_code`, `create_date`, `create_user`, `version`, `category_name`, `godung_id`, `description`) VALUES ('3', 'CAT20170830-00003', SYSDATE(), 'SYSTEM', '0', 'อลูมิเนียม', '2','อุปกรณ์อลูมิเนียม');

INSERT INTO `godung_online`.`measure` (`measure_id`,`measure_code`, `create_date`, `create_user`, `version`, `measure_name`, `godung_id`, `description`) VALUES ('1', 'MEA20170830-00001', SYSDATE(), 'SYSTEM', '0', 'เส้น', '2','อุปกรณ์เหล็ก');
INSERT INTO `godung_online`.`measure` (`measure_id`,`measure_code`, `create_date`, `create_user`, `version`, `measure_name`, `godung_id`, `description`) VALUES ('2', 'MEA20170830-00002', SYSDATE(), 'SYSTEM', '0', 'แผ่น', '2','อุปกรณ์กระจก');
INSERT INTO `godung_online`.`measure` (`measure_id`,`measure_code`, `create_date`, `create_user`, `version`, `measure_name`, `godung_id`, `description`) VALUES ('3', 'MEA20170830-00003', SYSDATE(), 'SYSTEM', '0', 'ท่อ', '2','อุปกรณ์อลูมิเนียม');

INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('1', 'BRA20170830-00001', SYSDATE(), 'SYSTEM', '0', 'Sony', '2','');
INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('2', 'BRA20170830-00002', SYSDATE(), 'SYSTEM', '0', 'Lg', '2','');
INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('3', 'BRA20170830-00003', SYSDATE(), 'SYSTEM', '0', 'Toyota', '2','');

INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('1', 'PRO20170830-00001', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 1.5นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('2', 'PRO20170830-00002', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 2นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('3', 'PRO20170830-00003', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 3นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('4', 'PRO20170830-00003', SYSDATE(), 'SYSTEM', '0', 'กระจกใสบานใหญ่', '2','');

INSERT INTO `godung_online`.`warehouse` (`warehouse_id`,`warehouse_code`, `create_date`, `create_user`, `version`, `warehouse_name`, `godung_id`, `description`) VALUES ('1', 'WAR20170830-00001', SYSDATE(), 'SYSTEM', '0', 'คลังสินค้าฉะเชิงเทรา', '2','คลังสินค้า ที่จังหวัดฉะเชิงเทรา');