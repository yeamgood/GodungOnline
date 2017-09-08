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
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('14','/user/supplier', '1', 'fa fa-university', 'Supplier', 'menu.name.supplier', '6','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('15','/user/customer', '1', 'fa fa-user', 'Customer', 'menu.name.customer', '7','8');
INSERT INTO `godung_online`.`menu` (`menu_id`,`action`, `active`, `icon`, `name`, `code`, `priority`,`parent_id`) VALUES ('16','/user/employee', '1', 'fa fa-users', 'Employee', 'menu.name.employee', '8','8');

INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '8');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '9');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '10');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '11');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '12');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '13');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '14');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '15');
INSERT INTO `godung_online`.`role_menu` (`role_id`, `menu_id`) VALUES ('2', '16');

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
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('1', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('2', '3', '30', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('3', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('4', '3', '30', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('5', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('6', '3', '30', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('7', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('8', '3', '30', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('9', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('10', '3', '30', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('11', '2', '20', '1','0');
INSERT INTO `godung_online`.`measure_convert` (`measure_convert_id`, `measure_ref_id`, `quantity`, `measure_id`,`version`) VALUES ('12', '3', '30', '1','0');


INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('1', 'BRA20170830-00001', SYSDATE(), 'SYSTEM', '0', 'Sony', '2','');
INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('2', 'BRA20170830-00002', SYSDATE(), 'SYSTEM', '0', 'Lg', '2','');
INSERT INTO `godung_online`.`brand` (`brand_id`,`brand_code`, `create_date`, `create_user`, `version`, `brand_name`, `godung_id`, `description`) VALUES ('3', 'BRA20170830-00003', SYSDATE(), 'SYSTEM', '0', 'Toyota', '2','');

INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('1', 'PRO20170830-00001', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 1.5นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('2', 'PRO20170830-00002', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 2นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('3', 'PRO20170830-00003', SYSDATE(), 'SYSTEM', '0', 'เหล็กท่อ 3นิ้ว', '2','');
INSERT INTO `godung_online`.`product` (`product_id`,`product_code`, `create_date`, `create_user`, `version`, `product_name`, `godung_id`, `description`) VALUES ('4', 'PRO20170830-00003', SYSDATE(), 'SYSTEM', '0', 'กระจกใสบานใหญ่', '2','');

INSERT INTO `godung_online`.`warehouse` (`warehouse_id`,`warehouse_code`, `create_date`, `create_user`, `version`, `warehouse_name`, `godung_id`, `description`) VALUES ('1', 'WAR20170830-00001', SYSDATE(), 'SYSTEM', '0', 'คลังสินค้าฉะเชิงเทรา', '2','คลังสินค้า ที่จังหวัดฉะเชิงเทรา');

-- PROVINCES --
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (1, '10', 'กรุงเทพมหานคร', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (2, '11', 'สมุทรปราการ', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (3, '12', 'นนทบุรี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (4, '13', 'ปทุมธานี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (5, '14', 'พระนครศรีอยุธยา', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (6, '15', 'อ่างทอง', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (7, '16', 'ลพบุรี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (8, '17', 'สิงห์บุรี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (9, '18', 'ชัยนาท', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (10, '19', 'สระบุรี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (11, '20', 'ชลบุรี', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (12, '21', 'ระยอง', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (13, '22', 'จันทบุรี', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (14, '23', 'ตราด', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (15, '24', 'ฉะเชิงเทรา', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (16, '25', 'ปราจีนบุรี', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (17, '26', 'นครนายก', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (18, '27', 'สระแก้ว', 5);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (19, '30', 'นครราชสีมา', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (20, '31', 'บุรีรัมย์', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (21, '32', 'สุรินทร์', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (22, '33', 'ศรีสะเกษ', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (23, '34', 'อุบลราชธานี', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (24, '35', 'ยโสธร', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (25, '36', 'ชัยภูมิ', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (26, '37', 'อำนาจเจริญ', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (27, '39', 'หนองบัวลำภู', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (28, '40', 'ขอนแก่น', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (29, '41', 'อุดรธานี', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (30, '42', 'เลย', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (31, '43', 'หนองคาย', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (32, '44', 'มหาสารคาม', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (33, '45', 'ร้อยเอ็ด', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (34, '46', 'กาฬสินธุ์', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (35, '47', 'สกลนคร', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (36, '48', 'นครพนม', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (37, '49', 'มุกดาหาร', 3);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (38, '50', 'เชียงใหม่', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (39, '51', 'ลำพูน', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (40, '52', 'ลำปาง', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (41, '53', 'อุตรดิตถ์', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (42, '54', 'แพร่', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (43, '55', 'น่าน', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (44, '56', 'พะเยา', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (45, '57', 'เชียงราย', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (46, '58', 'แม่ฮ่องสอน', 1);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (47, '60', 'นครสวรรค์', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (48, '61', 'อุทัยธานี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (49, '62', 'กำแพงเพชร', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (50, '63', 'ตาก', 4);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (51, '64', 'สุโขทัย', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (52, '65', 'พิษณุโลก', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (53, '66', 'พิจิตร', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (54, '67', 'เพชรบูรณ์', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (55, '70', 'ราชบุรี', 4);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (56, '71', 'กาญจนบุรี', 4);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (57, '72', 'สุพรรณบุรี', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (58, '73', 'นครปฐม', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (59, '74', 'สมุทรสาคร', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (60, '75', 'สมุทรสงคราม', 2);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (61, '76', 'เพชรบุรี', 4);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (62, '77', 'ประจวบคีรีขันธ์', 4);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (63, '80', 'นครศรีธรรมราช', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (64, '81', 'กระบี่', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (65, '82', 'พังงา', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (66, '83', 'ภูเก็ต', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (67, '84', 'สุราษฎร์ธานี', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (68, '85', 'ระนอง', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (69, '86', 'ชุมพร', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (70, '90', 'สงขลา', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (71, '91', 'สตูล', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (72, '92', 'ตรัง', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (73, '93', 'พัทลุง', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (74, '94', 'ปัตตานี', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (75, '95', 'ยะลา', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (76, '96', 'นราธิวาส', 6);
INSERT INTO `godung_online`.`province` (`PROVINCE_ID`, `PROVINCE_CODE`, `PROVINCE_NAME`, `GEO_ID`) VALUES (77, '97', 'บึงกาฬ', 3);