--Thêm account admin 
SET IDENTITY_INSERT [dbo].[accounts] ON;

INSERT INTO [dbo].[accounts]
      ([is_account_non_locked]
      ,[is_enabled]
      ,[verification_code]
      ,[id]
      ,[verification_code_expiration_date]
      ,[email]
      ,[password])
VALUES
      (1, -- is_account_non_locked
       1, -- is_enabled
       NULL, -- verification_code
       1, -- id, sử dụng giá trị hợp lệ cho kiểu bigint
       NULL, -- verification_code_expiration_date
       'admin@gmail.com', -- email
       '$2a$12$cF6FY5mvUbwM36UHN0Fgoe/80NPK.GbJSFMz/vfsBJTYf4L5drv7S') -- password
GO

SET IDENTITY_INSERT [dbo].[accounts] OFF;

INSERT INTO [dbo].[employees]
      ([birthday]
      ,[account_id]
      ,[created_at]
      ,[id]
      ,[address_detail]
      ,[avatar]
      ,[city]
      ,[district]
      ,[gender]
      ,[name]
      ,[phone]
      ,[status]
      ,[ward])
VALUES
      ('1990-01-01',          -- birthday
       1,                    -- account_id
       GETDATE(),            -- created_at
       1,                    -- id, giá trị hợp lệ cho kiểu bigint
       '123 Main Street',    -- address_detail
       'avatar.png',         -- avatar
       'Ha Noi',             -- city
       'Cau Giay',           -- district
       'Male',               -- gender
       'Nguyen Van A',       -- name
       '0123456789',         -- phone
       'Active',             -- status
       'Dich Vong Hau')      -- ward
GO

-- Thêm 10 giá trị khác nhau vào bảng [color]
INSERT INTO [dbo].[color] ([color_name])
VALUES
    ('blue'),
    ('green'),
    ('yellow'),
    ('purple'),
    ('red'),
    ('orange'),
    ('black'),
    ('white'),
    ('grey'),
    ('pink')
GO

-- Thêm 10 giá trị khác nhau vào bảng [categories]
INSERT INTO [dbo].[categories] ([category_name])
VALUES
    ('SmartPhone'),
    ('Laptop'),
    
GO

-- Thêm 10 giá trị khác nhau vào bảng [brand]
INSERT INTO [dbo].[brand] ([brand_name])
VALUES
    ('Samsung'),
    ('Iphone'),
    ('Lenovo'),
    ('Xiaomi'),
    ('Sony'),
    ('LG'),
    ('HP'),
    ('Dell'),
    ('Asus'),
    ('Acer')
GO

-- Thêm 10 giá trị khác nhau vào bảng [suppliers]
INSERT INTO [dbo].[suppliers] ([supplier_address], [supplier_name])
VALUES
    ('Ha Noi', 'Apple Store'),
    ('Sai Gon', 'Samsung Store'),
    ('Thanh Hoa', 'Xiaomi Store'),
    ('Da Nang', 'Sony Store'),
    ('Hai Phong', 'LG Store'),
    ('Can Tho', 'HP Store'),
    ('Vinh', 'Dell Store'),
    ('Hue', 'Asus Store'),
    ('Nha Trang', 'Acer Store'),
    ('Quang Ninh', 'Lenovo Store')
GO

-- Thêm 10 giá trị khác nhau vào bảng [product_tech]
INSERT INTO [dbo].[product_tech] ([memory], [ram], [size])
VALUES
    ('128', '4', '6.1'),
    ('64', '3', '5.8'),
    ('512', '8', '6.7'),
    ('256', '8', '6.4'),
    ('128', '6', '6.0'),
    ('32', '2', '4.7'),
    ('1000', '12', '7.0'),
    ('512', '12', '6.8'),
    ('256', '4', '6.5'),
    ('128', '3', '6.3'),
    ('64', '4', '5.5'),
    ('128', '6', '6.2'),
    ('256', '8', '6.3'),
    ('512', '12', '7.1'),
    ('1000', '16', '7.5'),
    ('32', '3', '4.5'),
    ('64', '4', '5.9'),
    ('256', '12', '6.6'),
    ('512', '16', '7.3'),
    ('128', '8', '6.4')
GO

