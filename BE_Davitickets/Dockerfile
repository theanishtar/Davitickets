# Sử dụng một image base có Java và các công cụ cần thiết đã cài đặt sẵn
FROM java:8

EXPOSE 8080

ADD target/davitickets.jar davitickets.jar

# Chạy ứng dụng Spring Boot khi container được khởi động
CMD ["java", "-jar", "davitickets.jar"]
