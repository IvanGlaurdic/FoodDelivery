-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users Table
CREATE TABLE Users (
   ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   FirstName VARCHAR(100) NOT NULL,
   LastName VARCHAR(100) NOT NULL,
   Username VARCHAR(100) UNIQUE NOT NULL,
   Email VARCHAR(150) UNIQUE NOT NULL,
   Password TEXT NOT NULL,
   PhoneNumber VARCHAR(15) NOT NULL,
   Role VARCHAR(100) NOT NULL
);

-- Restaurants Table
CREATE TABLE Restaurants (
     ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     Name VARCHAR(150) NOT NULL,
     ContactNumber VARCHAR(15) NOT NULL,
     Rating FLOAT DEFAULT 0,
     OwnerID UUID REFERENCES Users(ID)
);

-- Addresses Table
CREATE TABLE Addresses (
       ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       Street TEXT NOT NULL,
       City TEXT NOT NULL,
       Province TEXT NOT NULL,
       Country TEXT NOT NULL,
       PostalCode TEXT NOT NULL,
       Latitude FLOAT,
       Longitude FLOAT,
       UserID UUID REFERENCES Users(ID) ON DELETE CASCADE,
       RestaurantID UUID REFERENCES Restaurants(ID) ON DELETE CASCADE
);

-- MenuItems Table
CREATE TABLE MenuItems (
       ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
       RestaurantID UUID NOT NULL REFERENCES Restaurants(ID),
       Name VARCHAR(100) NOT NULL,
       Description TEXT,
       Price DECIMAL(10,2) NOT NULL,
       ImageURL TEXT,
       Category VARCHAR(100) NOT NULL
);

-- Orders Table
CREATE TABLE Orders (
    ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    UserID UUID REFERENCES Users(ID),
    RestaurantID UUID REFERENCES Restaurants(ID),
    OrderDate DATE NOT NULL,
    ScheduledDeliveryDate DATE,
    TotalAmount DECIMAL(10,2) NOT NULL,
    Status UUID NOT NULL
);

-- OrderItems Table
CREATE TABLE OrderItems (
        ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
        OrderID UUID REFERENCES Orders(ID),
        MenuItemID UUID REFERENCES MenuItems(ID),
        Quantity INT NOT NULL
);

-- Reviews Table
CREATE TABLE Reviews (
     ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
     OrderID UUID NOT NULL REFERENCES Orders(ID),
     Rating INT NOT NULL,
     Comment TEXT
);

-- Notifications Table
CREATE TABLE Notifications (
   ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
   OrderID UUID REFERENCES Orders(ID),
   Message TEXT NOT NULL,
   Timestamp TIMESTAMP DEFAULT NOW()
);
