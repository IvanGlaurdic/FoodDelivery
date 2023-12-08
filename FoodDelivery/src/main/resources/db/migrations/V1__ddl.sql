-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users Table
CREATE TABLE "user" (
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
CREATE TABLE Restaurant (
                            ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            Name VARCHAR(150) NOT NULL,
                            ContactNumber VARCHAR(15) NOT NULL,
                            Rating FLOAT DEFAULT 0,
                            OwnerID UUID REFERENCES "user"(ID)
);

-- Addresses Table
CREATE TABLE Address(
                        ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        Street TEXT NOT NULL,
                        City TEXT NOT NULL,
                        Province TEXT NOT NULL,
                        Country TEXT NOT NULL,
                        PostalCode TEXT NOT NULL,
                        Latitude FLOAT,
                        Longitude FLOAT,
                        UserID UUID REFERENCES "user"(ID) ON DELETE CASCADE,
                        RestaurantID UUID REFERENCES Restaurant(ID) ON DELETE CASCADE
);

-- MenuItems Table
CREATE TABLE MenuItem (
                          ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          RestaurantID UUID NOT NULL REFERENCES Restaurant(ID),
                          Name VARCHAR(100) NOT NULL,
                          Description TEXT,
                          Price DECIMAL(10,2) NOT NULL,
                          ImageURL TEXT,
                          Category VARCHAR(100) NOT NULL
);

-- Orders Table
CREATE TABLE "order" (
                         ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         UserID UUID REFERENCES "user"(ID),
                         RestaurantID UUID REFERENCES Restaurant(ID),
                         OrderDate DATE NOT NULL,
                         ScheduledDeliveryDate DATE,
                         TotalAmount DECIMAL(10,2) NOT NULL,
                         Status UUID NOT NULL
);

-- OrderItems Table
CREATE TABLE OrderItem (
                           ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           OrderID UUID REFERENCES "order"(ID),
                           MenuItemID UUID REFERENCES MenuItem(ID),
                           Quantity INT NOT NULL
);

-- Reviews Table
CREATE TABLE Review (
                        ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        OrderID UUID NOT NULL REFERENCES "order"(ID),
                        Rating INT NOT NULL,
                        Comment TEXT
);

-- Notifications Table
CREATE TABLE Notification (
                              ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              OrderID UUID REFERENCES "order"(ID),
                              Message TEXT NOT NULL,
                              Timestamp DATE
);
