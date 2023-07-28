USE master
GO
CREATE DATABASE DaviTickets
GO
--   DROP DATABASE DaviTickets
USE DaviTickets;
GO

--cách đặt phim: Chọn phim (Từ table movie) và chọn giờ (Từ table showtime) 
--sau đó chọn ghế (Từ table seat và kiểm tra ghế còn trống kh) 
--sau khi chọn xong (Dữ liệu được tạm thêm vào table booking kiểm tra xem có bao nhiêu vé và đưa tạm dữ liệu vào table payment) 
--tới bước thanh toán (Đọc dữ liệu vừa được thêm tạm vào table booking và table payment hiển thị ra số tiền cũng như vé)
--Thanh toán xong mới đẩy dữ liệu xuống dtb

CREATE TABLE movie(
    movie_id INT IDENTITY PRIMARY KEY NOT NULL,
    title_movie NVARCHAR(200) NOT NULL, --Tự đề phim
	release_date DATE NOT NULL, --Ngày ra mắt
	duration INT NOT NULL, --Phim bao nhiêu phút
	genre NVARCHAR(200) NOT NULL, --Thể loại
	poster NVARCHAR(MAX) NOT NULL, --Hình poster
	rating FLOAT NOT NULL, --Đánh giá trên thang điểm 5
	film_director NVARCHAR(200) NOT NULL, --Đạo diễn
	description_movie NVARCHAR(200) NOT NULL --Mô tả ngắn gọn phim
)

CREATE TABLE room (
	room_id INT PRIMARY KEY NOT NULL,
	room_name NVARCHAR(20) NOT NULL
)

--Giờ chiếu các phim
CREATE TABLE showtime (
	showtime_id INT IDENTITY PRIMARY KEY NOT NULL,
	movie_id INT NOT NULL FOREIGN KEY REFERENCES movie(movie_id), --id phim
	room_id INT NOT NULL FOREIGN KEY REFERENCES room(room_id), --id phòng
	showdate DATE NOT NULL, --Ngày chiếu
	start_time TIME NOT NULL, --Giờ chiếu
	end_time TIME NOT NULL, --Giờ kết thúc
	price INT NOT NULL --Tiền
)

--Ghế
CREATE TABLE seat(
    seat_id INT IDENTITY PRIMARY KEY NOT NULL,
	vip BIT NOT NULL, --Ghế vip
	row_symbol NVARCHAR(2) NOT NULL, --Hàng ghế
	seat_num INT NOT NULL, --Vị trí ghế theo hàng
	available BIT NOT NULL --Ghế còn khả dụng không
)

--Người dùng
CREATE TABLE users (
	userid INT IDENTITY PRIMARY KEY NOT NULL,
	full_name NVARCHAR(50) NOT NULL,
	gender NVARCHAR(5) NULL,
	user_password NVARCHAR(MAX) NOT NULL,
	phone VARCHAR(20) NULL,
	email NVARCHAR(100) NOT NULL,
	profile_picture NVARCHAR(MAX) NULL,
	account_status BIT NULL, --trạng thái hoạt động
	processed_by BIT NULL, --xác thực
	user_birtday DATE NULL,
	user_dayjoin DATE NOT NULL,
	gg_id NVARCHAR(MAX) NULL,
	user_role BIT NOT NULL --Vai trò người dùng
)


--Vé (lưu trữ thông tin về mỗi lượt đặt vé, bao gồm thời gian chiếu, chỗ ngồi và thông tin khách hàng.)
CREATE TABLE booking (
	booking_id INT IDENTITY PRIMARY KEY NOT NULL,
	showtime_id INT NOT NULL FOREIGN KEY REFERENCES showtime(showtime_id),
	seat_id INT NOT NULL FOREIGN KEY REFERENCES seat(seat_id),
	userid INT NOT NULL FOREIGN KEY REFERENCES users(userid), --id ng dung
	booking_date DATE NOT NULL
)

--Thanh toán
CREATE TABLE payment (
	payment_id INT IDENTITY PRIMARY KEY NOT NULL,
	booking_id INT NOT NULL FOREIGN KEY REFERENCES booking(booking_id), 
	total_amount INT NOT NULL, --Tổng tiền
	payment_date DATE NOT NULL --Ngày thanh toán
)

