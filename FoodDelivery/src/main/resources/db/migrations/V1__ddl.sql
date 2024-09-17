-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Addresses Table
-- Addresses Table
CREATE TABLE Address (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         street TEXT NOT NULL,
                         city TEXT NOT NULL,
                         province TEXT,
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
                        name VARCHAR(150) UNIQUE,
                        contact_number VARCHAR(15),
                        rating FLOAT DEFAULT 0,
                        picture_path VARCHAR(255),
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
                          restaurant_id UUID REFERENCES Restaurant(ID) ON DELETE CASCADE,
                          name VARCHAR(100),
                          description TEXT,
                          price DECIMAL(10,2),
                          image_url TEXT,
                          category VARCHAR(100)
);

-- Orders Table
CREATE TABLE "order" (
                         id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                         user_id UUID REFERENCES "user"(ID) ON DELETE CASCADE ,
                         restaurant_id UUID REFERENCES Restaurant(ID) ON DELETE CASCADE ,
                         order_date DATE,
                         scheduled_delivery_time TIMESTAMP WITHOUT TIME ZONE,
                         total_amount DECIMAL(10,2),
                         status text,
                         rating FLOAT DEFAULT 0,
                         comment TEXT
);

-- OrderItems Table
CREATE TABLE OrderItem (
                           id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                           order_id UUID REFERENCES "order"(ID) ON DELETE CASCADE,
                           menu_item_id UUID REFERENCES MenuItem(ID) ON DELETE CASCADE,
                           quantity INT
);


INSERT INTO Address (street, city, province, country, postal_code, latitude, longitude)
VALUES
/*McDonalds na Put Brodarice 6, Ul. Josipa Jovića 93, Vukovarska ul. 207, Poljička cesta 71*/
    ('Put Brodarice 6', 'Split', 'Split', 'Hrvatska', '21000',43.5201505,16.446659900450847 ),
    ('Ul. Josipa Jovića 93', 'Split', 'Split', 'Hrvatska', '21000',43.51911215,16.484022531173785 ),
    ('Vukovarska ul. 207', 'Split', 'Split', 'Hrvatska', '21000',43.5134862,16.502568236496238 ),
    ('Poljička cesta 71', 'Split', 'Split', 'Hrvatska', '21000',43.5109833,16.4802349 ),

/*Submarine Burgers na Morpurgova poljana 2', Ul. Josipa Jovića 93, Poljička cesta 39*/
    ('Poljička cesta 39', 'Split', 'Split', 'Hrvatska', '21000',43.508400699999996, 16.471765399999978 ),
    ('Morpurgova poljana 2', 'Split', 'Split', 'Hrvatska', '21000',43.5083309, 16.4375226 ),

/*Biberon Fine Food na Ul. Antuna Gustava Matoša 10*/
    ('Ul. Antuna Gustava Matoša 10', 'Solin', 'Solin', 'Hrvatska', '21210',43.5298854, 16.4903959),

/*Pizzeria Malo Misto na Vrlička ul. 59*/
    ('Vrlička ul. 59', 'Sinj', 'Sinj', 'Hrvatska', '21230',43.7055789, 16.638735),

/*Pizzeria Fantasia na Put Piketa 4*/
    ('Put Piketa 4', 'Sinj', 'Sinj', 'Hrvatska', '21230',43.6967401, 16.6497369),

/*Vlasnik McDonaldsa (Ante Antic) i Vlasnik Biberona (Mate Matic) na Ul. Domovinskog rata 61A*/
    ('Ul. Domovinskog rata 61A', 'Split', 'Split', 'Hrvatska', '21000',43.520332800000006,16.450891074474406),

/*Vlasnik Submarine Brugera (Stipe Stipic) i Korisnik (Toma Tomic) na Ul. don Frane Bulića 87*/
    ('Ul. don Frane Bulića 87', 'Solin', 'Solin', 'Hrvatska', '21210',43.54214035, 16.479618436238564),

/*Vlasnik Malog Mista i Fantasie (Ivan Ivic) na Vrlička ul. 59*/
    ('Vrlička ul. 59', 'Sinj', 'Sinj', 'Hrvatska', '21230',43.7045624, 16.639612),

/*Korsnici (Branko Brnas, Ana Anic, Lucija Lucic ) na Spinutska ul. 39 */
    ('Spinutska ul. 39', 'Split', 'Split', 'Hrvatska', '21000',43.514747850000006, 16.424176809622963),

/*Korsnici (Marta Martic, Bruno Galic(admin), Luka Lukic ) na Put Piketa 40 */
    ('Put Piketa 40', 'Sinj', 'Sinj', 'Hrvatska', '21230',43.6969577, 16.6557965);