-- Thêm 10 sản phẩm vào bảng [product]
INSERT INTO [dbo].[product]
           ([brand_id], [category_id], [supplier_id], [product_description], [product_image], [product_name])
     VALUES
           (2, 1, 1, 'Latest model Smartphone', 'samsung-galaxy-s23.webp', 'Samsung Galaxy S23'),
           (3, 2, 2, 'High performance Laptop', 'lenovo-thinkpad-x1.webp', 'Lenovo ThinkPad X1'),
           (4, 1, 3, 'Innovative Smartphone', 'xiaomi-mi-11.webp', 'Xiaomi Mi 11'),
           (1, 2, 1, 'Advanced Smartphone', 'iphone-13-pro-max.webp', 'iPhone 13 Pro Max'),
           (2, 1, 2, 'Compact Smartphone', 'samsung-galaxy-z-flip3.webp', 'Samsung Galaxy Z Flip3'),
           (3, 2, 3, 'Powerful Laptop', 'lenovo-legion-5.webp', 'Lenovo Legion 5'),
           (4, 1, 1, 'Stylish Smartphone', 'xiaomi-redmi-note-10.webp', 'Xiaomi Redmi Note 10'),
           (1, 2, 2, 'Affordable Smartphone', 'iphone-se-2022.webp', 'iPhone SE 2022'),
           (2, 1, 3, 'Durable Smartphone', 'samsung-galaxy-xcover-5.webp', 'Samsung Galaxy XCover 5'),
           (3, 2, 1, 'Versatile Laptop', 'lenovo-yoga-9i.webp', 'Lenovo Yoga 9i'),
           (4, 1, 2, 'Budget Smartphone', 'xiaomi-poco-x3.webp', 'Xiaomi Poco X3'),
           (1, 2, 3, 'Premium Smartphone', 'iphone-14-pro.webp', 'iPhone 14 Pro'),
           (2, 1, 1, 'Foldable Smartphone', 'samsung-galaxy-fold3.webp', 'Samsung Galaxy Fold3'),
           (3, 2, 2, 'Gaming Laptop', 'lenovo-ideapad-gaming-3.webp', 'Lenovo IdeaPad Gaming 3'),
           (4, 1, 3, 'High-end Smartphone', 'xiaomi-mi-mix-4.webp', 'Xiaomi Mi Mix 4'),
           (2, 1, 1, 'Ultra Smartphone', 'samsung-galaxy-ultra.webp', 'Samsung Galaxy Ultra'),
           (1, 2, 1, 'Latest Laptop', 'macbook-pro-2022.webp', 'MacBook Pro 2022'),
           (3, 1, 2, 'Affordable Phone', 'lenovo-phone-a5.webp', 'Lenovo Phone A5'),
           (4, 2, 3, 'Lightweight Laptop', 'xiaomi-mi-laptop.webp', 'Xiaomi Mi Laptop'),
           (1, 1, 2, 'Top Smartphone', 'iphone-top-model.webp', 'iPhone Top Model')
GO

-- Thêm các loại sản phẩm và giá tiền vào bảng [product_typies]
INSERT INTO [dbo].[product_typies]
      ([product_type_price]
      ,[product_type_quantity]
      ,[product_type_status]
      ,[color_id]
      ,[product_id]
      ,[tech_id]
      )
VALUES
-- Product ID 1
(999, 10, 'available', 1, 1, 1),
(1049, 5, 'available', 2, 1, 2),
(1099, 15, 'available', 3, 1, 3),

-- Product ID 2
(1299, 7, 'available', 1, 2, 4),
(1349, 6, 'available', 2, 2, 5),
(1399, 8, 'available', 3, 2, 6),

-- Product ID 3
(849, 12, 'available', 1, 3, 7),
(899, 10, 'available', 2, 3, 8),
(949, 9, 'available', 3, 3, 9),

-- Product ID 4
(1099, 20, 'available', 1, 4, 10),
(1149, 18, 'available', 2, 4, 1),
(1199, 25, 'available', 3, 4, 2),

-- Product ID 5
(999, 5, 'available', 1, 5, 3),
(1049, 7, 'available', 2, 5, 4),
(1099, 6, 'available', 3, 5, 5),

-- Product ID 6
(1399, 14, 'available', 1, 6, 6),
(1449, 12, 'available', 2, 6, 7),
(1499, 15, 'available', 3, 6, 8),

-- Product ID 7
(899, 8, 'available', 1, 7, 9),
(949, 7, 'available', 2, 7, 10),
(999, 9, 'available', 3, 7, 1),

-- Product ID 8
(699, 22, 'available', 1, 8, 2),
(749, 18, 'available', 2, 8, 3),
(799, 25, 'available', 3, 8, 4),

-- Product ID 9
(1049, 11, 'available', 1, 9, 5),
(1099, 13, 'available', 2, 9, 6),
(1149, 14, 'available', 3, 9, 7),

-- Product ID 10
(1349, 9, 'available', 1, 10, 8),
(1399, 10, 'available', 2, 10, 9),
(1449, 8, 'available', 3, 10, 10),

-- Product ID 11
(649, 5, 'available', 1, 11, 1),
(699, 6, 'available', 2, 11, 2),
(749, 4, 'available', 3, 11, 3),

-- Product ID 12
(1199, 16, 'available', 1, 12, 4),
(1249, 18, 'available', 2, 12, 5),
(1299, 15, 'available', 3, 12, 6),

-- Product ID 13
(1499, 20, 'available', 1, 13, 7),
(1549, 19, 'available', 2, 13, 8),
(1599, 22, 'available', 3, 13, 9),

-- Product ID 14
(899, 11, 'available', 1, 14, 10),
(949, 10, 'available', 2, 14, 1),
(999, 13, 'available', 3, 14, 2),

-- Product ID 15
(799, 9, 'available', 1, 15, 3),
(849, 8, 'available', 2, 15, 4),
(899, 10, 'available', 3, 15, 5)
GO




--TRIGGER
CREATE TRIGGER trg_UpdateProductStatus
ON [SWP391Project].[dbo].[product_typies]
AFTER UPDATE
AS
BEGIN
    -- Cập nhật trạng thái thành 'close' nếu số lượng bằng 0
    UPDATE p
    SET p.product_type_status = 'close'
    FROM [SWP391Project].[dbo].[product_typies] p
    INNER JOIN inserted i ON p.product_type_id = i.product_type_id
    WHERE i.product_type_quantity <= 0;
END