INSERT INTO movie VALUES 
	(N'Quỷ Quyệt', CAST('2023-07-07' AS Date), 132, N'Hài, Hành Động, Hồi hộp, Tâm Lý ', '/img/quyquyet.jpg', 4.5, N'Patrick Wilson', N'Phim rất hay thể loại rất phù hợp mọi lứa tuổi '),
	(N'Xứ Sở Các Nguyên Tố', CAST('2023-06-16' AS Date), 101, N'Hài, Hành Động, Kịch Tính', '/img/xusocacnguyento.jpg', 4.4, N'Peter Sohn', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'Tay Đua Kiệt Xuất', CAST('2023-07-10' AS Date), 128, N'Hài, Tình Cảm, Hành Động', '/img/tayduakietxuat.jpg', 4.0, N'Ross Venokur', N'Phim hài, rất xúc động tạo nên sự khác biệt của các thể loại tình cảm'),
	(N'Quý Công Tử', CAST('2023-06-20' AS Date), 96, N'Hành Động, Hài', '/img/quycongtu.jpg', 4.2, N'Duy Joseph', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'Đảo Tử Thần', CAST('2023-07-05' AS Date), 90, N'Hài, Hành Động', '/img/daotuthan.jpg', 4.6, N'Park Hoon-jung', N'Phim hành động xem kẻ hài hước '),
	(N'Sid Toang Rồi', CAST('2023-07-12' AS Date), 95, N'Hài', N'/img/sidtoangroi.jpg', 4.0, N'Eiichirō Hasumi', N'Phim hài hước tạo nên sự vui vẻ '),
	(N'RUBY Thủy Quái Tuổi Teen', CAST('2023-06-28' AS Date), 110, N'Hài, Tình Cảm', 'img/rubythuyquaituoiteen.jpg', 4.7, N'Eli Gonda', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'LIVESTREAM', CAST('2023-04-28' AS Date), 100, N'Hài, Hành Động', '/img/livestream.jpg', 4.6, N'Kirk DeMicco, Faryn Pearl', N'Phim hài hước, xen kẽ hành động'),
	(N'Bỗng Dưng Được Yêu', CAST('2023-07-05' AS Date), 100, N'Hành Động, Hài Hước', '/img/bongdungduocyeu.jpg', 4.1, N'Danny Đỗ', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'FLASH', CAST('2023-06-14' AS Date), 91, N'Tâm Lý, Tình Cảm', N'/img/flash.jpg', 4.8, N'Trần Ngọc Giàu', N'Phim hài, rất xúc động tạo nên sự khác biệt của các thể loại tình cảm'),
	(N'Ma Sơ Trục Quỷ', CAST('2023-06-28' AS Date), 101, N'Hài, Hành Động', N'/img/masotrucquy.jpg', 4.4, N'Hoàng Tuấn Cường', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'Cô Thành Trong Gương', CAST('2023-04-28' AS Date), 88, N'Hài, Tình Cảm', N'/img/cothanhtrongguog.jpg', 4.3, N'Đức Thịnh', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'Tà Chú Cấm', CAST('2023-06-21' AS Date), 87, N'Hài, Tình Cảm', '/img/tachucam.jpg', 4.6, N'Lê Lộc', N'Phim rất hay thể loại rất phù hợp mọi lứa tuổi')

INSERT INTO room VALUES
	(1, N'Phòng chiếu 1'),
	(2, N'Phòng chiếu 2'),
	(3, N'Phòng chiếu 3'),
	(4, N'Phòng chiếu 4'),
	(5, N'Phòng chiếu 5')

