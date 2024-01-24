-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Addresses Table
-- Addresses Table
CREATE TABLE Address (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         street TEXT NOT NULL,
                         city TEXT NOT NULL,
                         province TEXT NOT NULL,
                         country TEXT NOT NULL,
                         postal_code TEXT NOT NULL,
                         latitude FLOAT,
                         longitude FLOAT
);

-- Users Table
CREATE TABLE "user" (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        first_name VARCHAR(100) NOT NULL,
                        last_name VARCHAR(100) NOT NULL,
                        username VARCHAR(100) UNIQUE NOT NULL,
                        email VARCHAR(150) UNIQUE NOT NULL,
                        password TEXT NOT NULL,
                        phone_number VARCHAR(15),
                        role VARCHAR(100) NOT NULL,
                        address_id UUID REFERENCES Address(id) ON DELETE CASCADE
);

-- Restaurants Table
CREATE TABLE Restaurant (
                            id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                            name VARCHAR(150) UNIQUE NOT NULL,
                            contact_number VARCHAR(15) NOT NULL,
                            rating FLOAT DEFAULT 0,
                            owner_id UUID REFERENCES "user"(id) ON DELETE CASCADE
);

-- Restaurant Addresses Junction Table
CREATE TABLE restaurant_addresses (
                                      restaurant_id UUID REFERENCES Restaurant(id) ON DELETE CASCADE,
                                      address_id UUID REFERENCES Address(id),
                                      PRIMARY KEY (restaurant_id, address_id)
);
-- MenuItems Table
CREATE TABLE MenuItem (
                          id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                          restaurant_id UUID NOT NULL REFERENCES Restaurant(ID) ON DELETE CASCADE,
                          name VARCHAR(100) NOT NULL,
                          description TEXT,
                          price DECIMAL(10,2) NOT NULL,
                          image_url TEXT,
                          category VARCHAR(100) NOT NULL
);

-- Orders Table
CREATE TABLE "order" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         user_id UUID REFERENCES "user"(ID) ON DELETE CASCADE ,
                         restaurant_id UUID REFERENCES Restaurant(ID) ON DELETE CASCADE ,
                         order_date DATE NOT NULL,
                         scheduled_delivery_date DATE,
                         total_amount DECIMAL(10,2) NOT NULL,
                         status UUID NOT NULL
);

-- OrderItems Table
CREATE TABLE OrderItem (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           order_id UUID REFERENCES "order"(ID) ON DELETE CASCADE,
                           menu_item_id UUID REFERENCES MenuItem(ID) ON DELETE CASCADE,
                           quantity INT NOT NULL
);

-- Reviews Table
CREATE TABLE Review (
                        id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                        order_id UUID NOT NULL REFERENCES "order"(ID)  ON DELETE CASCADE,
                        rating INT NOT NULL,
                        comment TEXT
);

-- Notifications Table
CREATE TABLE Notification (
                              id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                              order_id UUID REFERENCES "order"(ID)  ON DELETE CASCADE,
                              message TEXT NOT NULL,
                              timestamp DATE
);
