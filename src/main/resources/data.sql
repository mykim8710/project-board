insert into users(created_at, last_modified_at, user_email, user_memo, user_nickname, user_password, user_name, user_role) values ('2022-04-17 13:18:26', '2022-04-17 13:18:26', 'abc@abc.com', 'memo', 'abc', '$2a$10$sqPmJxoV2Md0KLTS1WA6gugWHbAw1lWYmw5ASt61A1HVCUAog5hbi', 'abc', 'ROLE_USER');
insert into users(created_at, last_modified_at, user_email, user_memo, user_nickname, user_password, user_name, user_role) values ('2022-04-17 13:18:26', '2022-04-17 13:18:26', 'test@test.com', 'memo', 'test', '$2a$10$sqPmJxoV2Md0KLTS1WA6gugWHbAw1lWYmw5ASt61A1HVCUAog5hbi', 'test', 'ROLE_USER');
insert into users(created_at, last_modified_at, user_email, user_memo, user_nickname, user_password, user_name, user_role) values ('2022-04-17 13:18:26', '2022-04-17 13:18:26', 'zxcv@zxcv.com', 'memo', 'zxcv', '$2a$10$sqPmJxoV2Md0KLTS1WA6gugWHbAw1lWYmw5ASt61A1HVCUAog5hbi', 'zxcv', 'ROLE_USER');

insert into article (article_title, article_content, created_at, last_modified_at, user_id) values ('Nulla justo.', 'Vivamus tortor. Duis mattis egestas metus. Aenean fermentum. Donec ut mauris eget massa tempor convallis. Nulla neque libero, convallis eget, eleifend luctus, ultricies eu, nibh. Quisque id justo sit amet sapien dignissim vestibulum.', '2022-05-20 16:52:45', '2022-05-27 22:40:18', 1);
insert into article (article_title, article_content, created_at, last_modified_at, user_id) values ('Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Nulla dapibus dolor vel est.', 'Cras non velit nec nisi vulputate nonummy. Maecenas tincidunt lacus at velit. Vivamus vel nulla eget eros elementum pellentesque. Quisque porta volutpat erat. Quisque erat eros, viverra eget, congue eget, semper rutrum, nulla. Nunc purus.', '2022-09-22 09:11:33', '2023-03-21 18:28:07', 1);
insert into article (article_title, article_content, created_at, last_modified_at, user_id) values ('Fusce congue, diam id ornare imperdiet, sapien urna pretium nisl, ut volutpat sapien arcu sed augue.', 'Donec posuere metus vitae ipsum. Aliquam non mauris. Morbi non lectus. Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis. Fusce posuere felis sed lacus. Morbi sem mauris, laoreet ut, rhoncus aliquet, pulvinar sed, nisl. Nunc rhoncus dui vel sem. Sed sagittis. Nam congue, risus semper porta volutpat, quam pede lobortis ligula, sit amet eleifend pede libero quis orci.', '2023-02-08 05:12:52', '2022-08-01 10:36:54', 1);
insert into article (article_title, article_content, created_at, last_modified_at, user_id) values ('In est risus, auctor sed, tristique in, tempus sit amet, sem.', 'Aliquam sit amet diam in magna bibendum imperdiet. Nullam orci pede, venenatis non, sodales sed, tincidunt eu, felis. Fusce posuere felis sed lacus.', '2022-10-15 15:18:36', '2022-02-11 14:34:33', 1);
insert into article (article_title, article_content, created_at, last_modified_at, user_id) values ('Phasellus id sapien in sapien iaculis congue.', 'Vivamus in felis eu sapien cursus vestibulum. Proin eu mi. Nulla ac enim. In tempor, turpis nec euismod scelerisque, quam turpis adipiscing lorem, vitae mattis nibh ligula nec sem. Duis aliquam convallis nunc.', '2022-03-08 10:08:38', '2022-12-18 19:43:30', 1);

insert into hashtag (hashtag_name, created_at, last_modified_at) values ('Pink', '2022-12-09', '2023-02-03');
insert into hashtag (hashtag_name, created_at, last_modified_at) values ('Goldenrod', '2023-03-03', '2022-07-28');
insert into hashtag (hashtag_name, created_at, last_modified_at) values ('Crimson', '2022-07-31', '2022-11-24');
insert into hashtag (hashtag_name, created_at, last_modified_at) values ('Red', '2022-11-01', '2023-04-20');
insert into hashtag (hashtag_name, created_at, last_modified_at) values ('Blue', '2023-01-27', '2023-04-05');

insert into article_hashtag (article_id, hashtag_id) values (1, 5);
insert into article_hashtag (article_id, hashtag_id) values (1, 2);
insert into article_hashtag (article_id, hashtag_id) values (2, 2);
insert into article_hashtag (article_id, hashtag_id) values (2, 3);
insert into article_hashtag (article_id, hashtag_id) values (3, 1);
insert into article_hashtag (article_id, hashtag_id) values (3, 2);
insert into article_hashtag (article_id, hashtag_id) values (3, 4);
insert into article_hashtag (article_id, hashtag_id) values (3, 3);
insert into article_hashtag (article_id, hashtag_id) values (4, 1);
insert into article_hashtag (article_id, hashtag_id) values (4, 3);
insert into article_hashtag (article_id, hashtag_id) values (5, 5);
insert into article_hashtag (article_id, hashtag_id) values (5, 2);
insert into article_hashtag (article_id, hashtag_id) values (5, 4);
insert into article_hashtag (article_id, hashtag_id) values (5, 3);


insert into article_comment (article_comment_content, article_id, created_at, last_modified_at, user_id, parent_article_comment_id) values ('Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 1, '2022-04-27 13:29:07', '2022-11-22 11:56:39', 1, null);
insert into article_comment (article_comment_content, article_id, created_at, last_modified_at, user_id, parent_article_comment_id) values ('Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 1, '2022-04-27 13:29:07', '2022-11-22 11:56:39', 1, null);
insert into article_comment (article_comment_content, article_id, created_at, last_modified_at, user_id, parent_article_comment_id) values ('Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 1, '2022-04-27 13:29:07', '2022-11-22 11:56:39', 1, null);
insert into article_comment (article_comment_content, article_id, created_at, last_modified_at, user_id, parent_article_comment_id) values ('Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 1, '2022-04-27 13:29:07', '2022-11-22 11:56:39', 1, null);
insert into article_comment (article_comment_content, article_id, created_at, last_modified_at, user_id, parent_article_comment_id) values ('Duis bibendum, felis sed interdum venenatis, turpis enim blandit mi, in porttitor pede justo eu massa.', 1, '2022-04-27 13:29:07', '2022-11-22 11:56:39', 1, null);


