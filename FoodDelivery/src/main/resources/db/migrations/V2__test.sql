
CREATE TABLE test(
                     ID UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                     FirstName VARCHAR(100) NOT NULL,
                     LastName VARCHAR(100) NOT NULL,
                     Username VARCHAR(100) UNIQUE NOT NULL,
                     Email VARCHAR(150) UNIQUE NOT NULL,
                     Password TEXT NOT NULL,
                     PhoneNumber VARCHAR(15) NOT NULL,
                     Role VARCHAR(100) NOT NULL
);