INSERT INTO "user" (first_name, last_name, username, email, password, phone_number, role)
values
    ('Ante','Antic','ante.antic','ante.antic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','owner'),
    ('Mate','Matic','mate.matic','mate.matic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','owner'),
    ('Stipe','Stipic','stipe.stipic','stipe.stipic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','owner'),
    ('Ivan','Ivic','ivan.ivic','ivan.ivic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','owner'),
    ('Luka','Lukic','luka.lukic','luka.lukic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Toma','Tomic','toma.tomic','toma.tomic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Branko','Brnas','branko.brnas','branko.brnas@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Ana','Anic','ana.anic','ana.anic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Lucija','Lucic','lucija.lucic','lucija.lucic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Marta','Martic','marta.martic','marta.martic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','user'),
    ('Bruno','Galic','bruno.galic','bruno.galic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','admin'),
    ('Ivan','Glaurdic','Ivan','ivan.glaurdic@gmail.com','$2a$10$LzJGWFhAwt/C2TkX18lGieocdioTkYLyyg/pJzd8lTh7r7x/dji1u','123456789','admin');


UPDATE "user" u
SET address_id = a.id
FROM Address a
WHERE a.street = CASE
                     WHEN u.username = 'ante.antic' THEN 'Ul. Domovinskog rata 61A'
                     WHEN u.username = 'mate.matic' THEN 'Ul. Domovinskog rata 61A'
                     WHEN u.username = 'stipe.stipic' THEN 'Ul. don Frane Bulića 87'
                     WHEN u.username = 'ivan.ivic' THEN 'Vrlička ul. 59'
                     WHEN u.username = 'luka.lukic' THEN 'Put Piketa 40'
                     WHEN u.username = 'toma.tomic' THEN 'Ul. don Frane Bulića 87'
                     WHEN u.username = 'branko.brnas' THEN 'Spinutska ul. 39'
                     WHEN u.username = 'ana.anic' THEN 'Spinutska ul. 39'
                     WHEN u.username = 'lucija.lucic' THEN 'Spinutska ul. 39'
                     WHEN u.username = 'marta.martic' THEN 'Put Piketa 40'
                     WHEN u.username = 'bruno.galic' THEN 'Put Piketa 40'
                     WHEN u.username = 'Ivan' THEN 'Put Piketa 40'
                     ELSE NULL
    END;

INSERT INTO restaurant (name, contact_number, rating, picture_path)
VALUES
    ('McDonalds', 123456677, 0.0, 'images/restaurants/mcdonalds.jpg'),
    ('Submarine Burgers', 123456677, 0.0, 'images/restaurants/submarine.jpg'),
    ('Biberon Fine Food', 123456677, 0.0, 'images/restaurants/biberon.jpg'),
    ('Pizzeria Fantasia', 123456677, 0.0, 'images/restaurants/fantasia.jpg'),
    ('Pizzeria Malo Misto', 123456677, 0.0,'images/restaurants/malomisto.jpg');




UPDATE "restaurant" r
SET owner_id = u.id
FROM "user" u
WHERE u.username = CASE
                       WHEN r.name = 'McDonalds' THEN 'ante.antic'
                       WHEN r.name = 'Submarine Burgers' THEN 'mate.matic'
                       WHEN r.name = 'Biberon Fine Food' THEN 'stipe.stipic'
                       WHEN r.name = 'Pizzeria Fantasia' THEN 'ivan.ivic'
                       WHEN r.name = 'Pizzeria Malo Misto' THEN 'ivan.ivic'
                       ELSE NULL
    END;

-- Povezivanje McDonaldsa s njegovim adresama
INSERT INTO restaurant_addresses (restaurant_id, address_id)
SELECT r.id, a.id
FROM restaurant r, Address a
WHERE r.name = 'McDonalds' AND a.street IN ('Put Brodarice 6', 'Ul. Josipa Jovića 93', 'Vukovarska ul. 207', 'Poljička cesta 71');

-- Povezivanje Submarine Burgers s njegovim adresama
INSERT INTO restaurant_addresses (restaurant_id, address_id)
SELECT r.id, a.id
FROM restaurant r, Address a
WHERE r.name = 'Submarine Burgers' AND a.street IN ('Morpurgova poljana 2', 'Ul. Josipa Jovića 93', 'Poljička cesta 39');

-- Povezivanje Biberon Fine Food s njegovom adresom
INSERT INTO restaurant_addresses (restaurant_id, address_id)
SELECT r.id, a.id
FROM restaurant r, Address a
WHERE r.name = 'Biberon Fine Food' AND a.street = 'Ul. Antuna Gustava Matoša 10';

-- Povezivanje Pizzeria Malo Misto s njegovom adresom
INSERT INTO restaurant_addresses (restaurant_id, address_id)
SELECT r.id, a.id
FROM restaurant r, Address a
WHERE r.name = 'Pizzeria Malo Misto' AND a.street = 'Vrlička ul. 59';

-- Povezivanje Pizzeria Fantasia s njegovom adresom
INSERT INTO restaurant_addresses (restaurant_id, address_id)
SELECT r.id, a.id
FROM restaurant r, Address a
WHERE r.name = 'Pizzeria Fantasia' AND a.street = 'Put Piketa 4';


