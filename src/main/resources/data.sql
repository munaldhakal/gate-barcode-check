INSERT IGNORE INTO `user` (`id`, `address`, `created_by`, `edited_by`, `email`, `name`, `phone_number`, `user_type`) VALUES
(1, 'adminHome', 1, 1, 'admin@admin.admin', 'admin', '9999999999', 'ADMIN');

INSERT IGNORE INTO `login` (`id`, `last_login`, `login_status`, `password`, `user_id`, `username`) VALUES
(1, '2018-04-18 11:43:39.000000', 'LOGGEDOUT', '$2a$10$RY3x68jqdEClzm/zSv.I4.sHY/NvRr/TQAYuYxY7yom.jUh1LOt8O', 1, 'admin');