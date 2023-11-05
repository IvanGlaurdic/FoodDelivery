-- Users Table
CREATE TABLE Users (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    FirstName NVARCHAR(100) NOT NULL,
    LastName NVARCHAR(100) NOT NULL,
    Username NVARCHAR(100) UNIQUE NOT NULL,
    Email NVARCHAR(150) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    PhoneNumber NVARCHAR(15) NOT NULL,
    Role NVARCHAR(20) NOT NULL,
    CONSTRAINT CHK_Role CHECK (Role IN ('Admin', 'Customer', 'Owner'))
);

-- Restaurants Table
CREATE TABLE Restaurants (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    Name NVARCHAR(150) NOT NULL,
    Address NVARCHAR(255) NOT NULL,
    ContactNumber NVARCHAR(15) NOT NULL,
    Rating FLOAT DEFAULT 0,
    OwnerID UNIQUEIDENTIFIER,
    CONSTRAINT FK_Restaurants_Users FOREIGN KEY (OwnerID) REFERENCES Users(ID)
);

-- Addresses Table
CREATE TABLE Addresses (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    Street VARBINARY(MAX) NOT NULL,
    City VARBINARY(MAX) NOT NULL,
    Province VARBINARY(MAX) NOT NULL,
    Country VARBINARY(MAX) NOT NULL,
    PostalCode VARBINARY(128) NOT NULL,
    Latitude FLOAT,
    Longitude FLOAT,
    UserID UNIQUEIDENTIFIER,
    RestaurantID UNIQUEIDENTIFIER,
    CONSTRAINT FK_Addresses_Users FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE,
    CONSTRAINT FK_Addresses_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID) ON DELETE CASCADE
);

-- MenuItems Table
CREATE TABLE MenuItems (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    RestaurantID UNIQUEIDENTIFIER,
    Name NVARCHAR(100) NOT NULL,
    Description NVARCHAR(MAX),
    Price DECIMAL(10,2) NOT NULL,
    ImageURL NVARCHAR(MAX),
    Category NVARCHAR(100),
    CONSTRAINT FK_MenuItems_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID)
);

-- Orders Table
CREATE TABLE Orders (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    UserID UNIQUEIDENTIFIER,
    RestaurantID UNIQUEIDENTIFIER,
    OrderDate DATE NOT NULL,
    ScheduledDeliveryDate DATE,
    TotalAmount DECIMAL(10,2) NOT NULL,
    Status NVARCHAR(20) NOT NULL,
    CONSTRAINT CHK_OrderStatus CHECK (Status IN ('Placed', 'In Progress', 'In Delivery', 'Delivered', 'Cancelled')),
    CONSTRAINT FK_Orders_Users FOREIGN KEY (UserID) REFERENCES Users(ID),
    CONSTRAINT FK_Orders_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID)
);

-- OrderItems Table
CREATE TABLE OrderItems (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    OrderID UNIQUEIDENTIFIER,
    MenuItemID UNIQUEIDENTIFIER,
    Quantity INT NOT NULL,
    CONSTRAINT FK_OrderItems_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID),
    CONSTRAINT FK_OrderItems_MenuItems FOREIGN KEY (MenuItemID) REFERENCES MenuItems(ID)
);

-- Reviews Table
CREATE TABLE Reviews (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    OrderID UNIQUEIDENTIFIER,
    Rating INT NOT NULL,
    Comment NVARCHAR(MAX),
    CONSTRAINT FK_Reviews_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID)
);

-- Notifications Table
CREATE TABLE Notifications (
    ID UNIQUEIDENTIFIER PRIMARY KEY DEFAULT NEWID(),
    OrderID UNIQUEIDENTIFIER,
    Message NVARCHAR(MAX) NOT NULL,
    Timestamp DATETIME DEFAULT GETDATE(),
    CONSTRAINT FK_Notifications_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID)
);

-- [Previous Table Creation statements]

GO

CREATE TRIGGER tr_update_restaurant_rating ON Reviews
AFTER INSERT
AS
BEGIN
    DECLARE @avgRating FLOAT;
    DECLARE @restaurantID UNIQUEIDENTIFIER;

    -- Get RestaurantID using the inserted OrderID
    SELECT @restaurantID = O.RestaurantID
    FROM INSERTED I
    INNER JOIN Orders O ON I.OrderID = O.ID;

    -- Calculate average rating for the restaurant
    SELECT @avgRating = AVG(R.Rating)
    FROM Reviews R
    INNER JOIN Orders O ON R.OrderID = O.ID
    WHERE O.RestaurantID = @restaurantID;

    -- Update the restaurant rating
    UPDATE Restaurants SET Rating = @avgRating WHERE ID = @restaurantID;
END;


GO

CREATE TRIGGER tr_check_address_before_insert ON Addresses
AFTER INSERT
AS
BEGIN
    IF EXISTS (SELECT 1 FROM INSERTED WHERE (UserID IS NULL AND RestaurantID IS NULL) OR (UserID IS NOT NULL AND RestaurantID IS NOT NULL))
    BEGIN
        THROW 51000, 'Either UserID or RestaurantID should be set, but not both.', 1;
    END
END;

GO

CREATE TRIGGER tr_check_address_before_update ON Addresses
AFTER UPDATE
AS
BEGIN
    IF EXISTS (SELECT 1 FROM INSERTED WHERE (UserID IS NULL AND RestaurantID IS NULL) OR (UserID IS NOT NULL AND RestaurantID IS NOT NULL))
    BEGIN
        THROW 51000, 'Either UserID or RestaurantID should be set, but not both.', 1;
    END
END;

GO