INSERT INTO menuitem (name, description, price, image_url, category)
VALUES
    ('Big Tasty Bacon', 'Burger with sliced 100% beef steak, a slice of Emmentaler cheese, crispy bacon, tomato, onion, fresh salad and Big Tasty sauce in a fine bun', 6.20, 'images/restaurants/orders/mcdonalds_order1.jpg','Burger'),
    ('Vegan burger', 'Soy fritter with fresh tomato, salad, onion and pickles and a combination of ketchup and mustard in a fine pastry sprinkled with sesame', 5.10, 'images/restaurants/orders/mcdonalds_order2.jpg','Vegan'),
    ('Chicken McNuggets 6 kom', 'Breaded chicken breast medallions, prepared in 100% vegetable oil from sunflower and canola. With 1 sauce of your choice.', 4.70, 'images/restaurants/orders/mcdonalds_order3.jpg','Chicken'),
    ('Garden salad', 'A fresh mix of kristal, romaine, frisee lettuce, endive, red radicchio, baby spinach, carrots, cherry tomatoes and cucumbers. With salad dressing of your choice.', 2.20, 'images/restaurants/orders/mcdonalds_order4.jpg','Salad'),
    ('Avocado Chicken Caprese', 'Fresh avocado and grilled chicken on mixed salad, cherry tomatoes and mozzarella with Pesto Genovese sauce', 5.60, 'images/restaurants/orders/submarine_order1.jpg','Salad'),
    ('French Burger Double', 'Oh la la, what a burrrger: 100% natural beef burger from local farming, fresh rocket, brie cheese and gourmet Istrian white truffle sauce', 11.40, 'images/restaurants/orders/submarine_order2.jpg','Burger'),
    ('Galina', '400g mixed meat, 2 chevaps, 1 sousage, 1 chicken fille, 1 pork kotlet', 11.40, 'images/restaurants/orders/biberon_order1.jpg','Grill'),
    ('Vegetarian sandwich in pastry', 'Pastries, vegetables, side dishes of your choice', 3.50, 'images/restaurants/orders/biberon_order2.jpg','Sandwich'),
    ('Prosciutto cheese sandwich in a bun', 'Pastries, cheese, prosciutto, side dishes of your choice', 2.90, 'images/restaurants/orders/biberon_order3.jpg','Sandwich'),
    ('Splićo menu', '4 pieces Dragon roll, 4 pieces Prawn roll, 4 pieces Rainbow roll, 4 pieces Mediterranean roll, 6 pieces Agemaky fantasy', 34.30, 'images/restaurants/orders/biberon_order4.jpg','Sushi'),
    ('Pizza Plodovi mora', 'Pizza Seafood', 8.80, 'images/restaurants/orders/malomisto_order1.jpg','Pizza'),
    ('Pizza Seljačka', 'Pizza Seljačka', 8.50, 'images/restaurants/orders/malomisto_order2.jpg','Pizza'),
    ('4 cheese pizza', 'Pelati, 4 types of cheese', 10.00, 'images/restaurants/orders/malomisto_order3.jpg','Pizza'),
    ('Pizza Gourmet', 'Tomato, cheese, ham, mushrooms, pancetta, sweet pepperoni, cream, oregano', 10.90, 'images/restaurants/orders/fantasia_order1.jpg','Pizza'),
    ('Pizza s pršutom', 'Tomato, cheese, prosciutto, oregano', 10.90, 'images/restaurants/orders/fantasia_order2.jpg','Pizza'),
    ('Pizza Calzona', 'Pizza Calzona', 8.20, 'images/restaurants/orders/fantasia_order3.jpg','Pizza');


UPDATE "menuitem" m
SET restaurant_id = r.id
FROM "restaurant" r
WHERE r.name = CASE
                   WHEN m.name = 'Big Tasty Bacon' THEN 'McDonalds'
                   WHEN m.name = 'Vegan burger' THEN 'McDonalds'
                   WHEN m.name = 'Chicken McNuggets 6 kom' THEN 'McDonalds'
                   WHEN m.name = 'Garden salad' THEN 'McDonalds'
                   WHEN m.name = 'Avocado Chicken Caprese' THEN 'Submarine Burgers'
                   WHEN m.name = 'French Burger Double' THEN 'Submarine Burgers'
                   WHEN m.name = 'Galina' THEN 'Biberon Fine Food'
                   WHEN m.name = 'Vegetarian sandwich in pastry' THEN 'Biberon Fine Food'
                   WHEN m.name = 'Prosciutto cheese sandwich in a bun' THEN 'Biberon Fine Food'
                   WHEN m.name = 'Splićo menu' THEN 'Biberon Fine Food'
                   WHEN m.name = 'Pizza Plodovi mora' THEN 'Pizzeria Malo Misto'
                   WHEN m.name = 'Pizza Seljačka' THEN 'Pizzeria Malo Misto'
                   WHEN m.name = '4 cheese pizza' THEN 'Pizzeria Malo Misto'
                   WHEN m.name = 'Pizza Gourmet' THEN 'Pizzeria Fantasia'
                   WHEN m.name = 'Pizza s pršutom' THEN 'Pizzeria Fantasia'
                   WHEN m.name = 'Pizza Calzona' THEN 'Pizzeria Fantasia'
                   ELSE NULL
    END;


