-- Users Table
CREATE TABLE Users (
   ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
   FirstName VARCHAR(100) NOT NULL,
   LastName VARCHAR(100) NOT NULL,
   Username VARCHAR(100) UNIQUE NOT NULL,
   Email VARCHAR(150) UNIQUE NOT NULL,
   Password VARCHAR(255) NOT NULL,
   PhoneNumber VARCHAR(15) NOT NULL,
   Role VARCHAR(100) NOT NULL -- Changed type from INT to VARCHAR to match the diagram
);

-- Restaurants Table
CREATE TABLE Restaurants (
     ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
     Name VARCHAR(150) NOT NULL,
     ContactNumber VARCHAR(15) NOT NULL,
     Rating FLOAT DEFAULT 0,
     OwnerID CHAR(36),
     CONSTRAINT FK_Restaurants_Users FOREIGN KEY (OwnerID) REFERENCES Users(ID)
);

-- Addresses Table
CREATE TABLE Addresses (
    ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    Street BLOB NOT NULL,
    City BLOB NOT NULL,
    Province BLOB NOT NULL,
    Country BLOB NOT NULL,
    PostalCode BLOB NOT NULL,
    Latitude FLOAT,
    Longitude FLOAT,
    UserID CHAR(36),
    RestaurantID CHAR(36),
    CONSTRAINT FK_Addresses_Users FOREIGN KEY (UserID) REFERENCES Users(ID) ON DELETE CASCADE,
    CONSTRAINT FK_Addresses_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID) ON DELETE CASCADE
);

-- MenuItems Table
CREATE TABLE MenuItems (
   ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
   RestaurantID CHAR(36) NOT NULL, -- Added NOT NULL constraint as per the diagram
   Name VARCHAR(100) NOT NULL,
   Description TEXT,
   Price DECIMAL(10,2) NOT NULL,
   ImageURL TEXT,
   Category VARCHAR(100) NOT NULL, -- Added NOT NULL constraint as per the diagram
   CONSTRAINT FK_MenuItems_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID)
);

-- Orders Table
CREATE TABLE Orders (
    ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    UserID CHAR(36),
    RestaurantID CHAR(36),
    OrderDate DATE NOT NULL,
    ScheduledDeliveryDate DATE,
    TotalAmount DECIMAL(10,2) NOT NULL,
    Status CHAR(36) NOT NULL,
    CONSTRAINT FK_Orders_Users FOREIGN KEY (UserID) REFERENCES Users(ID),
    CONSTRAINT FK_Orders_Restaurants FOREIGN KEY (RestaurantID) REFERENCES Restaurants(ID)
);

-- OrderItems Table
CREATE TABLE OrderItems (
    ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    OrderID CHAR(36),
    MenuItemID CHAR(36),
    Quantity INT NOT NULL,
    CONSTRAINT FK_OrderItems_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID),
    CONSTRAINT FK_OrderItems_MenuItems FOREIGN KEY (MenuItemID) REFERENCES MenuItems(ID)
);

-- Reviews Table
CREATE TABLE Reviews (
     ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
     OrderID CHAR(36) NOT NULL, -- Added NOT NULL constraint as per the diagram
     Rating INT NOT NULL,
     Comment TEXT,
     CONSTRAINT FK_Reviews_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID)
);

-- Notifications Table
CREATE TABLE Notifications (
    ID CHAR(36) PRIMARY KEY DEFAULT (UUID()),
    OrderID CHAR(36),
    Message TEXT NOT NULL,
    Timestamp DATETIME DEFAULT NOW(),
    CONSTRAINT FK_Notifications_Orders FOREIGN KEY (OrderID) REFERENCES Orders(ID)
);

DROP TABLE Notifications;

