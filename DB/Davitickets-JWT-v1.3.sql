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

CREATE TABLE roles(
    role_id INT IDENTITY PRIMARY KEY NOT NULL,
	[name] NVARCHAR(50) NOT NULL,
	role_des NVARCHAR(max) NULL
)

--Người dùng
CREATE TABLE users (
	userid INT IDENTITY PRIMARY KEY NOT NULL,
	full_name NVARCHAR(50) NOT NULL,
	[user_name] VARCHAR(20) NOT NULL,
	gender NVARCHAR(5) NULL,
	user_password VARCHAR(MAX) NOT NULL,
	phone VARCHAR(20) NULL,
	email VARCHAR(100) NOT NULL,
	profile_picture VARCHAR(MAX) NULL,
	account_status BIT NULL, --trạng thái hoạt động
	processed_by BIT NULL, --xác thực
	user_birtday DATE NULL,
	user_dayjoin DATE NOT NULL,
	gg_id VARCHAR(MAX) NULL,
)

CREATE TABLE user_role(
	id INT IDENTITY PRIMARY KEY NOT NULL,
	userid INT NOT NULL FOREIGN KEY REFERENCES users(userid), --id ng dung
	role_id INT NOT NULL FOREIGN KEY REFERENCES roles(role_id)--Vai trò người dùng
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
	(N'Quỷ Quyệt', CAST('2023-07-07' AS Date), 132, N'Hài, Hành Động, Hồi hộp, Tâm Lý ', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/quyquyet.jpg?alt=media&token=c939c324-5934-4e66-9450-3552f201f091', 4.5, N'Patrick Wilson', N'Phim rất hay thể loại rất phù hợp mọi lứa tuổi '),
	(N'Xứ Sở Các Nguyên Tố', CAST('2023-06-16' AS Date), 101, N'Hài, Hành Động, Kịch Tính', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/xusocacnguyento.jpg?alt=media&token=e66a0c12-f74a-43cf-807b-8959be043da2', 4.4, N'Peter Sohn', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'Tay Đua Kiệt Xuất', CAST('2023-07-10' AS Date), 128, N'Hài, Tình Cảm, Hành Động', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/tayduakietxuat.jpg?alt=media&token=2254e7f8-529f-403e-ace5-a391021db6cc', 4.0, N'Ross Venokur', N'Phim hài, rất xúc động tạo nên sự khác biệt của các thể loại tình cảm'),
	(N'Quý Công Tử', CAST('2023-06-20' AS Date), 96, N'Hành Động, Hài', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/quycongtu.jpg?alt=media&token=f76afc37-59e0-46f9-bdbe-8d43e707a18a', 4.2, N'Duy Joseph', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'Đảo Tử Thần', CAST('2023-07-05' AS Date), 90, N'Hài, Hành Động', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/daotuthan.jpg?alt=media&token=592559f3-9fe7-42e9-8326-50b0d36477b5', 4.6, N'Park Hoon-jung', N'Phim hành động xem kẻ hài hước '),
	(N'Sid Toang Rồi', CAST('2023-07-12' AS Date), 95, N'Hài', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/sidtoangroi.jpg?alt=media&token=ba3aef62-fed0-4e6f-bc8a-d5d793cefbc4', 4.0, N'Eiichirō Hasumi', N'Phim hài hước tạo nên sự vui vẻ '),
	(N'RUBY Thủy Quái Tuổi Teen', CAST('2023-06-28' AS Date), 110, N'Hài, Tình Cảm', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/rubythuyquaituoiteen.jpg?alt=media&token=42304cf7-5e5f-40de-94ee-f6e3aaed0ba2', 4.7, N'Eli Gonda', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'LIVESTREAM', CAST('2023-04-28' AS Date), 100, N'Hài, Hành Động', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/livestream.jpg?alt=media&token=e3c9064e-b229-41aa-8960-dfa22c02dae4', 4.6, N'Kirk DeMicco, Faryn Pearl', N'Phim hài hước, xen kẽ hành động'),
	(N'Bỗng Dưng Được Yêu', CAST('2023-07-05' AS Date), 100, N'Hành Động, Hài Hước', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/bongdungduocyeu.jpg?alt=media&token=4a71b72a-f725-4a37-ace4-ddeacc94664b', 4.1, N'Danny Đỗ', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'FLASH', CAST('2023-06-14' AS Date), 120, N'Tâm Lý, Tình Cảm', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/flash.jpg?alt=media&token=bf515c1d-a1cf-4e01-bb42-319bc7e5cbca', 4.8, N'Trần Ngọc Giàu', N'Phim hài, rất xúc động tạo nên sự khác biệt của các thể loại tình cảm'),
	(N'Ma Sơ Trục Quỷ', CAST('2023-06-28' AS Date), 101, N'Hài, Hành Động', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/masotrucquy.jpg?alt=media&token=d7c10aa7-86ba-4b07-984c-28cbebc4755a', 4.4, N'Hoàng Tuấn Cường', N'Phim hành động pha trộn hài hước vui vẻ'),
	(N'Cô Thành Trong Gương', CAST('2023-04-28' AS Date), 88, N'Hài, Tình Cảm', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/cothanhtrongguog.jpg?alt=media&token=dde6b93a-2d47-452b-adc5-f2af93d82d77', 4.3, N'Đức Thịnh', N'Phim hài nhưng không kém phần xúc động và sự kịch tính'),
	(N'Tà Chú Cấm', CAST('2023-06-21' AS Date), 87, N'Hài, Tình Cảm', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/tachucam.jpg?alt=media&token=a95376bd-1c26-4602-89f4-01048a02f55c', 4.6, N'Lê Lộc', N'Phim rất hay thể loại rất phù hợp mọi lứa tuổi')

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

INSERT INTO roles VALUES
	('ROLE_ADMIN',N'Quản trị web'),
	('ROLE_USER',N'Người dùng')

INSERT INTO users VALUES 
	(N'Trần Hữu Đang',N'dangth', 'Nam', '$2a$10$AR78OxmWNlFMnmFlv.XWFe2TECixCdfV.2K9G4yrmQ1irWXvxcL72', N'0917288723', N'dangthpc04349@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/dangth.jpg?alt=media&token=e223770c-06cb-448e-9025-43000f55d764', 1, 1, CAST('9-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Lê Bích Vi', N'vilb', 'Nữ', '$2a$10$SvchmABRVVZjeLgOW4Dez.q7T1kcybCdiQF70DHKNs.nX30vmYLVi', N'0178296424', N'vilbpc04354@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/vilb.jpg?alt=media&token=83641b31-7ea9-432d-bd6b-4dd0f5e9062f', 1, 1, CAST('6-2-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Phùng Quốc Vinh',N'vinhpq',  'Nam', '$2a$10$aF6y9hGg06.We5mXYua13eM/N4o2wq0UZSD2JgC0PVja.1x1chXjS', N'0862738927', N'vinhpqpc04338@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/vinhpq.jpg?alt=media&token=635b97b6-bdf4-49b5-ae07-a802c17a979e', 1, 1, CAST('11-15-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Đoàn Hiệp Sỹ',N'sydh', 'Nam', '$2a$10$DYKf7ahE.Feac9JEy8exP.hMYXtaI5aayfeYua0ZCGVV0RXvu5.Gy', N'0836452473', N'sydhpc04388@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/sydh.jpg?alt=media&token=f907c8e9-4712-4448-b7a9-1d9df8f9b053', 1, 1, CAST('4-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Nguyễn Khánh Đan',N'dannk', 'Nữ', '$2a$10$CRFxFV1oJiYT0rTa3STe.ubKEz1V59HrdOSCl1OA6uVG2xYretjQ6', N'0924637483', N'dannkpc04351@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/dannk.jpg?alt=media&token=2cb34557-c380-4095-8a10-8a211add0940', 1, 1, CAST('11-7-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Châu Hoài Phúc', N'phucch','Nam', '$2a$10$pT5QFvN2Ha5jiOCtZTK.ZOY0dS5MKC/K31S2jyg2Ln978nju1BxCq', N'0918093162', N'phucchpc04191@fpt.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/phucch.jpg?alt=media&token=8ee61c10-23b1-41a5-97ed-b1e0e6d894ed', 1, 1, CAST('11-2-2003' AS DATE), CAST('7-22-2023' AS DATE), NULL),
	(N'Quách Hữu Nghĩa',N'nghiahq', 'Nam', N'$2a$10$WzBhlbBVtJxyafSiM1os9.4S0tDkSmoYgWY/om0Ma7dBBz9jlpUUq', N'012346789', N'nghiaqh@fe.edu.vn', 'https://firebasestorage.googleapis.com/v0/b/davitickets-2e627.appspot.com/o/thaynghia.jpg?alt=media&token=9fc95aed-1dfe-4b87-8ebe-2903ffd50678', 1, 1, CAST('1-1-1990' AS DATE), CAST('7-22-2023' AS DATE), NULL)

INSERT INTO user_role VALUES
	(1,1),
	(2 ,2),
	(3 ,2),
	(4 ,2),
	(5 ,2),
	(6 ,2),
	(7 ,1),
	(7 ,2)

INSERT INTO booking VALUES 
	(1, 27, 1, CAST('2023-06-17' AS Date)),
	(1, 28, 2, CAST('2023-06-17' AS Date)),
	(1, 29, 2, CAST('2023-06-17' AS Date)),
	(1, 30, 2, CAST('2023-06-17' AS Date)),
	(2, 38, 3, CAST('2023-07-17' AS Date)), --ghe vip = gia goc + 10 ngan
	(1, 39, 4, CAST('2023-07-17' AS Date)), --ghe vip = gia goc + 10 ngan
	(2, 51, 5, CAST('2023-07-17' AS Date)) --ghe vip = gia goc + 10 ngan

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

--xem ghế
/*	SELECT * FROM seat
	SELECT vip, row_symbol, seat_num FROM booking INNER JOIN seat ON booking.seat_id = seat.seat_id
*/

--Những vé chưa thanh toán của user
/*
	SELECT * FROM booking WHERE booking.booking_id NOT IN (SELECT booking_id FROM payment) AND userid = '2' 

	SELECT * FROM booking WHERE booking.booking_id IN (SELECT booking_id FROM payment) AND userid = '2' 
*/


--27/7/202333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333
/*
--Những ghế của lịch chiếu đã được đặt
SELECT DISTINCT vip, row_symbol, seat_num FROM seat 
	INNER JOIN booking ON booking.seat_id = seat.seat_id 
	INNER JOIN showtime ON showtime.movie_id = booking.booking_id
	INNER JOIN payment ON payment.booking_id = booking.booking_id
	WHERE booking.booking_id IN (SELECT booking_id FROM payment) 
	AND booking.showtime_id ='1'

SELECT * FROM movie
SELECT * FROM seat
SELECT * FROM showtime
SELECT * FROM booking
SELECT * FROM payment


--Ghế của người dùng đã đặt tại lịch chiếu 2
SELECT row_symbol, seat_num FROM booking INNER JOIN seat ON booking.seat_id = seat.seat_id WHERE booking.booking_id IN (SELECT booking_id FROM payment) AND userid = '2' AND booking.showtime_id = '2'

--TOP 4 phim thịnh hành trong tháng này
SELECT TOP 4 COUNT(movie.movie_id), movie.title_movie, movie.release_date, movie.genre FROM movie 
	INNER JOIN showtime ON showtime.movie_id = movie.movie_id
	INNER JOIN booking ON booking.booking_id = showtime.showtime_id
	INNER JOIN payment ON payment.booking_id = booking.booking_id
	WHERE MONTH(payment.payment_date) = '7'
	GROUP BY  movie.title_movie, movie.release_date, movie.genre
	ORDER BY COUNT(movie.movie_id)
	DESC

--Lịch sử thanh toán của user có id = 2
SELECT * FROM booking INNER JOIN payment ON payment.booking_id = booking.booking_id WHERE booking.userid = '2'

*/

select * from roles
select * from users
select * from user_role

SELECT r.name, r.role_des as name FROM users u INNER JOIN user_role ur ON u.userid = ur.userid 
						INNER JOIN roles r ON r.role_id =ur.role_id  and u.email='nghiaqh@fe.edu.vn'