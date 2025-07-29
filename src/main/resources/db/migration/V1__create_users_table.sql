CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    f_name VARCHAR(100) NOT NULL,
    user_name VARCHAR(50) UNIQUE,
    user_email VARCHAR(100) UNIQUE NOT NULL,
    user_password VARCHAR(255),
    phone_no VARCHAR(13),
    role VARCHAR(50) NOT NULL,
    first_time_login BOOLEAN DEFAULT TRUE
);
