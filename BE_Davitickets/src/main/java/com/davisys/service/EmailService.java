package com.davisys.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.davisys.entity.Showtime;
import com.davisys.entity.Users;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EmailService {

	private final JavaMailSender mailSender;

	public EmailService(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void sendEmailWithImage(List<byte[]> imageBytesList, Users user, Showtime showtime, String seat, int tickets,
			int money) throws MessagingException, IOException {
		List<String> tempImagePaths = new ArrayList<>();
		String[] s = seat.split(",");
		// Save the image to a temporary file
		for (int i = 0; i < imageBytesList.size(); i++) {
			byte[] imageBytes = imageBytesList.get(i);

			// Save the image to a temporary file
			String tempImagePath = "temp_image" + i + ".png";
			try (FileOutputStream fos = new FileOutputStream(tempImagePath)) {
				fos.write(imageBytes);
			}

			tempImagePaths.add(tempImagePath);

			// Send email with the image embedded in HTML
			sendHtmlEmail(tempImagePath, user, showtime, s[i], tickets, money);
			if (tempImagePath != null) {
				java.io.File tempFile = new java.io.File(tempImagePath);
				if (tempFile.exists()) {
					tempFile.delete();
				}
			}
		}

		// Delete the temporary image file
		// (You can also store the image in a different location or avoid creating a
		// file altogether if not needed)

	}

	public void sendHtmlEmail(String attachmentFilePath, Users user, Showtime showtime, String seat, int tickets,
			int money) throws MessagingException {
		String fromEmail = "daviticket@gmail.com";
		String toEmail = user.getEmail();
		String subject = "HTML Email with Image";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd-MM-YYYY");
		String body = "<!DOCTYPE html>\r\n" + "<html lang=\"en\">\r\n" + "\r\n" + "<head>\r\n"
				+ "    <meta charset=\"UTF-8\">\r\n"
				+ "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n"
				+ "    <title>Document</title>\r\n" + "    <style>\r\n" + "        body {\r\n"
				+ "            background-color: #e2e1e0;\r\n" + "            font-family: Open Sans, sans-serif;\r\n"
				+ "            font-size: 100%;\r\n" + "            font-weight: 400;\r\n"
				+ "            line-height: 1.4;\r\n" + "            color: #000;\r\n" + "        }\r\n" + "\r\n"
				+ "        table {\r\n" + "            max-width: 670px;\r\n"
				+ "            margin: 50px auto 10px;\r\n" + "            background-color: #fff;\r\n"
				+ "            padding: 50px;\r\n" + "            -webkit-border-radius: 3px;\r\n"
				+ "            -moz-border-radius: 3px;\r\n" + "            border-radius: 3px;\r\n"
				+ "            -webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, .12), 0 1px 2px rgba(0, 0, 0, .24);\r\n"
				+ "            -moz-box-shadow: 0 1px 3px rgba(0, 0, 0, .12), 0 1px 2px rgba(0, 0, 0, .24);\r\n"
				+ "            box-shadow: 0 1px 3px rgba(0, 0, 0, .12), 0 1px 2px rgba(0, 0, 0, .24);\r\n"
				+ "            border-top: solid 10px green;\r\n" + "        }\r\n" + "\r\n" + "        .th1 {\r\n"
				+ "            text-align: left;\r\n" + "            width: 30%;\r\n" + "        }\r\n" + "\r\n"
				+ "        .th2 {\r\n" + "\r\n" + "            font-weight: 700;\r\n"
				+ "            font-size: 20px;\r\n" + "        }\r\n" + "\r\n" + "        .td1 {\r\n"
				+ "            text-align: right;\r\n" + "            font-weight: 400;\r\n"
				+ "            height: 35px;\r\n" + "        }\r\n" + "\r\n" + "        h3 {\r\n"
				+ "            font-weight: 800;\r\n" + "            font-size: 23px;\r\n" + "        }\r\n" + "\r\n"
				+ "        h4 {\r\n" + "            margin-bottom: 10px;\r\n" + "            margin-top: 0;\r\n"
				+ "            color: #EE5007;\r\n" + "        }\r\n" + "\r\n" + "        img {\r\n"
				+ "            border: 1px solid rgb(186, 186, 186);\r\n" + "            border-radius: 3px;\r\n"
				+ "        }\r\n" + "\r\n" + "        h5 {\r\n" + "            margin-bottom: 5px;\r\n"
				+ "        }\r\n" + "\r\n" + "        /* .tr1 {\r\n" + "            display: flex;\r\n"
				+ "            justify-content: center;\r\n" + "            align-items: center;\r\n"
				+ "        } */\r\n" + "\r\n" + "        .td2 {\r\n" + "            text-align: center;\r\n"
				+ "        }\r\n" + "\r\n" + "        .p1 {\r\n" + "            font-size: 14px;\r\n"
				+ "            margin: 0 0 6px 0;\r\n" + "        }\r\n" + "\r\n" + "        .span1 {\r\n"
				+ "            font-weight: bold;\r\n" + "            display: inline-block;\r\n"
				+ "            min-width: 150px;\r\n" + "        }\r\n" + "\r\n" + "        .span2 {\r\n"
				+ "            font-weight: bold;\r\n" + "            display: inline-block;\r\n"
				+ "            min-width: 146px\r\n" + "        }\r\n" + "\r\n" + "        .td3 {\r\n"
				+ "            height: 35px;\r\n" + "        }\r\n" + "\r\n" + "        .td4 {\r\n"
				+ "            width: 50%;\r\n" + "            padding: 20px 20px 0 0;\r\n"
				+ "            vertical-align: top;\r\n" + "\r\n" + "        }\r\n" + "\r\n" + "        .td5 {\r\n"
				+ "            padding: 15px;\r\n" + "            vertical-align: top;\r\n" + "\r\n" + "        }\r\n"
				+ "\r\n" + "        .td6 {\r\n" + "\r\n" + "            padding: 20px 0 0 0;\r\n"
				+ "            vertical-align: top\r\n" + "        }\r\n" + "\r\n" + "        td.td6 {\r\n"
				+ "            width: 40%;\r\n" + "        }\r\n" + "\r\n" + "        .p2 {\r\n"
				+ "            margin: 0 0 10px 0;\r\n" + "            padding: 0;\r\n"
				+ "            font-size: 14px;\r\n" + "        }\r\n" + "\r\n" + "        .span3 {\r\n"
				+ "            display: block;\r\n" + "            font-weight: bold;\r\n"
				+ "            font-size: 13px;\r\n" + "\r\n" + "        }\r\n" + "\r\n" + "        .tg {\r\n"
				+ "            margin-top: 6px;\r\n" + "            font-size: 15px;\r\n"
				+ "            font-weight: 500;\r\n" + "        }\r\n" + "\r\n" + "        tbody.info-ticket {\r\n"
				+ "            border: 1px solid black;\r\n" + "}\r\n" + "    </style>\r\n" + "</head>\r\n" + "\r\n"
				+ "<body>\r\n" + "\r\n" + "    <table>\r\n" + "        <thead>\r\n" + "            <tr>\r\n"
				+ "                <td style=\"width: 100%;text-align: center;\" colspan=\"3\">\r\n"
				+ "                    <h3>Thông tin đặt vé</h3>\r\n" + "                </td>\r\n" + "\r\n"
				+ "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td class=\"td4\" colspan=\"3\">\r\n"
				+ "                    <p class=\"p2\">Xin chào <b>" + user.getFull_name() + ",</b>\r\n"
				+ "                    </p>\r\n" + "                    <p class=\"p2\">\r\n"
				+ "                        Cảm ơn bạn đã sử dụng dịch vụ của DaviTickets!\r\n"
				+ "                    </p>\r\n" + "                    <p class=\"p2\">\r\n"
				+ "                        DaviTickets xác nhận bạn đã đặt vé xem phim <b>"
				+ showtime.getMovie().getTitle_movie() + "</b> thành công lúc\r\n" + "\r\n"
				+ "                    </p>\r\n" + "                    <p class=\"p2\">\r\n"
				+ "                        <span><b>13:56:40</b></span> <span><b>" + sdfDate.format(date) + "</b></span>\r\n"
				+ "                    </p>\r\n" + "                </td>\r\n" + "            </tr>\r\n"
				+ "        </thead>\r\n" + "        <tbody class=\"info-ticket\">\r\n"
				+ "            <tr class=\"tr1\">\r\n" + "                <td class=\"td2\" colspan=\"3\">\r\n"
				+ "                    <h5>Mã đặt vé</h5>\r\n" + "                    <h4></h4>\r\n"
				+ "                    <img width=\"150px\"\r\n" + "                        src=\"cid:image\"\r\n"
				+ "                        alt=\"\">\r\n" + "                </td>\r\n" + "\r\n"
				+ "            </tr>\r\n" + "           \r\n" + "            <tr>\r\n"
				+ "                <td class=\"td2\" colspan=\"3\">\r\n"
				+ "                    <h5>Thời gian chiếu</h5>\r\n" + "                    <p class=\"tg\">"
				+ sdf.format(showtime.getStart_time()) + " " + sdfDate.format(showtime.getShowdate()) + "</p>\r\n"
				+ "                </td>\r\n" + "\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td class=\"td4\" colspan=\"3\">\r\n"
				+ "                    <p class=\"p2\"><span class=\"span3\">Phim</span>\r\n"
				+ "                       " + showtime.getMovie().getTitle_movie() + "\r\n"
				+ "                    </p>\r\n" + "                </td>\r\n" + "            </tr>\r\n" + "\r\n"
				+ "            <tr>\r\n" + "                <td class=\"td6\">\r\n"
				+ "                    <p class=\"p2\"><span class=\"span3\">Số ghế</span>\r\n"
				+ "                        " + seat + "\r\n" + "                    </p>\r\n"
				+ "                </td>\r\n" + "                <td class=\"td6\">\r\n"
				+ "                    <p class=\"p2\"><span class=\"span3\">Phòng chiếu</span>\r\n"
				+ "                       Phòng chiếu " + showtime.getRoom().getRoom_name() + "\r\n"
				+ "                    </p>\r\n" + "                </td>\r\n"
				+ "                <td class=\"td6\">\r\n"
				+ "                    <p class=\"p2\"><span class=\"span3\">Số vé</span>\r\n"
				+ "                        " + tickets + "\r\n" + "                    </p>\r\n"
				+ "                </td>\r\n" + "\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td class=\"td4\" colspan=\"3\">\r\n"
				+ "                    <p class=\"p2\"><span class=\"span3\">Địa chỉ :</span>\r\n"
				+ "                        The 80's Coffe\r\n" + "                    </p>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"2\">\r\n"
				+ "                    <p style=\"font-weight: 900;font-size:20px; color: red;\">Tổng tiền :</p>\r\n"
				+ "                </td>\r\n"
				+ "                <td style=\"font-weight: 800; color: red;font-size:20px;\">" + money + "</td>\r\n"
				+ "            </tr> </tbody>\r\n" + "        <tbody>\r\n" + "            <tr>\r\n"
				+ "                <td colspan=\"3\" style=\"font-size:14px;padding:10px 15px 0 0px;\">\r\n"
				+ "                    <strong style=\"display:block;margin:0 0 10px 0;font-size: 18px;\">Thông tin người đặt vé</strong>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td class=\"col-sm-3\">\r\n"
				+ "                    <p style=\"font-size:15px;\">Họ và tên :</p>\r\n" + "                </td>\r\n"
				+ "                <td class=\"col-sm-3\" style=\" font-size:15px;\">" + user.getFull_name() + "\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n"
				+ "                    <p style=\"font-size:15px;\">Số điện thoại :</p>\r\n"
				+ "                </td>\r\n" + "                <td style=\" font-size:15px;\" colspan=\"2\">"
				+ user.getPhone() + "\r\n" + "                </td>\r\n" + "            </tr>\r\n"
				+ "            <tr>\r\n" + "                <td>\r\n"
				+ "                    <p style=\"font-size:15px;\">Email :</p>\r\n" + "                </td>\r\n"
				+ "                <td style=\"font-size:15px; \" colspan=\"2\">" + user.getEmail() + "\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "        </tbody>\r\n" + "        <tbody>\r\n"
				+ "            <tr>\r\n"
				+ "                <td colspan=\"3\" style=\"font-size:14px;padding:10px 15px 0 0px;\">\r\n"
				+ "                    <strong style=\"display:block;margin:0 0 10px 0;font-size: 18px;\">Liên hệ hỗ trợ</strong>\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n" + "                    <p style=\"font-size:15px;\">Email: </p>\r\n"
				+ "                </td>\r\n"
				+ "                <td style=\"font-size:15px;\" colspan=\"2\">daviticket@gmail.com\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n" + "                    <p style=\"font-size:15px;\">Địa chỉ :</p>\r\n"
				+ "                </td>\r\n" + "                <td style=\" font-size:15px;\" colspan=\"2\">\r\n"
				+ "                    Đ. Số 22, Thường Thạnh, Cái Răng, Cần Thơ 900000, Việt Nam, Can Tho, Vietnam\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "            <tr>\r\n"
				+ "                <td>\r\n" + "                    <p style=\"font-size:15px;\">Website :</p>\r\n"
				+ "                </td>\r\n"
				+ "                <td style=\"font-size:15px; \" colspan=\"2\">davitickets.com\r\n"
				+ "                </td>\r\n" + "            </tr>\r\n" + "        </tbody>\r\n" + "    </table>\r\n"
				+ "</body>\r\n" + "\r\n" + "</html>";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		// Set email properties
		helper.setFrom(fromEmail);
		helper.setTo(toEmail);
		helper.setSubject(subject);

		// Add the HTML body with the image attachment (embedded)
		helper.setText(body, true);
		DataSource source = new FileDataSource(attachmentFilePath);
		helper.addInline("image", source);

		// Send the email
		mailSender.send(message);

		System.out.println("Email sent successfully!");
	}
}