INSERT INTO showtime VALUES 
	 (1, 1, CAST('2023-07-17' AS Date), CAST('17:00:00' AS Time), CAST('19:30:00' AS Time), 75000),
	 (2, 1, CAST('2023-06-23' AS Date), CAST('08:00:00' AS Time), CAST('09:30:00' AS Time), 60000),
	 (3, 2, CAST('2023-07-14' AS Date), CAST('07:00:00' AS Time), CAST('09:00:00' AS Time), 50000),
	 (4, 2, CAST('2023-06-23' AS Date), CAST('09:00:00' AS Time), CAST('10:30:00' AS Time), 55000),
	 (5, 1, CAST('2023-07-07' AS Date), CAST('13:00:00' AS Time), CAST('14:20:00' AS Time), 45000),
	 (6, 1, CAST('2023-07-14' AS Date), CAST('13:20:00' AS Time), CAST('14:25:00' AS Time), 40000),
	 (7, 4, CAST('2023-06-30' AS Date), CAST('15:30:00' AS Time), CAST('17:10:00' AS Time), 65000),
	 (8, 3, CAST('2023-04-30' AS Date), CAST('10:00:00' AS Time), CAST('11:30:00' AS Time), 60000),
	 (9, 2, CAST('2023-07-07' AS Date), CAST('10:30:00' AS Time), CAST('12:30:00' AS Time), 50000),
	 (10, 5, CAST('2023-06-16' AS Date), CAST('14:00:00' AS Time), CAST('15:20:00' AS Time), 55000),
	 (11, 3, CAST('2023-06-30' AS Date), CAST('14:30:00' AS Time), CAST('16:01:00' AS Time), 45000),
	 (12, 4, CAST('2023-04-30' AS Date), CAST('07:00:00' AS Time), CAST('08:18:00' AS Time), 40000),
	 (13, 4, CAST('2023-06-23' AS Date), CAST('09:00:00' AS Time), CAST('10:17:00' AS Time), 65000)


INSERT INTO seat VALUES 
	(0, 'A', 1, 1),
	(0, 'A', 2, 1),
	(0, 'A', 3, 1),
	(0, 'A', 4, 1),
	(0, 'A', 5, 1),
	(0, 'A', 6, 1),
	(0, 'A', 7, 1),
	(0, 'A', 8, 1),
	(0, 'A', 9, 1),
	(0, 'A', 10, 1),
	(0, 'A', 11, 1),
	(0, 'B', 1, 1),
	(0, 'B', 2, 1),
	(0, 'B', 3, 1),
	(0, 'B', 4, 1),
	(0, 'B', 5, 1),
	(0, 'B', 6, 1),
	(0, 'B', 7, 1),
	(0, 'B', 8, 1),
	(0, 'B', 9, 1),
	(0, 'B', 10, 1),
	(0, 'B', 11, 1),
	(0, 'C', 1, 1),
	(0, 'C', 2, 1),
	(0, 'C', 3, 1),
	(0, 'C', 4, 1),
	(0, 'C', 5, 1),
	(0, 'C', 6, 1),
	(0, 'C', 7, 1),
	(0, 'C', 8, 1),
	(0, 'C', 9, 1),
	(0, 'C', 10, 1),
	(0, 'C', 11, 1),
	(0, 'D', 1, 1),
	(0, 'D', 2, 1),
	(0, 'D', 3, 1),
	(1, 'D', 4, 1),
	(1, 'D', 5, 1),
	(1, 'D', 6, 1),
	(1, 'D', 7, 1),
	(1, 'D', 8, 1),
	(0, 'D', 9, 1),
	(0, 'D', 10, 1),
	(0, 'D', 11, 1),
	(0, 'E', 1, 1),
	(0, 'E', 2, 1),
	(0, 'E', 3, 1),
	(1, 'E', 4, 1),
	(1, 'E', 5, 1),
	(1, 'E', 6, 1),
	(1, 'E', 7, 1),
	(1, 'E', 8, 1),
	(0, 'E', 9, 1),
	(0, 'E', 10, 1),
	(0, 'E', 11, 1),
	(0, 'F', 1, 1),
	(0, 'F', 2, 1),
	(0, 'F', 3, 1),
	(1, 'F', 4, 1),
	(1, 'F', 5, 1),
	(1, 'F', 6, 1),
	(1, 'F', 7, 1),
	(1, 'F', 8, 1),
	(0, 'F', 9, 1),
	(0, 'F', 10, 1),
	(0, 'F', 11, 1),
	(0, 'G', 1, 1),
	(0, 'G', 2, 1),
	(0, 'G', 3, 1),
	(0, 'G', 4, 1),
	(0, 'G', 5, 1),
	(0, 'G', 6, 1),
	(0, 'G', 7, 1),
	(0, 'G', 8, 1),
	(0, 'G', 9, 1),
	(0, 'G', 10, 1),
	(0, 'G', 11, 1)

