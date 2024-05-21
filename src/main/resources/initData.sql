-- 话题初始数据
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(1, NOW(), '/avatars/icon/new.png', '最新', 1, 0);
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(2, NOW(), '/avatars/icon/recommend.png', '推荐', 2, 0);
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(3, NOW(), '/avatars/icon/follow.png', '关注', 3, 0);
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(4, NOW(), '/avatars/icon/communicate.png', '交流', 4, 0);
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(5, NOW(), '/avatars/icon/question.png', '提问', 5, 0);
INSERT INTO `topic`(id, create_time, icon, `name`, rank, `status`) VALUES(6, NOW(), '/avatars/icon/feedback.png', '反馈', 6, 0);
-- 标签初始数据
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (1, NOW(), 'java', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (2, NOW(), 'mysql', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (3, NOW(), 'springboot', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (4, NOW(), 'python', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (5, NOW(), 'C', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (6, NOW(), 'C++', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (7, NOW(), 'C+', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (8, NOW(), '工作', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (9, NOW(), '生活', 0);
INSERT INTO `tag` (id, create_time, `name`, `quoted_times`) VALUES (10, NOW(), '学习', 0);