INSERT INTO users VALUES 
	(N'Trần Hữu Đang', 'Nam', N'dangth', N'0917288723', N'dangthpc04349@fpt.edu.vn', NULL, 1, 1, CAST('9-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 1),
	(N'Lê Bích Vi', 'Nữ', N'vilb', N'0178296424', N'vilbpc04354@fpt.edu.vn', NULL, 1, 1, CAST('6-2-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0),
	(N'Phùng Quốc Vinh', 'Nam', N'vinhpq', N'0862738927', N'vinhpqpc04338@fpt.edu.vn',  NULL, 1, 1, CAST('11-15-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0),
	(N'Đoàn Hiệp Sỹ', 'Nam', N'sydh', N'0836452473', N'sydhpc04388@fpt.edu.vn', NULL, 1, 1, CAST('4-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0),
	(N'Nguyễn Khánh Đan', 'Nữ', N'dannk', N'0924637483', N'dannkpc04351@fpt.edu.vn', NULL, 1, 1, CAST('11-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0),
	(N'Châu Hoài Phúc', 'Nam', N'phucch', N'0918093162', N'phucchpc04191@fpt.edu.vn', NULL, 1, 1, CAST('11-2-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0),
	(N'Quách Hữu Nghĩa', 'Nam', N'nghiahq', N'012346789', N'nghiaqh@fe.edu.vn', NULL, 1, 1, CAST('1-1-1990' AS DATE), CAST('7-22-2023' AS DATE), NULL, 0)

/*
select * from movie
select * from seat
select * from users
select * from showtime
select * from booking
select * from payment
*/

INSERT INTO booking VALUES 
	(1, 27, 1, CAST('2023-06-17' AS Date)),
	(1, 28, 2, CAST('2023-06-17' AS Date)),
	(1, 29, 2, CAST('2023-06-17' AS Date)),
	(1, 30, 2, CAST('2023-06-17' AS Date)),
	(2, 38, 3, CAST('2023-07-17' AS Date)), --ghe vip = gia goc + 10 ngan
	(1, 39, 4, CAST('2023-07-17' AS Date)), --ghe vip = gia goc + 10 ngan
	(2, 51, 5, CAST('2023-07-17' AS Date)) --ghe vip = gia goc + 10 ngan

--xem ghế
/*	SELECT * FROM seat
	SELECT vip, row_symbol, seat_num FROM booking INNER JOIN seat ON booking.seat_id = seat.seat_id
*/

--Những vé chưa thanh toán của user
/*
	SELECT * FROM booking WHERE booking.booking_id NOT IN (SELECT booking_id FROM payment) AND userid = '2' 
*/

INSERT INTO payment VALUES
	(1, 0, CAST('2023-01-1' AS Date)),
	(1, 0, CAST('2023-02-1' AS Date)),
	(1, 0, CAST('2023-03-1' AS Date)),
	(1, 0, CAST('2023-04-1' AS Date)),
	(1, 0, CAST('2023-05-1' AS Date)),
	(1, 75000, CAST('2023-06-17' AS Date)),
	(2, 75000,CAST('2023-06-17' AS Date)),
	(3, 75000, CAST('2023-06-17' AS Date)),
	(4, 75000, CAST('2023-06-17' AS Date)),
	(5, 70000,CAST('2023-07-17' AS Date)), --ghe vip = 60.000 + 10.000
	(6, 85000, CAST('2023-07-17' AS Date)), --ghe vip = 75.000 + 10.000
	(7, 70000, CAST('2023-07-17' AS Date)) --ghe vip = 60.000 + 10 ngan


/*
--Thống kê doanh thu ngày
SELECT CONVERT( VARCHAR ,SUM(total_amount)) FROM payment WHERE DAY(payment_date) = '17'

--Thống kê doanh thu tháng
SELECT SUM(total_amount) FROM payment WHERE MONTH(payment_date) = '6'

--Phần trăm doanh thu hôm nay so với ngày cao nhất trong tháng
SELECT SUM(total_amount) FROM payment WHERE DAY(payment_date) = '17'

SELECT MAX(total_amount) FROM payment WHERE MONTH(payment_date) = '7'

--Thống kê tổng người dùng trong tháng
SELECT COUNT(userid) FROM users WHERE MONTH(user_dayjoin) = '7'

--Doanh thu theo năm
SELECT MONTH(payment_date) AS MONTH, SUM(total_amount) FROM payment GROUP BY MONTH(payment_date) ORDER BY MONTH(payment_date) ASC

--Top 3 khoảng giờ phim được xem nhiều nhất
SELECT TOP 3 DATEPART(HOUR, CONVERT(DATETIME, start_time, 8)), COUNT(*) AS TIMES FROM showtime GROUP BY showtime.start_time ORDER BY TIMES DESC
select * from showtime

*/


